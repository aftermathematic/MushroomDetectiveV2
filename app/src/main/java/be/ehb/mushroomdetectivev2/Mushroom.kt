package be.ehb.mushroomdetectivev2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mushroom_table")
data class Mushroom(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cap_diameter") val capDiameter: String,
    @ColumnInfo(name = "cap_shape") val capShape: String,
    @ColumnInfo(name = "cap_color") val capColor: String,
    @ColumnInfo(name = "stem_width") val stemWidth: String,
    @ColumnInfo(name = "photo_uri") val photoUri: String?,
    @ColumnInfo(name = "api_poison") val apiPoison: String?,
    @ColumnInfo(name = "api_confidence") val apiConfidence: String?
)