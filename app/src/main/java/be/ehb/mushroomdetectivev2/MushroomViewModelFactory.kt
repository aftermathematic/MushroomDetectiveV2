package be.ehb.mushroomdetectivev2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class MushroomViewModelFactory(private val repository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushroomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MushroomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}