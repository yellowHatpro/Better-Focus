package dev.yellowhatpro.betterfocus.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BetterFocusViewModel @Inject constructor(): ViewModel() {

    private fun updateFocusList(app: Pair<String, Pair<Int, Int>>) {
        viewModelScope.launch {
            val currentList = SharedPrefManager.focusList ?: listOf()
            val newList = currentList + listOf(app)
            SharedPrefManager.focusList = newList
        }
    }

    private fun removeAppFromFocusList(packageName: String) {
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
    fun updateTimeOfAppInFocusList(app: Pair<String, Pair<Int, Int>>) {
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
}