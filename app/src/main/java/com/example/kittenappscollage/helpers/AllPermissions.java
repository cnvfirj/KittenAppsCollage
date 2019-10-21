package com.example.kittenappscollage.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;


public class AllPermissions {

    private static AllPermissions single;

    public final static int APP_PERM = 7;

    public final static int STORAGE = 15;
    public final static int CAMERA = 14;
    public final static int ACCOUNT = 13;


    private boolean storage = false;

    private boolean camera = false;

    private boolean account = false;

    private Activity activity;

    private Fragment f;

    public AllPermissions() {

    }


    public static AllPermissions create(){
        if(single==null){
            synchronized (AllPermissions.class){
                single = new AllPermissions();
            }
        }
        return single;
    }

    public AllPermissions activity(Activity activity){
        this.activity = activity;
        return this;
    }

    /*проверяем разрешение*/
    public AllPermissions reqSingle(int perm){
        if(activity==null){
            Massages.ERROR("В класс AllPermissions не добален активити",getClass());
            return this;
        }
        switch (perm){
            case STORAGE:
                storage();
                break;
            case CAMERA:
                camera();
                break;
            case ACCOUNT:
                account();
                break;
        }
        return this;
    }


    /*запускаем запрос на разрешение*/
    public void callDialog(int perm){
        if(activity==null){
            Massages.ERROR("В класс AllPermissions не добален активити",getClass());
            return ;
        }
        switch (perm){
            case STORAGE:
                callDialogStorage();
                break;
            case CAMERA:
                callDialogCamera();
                break;
            case ACCOUNT:
                callDialogAccount();
                break;
        }
    }

    /*по результату запроса на разрешения действуем*/
    public void setPerm(boolean req, int perm) {
        if (activity == null) {
            Massages.ERROR("В класс AllPermissions не добален активити",getClass());
            return;
        }
        switch (perm) {
            case STORAGE:
                setStorage(req);
                if(!req) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        /*если отклонили разрешение вызываем диалог с объяснением
                         * для чего нужно оно*/
                        dialogRationale(STORAGE).show();
                    } else {
                        /*если отклонили разрешение и поставили флажок
                         * "не показывать больше" вызываем диалог с возможностью
                         * перейти в настройки и дать разрешение в ручную*/
                        dialogProperties().show();
                    }
                }
                break;

            case CAMERA:
                setCamera(req);
                if(!req) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.CAMERA)) {
                        dialogRationale(CAMERA).show();

                    } else {

                        dialogProperties().show();
                    }
                }
                break;
            case ACCOUNT:
                setAccount(req);
                if(!req) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.GET_ACCOUNTS)) {
                        dialogRationale(ACCOUNT).show();
                    } else {

                        dialogProperties().show();
                    }
                }
                break;
        }

    }

    public void setStorage(boolean storage) {
        this.storage = storage;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public boolean isStorage() {
        return storage;
    }

    public boolean isCamera() {
        return camera;
    }

    public boolean isAccount() {
        return account;
    }

    /*запускаем намерение в настройки приложения, где можно дать нужные расрешения*/
    private void startPropertiesApp(){

        Toast.makeText(activity,createToastResolution(), Toast.LENGTH_LONG).show();
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(appSettingsIntent,APP_PERM);
    }

    /*создаем диалог с пояснением для чего это разрешение*/
    private AlertDialog dialogRationale(final int perm){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(createTitle(perm))
                .setMessage(createMassage(perm))
                .setPositiveButton(activity.getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callDialog(perm);
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return dialog.create();
    }



    /*создаем диалог с возможностью попасть в настройки приложения*/
    private AlertDialog dialogProperties(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getResources().getString(R.string.NOT_PERMISSION))
                .setMessage(activity.getResources().getString(R.string.GO_TO_SETTINGS))
                .setPositiveButton(activity.getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startPropertiesApp();
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });


        return dialog.create();
    }


    private String createToastResolution(){
        return activity.getResources().getString(R.string.TOAST_RESOLUTION);
    }
    private String createMassage(int perm){
        switch (perm){
            case STORAGE:
                return activity.getResources().getString(R.string.RATIONALE_ASSES_WRITE)+
                        ". "+
                        activity.getResources().getString(R.string.REPEAT_REQUEST);
            case CAMERA:
                return activity.getResources().getString(R.string.RATIONALE_ASSES_CAMERA)+
                        ". "+
                        activity.getResources().getString(R.string.REPEAT_REQUEST);
            case ACCOUNT:
                return activity.getResources().getString(R.string.RATIONALE_ASSES_ACCAUNT)+
                        ". "+
                        activity.getResources().getString(R.string.REPEAT_REQUEST);
            default:
                return "null";
        }
    }

    private String createTitle(int perm){
        switch (perm){
            case STORAGE:
                return activity.getResources().getString(R.string.ASSES_TO_DATA);
            case CAMERA:
                return activity.getResources().getString(R.string.ASSES_TO_CAMERA);
                default:
                    return "null";
        }

    }


    private void storage(){
        storage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void camera(){
        camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void account(){
        account = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void callDialogStorage(){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE);
    }

    private void callDialogCamera(){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CAMERA},CAMERA);
    }

    private void callDialogAccount(){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.GET_ACCOUNTS},ACCOUNT);
    }




}
