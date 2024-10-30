package com.example.p72_weather.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.p72_weather.R
import com.example.p72_weather.repository.Repository
import com.example.p72_weather.ui.fragment.MainFragment
import com.example.p72_weather.viewmodel.MainViewModel
import com.example.p72_weather.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1200)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        initialise()
    }

    private fun initialise() {
        val repository = Repository()
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, MainFragment())
            .commit()
    }

}