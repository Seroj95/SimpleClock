package culture.baofeng.com.viewv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by huangyong on 2017/12/21.
 */

public class CircleView extends View{

    private Paint mPaint;//刻度
    private Paint mMaxDegree;//大刻度
    private Paint mMiniDegress;//中刻度
    private Paint mMinPaint;//分针
    private Paint mHourPaint;//时针
    private Paint mSecondPaint;//秒针
    private Path path;
    private int mWidth;
    private int mHeight;
    private Canvas scanvas;
    /* 时针角度 */
    private float mHourDegree;
    /* 分针角度 */
    private float mMinuteDegree;
    /* 秒针角度 */
    private float mSecondDegree;
    private TextPaint textPaint;
    private Rect mTextRect;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.WHITE);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setStyle(Paint.Style.STROKE);
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);
        mHourPaint.setStrokeWidth(10);
        mHourPaint.setColor(context.getResources().getColor(R.color.mHourColor));

        mMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinPaint.setStyle(Paint.Style.STROKE);
        mMinPaint.setStrokeWidth(6);
        mMinPaint.setStrokeCap(Paint.Cap.ROUND);
        mMinPaint.setColor(context.getResources().getColor(R.color.mMinColor));

        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(1);
        mSecondPaint.setColor(context.getResources().getColor(R.color.mSecondColor));

        //大刻度，总共12个
        mMaxDegree = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaxDegree.setColor(Color.WHITE);
        mMaxDegree.setStyle(Paint.Style.STROKE);
        mMaxDegree.setStrokeWidth(6);
        scanvas = new Canvas();
        path = new Path();
        RectF f = new RectF(0,100,4,0);
        path.addRect(f, Path.Direction.CCW);

        textPaint = new TextPaint();
        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
        mTextRect = new Rect();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth= w;
        this.mHeight =h;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        canvas.save();
        for (int i = 0; i <=360; i+=6) {
            if (i%30==0){
                canvas.drawCircle(0,-290,1,mMinPaint);
            }else if (i%10==0){
                canvas.drawLine(0,290,0,320,mMiniDegress);
            }else {
                canvas.drawLine(0,300,0,316,mPaint);
            }
            canvas.rotate(6);
        }
        canvas.restore();
        drawTimeText(canvas);
        getTimeDegree();
        drawHour(canvas);
        drawMinute(canvas);
        canvas.drawCircle(0,0,1,mMinPaint);
        drawSecond(canvas);
        invalidate();
    }

    private void drawTimeText(Canvas canvas) {

        String string = "12";
        textPaint.getTextBounds(string,0,string.length(),mTextRect);
        int textWidth= mTextRect.width();
        String string2 = "9";
        textPaint.getTextBounds(string,0,string2.length(),mTextRect);
        int textWidth2= mTextRect.width();

        for (int i = 1; i <=12; i++) {
            canvas.save();
            canvas.rotate(i*30);
            canvas.rotate(-i*30,0,-320);
            canvas.translate(-5,5);
            if (i>9){
                canvas.drawText(i+"",-textWidth/2,-314,textPaint);
            }else {
                if (i>6){

                    canvas.drawText(i+"",textWidth2/2,-314,textPaint);
                }else {
                    canvas.drawText(i+"",-textWidth2/2,-314,textPaint);
                }
            }

            canvas.restore();
        }
    }

    private void drawHour(Canvas canvas) {
        canvas.save();
        canvas.rotate(mHourDegree, 0,0);
        canvas.drawLine(0,0,0,-228,mHourPaint);
        canvas.restore();
    }

    private void drawMinute(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinuteDegree, 0,0);
        canvas.drawLine(0,0,0,-270,mMinPaint);
        canvas.restore();
    }

    private void drawSecond(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecondDegree, 0,0);
        canvas.drawLine(0,0,0,-278,mSecondPaint);
        canvas.restore();
    }

    /**
     * 获取当前时分秒所对应的角度
     * 为了不让秒针走得像老式挂钟一样僵硬，需要精确到毫秒
     */
    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND);
//        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;//平滑扫过
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;
        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;

    }
}
