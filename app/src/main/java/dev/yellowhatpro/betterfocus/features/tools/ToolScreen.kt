package dev.yellowhatpro.betterfocus.features.tools

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yellowhatpro.betterfocus.features.BetterFocusViewModel
import dev.yellowhatpro.betterfocus.ui.theme.GlobalFontFamily
import dev.yellowhatpro.betterfocus.ui.theme.Primary90Dark
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager

@Composable
fun ToolScreen(modifier : Modifier, packageManager: PackageManager, viewModel : BetterFocusViewModel = hiltViewModel()) {
    val listOfApplications = try {
        SharedPrefManager.allApplicationInfo ?: listOf()
    } catch (e: Exception) {
        listOf()
    }
    val isBenign by viewModel.isBenign.collectAsState()
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Static Analysis",
            color = Color.White,
            fontFamily = GlobalFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)) {
            items(listOfApplications) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Primary90Dark, RoundedCornerShape(12.dp))
                ) {
                    KeyValueTool(key = "Package Name", value = it?.packageName ?: "")
                    KeyValueTool(key = "Public Source Directory", value = it?.publicSourceDir ?: "")
                    val list = viewModel.getPerms(it?.packageName ?: "")
                    Text(
                        text = "Permissions",
                        color = Color.White,
                        fontFamily = GlobalFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                    list.map {
                        Text(text = it, modifier = Modifier.padding(10.dp),
                            fontFamily = GlobalFontFamily,
                            color = Color.White
                        )
                    }
//                    Text(text = viewModel.getManifestData(it?.publicSourceDir ?: ""), modifier = Modifier.padding(12.dp), fontSize = 16.sp,
//                        fontFamily = GlobalFontFamily,
//                        color = Color.White
//                        )

                    IconButton(onClick = {
                        viewModel.getMalwareReport(it?.packageName ?: "")
                        Toast
                            .makeText(context, isBenign, Toast.LENGTH_SHORT)
                            .show()
                    } , modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                        .padding(10.dp)) {
                        Row {
                            Icon(imageVector = Icons.Rounded.Check, contentDescription = "Check")
                            Text(text = "Check if Benign or Malicious",
                                fontWeight = FontWeight.Bold,
                            fontFamily = GlobalFontFamily)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyValueTool(key:String, value: String) {
    Row (horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = key,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
            fontFamily = GlobalFontFamily,
            )
        Text(
            text = value,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp),
            fontFamily = GlobalFontFamily,
            )
    }
}