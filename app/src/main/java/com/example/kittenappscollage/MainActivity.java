package com.example.kittenappscollage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.kittenappscollage.collect.fragment.FragmentGallery;
import com.example.kittenappscollage.collect.fragment.FragmentGalleryReviewImages;
import com.example.kittenappscollage.draw.fragment.AddLyrsFragmentDraw;
import com.example.kittenappscollage.draw.fragment.ApplyDrawToolsFragmentDraw;
import com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.kittenappscollage.draw.saveSteps.ClearCatch;
import com.example.kittenappscollage.draw.saveSteps.State;
import com.example.kittenappscollage.draw.saveSteps.Steps;
import com.example.kittenappscollage.helpers.AllPermissions;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.mainTabs.SelectSweepViewPager;
import com.example.kittenappscollage.mainTabs.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static android.provider.MediaStore.VOLUME_EXTERNAL;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_PATH_IMG;

public class MainActivity extends AppCompatActivity implements DialogLoadOldProject.ResultQuery, SavedKollagesFragmentDraw.ActionSave, MainSwitching {

    private ApplyDrawToolsFragmentDraw mFragDraw;

    private FragmentGalleryReviewImages mFragGal;

    private TabLayout mTabLayout;

    private SelectSweepViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkDBPerms.get(getApplicationContext());
        App.setMain(this);
        viewPager = findViewById(R.id.select_sweep_viewpager);
        setupViewPager(viewPager);
        requestOldProj();
    }

    @Override
    public void request(boolean block) {
        mFragGal.setBlock(block);
    }

    @Override
    public void savedFile(boolean saved, String fold, String img, String name) {

    }

    @Override
    public void savedStorage(boolean saved, String report, String delimiter) {
        final long date = System.currentTimeMillis();
        if(saved){
            String[]split = report.split(delimiter);
                if (App.checkVersion()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATE_TAKEN, date);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.MediaColumns.DATA, split[INDEX_PATH_IMG]);
                    Uri uri = getContentResolver().insert(getUri(),values);
                    mFragGal.setSavingInStorageCollage(uri, report, delimiter, date);
                }
        }
    }

    @Override
    public void onBackPressed() {
        if(!mFragGal.onBackPressed(mTabLayout.getSelectedTabPosition())){
            super.onBackPressed();
        }
    }

    @Override
    public void stepToEdit() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void resultScan(HashMap<String, ArrayList<String>> list) {
        mFragDraw.setListCollect(list);
    }

    private Uri getUri(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else return MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL);
    }

    private void setupViewPager(SelectSweepViewPager v){
        v.setAdapter(addFragments());
        addTabs(v);
    }

    private ViewPageAdapter addFragments(){
        mFragDraw = new ApplyDrawToolsFragmentDraw();
        mFragGal = new FragmentGalleryReviewImages();


        mFragGal.setBlock(false);


        ViewPageAdapter a = new ViewPageAdapter(getSupportFragmentManager());
        a.addFragment(mFragDraw);
        a.addFragment(mFragGal);
        return a;
    }

    private void addTabs(final SelectSweepViewPager v){
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(v);
        mTabLayout.getTabAt(0).setIcon(R.drawable.icon_edit);
        mTabLayout.getTabAt(1).setIcon(R.drawable.icon_collect);
        if(mTabLayout.getSelectedTabPosition()==0)v.setSweep(false);
        else v.setSweep(true);

        v.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0) v.setSweep(false);
                if(position==1) {
                    v.setSweep(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== SavedKollagesFragmentDraw.REQUEST_SAVED){
            AllPermissions.create()
                    .activity(this)
                    .setPerm(grantResults[0]== PackageManager.PERMISSION_GRANTED,requestCode);
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)mFragDraw.reSave();
        }else if(requestCode== FragmentGallery.REQUEST_READ_STORAGE){
            AllPermissions.create()
                    .activity(this)
                    .setPerm(grantResults[0]== PackageManager.PERMISSION_GRANTED,requestCode);
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)mFragGal.scanDevice();
        }else if(requestCode== AddLyrsFragmentDraw.REQUEST_READ_COLLECT){
            AllPermissions.create()
                    .activity(this)
                    .setPerm(grantResults[0]== PackageManager.PERMISSION_GRANTED,requestCode);
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)mFragDraw.reVisCollect();
        }
    }

    @Override
    public void result(boolean r) {
         if(r){
             State state = DialogLoadOldProject.requestData(BackNextStep.get().getFoldData());
             if(state!=null){
                 BackNextStep.get()
                         .setOldState(state)
                         .load(Steps.TARGET_ALL,Steps.MUT_SCALAR,state);
             }
         }else {
             BackNextStep.get().remove();
         }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCatchImges();
    }

    private void clearCatchImges(){
        /* синглтоны обязательно очистить
         * так как при повторном запуске
         * программы возможны баги */
        RepDraw.get().clearRep();
        BackNextStep.get().clearStacks();
        Intent intent = new Intent(this, ClearCatch.class);
        intent.putExtra(ClearCatch.KEY_FOLD, BackNextStep.get().getFoldData());
        App.getMain().startService(intent);
    }

    private void requestOldProj(){
       State state = DialogLoadOldProject.requestData(BackNextStep.get().getFoldData());
       if(state!=null){
           DialogLoadOldProject d = new DialogLoadOldProject();
           d.show(getSupportFragmentManager(), DialogLoadOldProject.TAG);
       }else BackNextStep.get().remove();
    }

}
