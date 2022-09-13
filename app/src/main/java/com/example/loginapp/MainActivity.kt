package com.example.loginapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, FragmentLog(), "HomeFragment")
            .addToBackStack(FragmentLog::class.java.toString())
            .commit()
    }
}