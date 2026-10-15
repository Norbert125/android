package com.example.progr3ssapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.progr3ssapp.navigation.AppNavGraph
import com.example.progr3ssapp.ui.theme.Progr3ssAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. A splash screen telepítése a super.onCreate() ELŐTT
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // 2. A splash látható marad, amíg a kezdeti betöltés tart
        var isLoading = true
        lifecycleScope.launch {
            delay(1500L) // ide jön majd a valódi inicializálás (token, adatbázis stb.)
            isLoading = false
        }
        splashScreen.setKeepOnScreenCondition { isLoading }

        // 3. Egyedi fade-out animáció kilépéskor
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val view = splashScreenViewProvider.view
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
                duration = 500L
                interpolator = AccelerateDecelerateInterpolator()
                doOnEnd { splashScreenViewProvider.remove() }
                start()
            }
        }

        enableEdgeToEdge()
        setContent {
            Progr3ssAppTheme {
                AppNavGraph()
            }
        }
    }
}
