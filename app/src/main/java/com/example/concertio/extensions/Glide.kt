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
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

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

fun MenuItem.loadImage(context: Context, uri: Uri, placeholderRes: Int) {
    loadImageIntoDrawable(context, uri, placeholderRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            iconTintList = null
            iconTintMode = null
        }
        icon = it
    }
}

fun loadImageIntoDrawable(
    context: Context,
    uri: Uri,
    placeholderRes: Int,
    onDrawableLoaded: (drawable: Drawable) -> Unit
) {
    Glide.with(context).asBitmap()
        .load(uri)
        .circleCrop().placeholder(placeholderRes)
        .into(object : CustomTarget<Bitmap>(32, 32) {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                onDrawableLoaded(BitmapDrawable(context.resources, resource))
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}