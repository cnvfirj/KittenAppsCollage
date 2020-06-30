package com.kittendevelop.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.util.LinkedList;
import java.util.Queue;

public class HelpFill {

    protected Bitmap hBitmap;


    protected int[] hPixels;

    protected int[] hCheckColor;

    protected int hWidth,hHeight;

    protected int hFillColor, hStartColor;

    protected int hX,hY;

    protected boolean[]hPixelsChecked;

    protected Queue<FillRange> hRanges;

    private boolean hTypeFill;

    public HelpFill() {
        hStartColor = Color.WHITE;
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

    protected void useParams(){
            hWidth = hBitmap.getWidth();
            hHeight = hBitmap.getHeight();
            hPixels = new int[hWidth * hHeight];
            hBitmap.getPixels(hPixels, 0, hWidth, 0, 0, hWidth, hHeight);
    }


    public HelpFill setFillColor(int color){
        hFillColor = color;
        return this;
    }

    public HelpFill setPoint(PointF p){
        if(p.x<0||p.y<0||p.x>=hBitmap.getWidth()||p.y>=hBitmap.getHeight())return this;
        hX = (int) p.x;
        hY = (int) p.y;
        hStartColor = hBitmap.getPixel(hX,hY);
        searchCheckColor(hStartColor);
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
        return endFill();

    }

    private void searchCheckColor(int startColor){
        hCheckColor = new int[4];
        if(hTypeFill) {
            hCheckColor[0] = Color.red(startColor);
            hCheckColor[1] = Color.green(startColor);
            hCheckColor[2] = Color.blue(startColor);
            hCheckColor[3] = Color.alpha(startColor);
        }else {
            hCheckColor[0] = Color.red(RepDraw.get().getColor());
            hCheckColor[1] = Color.green(RepDraw.get().getColor());
            hCheckColor[2] = Color.blue(RepDraw.get().getColor());
            hCheckColor[3] = Color.alpha(RepDraw.get().getColor());
        }
    }

    private void linearFill(int x, int y){
        int left = x; // the location to check/fill on the left
        int index = (hWidth * y) + x;

        while (true){
            filingUnder(index);
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
            filingUnder(index);
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
            return checkPixel(index);
       /*простая заливка без учета сглаживания*/
//        int color = hPixels[index];
//        if(hTypeFill) return color==hStartColor;
//        else return color!= RepDraw.get().getColor();

    }

    protected boolean checkPixel(int index) {
        int color = hPixels[index];
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);

        if(hTypeFill) {
            return (
                    red >= (hCheckColor[0] - 15)
                            && red <= (hCheckColor[0] + 15)
                            && green >= (hCheckColor[1] - 15)
                            && green <= (hCheckColor[1] + 15)
                            && blue >= (hCheckColor[2] - 15)
                            && blue <= (hCheckColor[2] + 15)
                            && alpha >= (hCheckColor[3] - 15)
                            && alpha <= (hCheckColor[3] + 15));
        }
        else {
            return (
                    red <= (hCheckColor[0] - 15)
                            ||red >= (hCheckColor[0] + 15)
                            ||green <= (hCheckColor[1] - 15)
                            ||green >= (hCheckColor[1] + 15)
                            ||blue <= (hCheckColor[2] - 15)
                            ||blue >= (hCheckColor[2] + 15)
                            ||alpha <= (hCheckColor[3] - 15)
                            ||alpha >= (hCheckColor[3] + 15));
        }
    }


    private void otherParams(){
        hPixelsChecked = new boolean[hPixels.length];
        hRanges = new LinkedList<FillRange>();
    }



    protected void filingUnder(int index){

    }

    protected Bitmap endFill(){
      return null;
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
