package com.example.yogaappwithdb.StuffForRequest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.yogaappwithdb.R;

public class IndicatingView extends View {

    public static final int NOTEXECUTED = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    public static final int EXECUTING = 3;

    int state = NOTEXECUTED;


    public IndicatingView(Context context) {
        super(context);
    }

    public IndicatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas)
    {
        int color = ContextCompat.getColor(getContext(), R.color.darkBlue);
        int color1 = ContextCompat.getColor(getContext(), R.color.lighterBlue);

        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint;
        switch (state)
        {
            case SUCCESS:
                paint = new Paint();
                paint.setColor(color);
                paint.setStrokeWidth(7f);
                //checkmark
                canvas.drawLine(0,0,width/2, height, paint);
                canvas.drawLine(width/2, height, width, height/2, paint);
                break;
            case FAILED:
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(10f);
                //X
                canvas.drawLine(0,0,width,height,paint);
                canvas.drawLine(0,height,width,0, paint);
                break;
            case EXECUTING:
                paint = new Paint();

                paint.setColor(color1);
                paint.setStrokeWidth(5f);

                //TRIANGLE
                canvas.drawLine(width/2,0,0,height,paint);
                canvas.drawLine(width, height, width/2, 0, paint);
                canvas.drawLine(0, height, width, height, paint);
                break;

            default:
                break;
        }
    }
}