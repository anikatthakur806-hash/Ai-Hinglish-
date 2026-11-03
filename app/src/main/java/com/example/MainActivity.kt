package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.HistoryFavoritesScreen
import com.example.ui.screens.InstagramChatSimulatorScreen
import com.example.ui.screens.MainTranslatorScreen
import com.example.ui.screens.QuickPhrasesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.TranslatorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TranslatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: TranslatorViewModel) {
    val state by viewModel.uiState.collectAsState()

    // Back button handling: pop to main Translate tab if on secondary tab
    BackHandler(enabled = state.currentTab != AppTab.TRANSLATE) {
        viewModel.setTab(AppTab.TRANSLATE)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF090D16),
        topBar = {
            // Hide custom top bar on Instagram simulator to preserve authentic full-screen IG UI
            if (state.currentTab != AppTab.INSTA_CHAT) {
                AppTopHeader(
                    currentTab = state.currentTab
                )
            }
        },
        bottomBar = {
            AppBottomNavigation(
                currentTab = state.currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = state.currentTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    AppTab.TRANSLATE -> MainTranslatorScreen(viewModel = viewModel)
                    AppTab.INSTA_CHAT -> InstagramChatSimulatorScreen(viewModel = viewModel)
                    AppTab.PHRASES -> QuickPhrasesScreen(viewModel = viewModel)
                    AppTab.HISTORY -> HistoryFavoritesScreen(viewModel = viewModel)
                    AppTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppTopHeader(currentTab: AppTab) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate900)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "अ/A",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "HiTranslate",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB).copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Hinglish to Hindi",
                            color = PrimaryBlueLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = when (currentTab) {
                        AppTab.TRANSLATE -> "1-Tap Chat Translator"
                        AppTab.PHRASES -> "Daily Chat Expressions"
                        AppTab.HISTORY -> "Saved Translations & Starred"
                        AppTab.SETTINGS -> "Floating Ball & Preferences"
                        AppTab.INSTA_CHAT -> "Instagram Live Chat Mode"
                    },
                    color = Slate400,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = Slate900,
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.TRANSLATE,
            onClick = { onTabSelected(AppTab.TRANSLATE) },
            icon = {
                Icon(
                    Icons.Default.Translate,
                    contentDescription = "Translate"
                )
            },
            label = { Text("Translate", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = PrimaryBlue,
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_tab_translate")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.INSTA_CHAT,
            onClick = { onTabSelected(AppTab.INSTA_CHAT) },
            icon = {
                Icon(
                    Icons.Default.ChatBubble,
                    contentDescription = "Insta Chat"
                )
            },
            label = { Text("Insta Chat", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color(0xFFE1306C),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_tab_insta_chat")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.PHRASES,
            onClick = { onTabSelected(AppTab.PHRASES) },
            icon = {
                Icon(
                    Icons.Default.Bolt,
                    contentDescription = "Phrases"
                )
            },
            label = { Text("Phrases", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = PrimaryBlue,
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_tab_phrases")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.HISTORY,
            onClick = { onTabSelected(AppTab.HISTORY) },
            icon = {
                Icon(
                    Icons.Default.History,
                    contentDescription = "History"
                )
            },
            label = { Text("History", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = PrimaryBlue,
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_tab_history")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.SETTINGS,
            onClick = { onTabSelected(AppTab.SETTINGS) },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = PrimaryBlue,
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_tab_settings")
        )
    }
}
