package com.demo.jetpackdatastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.demo.jetpackdatastore.preference.PreferenceUtils
import kotlinx.android.synthetic.main.home_main.*
import kotlinx.coroutines.launch

class ScreenLaunchCounterActivity : AppCompatActivity() {
    private lateinit var dataStoreUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)
        dataStoreUtils =
            PreferenceUtils(this)
        observeAppLaunchCounter()
        setAppLaunched()

        btn_reset.setOnClickListener {
            lifecycleScope.launch {
                dataStoreUtils.clearAppLaunchCounter()
            }
        }
    }

    private fun observeAppLaunchCounter() {
        dataStoreUtils.getCurrentAppLaunchCounter().asLiveData().observe(this, Observer {
            tv_app_launch_counter.text =
                String.format(resources.getString(R.string.screen_launch_counter_text), it)
        })
    }

    private fun setAppLaunched() {
        lifecycleScope.launch {
            dataStoreUtils.incrementAppLaunchCounter()
        }
    }
}