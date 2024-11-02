package data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface ImageDao {
    @Query("SELECT * FROM Image")
    fun getAll() : PagingSource<Int,ImageEntity>
    @Query("SELECT COUNT(*) FROM Image")
    suspend fun getCount() : Int
    @Upsert()
    suspend fun insert(image: List<ImageEntity>)
    @Query("DELETE FROM Image")
    suspend fun clearAll()
    @Query("SELECT * FROM Image WHERE rating == :tag")
    fun getSfw(tag:Char = 'g') : PagingSource<Int,ImageEntity>
    @Query("SELECT * FROM Fav")
    fun getFavAll() : PagingSource<Int,FavEntity>
    @Query("SELECT id FROM Fav")
    suspend fun getAllfav() : List<Int>
}