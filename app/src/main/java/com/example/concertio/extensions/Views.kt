package com.example.concertio.extensions

import android.content.Context
import android.net.Uri
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.view.isVisible
import com.example.concertio.R

fun initMedia(
    context: Context,
    imageView: ImageView?,
    videoView: VideoView?,
    uri: Uri,
    mediaType: String
) {
    if (mediaType == "image") {
        videoView?.isVisible = false
        imageView?.isVisible = true
        imageView?.loadReviewImage(
            context,
            uri,
            R.drawable.front_page_logo
        )
    } else {
        imageView?.isVisible = false
        videoView?.apply {
            setVideoURI(uri)
            seekTo(1)
            MediaController(context).also {
                it.setAnchorView(this)
            }
            isVisible = true
            setOnPreparedListener {
                requestFocus()
                setOnClickListener {
                    if (!isPlaying) start() else pause()
                }
                setOnCompletionListener {
                    seekTo(1)
                }
            }
        }
    }
}
