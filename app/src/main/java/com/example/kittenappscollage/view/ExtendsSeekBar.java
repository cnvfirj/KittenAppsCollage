package com.example.kittenappscollage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.mohammedalaa.seekbar.RangeSeekBarView;

public class ExtendsSeekBar extends RangeSeekBarView {

    private TrackSeekBar eTrack;





    public ExtendsSeekBar(Context context) {
        super(context);

    }

    public ExtendsSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setTracker(TrackSeekBar track){
        eTrack = track;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if(eTrack!=null)eTrack.touch(seekBar);
    }



    public interface TrackSeekBar{
        public void touch(SeekBar s);
    }

}
