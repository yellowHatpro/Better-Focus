package dev.yellowhatpro.betterfocus.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppCard(name:String,
    icon: Drawable,
    time: String,
    modifier : Modifier = Modifier) {
    Box(modifier = modifier

        .background(MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(75.dp),
                painter = rememberDrawablePainter(drawable = icon),
                contentDescription = "")
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = "Usage: $time", fontSize = 14.sp)
        }
    }
}