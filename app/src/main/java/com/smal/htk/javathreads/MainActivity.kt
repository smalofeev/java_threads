package com.smal.htk.javathreads

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private val uiHandler = Handler()
    private var generator: RandomCharacterGenerator? = null
    private val lock = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.text)

        val startBtn: Button = findViewById(R.id.start_btn)
        startBtn.setOnClickListener {
            startBtn.isEnabled = false
            startBtn.isClickable = false
            startBtn.isFocusable = false

            generator = synchronized(lock) {
                val generator = RandomCharacterGenerator()
                generator.addCharacterCallback {
                    charEvent ->
                    uiHandler.post {
                        text.text = "Please enter: ${charEvent.character}"
                    }
                }
                generator.start()
                generator
            }

        }

        val quitBtn: Button = findViewById(R.id.quit_btn)
        quitBtn.setOnClickListener {
            synchronized(lock) {
                generator?.interrupt()
            }

            startBtn.isEnabled = true
            startBtn.isClickable = true
            startBtn.isFocusable = true
        }

    }
}
