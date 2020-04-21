package com.example.targetviewnote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class NoteText extends androidx.appcompat.widget.AppCompatEditText {
    private static final int HIGHLIGHTER_YELLOW = Color.RED;
    private Rect lineBounds;
    private Paint highlightPaint;
    private int lineNumber;

    public NoteText(Context context) {
        super(context);
    }

    public NoteText(Context context, AttributeSet attrs) {
        super(context, attrs);
        lineBounds = new Rect();
        highlightPaint = new Paint();
        highlightPaint.setColor(HIGHLIGHTER_YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
            lineNumber = getLayout().getLineForOffset(getSelectionEnd());
            getLineBounds(lineNumber, lineBounds);
            canvas.drawRect(lineBounds, highlightPaint);
        super.onDraw(canvas);

    }

}
