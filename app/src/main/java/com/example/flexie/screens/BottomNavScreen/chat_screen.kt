package com.example.flexie.screens.BottomNavScreen

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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
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
import com.example.flexie.ViewModels.ChatScreenViewModel
import com.example.flexie.ui.theme.bottomNavColor
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.shimmerColor
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.setSystemBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun chat_screen(navHostController: NavHostController , chatScreenViewModel: ChatScreenViewModel) {
    setSystemBarColor(statusBarColor = darkBlue , navigationBarColor = bottomNavColor)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(darkBlue)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = Dimen.dimen.paddingMedium,
                        start = Dimen.dimen.paddingLarge,
                        end = Dimen.dimen.paddingLarge,
                        bottom = Dimen.dimen.paddingMedium
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chats", color = Color.LightGray,
                    fontSize = (Dimen.dimen.fontSizeLarge).sp,
                    letterSpacing = 0.7.sp,
                    fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(contentAlignment = Alignment.Center , modifier = Modifier.clickable {
                  navHostController.navigate("addPeople")
                }) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "",
                        tint = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.width(Dimen.dimen.button / 2))
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "",
                        tint = Color.LightGray
                    )
                }
            }
            chatItem(chatScreenViewModel , navHostController)
        }
    }
}

@Composable
fun chatItem(chatScreenViewModel: ChatScreenViewModel , navHostController: NavHostController) {
    var friendsList = chatScreenViewModel._friendsList.collectAsState().value
   LazyColumn{
       item{
           chatSearchBar()
           Spacer(modifier = Modifier.height(Dimen.dimen.medium1))
       }
       if(friendsList.isNotEmpty()) {
           itemsIndexed(friendsList) { index, it ->
               var isClickable by remember { mutableStateOf(true) }
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(Dimen.dimen.large)
                       .padding(horizontal = Dimen.dimen.paddingSmall + Dimen.dimen.extraSmall)
                       .clickable {
                           if (isClickable) {
                               navHostController.navigate("actualChat/${it.id}/${it.name}")
                               isClickable = false
                               CoroutineScope(Dispatchers.Main).launch {
                                   delay(500)
                                   isClickable = true
                               }
                           }
                       }
               ) {
                   Row {
                       Image(
                           painter = painterResource(id = if (index % 2 == 0) R.drawable.sitaramam else R.drawable.inter),
                           contentDescription = "",
                           modifier = Modifier
                               .aspectRatio(1f)
                               .padding(Dimen.dimen.padding4)
                               .clip(shape = RoundedCornerShape(percent = 50))
                               .background(
                                   color = shimmerColor,
                                   shape = RoundedCornerShape(percent = 50)
                               ),
                           contentScale = ContentScale.Crop
                       )
                       Spacer(modifier = Modifier.width(Dimen.dimen.small1))
                       Column(
                           modifier = Modifier
                               .weight(1f)
                               .fillMaxHeight(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.Start
                       ) {
                           Text(
                               text = it.name,
                               color = Color.White,
                               fontSize = Dimen.dimen.fontSizeMedium.sp,
                               fontWeight = FontWeight.Medium
                           )
                           Spacer(modifier = Modifier.height(Dimen.dimen.extraSmall))
                           Text(
                               text = "Hey wassup",
                               color = Color.Gray,
                               fontSize = Dimen.dimen.fontSizeSmall2.sp,
                               maxLines = 1
                           )
                       }
                   }
               }
               Spacer(modifier = Modifier.height(Dimen.dimen.small1))

           }
       }
       if(friendsList.isEmpty()){
           item {
               Box(modifier = Modifier
                   .fillMaxWidth()
                   .fillParentMaxHeight(.9f), contentAlignment = Alignment.Center){
                   Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
                       Image(painter = painterResource(id = R.drawable.message2), contentDescription = "" , modifier = Modifier.size(Dimen.dimen.width1))
                       Text(
                           text = "Start a new conversation!", color = Color.LightGray,
                           fontSize = (Dimen.dimen.fontSizeHeadLine).sp,
                           letterSpacing = 0.7.sp,
                           fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                           fontWeight = FontWeight.Bold
                       )
                       Text(
                           text = "Tap on the first button on the top right corner\nto add search and connect with people. You never know what interesting conversation might come up!", color = Color.DarkGray,
                           fontSize = (Dimen.dimen.fontSizeSmall2).sp,
                           letterSpacing = 0.7.sp,
                           fontFamily = FontFamily(Font(R.font.helvetica_neue)),
                           fontWeight = FontWeight.Bold,
                           textAlign = TextAlign.Center,
                           modifier = Modifier.padding(top = Dimen.dimen.paddingSmall)
                       )
                       Spacer(modifier = Modifier.height(Dimen.dimen.large))
                   }
               }
           }
       }
       if(friendsList.isNotEmpty()) {
           item {
               Spacer(modifier = Modifier.height(Dimen.dimen.medium4 * 2))
           }
       }
   }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun chatSearchBar() {
    val isKeyboardVisible by mutableStateOf(WindowInsets.isImeVisible)
    var query by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimen.dimen.paddingMedium,
                end = Dimen.dimen.paddingMedium
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
                if (query.isEmpty()) {
                    Text(
                        text = "Search here...",
                        color = Color.Gray,
                        fontSize = Dimen.dimen.fontSizeSmall2.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = { newQuery ->
                        query = newQuery
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color.LightGray,
                        fontSize = Dimen.dimen.fontSizeSmall2.sp,
                        textAlign = TextAlign.Start
                    ),
                    singleLine = true,
                    cursorBrush = if (isKeyboardVisible) SolidColor(Color.LightGray) else SolidColor(Color.Transparent)
                )
            }
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
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

