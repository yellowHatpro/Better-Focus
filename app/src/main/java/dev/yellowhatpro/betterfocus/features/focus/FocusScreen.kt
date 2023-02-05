package dev.yellowhatpro.betterfocus.features.focus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager

@Composable
fun FocusScreen(modifier : Modifier = Modifier
) {
    Column {
        LazyColumn{
            val focusApps = SharedPrefManager.focusList ?: listOf()
            items(focusApps){
                Text(text = it.first)
            }
        }
    }
}