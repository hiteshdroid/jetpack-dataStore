package com.demo.jetpackdatastore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.demo.jetpackdatastore.R
import com.demo.jetpackdatastore.data.preference.PreferenceRepository
import kotlinx.android.synthetic.main.home_main.*
import kotlinx.coroutines.launch

class ScreenLaunchCounterActivity : AppCompatActivity() {
    private lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)
        preferenceRepository =
            PreferenceRepository(this)
        observeAppLaunchCounter()
        setAppLaunched()

        btn_reset.setOnClickListener {
            lifecycleScope.launch {
                preferenceRepository.clearAppLaunchCounter()
            }
        }
    }

    private fun observeAppLaunchCounter() {
        preferenceRepository.getCurrentAppLaunchCounter().asLiveData().observe(this, Observer {
            tv_app_launch_counter.text =
                String.format(resources.getString(R.string.screen_launch_counter_text), it)
        })
    }

    private fun setAppLaunched() {
        lifecycleScope.launch {
            preferenceRepository.incrementAppLaunchCounter()
        }
    }
}