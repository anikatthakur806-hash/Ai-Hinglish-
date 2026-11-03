package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.TranslatorViewModel

data class QuickPhraseItem(
    val category: String,
    val hinglish: String,
    val hindi: String
)

@Composable
fun QuickPhrasesScreen(
    viewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Instagram & DMs", "Dosti & Yaari", "Pyaar & Flirt", "Rozana Greetings", "Office & Kaam")
    var selectedCategory by remember { mutableStateOf("All") }

    val phrases = remember {
        listOf(
            QuickPhraseItem("Instagram & DMs", "kya kar rahe ho bhai", "क्या कर रहे हो भाई"),
            QuickPhraseItem("Instagram & DMs", "reels dekhi kya meri?", "रील्स देखी क्या मेरी?"),
            QuickPhraseItem("Instagram & DMs", "bohot badiya photo hai yaar", "बहुत बढ़िया फ़ोटो है यार"),
            QuickPhraseItem("Instagram & DMs", "story me mention kar na", "स्टोरी में मेंशन कर ना"),
            QuickPhraseItem("Instagram & DMs", "number send karo apna", "नंबर सेंड करो अपना"),

            QuickPhraseItem("Dosti & Yaari", "aaj shaam ko milte hai", "आज शाम को मिलते हैं"),
            QuickPhraseItem("Dosti & Yaari", "chai peene chalein bhai?", "चाय पीने चलें भाई?"),
            QuickPhraseItem("Dosti & Yaari", "bhai party kab de raha hai", "भाई पार्टी कब दे रहा है"),
            QuickPhraseItem("Dosti & Yaari", "tension mat le sab theek hoga", "टेंशन मत ले सब ठीक होगा"),
            QuickPhraseItem("Dosti & Yaari", "kaha gayab ho itne din se", "कहाँ गायब हो इतने दिन से"),

            QuickPhraseItem("Pyaar & Flirt", "aap bohot sundar lag rahi ho", "आप बहुत सुंदर लग रही हो"),
            QuickPhraseItem("Pyaar & Flirt", "tumhari bohot yaad aa rahi hai", "तुम्हारी बहुत याद आ रही है"),
            QuickPhraseItem("Pyaar & Flirt", "khana kha liya kya aapne?", "खाना खा लिया क्या आपने?"),
            QuickPhraseItem("Pyaar & Flirt", "apna khayal rakhna sweet dreams", "अपना ख्याल रखना मीठे सपने"),
            QuickPhraseItem("Pyaar & Flirt", "kal coffee pe mil sakte hai?", "क्या कल कॉफ़ी पे मिल सकते हैं?"),

            QuickPhraseItem("Rozana Greetings", "shubh prabhat kaisa raha din", "शुभ प्रभात कैसा रहा दिन"),
            QuickPhraseItem("Rozana Greetings", "shubh ratri kal baat karte hai", "शुभ रात्रि कल बात करते हैं"),
            QuickPhraseItem("Rozana Greetings", "bahut bahut dhanyawad aapka", "बहुत बहुत धन्यवाद आपका"),
            QuickPhraseItem("Rozana Greetings", "jald milte hai dost", "जल्द मिलते हैं दोस्त"),

            QuickPhraseItem("Office & Kaam", "report bhej di hai check kar lo", "रिपोर्ट भेज दी है चेक कर लो"),
            QuickPhraseItem("Office & Kaam", "meeting kitne baje start hogi?", "मीटिंग कितने बजे स्टार्ट होगी?"),
            QuickPhraseItem("Office & Kaam", "main abhi busy hu baad me call karo", "मैं अभी व्यस्त हूँ बाद में कॉल करो")
        )
    }

    val filtered = if (selectedCategory == "All") phrases else phrases.filter { it.category == selectedCategory }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(16.dp)
            .testTag("quick_phrases_screen")
    ) {
        Text(
            text = "⚡ Instant Chat Phrases",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Direct Hinglish se Hindi me baat karne ke liye tap karein",
            color = Slate400,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) PrimaryBlue else Slate900)
                        .border(
                            1.dp,
                            if (isSelected) Color.White.copy(alpha = 0.5f) else Slate800,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Phrase Cards List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.hinglish,
                                color = Slate400,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.hindi,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Listen
                            IconButton(
                                onClick = { viewModel.speakHindi(item.hindi) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.VolumeUp,
                                    contentDescription = "Speak",
                                    tint = PrimaryBlueLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Copy
                            IconButton(
                                onClick = { viewModel.copyToClipboard(item.hindi) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Open in Instagram Chat
                            IconButton(
                                onClick = {
                                    viewModel.updateChatInput(item.hindi)
                                    viewModel.setTab(AppTab.INSTA_CHAT)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Open in Insta Chat",
                                    tint = Color(0xFFE1306C),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
