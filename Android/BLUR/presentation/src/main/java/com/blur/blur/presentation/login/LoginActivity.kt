package com.blur.blur.presentation.Login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.blur.blur.presentation.theme.BLURTheme

/**
 * @author soohwan.ok
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BLURTheme {
                LoginNavHost()
            }
        }
    }
}