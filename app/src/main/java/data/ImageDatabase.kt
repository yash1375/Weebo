package data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ImageEntity::class,FavEntity::class], version = 1)
@TypeConverters(TypeConvertVariants::class)
abstract class ImageDatabase : RoomDatabase(){
    abstract fun ImageDao() : ImageDao
}