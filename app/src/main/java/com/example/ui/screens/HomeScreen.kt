package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserEntity
import com.example.ui.components.PostCard
import com.example.ui.components.StoryAvatarItem
import com.example.ui.theme.NexoraPrimary
import com.example.ui.viewmodel.FeedTab
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun HomeScreen(
    viewModel: NexoraViewModel,
    currentUser: UserEntity?,
    allPosts: List<PostEntity>,
    followingPosts: List<PostEntity>,
    feedTab: FeedTab,
    allStories: List<StoryEntity>,
    modifier: Modifier = Modifier
) {
    val displayPosts = if (feedTab == FeedTab.FOLLOWING) followingPosts else allPosts

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // FEED SELECTOR TABS: For You | Following
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(3.dp)
            ) {
                Row {
                    FeedTabButton(
                        title = "For You",
                        selected = feedTab == FeedTab.FOR_YOU,
                        onClick = { viewModel.setFeedTab(FeedTab.FOR_YOU) },
                        testTag = "tab_for_you"
                    )
                    FeedTabButton(
                        title = "Following",
                        selected = feedTab == FeedTab.FOLLOWING,
                        onClick = { viewModel.setFeedTab(FeedTab.FOLLOWING) },
                        testTag = "tab_following"
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // STORIES ROW
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        // Current user's story slot
                        item {
                            val userStory = allStories.find { it.userId == currentUser?.id }
                            StoryAvatarItem(
                                story = userStory,
                                isCurrentUser = true,
                                currentUserAvatar = currentUser?.avatarUrl ?: "",
                                onClick = {
                                    if (userStory != null) {
                                        viewModel.openStory(userStory)
                                    } else {
                                        viewModel.setTab(ScreenTab.CREATE)
                                    }
                                }
                            )
                        }

                        // Other users stories
                        val otherStories = allStories.filter { it.userId != currentUser?.id }
                        items(otherStories) { story ->
                            StoryAvatarItem(
                                story = story,
                                isCurrentUser = false,
                                currentUserAvatar = "",
                                onClick = { viewModel.openStory(story) }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }

            // POSTS FEED
            if (displayPosts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp, horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.Explore,
                                contentDescription = null,
                                tint = NexoraPrimary,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (feedTab == FeedTab.FOLLOWING) "No posts from followed creators yet" else "No posts to show",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Follow more creators or explore trending posts on NEXORA",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.setTab(ScreenTab.EXPLORE) },
                                colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary)
                            ) {
                                Text("Discover Creators")
                            }
                        }
                    }
                }
            } else {
                items(displayPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onLike = { viewModel.toggleLikePost(post) },
                        onSave = { viewModel.toggleSavePost(post) },
                        onShare = { viewModel.sharePost(post) },
                        onCommentClick = { viewModel.openComments("POST", post.id) },
                        onAuthorClick = {
                            val user = viewModel.allUsers.value.find { it.id == post.userId }
                            if (user != null) {
                                viewModel.openUserProfile(user)
                            }
                        },
                        onReportClick = { viewModel.openReport("POST", post.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedTabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) NexoraPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
