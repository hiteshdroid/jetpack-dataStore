package com.demo.jetpackdatastore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_preference.setOnClickListener {
            val intent = Intent(this, ScreenLaunchCounterActivity::class.java)
            startActivity(intent)
        }

        btn_proto.setOnClickListener {
            val intent = Intent(this, FilterListActivity::class.java)
            startActivity(intent)
        }
    }
}