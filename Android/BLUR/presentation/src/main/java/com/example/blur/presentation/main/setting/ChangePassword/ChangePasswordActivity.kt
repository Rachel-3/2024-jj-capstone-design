package com.example.blur.presentation.Main.Setting.ChangePassword

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.blur.presentation.theme.BLURTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            BLURTheme {
                ChangePasswordActivity()
            }
        }
    }
}