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
import com.example.kanjigear.views.lesson.DrawKanji;

import java.util.ArrayList;

public class KanjiGraphicView extends View {

    private Paint paint = new Paint();
    private ArrayList<Stroke> strokes;
    private Path path = new Path();
    private int strokeWidth;

    private ArrayList<Path> drawnStrokes;
    private boolean drawMode = false;
    private int drawTolerance;
    private int drawStroke;
    private DrawKanji context;

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
        transformedPaths = false;
        strokeWidth = 5;
        this.strokes = strokes;
        drawUntilStroke = strokes.size();
        lastStrokeLength = 0;
        invalidate();
    }
    public void setDrawMode(DrawKanji context) {
        this.context = context;
        drawMode = true;
        drawUntilStroke = 0;
        drawStroke = 0;
        drawnStrokes = new ArrayList<>();
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
            if (drawStroke >= strokes.size()) {
                paint.setColor(Color.RED);
                paint.setAlpha(150);
                for (int i = 0; i < strokes.size(); i += 1) {
                    canvas.drawPath(drawnStrokes.get(i), paint);
                }
                paint.setColor(Color.BLACK);
                paint.setAlpha(255);
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
                    if (comparePaths(strokes.get(drawStroke).getPath(), path) > 10) {
                        drawnStrokes.add(path);
                        drawStroke += 1;
                        drawUntilStroke += 1;

                        if (drawStroke >= strokes.size()) {
                            drawMode = false;
                            context.finishedDrawing();
                        }
                    }
                    path = new Path();
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
        drawTolerance = (int)(getWidth() * 0.1);
        Log.d("path","tolerance: " + drawTolerance);
    }

    public void updateStrokeDrawDetails(int drawUntilStroke, float lastStrokeLength) {
        this.drawUntilStroke = drawUntilStroke;
        this.lastStrokeLength = lastStrokeLength;
        invalidate();
    }

    public int comparePaths(Path p1, Path p2) {
        int sampleCounter = 0;
        float distance = 0;
        for (float i = 0f; i < 1; i += 0.1f) {
            sampleCounter += 1;
            float[] coords1 = getPathCoords(p1, i);
            float[] coords2 = getPathCoords(p2, i);
            float dx = coords1[0] - coords2[0];
            float dy = coords1[1] - coords2[1];
            distance += Math.sqrt((dx * dx) + (dy * dy));
        }
        distance /=  sampleCounter;
        int ret = Math.max(0, (int)((100 - ((distance / drawTolerance) * 100))));
        Log.d("pathcalc","score: " + ret);
        return ret;
    }

    public float[] getPathCoords(Path p, float lengthPercentage) {
        PathMeasure pm = new PathMeasure(p, false);
        float[] coords = new float[2];
        pm.getPosTan(pm.getLength() * lengthPercentage, coords, null);
        return coords;
    }

    public void resetDrawing() {
        setDrawMode(context);
        invalidate();
    }
}
