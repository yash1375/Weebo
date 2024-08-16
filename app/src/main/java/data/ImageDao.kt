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
}