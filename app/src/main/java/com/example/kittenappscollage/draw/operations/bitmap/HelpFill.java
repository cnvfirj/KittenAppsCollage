package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import java.util.LinkedList;
import java.util.Queue;

public class HelpFill {

    protected Bitmap hBitmap;

    protected int[] hPixels;

    protected int hWidth,hHeight;

    protected int hFillColor, hStartColor;

//    protected int[]
//           []hStartColor,
//            hTargetColor;

    protected int hX,hY;

    protected boolean[]hPixelsChecked;

    protected Queue<FillRange> hRanges;

    private boolean hTypeFill;

    public HelpFill() {
//        hStartColor = new int[]{0,0,0,0};
        hStartColor = Color.WHITE;
//        hTargetColor = new int[]{0,0,0,0};
        hFillColor = Color.WHITE;
        hTypeFill = true;
    }


    public HelpFill useImg(Bitmap bitmap){
        hBitmap = bitmap;
        useParams();
        return this;
    }

    public HelpFill setTypeFill(MutableBit.Command command){
        if(command.equals(MutableBit.Command.FILL_C))hTypeFill = true;
        else if (command.equals(MutableBit.Command.FILL_B))hTypeFill = false;
        return this;
    }

    public Bitmap getBitmap() {
        return hBitmap;
    }

    private void useParams(){
        hWidth = hBitmap.getWidth();
        hHeight = hBitmap.getHeight();
        hPixels = new int[hWidth*hHeight];
        hBitmap.getPixels(hPixels, 0, hWidth, 0, 0, hWidth , hHeight );
    }

    public HelpFill setFillColor(int color){
        hFillColor = color;
//        setChapterColor(hFillColor);
//        hStartColor = color;
        return this;
    }

    public HelpFill setPoint(Point p){
        if(p.x<0||p.y<0||p.x>=hBitmap.getWidth()||p.y>=hBitmap.getHeight())return this;
        hX = p.x;
        hY = p.y;
//        setChapterColor(hBitmap.getPixel(hX,hY));
        hStartColor = hBitmap.getPixel(hX,hY);
        return this;
    }

    public HelpFill clear(){
        if(hPixels!=null)hPixels = new int[1];
        if(hPixelsChecked!=null)hPixelsChecked = new boolean[1];
        return this;
    }

    public Bitmap fill(){
        otherParams();

        linearFill(hX,hY);

        FillRange f;

        while (hRanges.size()>0){
           f = hRanges.remove();

            int upY = f.Y - 1;
            int downY = f.Y + 1;

           int downIndex = (hWidth * (f.Y + 1)) + f.startX;
           int upIndex = (hWidth * (f.Y - 1)) + f.startX;


           for (int i = f.startX;i<f.endX;i++){
               if(f.Y>0&&!hPixelsChecked[upIndex]&&checkColor(upIndex)){
                   linearFill(i,upY);
               }

               if(f.Y<hHeight-1&&!hPixelsChecked[downIndex]&&checkColor(downIndex)){
                   linearFill(i,downY);
               }

               downIndex++;
               upIndex++;
           }

        }
          hBitmap.setPixels(hPixels,0, hWidth, 0, 0, hWidth , hHeight );

        return hBitmap;

    }

    private void linearFill(int x, int y){
        int left = x; // the location to check/fill on the left
        int index = (hWidth * y) + x;

        while (true){
            hPixels[index] = hFillColor;
            hPixelsChecked[index] = true;

            left--;
            index--;

            if(left<0||hPixelsChecked[index]||!checkColor(index)){

                break;
            }
        }

        left++;


        int right = x;
        index = (hWidth * y) + x;
        while (true) {
            hPixels[index] = hFillColor;
            hPixelsChecked[index] = true;

            right++;
            index++;

            if(right>=hWidth||hPixelsChecked[index]||!checkColor(index)){
                break;
            }
        }

        right--;

        FillRange f = new FillRange(left,right,y);
        hRanges.offer(f);
    }

    private boolean checkColor(int index){
        int color = hPixels[index];

//        int alpha = Color.alpha(color);
//        int red = Color.red(color);
//        int green = Color.green(color);
//        int blue = Color.blue(color);
        if(hTypeFill) return color==hStartColor;
//                alpha==hStartColor[0]&&red==hStartColor[1]&&green==hStartColor[2]&&blue==hStartColor[3];
        else return color!=hFillColor;
    }

//    private void setChapterColor(int color){
//        hStartColor[0] = Color.alpha(color);
//        hStartColor[1] = Color.red(color);
//        hStartColor[2] = Color.green(color);
//        hStartColor[3] = Color.blue(color);
//
//
//
//    }



    private void otherParams(){
        hPixelsChecked = new boolean[hPixels.length];
        hRanges = new LinkedList<FillRange>();
    }






    protected class FillRange {
        public int startX;
        public int endX;
        public int Y;

        public FillRange(int startX, int endX, int y) {
            this.startX = startX;
            this.endX = endX;
            this.Y = y;
        }
    }
}
