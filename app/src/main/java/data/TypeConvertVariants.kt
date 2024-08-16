package data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConvertVariants {
    @TypeConverter
    fun convertToJson(mediaAsset: MediaAsset) :String{
        return Json.encodeToString(mediaAsset).toString()
    }
    @TypeConverter
    fun covertToObject(json: String) : MediaAsset {
        return Json.decodeFromString<MediaAsset>(json)
    }
}