package com.example.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.local.entities.ImageEntity

@Dao
interface ImagesDao {

    @Query("SELECT * FROM images WHERE orderType = :orderType LIMIT :pageSize OFFSET :pageIndex")
    suspend fun getImages(pageIndex: Int, pageSize: Int, orderType: String): List<ImageEntity>

    @Upsert
    suspend fun upsertImages(images: List<ImageEntity>)

}