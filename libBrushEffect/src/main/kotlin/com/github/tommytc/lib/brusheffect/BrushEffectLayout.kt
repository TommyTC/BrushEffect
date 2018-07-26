package com.github.tommytc.lib.brusheffect

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
//    ╔╦╗╔═╗╔╦╗╔╦╗╦ ╦  2018/7/26
//     ║ ║ ║║║║║║║╚╦╝  com.android.tommy@gmail.com
//     ╩ ╚═╝╩ ╩╩ ╩ ╩   ═══════════════════════════
 */
const val ORIENTATION_HORIZONTAL = 0xF1
const val ORIENTATION_VERTICAL = 0xF2

class BrushEffectLayout : FrameLayout {
    var isReverse = false
    var endColor = 0
    var startColor = 0
    var orientation = ORIENTATION_HORIZONTAL
    var listener:Listener? = null
    var duration: Long = 600
    var interpolator: Interpolator
    private lateinit var animator: ValueAnimator
    private var colorAnimator: ValueAnimator? = null
    private var paint: Paint
    private var progress = 0f
    private var centerPoint = 0f
    private var currentForwadPosition = 0f
    private var currentBackPosition = 0f
    private var strokeWidth = 2.0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        paint = Paint()
        paint.color = startColor
        paint.isAntiAlias = true
        paint.strokeWidth = 100f
        paint.strokeCap = Paint.Cap.SQUARE
        paint.style = Paint.Style.FILL_AND_STROKE

        interpolator = AccelerateDecelerateInterpolator()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        println("B:${currentBackPosition}\tF:${currentForwadPosition}")
        when (orientation) {
            ORIENTATION_HORIZONTAL -> run {
                canvas.drawLine(currentBackPosition, centerPoint, currentForwadPosition, centerPoint, paint)
            }
            ORIENTATION_VERTICAL -> run {
                canvas.drawLine(centerPoint, currentBackPosition,centerPoint,currentForwadPosition, paint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        when (orientation) {
            ORIENTATION_HORIZONTAL -> run {
                centerPoint = h * 0.5f
                paint.strokeWidth = h.toFloat() * strokeWidth
                animator = ValueAnimator.ofFloat(paint.strokeWidth*-2f, w.toFloat()+paint.strokeWidth)
            }
            ORIENTATION_VERTICAL -> run {
                centerPoint = w * 0.5f
                paint.strokeWidth = w.toFloat() * strokeWidth
                animator = ValueAnimator.ofFloat(paint.strokeWidth*-2f, h.toFloat()+paint.strokeWidth)
            }
        }
    }

    fun brush() {
        if (animator.isRunning)
            return
        val orientationValue = if (orientation== ORIENTATION_HORIZONTAL )width.toFloat() else height.toFloat()
        currentBackPosition = if (isReverse) orientationValue else paint.strokeWidth*-2f
        currentForwadPosition = currentBackPosition
        animator.removeAllListeners()
        animator.addListener(AnimationEvent({animator ->
            listener?.onStart()
        }){animator ->
            listener?.onCover()
            colorAnimator?.pause()
        })
        animator.removeAllUpdateListeners()
        animator.addUpdateListener { animator ->
            progress = animator.animatedFraction
            if (!isReverse)
                currentForwadPosition = animator.animatedValue as Float
            else
                currentForwadPosition = currentBackPosition - animator.animatedValue as Float
            invalidate()
        }
        if (startColor!=endColor)
            colorAnimator = ValueAnimator.ofArgb(startColor,endColor).apply {
                duration = animator.duration*2
                interpolator = animator.interpolator
                addUpdateListener { animator ->
                    paint.color = animator.animatedValue as Int
                }
                start()
            }
        else
            paint.color = startColor

        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    fun hide() {
        val orientationValue = if (orientation== ORIENTATION_HORIZONTAL )width.toFloat() else height.toFloat()
        currentBackPosition = if (isReverse) orientationValue else paint.strokeWidth*-2f
        animator.removeAllListeners()
        animator.addListener(AnimationEvent({animator ->
            colorAnimator?.resume()
        }){animator ->
            listener?.onFinish()
        })
        animator.removeAllUpdateListeners()
        animator.addUpdateListener { animator ->
            progress = animator.animatedFraction
            if (!isReverse)
                currentBackPosition = animator.animatedValue as Float
            else
                currentBackPosition = orientationValue - animator.animatedValue as Float
            invalidate()
        }

        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    interface Listener {
        fun onStart()
        fun onCover()
        fun onFinish()
    }

    class AnimationEvent(val onStart: ((animator: Animator?) -> Unit)? = null, val onEnd: (animator: Animator?) -> Unit) : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator?) {
            onEnd(p0)
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationStart(p0: Animator?) {
            onStart?.invoke(p0)
        }
    }

}
