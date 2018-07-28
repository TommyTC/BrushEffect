package com.github.tommytc.lib.brusheffect

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import java.lang.ref.SoftReference

/**
//    ╔╦╗╔═╗╔╦╗╔╦╗╦ ╦  2018/7/26
//     ║ ║ ║║║║║║║╚╦╝  com.android.tommy@gmail.com
//     ╩ ╚═╝╩ ╩╩ ╩ ╩   ═══════════════════════════
 */
const val ORIENTATION_HORIZONTAL = 0
const val ORIENTATION_VERTICAL = 1

class BrushEffectLayout : FrameLayout {
    var isReverse = false
    /**
     * @endColor will apply on the animation finish
     * if strokeWidth is 1.0 than the paint stroke will be this layout width or height.
     */
    var endColor = 0xFF0099CC.toInt()
    /**
     * @startColor will apply on the animation start
     * if startColor equals endColor than the animation has not color effect
     */
    var startColor = 0xFF0099CC.toInt()
        set(value) {
            field = value
            endColor = value
        }
    var orientation = ORIENTATION_HORIZONTAL
    var listener: Listener? = null
    var duration: Long = 300
    var inInterpolator: Interpolator
    var outInterpolator: Interpolator
    /**
     * @strokeWidth is a scale value,is not a absolute value.
     * if strokeWidth is 1.0 than the paint stroke will be this layout width or height.
     */
    var strokeWidth = 1.0f
    var strokeCap = Paint.Cap.BUTT
        set(value) {
            field = value
            paint.strokeCap = value
        }
    private var colorAnimator: ValueAnimator? = null
    private var paint: Paint
    private var progress = 0f
    private var centerPoint = 0f
    private var currentForwadPosition = 0f
    private var currentBackPosition = 0f
    private var isAnimatorRunning = false

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initializeView(context, attrs)
    }

    init {
        paint = Paint()
        paint.color = startColor
        paint.isAntiAlias = true
        paint.strokeWidth = 1f
        paint.strokeCap = strokeCap
        paint.style = Paint.Style.FILL_AND_STROKE

        inInterpolator = AccelerateInterpolator()
        outInterpolator = DecelerateInterpolator()
    }

    private fun initializeView(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.BrushEffectLayout).run {
            orientation = getInt(R.styleable.BrushEffectLayout_bel_orientation, ORIENTATION_HORIZONTAL)
            strokeWidth = getFloat(R.styleable.BrushEffectLayout_bel_strokeWidth, strokeWidth)
            isReverse = getBoolean(R.styleable.BrushEffectLayout_bel_reverse, false)
            startColor = getInt(R.styleable.BrushEffectLayout_bel_startColor, startColor)
            endColor = getInt(R.styleable.BrushEffectLayout_bel_endColor, endColor)
            duration = getInt(R.styleable.BrushEffectLayout_bel_duration, 300).toLong()
            strokeCap = when (getInt(R.styleable.BrushEffectLayout_bel_strokeCap, 0)) {
                0 -> Paint.Cap.BUTT
                1 -> Paint.Cap.ROUND
                else -> Paint.Cap.SQUARE
            }


            paint.color = startColor
            paint.strokeCap = strokeCap
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        println("B:${currentBackPosition}\tF:${currentForwadPosition}")
        when (orientation) {
            ORIENTATION_HORIZONTAL -> run {
                canvas.drawLine(currentBackPosition, centerPoint, currentForwadPosition, centerPoint, paint)
            }
            ORIENTATION_VERTICAL -> run {
                canvas.drawLine(centerPoint, currentBackPosition, centerPoint, currentForwadPosition, paint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        when (orientation) {
            ORIENTATION_HORIZONTAL -> run {
                centerPoint = h * 0.5f
                paint.strokeWidth = h.toFloat() * strokeWidth
            }
            ORIENTATION_VERTICAL -> run {
                centerPoint = w * 0.5f
                paint.strokeWidth = w.toFloat() * strokeWidth
            }
        }
    }

    fun brush() {
        brush(0)
    }

    fun brush(delay: Long) {
        if (isAnimatorRunning)
            return
        getAnimator().get()?.let { animator ->
            val orientationValue = if (orientation == ORIENTATION_HORIZONTAL) width.toFloat() else height.toFloat()
            currentBackPosition = if (isReverse) orientationValue + paint.strokeWidth else paint.strokeWidth * -2f
            currentForwadPosition = currentBackPosition

            animator.addListener(AnimationEvent({ _ ->
                isAnimatorRunning = true
                listener?.onStart()
            }) { _ ->
                listener?.onCover()
            })
            animator.addUpdateListener { animator ->
                progress = animator.animatedFraction
                if (!isReverse)
                    currentForwadPosition = animator.animatedValue as Float
                else
                    currentForwadPosition = currentBackPosition - animator.animatedValue as Float - paint.strokeWidth
                invalidate()
            }

            if (startColor != endColor)
                colorAnimator = getColorAnimator().get()?.apply { start() }
            else
                paint.color = startColor

            animator.interpolator = this@BrushEffectLayout.inInterpolator
            animator.duration = this@BrushEffectLayout.duration
            animator.startDelay = delay
            animator.start()
        }

    }

    fun hide() {
        getAnimator().get()?.let { animator ->
            val orientationValue = if (orientation == ORIENTATION_HORIZONTAL) width.toFloat() else height.toFloat()
            currentBackPosition = if (isReverse) orientationValue else paint.strokeWidth * -2f
            animator.addListener(AnimationEvent { _ ->
                listener?.onFinish()
                isAnimatorRunning = false
            })
            animator.addUpdateListener { animator ->
                progress = animator.animatedFraction
                if (!isReverse)
                    currentBackPosition = animator.animatedValue as Float
                else
                    currentBackPosition = orientationValue - animator.animatedValue as Float
                invalidate()
            }

            animator.interpolator = outInterpolator
            animator.duration = duration
            animator.start()
        }
    }

    private fun getAnimator(): SoftReference<ValueAnimator> {
        var animator: ValueAnimator = when (orientation) {
            ORIENTATION_HORIZONTAL -> run {
                centerPoint = height * 0.5f
                paint.strokeWidth = height.toFloat() * strokeWidth
                ValueAnimator.ofFloat(paint.strokeWidth * -2f, width.toFloat() + paint.strokeWidth)
            }
            else -> run {
                centerPoint = width * 0.5f
                paint.strokeWidth = width.toFloat() * strokeWidth
                ValueAnimator.ofFloat(paint.strokeWidth * -2f, height.toFloat() + paint.strokeWidth)
            }
        }
        return SoftReference(animator)
    }

    private fun getColorAnimator(): SoftReference<ValueAnimator> {
        return SoftReference(ValueAnimator().apply {
            setIntValues(startColor, endColor)
            setEvaluator(ArgbEvaluator())
            duration = this@BrushEffectLayout.duration * 2
            interpolator = this@BrushEffectLayout.inInterpolator
            addUpdateListener { animator: ValueAnimator? ->
                animator?.run {
                    paint.color = animatedValue as Int
                }
            }
        })
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
