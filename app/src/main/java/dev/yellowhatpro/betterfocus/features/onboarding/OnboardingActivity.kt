package dev.yellowhatpro.betterfocus.features.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.features.better_focus_main.BetterFocusActivity
import dev.yellowhatpro.betterfocus.ui.theme.BetterFocusTheme
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager

class OnboardingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefManager.isOnboardingCompleted==true){
            startActivity(
                Intent(
                    this@OnboardingActivity,
                    BetterFocusActivity::class.java
                )
            )
        }
        setContent {
            BetterFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Card {
                            Image(
                                painterResource(id = R.drawable.betterfocusicon),
                                contentDescription = ""
                            )
                        }
                        Button(onClick = {
                            startActivity(
                                Intent(
                                    this@OnboardingActivity,
                                    BetterFocusActivity::class.java
                                )
                            )
                            SharedPrefManager.isOnboardingCompleted = true
                        }) {
                            Text(text = "Go To New Activity")
                        }
                    }
                }
            }
        }
    }
}