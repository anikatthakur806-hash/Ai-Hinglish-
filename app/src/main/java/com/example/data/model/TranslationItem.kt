package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String = "Hinglish",
    val targetLang: String = "Hindi",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
