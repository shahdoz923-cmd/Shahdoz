package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.FeedTab
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.ScreenTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NexoraTopAppBar(
    viewModel: NexoraViewModel,
    unreadMessages: Int,
    unreadNotifs: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // BRAND LOGO & TITLE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .testTag("brand_header")
                    .clickable { viewModel.setTab(ScreenTab.HOME) }
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NexoraPrimary, NexoraSecondary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "N",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "NEXORA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            // ACTIONS: Circles, Notifications, Direct Messages
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Circles button
                IconButton(
                    onClick = { viewModel.toggleCircles(true) },
                    modifier = Modifier.testTag("btn_top_circles")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = "NEXORA Circles",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Notifications with badge
                BadgedBox(
                    badge = {
                        if (unreadNotifs > 0) {
                            Badge(
                                containerColor = NexoraAccentPink,
                                contentColor = Color.White
                            ) {
                                Text(unreadNotifs.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { viewModel.toggleNotifications(true) },
                        modifier = Modifier.testTag("btn_top_notifications")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Direct Messages with badge
                BadgedBox(
                    badge = {
                        if (unreadMessages > 0) {
                            Badge(
                                containerColor = NexoraPrimary,
                                contentColor = Color.White
                            ) {
                                Text(unreadMessages.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { viewModel.toggleMessages(true) },
                        modifier = Modifier.testTag("btn_top_messages")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Direct Messages",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NexoraBottomBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    currentUserAvatar: String,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        NavigationBarItem(
            selected = currentTab == ScreenTab.HOME,
            onClick = { onTabSelected(ScreenTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_home"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NexoraPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.EXPLORE,
            onClick = { onTabSelected(ScreenTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.EXPLORE) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_explore"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NexoraPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.CREATE,
            onClick = { onTabSelected(ScreenTab.CREATE) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NexoraPrimary, NexoraSecondary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            label = { Text("Create", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_create"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NexoraPrimary,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.REELS,
            onClick = { onTabSelected(ScreenTab.REELS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.REELS) Icons.Filled.MovieCreation else Icons.Outlined.MovieCreation,
                    contentDescription = "Reels"
                )
            },
            label = { Text("Reels", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_reels"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NexoraPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.PROFILE,
            onClick = { onTabSelected(ScreenTab.PROFILE) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (currentTab == ScreenTab.PROFILE) 2.dp else 1.dp,
                            color = if (currentTab == ScreenTab.PROFILE) NexoraPrimary else Color.Transparent,
                            shape = CircleShape
                        )
                ) {
                    AsyncImage(
                        model = currentUserAvatar,
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            label = { Text("Profile", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_profile"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NexoraPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
fun StoryAvatarItem(
    story: StoryEntity?,
    isCurrentUser: Boolean,
    currentUserAvatar: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val avatarUrl = if (isCurrentUser) currentUserAvatar else story?.authorAvatarUrl ?: ""
        val username = if (isCurrentUser) "Your Story" else story?.authorUsername ?: ""

        val borderBrush = when {
            isCurrentUser && story == null -> Brush.linearGradient(listOf(Color.LightGray, Color.Gray))
            story?.isCloseFriends == true -> Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
            story?.isViewed == true -> Brush.linearGradient(listOf(Color.LightGray, Color.LightGray))
            else -> Brush.sweepGradient(listOf(NexoraPrimary, NexoraAccentPink, NexoraSecondary, NexoraPrimary))
        }

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(borderBrush)
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(2.dp)
            ) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = username,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            if (isCurrentUser && story == null) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(NexoraPrimary)
                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Story",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = username,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.widthIn(max = 68.dp)
        )
    }
}

@Composable
fun PostCard(
    post: PostEntity,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onCommentClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var showHeartAnimation by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onAuthorClick() }
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, NexoraPrimary.copy(alpha = 0.3f), CircleShape)
                    ) {
                        AsyncImage(
                            model = post.authorAvatarUrl,
                            contentDescription = post.authorDisplayName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorUsername,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (post.authorVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = NexoraVerifiedBlue,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        if (post.location.isNotBlank()) {
                            Text(
                                text = post.location,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // More Options (Report / Share)
                IconButton(
                    onClick = { onReportClick() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Post Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // MEDIA CONTENT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!post.isLiked) onLike()
                                showHeartAnimation = true
                                coroutineScope.launch {
                                    delay(800)
                                    showHeartAnimation = false
                                }
                            }
                        )
                    }
            ) {
                if (post.mediaType == "TEXT" || post.mediaUrls.isBlank()) {
                    // Stylized Text Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        NexoraPrimary.copy(alpha = 0.9f),
                                        NexoraSecondary.copy(alpha = 0.85f)
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.contentText,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        )
                    }
                } else {
                    val mediaList = post.mediaUrls.split(",").filter { it.isNotBlank() }
                    if (mediaList.size > 1) {
                        // Carousel
                        val pagerState = rememberPagerState { mediaList.size }
                        Box(modifier = Modifier.fillMaxWidth().height(360.dp)) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                AsyncImage(
                                    model = mediaList[page],
                                    contentDescription = "Photo ${page + 1}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Dot indicator
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(mediaList.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) NexoraPrimary else Color.White.copy(alpha = 0.6f)
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(6.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        // Single Image
                        AsyncImage(
                            model = mediaList.firstOrNull() ?: "",
                            contentDescription = "Post photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(360.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Animated double tap heart
                androidx.compose.animation.AnimatedVisibility(
                    visible = showHeartAnimation,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Liked",
                        tint = NexoraAccentPink,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            // ACTION BAR (Like, Comment, Share, Save)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier.testTag("btn_like_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) NexoraAccentPink else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onCommentClick,
                        modifier = Modifier.testTag("btn_comment_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.testTag("btn_share_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("btn_save_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (post.isSaved) NexoraPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // ENGAGEMENT STATS & CAPTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp)
            ) {
                if (post.likesCount > 0) {
                    Text(
                        text = "${post.likesCount} likes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (post.contentText.isNotBlank() && post.mediaType != "TEXT") {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        Text(
                            text = "${post.authorUsername} ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = post.contentText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (post.commentsCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "View all ${post.commentsCount} comments",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { onCommentClick() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    targetType: String,
    targetId: Long,
    viewModel: NexoraViewModel,
    onDismiss: () -> Unit
) {
    val comments by viewModel.getComments(targetType, targetId).collectAsState(initial = emptyList())
    var commentInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
        ) {
            Text(
                text = "Comments (${comments.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // COMMENTS LIST
            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            tint = NexoraSecondary.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No comments yet. Start the conversation!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    comments.forEach { comment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                            ) {
                                AsyncImage(
                                    model = comment.authorAvatarUrl,
                                    contentDescription = comment.authorDisplayName,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = comment.authorUsername,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = comment.text,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // INPUT ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    placeholder = { Text("Add a comment...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_comment"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (commentInput.isNotBlank()) {
                            viewModel.addComment(commentInput)
                            commentInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(NexoraPrimary)
                        .testTag("btn_send_comment")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Post",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
