package com.eslam.jetadmobapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eslam.jetadmobapp.feateres.feater_addmob.AdmobScreenFactory
import com.eslam.jetadmobapp.ui.theme.JetAdMobAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetAdMobAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdmobScreenFactory(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}