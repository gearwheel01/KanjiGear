package com.example.kanjigear.views.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
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
    private int showHint;

    private ArrayList<Path> pathsOutline;
    private int outlineWidth;
    private ArrayList<Path> pathsHelper;
    private int helperWidth;
    private ArrayList<Path> drawnStrokes;
    private ArrayList<Integer> scores;
    private boolean drawMode = false;
    private int drawTolerance;
    private int drawStroke;
    private DrawKanji context;

    private boolean transformedPaths = false;
    private int drawUntilStroke;
    private float lastStrokeLength;

    private final float TOLERANCE_PERCENTAGE = 0.2f;
    private final int BAD_STROKE_PUNISHMENT = -100;
    private final int STROKE_QUALITY_MINIMUM = 50;
    private final int HINT_AFTER_MISSES = 3;

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
        outlineWidth = 2;
        helperWidth = 1;

        showHint = 0;
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
        scores = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!transformedPaths) {
            transformPaths();
        }

        if (strokes != null) {
            paint.setColor(getResources().getColor(R.color.purple_300));
            paint.setStrokeWidth(helperWidth);
            for (int i = 0; i < pathsHelper.size(); i += 1) {
                canvas.drawPath(pathsHelper.get(i), paint);
            }
            paint.setStrokeWidth(strokeWidth);

            if (showHint >= HINT_AFTER_MISSES) {
                paint.setColor(getResources().getColor(R.color.purple_300));
                canvas.drawPath(strokes.get(drawStroke).getPath(), paint);
            }

            paint.setColor(Color.BLACK);
            canvas.drawPath(path, paint);
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
                paint.setAlpha(255);
            }

            paint.setColor(getResources().getColor(R.color.purple_500));
            paint.setStrokeWidth(outlineWidth);
            for (int i = 0; i < pathsOutline.size(); i += 1) {
                canvas.drawPath(pathsOutline.get(i), paint);
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
                    int score = comparePaths(strokes.get(drawStroke).getPath(), path);
                    if (score >= STROKE_QUALITY_MINIMUM) {
                        scores.add(score);
                    }
                    else {
                        scores.add(BAD_STROKE_PUNISHMENT);
                        showHint += 1;
                    }
                    if (score >= STROKE_QUALITY_MINIMUM) {
                        showHint = 0;
                        drawnStrokes.add(path);
                        drawStroke += 1;
                        drawUntilStroke += 1;

                        if (drawStroke >= strokes.size()) {
                            drawMode = false;
                            context.finishedDrawing(getScoreAverage());
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

    private int getScoreAverage() {
        int ret = 0;
        for(int i = 0; i < scores.size(); i += 1) {
            ret += scores.get(i);
        }
        return (ret / scores.size());
    }

    public void transformPaths() {
        if ( (strokes != null) && (getWidth() > 0) ) {
            for (int i = 0; i < strokes.size(); i += 1) {
                strokes.get(i).transformPath(getWidth(), getHeight());
            }
            strokeWidth = strokeWidth * (getWidth() / 109);
            paint.setStrokeWidth(strokeWidth);
            drawTolerance = (int)(getWidth() * TOLERANCE_PERCENTAGE);

            outlineWidth = outlineWidth * (getWidth() / 109);
            pathsOutline = getPathsOutline(getWidth());
            helperWidth = helperWidth * (getWidth() / 109);
            pathsHelper = getPathsHelper(getWidth());

            transformedPaths = true;
        }
    }

    public void updateStrokeDrawDetails(int drawUntilStroke, float lastStrokeLength) {
        this.drawUntilStroke = drawUntilStroke;
        this.lastStrokeLength = lastStrokeLength;
        invalidate();
    }

    public int comparePaths(Path p1, Path p2) {
        int sampleCounter = 0;
        float distance = 0;
        for (float i = 0f; i < 1; i += 0.05f) {
            sampleCounter += 1;
            float[] coords1 = getPathCoords(p1, i);
            float[] coords2 = getPathCoords(p2, i);
            float dx = coords1[0] - coords2[0];
            float dy = coords1[1] - coords2[1];
            distance += Math.sqrt((dx * dx) + (dy * dy));
        }
        distance /=  sampleCounter;
        int ret = Math.max(0, (int)((100 - ((distance / drawTolerance) * 100))));
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

    public ArrayList<Path> getPathsOutline(int size) {
        ArrayList<Path> paths = new ArrayList<>();

        int s = outlineWidth / 2;
        int e = size - s;

        paths.add(getLinePath(0, s, e + s, s));
        paths.add(getLinePath(0, e, e + s, e));
        paths.add(getLinePath(s, s, s, e));
        paths.add(getLinePath(e, s, e, e));

        return paths;
    }

    public ArrayList<Path> getPathsHelper(int size) {
        ArrayList<Path> paths = new ArrayList<>();

        int s = helperWidth / 2;
        int m = (size / 2) - s;
        int e = size - s;

        paths.add(getLinePath(s, m, e, m));
        paths.add(getLinePath(m, s, m, e));

        return paths;
    }

    public Path getLinePath(int x1, int y1, int x2, int y2) {
        Path p = new Path();
        p.moveTo(x1, y1);
        p.lineTo(x2, y2);
        return p;
    }
}
