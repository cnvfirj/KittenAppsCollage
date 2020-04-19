package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

public class ListenVeil extends BodyVeil {

    private TargetView.OnClickTargetViewNoleListener listener;

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
            else touchVeil();

        }
        return super.onTouchEvent(event);
    }

    public void setListener(TargetView.OnClickTargetViewNoleListener listener) {
        this.listener = listener;
    }

    private boolean isInRect(PointF p, RectF r){
        if(p.x>=r.left&&p.x<=r.right){
            if(p.y>=r.top&&p.y<=r.bottom){
                return true;
            }
        }
        return false;
    }

    private void touchTarget(){
        if(listener!=null){
            listener.onClick(TargetView.TOUCH_TARGET);
        }
    }

    private void touchVeil(){
        if(listener!=null){
            listener.onClick(TargetView.TOUCH_VEIL);
        }
    }

    private void touchOut(){
        if(listener!=null){
            listener.onClick(TargetView.TOUCH_UOT);
        }
    }
}
