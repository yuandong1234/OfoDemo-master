package yuandong.ofodemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;

/**
 * 仿Ofo个人中心背景
 * Created by yuandong on 2017/12/28.
 */

public class OfoMenuDrawable extends Drawable {
    private static String TAG = OfoMenuDrawable.class.getSimpleName();

    private Context mContext;
    private Paint paint;

    //外层弧形path
    private Path mPath;
    //峰值常亮(80dp)
    private static final int HEIGHTEST_Y = 80;
    //弧度的峰值，为后面绘制贝塞尔曲线做准备
    private int arcY;

    public OfoMenuDrawable(Context mContext) {
        this.mContext = mContext;
        arcY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHTEST_Y, mContext.getResources().getDisplayMetrics());
        mPath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mPath.reset();
        mPath.moveTo(bounds.left, bounds.top + arcY);
        mPath.quadTo(bounds.centerX(), 0, bounds.right, bounds.top + arcY);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.top + arcY);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mPath, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
