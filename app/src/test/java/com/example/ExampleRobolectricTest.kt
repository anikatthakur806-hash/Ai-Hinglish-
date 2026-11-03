package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.HinglishTranslator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("HiTranslate", appName)
  }

  @Test
  fun `test hinglish to hindi chat translation`() {
    val input = "kya kar rahe ho bhai"
    val result = HinglishTranslator.translateHinglishToHindi(input)
    assertEquals("क्या कर रहे हो भाई", result)
  }

  @Test
  fun `test reverse hindi to hinglish translation`() {
    val input = "क्या कर रहे हो"
    val result = HinglishTranslator.translateHindiToHinglish(input)
    assertTrue(result.contains("kya"))
  }
}
