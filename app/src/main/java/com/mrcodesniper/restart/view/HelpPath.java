package com.mrcodesniper.restart.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.mrcodesniper.restart.Constant;


public class HelpPath {

    /**
     * 绘制网格:注意只有用path才能绘制虚线
     *
     * @param step 小正方形边长
     * @param winSize 屏幕尺寸
     */
    public static Path gridPath(int step, Point winSize) {
        Path path = new Path();
        for (int i = 0; i < winSize.y / step + 1; i++) {
            path.moveTo(0, step * i);
            path.lineTo(winSize.x, step * i);
        }
        for (int i = 0; i < winSize.x / step + 1; i++) {
            path.moveTo(step * i, 0);
            path.lineTo(step * i, winSize.y);
        }
        return path;
    }

    /**
     * 绘制网格
     * @param canvas 画布
     * @param winSize 屏幕尺寸
     * @param paint 画笔
     */
    public static void drawGrid(Canvas canvas, Point winSize, Paint paint) {
        //初始化网格画笔
        paint.setStrokeWidth(2);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE); //虚线
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        canvas.drawPath(HelpPath.gridPath(50, winSize), paint);
    }


    /**
     * 获得屏幕可展示内容区域高度
     * 例如三星S10 分辨率为3040*1440
     * 底部栏隐藏清空下
     * 在竖屏情况下 屏幕宽为:1440,可用屏幕高为:2891,状态栏高度:150
     * 在横屏情况下 可用屏幕宽为:2891,屏幕高为:1440,状态栏高度:84  ？？？？ 为什么状态栏高度变化了
     * 正常情况下 可用屏幕高度+状态栏高度+导航栏高度 =  设备高像素
     *
     *
     * @param ctx 上下文
     * @param winSize 屏幕尺寸
     */
    public static void loadWinSize(Context ctx, Point winSize) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        DisplayMetrics realMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);//获取window屏幕可用的像素信息
            wm.getDefaultDisplay().getRealMetrics(realMetrics); //获取window屏幕真实的像素信息
        }
        int usableWidth = outMetrics.widthPixels;
        int usableHeight = outMetrics.heightPixels;
        int realWidth = realMetrics.widthPixels;
        winSize.x = usableWidth;
        winSize.y = usableHeight;

        int statusHeight = ScreenUtils.Companion.getStatusHeight(ctx);
        int statusHeight2 = StatusBarUtil.INSTANCE.getStatusBarHeight(ctx);
        Log.d(Constant.LOG_TAG,"statusHeight1:"+statusHeight+",statusHeight2:"+statusHeight2);
        Log.d(Constant.LOG_TAG,"屏幕宽为:"+winSize.x+",屏幕高为:"+winSize.y+",状态栏高度:"+statusHeight+",底部栏高度:"+getNavigationBarHeight(ctx));
    }

    public static boolean hasNavigationBar(Context ctx){
        int id = ctx.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && ctx.getResources().getBoolean(id);
    }

    public static int getNavigationBarHeight(Context ctx) {
        int navigationBarHeight = 0;
        try {
            Resources resources = ctx.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return navigationBarHeight;
    }


    /**
     * 获取状态栏的高度
     *
     * @param context 上下文
     * @return px
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 坐标系路径
     *
     * @param coo     坐标点
     * @param winSize 屏幕尺寸
     * @return 坐标系路径
     */
    public static Path cooPath(Point coo, Point winSize) {
        Path path = new Path();
        //x正半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(winSize.x, coo.y);
        //x负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x - winSize.x, coo.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, coo.y - winSize.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, winSize.y);
        return path;
    }

    /**
     * 绘制坐标系
     * @param canvas  画布
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     * @param paint   画笔
     */
    public static void drawCoo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //初始化网格画笔
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(null);
        //绘制直线
        canvas.drawPath(HelpPath.cooPath(coo, winSize), paint);
        //左箭头
        canvas.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y - 20, paint);
        canvas.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y + 20, paint);
        //下箭头
        canvas.drawLine(coo.x, winSize.y, coo.x - 20, winSize.y - 40, paint);
        canvas.drawLine(coo.x, winSize.y, coo.x + 20, winSize.y - 40, paint);
        //为坐标系绘制文字
        drawText4Coo(canvas, coo, winSize, paint);
    }
    /**
     * 为坐标系绘制文字
     *
     * @param canvas  画布
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     * @param paint   画笔
     */
    private static void drawText4Coo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //绘制文字
        paint.setTextSize(50);
        canvas.drawText("x", winSize.x - 60, coo.y - 40, paint);
        canvas.drawText("y", coo.x - 40, winSize.y - 60, paint);
        paint.setTextSize(25);
        //X正轴文字
        for (int i = 1; i < (winSize.x - coo.x) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x - 20 + 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x + 100 * i, coo.y, coo.x + 100 * i, coo.y - 10, paint);
        }
        //X负轴文字
        for (int i = 1; i < coo.x / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x - 20 - 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x - 100 * i, coo.y, coo.x - 100 * i, coo.y - 10, paint);
        }
        //y正轴文字
        for (int i = 1; i < (winSize.y - coo.y) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x + 20, coo.y + 10 + 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y + 100 * i, coo.x + 10, coo.y + 100 * i, paint);
        }
        //y负轴文字
        for (int i = 1; i < coo.y / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x + 20, coo.y + 10 - 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y - 100 * i, coo.x + 10, coo.y - 100 * i, paint);
        }
    }
}
