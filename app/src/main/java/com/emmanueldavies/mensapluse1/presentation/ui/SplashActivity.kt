package com.emmanueldavies.mensapluse1.presentation.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.emmanueldavies.mensapluse1.presentation.ui.MensaView.MainActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.emmanueldavies.mensapluse1.R.layout.activity_splash)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
