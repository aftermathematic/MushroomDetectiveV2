package be.ehb.mushroomdetectivev2

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MushroomViewModel(private val repository: MushroomRepository) : ViewModel() {

    val allMushrooms: LiveData<List<Mushroom>> = repository.allMushrooms.asLiveData()

    fun insert(mushroom: Mushroom) = viewModelScope.launch {
        repository.insert(mushroom)
    }
}

class MushroomRepository(private val mushroomDao: MushroomDao) {
    val allMushrooms: Flow<List<Mushroom>> = mushroomDao.getAllMushrooms()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mushroom: Mushroom) {
        mushroomDao.insertMushroom(mushroom)
    }
}