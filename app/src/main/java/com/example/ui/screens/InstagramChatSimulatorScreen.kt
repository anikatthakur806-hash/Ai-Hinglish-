package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.TranslatorViewModel

@Composable
fun InstagramChatSimulatorScreen(
    viewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val currentContact = viewModel.contacts[state.selectedContactIndex]
    val listState = rememberLazyListState()

    // Scroll to bottom when messages update
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Instagram Dark Mode
            .testTag("instagram_chat_simulator_screen")
    ) {
        // Instagram Top Bar
        InstagramTopBar(
            contact = currentContact,
            contacts = viewModel.contacts,
            selectedIndex = state.selectedContactIndex,
            onSelectContact = { viewModel.selectChatContact(it) }
        )

        // Helper Tutorial Banner
        InstagramTutorialBanner()

        // Message Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Contact Profile Header inside Chat
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                Brush.linearGradient(listOf(InstaPurple, InstaPink, Color(0xFFFBAA47))),
                                CircleShape
                            )
                            .padding(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Slate900, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentContact.avatarLetter,
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentContact.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${currentContact.handle} • Instagram",
                        color = Slate400,
                        fontSize = 13.sp
                    )
                }
            }

            items(state.messages, key = { it.id }) { message ->
                InstagramChatBubble(
                    message = message,
                    onTranslateClick = { viewModel.toggleMessageTranslation(message.id) },
                    onCopyClick = { viewModel.copyToClipboard(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Quick Suggestion Chips for Fast Testing
        InstagramQuickChips(
            onChipClick = { hinglishText ->
                viewModel.updateChatInput(hinglishText)
            }
        )

        // Instagram Chat Input Bar with Floating Translate Ball Button
        InstagramChatInputBar(
            inputText = state.chatInputText,
            onInputChange = { viewModel.updateChatInput(it) },
            onTranslateClick = { viewModel.translateChatInputInPlace() },
            onSendClick = { viewModel.sendChatMessage() }
        )
    }
}

@Composable
private fun InstagramTopBar(
    contact: com.example.ui.viewmodel.ChatContact,
    contacts: List<com.example.ui.viewmodel.ChatContact>,
    selectedIndex: Int,
    onSelectContact: (Int) -> Unit
) {
    Surface(
        color = Color(0xFF000000),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Instagram Story Avatar Ring
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Brush.linearGradient(listOf(InstaPurple, InstaPink)),
                            CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.avatarLetter,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = contact.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF10B981), CircleShape)
                        )
                    }
                    Text(
                        text = contact.status,
                        color = Slate400,
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Call, contentDescription = "Audio Call", tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White)
                }
            }

            // Contact Switcher Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                contacts.forEachIndexed { index, item ->
                    val isSelected = index == selectedIndex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryBlue else Slate900)
                            .border(
                                1.dp,
                                if (isSelected) Color.White.copy(alpha = 0.5f) else Slate800,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectContact(index) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstagramTutorialBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(PrimaryBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Translate,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Live Instagram Chat Translate Demo",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Text(
                    text = "Niche Hinglish likho aur Translate Ball dabao - direct Hindi ban jayega!",
                    color = Color(0xFFFDBA74),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun InstagramChatBubble(
    message: ChatMessage,
    onTranslateClick: () -> Unit,
    onCopyClick: (String) -> Unit
) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                // Incoming Message with Translate Bubble
                Box(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp))
                        .background(Color(0xFF262626))
                        .animateContentSize()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            text = if (message.showTranslation && message.translatedHindi != null) {
                                message.translatedHindi
                            } else {
                                message.text
                            },
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )

                        if (message.showTranslation) {
                            Text(
                                text = "Original: ${message.text}",
                                color = Slate400,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .align(Alignment.End),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = message.time,
                                color = Slate400,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Magic Translate Ball on incoming message
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (message.showTranslation) listOf(Color(0xFF10B981), Color(0xFF059669))
                                else listOf(PrimaryBlue, Color(0xFF1D4ED8))
                            )
                        )
                        .clickable { onTranslateClick() }
                        .testTag("translate_bubble_${message.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (message.showTranslation) Icons.Default.Check else Icons.Default.Translate,
                        contentDescription = "Translate to Hindi",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                // Sent Message (User)
                Box(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .clip(RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp))
                        .background(
                            Brush.linearGradient(listOf(InstaPurple, InstaPink, InstaBlue))
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            text = message.text,
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = message.time,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstagramQuickChips(
    onChipClick: (String) -> Unit
) {
    val samples = listOf(
        "kya kar rahe ho bhai",
        "aaj sham ko milte hai",
        "khana kha liya kya",
        "kal exam ki tayari hai",
        "kaha ja rahe ho",
        "sab badhiya hai na"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        samples.forEach { sample ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B))
                    .clickable { onChipClick(sample) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "✍️ $sample",
                    color = Color(0xFF93C5FD),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun InstagramChatInputBar(
    inputText: String,
    onInputChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        color = Color(0xFF000000),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Instagram Camera Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text Input Box with embedded Hi Translate Floating Ball!
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF262626))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        placeholder = {
                            Text(
                                "Type Hinglish message...",
                                color = Slate400,
                                fontSize = 14.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("instagram_message_input"),
                        maxLines = 3
                    )

                    // THE ICONIC "HI TRANSLATE" BALL BUTTON!
                    // User requested: "Instagram per chat Mein Bus use click kar do vah Hindi Mein Likh De"
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF2563EB), Color(0xFF4F46E5), Color(0xFFEA580C))
                                )
                            )
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onTranslateClick() }
                            .testTag("hi_translate_chat_ball"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "अ/A",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            if (inputText.isNotBlank()) {
                IconButton(
                    onClick = onSendClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue, CircleShape)
                        .testTag("instagram_send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Color.White)
                }
            }
        }
    }
}
