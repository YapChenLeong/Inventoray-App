package com.example.inventory.custom

import android.graphics.Canvas
import android.util.Log
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlin.math.cos
import kotlin.math.sin

class CustomPieChartRenderer(pieChart: PieChart, private val circleRadius: Float)
    : PieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler) {
    // Add properties to store valueLinePart1Length and valueLinePart2Length
    private var valueLinePart1Length: Float = 0.3f
    private var valueLinePart2Length: Float = 1.0f

    // Add methods to update valueLinePart1Length and valueLinePart2Length
    fun setValueLineLengths(part1Length: Float, part2Length: Float) {
        valueLinePart1Length = part1Length
        valueLinePart2Length = part2Length
    }

    override fun drawValues(c: Canvas) {
        super.drawValues(c)

        val center = mChart.centerCircleBox

        val radius = mChart.radius
        var rotationAngle = mChart.rotationAngle
        val drawAngles = mChart.drawAngles
        val absoluteAngles = mChart.absoluteAngles

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val roundedRadius = (radius - radius * mChart.holeRadius / 100f) / 2f
        val holeRadiusPercent = mChart.holeRadius / 100f
        var labelRadiusOffset = radius / 10f * 3.6f

        if (mChart.isDrawHoleEnabled) {
            labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2f
            if (!mChart.isDrawSlicesUnderHoleEnabled && mChart.isDrawRoundedSlicesEnabled) {
                rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius).toFloat()
            }
        }

        val labelRadius = radius - labelRadiusOffset

        var dataSets = mChart.data.dataSets
        var angle: Float
        var xIndex = 0

        c.save()
        for (i in dataSets.indices) {
            var dataSet = dataSets[i]
            val sliceSpace = getSliceSpace(dataSet)
            for (j in 0 until dataSet.entryCount) {
                    angle = if (xIndex == 0) 0f else absoluteAngles[xIndex - 1] * phaseX
                    val sliceAngle = drawAngles[xIndex]
                    val sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius)
                    angle += (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f

                if(j < 2){
                    if (dataSet.valueLineColor != ColorTemplate.COLOR_NONE) {
                        val transformedAngle = rotationAngle + angle * phaseY
                        val sliceXBase = cos(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
                        val sliceYBase = sin(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
                        val valueLinePart1OffsetPercentage = dataSet.valueLinePart1OffsetPercentage / 100f
                        val line1Radius = if (mChart.isDrawHoleEnabled) {
                            (radius - radius * holeRadiusPercent) * valueLinePart1OffsetPercentage + radius * holeRadiusPercent
                        } else {
                            radius * valueLinePart1OffsetPercentage
                        }
                        val px = line1Radius * sliceXBase + center.x
                        val py = line1Radius * sliceYBase + center.y

                        if (dataSet.isUsingSliceColorAsValueLineColor) {
                            mRenderPaint.color = dataSet.getColor(j)
                        }


                        // Set the valueLinePart1Length and valueLinePart2Length
                        valueLinePart1Length = if (j < 2) 0.7f else 0f
                        valueLinePart2Length = if (j < 2) 0.3f else 0f

                        c.drawCircle(px, py, circleRadius, mRenderPaint)

                        drawValueLine(
                            c,
                            dataSet.valueLineColor,
                            circleRadius,
                            sliceXBase, // x-coordinate of the slice center
                            sliceYBase, // y-coordinate of the slice center
                            angle
                        )
                    }
                }


                xIndex++
            }
        }
        MPPointF.recycleInstance(center)
        c.restore()
    }
    private fun drawValueLine(
        c: Canvas,
        color: Int,
        circleRadius: Float,
        x: Float,
        y: Float,
        angle: Float
    ) {
        if (angle == 0f) return

        val lineX = circleRadius * valueLinePart1Length * cos(angle * Utils.FDEG2RAD.toDouble()).toFloat() + x
        val lineY = circleRadius * valueLinePart1Length * sin(angle * Utils.FDEG2RAD.toDouble()).toFloat() + y

        mRenderPaint.color = color
        mRenderPaint.strokeWidth = 2f
        c.drawLine(x, y, lineX, lineY, mRenderPaint)

        if (valueLinePart2Length > 0) {
            val valueLinePart2Factor = 0.5f
            val line2X = circleRadius * (valueLinePart1Length + valueLinePart2Length) * cos(angle * Utils.FDEG2RAD.toDouble()).toFloat() + x
            val line2Y = circleRadius * (valueLinePart1Length + valueLinePart2Length) * sin(angle * Utils.FDEG2RAD.toDouble()).toFloat() + y

            c.drawLine(lineX, lineY, line2X, line2Y, mRenderPaint)
        }
    }
}