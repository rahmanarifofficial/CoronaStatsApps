package com.rahmanarifofficial.coronastats.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.lifecycle.ViewModelProvider
import com.rahmanarifofficial.coronastats.R
import com.rahmanarifofficial.coronastats.utils.Preferences
import com.rahmanarifofficial.coronastats.utils.Utils
import com.rahmanarifofficial.coronastats.utils.ViewModelFactory
import com.rahmanarifofficial.coronastats.viewmodel.DataViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var dataViewModel: DataViewModel
    private lateinit var preferences: Preferences

    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        initObject()
        eventUI()
    }

    private fun initObject() {
        val factory = ViewModelFactory.getInstance(this)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]
        preferences = Preferences(this)
    }

    private fun eventUI() {
        if (preferences.isLogin){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        submitBtn?.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            val id = usernameEdt.text.toString()
            val pass = passwordEdt.text.toString()
            dataViewModel.login(id, pass) { isLogin ->
                if (isLogin){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Utils.showToast(this, getString(R.string.login_failed))
                }
            }
        }
    }
}