package com.example.kittenappscollage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kittenappscollage.collect.fragment.FragmentGallery;
import com.example.kittenappscollage.collect.fragment.FragmentGalleryAction;
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
import com.example.kittenappscollage.helpers.SaveImageToFile;
import com.example.kittenappscollage.mainTabs.SelectSweepViewPager;
import com.example.kittenappscollage.mainTabs.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class MainActivity extends AppCompatActivity implements DialogLoadOldProject.ResultQuery, SaveImageToFile.ActionSave {

    private ApplyDrawToolsFragmentDraw mFragDraw;

    private FragmentGalleryAction mFragGal;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.setMain(this);
        SaveImageToFile.setReport(this);
        setupViewPager((SelectSweepViewPager) findViewById(R.id.select_sweep_viewpager));
        requestOldProj();
    }

    @Override
    public void saved(boolean saved) {
    }

    @Override
    public void saved(boolean saved, String path) {
        if(saved){
            File file = new File(path);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            /*вместо пересканирования добавляем алрес изображения в массив
            * отсканированных данных*/
            mFragGal.setSavingCollage(path);
        }
    }

    @Override
    public void onBackPressed() {
        if(!mFragGal.onBackPressed(mTabLayout.getSelectedTabPosition())){
            super.onBackPressed();
        }
    }

    private void setupViewPager(SelectSweepViewPager v){
        v.setAdapter(addFragments());
        addTabs(v);
    }

    private ViewPageAdapter addFragments(){
        mFragDraw = new ApplyDrawToolsFragmentDraw();
        mFragGal = new FragmentGalleryAction();
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
