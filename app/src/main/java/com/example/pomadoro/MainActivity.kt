package com.example.pomadoro

import android.annotation.SuppressLint
import android.os.Bundle
import android.media.MediaPlayer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.os.CountDownTimer




class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView

    private lateinit var sessionNumber: TextView

    private var minutes: Long = 0
    private var seconds: Long = 0
    private var timeLeftInMillis: Long = minutes * 60 * 1000
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false

    private var working = false

    private var cycle: Boolean = false

    private var isPause: Boolean = false

    private var sessionCount = 1


    private fun chillTimer() {
        cycle = true
        minutes = 5
        seconds = 0
        timeLeftInMillis = minutes * 60 * 1000
        startTimer()
        minutes = 25
        seconds = 0


    }

    private fun workTimer() {

        cycle = false
        minutes = 25
        seconds = 0
        timeLeftInMillis = minutes * 60 * 1000
        startTimer()
        working == true
        minutes = 5
        seconds = 0

    }

    private fun startTimer() {

        if (!isTimerRunning) { // запускаем только если НЕ запущен

            sessionNumber.text = "SESSION $sessionCount"
            countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    val timeLeft = String.format("%02d:%02d", minutes, seconds)
                    timerTextView.text = timeLeft
                }

                override fun onFinish() {
                    isTimerRunning = false
                    timerTextView.text = "00:00"

                    if (cycle == true) {
                        sessionCount++
                        sessionNumber.text = "SESSION $sessionCount"
                        working == true
                    }
                }
            }.start()

            isTimerRunning = true
        }
    }






    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        timerTextView = findViewById(R.id.timerTextView)
        val startSound = MediaPlayer.create(this, R.raw.start)
        val pauseSound = MediaPlayer.create(this, R.raw.pause)
        val startButton: Button = findViewById(R.id.StartButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val resetButton: Button = findViewById(R.id.resetButton)
        val skipButton: Button = findViewById (R.id.skipButton)
        sessionNumber = findViewById(R.id.sessionNumber)


        fun startProgram(){
            if (isPause == true){
                startTimer()
                isPause = false
            } else {
                if (working == false) {
                    workTimer()
                } else {
                    chillTimer()
                }
            }
        }

        startButton.setOnClickListener {
            startSound.start()
            if (!isTimerRunning) {
                startProgram()
            }


        }


        pauseButton.setOnClickListener {
            pauseSound.start()
            if (isTimerRunning) {
                countDownTimer?.cancel()
                isTimerRunning = false
                isPause = true
            }
        }

        resetButton.setOnClickListener {
            working = false
            cycle = false
            minutes = 25
            seconds = 0
            countDownTimer?.cancel()
            isTimerRunning = false
            timeLeftInMillis = minutes * 60 * 1000
            timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            sessionCount = 1
            sessionNumber.text = "SESSION"
        }

        skipButton.setOnClickListener {
            isTimerRunning = false
            timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            if (cycle == true){
                sessionCount++
                sessionNumber.text = "SESSION $sessionCount"
                working == true

            }

            countDownTimer?.cancel()
            isTimerRunning = false
            working = !working
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }


    }


}
