package com.example.engine

import java.util.Locale

object HinglishTranslator {

    // Complete phrases for immediate high-accuracy chat translation
    private val phraseDictionary = mapOf(
        // Common Instagram / WhatsApp Greetings & Chat Starters
        "kya kar rahe ho" to "क्या कर रहे हो",
        "kya kar rahi ho" to "क्या कर रही हो",
        "kya kar rahe ho bhai" to "क्या कर रहे हो भाई",
        "kya haal chal" to "क्या हाल चाल",
        "kya haal hai" to "क्या हाल है",
        "kaise ho bhai" to "कैसे हो भाई",
        "kaise ho yaar" to "कैसे हो यार",
        "kaise ho aap" to "कैसे हैं आप",
        "main theek hu" to "मैं ठीक हूँ",
        "mai theek hu" to "मैं ठीक हूँ",
        "sab theek hai" to "सब ठीक है",
        "sab badhiya" to "सब बढ़िया",
        "sab kushal mangal" to "सब कुशल मंगल",
        "kaha ho abhi" to "कहाँ हो अभी",
        "kaha ja rahe ho" to "कहाँ जा रहे हो",
        "kaha par ho" to "कहाँ पर हो",
        "kab aa rahe ho" to "कब आ रहे हो",
        "kab milna hai" to "कब मिलना है",
        "aaj sham ko milte hai" to "आज शाम को मिलते हैं",
        "kal milte hai" to "कल मिलते हैं",
        "kal baat karte hai" to "कल बात करते हैं",
        "abhi thoda busy hu" to "अभी थोड़ा व्यस्त हूँ",
        "free hokar call karo" to "फ़्री होकर कॉल करो",
        "mujhe call karo" to "मुझे कॉल करो",
        "message dekh lo" to "मैसेज देख लो",
        "reels dekhi kya" to "रील्स देखी क्या",
        "reels bhej di" to "रील्स भेज दी",
        "photo bhej do" to "फ़ोटो भेज दो",
        "khana kha liya" to "खाना खा लिया",
        "khana kha liya kya" to "खाना खा लिया क्या",
        "kya khaya aaj" to "क्या खाया आज",
        "chalo bahar chalte hai" to "चलो बाहर चलते हैं",
        "chai peene chale" to "चाय पीने चलें",
        "coffee peete hai" to "कॉफ़ी पीते हैं",
        "bhai party kab de raha hai" to "भाई पार्टी कब दे रहा है",
        "party to banti hai" to "पार्टी तो बनती है",
        "bohot badiya" to "बहुत बढ़िया",
        "bahut badhiya" to "बहुत बढ़िया",
        "ekdum mast" to "एकदम मस्त",
        "shandar bhai" to "शानदार भाई",
        "zabardast post" to "ज़बरदस्त पोस्ट",
        "bhai sun na" to "भाई सुन ना",
        "ek baat bolu" to "एक बात बोलूँ",
        "kya baat hai" to "क्या बात है",
        "sahi baat hai" to "सही बात है",
        "koi baat nahi" to "कोई बात नहीं",
        "tension mat le" to "टेंशन मत ले",
        "sab theek ho jayega" to "सब ठीक हो जाएगा",
        "dhanyawad bhai" to "धन्यवाद भाई",
        "shukriya dost" to "शुक्रिया दोस्त",
        "alvida dhyan rakhna" to "अलविदा ध्यान रखना",
        "apna khayal rakhna" to "अपना ख्याल रखना",
        "apna dhyan rakhna" to "अपना ध्यान रखना",
        "subah milte hai" to "सुबह मिलते हैं",
        "raat ko baat karte hai" to "रात को बात करते हैं",
        "neend aa rahi hai" to "नींद आ रही है",
        "chalo so jao" to "चलो सो जाओ",
        "sweet dreams" to "मीठे सपने",
        "good night" to "शुभ रात्रि",
        "good morning" to "शुभ प्रभात",
        "have a nice day" to "आपका दिन शुभ हो",
        "how are you" to "आप कैसे हैं",
        "i am good" to "मैं ठीक हूँ",
        "i am fine" to "मैं ठीक हूँ",
        "what are you doing" to "आप क्या कर रहे हैं",
        "what's up" to "क्या चल रहा है",
        "whats up" to "क्या चल रहा है",
        "where are you" to "आप कहाँ हैं",
        "see you soon" to "जल्द मिलते हैं",
        "see you tomorrow" to "कल मिलते हैं",
        "take care" to "ख्याल रखना",
        "thank you" to "धन्यवाद",
        "thank you so much" to "बहुत बहुत धन्यवाद",
        "sorry yaar" to "माफ़ करना यार",
        "i love you" to "मैं तुमसे प्यार करता हूँ",
        "i miss you" to "मुझे तुम्हारी याद आ रही है",
        "happy birthday" to "जन्मदिन की हार्दिक शुभकामनाएं",
        "happy anniversary" to "शादी की सालगिरह मुबारक",
        "congratulations" to "हार्दिक बधाई",
        "all the best" to "शुभकामनाएं",
        "best of luck" to "शुभकामनाएं",
        "nice photo" to "बहुत सुंदर फ़ोटो",
        "looking gorgeous" to "बहुत खूबसूरत लग रही हो",
        "looking handsome" to "बहुत शानदार लग रहे हो"
    )

