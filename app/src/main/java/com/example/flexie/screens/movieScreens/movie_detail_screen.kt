package com.example.flexie.screens.movieScreens

import android.app.Activity
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.flexie.R
import com.example.flexie.ViewModels.Movie_detail_viewmodel
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.IdObject
import com.example.flexie.utils.setOrientation
import com.example.flexie.utils.setSystemBarColor


@Composable
fun movieDetailScreen(navHostController: NavHostController, ViewModel: Movie_detail_viewmodel) {
    setSystemBarColor(statusBarColor = darkBlue)
    val activity = LocalContext.current as Activity
    val _pageData = ViewModel._pageData.collectAsState().value
    val _moreLikeThis = ViewModel._moreLikeThis.collectAsState().value
    ViewModel.loadMovieData()
    activity.setOrientation()
    var painter =
        rememberImagePainter(data = ViewModel.posterurl)
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBlue)
        ) {

            if (_pageData != null) {
                ViewModel.posterurl = _pageData.realPosterUrl
                item {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(modifier = Modifier.wrapContentSize()) {
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
                                    text = _pageData.name.trim(),
                                    fontSize = Dimen.dimen.fontSizeHeadLine.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.helvetica_neue)
                                    ),
                                    color = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(Dimen.dimen.padding2))
                                Row {
                                    val category = _pageData.category[0].toString()
                                    val list = listOf(
                                        category,
                                        _pageData.year,
                                        _pageData.rating,
                                        _pageData.uA
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
                                playButton()
                                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
                            }
                        }
                    }
                    Text(
                        text = _pageData.description,
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
            } else {
                Log.d("rahul", "null")
            }

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
            item {
                Spacer(modifier = Modifier.height(Dimen.dimen.paddingMedium))
            }
            if (_pageData != null) {
                ViewModel.loadMoreMovies(_pageData.category[0])
                if (_moreLikeThis.isNotEmpty()) {
                    item {
                        val gridItems = _moreLikeThis
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
                                items(gridItems) { item ->
                                    moreLikeThisItem(item.imageUrI ,item.id, ViewModel )
                                }
                            }
                        }
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
            contentDescription = "backBtn",
            modifier = Modifier
                .padding(start = Dimen.dimen.paddingMedium, top = Dimen.dimen.paddingMedium)
                .clickable {
                    navHostController.popBackStack()
                }
                .size(Dimen.dimen.medium1)
        )
    }
}

@Composable
fun moreLikeThisItem(uri: String ,id : String , ViewModel : Movie_detail_viewmodel ) {
    val painter = rememberImagePainter(data = uri)
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }

    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(Dimen.dimen.height4)
            .padding(Dimen.dimen.small1 - 1.5.dp)
            .graphicsLayer { scaleX = scale.value
                scaleY = scale.value
            }
            .pointerInput(Unit){
                detectTapGestures (
                    onPress = {
                        scale.animateTo(0.95f)
                        tryAwaitRelease()
                        scale.animateTo(1f)
                    },
                    onTap = {
                        IdObject.id = id
                        ViewModel.loadMovieData()
                    }

                )
            }
            .clip(RoundedCornerShape(8.dp))

    )
}

@Composable
fun playButton() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {

            }, contentAlignment = Alignment.Center
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