package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.operations.bitmap.DrawBitmap;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;

import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.LYR_IMG;
import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.LYR_LYR;

public class DrawSpot extends BuildPath {

    private Paint hPaint;

    private Command hCommand;

    private Canvas hCanvas;

    private int hIndex, hLyr;

    private boolean hPreview;

    private Operation.ResultMutable hListener;

    public DrawSpot() {
        hPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hPaint.setStyle(Paint.Style.FILL);
        hPreview = false;
    }

    @Override
    protected void start(PointF s) {
        super.start(s);
        if(getCommand().equals(Command.SPOT)) {
            initParams();
        }
    }

    @Override
    protected void fin(PointF f) {
        super.fin(f);
        if(isPreview())getPath().reset();
    }

    public DrawSpot command(Command c){
        hCommand = c;
        return this;
    }

    public DrawSpot color(int c){
        hPaint.setColor(c);
        return this;
    }

    public DrawSpot canvas(Canvas c){
        hCanvas = c;
        return this;
    }

    public void listener(Operation.ResultMutable l){
        hListener = l;
    }

    public void mat(DeformMat m){

    }

    public void preview(boolean p){
        hPreview = p;
    }

    public void index(int i){
        hIndex = i;
    }

    public void lyr(int l){
        hLyr = l;
    }

    public void draw(){
        if(getCommand().equals(Command.SPOT)){
            drawSpot();
        }
    }

    private void drawSpot(){
        hCanvas.drawPath(getPath(),getPaintSpot());
        applyMutable();
    }

    protected void applyMutable(){
        if(getListener()!=null){
            if(getIndex()==RepDraw.ALL){
                RepDraw.get().startAllMutable();

                DrawBitmap.create(new Canvas(getImg()),getIMat()).antiAlias(true).draw(getOverlay(),getOMat());
                getListener().result(getImg(), getIMat(), RepDraw.ALL,RepDraw.LYR_IMG,RepDraw.MUTABLE_SIZE);

                DrawBitmap.create(new Canvas(getLyr()),getLMat()).antiAlias(true).draw(getOverlay(),getOMat());
                getListener().result(getLyr(), getLMat(), RepDraw.ALL,RepDraw.LYR_LYR,RepDraw.MUTABLE_SIZE);
            } else{
                RepDraw.get().startSingleMutable();
                if(getIndexLyr()==LYR_IMG){
                    DrawBitmap.create(new Canvas(getImg()),getIMat()).antiAlias(true).draw(getOverlay(),getOMat());
                    getListener().result(getImg(), getIMat(), getIndex(),getIndexLyr(),RepDraw.MUTABLE_SIZE);
                }else if(getIndexLyr()==LYR_LYR){
                    DrawBitmap.create(new Canvas(getLyr()),getLMat()).antiAlias(true).draw(getOverlay(),getOMat());
                    getListener().result(getLyr(), getLMat(), getIndex(),getIndexLyr(),RepDraw.MUTABLE_SIZE);
                }
            }
        }
    }

    private void initParams(){
        color(RepDraw.get().getColor());
        hPaint.setStyle(Paint.Style.FILL);

    }

    protected Operation.ResultMutable getListener(){
        return hListener;
    }
    protected Canvas getCanvas(){
        return hCanvas;
    }
    protected Paint getPaintSpot(){
        return hPaint;
    }

    protected int getIndex(){
        return hIndex;
    }

    protected int getIndexLyr(){
        return hLyr;
    }

    protected boolean isPreview(){
        return hPreview;
    }

    protected Command getCommand(){
        return hCommand;
    }

    protected Bitmap getImg(){
        return RepDraw.get().getImg();
    }

    protected Bitmap getLyr(){
        return RepDraw.get().getLyr();
    }

    protected Bitmap getOverlay(){
        return RepDraw.get().getOverlay();
    }

    protected DeformMat getOMat(){
        return RepDraw.get().getOMat();
    }

    protected DeformMat getLMat(){
        return RepDraw.get().getLMat();
    }

    protected DeformMat getIMat(){
        return RepDraw.get().getIMat();
    }


}
