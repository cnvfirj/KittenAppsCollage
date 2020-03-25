package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.otaliastudios.cameraview.Facing;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


public class DecodeCamera {

    private int dRotate = 0;
    private Facing dFacing = Facing.BACK;


    public static DecodeCamera build(){
        return new DecodeCamera();
    }


    @SuppressLint("CheckResult")
    public void decodeBitmap(final byte[] img, final LoadProjectListener callback) {
        decode(img).subscribe(bitmap -> callback.loadImage(bitmap));

    }

    @SuppressLint("CheckResult")
    public void decodeBitmap(CameraProperties properties, final LoadProjectListener callback){
        String facing = properties.getFacing();
        if(facing.equals(Facing.FRONT.name()))dFacing = Facing.FRONT;
        if(facing.equals(Facing.BACK.name()))dFacing = Facing.BACK;

//        dFacing = properties.getFacing();
        dRotate = 360-properties.getRotate();
        decode(properties.getImg()).subscribe(bitmap -> callback.loadImage(bitmap));
    }

    public DecodeCamera addFacing(Facing facing){
        dFacing = facing;
        return this;
    }

    public DecodeCamera addRotate(int rotate){
        dRotate = rotate;
        return this;
    }



    private Observable<Bitmap> decode(byte[]img){
        return Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            emitter.onNext(decodeBitmap(img));
            emitter.onComplete();
        }).compose(new ThreadTransformers.Processor<>());
    }


    private Bitmap decodeBitmap(byte[] source) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(source, 0, source.length);
        if(dFacing.equals(Facing.BACK)&&dRotate==0) return bitmap;

        Matrix matrix = new Matrix();


        if(dFacing.equals(Facing.FRONT)) {
            matrix.postScale(-1, 1) ;
        }
        if (dRotate != 0 ||dRotate!=360) {
            matrix.postRotate(dRotate);
        }

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }


    public static class CameraProperties implements Serializable {
//        private Facing facing = Facing.BACK;
        private String camera;
        private int rotate = 0;
        private byte[]img;

        public CameraProperties(@NonNull byte[] img, @Nullable String facing, @Nullable int rotate) {
//            this.facing = facing;
            camera = facing;
            this.rotate = rotate;
            this.img = img;
        }

        public String getFacing(){
            return camera;
        }

        public int getRotate() {
            return rotate;
        }

        public byte[] getImg() {
            return img;
        }
    }

}
