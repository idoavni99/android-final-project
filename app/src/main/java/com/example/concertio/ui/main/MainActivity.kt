package com.example.concertio.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.concertio.R
import com.example.concertio.extensions.loadImage
import com.example.concertio.ui.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        viewModel.observeMyProfile().observe(this) { myProfileData ->
            if (myProfileData == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                return@observe finish()
            }

            bottomNav.itemIconTintList = null
            myProfileData.profilePicture?.let {
                bottomNav.menu.findItem(R.id.userProfileFragment).apply {
                    loadImage(
                        applicationContext,
                        Uri.parse(it),
                        R.drawable.empty_profile_picture,
                        lifecycleScope
                    )
                }
            }
        }
    }

    private fun initBottomNav() {
        NavigationUI.setupWithNavController(
            bottomNav,
            navHostFragment.navController,
        )
    }
}