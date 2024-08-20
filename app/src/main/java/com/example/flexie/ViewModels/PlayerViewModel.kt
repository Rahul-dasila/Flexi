package com.example.flexie.ViewModels

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    val player: ExoPlayer,
    @ApplicationContext val context: Context
) : ViewModel() {
    var isFullScreen by mutableStateOf(false)
    var videoLoading by mutableStateOf(false)
    var playbackPosition: Long = 0L
    var playWhenReady: Boolean = true
    var uri: String = ""

    init {
        player.addListener(object : Player.Listener {
            override fun onIsLoadingChanged(isLoading: Boolean) {
                videoLoading = isLoading
            }
        })
    }

    init {
        player.playWhenReady = playWhenReady
    }

    @OptIn(UnstableApi::class)
    private fun buildMediaSource(uri: String, context: Context): MediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(context)
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
    }

    @OptIn(UnstableApi::class)
    fun prepareMediaItem(uri: String, context: Context) {
        val mediaSource = buildMediaSource(uri, context)
        player.setMediaSource(mediaSource)
        player.seekTo(playbackPosition)
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    override fun onCleared() {
        super.onCleared()
        // Save the state before the ViewModel is destroyed
        playbackPosition = player.currentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }
}