package com.zhumj.formview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View
import android.text.TextPaint
import java.util.*


/**
 * @author Created by zhumj
 * @date on 2017/9/6
 * @function 绘制表格（两列多行）
 */
class FormView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // 表格行数
    private var rowNum = 0
    // 表格列数
    private var colNum = 2
    //表格边框宽度
    private var strokeWidth = 2f
    //表格边框颜色
    private var strokeColor = Color.BLACK
    // 字符大小
    private var textSize = 35
    // 字符边距
    private var textPadding = 12
    //表格数据
    private var mDataList = ArrayList<String>()

    init {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.FormView)
        this.rowNum = ta.getInt(R.styleable.FormView_rowNum, 0)
        this.colNum = ta.getInt(R.styleable.FormView_colNum, 2)
        this.textSize = ta.getDimension(R.styleable.FormView_textSize, 35f).toInt()
        this.textPadding = ta.getDimension(R.styleable.FormView_textPadding, 12f).toInt()
        this.strokeWidth = ta.getFloat(R.styleable.FormView_strokeWidth, 2f)
        this.strokeColor = ta.getColor(R.styleable.FormView_strokeColor, Color.BLACK)
        ta.recycle()
    }

    /**
     * 设置表格数据
     */
    fun setDataList(dataList: ArrayList<String>) {
        this.mDataList.clear()
        this.mDataList.addAll(dataList)
        this.rowNum = if (mDataList.size % colNum == 0) mDataList.size/colNum else mDataList.size/colNum + 1
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //计算总高度
        var overHeight = 0f
        for (i in 0 until rowNum){
            val strArray = ArrayList<String>()
            (0 until colNum)
                    .filter { (i * colNum + it) < mDataList.size }
                    .mapTo(strArray) { mDataList[i * colNum + it] }
            //记录最后一行最底部位置
            overHeight += getRowHeight(strArray)
        }
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), overHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mDataList.size > 0){
            drawForm(canvas)
        }
    }

    /**
     * 画表格
     */
    private fun drawForm(canvas: Canvas?) {
        val formPaint = Paint()
        formPaint.color = strokeColor
        formPaint.style = Paint.Style.STROKE
        formPaint.strokeWidth = strokeWidth

        var cellX: Float
        var cellY: Float
        var cellBX: Float
        var cellBY: Float
        //最后一行最底部Y坐标
        var overHeight = 0f

        for (i in 0 until rowNum){
            val strArray = ArrayList<String>()
            (0 until colNum)
                    .filter { (i * colNum + it) < mDataList.size }
                    .mapTo(strArray) { mDataList[i * colNum + it] }

            cellY = overHeight
            //此行表格高度
            cellBY = overHeight + getRowHeight(strArray)

            for (k in 0 until colNum) {
                if ((i * colNum + k) < mDataList.size) {
                    cellX = (width/colNum*k).toFloat()
                    cellBX = (width/colNum*(k+1)).toFloat()

                    //画表格
                    canvas?.drawRect(cellX, cellY, cellBX, cellBY, formPaint)
                    //画文字
                    drawText(canvas, cellX, cellY, mDataList[i * colNum + k])
                }
            }

            //记录最后一行最底部位置
            overHeight = cellBY
        }
    }

    /**
     * 画文字
     */
    private fun drawText(canvas: Canvas?, cellX: Float, cellY: Float, text: String) {
        val textPaint = TextPaint()
        textPaint.color = Color.parseColor("#000000")
        textPaint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.textSize = textSize.toFloat()
        textPaint.isAntiAlias = true

        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        val layout = StaticLayout(text, textPaint, width/2 - textPadding*2,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true)

        canvas?.save()
        //从X，Y开始画
        canvas?.translate(cellX + textPadding, cellY + textPadding)
        layout.draw(canvas)
        canvas?.restore()
    }

    /**
     * 计算出文字的总长度和每个字的高度
     */
    private fun getTextWidthAndHeight(text: String): IntArray {
        val paint = Paint()
        paint.textSize = textSize.toFloat()
        val textWidth = paint.measureText(text).toInt()
        val fm = paint.fontMetrics
        val textHeight = Math.ceil((fm.descent-fm.ascent).toDouble()).toInt()
        return intArrayOf(textWidth, textHeight)
    }

    /**
     * 计算每一行最大高度
     */
    private fun getRowHeight(strList: ArrayList<String>): Float {
        var mFormRowHeight = 0f
        for (text in strList) {
            val textWidth = getTextWidthAndHeight(text)[0]
            val textHeight = getTextWidthAndHeight(text)[1]
            val formRowHeight: Float = if (textWidth <= measuredWidth/colNum - textPadding*2) {
                (textPadding*2 + textHeight).toFloat()
            } else {
                val num: Int = textWidth / (measuredWidth/colNum - textPadding*2)
                if (textWidth % (measuredWidth/colNum - textPadding*2) == 0) {
                    (textPadding*2 + textHeight * num).toFloat()
                } else {
                    (textPadding*2 + textHeight * (num + 1)).toFloat()
                }
            }
            if (mFormRowHeight < formRowHeight) {
                mFormRowHeight = formRowHeight
            }
        }

        return mFormRowHeight
    }

}