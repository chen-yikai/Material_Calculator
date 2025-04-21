package com.example.materialcalc

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mariuszgromada.math.mxparser.Expression

val buttons = listOf<String>(
    "AC",
    "( )",
    "%",
    "÷",
    "7",
    "8",
    "9",
    "×",
    "4",
    "5",
    "6",
    "-",
    "1",
    "2",
    "3",
    "+",
    "0",
    ".",
    "backspace",
    "="
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(showBackground = true)
@Composable
fun Calculator() {
    var displayText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 30.dp)
        ) {
            LazyRow(modifier = Modifier.align(Alignment.CenterEnd)) {
                item {
                    when (displayText) {
                        in listOf<String>("error", "NaN") ->
                            Text(
                                "Syntax Error",
                                fontSize = 50.sp,
                                color = MaterialTheme.colorScheme.error
                            )

                        else ->
                            Text(
                                displayText,
                                fontSize = 80.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                    }
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            contentPadding = PaddingValues(
                end = 5.dp,
                start = 5.dp,
                top = 15.dp
            )
        ) {
            items(buttons) {
                BoxWithConstraints(modifier = Modifier.padding(5.dp)) {
                    var isPress by remember { mutableStateOf(false) }
                    val rounded by animateDpAsState(
                        targetValue = if (isPress) 20.dp else 50.dp,
                        animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f),
                        label = "button rounded change"
                    )
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (it) {
                                in listOf<String>(
                                    "AC",
                                    "( )",
                                    "÷",
                                    "%",
                                    "×",
                                    "-",
                                    "+"
                                ) -> MaterialTheme.colorScheme.tertiary

                                "=" -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.secondary
                            }
                        ),
                        onClick = {
                            when (it) {
                                "AC" -> {
                                    displayText = ""
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                                    )
                                }

                                "backspace" -> {
                                    displayText = displayText.dropLast(1)
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                    )
                                }

                                "=" -> {
                                    val expression = Expression(displayText)
                                    if (expression.checkSyntax()) {
                                        displayText = expression.calculate().toString()
                                    } else {
                                        displayText = "error"
                                    }
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(
                                            VibrationEffect.EFFECT_CLICK
                                        )
                                    )
                                }

                                else -> {
                                    displayText += it
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .size(minWidth)
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        event.changes.forEach { change ->
                                            if (change.pressed) {
                                                isPress = true
                                            } else {
                                                isPress = false
                                            }
                                        }
                                    }
                                }
                            },
                        shape = RoundedCornerShape(rounded),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        when (it) {
                            "backspace" ->
                                Icon(
                                    Icons.Default.ArrowBack,
                                    modifier = Modifier.size(40.dp),
                                    contentDescription = ""
                                )

                            else -> Text(
                                it,
                                fontSize = 40.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}