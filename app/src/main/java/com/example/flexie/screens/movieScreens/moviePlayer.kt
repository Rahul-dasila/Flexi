package com.example.flexie.screens.movieScreens

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.flexie.R
import com.example.flexie.ViewModels.PlayerViewModel
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.ProgressBar2
import com.example.flexie.utils.setOrientation2
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun MoviePlayer(
    movieUri: String,
    viewModel: PlayerViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val view = LocalView.current
    val configuration = LocalConfiguration.current

    if (viewModel.uri.isEmpty()) {
        LaunchedEffect(key1 = Unit) {
            viewModel.prepareMediaItem(movieUri, context)
            viewModel.uri = movieUri
        }
    }

    DisposableEffect(Unit) {
        viewModel.play()
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
            viewModel.pause()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimen.dimen.height3)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        AndroidView(
            factory = {
                PlayerView(context).apply {
                    this.player = viewModel.player
                    this.useController = true
                    this.controllerAutoShow = false
                    this.controllerHideOnTouch = true
                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    this.layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = Gravity.CENTER
                    }

                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (viewModel.isFullScreen) {
            viewModel.pause()
        }

        var isClickable by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .padding(
                    end = Dimen.dimen.paddingMedium,
                    top = Dimen.dimen.paddingMedium
                )
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    if (isClickable) {
                        isClickable = false
                        navHostController.navigate("full")
                        // Using coroutine to handle the delay directly in the onClick
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(10000)
                            isClickable = true
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_fullscreen_24),
                contentDescription = "backBtn",
                modifier = Modifier
                    .padding(start = Dimen.dimen.small3 , bottom = Dimen.dimen.small3 , end = Dimen.dimen.small3)
                    .size(Dimen.dimen.medium2)
            )
        }

        if(viewModel.videoLoading){
            Box(modifier = Modifier.align(Alignment.Center)) {
                ProgressBar2()
            }
        }
    }
}


@OptIn(UnstableApi::class)
@Composable
fun fullMoviePlayer(playerViewModel: PlayerViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = context as Activity
    val systemUiController = rememberSystemUiController()
    var click by mutableStateOf(false)
    val window = activity.window
    activity.setOrientation2()
    DisposableEffect(Unit) {
        playerViewModel.play()
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
            playerViewModel.pause()
        }
    }

    DisposableEffect(key1 = Unit) {
        systemUiController.isSystemBarsVisible = false
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    }



    Box(modifier = Modifier
        .fillMaxSize()) {

        AndroidView(
            factory = {
                PlayerView(context).apply {
                    this.player = playerViewModel.player
                    this.useController = true
                    this.controllerAutoShow = false
                    this.controllerHideOnTouch = true
                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    this.setOnClickListener {
                        if(systemUiController.isSystemBarsVisible){
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed(Runnable {
                                  systemUiController.isSystemBarsVisible = false
                            } , 2000)
                        }
                    }

                    // Adjust layout parameters if needed
                    this.layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        gravity = Gravity.CENTER
                    }

                }
            },
            modifier = Modifier.fillMaxSize()

        )
        var isClickable2 by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .padding(
                    end = Dimen.dimen.paddingMedium,
                    top = Dimen.dimen.paddingMedium
                )
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    if (isClickable2) {
                        isClickable2 = false
                        navHostController.popBackStack()
                        // Using coroutine to handle the delay directly in the onClick
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(10000)
                            isClickable2 = true
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_fullscreen_exit_24),
                contentDescription = "Full screen Exit",
                modifier = Modifier
                    .padding(
                        top = Dimen.dimen.extraSmall,
                        bottom = Dimen.dimen.small3,
                        start = Dimen.dimen.small3
                    )
                    .size(Dimen.dimen.small3)
            )
        }

        var isClickable by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .padding(
                    start = Dimen.dimen.paddingMedium,
                    top = Dimen.dimen.paddingMedium
                )
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.TopStart)
                .clickable {
                    if (isClickable) {
                        isClickable = false
                        navHostController.popBackStack()
                        // Using coroutine to handle the delay directly in the onClick
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(10000)
                            isClickable = true
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "backBtn",
                modifier = Modifier
                    .padding(
                        top = Dimen.dimen.extraSmall,
                        bottom = Dimen.dimen.small3,
                        end = Dimen.dimen.small3
                    )
                    .size(Dimen.dimen.small3)
            )
        }

        if(playerViewModel.videoLoading){
            Box(modifier = Modifier.align(Alignment.Center)) {
                ProgressBar2()
            }
        }
    }
}
