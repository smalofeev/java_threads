package com.smal.htk.javathreads

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private val uiHandler = Handler()
    private lateinit var generator: RandomCharacterGenerator
    private lateinit var scoreManager: ScoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.text)
        scoreManager = ScoreManager(findViewById(R.id.score_tv))

        generator = RandomCharacterGenerator()
        generator.addCharacterCallback {
            charEvent ->
            uiHandler.post {
                text.text = "Please enter: ${charEvent.character}"
            }
        }
        scoreManager.resetGenerator(generator)
        scoreManager.resetScore()


        val startBtn: Button = findViewById(R.id.start_btn)
        startBtn.setOnClickListener {
            startBtn.isEnabled = false
            startBtn.isClickable = false
            startBtn.isFocusable = false

            generator.setDone(false)
            scoreManager.resetScore()
        }

        val quitBtn: Button = findViewById(R.id.quit_btn)
        quitBtn.setOnClickListener {
            scoreManager.clear()
            generator.setDone(true)

            startBtn.isEnabled = true
            startBtn.isClickable = true
            startBtn.isFocusable = true
            text.text = "Start new game"
        }

    }
}
