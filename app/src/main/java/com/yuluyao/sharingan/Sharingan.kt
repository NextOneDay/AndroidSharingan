package com.yuluyao.sharingan

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator

/**
 * 三勾玉写轮眼
 */
open class Sharingan : Eye {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

/*
  init {
    setOnClickListener {
      disappearSharingan().start()
    }
  }
*/

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    gouRadius = mRadius * 0.125f
    centerRadius = mRadius * 0.15f
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.save()
    canvas.translate(centerCoordinate[0], centerCoordinate[1])

    // center dot
    paint.color = Color.BLACK
    drawCenterDot(canvas)

    // middle ring
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = mRadius * 0.03f
    paint.color = middleRingColor
    val middleRingRadius = mRadius * 0.6f
    drawMiddleRing(canvas, middleRingRadius)

    // gou
    paint.color = Color.BLACK
    paint.style = Paint.Style.FILL
    drawGou(canvas, middleRingRadius)

    canvas.restore()
  }


  private fun drawCenterDot(canvas: Canvas) {
    canvas.drawCircle(0f, 0f, centerRadius, paint)
  }

  var centerRadius = 0F
    set(value) {
      field = value
      invalidate()
    }


  private fun drawMiddleRing(canvas: Canvas, middleRadius: Float) {
    canvas.drawCircle(0f, 0f, middleRadius, paint)
  }

  var middleRingColor = 0x55000000
    set(value) {
      field = value
      invalidate()
    }

  private fun drawGou(canvas: Canvas, middleRingRadius: Float) {
    for (degree in 0 until 360 step 120) {
      canvas.save()
      canvas.rotate(degree.toFloat())

      // 画勾玉
      canvas.drawPath(buildGouPath(middleRingRadius), paint)
      canvas.restore()
    }
  }

  var gouRadius = 0F
    set(value) {
      field = value
      invalidate()
    }

  private val gouPath = Path()
  private fun buildGouPath(middleRingRadius: Float): Path {
    gouPath.reset()
    gouPath.addCircle(middleRingRadius, 0f, gouRadius, Path.Direction.CW)
    val rectF = RectF(middleRingRadius - gouRadius * 2, 0 - gouRadius, middleRingRadius + gouRadius * 2, 0 + gouRadius * 3)
    gouPath.arcTo(rectF, -90f, 90f)
    val rectF2 = RectF(middleRingRadius, 0f, middleRingRadius + gouRadius * 2, 0 + gouRadius * 2)
    gouPath.arcTo(rectF2, 0f, -90f)
    return gouPath
  }

  protected fun disappearSharingan(): Animator {
    val animGou = ObjectAnimator.ofFloat(this, "gouRadius", 0f)
    animGou.interpolator = AccelerateInterpolator()
    val animCenterDot = ObjectAnimator.ofFloat(this, "centerRadius", 0f)
    animCenterDot.interpolator = AccelerateInterpolator()
    val animMiddleRing = ObjectAnimator.ofInt(this, "middleRingColor", Color.TRANSPARENT)
    animMiddleRing.setEvaluator(ArgbEvaluator())
    animMiddleRing.interpolator = AccelerateInterpolator()
    val disappearSet = AnimatorSet()
    disappearSet.playTogether(
      animGou,
      animCenterDot,
      animMiddleRing
    )
    disappearSet.duration = 500
    return disappearSet
  }

  protected open fun reset() {
    gouRadius = mRadius * 0.125f
    centerRadius = mRadius * 0.15f
    middleRingColor = 0x55000000
    invalidate()
  }
}