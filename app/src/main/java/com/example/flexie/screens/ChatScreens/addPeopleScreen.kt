package com.example.flexie.screens.ChatScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.example.flexie.R
import com.example.flexie.ViewModels.SearchPeopleViewModel
import com.example.flexie.models.friend
import com.example.flexie.models.friendRequest
import com.example.flexie.models.searchPeopleItem
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.searchColor
import com.example.flexie.ui.theme.shimmerColor
import com.example.flexie.utils.Dimen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun addPeopleScreen(searchPeopleViewModel: SearchPeopleViewModel , navHostController: NavHostController) {
    var result = searchPeopleViewModel.resultsSearch.collectAsState().value
    LaunchedEffect(key1 = Unit) {
        searchPeopleViewModel.query = ""
        searchPeopleViewModel.loading = false
        searchPeopleViewModel.loading1 = false
        searchPeopleViewModel.loadin2 = false
        searchPeopleViewModel.loadin3 = false
        searchPeopleViewModel.loadin4 = false
        searchPeopleViewModel.states.value = mutableMapOf()
        searchPeopleViewModel.resultsSearch.value = emptyList()
        searchPeopleViewModel.senderList.value = mutableMapOf()
        searchPeopleViewModel.receiverList.value = mutableMapOf()
        searchPeopleViewModel.friendItem.value = friend(friendList = emptyList())
    }

    if (searchPeopleViewModel.loading1 || searchPeopleViewModel.loadin2 || searchPeopleViewModel.loadin3 || searchPeopleViewModel.loadin4) {
        searchPeopleViewModel.loading = true
    }
    if (!searchPeopleViewModel.loading1 && !searchPeopleViewModel.loadin2 && !searchPeopleViewModel.loadin3 && !searchPeopleViewModel.loadin4) {
        searchPeopleViewModel.loading = false
    }

    LaunchedEffect(key1 = Unit) {
        if (searchPeopleViewModel.name.isEmpty()) {
            searchPeopleViewModel.getCurrentOne()
        }
    }
    LaunchedEffect(key1 = searchPeopleViewModel.query) {
        delay(500L)
        if (searchPeopleViewModel.query.isNotEmpty()) {
            searchPeopleViewModel.getSearchResult()
            searchPeopleViewModel.getFriend()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            addSearchBar(searchPeopleViewModel , navHostController)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                searchlist(result, searchPeopleViewModel)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun addSearchBar(searchPeopleViewModel: SearchPeopleViewModel , navHostController: NavHostController) {
    val context = LocalContext.current
    val isKeyboardVisible by mutableStateOf(WindowInsets.isImeVisible)
    Row(verticalAlignment = Alignment.CenterVertically) {
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
                        navHostController.popBackStack()
                        isClickable = false
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000)
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
                    .padding(Dimen.dimen.small1)
                    .size(Dimen.dimen.medium1)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Dimen.dimen.paddingMedium,
                    end = Dimen.dimen.paddingMedium,
                    start = Dimen.dimen.small1
                )
                .height(Dimen.dimen.searchBarSize)
                .clip(RoundedCornerShape(percent = 50))
                .background(Color.DarkGray)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(percent = 50)),
            contentAlignment = Alignment.Center

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(Dimen.dimen.paddingSmall + Dimen.dimen.extraSmall))
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search icon",
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchPeopleViewModel.query.isEmpty()) {
                        Text(
                            text = "Search here...",
                            color = Color.Gray,
                            fontSize = Dimen.dimen.fontSizeSmall2.sp
                        )
                    }
                    BasicTextField(
                        value = searchPeopleViewModel.query,
                        onValueChange = { newQuery ->
                            if (newQuery.isNotEmpty()) {
                                searchPeopleViewModel.loading1 = true
                            }
                            searchPeopleViewModel.query = newQuery
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = Color.LightGray,
                            fontSize = Dimen.dimen.fontSizeSmall2.sp,
                            textAlign = TextAlign.Start
                        ),
                        singleLine = true,
                        cursorBrush = if (isKeyboardVisible) SolidColor(Color.LightGray) else SolidColor(
                            Color.Transparent
                        )
                    )
                }
                if (searchPeopleViewModel.query.isNotEmpty()) {
                    IconButton(onClick = {
                        searchPeopleViewModel.query = ""
                        searchPeopleViewModel.resultsSearch.value = emptyList()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun searchlist(result: List<searchPeopleItem>, searchPeopleViewModel: SearchPeopleViewModel) {
    val context = LocalContext.current
    if (!searchPeopleViewModel.loading && result.isNotEmpty() && searchPeopleViewModel.query.isNotEmpty()) {
        val senderlst = searchPeopleViewModel.senderList.collectAsState().value
        val receiverlst = searchPeopleViewModel._receiverList.collectAsState().value
        val friendslst = searchPeopleViewModel._friendItem.collectAsState().value.friendList
        val states: MutableMap<String, String> = searchPeopleViewModel.states.collectAsState().value
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(
                start = Dimen.dimen.paddingSmall,
                top = Dimen.dimen.paddingSmall
            )
        ) {
            Text(
                text = "Top searches for \"${searchPeopleViewModel.query}\"",
                color = Color.LightGray,
                fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                letterSpacing = 0.7.sp,
                fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = Dimen.dimen.padding4,
                    top = Dimen.dimen.paddingMedium,
                    bottom = Dimen.dimen.padding4
                )
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dimen.dimen.paddingSmall, end = Dimen.dimen.paddingSmall)
            ) {
                items(result) {

                    var isClickable by remember { mutableStateOf(true) }
                    val coroutineScope = rememberCoroutineScope()
                    var isClickable2 by remember { mutableStateOf(true) }
                    val coroutineScope2 = rememberCoroutineScope()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sitaramam),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(Dimen.dimen.large + Dimen.dimen.small1)
                                    .padding(Dimen.dimen.padding4)
                                    .clip(
                                        RoundedCornerShape(percent = 50)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = it.name,
                                    color = Color.White,
                                    fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(Dimen.dimen.extraSmall))
                                Text(
                                    text = "Jindagi na milegi dobara",
                                    color = Color.LightGray,
                                    fontSize = Dimen.dimen.fontSizeSmall4.sp,
                                    fontWeight = FontWeight.Medium
                                )

                            }
                            val k: List<friendRequest> =
                                senderlst[it.oneSignalPlayerID] ?: emptyList()
                            val l = receiverlst[it.oneSignalPlayerID] ?: emptyList()

                            if (k.isEmpty() && l.isEmpty() && !friendslst.contains(
                                    it.oneSignalPlayerID
                                ) && states[it.oneSignalPlayerID] != "sent" && states[it.oneSignalPlayerID] != "friends"
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.White,
                                            shape = RoundedCornerShape(percent = 25)
                                        )
                                        .clickable(enabled = isClickable) {
                                            if (isClickable) {
                                                // Perform the click action
                                                searchPeopleViewModel.sendRequest(it.oneSignalPlayerID)
                                                states[it.oneSignalPlayerID] = "sent"
                                                // Disable further clicks temporarily
                                                isClickable = false
                                                // Re-enable click after a delay using coroutine scope
                                                coroutineScope.launch {
                                                    delay(2000) // Adjust the delay time as needed (e.g., 2000 ms = 2 seconds)
                                                    isClickable = true
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        text = "Request",
                                        color = darkBlue,
                                        fontSize = Dimen.dimen.fontSizeSmall2.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(Dimen.dimen.paddingSmall)
                                    )
                                }
                            } else if ((k.isNotEmpty() && l.isEmpty() && !friendslst.contains(it.oneSignalPlayerID)) || (states[it.oneSignalPlayerID] == "sent")) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.DarkGray,
                                            shape = RoundedCornerShape(percent = 25)
                                        )
                                ) {
                                    Text(
                                        text = "Sent",
                                        color = Color.LightGray,
                                        fontSize = Dimen.dimen.fontSizeSmall2.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(Dimen.dimen.paddingSmall)
                                    )
                                }
                            } else if (k.isEmpty() && l.isNotEmpty() && !friendslst.contains(it.oneSignalPlayerID) && states[it.oneSignalPlayerID] != "friends" && states[it.oneSignalPlayerID] != "sent") {
                                var check by mutableStateOf(states[it.oneSignalPlayerID])
                                if (check != "friends") {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color.White,
                                                shape = RoundedCornerShape(percent = 25)
                                            )
                                            .clickable(enabled = isClickable2) {
                                                if (isClickable2) {
                                                    searchPeopleViewModel.addFriend(it.oneSignalPlayerID)
                                                    states[it.oneSignalPlayerID] = "friends"
                                                    check = "friends"
                                                    isClickable = false
                                                    coroutineScope2.launch {
                                                        delay(2000) // Adjust the delay time as needed (e.g., 2000 ms = 2 seconds)
                                                        isClickable = true
                                                    }
                                                }
                                            }
                                    )
                                    {
                                        Text(
                                            text = "Accept",
                                            color = darkBlue,
                                            fontSize = Dimen.dimen.fontSizeSmall2.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(Dimen.dimen.paddingSmall)
                                        )
                                    }
                                }
                            }
                            if ((k.isEmpty() && l.isEmpty() && friendslst.contains(it.oneSignalPlayerID)) || (states[it.oneSignalPlayerID] == "friends")) {
                                Box(
                                    modifier = Modifier.size(Dimen.dimen.searchBarSize-2.dp)
                                        .background(
                                            darkBlue,
                                            shape = RoundedCornerShape(percent = 50)
                                        )
                                        .clickable {
                                            states[it.oneSignalPlayerID] = ""
                                        },
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Image(painter = painterResource(id = R.drawable.send4), contentDescription = "", modifier = Modifier.matchParentSize())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (searchPeopleViewModel.loading && searchPeopleViewModel.query.isNotEmpty()) {
        whileLoading()
    }

    if (!searchPeopleViewModel.loading && result.isEmpty() && searchPeopleViewModel.query.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = Dimen.dimen.searchBarSize * 5),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
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
                text = "Couldn't find \"${searchPeopleViewModel.query}\"",
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
                text = "Try searching for someone else or try with a \n different spelling",
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

@Composable
fun whileLoading() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(
            start = Dimen.dimen.paddingSmall,
            top = Dimen.dimen.paddingSmall
        )
    ) {
        Text(
            text = "Top searches for the current query",
            color = shimmerColor,
            fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
            letterSpacing = 0.7.sp,
            fontFamily = FontFamily(Font(R.font.helvetica_neue)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    start = Dimen.dimen.padding4,
                    top = Dimen.dimen.paddingMedium,
                    bottom = Dimen.dimen.padding4
                )
                .background(shape = RoundedCornerShape(percent = 25), color = shimmerColor)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dimen.dimen.paddingSmall, end = Dimen.dimen.paddingSmall)
        ) {
            items(listOf(1, 2, 3, 4)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(), contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shimmer),
                            contentDescription = "",
                            modifier = Modifier
                                .size(Dimen.dimen.large + Dimen.dimen.small1)
                                .padding(Dimen.dimen.padding4)
                                .clip(
                                    RoundedCornerShape(percent = 50)
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "loading data",
                                color = shimmerColor,
                                fontSize = (Dimen.dimen.fontSizeHeadLine - 4).sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.background(
                                    shape = RoundedCornerShape(percent = 25),
                                    color = shimmerColor
                                )
                            )
                            Spacer(modifier = Modifier.height(Dimen.dimen.extraSmall))
                            Text(
                                text = "Jindagi na milegi dobara",
                                color = shimmerColor,
                                fontSize = Dimen.dimen.fontSizeSmall4.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.background(
                                    shape = RoundedCornerShape(percent = 25),
                                    color = shimmerColor
                                )
                            )

                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    shimmerColor,
                                    shape = RoundedCornerShape(percent = 25)
                                )
                                .clickable {

                                }
                        ) {
                            Text(
                                text = "Request",
                                color = shimmerColor,
                                fontSize = Dimen.dimen.fontSizeSmall2.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(Dimen.dimen.paddingSmall)
                            )
                        }
                    }

                }
            }
        }
    }
}


