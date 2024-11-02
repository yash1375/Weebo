package data

import androidx.compose.runtime.Immutable
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageNetwork(
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
@Serializable
data class MediaAsset (

    @SerialName("id"           ) var id          : Int?                = null,
    @SerialName("created_at"   ) var createdAt   : String?             = null,
    @SerialName("updated_at"   ) var updatedAt   : String?             = null,
    @SerialName("md5"          ) var md5         : String?             = null,
    @SerialName("file_ext"     ) var fileExt     : String?             = null,
    @SerialName("file_size"    ) var fileSize    : Int?                = null,
    @SerialName("image_width"  ) var imageWidth  : Int?                = null,
    @SerialName("image_height" ) var imageHeight : Int?                = null,
    @SerialName("duration"     ) var duration    : Float?             = null,
    @SerialName("status"       ) var status      : String?             = null,
    @SerialName("file_key"     ) var fileKey     : String?             = null,
    @SerialName("is_public"    ) var isPublic    : Boolean?            = null,
    @SerialName("pixel_hash"   ) var pixelHash   : String?             = null,
    @SerialName("variants"     ) var variants    : ArrayList<Variants> = arrayListOf()

)


@Immutable
@Serializable
data class Variants (
    @SerialName("type"     ) var type    : String? = null,
    @SerialName("url"      ) var url     : String? = null,
    @SerialName("width"    ) var width   : Int?    = null,
    @SerialName("height"   ) var height  : Int?    = null,
    @SerialName("file_ext" ) var fileExt : String? = null

)
