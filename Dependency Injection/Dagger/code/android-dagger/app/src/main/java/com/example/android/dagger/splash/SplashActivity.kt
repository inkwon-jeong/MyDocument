package com.example.android.dagger.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.android.dagger.MyApplication
import com.example.android.dagger.R
import com.example.android.dagger.login.LoginActivity
import com.example.android.dagger.main.MainActivity
import com.example.android.dagger.registration.RegistrationActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.splashComponent().create().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        registerObserver()
        splashViewModel.load()
    }

    private fun registerObserver() {
        splashViewModel.isReady.observe(this, Observer {
            it?.let {
                if(it) {
                    navigate()
                    splashViewModel.doneLoading()
                }
            }
        })
    }

    private fun navigate() {
        if (!splashViewModel.isUserLoggedIn()) {
            if (!splashViewModel.isUserRegistered()) {
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}