package com.example.domain.usecases

import com.example.domain.models.Image
import com.example.domain.repositories.ImagesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    operator fun invoke(query: String, page: Int, perPage: Int): Flow<Resource<List<Image>>> {
        return imagesRepository.searchImages(query, page, perPage)
    }

}