package com.example.concertio.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ImageView.loadProfilePicture(context: Context, uri: Uri, placeholderRes: Int) {
    Glide.with(context)
        .load(uri)
        .circleCrop()
        .placeholder(placeholderRes)
        .into(this)
}

fun ImageView.loadReviewImage(context: Context, uri: Uri, placeholderRes: Int) {
    Glide.with(context)
        .load(uri)
        .placeholder(placeholderRes)
        .into(this)
}

fun MenuItem.loadImage(
    context: Context,
    uri: Uri,
    placeholderRes: Int,
    scope: CoroutineScope
) {
    loadImageIntoDrawable(context, uri, placeholderRes) {
        scope.launch(Dispatchers.Main) {
            icon = it
            icon?.setTint(context.getColor(com.google.android.material.R.color.design_default_color_secondary))
        }
    }
}

fun loadImageIntoDrawable(
    context: Context,
    uri: Uri,
    placeholderRes: Int,
    onDrawableLoaded: (drawable: Drawable) -> Unit
) {
    Glide.with(context)
        .asBitmap()
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(placeholderRes)
        .circleCrop()
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap,
                model: Any,
                target: Target<Bitmap>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                onDrawableLoaded(BitmapDrawable(context.resources, resource))
                return false
            }

        }).submit()
}