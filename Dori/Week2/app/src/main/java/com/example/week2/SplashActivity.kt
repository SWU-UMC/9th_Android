package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val handler = Handler(Looper.getMainLooper())


        handler.postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)

    }
}