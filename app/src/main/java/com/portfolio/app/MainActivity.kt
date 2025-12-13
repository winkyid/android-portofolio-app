package com.portfolio.app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.portfolio.app.databinding.ActivityMainBinding
import com.portfolio.app.util.ThemeManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var themeManager: ThemeManager
    private lateinit var btnThemeToggle: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeManager = ThemeManager(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnThemeToggle = findViewById(R.id.btn_theme_toggle)

        lifecycleScope.launch {
            val isDarkMode = themeManager.isDarkMode.first()
            applyThemeMode(isDarkMode)
            updateThemeIcon(isDarkMode)
        }

        setupNavigation()
        setupThemeToggle()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)
    }

    private fun setupThemeToggle() {
        btnThemeToggle.setOnClickListener {
            lifecycleScope.launch {
                val currentMode = themeManager.isDarkMode.first()
                val newMode = !currentMode
                themeManager.setDarkMode(newMode)
                updateThemeIcon(newMode)
                applyThemeMode(newMode)
            }
        }
    }

    private fun applyThemeMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateThemeIcon(isDarkMode: Boolean) {
        if (isDarkMode) {
            btnThemeToggle.setImageResource(R.drawable.ic_light_mode)
        } else {
            btnThemeToggle.setImageResource(R.drawable.ic_dark_mode)
        }
    }
}
