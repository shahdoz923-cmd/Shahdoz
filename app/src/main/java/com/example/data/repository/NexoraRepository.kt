package com.example.data.repository

import com.example.data.dao.NexoraDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NexoraRepository(private val dao: NexoraDao) {

    val allPosts: Flow<List<PostEntity>> = dao.getAllPostsFlow()
    val followingPosts: Flow<List<PostEntity>> = dao.getFollowingPostsFlow()
    val allReels: Flow<List<ReelEntity>> = dao.getAllReelsFlow()
    val allStories: Flow<List<StoryEntity>> = dao.getAllStoriesFlow()
    val allCircles: Flow<List<CircleEntity>> = dao.getAllCirclesFlow()
    val allConversations: Flow<List<ConversationEntity>> = dao.getAllConversationsFlow()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotificationsFlow()
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsersFlow()
    val allReports: Flow<List<ReportEntity>> = dao.getAllReportsFlow()
    val savedPosts: Flow<List<PostEntity>> = dao.getSavedPostsFlow()

    fun getCommentsFor(type: String, id: Long): Flow<List<CommentEntity>> =
        dao.getCommentsFlow(type, id)

    fun getCirclePosts(circleId: Long): Flow<List<CirclePostEntity>> =
        dao.getCirclePostsFlow(circleId)

    fun getChatMessages(conversationId: Long): Flow<List<ChatMessageEntity>> =
        dao.getMessagesFlow(conversationId)

    suspend fun checkAndSeedInitialData() {
        val users = dao.getAllUsersFlow().first()
        if (users.isEmpty()) {
            seedDatabase()
        }
    }

    private suspend fun seedDatabase() {
        // Seed primary users
        val primaryUser = UserEntity(
            id = 1,
            username = "alex_rivera",
            displayName = "Alex Rivera",
            email = "alex@nexora.io",
            bio = "Visual storyteller & digital creator 📸 Exploring next-gen media & creative AI. Welcome to my NEXORA space! ✨",
            website = "https://alexrivera.design",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1200&q=80",
            isVerified = true,
            isPrivate = false,
            followersCount = 14200,
            followingCount = 482,
            postsCount = 48,
            isBusiness = true,
            businessCategory = "Digital Creator",
            role = "CREATOR"
        )

        val elena = UserEntity(
            id = 2,
            username = "elena_creative",
            displayName = "Elena Rostova",
            email = "elena@artstudio.com",
            bio = "Architecture & Minimalist UI designer. Curating aesthetic spaces and digital art. 🎨🏛️",
            website = "https://elenarostova.art",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=1200&q=80",
            isVerified = true,
            followersCount = 89500,
            followingCount = 310,
            postsCount = 120,
            isFollowing = true
        )

        val kai = UserEntity(
            id = 3,
            username = "kai_tech",
            displayName = "Kai Nakamura",
            email = "kai@cyberlab.dev",
            bio = "Next-gen hardware engineer & mobile developer. Building open-source audio tech. ⚡🎧",
            website = "https://kai.tech",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=1200&q=80",
            isVerified = true,
            followersCount = 34100,
            followingCount = 195,
            postsCount = 64,
            isFollowing = true
        )

        val maya = UserEntity(
            id = 4,
            username = "maya_photo",
            displayName = "Maya Lin",
            email = "maya@lenscraft.org",
            bio = "National geographic contributor. Wildlife & neon street photography from Tokyo to Oslo 🌌",
            website = "https://mayalin.photo",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1200&q=80",
            isVerified = true,
            followersCount = 128000,
            followingCount = 520,
            postsCount = 310,
            isFollowing = false
        )

        val quantum = UserEntity(
            id = 5,
            username = "nexora_team",
            displayName = "NEXORA Official",
            email = "support@nexora.io",
            bio = "The home of creators. Create. Share. Discover. Connect. Join the revolution. 🚀🟣",
            website = "https://nexora.social",
            avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&q=80",
            coverUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=1200&q=80",
            isVerified = true,
            followersCount = 540000,
            followingCount = 12,
            postsCount = 15,
            role = "ADMIN",
            isFollowing = true
        )

        dao.insertUsers(listOf(primaryUser, elena, kai, maya, quantum))

        // Seed initial Posts
        val post1 = PostEntity(
            id = 1,
            userId = 2,
            authorUsername = "elena_creative",
            authorDisplayName = "Elena Rostova",
            authorAvatarUrl = elena.avatarUrl,
            authorVerified = true,
            contentText = "Golden hour reflections in Kyoto. The interplay between natural wood geometry and soft evening light never fails to inspire my UI architectural palettes. What's your favourite golden hour spot? 🌅✨ #Kyoto #MinimalArchitecture #DesignInspiration #NEXORACreators",
            mediaUrls = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=1200&q=80,https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=1200&q=80",
            mediaType = "CAROUSEL",
            likesCount = 3420,
            commentsCount = 142,
            sharesCount = 520,
            savesCount = 890,
            viewsCount = 18400,
            hashtags = "#Kyoto #MinimalArchitecture #DesignInspiration",
            location = "Kyoto, Japan",
            isLiked = false,
            isSaved = true,
            isFollowingAuthor = true
        )

        val post2 = PostEntity(
            id = 2,
            userId = 3,
            authorUsername = "kai_tech",
            authorDisplayName = "Kai Nakamura",
            authorAvatarUrl = kai.avatarUrl,
            authorVerified = true,
            contentText = "Built a custom tactile audio workstation with mechanical sliders and analog meters. Latency down to 1.2ms using low-level ALSA drivers! 🎛️⚡ Full schematic dropped in the Audio Tech Circle.",
            mediaUrls = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=1200&q=80",
            mediaType = "PHOTO",
            likesCount = 1840,
            commentsCount = 68,
            sharesCount = 210,
            savesCount = 430,
            viewsCount = 9800,
            hashtags = "#Maker #AudioEngineering #Synthesizer #Tech",
            location = "CyberLab Tokyo",
            isLiked = true,
            isSaved = false,
            isFollowingAuthor = true
        )

        val post3 = PostEntity(
            id = 3,
            userId = 4,
            authorUsername = "maya_photo",
            authorDisplayName = "Maya Lin",
            authorAvatarUrl = maya.avatarUrl,
            authorVerified = true,
            contentText = "Neon fog after midnight in Shibuya. Shot on 35mm f/1.4 handheld. There is something cinematic about empty crosswalks soaked in cyan and magenta light reflections. 🌧️💜",
            mediaUrls = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&q=80",
            mediaType = "PHOTO",
            likesCount = 9850,
            commentsCount = 312,
            sharesCount = 1420,
            savesCount = 3100,
            viewsCount = 45200,
            hashtags = "#Tokyo #CyberpunkVibes #NightPhotography",
            location = "Shibuya, Tokyo",
            isLiked = false,
            isSaved = false,
            isFollowingAuthor = false
        )

        val post4 = PostEntity(
            id = 4,
            userId = 5,
            authorUsername = "nexora_team",
            authorDisplayName = "NEXORA Official",
            authorAvatarUrl = quantum.avatarUrl,
            authorVerified = true,
            contentText = "Welcome to NEXORA! 🟣\n\nA new space designed from the ground up for genuine creative expression, high-fidelity media, collaborative Circles, and authentic creator growth.\n\nTag your first post with #HelloNexora to get featured on the Explore front page! 🚀",
            mediaUrls = "",
            mediaType = "TEXT",
            likesCount = 28400,
            commentsCount = 1890,
            sharesCount = 6200,
            savesCount = 1200,
            viewsCount = 120400,
            hashtags = "#HelloNexora #NexoraLaunch #CreatorFirst",
            location = "NEXORA HQ",
            isLiked = true,
            isSaved = true,
            isFollowingAuthor = true
        )

        dao.insertPosts(listOf(post1, post2, post3, post4))

        // Seed initial Reels
        val reel1 = ReelEntity(
            id = 1,
            userId = 2,
            authorUsername = "elena_creative",
            authorDisplayName = "Elena Rostova",
            authorAvatarUrl = elena.avatarUrl,
            authorVerified = true,
            videoUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=1080&q=80",
            videoCoverUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=1080&q=80",
            caption = "3 typography rules that changed my portfolio layouts forever. Save this for your next project! ✍️📐 #DesignTips #Typography #UIUX",
            musicTitle = "Midnight Dreamscape",
            musicArtist = "Elena Rostova • Original Sound",
            likesCount = 14500,
            commentsCount = 340,
            sharesCount = 2100,
            savesCount = 4200,
            viewsCount = 89000,
            isLiked = false,
            isSaved = true,
            isFollowingAuthor = true
        )

        val reel2 = ReelEntity(
            id = 2,
            userId = 4,
            authorUsername = "maya_photo",
            authorDisplayName = "Maya Lin",
            authorAvatarUrl = maya.avatarUrl,
            authorVerified = true,
            videoUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&q=80",
            videoCoverUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&q=80",
            caption = "Color grading rain reflections in Davinci Resolve in under 30 seconds. Watch the teal & orange pop 🎥🌧️",
            musicTitle = "Tokyo Rain Lofi Beats",
            musicArtist = "Nexora Chill Lab",
            likesCount = 32800,
            commentsCount = 812,
            sharesCount = 6900,
            savesCount = 11200,
            viewsCount = 240000,
            isLiked = true,
            isSaved = false,
            isFollowingAuthor = false
        )

        val reel3 = ReelEntity(
            id = 3,
            userId = 3,
            authorUsername = "kai_tech",
            authorDisplayName = "Kai Nakamura",
            authorAvatarUrl = kai.avatarUrl,
            authorVerified = true,
            videoUrl = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=1080&q=80",
            videoCoverUrl = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=1080&q=80",
            caption = "Soldering a portable USB-C powered Eurorack module with OLED visualization! 🔊🔋",
            musicTitle = "Synthwave Pulse 140BPM",
            musicArtist = "Kai Nakamura",
            likesCount = 8400,
            commentsCount = 210,
            sharesCount = 980,
            savesCount = 1450,
            viewsCount = 54000,
            isLiked = false,
            isSaved = false,
            isFollowingAuthor = true
        )

        dao.insertReels(listOf(reel1, reel2, reel3))

        // Seed 24-hour Stories
        val story1 = StoryEntity(
            id = 1,
            userId = 1,
            authorUsername = "alex_rivera",
            authorDisplayName = "Alex Rivera",
            authorAvatarUrl = primaryUser.avatarUrl,
            mediaUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&q=80",
            caption = "Behind the scenes shooting the new spring lookbook! 📸",
            type = "PHOTO",
            isCloseFriends = false,
            isViewed = false
        )

        val story2 = StoryEntity(
            id = 2,
            userId = 2,
            authorUsername = "elena_creative",
            authorDisplayName = "Elena Rostova",
            authorAvatarUrl = elena.avatarUrl,
            mediaUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600&q=80",
            caption = "Morning espresso & Figma sessions ☕️🤍",
            type = "PHOTO",
            isCloseFriends = true,
            isViewed = false
        )

        val story3 = StoryEntity(
            id = 3,
            userId = 3,
            authorUsername = "kai_tech",
            authorDisplayName = "Kai Nakamura",
            authorAvatarUrl = kai.avatarUrl,
            mediaUrl = "",
            caption = "New audio benchmark running at 0.8ms jitter! Dropping beta code in Circles soon 🚀",
            type = "TEXT",
            bgColorHex = "#6546D9",
            isCloseFriends = false,
            isViewed = false
        )

        val story4 = StoryEntity(
            id = 4,
            userId = 4,
            authorUsername = "maya_photo",
            authorDisplayName = "Maya Lin",
            authorAvatarUrl = maya.avatarUrl,
            mediaUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&q=80",
            caption = "Spotted this eagle over the fjord today 🦅",
            type = "PHOTO",
            isCloseFriends = false,
            isViewed = false
        )

        dao.insertStories(listOf(story1, story2, story3, story4))

        // Seed Comments
        val comment1 = CommentEntity(
            id = 1,
            targetType = "POST",
            targetId = 1,
            userId = 3,
            authorUsername = "kai_tech",
            authorDisplayName = "Kai Nakamura",
            authorAvatarUrl = kai.avatarUrl,
            text = "That lighting ratio in the second frame is unbelievable! Was that natural diffuse ambient?",
            likesCount = 24
        )

        val comment2 = CommentEntity(
            id = 2,
            targetType = "POST",
            targetId = 1,
            userId = 4,
            authorUsername = "maya_photo",
            authorDisplayName = "Maya Lin",
            authorAvatarUrl = maya.avatarUrl,
            text = "Incredible sense of depth Elena. Kyoto never disappoints in spring.",
            likesCount = 18
        )

        val comment3 = CommentEntity(
            id = 3,
            targetType = "REEL",
            targetId = 1,
            userId = 1,
            authorUsername = "alex_rivera",
            authorDisplayName = "Alex Rivera",
            authorAvatarUrl = primaryUser.avatarUrl,
            text = "Rule #2 on typographic hierarchy literally solved a pitch deck issue I had this morning 🙌",
            likesCount = 12
        )

        dao.insertComment(comment1)
        dao.insertComment(comment2)
        dao.insertComment(comment3)

        // Seed NEXORA Circles (Communities)
        val circles = listOf(
            CircleEntity(
                id = 1,
                name = "Design & Architecture",
                slug = "design-architecture",
                description = "For creators passionate about minimal UI, interior spaces, typography, and visual systems.",
                category = "Graphic Design",
                bannerUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=800&q=80",
                iconUrl = "🎨",
                memberCount = 28400,
                postCount = 1420,
                isJoined = true
            ),
            CircleEntity(
                id = 2,
                name = "Cybernetics & Hardware",
                slug = "cybernetics-hardware",
                description = "Custom electronics, robotics, synthesizers, embedded chips and IoT makers.",
                category = "Technology",
                bannerUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&q=80",
                iconUrl = "⚡",
                memberCount = 19200,
                postCount = 980,
                isJoined = true
            ),
            CircleEntity(
                id = 3,
                name = "Cinematography & 35mm",
                slug = "cinematography-35mm",
                description = "Lenses, film grain, lighting rigs, grading LUTs, and visual storyboarding.",
                category = "Photography",
                bannerUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80",
                iconUrl = "📷",
                memberCount = 42100,
                postCount = 3100,
                isJoined = false
            ),
            CircleEntity(
                id = 4,
                name = "Indie Game Developers",
                slug = "indie-game-devs",
                description = "Pixel art, shader math, gameplay loops, sound design, and launch strategies.",
                category = "Gaming",
                bannerUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&q=80",
                iconUrl = "👾",
                memberCount = 31500,
                postCount = 2100,
                isJoined = false
            ),
            CircleEntity(
                id = 5,
                name = "Quantum & Deep Science",
                slug = "quantum-deep-science",
                description = "Astrophysics, biotech, quantum computing breakthroughs, and open research discussions.",
                category = "Science",
                bannerUrl = "https://images.unsplash.com/photo-1507668077129-56e32842fceb?w=800&q=80",
                iconUrl = "🔬",
                memberCount = 15300,
                postCount = 640,
                isJoined = false
            )
        )
        dao.insertCircles(circles)

        // Seed Circle Posts with Polls
        val circlePost1 = CirclePostEntity(
            id = 1,
            circleId = 1,
            circleName = "Design & Architecture",
            userId = 2,
            authorUsername = "elena_creative",
            authorDisplayName = "Elena Rostova",
            authorAvatarUrl = elena.avatarUrl,
            title = "What is your primary design tool in 2026?",
            content = "With rapid advancements in generative vector tokens and spatial design, which canvas tool remains your daily driver?",
            pollQuestion = "Which primary canvas do you use for product UI?",
            pollOptions = "Figma,Penpot,Sketch,Spline 3D",
            pollVotes = "420,110,32,85",
            userVotedIndex = 0,
            likesCount = 89,
            commentsCount = 28
        )

        val circlePost2 = CirclePostEntity(
            id = 2,
            circleId = 2,
            circleName = "Cybernetics & Hardware",
            userId = 3,
            authorUsername = "kai_tech",
            authorDisplayName = "Kai Nakamura",
            authorAvatarUrl = kai.avatarUrl,
            title = "Designing an open-source USB-C audio DAC",
            content = "Sharing test PCB layouts for our dual-differential headphone amplifier. Seeking feedback on ground plane decoupling.",
            likesCount = 142,
            commentsCount = 37
        )

        dao.insertCirclePosts(listOf(circlePost1, circlePost2))

        // Seed Direct Conversations
        val conv1 = ConversationEntity(
            id = 1,
            title = "Elena Rostova",
            avatarUrl = elena.avatarUrl,
            isGroup = false,
            lastMessage = "Sent you the moodboard link for our joint exhibition! Let me know what you think.",
            lastMessageTime = System.currentTimeMillis() - 1000 * 60 * 15,
            unreadCount = 1,
            participantUsername = "elena_creative"
        )

        val conv2 = ConversationEntity(
            id = 2,
            title = "Kai Nakamura",
            avatarUrl = kai.avatarUrl,
            isGroup = false,
            lastMessage = "The latency test on the audio board passed! We are ready for live demos.",
            lastMessageTime = System.currentTimeMillis() - 1000 * 60 * 120,
            unreadCount = 0,
            participantUsername = "kai_tech"
        )

        val conv3 = ConversationEntity(
            id = 3,
            title = "NEXORA Creator Council",
            avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&q=80",
            isGroup = true,
            lastMessage = "Alex: Exciting feature drops scheduled for this Friday!",
            lastMessageTime = System.currentTimeMillis() - 1000 * 60 * 60 * 8,
            unreadCount = 0,
            participantUsername = "nexora_team"
        )

        dao.insertConversations(listOf(conv1, conv2, conv3))

        // Seed Chat Messages
        val messages = listOf(
            ChatMessageEntity(
                conversationId = 1,
                senderId = 2,
                senderUsername = "elena_creative",
                senderAvatar = elena.avatarUrl,
                text = "Hey Alex! Loved your new carousel post on NEXORA.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 45
            ),
            ChatMessageEntity(
                conversationId = 1,
                senderId = 1,
                senderUsername = "alex_rivera",
                senderAvatar = primaryUser.avatarUrl,
                text = "Thanks Elena! The feedback has been amazing. How is the exhibition planning going?",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 30
            ),
            ChatMessageEntity(
                conversationId = 1,
                senderId = 2,
                senderUsername = "elena_creative",
                senderAvatar = elena.avatarUrl,
                text = "Sent you the moodboard link for our joint exhibition! Let me know what you think.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 15
            )
        )
        dao.insertChatMessages(messages)

        // Seed Notifications
        val notifs = listOf(
            NotificationEntity(
                userId = 1,
                type = "LIKE",
                title = "New Post Reaction",
                body = "elena_creative and 142 others liked your latest photo carousel.",
                userAvatar = elena.avatarUrl,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 10
            ),
            NotificationEntity(
                userId = 1,
                type = "FOLLOW",
                title = "New Follower",
                body = "maya_photo started following your NEXORA profile.",
                userAvatar = maya.avatarUrl,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60
            ),
            NotificationEntity(
                userId = 1,
                type = "CIRCLE",
                title = "Circle Trending",
                body = "Your post in 'Design & Architecture' reached the top trending spot today! 🔥",
                userAvatar = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=400&q=80",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 180
            ),
            NotificationEntity(
                userId = 1,
                type = "SYSTEM",
                title = "Creator Studio Milestone",
                body = "Congratulations! You crossed 14,000 followers and 120,000 weekly impressions.",
                userAvatar = quantum.avatarUrl,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24
            )
        )
        dao.insertNotifications(notifs)

        // Seed Reports for Admin/Moderation queue
        val report = ReportEntity(
            id = 1,
            targetType = "POST",
            targetId = 99,
            targetSummary = "Flagged external promotional link in comment section",
            reason = "Suspected spam / unauthorized affiliate link",
            reportedBy = "community_bot",
            status = "PENDING"
        )
        dao.insertReport(report)
    }

    // ACTIONS
    suspend fun toggleLikePost(post: PostEntity) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
        dao.updatePost(post.copy(isLiked = newLiked, likesCount = newCount))
    }

    suspend fun toggleSavePost(post: PostEntity) {
        val newSaved = !post.isSaved
        val newCount = if (newSaved) post.savesCount + 1 else maxOf(0, post.savesCount - 1)
        dao.updatePost(post.copy(isSaved = newSaved, savesCount = newCount))
    }

    suspend fun sharePost(post: PostEntity) {
        dao.updatePost(post.copy(sharesCount = post.sharesCount + 1))
    }

    suspend fun toggleLikeReel(reel: ReelEntity) {
        val newLiked = !reel.isLiked
        val newCount = if (newLiked) reel.likesCount + 1 else maxOf(0, reel.likesCount - 1)
        dao.updateReel(reel.copy(isLiked = newLiked, likesCount = newCount))
    }

    suspend fun toggleSaveReel(reel: ReelEntity) {
        val newSaved = !reel.isSaved
        val newCount = if (newSaved) reel.savesCount + 1 else maxOf(0, reel.savesCount - 1)
        dao.updateReel(reel.copy(isSaved = newSaved, savesCount = newCount))
    }

    suspend fun toggleFollowUser(user: UserEntity) {
        val newFollowing = !user.isFollowing
        val newFollowers = if (newFollowing) user.followersCount + 1 else maxOf(0, user.followersCount - 1)
        dao.updateUser(user.copy(isFollowing = newFollowing, followersCount = newFollowers))

        // Also update posts author following state
        val allCurrentPosts = dao.getAllPostsFlow().first()
        allCurrentPosts.filter { it.userId == user.id }.forEach { p ->
            dao.updatePost(p.copy(isFollowingAuthor = newFollowing))
        }
    }

    suspend fun addComment(targetType: String, targetId: Long, user: UserEntity, text: String) {
        val comment = CommentEntity(
            targetType = targetType,
            targetId = targetId,
            userId = user.id,
            authorUsername = user.username,
            authorDisplayName = user.displayName,
            authorAvatarUrl = user.avatarUrl,
            text = text.trim()
        )
        dao.insertComment(comment)

        if (targetType == "POST") {
            dao.getPostById(targetId)?.let { p ->
                dao.updatePost(p.copy(commentsCount = p.commentsCount + 1))
            }
        } else if (targetType == "REEL") {
            dao.getReelById(targetId)?.let { r ->
                dao.updateReel(r.copy(commentsCount = r.commentsCount + 1))
            }
        }
    }

    suspend fun createPost(
        user: UserEntity,
        content: String,
        mediaUrls: String,
        mediaType: String,
        hashtags: String,
        location: String
    ): Long {
        val post = PostEntity(
            userId = user.id,
            authorUsername = user.username,
            authorDisplayName = user.displayName,
            authorAvatarUrl = user.avatarUrl,
            authorVerified = user.isVerified,
            contentText = content,
            mediaUrls = mediaUrls,
            mediaType = mediaType,
            hashtags = hashtags,
            location = location,
            isFollowingAuthor = false
        )
        val postId = dao.insertPost(post)
        dao.updateUser(user.copy(postsCount = user.postsCount + 1))
        return postId
    }

    suspend fun createReel(
        user: UserEntity,
        caption: String,
        videoUrl: String,
        musicTitle: String,
        musicArtist: String
    ): Long {
        val reel = ReelEntity(
            userId = user.id,
            authorUsername = user.username,
            authorDisplayName = user.displayName,
            authorAvatarUrl = user.avatarUrl,
            authorVerified = user.isVerified,
            videoUrl = videoUrl,
            videoCoverUrl = videoUrl,
            caption = caption,
            musicTitle = musicTitle.ifBlank { "Original Sound" },
            musicArtist = musicArtist.ifBlank { user.displayName },
            isFollowingAuthor = false
        )
        return dao.insertReel(reel)
    }

    suspend fun createStory(
        user: UserEntity,
        mediaUrl: String,
        caption: String,
        type: String,
        bgColorHex: String,
        isCloseFriends: Boolean
    ): Long {
        val story = StoryEntity(
            userId = user.id,
            authorUsername = user.username,
            authorDisplayName = user.displayName,
            authorAvatarUrl = user.avatarUrl,
            mediaUrl = mediaUrl,
            caption = caption,
            type = type,
            bgColorHex = bgColorHex,
            isCloseFriends = isCloseFriends
        )
        return dao.insertStory(story)
    }

    suspend fun toggleJoinCircle(circle: CircleEntity) {
        val newJoined = !circle.isJoined
        val newMembers = if (newJoined) circle.memberCount + 1 else maxOf(0, circle.memberCount - 1)
        dao.updateCircle(circle.copy(isJoined = newJoined, memberCount = newMembers))
    }

    suspend fun createCirclePost(
        circleId: Long,
        circleName: String,
        user: UserEntity,
        title: String,
        content: String,
        pollQuestion: String,
        pollOptions: String
    ) {
        val votes = if (pollOptions.isNotBlank()) {
            pollOptions.split(",").map { "0" }.joinToString(",")
        } else ""

        val post = CirclePostEntity(
            circleId = circleId,
            circleName = circleName,
            userId = user.id,
            authorUsername = user.username,
            authorDisplayName = user.displayName,
            authorAvatarUrl = user.avatarUrl,
            title = title,
            content = content,
            pollQuestion = pollQuestion,
            pollOptions = pollOptions,
            pollVotes = votes
        )
        dao.insertCirclePost(post)
        dao.getCircleById(circleId)?.let { c ->
            dao.updateCircle(c.copy(postCount = c.postCount + 1))
        }
    }

    suspend fun voteInPoll(post: CirclePostEntity, optionIndex: Int) {
        if (post.userVotedIndex != -1) return // already voted
        val votesList = post.pollVotes.split(",").map { it.toIntOrNull() ?: 0 }.toMutableList()
        if (optionIndex in votesList.indices) {
            votesList[optionIndex] += 1
            val updatedVotes = votesList.joinToString(",")
            dao.updateCirclePost(post.copy(pollVotes = updatedVotes, userVotedIndex = optionIndex))
        }
    }

    suspend fun sendMessage(conversationId: Long, user: UserEntity, text: String): Long {
        val msg = ChatMessageEntity(
            conversationId = conversationId,
            senderId = user.id,
            senderUsername = user.username,
            senderAvatar = user.avatarUrl,
            text = text.trim()
        )
        val id = dao.insertChatMessage(msg)
        dao.getConversationById(conversationId)?.let { conv ->
            dao.updateConversation(
                conv.copy(
                    lastMessage = text.trim(),
                    lastMessageTime = System.currentTimeMillis(),
                    unreadCount = 0
                )
            )
        }
        return id
    }

    suspend fun createConversation(targetUser: UserEntity, currentUser: UserEntity): Long {
        val existing = dao.getAllConversationsFlow().first().firstOrNull {
            it.participantUsername == targetUser.username
        }
        if (existing != null) return existing.id

        val newConv = ConversationEntity(
            title = targetUser.displayName,
            avatarUrl = targetUser.avatarUrl,
            isGroup = false,
            lastMessage = "Started a new chat",
            lastMessageTime = System.currentTimeMillis(),
            unreadCount = 0,
            participantUsername = targetUser.username
        )
        return dao.insertConversation(newConv)
    }

    suspend fun updateUserProfile(user: UserEntity) {
        dao.updateUser(user)
    }

    suspend fun markAllNotificationsRead() {
        dao.markAllNotificationsRead()
    }

    suspend fun resolveReport(report: ReportEntity, newStatus: String) {
        dao.updateReport(report.copy(status = newStatus))
    }

    suspend fun submitReport(targetType: String, targetId: Long, summary: String, reason: String, reportedBy: String) {
        val report = ReportEntity(
            targetType = targetType,
            targetId = targetId,
            targetSummary = summary,
            reason = reason,
            reportedBy = reportedBy
        )
        dao.insertReport(report)
    }
}
