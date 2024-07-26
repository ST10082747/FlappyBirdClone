package com.example.flappybirdclone

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), Runnable {
    private lateinit var gameThread: Thread
    var isRunning = false
    private lateinit var pipeManager: PipeManager
    private lateinit var bird: Bird
    private val backgroundPaint = Paint()
    private val birdPaint = Paint()



    private var isGameOver = false
    private val gameOverPaint = Paint().apply {
        color = Color.RED
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }

    init {
        //backgroundPaint.color = Color.CYAN
        birdPaint.color = Color.YELLOW
        pipeManager = PipeManager(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
        bird = Bird(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)

    }


    private var score = 0
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.LEFT
    }

    private fun updateScore() {
        pipeManager.getPipes().forEach { pipe ->
            if (bird.x > pipe.xPosition + pipe.pipeWidth && !pipe.passed) {
                score++
                pipe.passed = true
            }
        }
    }

    private fun drawScore(canvas: Canvas) {
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)
    }


    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()

            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

            val pipePaint = Paint()
            pipePaint.color = Color.GREEN
            pipeManager.draw(canvas, pipePaint)

            bird.draw(canvas, birdPaint)
            drawScore(canvas)

            if (isGameOver) {
                canvas.drawText("Game Over", width / 2f, height / 2f, gameOverPaint)
                canvas.drawText("Score: $score", width / 2f, height / 2f + 100f, gameOverPaint)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun update() {
        pipeManager.update()
        bird.update()
        updateScore()
        checkCollision()
    }

    private fun checkCollision() {
        // Check collision with pipes
        for (pipe in pipeManager.getPipes()) {
            if (bird.getBounds().intersect(pipe.topPipe) ||
                bird.getBounds().intersect(pipe.bottomPipe)) {
                gameOver()
                return
            }
        }

        // Check if bird is out of screen bounds
        if (bird.y < 0 || bird.y > height) {
            gameOver()
        }
    }

    private fun gameOver() {
        isGameOver = true
        isRunning = false
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            gameThread = Thread(this)
            gameThread.start()
        }
    }

    fun stop() {
        isRunning = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        start()
    }

    fun pause() {
        stop()
    }


    override fun run() {
        while (isRunning) {
            if (!isGameOver) {
                update()
            }
            draw()
            sleep()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(16)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isGameOver) {
                    bird.jump()
                } else {
                    // Reset the game
                    resetGame()
                }
            }
        }
        return true
    }

    private fun resetGame() {
        isGameOver = false
        score = 0
        bird = Bird(width, height)
        pipeManager = PipeManager(width, height)
        start()
    }
}
