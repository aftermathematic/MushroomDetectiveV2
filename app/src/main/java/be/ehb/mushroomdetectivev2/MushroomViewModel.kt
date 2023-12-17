package be.ehb.mushroomdetectivev2

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class MushroomViewModel(private val repository: MushroomRepository) : ViewModel() {

    val allMushrooms: LiveData<List<Mushroom>> = repository.allMushrooms.asLiveData()

}

class MushroomRepository(private val mushroomDao: MushroomDao) {
    val allMushrooms: Flow<List<Mushroom>> = mushroomDao.getAllMushrooms()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mushroom: Mushroom) {
        mushroomDao.insertMushroom(mushroom)
    }
}