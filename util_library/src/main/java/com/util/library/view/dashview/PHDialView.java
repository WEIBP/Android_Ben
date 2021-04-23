package com.util.library.view.dashview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import com.util.library.common.ColorUtil;

/**
 * author: Ben
 * created on: 2020/5/19 9:57
 * description:
 */
public class PHDialView extends View {

        private   Paint.FontMetrics fontMetrics;
        private  Paint pointerPaint;
        private  int phWidth = 30;
        private   int phHeigh = 60;
        private   int phSpace = 8;

        private int textSizeDial = 22;

        private Paint mPaint;
        private Path mPath;
        private int currentValue = 1;

        public static final int[] PH_COLORS = {
                ColorUtil.rgb("#943346"), ColorUtil.rgb("#bf021b"), ColorUtil.rgb("#ee181a"), ColorUtil.rgb("#ea5534"),
                ColorUtil.rgb("#ea9600"), ColorUtil.rgb("#ffe200"), ColorUtil.rgb("#6fba2c"), ColorUtil.rgb("#638ec1"),
                ColorUtil.rgb("#1e69ac"), ColorUtil.rgb("#5e1d6f"), ColorUtil.rgb("#201A3E"), ColorUtil.rgb("#170F38"),
                ColorUtil.rgb("#1D0733"), ColorUtil.rgb("#1C0C30"),

        };

        public PHDialView(Context context) {
                super(context);
        }

        public PHDialView(Context context, @Nullable AttributeSet attrs) {
                super(context, attrs);
                mPaint = new Paint();
                mPath = new Path();
                pointerPaint = new Paint();
                pointerPaint.setAntiAlias(true);
                pointerPaint.setTextSize(textSizeDial);
                pointerPaint.setTextAlign(Paint.Align.CENTER);
                fontMetrics = pointerPaint.getFontMetrics();
        }

        public PHDialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                int width = measureWidth(widthMeasureSpec);
                int height = measureHeight(heightMeasureSpec);
                setMeasuredDimension(width, height);
        }

        private int measureWidth(int measureSpec) {
                int result ;
                int specMode = MeasureSpec.getMode(measureSpec);
                int specSize = MeasureSpec.getSize(measureSpec);


                if (specMode == MeasureSpec.EXACTLY) {
                        result = specSize;
                } else {
                        result =  14*phWidth+13*phSpace+ getPaddingLeft() + getPaddingRight();
                        if (specMode == MeasureSpec.AT_MOST) {
                                result = Math.min(result, specSize);
                        }
                }
                phSpace = (result -getPaddingLeft() - getPaddingRight())/69;
                phWidth = phSpace * 4 ;
                return result;
        }


        private int measureHeight(int measureSpec) {
                int mHeight ;
                int specMode = MeasureSpec.getMode(measureSpec);
                int heightSize = MeasureSpec.getSize(measureSpec);

                if (specMode == MeasureSpec.EXACTLY) {
                        mHeight = heightSize;
                } else {
                        mHeight = getPaddingTop() + phHeigh * 2 + getPaddingBottom();
                        if (specMode == MeasureSpec.AT_MOST) {
                                mHeight = Math.min(mHeight, heightSize);
                        }
                }
                phHeigh = ( mHeight - getPaddingTop() - getPaddingBottom() ) / 2;
                return mHeight;
        }



        @Override protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                drawlayout(canvas);
                drawValue(canvas);
        }

        private void drawValue(Canvas canvas) {
                mPath.reset();
                if (0<= currentValue && currentValue <=13) {
                        // 绘制这个三角形,你可以绘制任意多边形
                        mPaint.setColor(Color.BLACK);
                        int y = phHeigh>>3;
                        mPath.moveTo(currentValue*(phSpace+phWidth)+ (phWidth>>3) , y);// 此点为多边形的起点
                        mPath.lineTo(currentValue*(phSpace+phWidth)+ (phWidth*7>>3), y);
                        mPath.lineTo(currentValue*(phSpace+phWidth)+ (phWidth>>1), 3*y);
                        mPath.close(); // 使这些点构成封闭的多边形
                        canvas.drawPath(mPath, mPaint);
                }

        }

        private void drawlayout(Canvas canvas) {
                //绘制直方图，柱形图是用较粗的直线来实现的

                mPaint.setStrokeWidth(phWidth);

                for (int i = 0; i < PH_COLORS.length; i++) {
                        mPaint.setColor(PH_COLORS[i]);

                        canvas.drawLine(i*(phSpace+phWidth)+15, phHeigh >> 1,
                                i*(phSpace+phWidth)+15, phHeigh * 3 >> 1,mPaint);

                        pointerPaint.setColor( PH_COLORS[i]);
                        int textBaseLine = (int) ((phHeigh * 3 >> 1) + (fontMetrics.bottom - fontMetrics.top) );
                        int x = i*(phSpace+phWidth)+(phWidth>>1);
                        canvas.drawText(String.valueOf(i+1), x, textBaseLine, pointerPaint);
                }

        }

        public void setValue(int value) {
                if (value > 14) {
                        currentValue = 14;
                }
                if (value < 1) {
                        currentValue = 1;
                } else {
                        currentValue = value;
                }

                currentValue--;
                invalidate();
        }
}
