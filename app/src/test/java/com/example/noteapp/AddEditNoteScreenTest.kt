package com.example.noteapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteContent
import com.example.noteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [34],
    qualifiers = "w411dp-h891dp-mdpi",
    fontScale = 1.0f
)
class AddEditNoteScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    // ✅ Test 1: Title text field with hint
    @Test
    fun snapshot_titleField_withHint() {
        composeTestRule.setContent {
            MaterialTheme {
                TransparentHintTextField(
                    text = "",
                    hint = "Title",
                    onValueChange = {},
                    onFocusChange = {},
                    isHintVisible = true,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage("titleField_hint.png")
    }

    // ✅ Test 2: Title field with text
    @Test
    fun snapshot_titleField_withText() {
        composeTestRule.setContent {
            MaterialTheme {
                TransparentHintTextField(
                    text = "My Shopping List",
                    hint = "Title",
                    onValueChange = {},
                    onFocusChange = {},
                    isHintVisible = false,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage("titleField_text.png")
    }

    // ✅ Test 3: Full screen — yellow background
    @Test
    fun snapshot_fullScreen_yellowBackground() {
        composeTestRule.setContent {
            MaterialTheme {
                AddEditNoteContent(
                    backgroundColor = Color.Yellow,
                    titleText = "My Note",
                    contentText = "Note body goes here...",
                    selectedColor = Color.Yellow.toArgb(),
                    onColorClick = {},
                    onTitleChange = {},
                    onContentChange = {},
                    onSaveClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage("fullScreen_yellow.png")
    }

    // ✅ Test 4: Dark theme
    @Test
    fun snapshot_darkTheme() {
        composeTestRule.setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                AddEditNoteContent(
                    backgroundColor = Color(0xFF1C1C1E),
                    titleText = "Dark Theme Note",
                    contentText = "",
                    selectedColor = Color.Red.toArgb(),
                    onColorClick = {},
                    onTitleChange = {},
                    onContentChange = {},
                    onSaveClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage("fullScreen_dark.png")
    }
}