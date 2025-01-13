package com.example.concertio.extensions

import android.content.Context
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.concertio.R

fun ImageView.loadBase64(view: View, base64String: String, placeholderRes: Int) {
    Glide.with(view)
        .load(Base64.decode(base64String, Base64.DEFAULT))
        .circleCrop()
        .placeholder(placeholderRes)
        .into(this)
}

fun ImageView.loadBase64(context: Context, base64String: String, placeholderRes: Int) {
    Glide.with(context)
        .load(Base64.decode(base64String, Base64.DEFAULT))
        .circleCrop()
        .placeholder(placeholderRes)
        .into(this)
}

fun ImageView.loadBase64(fragment: Fragment, base64String: String, placeholderRes: Int) {
    Glide.with(fragment)
        .load(Base64.decode(base64String, Base64.DEFAULT))
        .circleCrop()
        .placeholder(placeholderRes)
        .into(this)
}