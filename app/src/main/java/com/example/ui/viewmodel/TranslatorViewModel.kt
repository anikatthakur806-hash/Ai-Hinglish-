package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TranslationDatabase
import com.example.data.model.TranslationItem
import com.example.engine.HinglishTranslator
import com.example.service.FloatingBubbleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

enum class AppTab(val title: String) {
    TRANSLATE("Translate"),
    INSTA_CHAT("Insta Chat"),
    PHRASES("Phrases"),
    HISTORY("History"),
    SETTINGS("Settings")
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val time: String = "Now",
    val translatedHindi: String? = null,
    val showTranslation: Boolean = false
)

data class ChatContact(
    val name: String,
    val handle: String,
    val avatarLetter: String,
    val status: String,
    val initialMessages: List<ChatMessage>
)

data class TranslatorUiState(
    val currentTab: AppTab = AppTab.TRANSLATE,
    val inputText: String = "",
    val translatedText: String = "",
    val sourceLang: String = "Hinglish",
    val targetLang: String = "Hindi",
    val isTranslating: Boolean = false,
    val isFloatingBallActive: Boolean = false,
    val toastMessage: String? = null,
    // Instagram Chat Simulator state
    val selectedContactIndex: Int = 0,
    val chatInputText: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val showTranslateSparkle: Boolean = false
)

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = TranslationDatabase.getDatabase(application)
    private val dao = db.translationDao()

    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    val historyList: StateFlow<List<TranslationItem>> = dao.getAllTranslations()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val favoriteList: StateFlow<List<TranslationItem>> = dao.getFavoriteTranslations()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    val contacts = listOf(
        ChatContact(
            name = "Rahul Verma",
            handle = "rahul_verma",
            avatarLetter = "R",
            status = "Active now",
            initialMessages = listOf(
                ChatMessage(
                    text = "Bhai kaisa hai? Shaam ko milte hai kya?",
                    isUser = false,
                    time = "5:12 PM",
                    translatedHindi = "भाई कैसा है? शाम को मिलते हैं क्या?"
                ),
                ChatMessage(
                    text = "Haan bhai sab badhiya, tu bata!",
                    isUser = true,
                    time = "5:14 PM",
                    translatedHindi = "हाँ भाई सब बढ़िया, तू बता!"
                ),
                ChatMessage(
                    text = "Arey kal exam ki tayari ki tune?",
                    isUser = false,
                    time = "5:15 PM",
                    translatedHindi = "अरे कल परीक्षा की तैयारी की तुमने?"
                )
            )
        ),
        ChatContact(
            name = "Priya Sharma",
            handle = "priya_k",
            avatarLetter = "P",
            status = "Active 10m ago",
            initialMessages = listOf(
                ChatMessage(
                    text = "Hey! Did you check that new reel?",
                    isUser = false,
                    time = "3:30 PM",
                    translatedHindi = "अरे! क्या तुमने वो नई रील देखी?"
                ),
                ChatMessage(
                    text = "Bohot achhi reel thi yaar!",
                    isUser = true,
                    time = "3:32 PM",
                    translatedHindi = "बहुत अच्छी रील थी यार!"
                ),
                ChatMessage(
                    text = "Kal coffee peene chalein sath me?",
                    isUser = false,
                    time = "3:35 PM",
                    translatedHindi = "कल कॉफ़ी पीने चलें साथ में?"
                )
            )
        ),
        ChatContact(
            name = "Aman Office",
            handle = "aman_work",
            avatarLetter = "A",
            status = "In a meeting",
            initialMessages = listOf(
                ChatMessage(
                    text = "Project report submit kar di kya?",
                    isUser = false,
                    time = "11:20 AM",
                    translatedHindi = "प्रोजेक्ट रिपोर्ट सबमिट कर दी क्या?"
                ),
                ChatMessage(
                    text = "Sir report abhi prepare ho rahi hai",
                    isUser = true,
                    time = "11:22 AM",
                    translatedHindi = "सर रिपोर्ट अभी तैयार हो रही है"
                )
            )
        )
    )

    init {
        // Init simulator messages with first contact
        _uiState.update {
            it.copy(
                messages = contacts[0].initialMessages,
                isFloatingBallActive = FloatingBubbleService.isServiceRunning
            )
        }

        // Init TextToSpeech
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("hi", "IN"))
                isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun updateInputText(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun swapLanguages() {
        _uiState.update {
            val oldSource = it.sourceLang
            val oldTarget = it.targetLang
            val oldInput = it.inputText
            val oldTranslated = it.translatedText
            it.copy(
                sourceLang = oldTarget,
                targetLang = oldSource,
                inputText = oldTranslated,
                translatedText = oldInput
            )
        }
    }

    fun translateNow(textOverride: String? = null) {
        val textToTranslate = textOverride ?: _uiState.value.inputText
        if (textToTranslate.isBlank()) return

        val sourceLang = _uiState.value.sourceLang
        val translated = if (sourceLang == "Hinglish") {
            HinglishTranslator.translateHinglishToHindi(textToTranslate)
        } else {
            HinglishTranslator.translateHindiToHinglish(textToTranslate)
        }

        _uiState.update {
            it.copy(
                inputText = textToTranslate,
                translatedText = translated,
                isTranslating = false
            )
        }

        // Save to Room DB history asynchronously
        viewModelScope.launch {
            dao.insertTranslation(
                TranslationItem(
                    sourceText = textToTranslate,
                    translatedText = translated,
                    sourceLang = sourceLang,
                    targetLang = _uiState.value.targetLang
                )
            )
        }
    }

    fun copyToClipboard(text: String, label: String = "Translated Text") {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        showToast("Copied to clipboard!")
    }

    fun speakHindi(text: String) {
        if (!isTtsReady) {
            showToast("Hindi voice engine is preparing...")
            return
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "HinglishTTS")
    }

    fun toggleFavorite(item: TranslationItem) {
        viewModelScope.launch {
            dao.setFavorite(item.id, !item.isFavorite)
        }
    }

    fun deleteHistoryItem(item: TranslationItem) {
        viewModelScope.launch {
            dao.deleteTranslation(item)
            showToast("Removed from history")
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            dao.clearAll()
            showToast("History cleared")
        }
    }

    fun toggleFloatingBubble(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                android.net.Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            showToast("Please allow 'Display over other apps' permission")
            return
        }

        val currentState = _uiState.value.isFloatingBallActive
        val intent = Intent(context, FloatingBubbleService::class.java)

        if (currentState) {
            context.stopService(intent)
            _uiState.update { it.copy(isFloatingBallActive = false) }
            showToast("Floating Ball disabled")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startService(intent)
            } else {
                context.startService(intent)
            }
            _uiState.update { it.copy(isFloatingBallActive = true) }
            showToast("Floating Ball enabled! You can drag it on screen")
        }
    }

    // Instagram Chat Simulator Logic
    fun updateChatInput(text: String) {
        _uiState.update { it.copy(chatInputText = text) }
    }

    fun selectChatContact(index: Int) {
        if (index in contacts.indices) {
            _uiState.update {
                it.copy(
                    selectedContactIndex = index,
                    messages = contacts[index].initialMessages,
                    chatInputText = ""
                )
            }
        }
    }

    /**
     * CRITICAL USER FEATURE:
     * "Jaise Instagram per chat Mein Bus use click kar do vah Hindi Mein Likh De"
     * Click the Hi Translate ball inside the chat input bar, and it converts the Hinglish
     * text directly in place to Hindi!
     */
    fun translateChatInputInPlace() {
        val currentInput = _uiState.value.chatInputText
        if (currentInput.isBlank()) {
            showToast("Type something in Hinglish first!")
            return
        }

        val hindi = HinglishTranslator.translateHinglishToHindi(currentInput)
        _uiState.update {
            it.copy(
                chatInputText = hindi,
                showTranslateSparkle = true
            )
        }
        showToast("✨ Translated to Hindi!")
    }

    fun sendChatMessage() {
        val currentInput = _uiState.value.chatInputText.trim()
        if (currentInput.isBlank()) return

        val userMessage = ChatMessage(
            text = currentInput,
            isUser = true,
            time = "Just now"
        )

        val updated = _uiState.value.messages + userMessage
        _uiState.update {
            it.copy(
                messages = updated,
                chatInputText = ""
            )
        }

        // Automatic friendly reply simulation after 1 second
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            val replyText = when {
                currentInput.contains("हाँ") || currentInput.contains("haan") -> "Chalo badhiya, milte hai fir!"
                currentInput.contains("कहाँ") || currentInput.contains("kaha") -> "Main abhi ghar pe hi hu dost."
                currentInput.contains("कैसे") || currentInput.contains("kaise") -> "Main mast hu bhai, tu sunao!"
                currentInput.contains("खाना") || currentInput.contains("khana") -> "Haan kha liya, tumne khaya?"
                else -> "Achha theek hai, samajh gaya bhai!"
            }
            val replyHindi = HinglishTranslator.translateHinglishToHindi(replyText)
            val replyMessage = ChatMessage(
                text = replyText,
                isUser = false,
                time = "Just now",
                translatedHindi = replyHindi
            )
            _uiState.update { it.copy(messages = it.messages + replyMessage) }
        }
    }

    fun toggleMessageTranslation(messageId: String) {
        _uiState.update { state ->
            val updated = state.messages.map { msg ->
                if (msg.id == messageId) {
                    val hindi = msg.translatedHindi ?: HinglishTranslator.translateHinglishToHindi(msg.text)
                    msg.copy(
                        translatedHindi = hindi,
                        showTranslation = !msg.showTranslation
                    )
                } else {
                    msg
                }
            }
            state.copy(messages = updated)
        }
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}
