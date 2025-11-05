package com.example.w051

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w051.ui.theme.Test2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test2Theme {
                CombinedScreen()
            }
        }
    }
}

// 🌟 Counter + StopWatch 통합 화면
@Composable
fun CombinedScreen() {
    val count = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CounterApp(count)
        Spacer(modifier = Modifier.height(48.dp))
        StopWatchApp()
    }
}

// ---------------- Counter ----------------
@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: ${count.value}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { count.value++ }) {
                Text("Increase")
            }
            Button(onClick = { count.value = 0 }) {
                Text("Reset")
            }
        }
    }
}

// ---------------- StopWatch ----------------
@Composable
fun StopWatchApp() {
    var time by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    // 타이머 동작
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            time += 1000L
        }
    }

    val formattedTime = formatTime(time)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formattedTime,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isRunning = true }) {
                Text("Start")
            }
            Button(onClick = { isRunning = false }) {
                Text("Stop")
            }
            Button(onClick = {
                isRunning = false
                time = 0L
            }) {
                Text("Reset")
            }
        }
    }
}

// ---------------- 시간 포맷 함수 ----------------
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

// ---------------- Preview ----------------
@Preview(showBackground = true)
@Composable
fun CombinedPreview() {
    val count = remember { mutableStateOf(0) } // Interactive Mode용 상태 변수
    Test2Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CounterApp(count)
            Spacer(modifier = Modifier.height(48.dp))
            StopWatchApp()
        }
    }
}
