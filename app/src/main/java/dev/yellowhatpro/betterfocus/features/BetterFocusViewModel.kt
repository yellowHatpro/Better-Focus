package dev.yellowhatpro.betterfocus.features

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Xml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.Hours
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yellowhatpro.betterfocus.App.Companion.context
import dev.yellowhatpro.betterfocus.data.FocusApp
import dev.yellowhatpro.betterfocus.network.MalwareClassifierApi
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject


@HiltViewModel
class BetterFocusViewModel @Inject constructor(): ViewModel() {

    private var _isBenign = MutableStateFlow("Checking...")
    val isBenign = _isBenign.asStateFlow()
    private var _focusApps = MutableStateFlow(emptyList<FocusApp>())
    val focusApps = _focusApps.asStateFlow()
    private fun updateFocusList(app: FocusApp) {
        viewModelScope.launch {
            val currentList = SharedPrefManager.focusList ?: listOf()
            val newList = currentList + listOf(app)
            SharedPrefManager.focusList = newList
        }
    }

    fun removeAppFromFocusList(packageName: String) {
        viewModelScope.launch {
            val currentList = SharedPrefManager.focusList ?: listOf()
            val newList = currentList.filter {
                it.packageName != packageName
            }
            SharedPrefManager.focusList = newList
        }
    }

    private fun focusListContainsApp(packageName: String): Boolean {
        val currentList = SharedPrefManager.focusList
        return if (currentList.isNullOrEmpty()) {
            false
        } else {
            currentList.map { it.packageName }.contains(packageName)
        }
    }

    fun updateTimeOfAppInFocusList(app: Pair<String, Hours>) {
        viewModelScope.launch {
            val newApp = FocusApp(
                packageName = app.first,
                hours = app.second.hours,
                minutes = app.second.minutes
            )
            if (focusListContainsApp(newApp.packageName)) {
                removeAppFromFocusList(newApp.packageName)
                updateFocusList(newApp)
            } else {
                val currentList = SharedPrefManager.focusList ?: listOf()
                val newList = currentList + listOf(newApp)
                SharedPrefManager.focusList = newList
            }
        }
    }

    fun provideFocusApps() {
        viewModelScope.launch {
            _focusApps.value = SharedPrefManager.focusList ?: listOf()
        }
    }

    fun getAllAppsFromPM() {
        val pm = context?.packageManager
        val apps = pm!!.getInstalledApplications(
            PackageManager.GET_META_DATA or PackageManager.GET_SHARED_LIBRARY_FILES
        ) as List<ApplicationInfo>
        SharedPrefManager.allApplicationInfo = apps
    }

     fun getManifestData(path: String) {
         viewModelScope.launch {
             withContext(Dispatchers.IO) {
                 val apk = ZipFile(path)
                 val manifest: ZipEntry? = apk.getEntry("AndroidManifest.xml")
                 manifest?.let {
                     val stream = apk.getInputStream(manifest)
                     readXmlFromInputStream(stream)
                     stream.close()
                 }
                 apk.close()
             }
         }
     }


    private suspend fun readXmlFromInputStream(inputStream: InputStream) {
        withContext(Dispatchers.IO) {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                when (parser.eventType) {
                    XmlPullParser.START_TAG -> {
                        val tagName = parser.name
                        // Do something with the start tag
                        println("Start tag: $tagName")
                    }

                    XmlPullParser.END_TAG -> {
                        val tagName = parser.name
                        // Do something with the end tag
                        println("End tag: $tagName")
                    }

                    XmlPullParser.TEXT -> {
                        val text = parser.text
                        // Do something with the text content
                        println("Text content: $text")
                    }
                }
                parser.next()
            }
            inputStream.close()
        }
    }


    fun getPerms(packageName: String): Array<String> {
        var perms : Array<String> = arrayOf()
        viewModelScope.launch {
            val packageInfo = context?.packageManager?.getPackageInfo(packageName,  PackageManager.GET_PERMISSIONS )
            perms = packageInfo?.requestedPermissions ?: arrayOf()
        }
        return  perms
    }

    //Retrofit stuff
    fun getMalwareReport(pck: String) {
        val permissions = getPerms(pck).toList().toString()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result =
                    MalwareClassifierApi.retrofitService.getResult(mapOf("perms" to permissions))
                        .execute().body()
                val jsonObj = getJsonObjectFromString(result?.trimIndent() ?: "")
                _isBenign.value = jsonObj.getString("analysis")

            }
        }
    }
    private fun getJsonObjectFromString(jsonString: String): JSONObject {
        return JSONObject(jsonString)
    }
 }