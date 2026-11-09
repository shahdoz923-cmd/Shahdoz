package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CircleEntity
import com.example.data.model.CirclePostEntity
import com.example.ui.theme.NexoraPrimary
import com.example.ui.theme.NexoraSecondary
import com.example.ui.viewmodel.NexoraViewModel

@Composable
fun CirclesScreen(
    viewModel: NexoraViewModel,
    allCircles: List<CircleEntity>,
    activeCircle: CircleEntity?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showNewDiscussionDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Graphic Design", "Technology", "Photography", "Gaming", "Science", "Education", "Art")

    val filteredCircles = remember(allCircles, selectedCategory) {
        if (selectedCategory == "All") allCircles
        else allCircles.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (activeCircle != null) viewModel.closeCircle()
                    else onClose()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = activeCircle?.name ?: "NEXORA Circles",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (activeCircle != null) activeCircle.category else "Creator & Interest Communities",
                        fontSize = 12.sp,
                        color = NexoraPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (activeCircle != null) {
                IconButton(onClick = { showNewDiscussionDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = "New Discussion",
                        tint = NexoraPrimary
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        if (activeCircle == null) {
            // CATEGORY STRIP
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NexoraPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // CIRCLE LIST
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredCircles, key = { it.id }) { circle ->
                    CircleCard(
                        circle = circle,
                        onJoinToggle = { viewModel.toggleJoinCircle(circle) },
                        onClick = { viewModel.openCircle(circle) }
                    )
                }
            }
        } else {
            // CIRCLE DETAIL & DISCUSSIONS
            val posts by viewModel.getCirclePosts(activeCircle.id).collectAsState(initial = emptyList())

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // CIRCLE HEADER CARD
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            ) {
                                AsyncImage(
                                    model = activeCircle.bannerUrl,
                                    contentDescription = activeCircle.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(activeCircle.iconUrl, fontSize = 24.sp)
                                }
                            }

                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = activeCircle.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeCircle.description,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Rules: ${activeCircle.rules}",
                                    fontSize = 11.sp,
                                    color = NexoraPrimary,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${activeCircle.memberCount} Members • ${activeCircle.postCount} Discussions",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Button(
                                        onClick = { viewModel.toggleJoinCircle(activeCircle) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (activeCircle.isJoined) MaterialTheme.colorScheme.surfaceVariant else NexoraPrimary,
                                            contentColor = if (activeCircle.isJoined) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(if (activeCircle.isJoined) "Joined" else "Join Circle", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Community Discussions & Polls",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        TextButton(onClick = { showNewDiscussionDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("New Post", fontSize = 12.sp)
                        }
                    }
                }

                if (posts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No discussions yet. Start the first topic!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(posts, key = { it.id }) { post ->
                        CirclePostCard(
                            post = post,
                            onVote = { optIndex -> viewModel.votePoll(post, optIndex) }
                        )
                    }
                }
            }
        }
    }

    // NEW DISCUSSION DIALOG
    if (showNewDiscussionDialog && activeCircle != null) {
        var postTitle by remember { mutableStateOf("") }
        var postBody by remember { mutableStateOf("") }
        var pollQuestion by remember { mutableStateOf("") }
        var pollOptions by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewDiscussionDialog = false },
            title = { Text("Start Discussion in ${activeCircle.name}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = postTitle,
                        onValueChange = { postTitle = it },
                        label = { Text("Discussion Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = postBody,
                        onValueChange = { postBody = it },
                        label = { Text("Discussion Body") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = pollQuestion,
                        onValueChange = { pollQuestion = it },
                        label = { Text("Add Poll Question (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (pollQuestion.isNotBlank()) {
                        OutlinedTextField(
                            value = pollOptions,
                            onValueChange = { pollOptions = it },
                            label = { Text("Poll Options (comma separated)") },
                            placeholder = { Text("Option A, Option B, Option C") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postTitle.isNotBlank()) {
                            viewModel.createCirclePost(
                                circleId = activeCircle.id,
                                circleName = activeCircle.name,
                                title = postTitle,
                                content = postBody,
                                pollQuestion = pollQuestion,
                                pollOptions = pollOptions
                            )
                            showNewDiscussionDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary)
                ) {
                    Text("Publish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewDiscussionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CircleCard(
    circle: CircleEntity,
    onJoinToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            ) {
                AsyncImage(
                    model = circle.bannerUrl,
                    contentDescription = circle.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(circle.iconUrl, fontSize = 20.sp)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = circle.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${circle.memberCount} members • ${circle.category}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onJoinToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (circle.isJoined) MaterialTheme.colorScheme.surfaceVariant else NexoraPrimary,
                        contentColor = if (circle.isJoined) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(if (circle.isJoined) "Joined" else "Join", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CirclePostCard(
    post: CirclePostEntity,
    onVote: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = post.authorAvatarUrl,
                        contentDescription = post.authorDisplayName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = post.authorDisplayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "@${post.authorUsername}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (post.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.content,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // POLL SECTION (IF PRESENT)
            if (post.pollQuestion.isNotBlank() && post.pollOptions.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📊 ${post.pollQuestion}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val options = post.pollOptions.split(",")
                        val votes = post.pollVotes.split(",").map { it.toIntOrNull() ?: 0 }
                        val totalVotes = votes.sum().coerceAtLeast(1)

                        options.forEachIndexed { index, option ->
                            val optionVotes = votes.getOrElse(index) { 0 }
                            val pct = (optionVotes.toFloat() / totalVotes * 100).toInt()
                            val isVoted = post.userVotedIndex == index

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isVoted) NexoraPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface)
                                    .clickable { onVote(index) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = option,
                                        fontSize = 12.sp,
                                        fontWeight = if (isVoted) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = "$pct% ($optionVotes)",
                                        fontSize = 11.sp,
                                        color = NexoraPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { optionVotes.toFloat() / totalVotes },
                                    modifier = Modifier.fillMaxWidth().height(4.dp),
                                    color = NexoraPrimary,
                                    trackColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