    // Extensive word-level dictionary (Hinglish -> Hindi Devanagari)
    private val wordDictionary = mapOf(
        // Pronouns & Demonstratives
        "main" to "मैं", "mai" to "मैं", "hum" to "हम", "aap" to "आप", "tum" to "तुम",
        "tu" to "तू", "ye" to "ये", "yeh" to "यह", "vo" to "वो", "wo" to "वो", "woh" to "वह",
        "mera" to "मेरा", "meri" to "मेरी", "mere" to "मेरे",
        "tera" to "तेरा", "teri" to "तेरी", "tere" to "तेरे",
        "apka" to "आपका", "aapka" to "आपका", "apki" to "आपकी", "aapki" to "आपकी", "apke" to "आपके", "aapke" to "आपके",
        "tumhara" to "तुम्हारा", "tumhari" to "तुम्हारी", "tumhare" to "तुम्हारे",
        "hamara" to "हमारा", "hamari" to "हमारी", "hamare" to "हमारे",
        "uska" to "उसका", "uski" to "उसकी", "uske" to "उसके",
        "unka" to "उनका", "unki" to "उनकी", "unke" to "उनके",
        "iska" to "इसका", "iski" to "इसकी", "iske" to "इसके",
        "apna" to "अपना", "apni" to "अपनी", "apne" to "अपने",
        "kisi" to "किसी", "sab" to "सब", "sabka" to "सबका", "sabko" to "सबको",

        // Questions
        "kya" to "क्या", "kyu" to "क्यों", "kyun" to "क्यों", "kab" to "कब",
        "kaha" to "कहाँ", "kahan" to "कहाँ", "kidhar" to "किधर", "kaise" to "कैसे",
        "kaisa" to "कैसा", "kaisi" to "कैसी", "kaun" to "कौन", "kitna" to "कितना",
        "kitne" to "कितने", "kitni" to "कितनी",

        // Chat & Slang Greetings / Addressing
        "bhai" to "भाई", "bhaiya" to "भैया", "yaar" to "यार", "dost" to "दोस्त",
        "bro" to "भाई", "dude" to "यार", "sir" to "सर", "madam" to "मैडम",
        "namaste" to "नमस्ते", "pranam" to "प्रणाम", "ram" to "राम",
        "shukriya" to "शुक्रिया", "dhanyawad" to "धन्यवाद", "alvida" to "अलविदा",

        // State & Auxiliary Verbs
        "hai" to "है", "hain" to "हैं", "ho" to "हो", "hu" to "हूँ", "hoon" to "हूँ",
        "tha" to "था", "thi" to "थी", "the" to "थे",
        "hoga" to "होगा", "hogi" to "होगी", "honge" to "होंगे",
        "raha" to "रहा", "rahi" to "रही", "rahe" to "रहे",
        "hua" to "हुआ", "hui" to "हुई", "hue" to "हुए",

        // Action Verbs
        "kar" to "कर", "karo" to "करो", "karna" to "करना", "karta" to "करता", "karti" to "करती", "karte" to "करते",
        "dekh" to "देख", "dekho" to "देखो", "dekhna" to "देखना", "dekha" to "देखा",
        "sun" to "सुन", "suno" to "सुनो", "sunna" to "सुनना", "suna" to "सुना",
        "bol" to "बोल", "bolo" to "बोलो", "bolna" to "बोलना", "bola" to "बोला",
        "bata" to "बता", "batao" to "बताओ", "batana" to "बताना", "bataya" to "बताया",
        "aa" to "आ", "aao" to "आओ", "aana" to "आना", "aaya" to "आया", "aayi" to "आयी", "aaye" to "आए", "aunga" to "आऊँगा", "aaungi" to "आऊँगी",
        "ja" to "जा", "jao" to "जाओ", "jana" to "जाना", "gaya" to "गया", "gayi" to "गयी", "gaye" to "गए", "jaunga" to "जाऊँगा",
        "chal" to "चल", "chalo" to "चलो", "chalna" to "चलना", "chalte" to "चलते",
        "mil" to "मिल", "milo" to "मिलो", "milna" to "मिलना", "milte" to "मिलते", "mila" to "मिला",
        "soch" to "सोच", "socho" to "सोचो", "sochna" to "सोचना", "socha" to "सोचा",
        "samajh" to "समझ", "samjho" to "समझो", "samjha" to "समझा", "samjhe" to "समझे",
        "le" to "ले", "lo" to "लो", "lena" to "लेना", "liya" to "लिया", "lenge" to "लेंगे",
        "de" to "दे", "do" to "दो", "dena" to "देना", "diya" to "दिया", "denge" to "देंगे",
        "bhej" to "भेज", "bhejo" to "भेजो", "bhejna" to "भेजना", "bheja" to "भेजा",
        "kha" to "खा", "khao" to "खाओ", "khana" to "खाना", "khaya" to "खाया",
        "pee" to "पी", "peeyo" to "पीओ", "peena" to "पीना", "piya" to "पिया",
        "so" to "सो", "soja" to "सोजा", "sojao" to "सो जाओ", "sona" to "सोना", "soya" to "सोया",
        "uth" to "उठ", "utho" to "उठो", "uthna" to "उठना", "utha" to "उठा",
        "baith" to "बैठ", "baitho" to "बैठो", "baithna" to "बैठना",
        "padh" to "पढ़", "padho" to "पढ़ो", "padhna" to "पढ़ना",
        "likh" to "लिख", "likho" to "लिखो", "likhna" to "लिखना", "likha" to "लिखा",
        "bano" to "बनो", "banao" to "बनाओ", "banaya" to "बनाया",
        "pa" to "पा", "paa" to "पा", "paoge" to "पाओगे", "sakta" to "सकता", "sakti" to "सकती", "sakte" to "सकते",

        // Negation & Affirmation
        "ha" to "हाँ", "haan" to "हाँ", "yes" to "हाँ",
        "nahi" to "नहीं", "nahin" to "नहीं", "no" to "नहीं", "na" to "ना",
        "mat" to "मत", "kabhi" to "कभी",

        // Time & Day
        "aaj" to "आज", "kal" to "कल", "parso" to "परसों", "narso" to "नरसों",
        "subah" to "सुबह", "dophar" to "दोपहर", "sham" to "शाम", "shaam" to "शाम", "raat" to "रात",
        "din" to "दिन", "roz" to "रोज़", "hamesha" to "हमेशा",
        "abhi" to "अभी", "baad" to "बाद", "pehle" to "पहले", "jaldi" to "जल्दी", "der" to "देर",
        "samay" to "समय", "waqt" to "वक़्त", "ghanta" to "घंटा", "minute" to "मिनट",

        // Adjectives & Qualifiers
        "theek" to "ठीक", "thik" to "ठीक", "sahi" to "सही", "galat" to "गलत",
        "acha" to "अच्छा", "achha" to "अच्छा", "achhi" to "अच्छी", "achhe" to "अच्छे",
        "bura" to "बुरा", "buri" to "बुरी", "bure" to "बुरे",
        "badhiya" to "बढ़िया", "badiya" to "बढ़िया", "shandar" to "शानदार", "zabardast" to "ज़बरदस्त",
        "mast" to "मस्त", "sundar" to "सुंदर", "khubsurat" to "खूबसूरत",
        "bahut" to "बहुत", "bohot" to "बहुत", "zyada" to "ज़्यादा", "jyada" to "ज़्यादा",
        "kam" to "कम", "thoda" to "थोड़ा", "thodi" to "थोड़ी", "pura" to "पूरा", "puri" to "पूरी",
        "khas" to "ख़ास", "zaruri" to "ज़रूरी", "mushkil" to "मुश्किल", "aasan" to "आसान",
        "naya" to "नया", "nayi" to "नई", "naye" to "नए", "purana" to "पुराना",
        "bada" to "बड़ा", "badi" to "बड़ी", "bade" to "बड़े",
        "chota" to "छोटा", "choti" to "छोटी", "chote" to "छोटे",

        // Places & Objects
        "ghar" to "घर", "office" to "ऑफ़िस", "college" to "कॉलेज", "school" to "स्कूल",
        "rasta" to "रास्ता", "kamra" to "कमरा", "shahar" to "शहर", "gaon" to "गाँव",
        "gaadi" to "गाड़ी", "car" to "कार", "bike" to "बाइक",
        "phone" to "फ़ोन", "mobile" to "मोबाइल", "message" to "मैसेज", "call" to "कॉल",
        "post" to "पोस्ट", "story" to "स्टोरी", "reel" to "रील", "video" to "वीडियो", "photo" to "फ़ोटो",
        "chai" to "चाय", "coffee" to "कॉफ़ी", "paani" to "पानी", "pani" to "पानी",
        "paisa" to "पैसा", "paise" to "पैसे", "rupaye" to "रुपये",

        // Feelings & Relations
        "pyaar" to "प्यार", "pyar" to "प्यार", "mohabbat" to "मोहब्बत", "ishq" to "इश्क़",
        "dil" to "दिल", "khushi" to "ख़ुशी", "hasi" to "हँसी", "gussa" to "गुस्सा",
        "darr" to "डर", "yaad" to "याद", "sapna" to "सपना", "sapne" to "सपने",
        "baat" to "बात", "baatein" to "बातें", "kahani" to "कहानी",
        "maa" to "माँ", "papa" to "पापा", "behan" to "बहन", "beta" to "बेटा", "beti" to "बेटी",

        // Conjunctions & Prepositions
        "aur" to "और", "ya" to "या", "lekin" to "लेकिन", "par" to "पर", "magar" to "मगर",
        "kyunki" to "क्योंकि", "isliye" to "इसलिए", "to" to "तो", "bhi" to "भी",
        "se" to "से", "ko" to "को", "me" to "में", "mein" to "में", "pe" to "पे",
        "ke" to "के", "ki" to "की", "ka" to "का", "sath" to "साथ", "bina" to "बिना", "liye" to "लिए"
    )

