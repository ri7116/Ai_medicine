package com.example.chatbot.features.pharmacy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatbot.ui.theme.ChatBotTheme

@Composable
fun PharmacyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "여기는 약국 화면입니다.")
    }
}

@Preview(showBackground = true)
@Composable
fun PharmacyScreenPreview() {
    ChatBotTheme {
        PharmacyScreen()
    }
}
