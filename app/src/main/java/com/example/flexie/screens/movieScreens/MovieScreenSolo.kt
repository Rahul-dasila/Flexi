package com.example.flexie.screens.movieScreens

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.flexie.R
import com.example.flexie.ViewModels.PlayerViewModel
import com.example.flexie.ViewModels.SharedViewModelMovie
import com.example.flexie.models.movie_home_row
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.shimmerColor
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.setOrientation
import com.example.flexie.utils.setSystemBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun movieScreenSolo(
    playerViewModel: PlayerViewModel,
    navHostController: NavHostController,
    id: String,
    sharedViewModelMovie: SharedViewModelMovie
) {
    val context = LocalContext.current
    val activity = context as Activity
    val moreLikeThis = sharedViewModelMovie._moreLikeThis.collectAsState().value
    activity.setOrientation()
    setSystemBarColor(statusBarColor = darkBlue)

    LaunchedEffect(key1 = sharedViewModelMovie._category) {
        Log.d("rahul","cheeck2")
        if (sharedViewModelMovie._category.value.isNotEmpty()) {
            Log.d("rahul","cheeck")
            sharedViewModelMovie.loadMoreMovies()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
            .windowInsetsPadding(WindowInsets.safeDrawing)

    ) {
        if (sharedViewModelMovie.movieData != null) {
            sharedViewModelMovie.addWatching(id)
            Column(modifier = Modifier.fillMaxSize()) {
                Box {
                    MoviePlayer(
                        movieUri = sharedViewModelMovie.movieData!!.videoPath,
                        viewModel = playerViewModel,
                        navHostController = navHostController
                    )
                }
                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(start = Dimen.dimen.paddingMedium)
                ) {
                    Text(
                        text = sharedViewModelMovie.movieData!!.name,
                        fontSize = Dimen.dimen.fontSizeHeadLine.sp,
                        fontFamily = FontFamily(
                            Font(R.font.helvetica_neue)
                        ),
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(Dimen.dimen.padding2))
                    Row {
                        val list = listOf(
                            sharedViewModelMovie.movieData!!.category[0],
                            sharedViewModelMovie.movieData!!.year,
                            sharedViewModelMovie.movieData!!.rating,
                            sharedViewModelMovie.movieData!!.uA
                        )
                        list.forEach {
                            Text(
                                text = it.toString(),
                                fontSize = Dimen.dimen.fontSizeSmall.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.helvetica_neue)
                                ),
                                color = Color.LightGray
                            )
                            Spacer(modifier = Modifier.width(Dimen.dimen.padding2))
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimen.dimen.paddingSmall))
                }
                additionalOptions()
                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
                if (!sharedViewModelMovie.loading1) {
                    if (moreLikeThis.isNotEmpty()) {
                        Text(
                            text = "More like this", color = Color.LightGray,
                            fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                            letterSpacing = 0.7.sp,
                            fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = Dimen.dimen.paddingMedium,
                                top = Dimen.dimen.paddingMedium
                            )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimen.dimen.boxHeight)
                                .padding(
                                    top = Dimen.dimen.paddingSmall - 2.5.dp,
                                    start = Dimen.dimen.padding4,
                                    end = Dimen.dimen.padding4
                                )
                        ) {
                            Log.d("moreLikeThis" , "$moreLikeThis")
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(1.dp),
                                modifier = Modifier
                                    .fillMaxWidth() // Use width constraint instead of size constraint
                                    .wrapContentHeight() // Use wrapContentHeight to avoid infinite constraints
                            ) {
                                items(moreLikeThis) {
                                    moreLikeThisItem2(
                                        it,
                                        sharedViewModelMovie.movieId,
                                        navHostController,
                                        sharedViewModelMovie
                                    )
                                }
                            }
                        }
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .padding(
                                start = Dimen.dimen.paddingMedium,
                                top = Dimen.dimen.paddingMedium
                            )
                            .height(Dimen.dimen.small3)
                            .width(Dimen.dimen.height1)
                            .clip(
                                RoundedCornerShape(4.dp)
                            )
                            .background(color = shimmerColor)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimen.dimen.boxHeight)
                            .padding(
                                top = Dimen.dimen.paddingSmall - 2.5.dp,
                                start = Dimen.dimen.padding4,
                                end = Dimen.dimen.padding4
                            )
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(1.dp),
                            modifier = Modifier
                                .fillMaxWidth() // Use width constraint instead of size constraint
                                .wrapContentHeight() // Use wrapContentHeight to avoid infinite constraints
                        ) {
                            items(9) {
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.height4)
                                        .padding(Dimen.dimen.small1 - 1.5.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(shimmerColor)
                                )
                            }
                        }
                    }
                }
            }
        }

        var isClickable by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .padding(
                    start = Dimen.dimen.paddingMedium,
                    top = Dimen.dimen.paddingMedium
                )
                .clip(RoundedCornerShape(10.dp))
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
                    .padding(start = Dimen.dimen.small3 , bottom = Dimen.dimen.small3 , end = Dimen.dimen.small3)
                    .size(Dimen.dimen.medium1)
            )
        }

    }
}



