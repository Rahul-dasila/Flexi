package com.example.flexie.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ehsanmsz.mszprogressindicator.progressindicator.BallSpinFadeLoaderProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.LineSpinFadeLoaderProgressIndicator

@Composable
fun Progressbar() {
    LineSpinFadeLoaderProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        color = Color.Red,
        animationDuration = 800,
        isClockwise = true
    )
}

@Composable
fun ProgressBar2() {
    BallSpinFadeLoaderProgressIndicator(
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
        color = Color.White,
        animationDuration = 1000
    )
}