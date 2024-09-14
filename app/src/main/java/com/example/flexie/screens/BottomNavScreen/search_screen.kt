package com.example.flexie.screens.BottomNavScreen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.flexie.R
import com.example.flexie.ViewModels.SearchViewModel
import com.example.flexie.ui.theme.bottomNavColor
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.searchColor
import com.example.flexie.ui.theme.shimmerColor
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.setOrientation
import com.example.flexie.utils.setSystemBarColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun Search_screen(searchViewModel: SearchViewModel, navHostController: NavHostController) {
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = bottomNavColor)
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    activity.setOrientation()
    val recommendedList = searchViewModel._recommendedList.collectAsState().value
    LaunchedEffect(key1 = Unit) {
        searchViewModel.searchResult.value = emptyList()
        searchViewModel.query = ""

    }
    var searchResult = searchViewModel._searchResult.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(darkBlue),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            searchBar(searchViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            if (searchViewModel.query.trim() == "") {
                if (searchViewModel.loading) {
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp, bottom = 5.dp)
                            .height(Dimen.dimen.small3)
                            .width(Dimen.dimen.height1)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                shimmerColor
                            )
                    )
                    whenloading()
                }
                if (!searchViewModel.loading) {
                    Text(
                        text = "Recommended for you", color = Color.LightGray,
                        fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                        letterSpacing = 0.7.sp,
                        fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 11.dp, bottom = 5.dp)
                    )
                }
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    itemsIndexed(recommendedList) { index, item ->

                        var scale = remember {
                            androidx.compose.animation.core.Animatable(1f)
                        }
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imageUrI).placeholder(R.drawable.shimmer)
                                .build()
                        )
                        var aspectRatio = 0f
                        if (index == 0 || (index + 1) % 5 == 0) {
                            aspectRatio = 0.55f
                        } else {
                            aspectRatio = 0.75f
                        }
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(Dimen.dimen.extraSmall2)
                                .clip(RoundedCornerShape(Dimen.dimen.small1))
                                .aspectRatio(aspectRatio)
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
                                                navHostController.navigate("movieDetail/${item.id}")
                                            }
                                        }
                                    )
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(65.dp))
                    }
                }
            } else {
                if (!searchViewModel.loading2) {
                    if (searchResult.isNotEmpty()) {
                        Text(
                            text = "Top searches for \"${searchViewModel.query}\"",
                            color = Color.LightGray,
                            fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                            letterSpacing = 0.7.sp,
                            fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 11.dp, bottom = 5.dp)
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            ),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(searchResult) { item ->
                                val scale = remember {
                                    androidx.compose.animation.core.Animatable(1f)
                                }
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(context).data(item.imageUrI)
                                        .placeholder(R.drawable.shimmer).build()
                                )
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(Dimen.dimen.extraSmall2)
                                        .clip(RoundedCornerShape(Dimen.dimen.small1))
                                        .aspectRatio(.75f)
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
                                                        navHostController.navigate("movieDetail/${item.id}")
                                                    }
                                                }
                                            )
                                        }
                                )
                            }
                        }
                    }
                    if (searchResult.isEmpty() && !searchViewModel.searching) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = Dimen.dimen.searchBarSize * 5),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth() ,
                                contentAlignment = Alignment.Center// Make the Box take up the full width of the Column
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.image),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(Dimen.dimen.height4)
                                )
                            }
                            Text(
                                text = "Couldn't find \"${searchViewModel.query}\"",
                                color = Color.LightGray,
                                fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                                letterSpacing = 0.7.sp,
                                fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Try searching for something else or try with a \n different spelling",
                                color = searchColor,
                                fontSize = (Dimen.dimen.fontSizeSmall2).sp,
                                letterSpacing = 0.7.sp,
                                fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                if(searchViewModel.loading2){
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp, bottom = 5.dp)
                            .height(Dimen.dimen.small3)
                            .width(Dimen.dimen.height1)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                shimmerColor
                            )
                    )
                    val list = listOf(1,2,3,4,5)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(list) { item ->
                            Image(
                                painter = painterResource(id = R.drawable.shimmer),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(Dimen.dimen.extraSmall2)
                                    .clip(RoundedCornerShape(Dimen.dimen.small1))
                                    .aspectRatio(.75f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun searchBar(searchViewModel: SearchViewModel) {
    val debouncePeriod = 500L
    val isKeyboardVisible by mutableStateOf(WindowInsets.isImeVisible)

    LaunchedEffect(key1 = searchViewModel.query) {
        delay(debouncePeriod)
        if (searchViewModel.query.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                searchViewModel.getSearchResult(searchViewModel.query.trim())
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
            .height(Dimen.dimen.searchBarSize)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "search icon",
                tint = darkBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (searchViewModel.query.isEmpty()) {
                    Text(
                        text = "Search here...",
                        color = Color.Gray,
                        fontSize = Dimen.dimen.fontSizeSmall2.sp
                    )
                }
                BasicTextField(
                    value = searchViewModel.query,
                    onValueChange = { newQuery ->
                        searchViewModel.searching = true
                        searchViewModel.searchResult.value = emptyList()
                        searchViewModel.query = newQuery
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color.DarkGray,
                        fontSize = Dimen.dimen.fontSizeSmall2.sp,
                        textAlign = TextAlign.Start
                    ),
                    singleLine = true,
                    cursorBrush = if (isKeyboardVisible) SolidColor(darkBlue) else SolidColor(Color.Transparent)
                )
            }
            if (searchViewModel.query.isNotEmpty()) {
                IconButton(onClick = { searchViewModel.query = "" }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                        tint = darkBlue
                    )
                }
            }
        }
    }
}

@Composable
fun whenloading() {
    val list = listOf(1, 2, 3, 5, 6, 7, 8, 9, 12, 2, 3, 4, 5)
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(list) { index, item ->
            var aspectRatio = 0f
            if (index == 0 || (index + 1) % 5 == 0) {
                aspectRatio = 0.55f
            } else {
                aspectRatio = 0.75f
            }
            Image(
                painter = painterResource(id = R.drawable.shimmer),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(Dimen.dimen.extraSmall2)
                    .clip(RoundedCornerShape(Dimen.dimen.small1))
                    .aspectRatio(aspectRatio)
            )
        }
    }
}
