package com.example.materialcalc

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 30.dp)
        ) {
            Text(displayText, fontSize = 60.sp, modifier = Modifier.align(Alignment.CenterEnd))
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentPadding = PaddingValues(bottom = 20.dp, end = 5.dp, start = 5.dp, top = 15.dp)
        ) {
            items(buttons) {
                BoxWithConstraints(modifier = Modifier.padding(5.dp)) {
                    Button(
                        onClick = {
                            when (it) {
                                "AC" -> displayText = ""
                                "backspace" -> displayText = displayText.dropLast(1)
                                "=" -> {
                                    val expression = Expression(displayText)
                                    if (expression.checkSyntax()) {
                                        displayText = expression.calculate().toString()
                                    } else {
                                        displayText = "Syntax Error"
                                    }
                                }

                                else -> displayText += it
                            }
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(minWidth),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        if (it !== "backspace") {
                            Text(it, fontSize = 25.sp, modifier = Modifier.padding(10.dp))
                        } else {
                            Icon(Icons.Default.ArrowBack, contentDescription = "")
                        }
                    }
                }
            }
        }
    }
}