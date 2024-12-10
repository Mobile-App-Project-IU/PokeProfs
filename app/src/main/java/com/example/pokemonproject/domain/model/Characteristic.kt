package com.example.pokemonproject.domain.model

import org.intellij.lang.annotations.Language


data class ResponseModel(
    val descriptions: List<Description>,
)
data class Description(
    val description: String
)



