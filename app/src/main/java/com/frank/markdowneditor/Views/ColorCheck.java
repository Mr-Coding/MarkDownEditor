package com.frank.markdowneditor.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorCheck extends View {
    public ColorCheck(Context context) {    super(context); }
    public ColorCheck(Context context, @Nullable AttributeSet attrs) {  super(context, attrs);  }

    private int                    color                = Color.GRAY;
    private boolean                isChecked            = false;
    private OnCheckChangedListener checkChangedListener = null;

    public void setCheckChangedListener(OnCheckChangedListener checkChangedListener){
        this.checkChangedListener = checkChangedListener;
    }

    public interface OnCheckChangedListener{
        void isCheckChanged(boolean isChecked, int ID);
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
//        requestLayout();
        invalidate();
    }

    public void setColor(int color){
        this.color = color;
//        requestLayout();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!isChecked){
                isChecked = true;
            }else{
                isChecked = false;
            }
            // 刷新和重绘View
//            requestLayout();//先调用这个
            invalidate();   //再调用这个
            if (checkChangedListener != null) {
                checkChangedListener.isCheckChanged(isChecked, getId());
            }

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        RectF r1=new RectF(); //RectF对象  
        r1.left=8;   //左边  
        r1.top=8;   //上边  
        r1.right=getWidth()-8; //右边
        r1.bottom=getHeight()-8;//下边
        canvas.drawRoundRect(r1,20,20,paint);
        if (isChecked) {
            paint.setStrokeWidth((float)8.0);
            paint.setColor(Color.rgb(65,65,65));
            paint.setStyle(Paint.Style.STROKE);
            RectF r2=new RectF(); //RectF对象  
            r2.left=10;   //左边  
            r2.top=10;   //上边  
            r2.right=getWidth()-10; //右边
            r2.bottom=getHeight()-10;//下边
            canvas.drawRoundRect(r2,20,20,paint);
        }

    }
}
