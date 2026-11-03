package com.example.service

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.engine.HinglishTranslator
import kotlin.math.abs

class FloatingBubbleService : Service() {

    private var windowManager: WindowManager? = null
    private var bubbleView: View? = null
    private var overlayDialogView: View? = null
    private var isDialogOpen = false

    companion object {
        var isServiceRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createFloatingBubble()
        isServiceRunning = true
    }

    private fun createFloatingBubble() {
        val sizePx = (56 * resources.displayMetrics.density).toInt()
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            sizePx,
            sizePx,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 20
            y = 300
        }

        // Circular styled floating ball
        val bubbleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                colors = intArrayOf(Color.parseColor("#2563EB"), Color.parseColor("#1D4ED8"))
                gradientType = GradientDrawable.LINEAR_GRADIENT
                setStroke((2 * resources.displayMetrics.density).toInt(), Color.WHITE)
            }
            elevation = 16f
        }

        val textIcon = TextView(this).apply {
            text = "अ/A"
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        bubbleLayout.addView(textIcon)

        // Dragging & Click Handling
        bubbleLayout.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var isClick = false

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isClick = true
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - initialTouchX
                        val dy = event.rawY - initialTouchY
                        if (abs(dx) > 10 || abs(dy) > 10) {
                            isClick = false
                        }
                        params.x = initialX + dx.toInt()
                        params.y = initialY + dy.toInt()
                        try {
                            windowManager?.updateViewLayout(bubbleLayout, params)
                        } catch (_: Exception) {}
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (isClick) {
                            toggleOverlayDialog()
                        }
                        return true
                    }
                }
                return false
            }
        })

        bubbleView = bubbleLayout
        try {
            windowManager?.addView(bubbleLayout, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun toggleOverlayDialog() {
        if (isDialogOpen) {
            dismissOverlayDialog()
        } else {
            showOverlayDialog()
        }
    }

    private fun showOverlayDialog() {
        if (overlayDialogView != null) return

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val dialogWidth = (320 * resources.displayMetrics.density).toInt()
        val params = WindowManager.LayoutParams(
            dialogWidth,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        val density = resources.displayMetrics.density

        // Card container
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding((16 * density).toInt(), (16 * density).toInt(), (16 * density).toInt(), (16 * density).toInt())
            background = GradientDrawable().apply {
                cornerRadius = 24 * density
                setColor(Color.parseColor("#0F172A"))
                setStroke((1 * density).toInt(), Color.parseColor("#334155"))
            }
            elevation = 20f
        }

        // Header
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val title = TextView(this).apply {
            text = "✨ HiTranslate Quick Ball"
            setTextColor(Color.WHITE)
            textSize = 16f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        val closeBtn = Button(this).apply {
            text = "✕"
            setTextColor(Color.parseColor("#94A3B8"))
            setBackgroundColor(Color.TRANSPARENT)
            textSize = 18f
            setOnClickListener { dismissOverlayDialog() }
        }
        header.addView(title)
        header.addView(closeBtn)
        card.addView(header)

        // Input field
        val input = EditText(this).apply {
            hint = "Type or paste Hinglish (e.g. kya kar rahe ho)..."
            setHintTextColor(Color.parseColor("#64748B"))
            setTextColor(Color.WHITE)
            textSize = 14f
            background = GradientDrawable().apply {
                cornerRadius = 12 * density
                setColor(Color.parseColor("#1E293B"))
                setStroke((1 * density).toInt(), Color.parseColor("#475569"))
            }
            setPadding((12 * density).toInt(), (12 * density).toInt(), (12 * density).toInt(), (12 * density).toInt())
        }

        // Check clipboard for prefilled text
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipItem = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
        if (!clipItem.isNullOrBlank() && clipItem.length < 200) {
            input.setText(clipItem)
        }

        card.addView(input)

        // Translated output area
        val outputText = TextView(this).apply {
            text = "Hindi text will appear here..."
            setTextColor(Color.parseColor("#93C5FD"))
            textSize = 16f
            setPadding((12 * density).toInt(), (10 * density).toInt(), (12 * density).toInt(), (10 * density).toInt())
            visibility = View.GONE
        }
        card.addView(outputText)

        // Buttons row
        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END
            val margin = (8 * density).toInt()
            setPadding(0, margin, 0, 0)
        }

        val translateBtn = Button(this).apply {
            text = "Translate to Hindi"
            setTextColor(Color.WHITE)
            textSize = 12f
            background = GradientDrawable().apply {
                cornerRadius = 12 * density
                setColor(Color.parseColor("#2563EB"))
            }
            setOnClickListener {
                val query = input.text.toString()
                if (query.isNotBlank()) {
                    val hindi = HinglishTranslator.translateHinglishToHindi(query)
                    outputText.text = hindi
                    outputText.visibility = View.VISIBLE
                }
            }
        }

        val copyBtn = Button(this).apply {
            text = "📋 Copy"
            setTextColor(Color.WHITE)
            textSize = 12f
            background = GradientDrawable().apply {
                cornerRadius = 12 * density
                setColor(Color.parseColor("#10B981"))
            }
            val paramsBtn = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = (8 * density).toInt()
            }
            layoutParams = paramsBtn
            setOnClickListener {
                val textToCopy = if (outputText.visibility == View.VISIBLE && outputText.text.isNotBlank()) {
                    outputText.text.toString()
                } else {
                    HinglishTranslator.translateHinglishToHindi(input.text.toString())
                }
                val clip = ClipData.newPlainText("Hindi Text", textToCopy)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this@FloatingBubbleService, "Hindi Copied! Paste in Instagram", Toast.LENGTH_SHORT).show()
                dismissOverlayDialog()
            }
        }

        buttonRow.addView(translateBtn)
        buttonRow.addView(copyBtn)
        card.addView(buttonRow)

        overlayDialogView = card
        try {
            windowManager?.addView(card, params)
            isDialogOpen = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dismissOverlayDialog() {
        overlayDialogView?.let {
            try {
                windowManager?.removeView(it)
            } catch (_: Exception) {}
            overlayDialogView = null
        }
        isDialogOpen = false
    }

    override fun onDestroy() {
        dismissOverlayDialog()
        bubbleView?.let {
            try {
                windowManager?.removeView(it)
            } catch (_: Exception) {}
            bubbleView = null
        }
        isServiceRunning = false
        super.onDestroy()
    }
}
