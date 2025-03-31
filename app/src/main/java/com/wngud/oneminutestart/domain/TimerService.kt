package com.wngud.oneminutestart.domain

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.wngud.oneminutestart.MyApplication.Companion.TIMER_SERVICE_CHANNEL_ID
import com.wngud.oneminutestart.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {
    private val NOTIFICATION_ID = 1
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning: Boolean = false
    private var timerJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "START_TIMER" -> startTimer(it.getLongExtra("time", 0))
                "PAUSE_TIMER" -> pauseTimer()
                "RESUME_TIMER" -> resumeTimer()
                "STOP_TIMER" -> stopTimer()
            }
        }
        return START_STICKY
    }

    private fun startTimer(time: Long) {
        timeLeftInMillis = time
        isTimerRunning = true

        if (timerJob == null) {
            startForeground(NOTIFICATION_ID, createNotification())
            timerJob = CoroutineScope(Dispatchers.Main).launch {
                while (timeLeftInMillis > 0 && isTimerRunning) {
                    delay(1000)
                    if (!isTimerRunning) {
                        break
                    }
                    timeLeftInMillis -= 1000
                    updateNotification()
                }
                if (timeLeftInMillis <= 0) {
                    stopSelf()
                }
                timerJob = null
            }
        } else {
            updateNotification()
        }
    }

    private fun pauseTimer() {
        isTimerRunning = false
        updateNotification()
    }

    private fun resumeTimer() {
        isTimerRunning = true
        updateNotification()
        startTimer(timeLeftInMillis)
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        isTimerRunning = false
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
        val seconds = (timeLeftInMillis / 1000) % 60
        val minutes = (timeLeftInMillis / 1000) / 60
        val contentText = if (minutes > 0) {
            "남은 시간: ${minutes}분 ${seconds}초"
        } else {
            "남은 시간: ${seconds}초"
        }
        val builder = NotificationCompat.Builder(this, TIMER_SERVICE_CHANNEL_ID)
            .setContentTitle("타이머")
            .setContentText(contentText)
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TimerService", "서비스 종료")
    }
}