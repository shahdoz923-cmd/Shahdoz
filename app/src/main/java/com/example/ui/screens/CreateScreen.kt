package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.NexoraPrimary
import com.example.ui.theme.NexoraSecondary
import com.example.ui.viewmodel.NexoraViewModel

enum class CreateMode {
    POST, REEL, STORY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    viewModel: NexoraViewModel,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableStateOf(CreateMode.POST) }

    // POST FIELDS
    var postCaption by remember { mutableStateOf("") }
    var selectedMediaPreset by remember {
        mutableStateOf("https://images.unsplash.com/photo-1518770660439-4636190af475?w=1080&q=80")
    }
    var postLocation by remember { mutableStateOf("Tokyo, Japan") }
    var isCarousel by remember { mutableStateOf(false) }

    // REEL FIELDS
    var reelCaption by remember { mutableStateOf("") }
    var reelMusicTitle by remember { mutableStateOf("NEXORA Chill Synth") }
    var reelMusicArtist by remember { mutableStateOf("Nexora Sound Lab") }
    var selectedReelCover by remember {
        mutableStateOf("https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&q=80")
    }

    // STORY FIELDS
    var storyCaption by remember { mutableStateOf("") }
    var storyType by remember { mutableStateOf("PHOTO") } // PHOTO, TEXT
    var selectedBgColor by remember { mutableStateOf("#6546D9") }
    var isCloseFriendsStory by remember { mutableStateOf(false) }

    val presetImages = listOf(
        "https://images.unsplash.com/photo-1518770660439-4636190af475?w=1080&q=80",
        "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&q=80",
        "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=1080&q=80",
        "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=1080&q=80",
        "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=1080&q=80"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Create Content",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // MODE TABS
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(2.dp)
            ) {
                listOf(
                    CreateMode.POST to "Post",
                    CreateMode.REEL to "Reel",
                    CreateMode.STORY to "Story"
                ).forEach { (m, label) ->
                    val isSelected = mode == m
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) NexoraPrimary else Color.Transparent)
                            .clickable { mode = m }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // SCROLLABLE FORM
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (mode) {
                CreateMode.POST -> {
                    // PREVIEW MEDIA
                    Text("Select Media Asset", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetImages.forEach { imgUrl ->
                            val isChosen = selectedMediaPreset == imgUrl
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = if (isChosen) 2.5.dp else 1.dp,
                                        color = if (isChosen) NexoraPrimary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedMediaPreset = imgUrl }
                            ) {
                                AsyncImage(
                                    model = imgUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // MEDIA PREVIEW BOX
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        AsyncImage(
                            model = selectedMediaPreset,
                            contentDescription = "Selected media",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CAPTION INPUT
                    OutlinedTextField(
                        value = postCaption,
                        onValueChange = { postCaption = it },
                        label = { Text("Write a caption...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_post_caption"),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // HASHTAG SUGGESTIONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("#NEXORA", "#Creators", "#Design", "#CyberLab", "#Aesthetics").forEach { tag ->
                            SuggestionChip(
                                onClick = {
                                    postCaption = if (postCaption.isBlank()) tag else "$postCaption $tag"
                                },
                                label = { Text(tag, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // LOCATION INPUT
                    OutlinedTextField(
                        value = postLocation,
                        onValueChange = { postLocation = it },
                        label = { Text("Location Tag") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = NexoraPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // CAROUSEL TOGGLE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Carousel Post", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Add multiple high-resolution photos", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isCarousel,
                            onCheckedChange = { isCarousel = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val mediaUrls = if (isCarousel) {
                                "$selectedMediaPreset,https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=1080&q=80"
                            } else selectedMediaPreset

                            viewModel.createPost(
                                content = postCaption.ifBlank { "Sharing from my creative space on NEXORA! ✨ #NEXORA" },
                                mediaUrls = mediaUrls,
                                mediaType = if (isCarousel) "CAROUSEL" else "PHOTO",
                                hashtags = "#NEXORA #Creators",
                                location = postLocation
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_publish_post"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share to NEXORA Feed", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                CreateMode.REEL -> {
                    Text("Select Reel Video Source", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetImages.take(4).forEach { imgUrl ->
                            val isChosen = selectedReelCover == imgUrl
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = if (isChosen) 2.5.dp else 1.dp,
                                        color = if (isChosen) NexoraPrimary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedReelCover = imgUrl }
                            ) {
                                AsyncImage(
                                    model = imgUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // REEL CAPTION
                    OutlinedTextField(
                        value = reelCaption,
                        onValueChange = { reelCaption = it },
                        label = { Text("Reel Caption & Hashtags") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_reel_caption"),
                        minLines = 3,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // MUSIC TRACK
                    OutlinedTextField(
                        value = reelMusicTitle,
                        onValueChange = { reelMusicTitle = it },
                        label = { Text("Audio Track Title") },
                        leadingIcon = { Icon(Icons.Default.MusicNote, contentDescription = null, tint = NexoraPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reelMusicArtist,
                        onValueChange = { reelMusicArtist = it },
                        label = { Text("Artist / Sound Creator") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.createReel(
                                caption = reelCaption.ifBlank { "Check out my new vertical reel on NEXORA! 🎬⚡ #NEXORAReels" },
                                videoUrl = selectedReelCover,
                                musicTitle = reelMusicTitle,
                                musicArtist = reelMusicArtist
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_publish_reel"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.MovieCreation, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publish Short Video Reel", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                CreateMode.STORY -> {
                    Text("Story Format", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = storyType == "PHOTO",
                            onClick = { storyType = "PHOTO" },
                            label = { Text("Photo Story") },
                            leadingIcon = { Icon(Icons.Default.PhotoCamera, contentDescription = null) }
                        )

                        FilterChip(
                            selected = storyType == "TEXT",
                            onClick = { storyType = "TEXT" },
                            label = { Text("Text Story") },
                            leadingIcon = { Icon(Icons.Default.TextFields, contentDescription = null) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (storyType == "TEXT") {
                        Text("Background Color", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("#6546D9", "#FF3366", "#00E5FF", "#10B981", "#1E1B4B").forEach { hex ->
                                val color = Color(android.graphics.Color.parseColor(hex))
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (selectedBgColor == hex) 3.dp else 1.dp,
                                            color = if (selectedBgColor == hex) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedBgColor = hex }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    OutlinedTextField(
                        value = storyCaption,
                        onValueChange = { storyCaption = it },
                        label = { Text(if (storyType == "TEXT") "Story Thoughts..." else "Story Caption (Optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_story_caption"),
                        minLines = 3,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Close Friends Only", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Share with your private inner circle", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isCloseFriendsStory,
                            onCheckedChange = { isCloseFriendsStory = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.createStory(
                                mediaUrl = if (storyType == "PHOTO") presetImages.first() else "",
                                caption = storyCaption.ifBlank { "Enjoying the NEXORA community today! ✨" },
                                type = storyType,
                                bgColorHex = selectedBgColor,
                                isCloseFriends = isCloseFriendsStory
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_publish_story"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexoraPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Timelapse, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to 24h Story", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}
