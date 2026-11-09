package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.NexoraDatabase
import com.example.data.model.*
import com.example.data.repository.NexoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME, EXPLORE, CREATE, REELS, PROFILE
}

enum class FeedTab {
    FOR_YOU, FOLLOWING
}

class NexoraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NexoraRepository

    init {
        val db = NexoraDatabase.getDatabase(application)
        repository = NexoraRepository(db.nexoraDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // CURRENT USER STATE
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allUsers.collect { users ->
                if (_currentUser.value == null && users.isNotEmpty()) {
                    _currentUser.value = users.first()
                } else if (_currentUser.value != null) {
                    val updated = users.find { it.id == _currentUser.value?.id }
                    if (updated != null) _currentUser.value = updated
                }
            }
        }
    }

    // NAVIGATION & OVERLAY STATES
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _feedTab = MutableStateFlow(FeedTab.FOR_YOU)
    val feedTab: StateFlow<FeedTab> = _feedTab.asStateFlow()

    private val _activeChatConversation = MutableStateFlow<ConversationEntity?>(null)
    val activeChatConversation: StateFlow<ConversationEntity?> = _activeChatConversation.asStateFlow()

    private val _activeCircleDetail = MutableStateFlow<CircleEntity?>(null)
    val activeCircleDetail: StateFlow<CircleEntity?> = _activeCircleDetail.asStateFlow()

    private val _activeStory = MutableStateFlow<StoryEntity?>(null)
    val activeStory: StateFlow<StoryEntity?> = _activeStory.asStateFlow()

    private val _activeProfileUser = MutableStateFlow<UserEntity?>(null)
    val activeProfileUser: StateFlow<UserEntity?> = _activeProfileUser.asStateFlow()

    private val _commentsTarget = MutableStateFlow<Pair<String, Long>?>(null) // ("POST" or "REEL", id)
    val commentsTarget: StateFlow<Pair<String, Long>?> = _commentsTarget.asStateFlow()

    private val _showCreatorStudio = MutableStateFlow(false)
    val showCreatorStudio: StateFlow<Boolean> = _showCreatorStudio.asStateFlow()

    private val _showAdminPanel = MutableStateFlow(false)
    val showAdminPanel: StateFlow<Boolean> = _showAdminPanel.asStateFlow()

    private val _showSettings = MutableStateFlow(false)
    val showSettings: StateFlow<Boolean> = _showSettings.asStateFlow()

    private val _showMessages = MutableStateFlow(false)
    val showMessages: StateFlow<Boolean> = _showMessages.asStateFlow()

    private val _showNotifications = MutableStateFlow(false)
    val showNotifications: StateFlow<Boolean> = _showNotifications.asStateFlow()

    private val _showCircles = MutableStateFlow(false)
    val showCircles: StateFlow<Boolean> = _showCircles.asStateFlow()

    private val _showAuthModal = MutableStateFlow(false)
    val showAuthModal: StateFlow<Boolean> = _showAuthModal.asStateFlow()

    private val _reportTarget = MutableStateFlow<Pair<String, Long>?>(null)
    val reportTarget: StateFlow<Pair<String, Long>?> = _reportTarget.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _exploreCategory = MutableStateFlow("All")
    val exploreCategory: StateFlow<String> = _exploreCategory.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // DATA FLOWS
    val allPosts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val followingPosts: StateFlow<List<PostEntity>> = repository.followingPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReels: StateFlow<List<ReelEntity>> = repository.allReels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCircles: StateFlow<List<CircleEntity>> = repository.allCircles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allConversations: StateFlow<List<ConversationEntity>> = repository.allConversations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPosts: StateFlow<List<PostEntity>> = repository.savedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getComments(type: String, id: Long): Flow<List<CommentEntity>> =
        repository.getCommentsFor(type, id)

    fun getCirclePosts(circleId: Long): Flow<List<CirclePostEntity>> =
        repository.getCirclePosts(circleId)

    fun getChatMessages(convId: Long): Flow<List<ChatMessageEntity>> =
        repository.getChatMessages(convId)

    // NAVIGATION ACTIONS
    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setFeedTab(tab: FeedTab) {
        _feedTab.value = tab
    }

    fun openChat(conv: ConversationEntity) {
        _activeChatConversation.value = conv
        _showMessages.value = true
    }

    fun closeChat() {
        _activeChatConversation.value = null
    }

    fun openCircle(circle: CircleEntity) {
        _activeCircleDetail.value = circle
    }

    fun closeCircle() {
        _activeCircleDetail.value = null
    }

    fun openStory(story: StoryEntity) {
        _activeStory.value = story
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun openUserProfile(user: UserEntity) {
        _activeProfileUser.value = user
    }

    fun closeUserProfile() {
        _activeProfileUser.value = null
    }

    fun openComments(type: String, id: Long) {
        _commentsTarget.value = Pair(type, id)
    }

    fun closeComments() {
        _commentsTarget.value = null
    }

    fun toggleCreatorStudio(show: Boolean) {
        _showCreatorStudio.value = show
    }

    fun toggleAdminPanel(show: Boolean) {
        _showAdminPanel.value = show
    }

    fun toggleSettings(show: Boolean) {
        _showSettings.value = show
    }

    fun toggleMessages(show: Boolean) {
        _showMessages.value = show
    }

    fun toggleNotifications(show: Boolean) {
        _showNotifications.value = show
    }

    fun toggleCircles(show: Boolean) {
        _showCircles.value = show
    }

    fun toggleAuthModal(show: Boolean) {
        _showAuthModal.value = show
    }

    fun openReport(type: String, id: Long) {
        _reportTarget.value = Pair(type, id)
    }

    fun closeReport() {
        _reportTarget.value = null
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun setExploreCategory(cat: String) {
        _exploreCategory.value = cat
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // SOCIAL INTERACTIONS
    fun toggleLikePost(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleLikePost(post)
        }
    }

    fun toggleSavePost(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleSavePost(post)
            showToast(if (!post.isSaved) "Post saved to your collection" else "Post removed from saved")
        }
    }

    fun sharePost(post: PostEntity) {
        viewModelScope.launch {
            repository.sharePost(post)
            showToast("Post link copied & shared!")
        }
    }

    fun toggleLikeReel(reel: ReelEntity) {
        viewModelScope.launch {
            repository.toggleLikeReel(reel)
        }
    }

    fun toggleSaveReel(reel: ReelEntity) {
        viewModelScope.launch {
            repository.toggleSaveReel(reel)
            showToast(if (!reel.isSaved) "Reel saved" else "Reel removed from saved")
        }
    }

    fun toggleFollowUser(user: UserEntity) {
        viewModelScope.launch {
            repository.toggleFollowUser(user)
            showToast(if (!user.isFollowing) "Following @${user.username}" else "Unfollowed @${user.username}")
        }
    }

    fun addComment(text: String) {
        val target = _commentsTarget.value ?: return
        val user = _currentUser.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(target.first, target.second, user, text)
            showToast("Comment posted")
        }
    }

    fun createPost(
        content: String,
        mediaUrls: String,
        mediaType: String,
        hashtags: String,
        location: String
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createPost(user, content, mediaUrls, mediaType, hashtags, location)
            _currentTab.value = ScreenTab.HOME
            showToast("Post published to NEXORA! 🚀")
        }
    }

    fun createReel(caption: String, videoUrl: String, musicTitle: String, musicArtist: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createReel(user, caption, videoUrl, musicTitle, musicArtist)
            _currentTab.value = ScreenTab.REELS
            showToast("Reel uploaded successfully! 🎬")
        }
    }

    fun createStory(mediaUrl: String, caption: String, type: String, bgColorHex: String, isCloseFriends: Boolean) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createStory(user, mediaUrl, caption, type, bgColorHex, isCloseFriends)
            _currentTab.value = ScreenTab.HOME
            showToast("Story shared! Active for 24 hours ⏱️")
        }
    }

    fun toggleJoinCircle(circle: CircleEntity) {
        viewModelScope.launch {
            repository.toggleJoinCircle(circle)
            showToast(if (!circle.isJoined) "Joined ${circle.name}!" else "Left ${circle.name}")
        }
    }

    fun createCirclePost(
        circleId: Long,
        circleName: String,
        title: String,
        content: String,
        pollQuestion: String,
        pollOptions: String
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createCirclePost(circleId, circleName, user, title, content, pollQuestion, pollOptions)
            showToast("Community discussion created! 💬")
        }
    }

    fun votePoll(post: CirclePostEntity, optionIndex: Int) {
        viewModelScope.launch {
            repository.voteInPoll(post, optionIndex)
            showToast("Vote recorded!")
        }
    }

    fun sendChatMessage(text: String) {
        val conv = _activeChatConversation.value ?: return
        val user = _currentUser.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(conv.id, user, text)
        }
    }

    fun startDirectMessage(targetUser: UserEntity) {
        val current = _currentUser.value ?: return
        viewModelScope.launch {
            val convId = repository.createConversation(targetUser, current)
            val all = repository.allConversations.first()
            val conv = all.find { it.id == convId }
            if (conv != null) {
                openChat(conv)
            }
        }
    }

    fun updateProfile(displayName: String, bio: String, website: String, isPrivate: Boolean, isBusiness: Boolean, category: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateUserProfile(
                user.copy(
                    displayName = displayName.trim(),
                    bio = bio.trim(),
                    website = website.trim(),
                    isPrivate = isPrivate,
                    isBusiness = isBusiness,
                    businessCategory = category
                )
            )
            showToast("Profile updated successfully ✨")
        }
    }

    fun toggleTwoFactor() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val new2FA = !user.twoFactorEnabled
            repository.updateUserProfile(user.copy(twoFactorEnabled = new2FA))
            showToast(if (new2FA) "Two-Factor Authentication enabled 🔐" else "2FA disabled")
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun submitReport(reason: String) {
        val target = _reportTarget.value ?: return
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.submitReport(
                targetType = target.first,
                targetId = target.second,
                summary = "Reported by @${user.username}",
                reason = reason,
                reportedBy = user.username
            )
            _reportTarget.value = null
            showToast("Report submitted to NEXORA moderation team.")
        }
    }

    fun resolveReport(report: ReportEntity, status: String) {
        viewModelScope.launch {
            repository.resolveReport(report, status)
            showToast("Report marked as $status")
        }
    }

    fun switchUser(user: UserEntity) {
        _currentUser.value = user
        showToast("Switched account to @${user.username}")
        _showAuthModal.value = false
    }

    fun registerNewUser(username: String, displayName: String, email: String) {
        viewModelScope.launch {
            val newUser = UserEntity(
                username = username.lowercase().replace(" ", "_"),
                displayName = displayName,
                email = email,
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&q=80",
                bio = "New creator on NEXORA! 👋",
                followersCount = 0,
                followingCount = 0,
                postsCount = 0
            )
            val db = NexoraDatabase.getDatabase(getApplication())
            val id = db.nexoraDao().insertUser(newUser)
            val created = db.nexoraDao().getUserById(id)
            if (created != null) {
                _currentUser.value = created
                showToast("Account created! Welcome @${created.username} 🎉")
                _showAuthModal.value = false
            }
        }
    }
}
