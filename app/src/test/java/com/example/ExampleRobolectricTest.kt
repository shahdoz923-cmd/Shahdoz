package com.example

import com.example.data.model.CircleEntity
import com.example.data.model.ReelEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun reel_model_initializesDefaults() {
        val reel = ReelEntity(
            id = 1,
            userId = 2,
            authorUsername = "elena_creative",
            authorDisplayName = "Elena Rostova",
            authorAvatarUrl = "",
            authorVerified = true,
            videoUrl = "https://example.com/video.mp4",
            caption = "Test reel"
        )
        assertEquals("Test reel", reel.caption)
        assertFalse(reel.isLiked)
    }

    @Test
    fun circle_model_initializesDefaults() {
        val circle = CircleEntity(
            id = 1,
            name = "Design & Architecture",
            slug = "design-architecture",
            description = "UI & visual design community",
            category = "Graphic Design",
            bannerUrl = "",
            iconUrl = "🎨",
            memberCount = 1400
        )
        assertEquals("Graphic Design", circle.category)
        assertEquals(1400, circle.memberCount)
    }
}
