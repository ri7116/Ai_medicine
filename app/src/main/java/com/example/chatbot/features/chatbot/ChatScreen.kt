package com.example.chatbot.features.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.ui.theme.ChatBotTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessageUiModel(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long
)

fun Long.formatTime(): String {
    val sdf = SimpleDateFormat("a hh:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

val Gray200 = Color(0xFFEEEEEE)
val Gray300 = Color(0xFFE0E0E0)
val Gray500 = Color(0xFF9E9E9E)
val Gray900 = Color(0xFF212121)

val TextSRegular = TextStyle(fontSize = 16.sp, color = Gray900)
val InfoS = TextStyle(fontSize = 12.sp, color = Gray500)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    val sampleMessages = listOf(
        ChatMessageUiModel("안녕하세요! AI 약사입니다.", false, System.currentTimeMillis() - 200000),
        ChatMessageUiModel("요즘 체력이 안 좋은데 약 추천해줄 수 있어?", true, System.currentTimeMillis() - 50000),
        ChatMessageUiModel("네, 타우린을 추천드려요", false, System.currentTimeMillis())
    )
    val messages = remember { mutableStateListOf(*sampleMessages.toTypedArray()) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("메시지 입력...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (textInput.isNotBlank()) {
                        messages.add(
                            ChatMessageUiModel(textInput, true, System.currentTimeMillis())
                        )
                        val currentInput = textInput
                        textInput = ""
                        scope.launch {
                            kotlinx.coroutines.delay(1000)
                            messages.add(
                                ChatMessageUiModel("'$currentInput'라고 하셨네요!", false, System.currentTimeMillis())
                            )
                        }
                    }
                }) {
                    Text("전송")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(chatMessage = message)
            }
        }
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
}

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessageUiModel,
) {
    val messageArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = messageArrangement,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (chatMessage.isUser) {
            TimeText(time = chatMessage.timestamp.formatTime())
            Spacer(modifier = Modifier.width(8.dp))
            MessageBox(
                message = chatMessage.message,
                isUser = true,
            )
        } else {
            ProfileImage(
                modifier = Modifier
                    .align(Alignment.Top)
                    .size(48.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            MessageBox(
                message = chatMessage.message,
                isUser = false,
            )
            Spacer(modifier = Modifier.width(8.dp))
            TimeText(time = chatMessage.timestamp.formatTime())
        }
    }
}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    isUser: Boolean,
) {
    val maxWidthDp = LocalConfiguration.current.screenWidthDp.dp * 2 / 3
    Box(
        modifier = modifier
            .widthIn(max = if (isUser) maxWidthDp else maxWidthDp - 56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isUser) Gray200 else Gray300)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(all = 4.dp),
            style = TextSRegular,
        )
    }
}

@Composable
fun TimeText(time: String) {
    Text(
        text = time,
        style = InfoS,
    )
}

@Composable
fun ProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Gray500)
    )
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatBotTheme {
        ChatScreen()
    }
}
