package com.example.data.network

import com.example.data.network.dtos.image.ImageDto
import com.example.data.network.dtos.search.SearchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApiService {

    @GET(NetworkConstants.ENDPOINT_PHOTOS)
    suspend fun getImages(
        @Query(NetworkConstants.QUERY_PAGE) page: Int,
        @Query(NetworkConstants.QUERY_PER_PAGE) perPage: Int,
        @Query(NetworkConstants.QUERY_ORDER_BY) orderBy: String
    ): Response<List<ImageDto>>

    @GET(NetworkConstants.ENDPOINT_SEARCH_PHOTOS)
    suspend fun searchImages(
        @Query(NetworkConstants.QUERY_PAGE) page: Int,
        @Query(NetworkConstants.QUERY_PER_PAGE) perPage: Int,
        @Query(NetworkConstants.QUERY_SEARCH_QUERY) orderBy: String
    ): Response<SearchDto>


}