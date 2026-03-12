package com.example.noteapp.feature_note.presentation.signIn.phone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInput(
    onOtpComplete: (String) -> Unit
) {

    val otpLength = 6
    var otp by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        repeat(otpLength) { index ->

            val char = otp.getOrNull(index)?.toString() ?: ""

            OutlinedTextField(
                value = char,
                onValueChange = { value ->

                    if (value.length <= 1) {

                        otp = when {
                            value.isEmpty() && index < otp.length -> {
                                otp.removeRange(index, index + 1)
                            }

                            value.isNotEmpty() && otp.length <= index -> {
                                otp + value
                            }

                            value.isNotEmpty() && otp.length > index -> {
                                otp.replaceRange(index, index + 1, value)
                            }

                            else -> otp
                        }.take(otpLength)

                        if (otp.length == otpLength) {
                            onOtpComplete(otp)
                        }
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .height(60.dp),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )
        }
    }
}

