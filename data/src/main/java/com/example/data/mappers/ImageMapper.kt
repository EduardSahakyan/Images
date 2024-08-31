package com.example.data.mappers

import com.example.data.local.entities.ImageEntity
import com.example.data.network.dtos.image.ImageDto
import com.example.data.utils.convertDate
import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType

private fun ImageDto.toEntity(orderType: ImageOrderType) = ImageEntity(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = urls.raw,
    thumbnailUrl = urls.thumbnail,
    ownerName = owner.name,
    updatedAt = updatedAt,
    orderType = orderType.name
)

private fun ImageEntity.toImage() = Image(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl,
    ownerName = ownerName,
    updatedAt = convertDate(updatedAt)
)

private fun ImageDto.toImage() = Image(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = urls.raw,
    thumbnailUrl = urls.thumbnail,
    ownerName = owner.name,
    updatedAt = convertDate(updatedAt),
)

fun List<ImageDto>.toEntities(orderType: ImageOrderType) = map { it.toEntity(orderType) }

@JvmName("ImageDtoToImage")
fun List<ImageEntity>.toImages() = map { it.toImage() }

fun List<ImageDto>.toImages() = map { it.toImage() }