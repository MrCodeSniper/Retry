package com.mrcodesniper.restart.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.View

class CanvasView(context: Context) : View(context) {

    init {
        initView()
    }

    //网格画笔
    private var mGridPaint: Paint? = null

    //屏幕尺寸
    private var mWinSize: Point? = null

    //坐标系原点
    private var mCoo: Point? = null

    private fun initView() {
        mWinSize = Point()
        mCoo = Point(500, 500)
        HelpPath.loadWinSize(context, mWinSize)
        mGridPaint = Paint(Paint.ANTI_ALIAS_FLAG);
    }


    /**
     * 绘制方法是频繁触发的要避免 在内部创建对象
     * 在draw方法中触发
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas) //在绘制时获取画布
        HelpPath.drawGrid(canvas, mWinSize, mGridPaint)
        HelpPath.drawCoo(canvas, mCoo, mWinSize, mGridPaint);
    }

}