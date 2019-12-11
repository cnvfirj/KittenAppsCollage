package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;

import java.util.ArrayList;

public class HelperSershPoints {

        public static final int ZERO_VAL = -121;

        protected MutableBit.Command hCommand;

        protected DeformMat hMat;

        protected boolean hOunStart,hOunProcess;

        protected PointF hPoint,hStartSegment,hFinSegment, hOldPoint, hOldZeroVector;

        protected PointF hStartAnglePoint,hFinAnglePoint;

        protected boolean hCreateAngle;

        protected boolean hRightSegment;

        protected ArrayList<PointF> hZeroingPoints,hPointsCenter;

        protected ArrayList<PointF> hPointsCircle;

        protected ArrayList<Integer> hValuesAlpha;

        protected boolean[]hCheckPixel;

        protected int[]hAllPixels;

        protected int hWidth;

        protected int hHeight;

        protected Bitmap hBitmap;

        protected boolean hConvert;



        public HelperSershPoints() {
            init();
        }

        protected void init(){
            hZeroingPoints = new ArrayList<>();
            hValuesAlpha = new ArrayList<>();
            hPointsCenter = new ArrayList<>();
            hPointsCircle = new ArrayList<>();
//            hRadius = 20;
//            hAlpha = 0;
            hConvert = false;
        }


        public HelperSershPoints bitmap(Bitmap b, DeformMat mat){
            hBitmap = b;
            hMat = mat;
//            searchZeroCirc(hPointsCircle,hRadius);
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
//            searchZeroCirc(hPointsCircle,hRadius);
            searchZeroCirc(hPointsCircle,RepDraw.get().getWidth()/2.0f);

            return this;
        }

        public HelperSershPoints alpha(int alpha){
//            hAlpha = alpha;
            return this;
        }

        public HelperSershPoints command(MutableBit.Command command){
            hCommand = command;
            return this;
        }

