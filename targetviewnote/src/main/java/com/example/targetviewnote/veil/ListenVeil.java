package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

public class ListenVeil extends BodyVeil {

    private DrawMidiVeil.InternalListener listener;

    public ListenVeil(Context context) {
        super(context);
    }

    public ListenVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            PointF touch = new PointF(event.getX(),event.getY());
            if(getTarget()!=null&&isInRect(touch,getTarget()))touchTarget();
            else if(getTarget()!=null&&isInRect(touch, getVeil()))touchVeil();
            else touchOut();
        }
        return super.onTouchEvent(event);
    }

    public void setListener(DrawMidiVeil.InternalListener listener) {
        this.listener = listener;
    }

    public DrawMidiVeil.InternalListener getListener() {
        return listener;
    }

    protected boolean isInRect(PointF p, RectF r){
        if(p.x>=r.left&&p.x<=r.right){
            if(p.y>=r.top&&p.y<=r.bottom){
                return true;
            }
        }
        return false;
    }

    protected void touchTarget(){
        if(listener!=null){
            listener.click(TargetView.TOUCH_TARGET);
        }else {
            Log.e("ListenVeil","null click listener");
        }
    }

    protected void touchVeil(){
        if(listener!=null){
            listener.click(TargetView.TOUCH_VEIL);
        }
    }

    protected void touchOut(){
        if(listener!=null){
            listener.click(TargetView.TOUCH_UOT);
        }
    }
}
