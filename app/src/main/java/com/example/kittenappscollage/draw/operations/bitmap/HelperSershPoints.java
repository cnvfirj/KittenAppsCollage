package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;
import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class HelperSershPoints {

    protected final int X = 0;

    protected final int Y = 1;

    private MutableBit.Command hCommand;

    private DeformMat hMat;

    private boolean hOunStart,hOunProcess;

    private PointF hPoint,hStartSegment,hFinSegment, hOldPoint, hOldZeroVector;

    private int[] hStartAnglePoint,hFinAnglePoint;

    private float hWidthStartToFinAngleSegment, hDiameterLine, hRadiusLine;

    private boolean hCreateAngle;

    private boolean hRightSegment;

    private ArrayList<int[]> hZeroingPoints,hPointsCenter;

    private ArrayList<int[]> hPointsCircle;

    private ArrayList<Integer> hValuesAlpha;

    private boolean[]hCheckPixel;

    private int[]hAllPixels;

    private int hWidth;

    private int hHeight;

    private Bitmap hBitmap;

    private boolean hConvert;

        public HelperSershPoints() {
            init();
        }

        protected void init(){
            hZeroingPoints = new ArrayList<>();
            hValuesAlpha = new ArrayList<>();
            hPointsCenter = new ArrayList<>();
            hPointsCircle = new ArrayList<>();
            hConvert = false;
        }

        public HelperSershPoints bitmap(Bitmap b, DeformMat mat){
            hBitmap = b;
            hMat = mat;
            searchZeroCirc(hPointsCircle,RepDraw.get().getWidth()/2.0f);

            return this;
        }

        public HelperSershPoints bitmap(Bitmap b){
            hBitmap = b;
            return this;
        }

        public HelperSershPoints convert(){
            hWidth = hBitmap.getWidth();
            hHeight = hBitmap.getHeight();
            final int length = hWidth*hHeight;
            hCheckPixel = new boolean[length];
            hAllPixels = new int[length];
            hBitmap.getPixels(hAllPixels,
                    0, hBitmap.getWidth(),
                    0, 0, hBitmap.getWidth() , hBitmap.getHeight() );
            hConvert = true;
            return this;
        }

        public HelperSershPoints resetOun(){
            hOunStart = false;
            hOunProcess = false;
            hOldPoint = null;
            hOldZeroVector = null;
            return this;
        }

        public HelperSershPoints mat(DeformMat mat){
            hMat = mat;
            searchZeroCirc(hPointsCircle,RepDraw.get().getWidth()/2.0f);

            return this;
        }

        public HelperSershPoints alpha(int alpha){

            return this;
        }

        public HelperSershPoints command(MutableBit.Command command){
            hCommand = command;
            return this;
        }

        public HelperSershPoints radius(float radius){

            return this;
        }

        public HelperSershPoints point(PointF point){
            hPoint = point;
            if(hOunProcess){
                hOldPoint = new PointF(hStartSegment.x,hStartSegment.y);
                hStartSegment = new PointF(hFinSegment.x,hFinSegment.y);
            }
            if(!hOunStart){
                hStartSegment = new PointF(point.x,point.y);
                hOunStart = true;
                hCreateAngle = false;
                hDiameterLine = RepDraw.get().getWidth()/ gethMat().getRepository().getScale();
                hRadiusLine = hDiameterLine/2.0f;
            }else {
                hFinSegment = new PointF(point.x,point.y);
                hOunProcess = true;
            }


            return this;
        }

        /*вызываем при завершении жеста*/
        public HelperSershPoints fin(){
            hOunStart = false;
            hOunProcess = false;
            hConvert = false;
            return this;
        }

        public HelperSershPoints apply(){
            if(hOunProcess){
                searchRect(hMat.getPointBitmap(hStartSegment),hMat.getPointBitmap(hFinSegment));
            }
            hBitmap.setPixels(hAllPixels,0, hBitmap.getWidth(), 0, 0,hBitmap.getWidth() , hBitmap.getHeight() );
            return this;
        }

        public HelperSershPoints clear(){
            hCheckPixel = new boolean[1];
            hAllPixels = new int[1];
            return this;
        }

        public boolean isConvert(){
            return hConvert;
        }

        public PointF getPoint() {
            return hPoint;
        }

        protected void applyElastPixel(int[]p, int value){

        }

        private void searchPointsCirc(ArrayList<int[]> points, PointF zero, boolean right){
            int[] s;
            int[] f;
            /*в зависимости от поворота отрезка, выбираем
            * путь к которому заполним угол*/
            if(right){
                s = new int[]{(int)(-hOldZeroVector.x),(int)(hOldZeroVector.y)};//левая точка
                f = new int[]{(int)(-zero.x),(int)(zero.y)};//левая точка
            }else {
                s = new int[]{(int)(hOldZeroVector.x),(int)(-hOldZeroVector.y)};//правая точка
                f = new int[]{(int)(zero.x),(int)(-zero.y)};//правая точка
            }
                hCreateAngle = true;
                hRightSegment = right;
                hStartAnglePoint = new int[]{(-s[X]-f[X])/2,(-s[Y]-f[Y])/2};
                hFinAnglePoint = new int[]{(s[X]+f[X])/2,(s[Y]+f[Y])/2};
                hWidthStartToFinAngleSegment = (widthVector(vector(hStartAnglePoint,hFinAnglePoint))+getDiameterLine())/2f;
//                hWidthStartToFinAngleSegment =  getDiameterLine();

                addAllPoints(points,s,f,null);
        }

        private void searchZeroCirc(ArrayList<int[]> points, float radius){
            if(hMat==null)return;
            points.clear();
            ArrayList<int[]> p1 = new ArrayList<>();
            ArrayList<int[]> p2 = new ArrayList<>();
            ArrayList<int[]> p3 = new ArrayList<>();
            ArrayList<int[]> p4 = new ArrayList<>();

            int x = 0;
            int y =  Math.round(radius/hMat.getRepository().getScale())-1;
            int delta = 1 - 2 * (int) radius;
            int error = 0;
            while (y >= 0){
                p1.add(new int[]{x,y});
                p2.add(new int[]{x, - y});
                p3.add(new int[]{ - x, y});
                p4.add(new int[]{- x, - y});

                error = 2 * (delta + y) - 1;
                if ((delta < 0) && (error <= 0)){
                    delta += 2 * ++x + 1;
                    continue;
                }
                if ((delta > 0) && (error > 0)){
                    delta -= 2 * --y + 1;
                    continue;
                }
                delta += 2 * (++x - y--);
            }
            points.addAll(p1);
            points.addAll(p2);
            points.addAll(p3);
            points.addAll(p4);
        }

        protected void variableParams(int[] p,ArrayList<int[]>points, ArrayList<Integer>values,int[] reperA, int[] reperB){
           points.add(p);
        }

        /*определяем значение которым будем изменять пиксель*/
        protected int selectValue(){
            return 0;
        }

        /*определяем условие для взятия значения из массива значений,
        * которым изменим пиксель*/
        protected boolean condition(){
            return false;
        }
        /*находим точки между s и f*/
        private void addAllPoints(ArrayList<int[]> points, int[] s, int[] f, ArrayList<Integer> alpha){
        int[] a = s.clone();
        int[] b = f.clone();
        int deltaX = Math.abs(b[X] - a[X]);
        int deltaY = Math.abs(b[Y] - a[Y]);
        int signX = a[X] < b[X] ? 1 : -1;
        int signY = a[Y] < b[Y] ? 1 : -1;
        int error = deltaX - deltaY;
            variableParams(new int[]{b[X],b[Y]},points,alpha,s,f);
        while (a[X]!=b[X]||a[Y]!=b[Y]){
            variableParams(new int[]{a[X],a[Y]},points,alpha,s,f);

            int shift = 0;
            int error2 = error*2;
            if (error2 > -deltaY) {
                error -= deltaY;
                a[X] += signX;
                shift++;
            }
            if (error2 < deltaX) {
                error += deltaX;
                a[Y] += signY;
                shift++;
            }
            if(shift==2){
                variableParams(new int[]{a[X],a[Y]-signY},points,alpha,s,f);

            }
        }
    }

        private void searchRect(PointF start, PointF fin){
            PointF zero = zeroingVector(start,fin);
            if(zero==null)return;
            if(hOldZeroVector!=null){
                /*Этим куском заполняем угол между эти и предыдйщим отрезками*/
                hPointsCenter.clear();
                searchPointsCirc(hPointsCenter,
                         zero,
                         isRight(vector(start,fin),vector(hMat.getPointBitmap(hOldPoint),start)));
                hZeroingPoints.clear();
                hValuesAlpha.clear();
                for (int[] p:hPointsCenter){
                        addAllPoints(hZeroingPoints,hStartAnglePoint,p,hValuesAlpha);
                }
                int alpha = selectValue();
                for (int i=0;i<hZeroingPoints.size();i++){

                    if(condition()){
                        alpha = hValuesAlpha.get(i);
                    }
                    applyElastPixel(new int[]{
                            (int)(start.x+hZeroingPoints.get(i)[X]),
                            (int)(start.y+hZeroingPoints.get(i)[Y])
                    },alpha);
                }
            }

            hCreateAngle = false;
            /*заполняем сам отрезок*/
            hPointsCenter.clear();
            /*находим все точки от старта к финишу*/
            addAllPoints(hPointsCenter,extPoint(start),extPoint(fin),null);

            hZeroingPoints.clear();
            hValuesAlpha.clear();
            /*находим все точки от левой до правой стороны*/
            addAllPoints(hZeroingPoints,new int[]{(int)-zero.x,(int)zero.y},new int[]{(int)zero.x,(int)-zero.y},hValuesAlpha);

            /*перебираем оба массива и находим каждую точку отрезка*/
            int alpha = selectValue();
            for (int[] p:hPointsCenter){
                for (int i=0;i<hZeroingPoints.size();i++){
                    if(condition()){
                        alpha = hValuesAlpha.get(i);
                    }
                    applyElastPixel(new int[]{
                             (p[X]+hZeroingPoints.get(i)[X]),
                             (p[Y]+hZeroingPoints.get(i)[Y]),
                    },alpha);

                }
            }
            hOldZeroVector = new PointF(zero.x,zero.y);
        }

        private PointF zeroingVector(PointF start, PointF fin){
            if(start.x==fin.x&&start.y==fin.y){
                return null;
            }
            PointF vector = vector(start,fin);
            float segment = widthVector(vector) ;
            PointF zeroing = new PointF(vector.x/segment,vector.y/segment);
            final float y = zeroing.x*getRadiusLine();
            final float x = zeroing.y*getRadiusLine();

            return new PointF(x,y);
        }

        private float widthVector(PointF vector){
            return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
        }

        private int[]extPoint(PointF p){
            return new int[]{(int)p.x,(int)p.y};
        }

        private PointF vector(PointF one, PointF two){
            return new PointF(two.x-one.x,two.y-one.y);
        }

        private PointF vector(int[]one,int[]two){
            return new PointF(two[X]-one[X],two[Y]-one[Y]);
        }
        private boolean isRight(PointF main, PointF vector){
            return (vector.x*main.y-vector.y*main.x)<0;
        }

        protected DeformMat gethMat(){
            return hMat;
        }

        protected MutableBit.Command getCommand(){
            return hCommand;
        }

        protected int gethWidth(){
            return hWidth;
        }

        protected int gethHeight(){
            return hHeight;
        }

        protected float getWidthStartToFinAngleSegment(){
            return hWidthStartToFinAngleSegment;
        }

        protected float getDiameterLine(){
            return hDiameterLine;
        }

        protected float getRadiusLine(){
            return hRadiusLine;
        }

        protected boolean isCreateAngle(){
            return hCreateAngle;
        }

        protected boolean isRightSegment(){
            return hRightSegment;
        }

        protected int[] getStartAnglePoint(){
            return hStartAnglePoint;
        }

        protected int[] gethFinAnglePoint(){
            return hFinAnglePoint;
        }


        protected boolean[]getCheckeds(){
            return hCheckPixel;
        }

        protected int[]getPixels(){
            return hAllPixels;
        }

}
