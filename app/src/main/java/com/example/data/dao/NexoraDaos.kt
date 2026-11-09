package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NexoraDao {
    // USERS
    @Query("SELECT * FROM users ORDER BY followersCount DESC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    // POSTS
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isFollowingAuthor = 1 ORDER BY createdAt DESC")
    fun getFollowingPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUserId(userId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isSaved = 1 ORDER BY createdAt DESC")
    fun getSavedPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)

    // REELS
    @Query("SELECT * FROM reels ORDER BY createdAt DESC")
    fun getAllReelsFlow(): Flow<List<ReelEntity>>

    @Query("SELECT * FROM reels WHERE id = :id LIMIT 1")
    suspend fun getReelById(id: Long): ReelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReel(reel: ReelEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReels(reels: List<ReelEntity>)

    @Update
    suspend fun updateReel(reel: ReelEntity)

    // STORIES
    @Query("SELECT * FROM stories ORDER BY createdAt DESC")
    fun getAllStoriesFlow(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Update
    suspend fun updateStory(story: StoryEntity)

    // COMMENTS
    @Query("SELECT * FROM comments WHERE targetType = :targetType AND targetId = :targetId ORDER BY createdAt ASC")
    fun getCommentsFlow(targetType: String, targetId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    // CIRCLES
    @Query("SELECT * FROM circles ORDER BY memberCount DESC")
    fun getAllCirclesFlow(): Flow<List<CircleEntity>>

    @Query("SELECT * FROM circles WHERE id = :id LIMIT 1")
    suspend fun getCircleById(id: Long): CircleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCircle(circle: CircleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCircles(circles: List<CircleEntity>)

    @Update
    suspend fun updateCircle(circle: CircleEntity)

    // CIRCLE POSTS
    @Query("SELECT * FROM circle_posts WHERE circleId = :circleId ORDER BY createdAt DESC")
    fun getCirclePostsFlow(circleId: Long): Flow<List<CirclePostEntity>>

    @Query("SELECT * FROM circle_posts ORDER BY createdAt DESC")
    fun getAllCirclePostsFlow(): Flow<List<CirclePostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCirclePost(post: CirclePostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCirclePosts(posts: List<CirclePostEntity>)

    @Update
    suspend fun updateCirclePost(post: CirclePostEntity)

    // CONVERSATIONS
    @Query("SELECT * FROM conversations ORDER BY lastMessageTime DESC")
    fun getAllConversationsFlow(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id LIMIT 1")
    suspend fun getConversationById(id: Long): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conv: ConversationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(convs: List<ConversationEntity>)

    @Update
    suspend fun updateConversation(conv: ConversationEntity)

    // CHAT MESSAGES
    @Query("SELECT * FROM chat_messages WHERE conversationId = :convId ORDER BY timestamp ASC")
    fun getMessagesFlow(convId: Long): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(msg: ChatMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(msgs: List<ChatMessageEntity>)

    // NOTIFICATIONS
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsRead()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notif: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifs: List<NotificationEntity>)

    // REPORTS (ADMIN & MODERATION)
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReportsFlow(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<ReportEntity>)

    @Update
    suspend fun updateReport(report: ReportEntity)
}
