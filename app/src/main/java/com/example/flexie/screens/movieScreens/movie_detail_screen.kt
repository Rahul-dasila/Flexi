package com.example.flexie.screens.movieScreens

import android.app.Activity
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.flexie.R
import com.example.flexie.ViewModels.Movie_detail_viewmodel
import com.example.flexie.ViewModels.PlayerViewModel
import com.example.flexie.ViewModels.SharedViewModelMovie
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
fun movieDetailScreen(
    navHostController: NavHostController,
    id: String,
    playerViewModel: PlayerViewModel,
    sharedViewModelMovie: SharedViewModelMovie
) {

    LaunchedEffect(key1 = Unit) {
        playerViewModel.uri = ""
        playerViewModel.playbackPosition = 0L
        playerViewModel.playWhenReady = true
        playerViewModel.isFullScreen = false
    }
    val ViewModel: Movie_detail_viewmodel = hiltViewModel()
    val context = LocalContext.current
    setSystemBarColor(statusBarColor = darkBlue)
    val activity = LocalContext.current as Activity
    activity.setOrientation()

    LaunchedEffect(key1 = id) {
        if (id.isNotEmpty()) {
            ViewModel.movieId = id
            sharedViewModelMovie.movieId = id
            ViewModel.loadMovieData(id)
        }
    }

    var category1 = ViewModel._category.collectAsState().value
    LaunchedEffect(key1 = category1) {
        if (category1.isNotEmpty()) {
            sharedViewModelMovie._category.value = category1
            ViewModel.loadMoreMovies()
        }
    }
    val moreLikeThis = ViewModel._moreLikeThis.collectAsState().value
    val pageData = ViewModel._pageData.collectAsState().value


    var painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data("").build()
    )
    if (pageData != null && !ViewModel.loading1) {
        painter =
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(pageData.realPosterUrl)
                    .crossfade(200).build()
            )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBlue)
        ) {
            if (ViewModel.loading1 || pageData == null) {
                item {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(modifier = Modifier.wrapContentSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimen.dimen.moviePostor)
                                    .background(shimmerColor)
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent, darkBlue
                                            )
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent, darkBlue
                                            ), startY = 150f, endY = 0f
                                        )
                                    )
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(start = Dimen.dimen.paddingMedium)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.button2)
                                        .width(Dimen.dimen.height1)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            shimmerColor
                                        )
                                )
                                Spacer(modifier = Modifier.height(Dimen.dimen.padding2))
                                Row {
                                    val list = listOf(
                                        1, 2, 3, 4
                                    )
                                    list.forEach {
                                        Box(
                                            modifier = Modifier
                                                .height(Dimen.dimen.small1)
                                                .width(Dimen.dimen.medium2)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    shimmerColor
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(Dimen.dimen.padding2))
                                    }
                                }
                                Spacer(modifier = Modifier.height(Dimen.dimen.paddingSmall))
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.medium3)
                                        .width(Dimen.dimen.height2)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            shimmerColor
                                        )
                                )
                                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.small2)
                                        .padding(top = Dimen.dimen.extraSmall)
                                        .fillMaxWidth(.95f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            shimmerColor
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.small2)
                                        .padding(top = Dimen.dimen.extraSmall)
                                        .fillMaxWidth(.95f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            shimmerColor
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .height(Dimen.dimen.small2)
                                        .padding(top = Dimen.dimen.extraSmall)
                                        .fillMaxWidth(.95f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            shimmerColor
                                        )
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(modifier = Modifier.wrapContentSize()) {
                            if (painter.state is AsyncImagePainter.State.Loading) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(Dimen.dimen.moviePostor)
                                            .clip(
                                                RoundedCornerShape(15.dp)
                                            )
                                            .background(
                                                shimmerColor
                                            )
                                    )
                                }
                            }
                            Image(
                                painter = painter,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimen.dimen.moviePostor),
                                contentScale = ContentScale.Crop
                            )


                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent, darkBlue
                                            )
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent, darkBlue
                                            ), startY = 150f, endY = 0f
                                        )
                                    )
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(start = Dimen.dimen.paddingMedium)
                            ) {
                                Text(
                                    text = pageData.name.trim(),
                                    fontSize = Dimen.dimen.fontSizeHeadLine.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.helvetica_neue)
                                    ),
                                    color = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(Dimen.dimen.padding2))
                                Row {
                                    val category = pageData.category[0].toString()
                                    ViewModel._category.value = category
                                    val list = listOf(
                                        category,
                                        pageData.year,
                                        pageData.rating,
                                        pageData.uA
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
                                sharedViewModelMovie.movieData = pageData
                                Spacer(modifier = Modifier.height(Dimen.dimen.paddingSmall))
                                playButton(ViewModel, navHostController)
                                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
                            }
                        }
                    }
                    Text(
                        text = pageData.description,
                        fontSize = Dimen.dimen.fontSizeSmall4.sp,
                        color = Color.LightGray,
                        fontFamily = FontFamily(
                            Font(R.font.helvetica_neue)
                        ),
                        textAlign = TextAlign.Unspecified,
                        modifier = Modifier.padding(
                            start = Dimen.dimen.paddingMedium, end = Dimen.dimen.paddingSmall
                        )
                    )

                }
            }


            if (ViewModel.loading1 || pageData == null) {
                item {
                    Row(modifier = Modifier.padding(top = Dimen.dimen.paddingLarge)) {
                        Spacer(modifier = Modifier.width(Dimen.dimen.paddingMedium))
                        Box(
                            modifier = Modifier
                                .size(Dimen.dimen.button2)
                                .clip(RoundedCornerShape(4.dp))
                                .background(shimmerColor)

                        )
                        Spacer(modifier = Modifier.width(Dimen.dimen.button2))
                        Box(
                            modifier = Modifier
                                .size(Dimen.dimen.button2)
                                .clip(RoundedCornerShape(4.dp))
                                .background(shimmerColor)

                        )
                        Spacer(modifier = Modifier.width(Dimen.dimen.button2))
                        Box(
                            modifier = Modifier
                                .size(Dimen.dimen.button2)
                                .clip(RoundedCornerShape(4.dp))
                                .background(shimmerColor)

                        )

                    }
                }
            } else {
                item {
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
            }


            item {
                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
            }


            item {
                if (ViewModel.loading2) {
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
                            items(5) {
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
                } else if (moreLikeThis.isNotEmpty()) {
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
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(1.dp),
                            modifier = Modifier
                                .fillMaxWidth() // Use width constraint instead of size constraint
                                .wrapContentHeight() // Use wrapContentHeight to avoid infinite constraints
                        ) {
                            items(moreLikeThis) {
                                moreLikeThisItem(it.imageUrI, it.id, navHostController, id)
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
                    .padding(Dimen.dimen.small3)
                    .size(Dimen.dimen.medium1)
            )
        }

    }
}

@Composable
fun moreLikeThisItem(url: String, id: String, navHostController: NavHostController, oldId: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(200).build()
    )
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }

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
                            navHostController.navigate("movieDetail/${id}") {
                                popUpTo("movieDetail/${oldId}") {
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
fun playButton(viewModel: Movie_detail_viewmodel, navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {
                navHostController.navigate("PlayerScreenSolo/${viewModel.movieId}")
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = Dimen.dimen.padding2,
                    bottom = Dimen.dimen.padding2,
                    start = 40.dp,
                    end = 40.dp
                )
                .wrapContentHeight()
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "play",
                modifier = Modifier.size(Dimen.dimen.button),
                colorFilter = ColorFilter.tint(
                    darkBlue
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Watch Now",
                color = darkBlue,
                fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                fontSize = Dimen.dimen.fontSizeSmall2.sp
            )
            Spacer(modifier = Modifier.width(11.5.dp))
        }
    }
}