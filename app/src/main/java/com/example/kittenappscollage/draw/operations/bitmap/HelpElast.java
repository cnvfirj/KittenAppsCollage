package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;

import java.util.ArrayList;

public class HelpElast {

        public static final int ZERO_VAL = -121;

        protected MutableBit.Command hCommand;

        protected DeformMat hMat;

        protected boolean hOunStart,hOunProcess;

        protected PointF hPoint,hStartSegment,hFinSegment, hOldPoint, hOldLeft,hOldRight, hOldZeroVector;

        protected ArrayList<PointF> hZeroingPoints,hPointsCenter;

        protected ArrayList<PointF> hPointsCircle;


        protected ArrayList<Integer> hValuesAlpha;

        protected boolean[]hCheckPixel;

        protected int[]hAllPixels;

        protected int hWidth;

        protected int hHeight;

        protected Bitmap hBitmap;

        private boolean hConvert;



        public HelpElast() {
            init();
        }

        private void init(){
            hZeroingPoints = new ArrayList<>();
            hValuesAlpha = new ArrayList<>();
            hPointsCenter = new ArrayList<>();
            hPointsCircle = new ArrayList<>();
//            hRadius = 20;
//            hAlpha = 0;
            hConvert = false;
        }


        public HelpElast bitmap(Bitmap b, DeformMat mat){
            hBitmap = b;
            hMat = mat;
//            searchZeroCirc(hPointsCircle,hRadius);
            searchZeroCirc(hPointsCircle,RepDraw.get().getWidth()/2.0f);

            return this;
        }

        public HelpElast bitmap(Bitmap b){
            hBitmap = b;
            return this;
        }


        public HelpElast convert(){
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


        public HelpElast resetOun(){
            hOunStart = false;
            hOunProcess = false;
            hOldPoint = null;
            hOldZeroVector = null;
            return this;
        }

        public HelpElast mat(DeformMat mat){
            hMat = mat;
//            searchZeroCirc(hPointsCircle,hRadius);
            searchZeroCirc(hPointsCircle,RepDraw.get().getWidth()/2.0f);

            return this;
        }

        public HelpElast alpha(int alpha){
//            hAlpha = alpha;
            return this;
        }

        public HelpElast command(MutableBit.Command command){
            hCommand = command;
            return this;
        }

        public HelpElast radius(float radius){
//            hRadius = radius;
//            searchZeroCirc(hPointsCircle,hRadius);
            return this;
        }



        public HelpElast point(PointF point){
            hPoint = point;
            if(hOunProcess){
                hOldPoint = new PointF(hStartSegment.x,hStartSegment.y);
                hStartSegment = new PointF(hFinSegment.x,hFinSegment.y);
            }
            if(!hOunStart){
                hStartSegment = new PointF(point.x,point.y);
                hOunStart = true;
            }else {
                hFinSegment = new PointF(point.x,point.y);
                hOunProcess = true;
            }
            return this;
        }

        public HelpElast fin(){
            hOunStart = false;
            hOunProcess = false;
            hConvert = false;
            return this;
        }
        public HelpElast apply(){
            if(hCommand.equals(MutableBit.Command.ELAST_1)||hCommand.equals(MutableBit.Command.ELAST_2)||
                    hCommand.equals(MutableBit.Command.ELAST_3)){
                elast();
            }else if (hCommand.equals(MutableBit.Command.ELAST_4)){
                elastSpot();
            }
            return this;
        }

        public HelpElast clear(){
            hCheckPixel = new boolean[1];
            hAllPixels = new int[1];
            return this;
        }

        public boolean isConvert(){
            return hConvert;
        }

        public PointF getPoint() {
            createPoint();
            return hPoint;
        }

//        public float getRadius(){
//            return hRadius;
//        }

        private void createPoint(){
//            if(hPoint==null)hPoint = new PointF(mView.x/2,mView.y/2);
        }



        private void setPixel(int []p, int alpha){
            final int index = p[1]*hWidth+p[0];
            if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
                if(!hCheckPixel[index])implementElast1(index,alpha);
            }
        }


