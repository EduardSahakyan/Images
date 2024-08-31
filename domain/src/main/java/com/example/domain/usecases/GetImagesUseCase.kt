package com.example.domain.usecases

import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType
import com.example.domain.repositories.ImagesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    operator fun invoke(page: Int, perPage: Int, orderType: ImageOrderType): Flow<Resource<List<Image>>> {
        return imagesRepository.getImages(page, perPage, orderType)
    }

}