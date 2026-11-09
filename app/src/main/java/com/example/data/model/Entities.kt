package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val displayName: String,
    val email: String,
    val passwordHash: String = "hashed_pw",
    val bio: String = "",
    val website: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val isBusiness: Boolean = false,
    val businessCategory: String = "Digital Creator",
    val twoFactorEnabled: Boolean = false,
    val role: String = "USER", // "USER", "CREATOR", "MODERATOR", "ADMIN"
    val isFollowing: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val authorVerified: Boolean,
    val contentText: String,
    val mediaUrls: String, // Comma separated URLs or drawable keys
    val mediaType: String = "PHOTO", // PHOTO, CAROUSEL, TEXT
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val savesCount: Int = 0,
    val viewsCount: Int = 0,
    val hashtags: String = "",
    val location: String = "",
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowingAuthor: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reels")
data class ReelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val authorVerified: Boolean,
    val videoUrl: String,
    val videoCoverUrl: String = "",
    val caption: String,
    val musicTitle: String = "Original Audio",
    val musicArtist: String = "Nexora Sound Lab",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val savesCount: Int = 0,
    val viewsCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowingAuthor: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val mediaUrl: String,
    val caption: String = "",
    val type: String = "PHOTO", // PHOTO, TEXT
    val bgColorHex: String = "#6546D9",
    val isCloseFriends: Boolean = false,
    val isViewed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetType: String, // "POST" or "REEL"
    val targetId: Long,
    val userId: Long,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val text: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "circles")
data class CircleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val slug: String,
    val description: String,
    val category: String, // "Science", "Technology", "Education", "Gaming", "Photography", "Art", etc.
    val bannerUrl: String,
    val iconUrl: String,
    val memberCount: Int = 0,
    val postCount: Int = 0,
    val isJoined: Boolean = false,
    val rules: String = "Be respectful. No spam. Keep content relevant."
)

@Entity(tableName = "circle_posts")
data class CirclePostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val circleId: Long,
    val circleName: String,
    val userId: Long,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val title: String,
    val content: String,
    val pollQuestion: String = "",
    val pollOptions: String = "", // Comma-separated or option list
    val pollVotes: String = "", // Comma-separated counts
    val userVotedIndex: Int = -1,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val avatarUrl: String,
    val isGroup: Boolean = false,
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val participantUsername: String = ""
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long,
    val senderId: Long,
    val senderUsername: String,
    val senderAvatar: String,
    val text: String,
    val mediaUrl: String = "",
    val isRead: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: String, // "LIKE", "COMMENT", "FOLLOW", "CIRCLE", "SYSTEM"
    val title: String,
    val body: String,
    val userAvatar: String,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetType: String, // "POST", "REEL", "USER", "CIRCLE"
    val targetId: Long,
    val targetSummary: String,
    val reason: String,
    val reportedBy: String,
    val status: String = "PENDING", // PENDING, RESOLVED, DISMISSED
    val timestamp: Long = System.currentTimeMillis()
)
