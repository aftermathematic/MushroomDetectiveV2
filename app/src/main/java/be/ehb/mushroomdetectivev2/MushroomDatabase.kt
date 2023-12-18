package be.ehb.mushroomdetectivev2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mushroom::class], version = 2)
abstract class MushroomDatabase : RoomDatabase() {
    abstract fun mushroomDao(): MushroomDao

    // create a singleton
    companion object {
        @Volatile
        private var INSTANCE: MushroomDatabase? = null

        fun getDatabase(context: Context): MushroomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MushroomDatabase::class.java,
                    "mushroom_database.db"
                ).fallbackToDestructiveMigration() // Handle migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}