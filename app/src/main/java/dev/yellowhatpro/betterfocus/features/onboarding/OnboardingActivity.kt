package dev.yellowhatpro.betterfocus.features.onboarding

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.features.better_focus_main.BetterFocusActivity
import dev.yellowhatpro.betterfocus.ui.theme.BetterFocusTheme
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager


class OnboardingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefManager.isOnboardingCompleted == true) {
            startActivity(
                Intent(
                    this@OnboardingActivity,
                    BetterFocusActivity::class.java
                )
            )
            finish()
        }
        setContent {
            BetterFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF062f40)
                ) {
                    Column {
                        Image(
                            painterResource(id = R.drawable.betterfocusicon),
                            contentDescription = ""
                        )

                        Button(onClick = {
                            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                            checkUsageStatsPermissionGranted()
                        }) {
                            Text(text = "Give system setting permission")
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

    private fun checkUsageStatsPermissionGranted() : Boolean{
        val mode = this.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val check = mode.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            this.packageName
        )
        SharedPrefManager.isUsageStatsPermissionGranted = check == AppOpsManager.MODE_ALLOWED
        return check == AppOpsManager.MODE_ALLOWED
    }
}