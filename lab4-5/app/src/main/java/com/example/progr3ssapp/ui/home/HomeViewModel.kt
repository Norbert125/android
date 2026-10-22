package com.example.progr3ssapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.progr3ssapp.utils.SessionManager

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager.getInstance(application)

    fun logout() = sessionManager.clear()
}
