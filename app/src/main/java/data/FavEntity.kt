package data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
@Entity("Fav")
data class FavEntity (
        @PrimaryKey(autoGenerate = true)
        val tableId : Int = 0,
        val id : Int,
        val rating : Char,
        @SerialName("tag_string_general")
        val tagStringGeneral: String?,
        @SerialName("tag_string_character")
        val tagStringCharacter: String?,
        @SerialName("tag_string_copyright")
        val tagStringCopyright: String?,
        @SerialName("tag_string_artist")
        val tagStringArtist: String?,
        @SerialName("tag_string_meta")
        val tagStringMeta: String?,
        @TypeConverters(TypeConverter::class)
        @SerialName("media_asset")
        var mediaAsset          : MediaAsset? = MediaAsset()
    )