    // Reverse dictionary from Hindi to Hinglish for instant reverse transliteration
    private val reverseWordDictionary: Map<String, String> by lazy {
        val map = mutableMapOf<String, String>()
        for ((hinglish, hindi) in wordDictionary) {
            if (!map.containsKey(hindi)) {
                map[hindi] = hinglish
            }
        }
        map
    }

    /**
     * Translates input text from Hinglish (Roman English) to Hindi Devanagari.
     */
    fun translateHinglishToHindi(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ""

        // 1. Direct whole-phrase lookup (ignoring case)
        val normalizedLower = trimmed.lowercase(Locale.ROOT)
        phraseDictionary[normalizedLower]?.let { return it }

        // 2. Tokenize by words and punctuation
        val tokens = tokenize(trimmed)
        val builder = StringBuilder()

        for (token in tokens) {
            if (isWord(token)) {
                val lower = token.lowercase(Locale.ROOT)
                // Check word dictionary
                val translatedWord = wordDictionary[lower]
                    ?: phoneticTransliterateWord(lower)
                builder.append(translatedWord)
            } else {
                // Punctuation or whitespace preserved
                builder.append(token)
            }
        }

        return builder.toString()
    }

    /**
     * Translates Devanagari Hindi text back to Hinglish (Roman script).
     */
    fun translateHindiToHinglish(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ""

        val tokens = tokenizeDevanagari(trimmed)
        val builder = StringBuilder()

        for (token in tokens) {
            val matched = reverseWordDictionary[token]
            if (matched != null) {
                builder.append(matched)
            } else {
                builder.append(phoneticDevanagariToRoman(token))
            }
        }

        return builder.toString()
    }

