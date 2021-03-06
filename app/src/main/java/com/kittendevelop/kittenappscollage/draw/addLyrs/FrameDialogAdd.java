package com.kittendevelop.kittenappscollage.draw.addLyrs;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.draw.addLyrs.loadImage.DecodeCamera;

import static com.kittendevelop.kittenappscollage.helpers.Massages.LYTE;

public class FrameDialogAdd extends DialogFragment implements SelectorFrameFragments{

    public static final int ADD_CAM = 1;
    public static final int ADD_NEW = 2;
    public static final int ADD_NET = 3;
    public static final int ADD_COLL = 4;

    public static final String KEY_SOURCE_ADD = "key source";

    private FragmentManager dManager;

    private AddLyr dFragmentAdd;

    private Fragment dSelectedFragment;

    private int sel;

    public static FrameDialogAdd instance(int ind){
        FrameDialogAdd dialog = new FrameDialogAdd();
        Bundle b = new Bundle();
        b.putInt(KEY_SOURCE_ADD,ind);
        dialog.setArguments(b);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle!=null){
            sel = bundle.getInt(KEY_SOURCE_ADD,ADD_NEW);
            dSelectedFragment = select(sel);
        }
        dFragmentAdd = new AddLyr();
        dManager = getChildFragmentManager();
        return inflater.inflate(R.layout.dialog_add_frame,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dManager.beginTransaction().add(R.id.dialog_add_frame,dSelectedFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.9), (int)(rect.bottom*0.7));
        window.setGravity(Gravity.CENTER);

    }

    private Fragment select(int index){
        if(index==ADD_CAM)return new AddLyrInCam();
        else if(index==ADD_COLL)return new GalleryFragment();
        else if(index==ADD_NEW)return new AddLyrInCreator();
        return null;
    }

    @Override
    public void backInAddLyr(View v, Object way) {
        Bundle bundle = null;
        if(v!=null) {
            switch (v.getId()) {
                case R.id.creator_lyr_done:
                    bundle = new Bundle();
                    bundle.putSerializable(AddLyr.KEY_EXTRACTOR_WAY, (String) way);
                    bundle.putInt(AddLyr.KEY_SOURCE, R.dimen.PATH_NEW);
                    break;
                case R.id.cam_click:
                    bundle = new Bundle();
                    bundle.putSerializable(AddLyr.KEY_EXTRACTOR_WAY, (DecodeCamera.CameraProperties) way);
                    bundle.putInt(AddLyr.KEY_SOURCE, R.dimen.PATH_CAM);
                    break;
            }
        }else {
            bundle = new Bundle();
            bundle.putSerializable(AddLyr.KEY_EXTRACTOR_WAY, (String) way);
            bundle.putInt(AddLyr.KEY_SOURCE, R.dimen.PATH_FILE);
        }
        dFragmentAdd.setArguments(bundle);
        if(!dFragmentAdd.isAdded())dManager.beginTransaction().add(R.id.dialog_add_frame,dFragmentAdd).commit();

    }

    @Override
    public void backInSelectedLyr() {
//        dManager.beginTransaction().replace(R.id.dialog_add_frame,dSelectedFragment).commit();
        dManager.beginTransaction().remove(dFragmentAdd).commit();

    }

    @Override
    public void exitAll() {
        dFragmentAdd.clear();
        dManager.beginTransaction().remove(dSelectedFragment).commit();
        dManager.beginTransaction().remove(dFragmentAdd).commit();
        dismiss();
    }

    @Override
    public void doneLyr(Bitmap lyr) {
        /*здесь надо отправить полученный слой куда надо*/
        RepDraw.get().addLyr(lyr);
        dFragmentAdd.clear();
        dManager.beginTransaction().remove(dSelectedFragment).commit();
        dManager.beginTransaction().remove(dFragmentAdd).commit();
        dismiss();
    }

    @Override
    public void onDestroyView() {
        dFragmentAdd.clear();
        super.onDestroyView();
    }

}
