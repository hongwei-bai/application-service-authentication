package com.hongwei.model.privilege

data class PhotoPrivilege(
        val all: Boolean? = false,
        val byAlbum: List<String>? = emptyList()
)