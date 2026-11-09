package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NexoraAccentPink
import com.example.ui.theme.NexoraPrimary
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NexoraApp()
            }
        }
    }
}

@Composable
fun NexoraApp(viewModel: NexoraViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val feedTab by viewModel.feedTab.collectAsStateWithLifecycle()

    val allPosts by viewModel.allPosts.collectAsStateWithLifecycle()
    val followingPosts by viewModel.followingPosts.collectAsStateWithLifecycle()
    val allReels by viewModel.allReels.collectAsStateWithLifecycle()
    val allStories by viewModel.allStories.collectAsStateWithLifecycle()
    val allCircles by viewModel.allCircles.collectAsStateWithLifecycle()
    val allConversations by viewModel.allConversations.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val savedPosts by viewModel.savedPosts.collectAsStateWithLifecycle()
    val allReports by viewModel.allReports.collectAsStateWithLifecycle()

    val activeStory by viewModel.activeStory.collectAsStateWithLifecycle()
    val activeChatConversation by viewModel.activeChatConversation.collectAsStateWithLifecycle()
    val activeCircleDetail by viewModel.activeCircleDetail.collectAsStateWithLifecycle()
    val activeProfileUser by viewModel.activeProfileUser.collectAsStateWithLifecycle()
    val commentsTarget by viewModel.commentsTarget.collectAsStateWithLifecycle()
    val reportTarget by viewModel.reportTarget.collectAsStateWithLifecycle()

    val showCreatorStudio by viewModel.showCreatorStudio.collectAsStateWithLifecycle()
    val showAdminPanel by viewModel.showAdminPanel.collectAsStateWithLifecycle()
    val showSettings by viewModel.showSettings.collectAsStateWithLifecycle()
    val showMessages by viewModel.showMessages.collectAsStateWithLifecycle()
    val showNotifications by viewModel.showNotifications.collectAsStateWithLifecycle()
    val showCircles by viewModel.showCircles.collectAsStateWithLifecycle()
    val showAuthModal by viewModel.showAuthModal.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val exploreCategory by viewModel.exploreCategory.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearToast()
        }
    }

    // BACK HANDLER
    BackHandler(
        enabled = activeStory != null ||
                commentsTarget != null ||
                reportTarget != null ||
                activeChatConversation != null ||
                showMessages ||
                showNotifications ||
                showCreatorStudio ||
                showAdminPanel ||
                showSettings ||
                activeCircleDetail != null ||
                showCircles ||
                activeProfileUser != null ||
                showAuthModal ||
                currentTab != ScreenTab.HOME
    ) {
        when {
            activeStory != null -> viewModel.closeStory()
            commentsTarget != null -> viewModel.closeComments()
            reportTarget != null -> viewModel.closeReport()
            activeChatConversation != null -> viewModel.closeChat()
            showMessages -> viewModel.toggleMessages(false)
            showNotifications -> viewModel.toggleNotifications(false)
            showCreatorStudio -> viewModel.toggleCreatorStudio(false)
            showAdminPanel -> viewModel.toggleAdminPanel(false)
            showSettings -> viewModel.toggleSettings(false)
            activeCircleDetail != null -> viewModel.closeCircle()
            showCircles -> viewModel.toggleCircles(false)
            activeProfileUser != null -> viewModel.closeUserProfile()
            showAuthModal -> viewModel.toggleAuthModal(false)
            currentTab != ScreenTab.HOME -> viewModel.setTab(ScreenTab.HOME)
        }
    }

    val unreadMessages = allConversations.sumOf { it.unreadCount }
    val unreadNotifs = allNotifications.count { !it.isRead }

    val isFullScreenOverlay = activeStory != null ||
            showMessages ||
            showNotifications ||
            showCircles ||
            showCreatorStudio ||
            showAdminPanel ||
            showSettings ||
            activeProfileUser != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isFullScreenOverlay && currentTab != ScreenTab.REELS) {
                NexoraTopAppBar(
                    viewModel = viewModel,
                    unreadMessages = unreadMessages,
                    unreadNotifs = unreadNotifs
                )
            }
        },
        bottomBar = {
            if (!isFullScreenOverlay) {
                NexoraBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) },
                    currentUserAvatar = currentUser?.avatarUrl ?: ""
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (!isFullScreenOverlay && currentTab != ScreenTab.REELS) innerPadding.calculateTopPadding() else 0.dp,
                    bottom = if (!isFullScreenOverlay) innerPadding.calculateBottomPadding() else 0.dp
                )
        ) {
            // BASE TABS
            when (currentTab) {
                ScreenTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        currentUser = currentUser,
                        allPosts = allPosts,
                        followingPosts = followingPosts,
                        feedTab = feedTab,
                        allStories = allStories
                    )
                }
                ScreenTab.EXPLORE -> {
                    ExploreScreen(
                        viewModel = viewModel,
                        allPosts = allPosts,
                        allReels = allReels,
                        allUsers = allUsers,
                        searchQuery = searchQuery,
                        exploreCategory = exploreCategory
                    )
                }
                ScreenTab.CREATE -> {
                    CreateScreen(viewModel = viewModel)
                }
                ScreenTab.REELS -> {
                    ReelsScreen(viewModel = viewModel, allReels = allReels)
                }
                ScreenTab.PROFILE -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        user = currentUser,
                        isSelf = true,
                        allPosts = allPosts,
                        allReels = allReels,
                        savedPosts = savedPosts
                    )
                }
            }

            // FULL SCREEN OVERLAYS & SHEETS
            if (activeProfileUser != null) {
                ProfileScreen(
                    viewModel = viewModel,
                    user = activeProfileUser,
                    isSelf = activeProfileUser?.id == currentUser?.id,
                    allPosts = allPosts,
                    allReels = allReels,
                    savedPosts = savedPosts,
                    onBack = { viewModel.closeUserProfile() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showCircles) {
                CirclesScreen(
                    viewModel = viewModel,
                    allCircles = allCircles,
                    activeCircle = activeCircleDetail,
                    onClose = { viewModel.toggleCircles(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showMessages) {
                MessagesScreen(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    conversations = allConversations,
                    activeConversation = activeChatConversation,
                    onClose = { viewModel.toggleMessages(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showNotifications) {
                NotificationsScreen(
                    viewModel = viewModel,
                    notifications = allNotifications,
                    onClose = { viewModel.toggleNotifications(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showCreatorStudio) {
                CreatorStudioScreen(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    allPosts = allPosts,
                    allReels = allReels,
                    onClose = { viewModel.toggleCreatorStudio(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showAdminPanel) {
                AdminScreen(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    reports = allReports,
                    totalUsersCount = allUsers.size,
                    totalPostsCount = allPosts.size,
                    totalReelsCount = allReels.size,
                    onClose = { viewModel.toggleAdminPanel(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (showSettings) {
                SettingsScreen(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    allUsers = allUsers,
                    onClose = { viewModel.toggleSettings(false) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (activeStory != null) {
                StoryViewerScreen(
                    story = activeStory!!,
                    viewModel = viewModel,
                    onClose = { viewModel.closeStory() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // COMMENTS BOTTOM SHEET
            if (commentsTarget != null) {
                CommentsBottomSheet(
                    targetType = commentsTarget!!.first,
                    targetId = commentsTarget!!.second,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeComments() }
                )
            }

            // REPORT DIALOG
            if (reportTarget != null) {
                ReportDialog(
                    targetType = reportTarget!!.first,
                    targetId = reportTarget!!.second,
                    onDismiss = { viewModel.closeReport() },
                    onSubmit = { reason -> viewModel.submitReport(reason) }
                )
            }

            // AUTH DIALOG
            if (showAuthModal) {
                AuthDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.toggleAuthModal(false) }
                )
            }
        }
    }
}

@Composable
fun ReportDialog(
    targetType: String,
    targetId: Long,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val reportReasons = listOf(
        "Spam or misleading promotional content",
        "Harassment, hate speech, or bullying",
        "Violence or dangerous content",
        "Intellectual property violation",
        "Inappropriate media for general audience"
    )

    var selectedReason by remember { mutableStateOf(reportReasons.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Content to Moderation", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Reporting $targetType #$targetId. NEXORA takes community safety seriously.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                reportReasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(reason, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = NexoraAccentPink),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Submit Report")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
