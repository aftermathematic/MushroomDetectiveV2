package be.ehb.mushroomdetectivev2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MushroomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend
    fun insertMushroom(mushroom: Mushroom): Long

    @Query("SELECT * FROM mushroom_table")
    fun getAllMushrooms(): Flow<List<Mushroom>>
}