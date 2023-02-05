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

    fun removeAppFromFocusList(packageName: String) {
        viewModelScope.launch {
            val currentList = SharedPrefManager.focusList ?: listOf()
            currentList.filter {
                it.first != packageName
            }
            SharedPrefManager.focusList = currentList
        }
    }

    private fun focusListContainsApp(app: Pair<String, Pair<Int, Int>>) : Boolean {
        val currentList = SharedPrefManager.focusList
        return if (currentList.isNullOrEmpty()) {
            false
        } else {
            currentList.map { it.first }.contains(app.first)
        }
    }
    fun updateTimeOfAppInFocusList(app: Pair<String, Pair<Int, Int>>) {
        viewModelScope.launch {
            if (focusListContainsApp(app)){
                updateFocusList(app)
            } else {
                removeAppFromFocusList(app.first)
                val currentList = SharedPrefManager.focusList ?: listOf()
                val newList = currentList + listOf(app)
                SharedPrefManager.focusList = newList
            }
        }
    }
}