package com.example.flappybirdclone

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.random.Random


class Pipe(screenWidth: Int, screenHeight: Int) {
    var passed = false
    val topPipe: RectF
    val bottomPipe: RectF
    val pipeWidth: Int = PIPE_WIDTH
    private val gapHeight: Int
    var xPosition: Float
    private val speed: Float = 5f

    init {
        gapHeight = MIN_GAP + Random.nextInt(200) // Random gap height
        xPosition = screenWidth.toFloat()

        val gapStart = Random.nextInt(screenHeight - gapHeight - 200) + 100

        topPipe = RectF(xPosition, 0f, xPosition + pipeWidth, gapStart.toFloat())
        bottomPipe = RectF(
            xPosition,
            (gapStart + gapHeight).toFloat(),
            xPosition + pipeWidth,
            screenHeight.toFloat()
        )
    }

    fun update() {
        xPosition -= speed
        topPipe.left = xPosition
        topPipe.right = xPosition + pipeWidth
        bottomPipe.left = xPosition
        bottomPipe.right = xPosition + pipeWidth
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(topPipe, paint)
        canvas.drawRect(bottomPipe, paint)
    }

    val isOffScreen: Boolean
        get() = xPosition + pipeWidth < 0

    companion object {
        private const val MIN_GAP = 500 // Minimum gap between pipes
        private const val PIPE_WIDTH = 200 // Width of the pipe
    }
}

class PipeManager(private val screenWidth: Int, private val screenHeight: Int) {
    private val pipes = mutableListOf<Pipe>()
    private var lastPipeTime = System.currentTimeMillis()

    fun update() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPipeTime > PIPE_INTERVAL) {
            pipes.add(Pipe(screenWidth, screenHeight))
            lastPipeTime = currentTime
        }

        pipes.removeAll { it.isOffScreen }
        pipes.forEach { it.update() }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        pipes.forEach { it.draw(canvas, paint) }
    }

    fun getPipes(): List<Pipe> = pipes

    companion object {
        private const val PIPE_INTERVAL = 5000L
    }
}