    /**
     * High-accuracy phonetic algorithm that converts any arbitrary Hinglish word
     * into readable Devanagari characters using vowel matras and consonants.
     */
    private fun phoneticTransliterateWord(word: String): String {
        val sb = StringBuilder()
        var i = 0
        val len = word.length

        // Specific prefixes
        if (word == "aaj") return "आज"
        if (word == "aur") return "और"
        if (word == "ek") return "एक"

        var isStart = true

        while (i < len) {
            // Check 3-letter clusters
            if (i + 3 <= len) {
                val sub3 = word.substring(i, i + 3)
                when (sub3) {
                    "chh" -> { sb.append("छ"); i += 3; isStart = false; continue }
                    "ksh" -> { sb.append("क्ष"); i += 3; isStart = false; continue }
                }
            }

            // Check 2-letter clusters
            if (i + 2 <= len) {
                val sub2 = word.substring(i, i + 2)
                val mappedConsonant = when (sub2) {
                    "kh" -> "ख"
                    "gh" -> "घ"
                    "ch" -> "च"
                    "jh" -> "झ"
                    "th" -> "थ"
                    "dh" -> "ध"
                    "ph" -> "फ"
                    "bh" -> "भ"
                    "sh" -> "श"
                    "zh" -> "ज़"
                    "tr" -> "त्र"
                    "gy" -> "ज्ञ"
                    "ng" -> "ंग"
                    "aa" -> if (isStart) "आ" else "ा"
                    "ee", "ii" -> if (isStart) "ई" else "ी"
                    "oo", "uu" -> if (isStart) "ऊ" else "ू"
                    "ai" -> if (isStart) "ऐ" else "ै"
                    "au", "ou" -> if (isStart) "औ" else "ौ"
                    else -> null
                }
                if (mappedConsonant != null) {
                    sb.append(mappedConsonant)
                    i += 2
                    isStart = false
                    continue
                }
            }

            // Single letter
            val c = word[i]
            val mapped = when (c) {
                'a' -> if (isStart) "अ" else "" // implicit vowel or trailing matra
                'e' -> if (isStart) "ए" else "े"
                'i' -> if (isStart) "इ" else "ि"
                'o' -> if (isStart) "ओ" else "ो"
                'u' -> if (isStart) "उ" else "ु"
                'k' -> "क"
                'g' -> "ग"
                'c' -> "क"
                'j' -> "ज"
                't' -> "त"
                'd' -> "द"
                'n' -> "न"
                'p' -> "प"
                'f' -> "फ़"
                'b' -> "ब"
                'm' -> "म"
                'y' -> "य"
                'r' -> "र"
                'l' -> "ल"
                'v', 'w' -> "व"
                's' -> "स"
                'h' -> "ह"
                'z' -> "ज़"
                'q' -> "क़"
                'x' -> "क्स"
                else -> c.toString()
            }
            sb.append(mapped)
            i++
            isStart = false
        }

        return sb.toString()
    }

