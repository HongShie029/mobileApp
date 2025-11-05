package com.example.w06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w06.ui.theme.Test2Theme
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.random.Random

// 🧠 게임 전체 상태 관리용 데이터 클래스
data class GameState(
    var bubbles: List<Bubble> = listOf(),
    var score: Int = 0,
    var timeLeft: Int = 30,
    var isGameOver: Boolean = false
)

// 🎈 버블 데이터 클래스
data class Bubble(
    val id: Int,
    var position: Offset,
    val radius: Float,
    val color: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleGameScreen()
                }
            }
        }
    }
}

@Composable
fun BubbleGameScreen() {
    var gameState by remember { mutableStateOf(GameState()) }

    // ⏰ 타이머 동작
    LaunchedEffect(gameState.timeLeft, gameState.isGameOver) {
        if (!gameState.isGameOver && gameState.timeLeft > 0) {
            while (gameState.timeLeft > 0) {
                delay(1000)
                gameState = gameState.copy(timeLeft = gameState.timeLeft - 1)
            }
            gameState = gameState.copy(isGameOver = true)
        }
    }

    // 🎈 버블 생성 루프
    LaunchedEffect(gameState.isGameOver) {
        while (!gameState.isGameOver) {
            delay(800)
            val newBubble = Bubble(
                id = Random.nextInt(),
                position = Offset(
                    x = Random.nextFloat() * 800,
                    y = Random.nextFloat() * 1600
                ),
                radius = Random.nextFloat() * 60 + 40,
                color = Color(
                    red = Random.nextInt(256),
                    green = Random.nextInt(256),
                    blue = Random.nextInt(256),
                    alpha = 220
                )
            )
            gameState = gameState.copy(bubbles = gameState.bubbles + newBubble)
        }
    }

    // 🎮 메인 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8FF))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GameStatusRow(score = gameState.score, timeLeft = gameState.timeLeft)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(gameState.bubbles) {
                        detectTapGestures { tapOffset ->
                            val tapped = gameState.bubbles.findLast {
                                hypot(
                                    tapOffset.x - it.position.x,
                                    tapOffset.y - it.position.y
                                ) < it.radius
                            }
                            if (tapped != null) {
                                gameState = gameState.copy(
                                    score = gameState.score + 1,
                                    bubbles = gameState.bubbles.filter { it.id != tapped.id }
                                )
                            }
                        }
                    }
            ) {
                BubbleCanvas(gameState.bubbles)
            }
        }

        // 💥 게임 오버 다이얼로그
        if (gameState.isGameOver) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Game Over") },
                text = { Text("Final Score: ${gameState.score}") },
                confirmButton = {
                    TextButton(onClick = {
                        gameState = GameState() // 다시 시작
                    }) {
                        Text("Restart")
                    }
                }
            )
        }
    }
}

// 🫧 버블들을 화면에 그림
@Composable
fun BubbleCanvas(bubbles: List<Bubble>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        bubbles.forEach { bubble ->
            drawCircle(
                color = bubble.color,
                radius = bubble.radius,
                center = bubble.position
            )
        }
    }
}

// 상단 점수/타이머 표시
@Composable
fun GameStatusRow(score: Int, timeLeft: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Score: $score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Time: ${timeLeft}s", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBubbleGame() {
    Test2Theme {
        BubbleGameScreen()
    }
}
