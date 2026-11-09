package com.example

import com.example.data.model.PostEntity
import com.example.data.model.UserEntity
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun user_model_createsCorrectly() {
        val user = UserEntity(
            id = 1,
            username = "alex_rivera",
            displayName = "Alex Rivera",
            email = "alex@nexora.io"
        )
        assertEquals("alex_rivera", user.username)
        assertEquals("Alex Rivera", user.displayName)
    }

    @Test
    fun post_model_createsCorrectly() {
        val post = PostEntity(
            id = 1,
            userId = 1,
            authorUsername = "alex_rivera",
            authorDisplayName = "Alex Rivera",
            authorAvatarUrl = "",
            authorVerified = true,
            contentText = "Testing Nexora post",
            mediaUrls = ""
        )
        assertEquals(1L, post.id)
        assertEquals(0, post.likesCount)
        assertFalse(post.isLiked)
    }
}
