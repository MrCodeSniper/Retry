package com.mrcodesniper.restart.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import com.mrcodesniper.restart.R


class CanvasView(context: Context) : View(context) {

    init {
        initView()
    }

    //网格画笔
    private var mGridPaint: Paint? = null

    //屏幕尺寸
    private var mWinSize: Point? = null

    //坐标系原点
    private lateinit var mCoo: Point

    private var mRedPaint: Paint? = null

    private fun initView() {
        mWinSize = Point()
        mRedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCoo = Point(500, 500)
        HelpPath.loadWinSize(context, mWinSize)
        mGridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }


    /**
     * 绘制方法是频繁触发的要避免 在内部创建对象
     * 在draw方法中触发
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas) //在绘制时获取画布
        //A = 10 B=11  C=12  D=13
        //绘制背景颜色 这里的255为十进制值 如果想绘制蓝色 #03DAC5 这里是16进制表示 两个16进制表示一个色素
        //通过转化后则为 48,218,197
        canvas?.drawRGB(48,218,197)
        //canvas?.rotate(45f) //画布旋转 之后的绘制都会在这个角度上旋转
        HelpPath.drawGrid(canvas, mWinSize, mGridPaint)
        HelpPath.drawCoo(canvas, mCoo, mWinSize, mGridPaint)
        //canvas通过画笔进行绘制 view作为我们代码编写的地方作为控制者调度绘制 实现view的自定义
        //canvas api规定了其拥有的能力 从图形来说 最基本需要具备 画点 线  平方体  立方体的能力
        if(canvas!=null) {
            if(mRedPaint!=null){
                drawPoint(canvas, mRedPaint!!)
                drawLine(canvas,mRedPaint!!)
                drawRect(canvas,mRedPaint!!)
                drawLikeCircle(canvas,mRedPaint!!)
                drawBitmap(canvas,mRedPaint!!)
                drawText(canvas,mRedPaint!!)
                drawPicture(canvas,mRedPaint!!)
                drawRotateRect(canvas,mRedPaint!!)
                drawTranslateAndRotateRect(canvas,mRedPaint!!)
                drawTranslateAndScaleRect(canvas,mRedPaint!!)
//                clip(canvas,mRedPaint!!,true)
//                clip(canvas,mRedPaint!!,false)
            }
        }
    }

    /**
     * 画布内外裁剪
     */
    private fun clip(canvas: Canvas,paint:Paint,isOutRect:Boolean){
        //原图
        paint.color = Color.parseColor("#880FB5FD")
        paint.strokeWidth = 30f
        canvas.drawRect(mCoo.x+0f, mCoo.y+0f, mCoo.x+200f, mCoo.y+300f, paint)
        //裁剪
        paint.color = Color.RED
        paint.strokeWidth = 30f
        val rect = Rect(mCoo.x+20,mCoo.y+100,mCoo.x+250,mCoo.y+300)
        if(isOutRect){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(rect) //确定外裁剪范围
            }
        }else{
            canvas.clipRect(rect) //确定内裁剪范围
        }
        //内裁剪后续绘制了图形在裁剪范围内的保留
        //外裁剪后续绘制了图形在裁剪范围外的保留
        canvas.drawRect(mCoo.x+0f, mCoo.y+0f, mCoo.x+200f, mCoo.y+300f, paint)
    }

    /**
     * 绘制点 坐标xy决定一个点的位置
     */
    private fun drawPoint(canvas: Canvas,paint:Paint){
        paint.color = Color.RED
        paint.strokeWidth = 30f
        canvas.drawPoint(mCoo.x+200f,mCoo.y-400f,paint)
        canvas.drawPoints(floatArrayOf(
            mCoo.x + 400f, mCoo.y - 400f,
            mCoo.x + 200f, mCoo.y - 200f,
            mCoo.x + 400f, mCoo.y - 200f,
        ),paint)
    }

    /**
     * 绘制线 startx starty endX endY  两点确定一条直线
     */
    private fun drawLine(canvas: Canvas,paint:Paint){
        paint.color = Color.RED
        paint.strokeWidth = 30f
        canvas.drawLine(mCoo.x+200f,mCoo.y-400f,mCoo.x+400f,mCoo.y-400f,paint)
        canvas.drawLine(mCoo.x+400f,mCoo.y-400f,mCoo.x+200f,mCoo.y-200f,paint)
        canvas.drawLine(mCoo.x+200f,mCoo.y-200f, mCoo.x+400f,mCoo.y-200f,paint)
    }


    /**
     * 绘制矩形 绘制圆角
     * 通过左上点和右下点确定位置
     * rf ry 为radius
     */
    private fun drawRect(canvas: Canvas,paint:Paint){
        paint.color = Color.RED
        paint.strokeWidth = 30f

        canvas.drawRect(mCoo.x+600f,mCoo.y-400f,mCoo.x+1000f,mCoo.y-200f,paint)

        canvas.drawRoundRect(mCoo.x+1100f,mCoo.y-400f,mCoo.x+1500f,mCoo.y-200f,20f,20f,paint)
    }

    /**
     * 嵌套图层操作
     */
    private fun drawTranslateAndScaleRect(canvas: Canvas,paint:Paint){
        //1.先将画布平移到坐标系原点 绘制矩形
        canvas.save()
        paint.color = Color.RED
        canvas.translate(mCoo.x.toFloat(), mCoo.y.toFloat())
        canvas.drawRect(-400f,400f,0f,600f,paint)

        canvas.save()
        paint.color = Color.parseColor("#880FB5FD")
        canvas.scale(0.5f,0.5f,-400f,400f)//根据中心点缩放2x
        canvas.drawRect(-400f,400f,0f,600f,paint)
        canvas.restore()

        canvas.restore()
    }

    /**
     * 嵌套图层操作
     */
    private fun drawTranslateAndRotateRect(canvas: Canvas,paint:Paint){
        //1.先将画布平移到坐标系原点 绘制矩形
        canvas.save()
        paint.color = Color.RED
        canvas.translate(mCoo.x.toFloat(), mCoo.y.toFloat())
        canvas.drawRect(1700f,-400f,2100f,-200f,paint)

        //2.在此基础上 旋转画布 绘制 旋转45度的矩形
        canvas.save()
        paint.color = Color.parseColor("#880FB5FD")
        canvas.rotate(45f,1900f,-300f)
        canvas.drawRect(1700f,-400f,2100f,-200f,paint)
        canvas.restore()
        //内部图层关闭

//        val canvasCount = canvas.saveCount //图层数量
//        canvas.restoreToCount(1)//直接恢复到第几个图层 类似出栈恢复一样


        canvas.restore() //外部图层关闭
    }


    /**
     * 绘制画布旋转之后的操作
     */
    private fun drawRotateRect(canvas: Canvas,paint:Paint){
        canvas.save() //保存画布状态 出现新的图层
        paint.color = Color.parseColor("#880FB5FD")
        paint.strokeWidth = 30f
        canvas.rotate(45f,mCoo.x+800f,mCoo.y-300f) //根据中心点旋转
        canvas.drawRect(mCoo.x+600f,mCoo.y-400f,mCoo.x+1000f,mCoo.y-200f,paint)
        canvas.restore() //画布还原 合并图层
    }

    /**
     * 绘制圆  通过中心点和半径确定圆
     * 绘制椭圆 通过上下左右4个坐标确定椭圆范围
     * 绘制圆弧
     */
    private fun drawLikeCircle(canvas: Canvas,paint:Paint){
        paint.color = Color.RED
        canvas.drawCircle(mCoo.x+300f,mCoo.y+300f,100f,paint)
        val rect = RectF(mCoo.x+600f, mCoo.y+200f, mCoo.x+1000f, mCoo.y+400f)
        canvas.drawOval(rect,paint)
        //绘制圆弧 rectf就是定义圆的范围  角度0~360 右边开始为0 往下旋  useCenter表示是否绘制出半径 为false即为旋转开始点到结束点的区域 true则包括圆心区域
        canvas.drawArc(mCoo.x+1100f,mCoo.y+100f,mCoo.x+1400f,mCoo.y+400f,0f,90f,false,paint)
    }

    /**
     * 绘制图片
     * 通过图片左上角摆放位置
     */
    private fun drawBitmap(canvas: Canvas,paint:Paint){
        //解码为bitmap加载到内存中
        val bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.logo)
        canvas.drawBitmap(bitmap,mCoo.x+1500f,mCoo.x+100f,paint)
        //2.适用变换矩阵绘制图片
        val matrix = Matrix()
        //错切可分为水平错切和垂直错切。
        //水平错切表示变换后，Y坐标不变，X坐标则按比例发生平移，且平移的大小和Y坐标成正比，即新的坐标为(X+Matrix[MSKEW_X] * Y,Y)。
        //垂直错切表示变换后，X坐标不变，Y坐标则按比例发生平移，且平移的大小和X坐标成正比，即新的坐标为(X,Y+Matrix[MSKEW_Y] * X)。
        //设置变换矩阵:缩小3倍，斜切0.5,右移150，下移100
        //matrix矩阵由9个float值构成，是一个3*3的矩阵
        //依此是
        //X轴缩放比例， X轴错切   ， 水平方向位移
        // Y轴错切     ，Y轴缩放比例    ， 垂直方向位移
        //
