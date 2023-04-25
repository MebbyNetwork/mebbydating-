package com.example.mebby.domain.models.city

data class GeoDBModel(
    val data: List<CityModel>,
    val linkModels: List<LinkModel>,
    val metadataModel: MetadataModel
)