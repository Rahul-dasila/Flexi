package com.example.flexie.screens.BottomNavScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.flexie.R
import com.example.flexie.ViewModels.d_homeScreen_ViewModel
import com.example.flexie.models.movie_view_pager
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.px
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun d_home_Screen(d_homeViewModel: d_homeScreen_ViewModel) {
    val movieViewPager = d_homeViewModel.movieViewPager.collectAsState().value
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    var _key = remember { mutableStateOf(false) }
    val key = _key.value
    var _movieItem = remember { mutableStateOf(-1) }
    val movieItem = _movieItem.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (movieViewPager.isNotEmpty()) {
                _key.value = true
                item {
                    HorizontalPager(
                        count = movieViewPager.size,
                        state = pagerState,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()

                    ) {
                        movieViewPagerItem(movie = movieViewPager[it])
                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
                            watchButton(movieItem)
                    }
                }
            }
        }
        CustomFloatingActionButton()
    }
    if (key) {
        LaunchedEffect(key1 = pagerState.currentPage) {
            delay(6000)
            coroutineScope.launch {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % movieViewPager.size)
            }
        }
    }
}


@Composable
fun movieViewPagerItem(movie: movie_view_pager) {
    val painter = rememberImagePainter(
        request = ImageRequest.Builder(LocalContext.current).data(movie.imageUrl).crossfade(true)
            .build()
    )
    val colors = listOf(
        Color.Transparent ,
        darkBlue
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
                .padding(top = 20.dp)
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.px
            val screenWidth = LocalConfiguration.current.screenWidthDp.px
            Image(
                painter = painter,
                contentDescription = movie.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable { },
                contentScale = ContentScale.Crop

            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(colors, startY = 400f, endY = 800f)
                    )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(colors, startY = 600f, endY = 0f)
                    )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(colors, startX = 600f, endX = screenWidth / 1f)
                    )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(colors, startX = 400f, endX = 0f)
                    )
            )
        }
        Row(modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 20.dp), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = movie.language, color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) )
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = movie.type, color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) )
            Spacer(modifier = Modifier.width(13.dp))
            Text(text = movie.rating.toString()+" 🔥", color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) )
        }
    }
}

@Composable
fun watchButton(movieItem: Int) {
    Box(modifier = Modifier
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .background(Color.DarkGray)
        .clickable {
            Log.d("rahul", movieItem.toString())
        }
        ) {
        Row (modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 40.dp, end = 40.dp), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
            Image(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = "play" , modifier = Modifier.padding(end = 3.dp))
            Text(text = "Watch Now", color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)))
        }
        }
}

@Preview(showSystemUi = true)
@Composable
fun CustomFloatingActionButton() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        homeFloatingActionButton()
    }
}


@Preview(showSystemUi = true)
@Composable
fun homeFloatingActionButton() {
    Box(
        modifier = Modifier
            .padding(bottom = 70.dp) // Adjust the padding to place the FAB properly
            .height(45.dp)
            .fillMaxWidth(.65f)
            .clip(shape = RoundedCornerShape(25.dp))
            .background(Color.DarkGray), // Adjust the height to make the FAB visible
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val list = listOf("Movies", "Series", "Anime", "Shows")
            list.forEach {
                Text(
                    text = it,
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.5.dp, end = 6.5.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.helvetica_neue))
                )
                if (it != "Shows") {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight(.45f)
                            .width(.5.dp)
                            .background(Color.LightGray)
                    )
                }
            }
        }
    }
}