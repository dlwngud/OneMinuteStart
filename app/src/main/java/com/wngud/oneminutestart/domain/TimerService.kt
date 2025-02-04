package com.wngud.oneminutestart.domain

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.NotificationCompat
import com.wngud.oneminutestart.MyApplication.Companion.TIMER_SERVICE_CHANNEL_ID
import com.wngud.oneminutestart.R

class TimerService : Service() {
    private val NOTIFICATION_ID = 1
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 60000 // 1분
    private var isTimerRunning: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "START_TIMER" -> startTimer()
                "PAUSE_TIMER" -> pauseTimer()
                "RESUME_TIMER" -> resumeTimer()
                "STOP_TIMER" -> stopTimer()
            }
        }
        return START_STICKY
    }

    private fun startTimer() {
        Log.d("TimerService", "startTimer")
        isTimerRunning = true
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateNotification()
            }

            override fun onFinish() {
                isTimerRunning = false
                stopForeground(true)
                stopSelf()
            }
        }.start()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
            updateNotification()
        }
    }

    private fun resumeTimer() {
        if (!isTimerRunning) {
            startTimer()
        }
    }

    private fun stopTimer() {
        countDownTimer.cancel()
        stopForeground(true)
        stopSelf()
    }

    private fun updateNotification() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        val dismissedIntent = Intent("DISMISSED_ACTION")
        dismissedIntent.setPackage(packageName) // This is required on Android 14
        val dismissedPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            dismissedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val builder = NotificationCompat.Builder(this, TIMER_SERVICE_CHANNEL_ID)
            .setContentTitle("타이머")
            .setContentText("남은 시간: ${timeLeftInMillis / 1000}초")
            .setDeleteIntent(dismissedPendingIntent)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .addAction(R.drawable.baseline_timer_24, "종료", getPendingIntent("STOP_TIMER"))

        if (isTimerRunning) {
            builder.addAction(R.drawable.baseline_timer_24, "일시정지", getPendingIntent("PAUSE_TIMER"))
        } else {
            builder.addAction(R.drawable.baseline_timer_24, "재시작", getPendingIntent("RESUME_TIMER"))
        }

        return builder.build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, TimerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    override fun onBind(intent: Intent?) = null
}