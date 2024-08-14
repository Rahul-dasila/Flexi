package com.example.flexie.screens.BottomNavScreen

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.flexie.R
import com.example.flexie.ViewModels.d_homeScreen_ViewModel
import com.example.flexie.models.movie_home_row
import com.example.flexie.models.movie_view_pager
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.IdObject
import com.example.flexie.utils.px
import com.example.flexie.utils.setOrientation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun d_home_Screen(d_homeViewModel: d_homeScreen_ViewModel  , navHostController: NavHostController) {
    val movieViewPager = d_homeViewModel.movieViewPager.collectAsState().value
    val movieCategories = d_homeViewModel.movieCategories.collectAsState().value
    val movieRowData = d_homeViewModel.movieRow.collectAsState().value
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    var _key = remember { mutableStateOf(false) }
    val key = _key.value
    var _movieItem = remember { mutableStateOf(-1) }
    val movieItem = _movieItem.value
    val activity = LocalContext.current as Activity
    activity.setOrientation()


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
                            .padding(bottom = 15.dp)

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
            item{
                Column (modifier = Modifier.padding(top = 19.dp, bottom = 8.dp)){
                    Text(
                        text = "Continue watching for you",
                        color = Color.LightGray,
                        letterSpacing = 0.7.sp,
                        fontSize = (Dimen.dimen.fontSizeHeadLine-4).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                        modifier = Modifier.padding(start = 8.6.dp, bottom = 7.dp)
                    )
                    ContinueWatchingItem()
                }
            }

            if (movieCategories.isNotEmpty()) {
                movieCategories.forEachIndexed { index, category ->
                    item {
                        LaunchedEffect(key1 = Unit) {
                            d_homeViewModel.loadMovies(category.category)
                        }
                        if (!movieRowData[category.category].isNullOrEmpty()) {
                            Column(
                                modifier = Modifier.padding(
                                    top = Dimen.dimen.paddingLarge,
                                    bottom = Dimen.dimen.padding3
                                )
                            ) {
                                Text(
                                    text = category.category,
                                    color = Color.LightGray,
                                    fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                                    letterSpacing = 0.7.sp,
                                    fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.6.dp, bottom = 7.dp)
                                )
                                movieRowData[category.category]?.let { movies ->
                                    // Use the index to determine when to change the size
                                    if ((index + 1) % 4 == 0) {
                                        MovieRowItem(
                                            list = movies,
                                            height = Dimen.dimen.height3,
                                            width = Dimen.dimen.width3 , navHostController
                                        )
                                    } else {
                                        MovieRowItem(list = movies ,navHostController = navHostController)
                                    }
                                }
                            }
                        }
                    }
                }
            }


            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp))
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
fun ContinueWatchingItem(){
    val list = listOf<Int>(1,2,3)
    LazyRow(modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight()){
        items(list){
            Image(painter = painterResource(id = R.drawable.inter), contentDescription ="", modifier = Modifier
                .width(Dimen.dimen.width2)
                .height(Dimen.dimen.height2)
                .padding(start = 10.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(9.dp)),contentScale = ContentScale.Crop )
        }
    }
}

@Composable
fun MovieRowItem(list : List<movie_home_row>, height : Dp = Dimen.dimen.height1, width : Dp = Dimen.dimen.width1 , navHostController: NavHostController){
    LazyRow(modifier = Modifier
        .wrapContentWidth()
        .wrapContentWidth()){
     items(list){
         val scale = remember {
             Animatable(1f)
         }
         val myId = it.id
         val painter = rememberImagePainter(request = ImageRequest.Builder(LocalContext.current).data(it.imageUrI).crossfade(true).build())
         Image(painter = painter, contentDescription =" ", modifier = Modifier
             .width(width)
             .height(height)
             .padding(start = 10.dp)
             .graphicsLayer {
                 scaleX = scale.value
                 scaleY = scale.value
             }
             .pointerInput(Unit){
                 detectTapGestures(
                     onPress = {
                         scale.animateTo(0.95f)
                         tryAwaitRelease()
                         scale.animateTo(1f)
                     },
                     onTap = {
                         IdObject.id = myId
                         navHostController.navigate("movieDetail")
                     }
                 )
             }
             .clip(RoundedCornerShape(4.dp))

             ,contentScale = ContentScale.Crop)
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
                .height(Dimen.dimen.viewPager)
                .padding(top = Dimen.dimen.padding1)
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
                        Brush.verticalGradient(colors)
                    )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(colors, startY = 250f, endY = 0f)
                    )
            )

        }
        Row(modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = movie.language, color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) , fontSize = Dimen.dimen.fontSizeSmall2.sp )
            Spacer(modifier = Modifier.width(Dimen.dimen.small1+1.5.dp))
            Text(text = movie.type, color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) , fontSize = Dimen.dimen.fontSizeSmall2.sp )
            Spacer(modifier = Modifier.width(Dimen.dimen.small1+1.5.dp))
            Text(text = movie.rating.toString()+" 🔥", color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) , fontSize = Dimen.dimen.fontSizeSmall2.sp )
        }
    }
}

@Composable
fun watchButton(movieItem: Int) {
    Box(modifier = Modifier
        .padding(bottom = 30.dp)
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .background(Color.DarkGray)
        .clickable {

        }, contentAlignment = Alignment.Center
        ) {
        Row (modifier = Modifier
            .padding(
                top = Dimen.dimen.padding2,
                bottom = Dimen.dimen.padding2,
                start = 40.dp,
                end = 40.dp
            )
            .wrapContentHeight()
            .wrapContentWidth(), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
            Image(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = "play" , modifier = Modifier.size(Dimen.dimen.button))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Watch Now", color = Color.LightGray, fontFamily = FontFamily(Font(R.font.helvetica_neue)) , fontSize = Dimen.dimen.fontSizeSmall2.sp)
            Spacer(modifier = Modifier.width(11.5.dp))
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
                    fontSize = Dimen.dimen.fontSizeSmall3.sp,
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