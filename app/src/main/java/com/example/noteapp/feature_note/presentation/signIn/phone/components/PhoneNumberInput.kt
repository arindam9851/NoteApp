package com.example.noteapp.feature_note.presentation.signIn.phone.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhoneNumberInput(
    onPhoneChange: (String) -> Unit
) {

    var countryCode by remember { mutableStateOf("+46") }
    var phone by remember { mutableStateOf("") }

    Column {

        Text(
            text = "Country              Phone",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = countryCode,
                onValueChange = {
                    countryCode = it
                    onPhoneChange(countryCode + phone)
                },
                modifier = Modifier.width(90.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    onPhoneChange(countryCode + phone)
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
        }
    }
}

