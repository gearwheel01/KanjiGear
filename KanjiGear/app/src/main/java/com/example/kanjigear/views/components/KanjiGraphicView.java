package com.example.kanjigear.views.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Stroke;

import java.util.ArrayList;

public class KanjiGraphicView extends View {

    private Paint paint = new Paint();
    private ArrayList<Stroke> strokes;
    private Path path = new Path();
    private boolean drawMode = false;
    private int strokeWidth = 5;

    private boolean transformedPaths = false;
    private int drawUntilStroke;
    private float lastStrokeLength;

    public KanjiGraphicView(Context context, AttributeSet attributes) {
        super(context, attributes);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setBackgroundResource(R.color.purple_200);
    }

    public void setStrokes(ArrayList<Stroke> strokes) {
        this.strokes = strokes;
        drawUntilStroke = strokes.size();
        lastStrokeLength = 0;
        invalidate();
    }
    public void setDrawMode(boolean dm) {
        drawMode = dm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);

        if (!transformedPaths) {
            transformPaths();
        }

        if (strokes != null) {
            for (int i = 0; i < strokes.size(); i += 1) {
                if (i <= drawUntilStroke) {
                    Path p = strokes.get(i).getPath();
                    if (i == drawUntilStroke) {
                        PathMeasure measure = new PathMeasure(strokes.get(i).getPath(), false);
                        float length = measure.getLength();
                        p = new Path();
                        measure.getSegment(0f, (length * lastStrokeLength), p, true);
                    }
                    canvas.drawPath(p, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawMode) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x, y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    return false;
            }

            invalidate();
        }
        return true;
    }

    public void transformPaths() {
        if ( (strokes != null) && (getWidth() > 0) ) {
            for (int i = 0; i < strokes.size(); i += 1) {
                strokes.get(i).transformPath(getWidth(), getHeight());
            }
            transformedPaths = true;
        }
        strokeWidth = strokeWidth * (getWidth() / 109);
        paint.setStrokeWidth(strokeWidth);
    }

    public void updateStrokeDrawDetails(int drawUntilStroke, float lastStrokeLength) {
        this.drawUntilStroke = drawUntilStroke;
        this.lastStrokeLength = lastStrokeLength;
        invalidate();
    }
}
