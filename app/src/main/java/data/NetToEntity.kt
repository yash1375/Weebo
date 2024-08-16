package data

fun ImageNetwork.Companion.toImageEntity(image: ImageNetwork): ImageEntity{
    return ImageEntity(
        id = image.id,
        rating = image.rating,
        tagStringGeneral = image.tagStringGeneral,
        tagStringCharacter = image.tagStringCharacter,
        tagStringCopyright = image.tagStringCopyright,
        tagStringArtist = image.tagStringArtist,
        tagStringMeta = image.tagStringMeta,
        mediaAsset = image.mediaAsset
    )
}