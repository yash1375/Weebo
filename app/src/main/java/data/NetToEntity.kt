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

fun ImageEntity.Companion.toFavEntity(imageEntity: ImageEntity) : FavEntity {
    return FavEntity(
        id = imageEntity.id,
        rating = imageEntity.rating,
        tagStringGeneral = imageEntity.tagStringGeneral,
        tagStringCharacter = imageEntity.tagStringCharacter,
        tagStringCopyright = imageEntity.tagStringCopyright,
        tagStringArtist = imageEntity.tagStringArtist,
        tagStringMeta = imageEntity.tagStringMeta,
        mediaAsset = imageEntity.mediaAsset

    )
}

fun FavEntity.Companion.toImageEntity(imageEntity: FavEntity) : ImageEntity {
    return ImageEntity(
        id = imageEntity.id,
        rating = imageEntity.rating,
        tagStringGeneral = imageEntity.tagStringGeneral,
        tagStringCharacter = imageEntity.tagStringCharacter,
        tagStringCopyright = imageEntity.tagStringCopyright,
        tagStringArtist = imageEntity.tagStringArtist,
        tagStringMeta = imageEntity.tagStringMeta,
        mediaAsset = imageEntity.mediaAsset

    )
}
