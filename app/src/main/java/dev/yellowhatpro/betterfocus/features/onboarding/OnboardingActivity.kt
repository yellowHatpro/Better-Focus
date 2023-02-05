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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.features.BetterFocusActivity
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
            var isSysPermissionGot by remember {
                mutableStateOf(false)
            }
            BetterFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF062f40)
                ) {
                    Column( modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(id = R.drawable.betterfocusicon),
                            contentDescription = ""
                        )

                        Text(text = "Better Focus", fontWeight = FontWeight.Black, fontSize = 48.sp, textAlign = TextAlign.Center, color = Color(0xFFFFFFFF))
                        Divider(modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp).clip(
                            RoundedCornerShape(20.dp)), thickness = 2.dp, color = Color(0xFFFFFFFF)  )
                        Button(onClick = {
                            isSysPermissionGot = checkUsageStatsPermissionGranted()
                            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

                        }) {
                            Text(text = "Give system setting permission" , textDecoration = if (isSysPermissionGot) TextDecoration.LineThrough else TextDecoration.None, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Button(onClick = {
                            startActivity(
                                Intent(
                                    this@OnboardingActivity,
                                    BetterFocusActivity::class.java
                                )
                            )
                            SharedPrefManager.isOnboardingCompleted = true
                        }) {
                            Text(text = "Let's get started", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    private fun checkUsageStatsPermissionGranted(): Boolean {
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