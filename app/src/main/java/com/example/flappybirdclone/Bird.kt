package com.example.flappybirdclone

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Bird(screenWidth: Int, screenHeight: Int) {
    var x: Float
    var y: Float
    private val size: Float = 50f  // Bird size
    private var velocity: Float = 0f
    private val gravity: Float = 0.6f
    private val jumpVelocity: Float = -15f

    private val bounds: RectF

    init {
        x = screenWidth / 3f
        y = screenHeight / 2f
        bounds = RectF(x, y, x + size, y + size)
    }

    fun update() {
        velocity += gravity
        y += velocity
        updateBounds()
    }

    fun jump() {
        velocity = jumpVelocity
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(bounds, paint)
    }

    private fun updateBounds() {
        bounds.set(x, y, x + size, y + size)
    }

    fun getBounds(): RectF = bounds

}