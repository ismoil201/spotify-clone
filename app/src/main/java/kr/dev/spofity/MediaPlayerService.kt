package kr.dev.spofity

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MediaPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic = 0
    private var isPlaying = false
    private val handler = Handler()

    companion object {
        const val CHANNEL_ID = "music_channel"
        const val NOTIF_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Music Player",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> togglePlayPause()
            MEDIA_PLAYER_NEXT -> nextMusic()
            MEDIA_PLAYER_BACK -> previousMusic()
            "CLOSE" -> stopSelf()
            "SEEK_TO" -> {
                val position = intent.getIntExtra("seekPosition", 0)
                mediaPlayer?.seekTo(position)
            }
        }
        return START_STICKY
    }

    private fun togglePlayPause() {
        if (mediaPlayer == null) loadMusic(currentMusic)
        if (!isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
            startUpdatingSeekbar()
        } else {
            mediaPlayer?.pause()
            isPlaying = false
        }
        updateNotification()
        sendUpdateBroadcast()
    }

    private fun loadMusic(index: Int) {
        releasePlayer()
        val music = PlayerActivity2.musicList[index]
        mediaPlayer = MediaPlayer.create(this, music.music)
        mediaPlayer?.setOnCompletionListener {
            isPlaying = false
            sendUpdateBroadcast()
            updateNotification()
        }
        updateNotification()
        sendUpdateBroadcast()
    }

    private fun nextMusic() {
        releasePlayer()
        currentMusic++
        if (currentMusic >= PlayerActivity2.musicList.size) currentMusic = 0
        loadMusic(currentMusic)
        mediaPlayer?.start()
        isPlaying = true
        startUpdatingSeekbar()
    }

    private fun previousMusic() {
        releasePlayer()
        currentMusic--
        if (currentMusic < 0) currentMusic = PlayerActivity2.musicList.size - 1
        loadMusic(currentMusic)
        mediaPlayer?.start()
        isPlaying = true
        startUpdatingSeekbar()
    }

    /** 🔁 Real-time progress updater */
    private fun startUpdatingSeekbar() {
        handler.removeCallbacksAndMessages(null)
        handler.post(object : Runnable {
            override fun run() {
                if (mediaPlayer != null && isPlaying) {
                    val current = mediaPlayer!!.currentPosition
                    val duration = mediaPlayer!!.duration
                    val progressIntent = Intent("MUSIC_PROGRESS")
                    progressIntent.putExtra("current", current)
                    progressIntent.putExtra("duration", duration)
                    sendBroadcast(progressIntent)

                    // 🔔 Notification progressni ham yangilaymiz
                    updateNotification(current, duration)

                    handler.postDelayed(this, 500)
                }
            }
        })
    }

    private fun sendUpdateBroadcast() {
        val music = PlayerActivity2.musicList[currentMusic]
        val intent = Intent("MUSIC_UPDATE")
        intent.putExtra("musicName", music.musicName)
        intent.putExtra("musicImage", music.image)
        intent.putExtra("isPlaying", isPlaying)
        sendBroadcast(intent)
    }

    /** 🎵 Notificationni progress bar bilan yangilovchi funksiya */
    private fun updateNotification(current: Int = 0, duration: Int = 0) {
        val music = PlayerActivity2.musicList[currentMusic]
        val progress = if (duration > 0) (current * 100 / duration) else 0

        // ✅ Layout bilan bir xil ID nomi: progressBar
        val remoteView = RemoteViews(packageName, R.layout.notification_media)

        // 🔹 Qo‘shiq nomi, artist, rasm va progress
        remoteView.setTextViewText(R.id.tvSongTitle, music.musicName)
        remoteView.setTextViewText(R.id.tvArtist, "Unknown Artist") // agar artist yo‘q bo‘lsa
        remoteView.setImageViewResource(R.id.ivCover, music.image)
        remoteView.setProgressBar(R.id.progressBar, 100, progress, false)

        // 🔹 Play/Pause tugmasini yangilaymiz
        val playIcon = if (isPlaying)
            R.drawable.pause_svg_for_notif
        else
            R.drawable.play_svg_for_notif

        remoteView.setImageViewResource(R.id.btnPlay, playIcon)

        // 🔹 Tugmalarni PendingIntent bilan bog‘laymiz
        remoteView.setOnClickPendingIntent(
            R.id.btnPlay,
            PendingIntent.getService(
                this,
                1,
                Intent(this, MediaPlayerService::class.java).setAction(MEDIA_PLAYER_PLAY),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.btnNext,
            PendingIntent.getService(
                this,
                2,
                Intent(this, MediaPlayerService::class.java).setAction(MEDIA_PLAYER_NEXT),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.btnPrev,
            PendingIntent.getService(
                this,
                3,
                Intent(this, MediaPlayerService::class.java).setAction(MEDIA_PLAYER_BACK),
                PendingIntent.FLAG_IMMUTABLE
            )
        )

        // 🔹 Notification yaratamiz
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.spotify_color_svgrepo_com)
            .setOngoing(isPlaying)
            .setCustomContentView(remoteView)
            .setCustomBigContentView(remoteView)
            .build()

        startForeground(NOTIF_ID, notif)
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        releasePlayer()
        handler.removeCallbacksAndMessages(null)
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
}