@Composable
fun moreLikeThisItem2(item: movie_home_row, OldId: String, navHostController: NavHostController , sharedViewModelMovie: SharedViewModelMovie) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(item.imageUrI).crossfade(200)
            .build()
    )
    val scale = remember { Animatable(1f) }

    if (painter.state is AsyncImagePainter.State.Loading) {
        Box(
            modifier = Modifier
                .height(Dimen.dimen.height4)
                .padding(Dimen.dimen.small1 - 1.5.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = shimmerColor)
        )
    }

    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(Dimen.dimen.height4)
            .padding(Dimen.dimen.small1 - 1.5.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        scale.animateTo(0.95f)
                        val success = tryAwaitRelease()
                        scale.animateTo(1f)
                        if (success) {
                            navHostController.navigate("movieDetail/${item.id}") {
                                popUpTo("movieDetail/${OldId}") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }
            .clip(RoundedCornerShape(8.dp))
    )
}


@Composable
fun additionalOptions() {
    LazyRow(modifier = Modifier.padding(top = Dimen.dimen.paddingLarge)) {
        item {
            Spacer(modifier = Modifier.width(Dimen.dimen.paddingMedium))
        }
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Invite",
                    modifier = Modifier.size(Dimen.dimen.button2),
                    colorFilter = ColorFilter.tint(
                        Color.LightGray
                    )
                )
                Text(
                    text = "Invite",
                    fontSize = Dimen.dimen.fontSizeSmall.sp,
                    color = Color.LightGray,
                    fontFamily = FontFamily(
                        Font(R.font.helvetica_neue)
                    )
                )
            }
        }
        item {
            Spacer(modifier = Modifier.width(Dimen.dimen.button2))
        }
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_file_download_24),
                    contentDescription = "download",
                    modifier = Modifier.size(Dimen.dimen.button2),
                    colorFilter = ColorFilter.tint(
                        Color.LightGray
                    )
                )
                Text(
                    text = "Download",
                    fontSize = Dimen.dimen.fontSizeSmall.sp,
                    color = Color.LightGray,
                    fontFamily = FontFamily(
                        Font(R.font.helvetica_neue)
                    ),

                    )
            }
        }
        item {
            Spacer(modifier = Modifier.width(Dimen.dimen.button2))
        }
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_playlist_add_24),
                    contentDescription = "watchlist",
                    modifier = Modifier.size(Dimen.dimen.button2),
                    colorFilter = ColorFilter.tint(
                        Color.LightGray
                    )
                )
                Text(
                    text = "Watchlist",
                    fontSize = Dimen.dimen.fontSizeSmall.sp,
                    color = Color.LightGray,
                    fontFamily = FontFamily(
                        Font(R.font.helvetica_neue)
                    )
                )
            }
        }
    }
}