//        matrix.setValues(floatArrayOf(
//            1f, 0.5f, 1500 * 3f,
//            0f, 1f, 100 * 3f,
//            0f, 0f, 3f))
        //Matrix变换设置的时候，一定要注意顺序，不 同的顺序，会有不同的结果
        matrix.setTranslate(500f*2,500f*2) //缩放之后 位移需要添加倍数
        matrix.postScale(0.5f,0.5f)
//        matrix.preRotate(90f)

        canvas.drawBitmap(bitmap,matrix,paint)
        //fix xy 缩放的形式 在矩形区域存放图片
        val rectf = RectF(mCoo.x+200f,mCoo.x+500f,mCoo.x+600f,mCoo.y+900f)
        canvas.drawBitmap(bitmap,null,rectf,paint)


        //在rect2区域内存放 图片裁剪相对区域后的矩形图片 裁剪的区域为图片的相对区域
        val rect = Rect(bitmap.width/2, bitmap.height/2, bitmap.width, bitmap.height)
        val rectf2 = RectF(mCoo.x+700f,mCoo.x+500f,mCoo.x+1100f,mCoo.y+900f)
        canvas.drawBitmap(bitmap,rect,rectf2,paint)
    }

    /**
     * 拷贝存储画布内容 再粘贴
     */
    private fun drawPicture(canvas: Canvas,paint:Paint){
        val picture = Picture()
        //创建一个待拷贝的picture 往内部写入三个元素
        val recordingCanvas = picture.beginRecording(canvas.width,canvas.height)
        recordingCanvas.drawRect(100f, 0f, 200f, 100f, paint);
        recordingCanvas.drawRect(0f, 100f, 100f, 200f, paint);
        recordingCanvas.drawRect(200f, 100f, 300f, 200f, paint);
        picture.endRecording() //停止picture录入元素
        canvas.save() //每次调用save()函数，都会先保存当前画布的状态，然后将其放到特定的栈中
        canvas.drawPicture(picture) //在画布上绘制 这个复制的操作
        canvas.translate(0f,300f) //画布往下平移
//        picture.draw(canvas)
        canvas.drawPicture(picture) //再次绘制复制的动作
        canvas.restore() //每次调用restore()函数，都会把栈中的画布状态取处理，并按照这个状态恢复当前的画布，然后在这个画布上作画。
    }

    /**
     * 绘制文字
     */
    private fun drawText(canvas: Canvas,paint:Paint){
        paint.textSize = 100f
        paint.color = Color.RED
        paint.strokeWidth = 30f
        canvas.drawText("MrCodeSniper",mCoo.x-450f,mCoo.y+300f,paint)
    }
}