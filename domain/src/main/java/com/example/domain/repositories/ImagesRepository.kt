package com.example.domain.repositories

import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImages(page: Int, perPage: Int, orderType: ImageOrderType): Flow<Resource<List<Image>>>

    fun searchImages(query: String, page: Int, perPage: Int): Flow<Resource<List<Image>>>

}