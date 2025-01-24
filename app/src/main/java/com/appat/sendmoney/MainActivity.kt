package com.appat.sendmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.appat.sendmoney.navigation.SMNavigation
import com.appat.sendmoney.ui.theme.SendMoneyTheme
import com.appat.sendmoney.utils.currentLanguage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentLanguage = resources.configuration.locales.get(0).language
        enableEdgeToEdge()
        setContent {
            SendMoneyTheme {
                SMNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}