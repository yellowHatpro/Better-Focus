package dev.yellowhatpro.betterfocus.ui.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnim(@RawRes rawRes: Int){
    var isLottiePlaying by remember {
        mutableStateOf(true)
    }
    var animationSpeed by remember {
        mutableStateOf(1F)
    }
    val animationSpec by rememberLottieComposition(spec =
    LottieCompositionSpec.RawRes(rawRes)
    )

    val lottieAnimation by animateLottieCompositionAsState(
        composition = animationSpec,
        iterations = LottieConstants.IterateForever,
        isPlaying = isLottiePlaying,
        speed = animationSpeed,
        restartOnPlay = false
    )
    LottieAnimation(
        animationSpec,
        lottieAnimation,
        modifier = Modifier.size(400.dp)
    )
}