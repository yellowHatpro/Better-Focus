package dev.yellowhatpro.betterfocus.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.Hours
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BetterFocusViewModel @Inject constructor(): ViewModel() {

    private var _focusApps = MutableStateFlow(emptyList<Pair<String, Hours>>())
    val focusApps = _focusApps.asStateFlow()
    private fun updateFocusList(app: Pair<String, Hours>) {
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
                it.first != packageName
            }
            SharedPrefManager.focusList = newList
        }
    }

    private fun focusListContainsApp(packageName: String) : Boolean {
        val currentList = SharedPrefManager.focusList
        return if (currentList.isNullOrEmpty()) {
            false
        } else {
            currentList.map { it.first }.contains(packageName)
        }
    }
    fun updateTimeOfAppInFocusList(app: Pair<String, Hours>) {
        viewModelScope.launch {
            if (focusListContainsApp(app.first)){
                removeAppFromFocusList(app.first)
                updateFocusList(app)
            } else {
                val currentList = SharedPrefManager.focusList ?: listOf()
                val newList = currentList + listOf(app)
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