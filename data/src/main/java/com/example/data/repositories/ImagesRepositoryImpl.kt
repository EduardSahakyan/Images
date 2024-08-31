package com.example.data.repositories

import com.example.data.local.ImagesDao
import com.example.data.mappers.toEntities
import com.example.data.mappers.toImages
import com.example.data.network.ImagesApiService
import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType
import com.example.domain.repositories.ImagesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val apiService: ImagesApiService,
    private val dao: ImagesDao
) : ImagesRepository {

    override fun searchImages(query: String, page: Int, perPage: Int): Flow<Resource<List<Image>>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.searchImages(page, perPage, query)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        val images = it.results.toImages()
                        emit(Resource.Success(images))
                    }
                } else {
                    emit(Resource.Error(Exception("Something gone wrong")))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    override fun getImages(page: Int, perPage: Int, orderType: ImageOrderType): Flow<Resource<List<Image>>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.getImages(page, perPage, orderType.name.lowercase())
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        val entities = it.toEntities(orderType)
                        dao.upsertImages(entities)
                        val images = entities.toImages()
                        emit(Resource.Success(images))
                    }
                } else {
                    emit(tryLoadFromCache(page, perPage, orderType))
                }
            } catch (e: Exception) {
                emit(tryLoadFromCache(page, perPage, orderType))
            }
        }
    }

    private suspend fun tryLoadFromCache(
        page: Int,
        perPage: Int,
        orderType: ImageOrderType,
    ): Resource<List<Image>> {
        val pageIndex = (page - 1) * perPage
        val cached = dao.getImages(pageIndex, perPage, orderType.name)
        return if (cached.isEmpty()) {
            Resource.Error(Exception("Something gone wrong"))
        } else {
            Resource.Success(cached.toImages())
        }
    }

}