    /**
     * Converts Devanagari characters to Roman script phonetically.
     */
    private fun phoneticDevanagariToRoman(text: String): String {
        val map = mapOf(
            'अ' to "a", 'आ' to "aa", 'इ' to "i", 'ई' to "ee", 'उ' to "u", 'ऊ' to "oo",
            'ए' to "e", 'ऐ' to "ai", 'ओ' to "o", 'औ' to "au", 'क' to "k", 'ख' to "kh",
            'ग' to "g", 'घ' to "gh", 'च' to "ch", 'छ' to "chh", 'ज' to "j", 'झ' to "jh",
            'ट' to "t", 'ठ' to "th", 'ड' to "d", 'ढ' to "dh", 'ण' to "n", 'त' to "t",
            'थ' to "th", 'द' to "d", 'ध' to "dh", 'न' to "n", 'प' to "p", 'फ' to "ph",
            'ब' to "b", 'भ' to "bh", 'म' to "m", 'य' to "y", 'र' to "r", 'ल' to "l",
            'व' to "v", 'श' to "sh", 'ष' to "sh", 'स' to "s", 'ह' to "h", 'ा' to "a",
            'ि' to "i", 'ी' to "ee", 'ु' to "u", 'ू' to "oo", 'े' to "e", 'ै' to "ai",
            'ो' to "o", 'ौ' to "au", '्' to "", 'ं' to "n", 'ः' to "h"
        )
        val sb = StringBuilder()
        for (ch in text) {
            sb.append(map[ch] ?: ch.toString())
        }
        return sb.toString()
    }

    private fun tokenize(text: String): List<String> {
        val list = mutableListOf<String>()
        val current = StringBuilder()
        var currentIsWord = false

        for (ch in text) {
            val isAlpha = ch.isLetter() || ch.isDigit()
            if (current.isEmpty()) {
                current.append(ch)
                currentIsWord = isAlpha
            } else if (currentIsWord == isAlpha) {
                current.append(ch)
            } else {
                list.add(current.toString())
                current.clear()
                current.append(ch)
                currentIsWord = isAlpha
            }
        }
        if (current.isNotEmpty()) {
            list.add(current.toString())
        }
        return list
    }

    private fun tokenizeDevanagari(text: String): List<String> {
        val list = mutableListOf<String>()
        val current = StringBuilder()
        var currentIsWord = false

        for (ch in text) {
            val isWordChar = !ch.isWhitespace() && ch !in ".,!?;:-()[]{}\"'"
            if (current.isEmpty()) {
                current.append(ch)
                currentIsWord = isWordChar
            } else if (currentIsWord == isWordChar) {
                current.append(ch)
            } else {
                list.add(current.toString())
                current.clear()
                current.append(ch)
                currentIsWord = isWordChar
            }
        }
        if (current.isNotEmpty()) {
            list.add(current.toString())
        }
        return list
    }

    private fun isWord(token: String): Boolean {
        return token.isNotEmpty() && (token[0].isLetter() || token[0].isDigit())
    }
}