        public HelperSershPoints radius(float radius){
//            hRadius = radius;
//            searchZeroCirc(hPointsCircle,hRadius);
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

//        protected Point getStartAnglePoint(){
//            return hStartAnglePoint;
//        }
        /*searchPointsCirc(points,zero,isRight(vector(start,fin),vector(hMat.getPointBitmap(hOldPoint),start)));*/
        private void searchPointsCirc(ArrayList<PointF> points, PointF zero, boolean right){
            Point s=null;
            Point f=null;
            /*в зависимости от поворота отрезка, выбираем
            * путь к которому заполним угол*/
            if(right){
                s = new Point((int)(-hOldZeroVector.x),(int)(hOldZeroVector.y));//левая точка
                f = new Point((int)(-zero.x),(int)(zero.y));//левая точка

            }else {
                s = new Point((int)(hOldZeroVector.x),(int)(-hOldZeroVector.y));//правая точка
                f = new Point((int)(zero.x),(int)(-zero.y));//правая точка
            }
                hCreateAngle = true;
                hRightSegment = right;
                hStartAnglePoint = new PointF((-s.x-f.x)/2,(-s.y-f.y)/2);
                hFinAnglePoint = new PointF((s.x+f.x)/2,(s.y+f.y)/2);
                addAllPoints(points,s,f,null);

        }


        private void searchZeroCirc(ArrayList<PointF> points, float radius){
            if(hMat==null)return;
            points.clear();
            ArrayList<PointF> p1 = new ArrayList<>();
            ArrayList<PointF> p2 = new ArrayList<>();
            ArrayList<PointF> p3 = new ArrayList<>();
            ArrayList<PointF> p4 = new ArrayList<>();

            int x = 0;
            int y =  Math.round(radius/hMat.getRepository().getScale())-1;
            int delta = 1 - 2 * (int) radius;
            int error = 0;
            while (y >= 0){
                p1.add(new PointF(x,y));
                p2.add(new PointF(x, - y));
                p3.add(new PointF( - x, y));
                p4.add(new PointF(- x, - y));
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

        protected void variableParams(PointF p,ArrayList<PointF>points, ArrayList<Integer>values,Point reperA, Point reperB){
           points.add(p);
        }


        /*находим точки между s и f*/
    private void addAllPoints(ArrayList<PointF> points, Point s, Point f, ArrayList<Integer> alpha){
        Point a = new Point(s.x,s.y);
        Point b = new Point(f.x,f.y);
        int deltaX = Math.abs(b.x - a.x);
        int deltaY = Math.abs(b.y - a.y);
        int signX = a.x < b.x ? 1 : -1;
        int signY = a.y < b.y ? 1 : -1;
        int error = deltaX - deltaY;
        variableParams(new PointF(b.x,b.y),points,alpha,s,f);

        while (a.x!=b.x||a.y!=b.y){
            variableParams(new PointF(a.x,a.y),points,alpha,s,f);

            int shift = 0;
            int error2 = error*2;
            if (error2 > -deltaY) {
                error -= deltaY;
                a.x += signX;
                shift++;
            }
            if (error2 < deltaX) {
                error += deltaX;
                a.y += signY;
                shift++;
            }
            if(shift==2){
                variableParams(new PointF(a.x,a.y-signY),points,alpha,s,f);

            }
        }
    }




        /*searchRect(hPointsCenter,mMat.getPointBitmap(hStartSegment),mMat.getPointBitmap(hFinSegment));*/
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
                for (PointF p:hPointsCenter){
                    addAllPoints(hZeroingPoints,new Point(0,0),extPoint(p),hValuesAlpha);
                }

                int alpha = RepDraw.get().getAlpha();

                for (int i=0;i<hZeroingPoints.size();i++){
                    if(hCommand.equals(MutableBit.Command.ELAST_2)||hCommand.equals(MutableBit.Command.ELAST_3)){
                        alpha = hValuesAlpha.get(i);
                    }
                    applyElastPixel(new int[]{
                            (int)(start.x+hZeroingPoints.get(i).x),
                            (int)(start.y+hZeroingPoints.get(i).y)
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
            addAllPoints(hZeroingPoints,new Point((int)-zero.x,(int)zero.y),new Point((int)zero.x,(int)-zero.y),hValuesAlpha);

            /*перебираем оба массива и находим каждую точку отрезка*/
            int alpha = RepDraw.get().getAlpha();
            for (PointF p:hPointsCenter){
                for (int i=0;i<hZeroingPoints.size();i++){
                    if(hCommand.equals(MutableBit.Command.ELAST_2)||hCommand.equals(MutableBit.Command.ELAST_3)){
                        alpha = hValuesAlpha.get(i);
                    }

                    applyElastPixel(new int[]{
                            (int)(p.x+hZeroingPoints.get(i).x),
                            (int)(p.y+hZeroingPoints.get(i).y)
                    },alpha);
                }
            }


            hOldZeroVector = new PointF(zero.x,zero.y);
        }



        private float ounCirc(PointF p, float r){
            return (p.x*p.x)/(r*r)+(p.y*p.y)/(r*r);
        }



        private PointF zeroingVector(PointF start, PointF fin){
            if(start.x==fin.x&&start.y==fin.y){
                return null;
            }
//            PointF vector = new PointF(fin.x- start.x, fin.y- start.y);
            PointF vector = vector(start,fin);
            float segment = widthVector(vector) ;
            PointF zeroing = new PointF(vector.x/segment,vector.y/segment);
//            float x = zeroing.x*hRadius/hMat.getRepository().getScale();
//            float y = zeroing.y*hRadius/hMat.getRepository().getScale();
            float x = zeroing.x*(RepDraw.get().getWidth()/2.0f)/hMat.getRepository().getScale();
            float y = zeroing.y*(RepDraw.get().getWidth()/2.0f)/hMat.getRepository().getScale();

            return new PointF(y,x);
        }

        private float widthVector(PointF vector){
            return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
        }

        private Point extPoint(PointF p){
            return new Point((int)p.x,(int)p.y);
        }

        private PointF vector(PointF one, PointF two){
            return new PointF(two.x-one.x,two.y-one.y);
        }

        private boolean isRight(PointF main, PointF vector){
            return (vector.x*main.y-vector.y*main.x)<0;
        }

}
