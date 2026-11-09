package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PostEntity
import com.example.data.model.ReelEntity
import com.example.data.model.UserEntity
import com.example.ui.theme.NexoraPrimary
import com.example.ui.theme.NexoraSecondary
import com.example.ui.theme.NexoraVerifiedBlue
import com.example.ui.viewmodel.NexoraViewModel

enum class ProfileTab {
    POSTS, REELS, SAVED
}

@Composable
fun ProfileScreen(
    viewModel: NexoraViewModel,
    user: UserEntity?,
    isSelf: Boolean,
    allPosts: List<PostEntity>,
    allReels: List<ReelEntity>,
    savedPosts: List<PostEntity>,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (user == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NexoraPrimary)
        }
        return
    }

    var selectedTab by remember { mutableStateOf(ProfileTab.POSTS) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val userPosts = remember(allPosts, user.id) { allPosts.filter { it.userId == user.id } }
    val userReels = remember(allReels, user.id) { allReels.filter { it.userId == user.id } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR FOR PROFILE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isSelf && onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (user.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = NexoraVerifiedBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelf) {
                    IconButton(onClick = { viewModel.toggleCreatorStudio(true) }) {
                        Icon(
                            imageVector = Icons.Outlined.Analytics,
                            contentDescription = "Creator Studio",
                            tint = NexoraPrimary
                        )
                    }
                    IconButton(onClick = { viewModel.toggleSettings(true) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    IconButton(onClick = { viewModel.startDirectMessage(user) }) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Message",
                            tint = NexoraPrimary
                        )
                    }
                }
            }
        }

        // PROFILE INFO HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            Brush.linearGradient(listOf(NexoraPrimary, NexoraSecondary)),
                            CircleShape
                        )
                ) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = user.displayName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Stats row
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCounter(count = userPosts.size.toString(), label = "Posts")
                    StatCounter(count = "${user.followersCount}", label = "Followers")
                    StatCounter(count = "${user.followingCount}", label = "Following")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Display Name & Category
            Text(
                text = user.displayName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (user.isBusiness) {
                Text(
                    text = user.businessCategory,
                    fontSize = 11.sp,
                    color = NexoraPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            if (user.bio.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.bio,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            if (user.website.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = NexoraPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.website,
                        fontSize = 12.sp,
                        color = NexoraPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ACTION BUTTONS
            if (isSelf) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_edit_profile"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Edit Profile", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.toggleCreatorStudio(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_creator_studio"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Insights, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Creator Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.toggleFollowUser(user) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.isFollowing) MaterialTheme.colorScheme.surfaceVariant else NexoraPrimary,
                            contentColor = if (user.isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (user.isFollowing) "Following" else "Follow", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.startDirectMessage(user) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Message", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // TAB SELECTOR: Posts | Reels | Saved
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ProfileTabItem(
                icon = Icons.Outlined.GridOn,
                isSelected = selectedTab == ProfileTab.POSTS,
                onClick = { selectedTab = ProfileTab.POSTS }
            )
            ProfileTabItem(
                icon = Icons.Outlined.MovieCreation,
                isSelected = selectedTab == ProfileTab.REELS,
                onClick = { selectedTab = ProfileTab.REELS }
            )
            if (isSelf) {
                ProfileTabItem(
                    icon = Icons.Outlined.BookmarkBorder,
                    isSelected = selectedTab == ProfileTab.SAVED,
                    onClick = { selectedTab = ProfileTab.SAVED }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // GRID CONTENT
        val displayItems: List<Any> = when (selectedTab) {
            ProfileTab.POSTS -> userPosts
            ProfileTab.REELS -> userReels
            ProfileTab.SAVED -> savedPosts
        }

        if (displayItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (selectedTab) {
                        ProfileTab.POSTS -> "No posts yet"
                        ProfileTab.REELS -> "No reels uploaded"
                        ProfileTab.SAVED -> "No saved posts"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(2.dp)
            ) {
                items(displayItems) { item ->
                    when (item) {
                        is PostEntity -> {
                            val media = item.mediaUrls.split(",").firstOrNull { it.isNotBlank() }
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                if (media != null) {
                                    AsyncImage(
                                        model = media,
                                        contentDescription = item.contentText,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(NexoraPrimary)
                                            .padding(6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.contentText,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        is ReelEntity -> {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(0.8f)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AsyncImage(
                                    model = item.videoCoverUrl.ifBlank { item.videoUrl },
                                    contentDescription = item.caption,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(4.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                        Text("${item.viewsCount}", color = Color.White, fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // EDIT PROFILE DIALOG
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(user.displayName) }
        var editBio by remember { mutableStateOf(user.bio) }
        var editWebsite by remember { mutableStateOf(user.website) }
        var editCategory by remember { mutableStateOf(user.businessCategory) }
        var editIsPrivate by remember { mutableStateOf(user.isPrivate) }
        var editIsBusiness by remember { mutableStateOf(user.isBusiness) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit NEXORA Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editWebsite,
                        onValueChange = { editWebsite = it },
                        label = { Text("Website") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = { editCategory = it },
                        label = { Text("Category") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Creator/Business Mode")
                        Switch(checked = editIsBusiness, onCheckedChange = { editIsBusiness = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Private Account")
                        Switch(checked = editIsPrivate, onCheckedChange = { editIsPrivate = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            displayName = editName,
                            bio = editBio,
                            website = editWebsite,
                            isPrivate = editIsPrivate,
                            isBusiness = editIsBusiness,
                            category = editCategory
                        )
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatCounter(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileTabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) NexoraPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(2.dp)
                .background(if (isSelected) NexoraPrimary else Color.Transparent)
        )
    }
}
