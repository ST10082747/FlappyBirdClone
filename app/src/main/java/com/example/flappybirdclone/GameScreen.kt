package com.example.flappybirdclone

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameScreen : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_screen)

        gameView = findViewById(R.id.gameView)
        gameView.start()

        button = findViewById(R.id.pauseButton)

        button.setOnClickListener {
            if (gameView.isRunning) {
                gameView.stop()
              button.text = "Resume"
            } else {
                gameView.start()
                button.text = "Pause"
            }
        }

    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

}