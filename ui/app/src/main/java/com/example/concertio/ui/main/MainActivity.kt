package com.example.concertio.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.concertio.R
import com.example.concertio.ui.auth.AuthActivity
import com.example.concertio.ui.main.fragments.save_review.SaveReviewMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val bottomNav by lazy { findViewById<BottomNavigationView>(R.id.bottom_nav) }
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }
    private val viewModel: UserProfileViewModel by viewModels<UserProfileViewModel> { ViewModelProvider.NewInstanceFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initBottomNav()

        viewModel.observeMyProfile().observe(this) {
            if (it == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                return@observe finish()
            }
            Glide.with(applicationContext).asBitmap()
                .load(Base64.decode(it.profilePicture, Base64.DEFAULT))
                .circleCrop().placeholder(R.drawable.empty_profile_picture)
                .into(object : CustomTarget<Bitmap>(32, 32) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bottomNav.menu[2].apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                iconTintList = null
                                iconTintMode = null
                            }
                            icon = BitmapDrawable(resources, resource)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    private fun initBottomNav() {
        NavigationUI.setupWithNavController(
            bottomNav,
            navHostFragment.navController,
        )
    }
}