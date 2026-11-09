package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.outlined.Search
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
import com.example.ui.theme.NexoraBorderLight
import com.example.ui.theme.NexoraPrimary
import com.example.ui.theme.NexoraVerifiedBlue
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun ExploreScreen(
    viewModel: NexoraViewModel,
    allPosts: List<PostEntity>,
    allReels: List<ReelEntity>,
    allUsers: List<UserEntity>,
    searchQuery: String,
    exploreCategory: String,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Trending", "Design", "Tech", "Photography", "Gaming", "Science")

    // Filter posts and users by search query or category
    val filteredPosts = remember(allPosts, searchQuery, exploreCategory) {
        allPosts.filter { post ->
            val matchesQuery = searchQuery.isBlank() ||
                    post.contentText.contains(searchQuery, ignoreCase = true) ||
                    post.authorUsername.contains(searchQuery, ignoreCase = true) ||
                    post.hashtags.contains(searchQuery, ignoreCase = true) ||
                    post.location.contains(searchQuery, ignoreCase = true)

            val matchesCat = when (exploreCategory) {
                "All", "Trending" -> true
                "Design" -> post.hashtags.contains("Design", ignoreCase = true) || post.hashtags.contains("Architecture", ignoreCase = true)
                "Tech" -> post.hashtags.contains("Tech", ignoreCase = true) || post.hashtags.contains("Audio", ignoreCase = true)
                "Photography" -> post.hashtags.contains("Photo", ignoreCase = true) || post.hashtags.contains("Tokyo", ignoreCase = true)
                "Gaming" -> post.hashtags.contains("Game", ignoreCase = true)
                "Science" -> post.hashtags.contains("Science", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesCat
        }
    }

    val matchedUsers = remember(allUsers, searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allUsers.filter {
            it.username.contains(searchQuery, ignoreCase = true) ||
                    it.displayName.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // SEARCH BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search creators, tags, or topics...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_search"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )
        }

        // CATEGORY CHIPS
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = exploreCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setExploreCategory(cat) },
                    label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NexoraPrimary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // SEARCH USER RESULTS (IF SEARCHING)
        if (matchedUsers.isNotEmpty()) {
            Text(
                text = "Creators found",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(matchedUsers) { user ->
                    SuggestedCreatorCard(
                        user = user,
                        onFollow = { viewModel.toggleFollowUser(user) },
                        onClick = { viewModel.openUserProfile(user) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // SUGGESTED CREATORS STRIP (IF NOT SEARCHING)
        if (searchQuery.isBlank()) {
            Text(
                text = "Trending Creators to Follow",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allUsers.filter { !it.isFollowing }) { user ->
                    SuggestedCreatorCard(
                        user = user,
                        onFollow = { viewModel.toggleFollowUser(user) },
                        onClick = { viewModel.openUserProfile(user) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // 3-COLUMN DISCOVERY GRID
        Text(
            text = "Discover Content",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(filteredPosts, key = { it.id }) { post ->
                val firstMedia = post.mediaUrls.split(",").firstOrNull { it.isNotBlank() }
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            val author = allUsers.find { it.id == post.userId }
                            if (author != null) viewModel.openUserProfile(author)
                        }
                ) {
                    if (firstMedia != null) {
                        AsyncImage(
                            model = firstMedia,
                            contentDescription = post.contentText,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(NexoraPrimary)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.contentText,
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Carousel or video badge overlay
                    if (post.mediaType == "CAROUSEL") {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MovieCreation,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Likes overlay badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(4.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${post.likesCount}",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestedCreatorCard(
    user: UserEntity,
    onFollow: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = user.displayName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (user.isVerified) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = NexoraVerifiedBlue,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Text(
                text = "@${user.username}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onFollow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (user.isFollowing) MaterialTheme.colorScheme.surfaceVariant else NexoraPrimary,
                    contentColor = if (user.isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (user.isFollowing) "Following" else "Follow",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
