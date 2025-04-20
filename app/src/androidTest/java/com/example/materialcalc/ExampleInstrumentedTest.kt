package com.example.materialcalc

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val rule = createComposeRule()

    fun sleep() {
        Thread.sleep(1000)
    }

    @Test
    fun basicCalc() {
        rule.setContent {
            Calculator()
        }
        sleep()
        rule.onNodeWithText("5").performClick()
        sleep()
        rule.onNodeWithText("×").performClick()
        sleep()
        rule.onNodeWithText("5").performClick()
        sleep()
        rule.onNodeWithText("=").performClick()
        sleep()
        rule.onNodeWithText("25.0").assertExists()
        sleep()
    }

    @Test
    fun errorHandling() {
        rule.setContent {
            Calculator()
        }
        sleep()
        rule.onNodeWithText("5").performClick()
        sleep()
        rule.onNodeWithText("÷").performClick()
        sleep()
        rule.onNodeWithText("0").performClick()
        sleep()
        rule.onNodeWithText("=").performClick()
        sleep()
        rule.onNodeWithText("Syntax Error").assertExists()
        sleep()
    }
}