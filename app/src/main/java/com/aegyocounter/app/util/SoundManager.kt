package com.aegyocounter.app.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.aegyocounter.app.R

object SoundManager {

    private lateinit var soundPool: SoundPool
    private var popSound = 0

    fun init(context: Context) {

        if (::soundPool.isInitialized) return

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(2)
            .build()

        popSound = soundPool.load(context, R.raw.pop, 1)
    }

    fun playPop() {
        if (::soundPool.isInitialized) {
            soundPool.play(
                popSound,
                1f,
                1f,
                1,
                0,
                1f
            )
        }
    }

    fun release() {
        if (::soundPool.isInitialized) {
            soundPool.release()
            popSound = 0
        }
    }
}