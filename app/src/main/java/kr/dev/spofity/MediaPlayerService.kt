package kr.dev.spofity

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.widget.Toast

class MediaPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic = 0
    private var isPlaying = false
    private val handler = Handler()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> togglePlayPause()
            MEDIA_PLAYER_NEXT -> nextMusic()
            MEDIA_PLAYER_BACK -> previousMusic()
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
            Toast.makeText(this, "▶ Playing", Toast.LENGTH_SHORT).show()
        } else {
            mediaPlayer?.pause()
            isPlaying = false
            Toast.makeText(this, "⏸ Paused", Toast.LENGTH_SHORT).show()
        }

        sendUpdateBroadcast()
    }

    private fun loadMusic(index: Int) {
        releasePlayer()
        val music = PlayerActivity2.musicList[index]
        mediaPlayer = MediaPlayer.create(this, music.music)

        mediaPlayer?.setOnCompletionListener {
            isPlaying = false
            sendUpdateBroadcast()
        }

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
        sendUpdateBroadcast()
    }

    private fun previousMusic() {
        releasePlayer()
        currentMusic--
        if (currentMusic < 0) currentMusic = PlayerActivity2.musicList.size - 1
        loadMusic(currentMusic)
        mediaPlayer?.start()
        isPlaying = true
        startUpdatingSeekbar()
        sendUpdateBroadcast()
    }

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

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        releasePlayer()
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
