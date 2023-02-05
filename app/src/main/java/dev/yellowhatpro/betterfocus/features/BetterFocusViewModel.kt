package dev.yellowhatpro.betterfocus.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.Hours
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yellowhatpro.betterfocus.data.FocusApp
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BetterFocusViewModel @Inject constructor(): ViewModel() {

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

    private fun focusListContainsApp(packageName: String) : Boolean {
        val currentList = SharedPrefManager.focusList
        return if (currentList.isNullOrEmpty()) {
            false
        } else {
            currentList.map { it.packageName }.contains(packageName)
        }
    }
    fun updateTimeOfAppInFocusList(app: Pair<String, Hours>) {
        viewModelScope.launch {
            val newApp = FocusApp(packageName = app.first, hours = app.second.hours, minutes = app.second.minutes)
            if (focusListContainsApp(newApp.packageName)){
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

}