        private void setGradPixel(int []p,int alpha){
            final int index = p[1]*hWidth+p[0];
            if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
                implementElast2(index,alpha);
            }
        }

        private void setSideGradPixel(int []p,int alpha){
            final int index = p[1]*hWidth+p[0];
            if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
                implementElast3(index,alpha);
            }
        }

        protected void implementElast1(int index,int alpha){
            hAllPixels[index] = alphaColor(hAllPixels[index], alpha);
            hCheckPixel[index]=true;
        }

        protected void implementElast2(int index,int alpha){
            hAllPixels[index] = alphaColor(hAllPixels[index], alpha);
        }

        protected void implementElast3(int index,int alpha){

        }




        protected int alphaColor(int color,int alpha){
            if(Color.alpha(color)<alpha)return color;
            else return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        }

        private void applyElastPixel(int[]p, int alpha){
            if(hCommand.equals(MutableBit.Command.ELAST_1))setPixel(p, alpha);
            else if(hCommand.equals(MutableBit.Command.ELAST_2))setGradPixel(p, alpha);
            else if(hCommand.equals(MutableBit.Command.ELAST_3))setSideGradPixel(p, alpha);
        }

        private int computeAlpha(float power){
//            int segm = 255 - hAlpha;
//            return (int)(segm*power)+hAlpha;
            int segm = 255 - RepDraw.get().getAlpha();
            return (int)(segm*power)+RepDraw.get().getAlpha();

        }

        private void elastSpot(){

        }

        private void elast(){
            if(hOunProcess){
                searchRect(hPointsCenter,hMat.getPointBitmap(hStartSegment),hMat.getPointBitmap(hFinSegment));
            }
            hBitmap.setPixels(hAllPixels,0, hBitmap.getWidth(), 0, 0,hBitmap.getWidth() , hBitmap.getHeight() );
        }
        /*searchPointsCirc(points,zero,isRight(vector(start,fin),vector(hMat.getPointBitmap(hOldPoint),start)));*/
        private void searchPointsCirc(ArrayList<PointF> points, PointF zero, boolean right){
            Point o=null;
            Point n=null;
            if(right){
                o = new Point((int)(-hOldZeroVector.x),(int)(hOldZeroVector.y));//левая точка
                n = new Point((int)(-zero.x),(int)(zero.y));//левая точка

            }else {
                o = new Point((int)(hOldZeroVector.x),(int)(-hOldZeroVector.y));//правая точка
                n = new Point((int)(zero.x),(int)(-zero.y));//правая точка
            }
            if(o!=null&&n!=null){
                addAllPoints(points,o,n,null);
            }
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

        private void addAllPoints(ArrayList<PointF> points, Point a, Point b, ArrayList<Integer> alpha){
            int deltaX = Math.abs(b.x - a.x);
            int deltaY = Math.abs(b.y - a.y);
            int signX = a.x < b.x ? 1 : -1;
            int signY = a.y < b.y ? 1 : -1;
            int error = deltaX - deltaY;
            points.add(new PointF(b.x,b.y));
            /***************************/
            if(hCommand.equals(MutableBit.Command.ELAST_3)){
                fillAlterPointsAlpha(alpha,a,b,points.get(points.size() - 1));
            }
            if(hCommand.equals(MutableBit.Command.ELAST_2)) {
//                fillPointsAlpha(alpha, points.get(points.size() - 1),
//                        hRadius / hMat.getRepository().getScale());
               fillPointsAlpha(alpha, points.get(points.size() - 1),
                       RepDraw.get().getWidth()/2.0f / hMat.getRepository().getScale());

            }
            /***************************/
            while (a.x!=b.x||a.y!=b.y){
                points.add(new PointF(a.x,a.y));
                /***************************/
                if(hCommand.equals(MutableBit.Command.ELAST_3)){
                    fillAlterPointsAlpha(alpha,a,b,points.get(points.size() - 1));
                }
                if(hCommand.equals(MutableBit.Command.ELAST_2)) {
//                    fillPointsAlpha(alpha, points.get(points.size() - 1),
//                            hRadius / hMat.getRepository().getScale());
                    fillPointsAlpha(alpha, points.get(points.size() - 1),
                            RepDraw.get().getWidth()/2.0f / hMat.getRepository().getScale());

                }
                /***************************/
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
                    points.add(new PointF(a.x,a.y-signY));
                    /***************************/
                    if(hCommand.equals(MutableBit.Command.ELAST_3)){
                        fillAlterPointsAlpha(alpha,a,b,points.get(points.size() - 1));
                    }
                    if(hCommand.equals(MutableBit.Command.ELAST_2)) {
//                        fillPointsAlpha(alpha, points.get(points.size() - 1),
//                                hRadius / hMat.getRepository().getScale());
                        fillPointsAlpha(alpha, points.get(points.size() - 1),
                                RepDraw.get().getWidth()/2.0f / hMat.getRepository().getScale());

                    }
                    /***************************/
                }
            }
        }

        private void fillAlterPointsAlpha(ArrayList<Integer> arr,Point a,Point b,PointF p){
            PointF fA = new PointF(a.x,a.y);
            PointF fB = new PointF(b.x,b.y);
            float r = widthVector(vector(fA,fB));

            if(arr!=null) {
                arr.add(computeAlpha(ounCirc(vector(fA,p), r)));
            }

        }

        private void fillPointsAlpha(ArrayList<Integer> arr, PointF p, float r){

            if(arr!=null) {
                arr.add(computeAlpha(ounCirc(p, r)));
            }
        }

        /*searchRect(hPointsCenter,mMat.getPointBitmap(hStartSegment),mMat.getPointBitmap(hFinSegment));*/
        private void searchRect(ArrayList<PointF> points, PointF start, PointF fin){
            PointF zero = zeroingVector(start,fin);
            if(zero==null)return;

            if(hOldZeroVector!=null){
                points.clear();
                searchPointsCirc(points,zero,isRight(vector(start,fin),vector(hMat.getPointBitmap(hOldPoint),start)));
                hZeroingPoints.clear();
                hValuesAlpha.clear();
                for (PointF p:points){
                    addAllPoints(hZeroingPoints,new Point(0,0),extPoint(p),hValuesAlpha);
                }
//                 int alpha = hAlpha;
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

            points.clear();
            addAllPoints(points,extPoint(start),extPoint(fin),null);

            hZeroingPoints.clear();
            hValuesAlpha.clear();

            addAllPoints(hZeroingPoints,new Point((int)-zero.x,(int)zero.y),new Point((int)zero.x,(int)-zero.y),hValuesAlpha);

//            int alpha = hAlpha;
            int alpha = RepDraw.get().getAlpha();
            for (PointF p:points){
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
