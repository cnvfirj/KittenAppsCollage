package com.kittendevelop.kittenappscollage.draw.operations.bitmap;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Pair;
import android.util.Size;

import com.example.mutmatrix.DeformMat;
import com.example.mutmatrix.actions.Deform;

import java.util.Arrays;

/*класс для приведения перевернутого и искаженного битмап
* к обысному состоянию с учетом всех Matrix преобразований*/
public class CoercionBitmap {






    /*создаем чистый битмап размером в который можно вписать
    * искаженный без масштабирования(перевернытфй или искаженный)*/
    public static Bitmap blankBitmap(DeformMat mat){

        PointF[]realVer = verticesRealScale(verticesScaled(mat),mat.getRepository().getScale());
        return createBitmap(size(minMax(realVer)));
    }

//    public static Bitmap blank(DeformMat mat){
//        float[][]find = mat.getRepository().getLoc();
//        PointF[]realVerRotate = verticesRealScale(new float[][]{find[1],find[2],find[3],find[4]},mat.getRepository().getScale());
//        return createBitmap(size(minMax(realVerRotate)));
//    }

    public static Bitmap correctBlank(DeformMat mat){
        PointF[]p = mat.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
        return createBitmap(size(p,mat.getRepository().getScale()));
    }

    public static PointF correctTrans(DeformMat mat){
        PointF[]p = mat.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
        return minMax(p).first;
    }

    private static Size size(PointF[]p, float scale){
        Pair<PointF,PointF> minMax = minMax(p);
        float w = (minMax.second.x-minMax.first.x)/scale;
        float h = (minMax.second.y-minMax.first.y)/scale;
        return new Size((int)w,(int)h);
    }

//    public static DeformMat mat(DeformMat mat){
//        float[][]find = mat.getRepository().getLoc();
//        PointF[]realVerRotate = verticesRealScale(new float[][]{find[1],find[2],find[3],find[4]},mat.getRepository().getScale());
//        Pair<PointF, PointF>m = minMax(realVerRotate);
//        return null;
//    }






    /*получаем матрицу по которй нарисуем на пустом битмап
    * заданный с учетом искажений. */
    public static Matrix matrixBitmap(DeformMat mat){
        Matrix matrix = new Matrix();
        final float[]src = {0,0,mat.getRepository().getBitmap().x,0,mat.getRepository().getBitmap().x,mat.getRepository().getBitmap().y,0,mat.getRepository().getBitmap().y};
        matrix.setPolyToPoly(src,0,mat.getRepository().getDst(),0,4);
        matrix.postRotate(mat.getRepository().getRotate());

        PointF[]ver = verticesScaled(mat);
        PointF[]realVer = verticesRealScale(ver,mat.getRepository().getScale());

        Pair<PointF, PointF> minMaxRealVer = minMax(realVer);
        PointF[] real = shiftRealVer(minMaxRealVer.first,realVer);
        matrix.postTranslate(real[0].x,real[0].y);

        return matrix;
    }

    public static void drawBitmap(Canvas canvas, Matrix matrix, Bitmap bitmap){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(bitmap,matrix,p);
    }
    /*получаем сдвиг уже нового битмап*/
    public static PointF transBitmap(DeformMat mat){
        return minMax(verticesScaled(mat)).first;
    }

//    public static float scaleBitmap(DeformMat mat){
//        return mat.getRepository().getScale();
//    }
//
//    private static void zeroingBitmap(Bitmap b){
//        if(b!=null||!b.isRecycled()){
//            b.recycle();
//            b=null;
//        }
//    }




//    private static float[]src(Bitmap bitmap){
//        return new float[]{0,0,bitmap.getWidth(),0,bitmap.getWidth(),bitmap.getHeight(),0,bitmap.getHeight()};
//    }

    /*получаем координаты вершин с учетом масштаба,
    * сдвига, вращения и деформации*/
    private static PointF[]verticesScaled(DeformMat mat){
        return mat.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
    }

    /*вычисляем координаты вершин с масштабом 1:1
    * сдвиг здесь не тот что отображается*/
    private static PointF[] verticesRealScale(PointF[]ver, float scale){
        PointF[] p = new PointF[4];
        for(int i=0;i<p.length;i++){
            p[i] = new PointF(ver[i].x/scale,ver[i].y/scale);
        }

        return p;
    }
    private static PointF[] verticesRealScale(float[][]ver, float scale){
        PointF[] p = new PointF[4];
        for(int i=0;i<p.length;i++){
            p[i] = new PointF(ver[i][0]/scale,ver[i][1]/scale);
        }

        return p;
    }



    private static PointF[]shiftRealVer(PointF min, PointF[]ver){
        PointF[]p = new PointF[4];
        for (int i =0;i<ver.length;i++){
            p[i] = new PointF(ver[i].x-min.x,ver[i].y-min.y);
        }
        return p;
    }

//    private static Pair<PointF, PointF> minMax(PointF[]ver1, PointF[]ver2){
//        float[]arrX = new float[8];
//        float[]arrY = new float[8];
//        for (int i=0;i<4;i+=1){
//            arrX[i] = ver1[i].x;
//            arrY[i] = ver1[i].y;
//        }
//
//        for (int i=4;i<8;i++){
//            arrX[i] = ver2[i-4].x;
//            arrY[i] = ver2[i-4].y;
//        }
//        Arrays.sort(arrX);
//        Arrays.sort(arrY);
//
//        return new Pair<>(new PointF(arrX[0],arrY[0]),new PointF(arrX[7],arrY[7]));
//    }

    /*выяснием минимальные и мксимальные значения
    * x и y из всех вершин*/
    private static Pair<PointF, PointF> minMax(PointF[]ver){
        float[]arrX = new float[4];
        float[]arrY = new float[4];
        for (int i=0;i<ver.length;i++){
            arrX[i] = ver[i].x;
            arrY[i] = ver[i].y;
        }
        Arrays.sort(arrX);
        Arrays.sort(arrY);

        return new Pair<>(new PointF(arrX[0],arrY[0]),new PointF(arrX[3],arrY[3]));
    }

    /*вычисляем размер выходного Bitmap*/
    private static Size size(Pair<PointF, PointF> minMax){

        int width = (int)(minMax.second.x-minMax.first.x)>0?(int)(minMax.second.x-minMax.first.x):1;
        int height = (int)(minMax.second.y-minMax.first.y)>0?(int)(minMax.second.y-minMax.first.y):1;
        return new Size(width,height);
    }

    private static Bitmap createBitmap(Size size){
        return Bitmap.createBitmap(size.getWidth(),size.getHeight(), Bitmap.Config.ARGB_8888);
    }


}
