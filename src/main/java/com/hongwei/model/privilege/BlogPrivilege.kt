package com.hongwei.model.privilege

data class BlogPrivilege(
        val main: Boolean = false,
        val readAllBlogs: Boolean = false,
        val readBlogByAuthors: List<String>,
        val postBlog: Boolean
)