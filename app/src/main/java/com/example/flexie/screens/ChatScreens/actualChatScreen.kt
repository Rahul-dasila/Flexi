package com.example.flexie.screens.ChatScreens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.KeyboardAlt
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.rounded.Send
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.flexie.R
import com.example.flexie.ViewModels.ActualChatViewModel
import com.example.flexie.ui.theme.color3
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.receiverChat
import com.example.flexie.ui.theme.senderChat
import com.example.flexie.ui.theme.shimmerColor
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.setSystemBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun actualChatScreen(
    id: String,
    name: String,
    actualChatViewModel: ActualChatViewModel,
    navHostController: NavHostController
) {
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = Color.Black)
    LaunchedEffect(key1 = Unit) {
        actualChatViewModel.loadChats(id)
        actualChatViewModel.showChats(id)
    }
    val chats = actualChatViewModel._chats.collectAsState().value.reversed()
    val listState = rememberLazyListState()

    if (chats.isNotEmpty()) {
        LaunchedEffect(key1 = chats.size) {
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        val painter = rememberImagePainter(data = R.drawable.newbg2, builder = {
            size(1200)
        })
        Image(
            painter = painter,
            contentDescription = "",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(backgroundColor = darkBlue, modifier = Modifier.animateContentSize()) {
                var isClickable by remember { mutableStateOf(true) }
                Box(
                    modifier = Modifier
                        .padding(
                            start = Dimen.dimen.padding3
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
                            .size(Dimen.dimen.medium1)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.inter),
                    contentDescription = "",
                    modifier = Modifier
                        .size(Dimen.dimen.medium4)
                        .padding(Dimen.dimen.padding3)
                        .clip(shape = RoundedCornerShape(percent = 50))
                        .background(
                            color = shimmerColor,
                            shape = RoundedCornerShape(percent = 50)
                        ),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = name.trim(),
                    color = Color.White,
                    maxLines = 1,
                    fontSize = (Dimen.dimen.fontSizeMedium - 1).sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier
                        .padding(
                            start = Dimen.dimen.extraSmall
                        )
                        .weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    contentDescription = "videoCall",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(Dimen.dimen.paddingLarge))
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = "",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(Dimen.dimen.paddingLarge))
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(Dimen.dimen.paddingSmall))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState,
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {
                items(chats) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                top = Dimen.dimen.extraSmall,
                                start = Dimen.dimen.small1,
                                end = Dimen.dimen.small1
                            ),
                        contentAlignment = if (it.receiverId == actualChatViewModel.currentId()) Alignment.CenterStart else Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .clip(
                                    RoundedCornerShape(Dimen.dimen.small1)
                                )
                                .background(color = if (it.receiverId == actualChatViewModel.currentId()) senderChat else receiverChat)
                                .padding(Dimen.dimen.small1),
                            contentAlignment = Alignment.Center
                        ) {
                            Column (horizontalAlignment = Alignment.End){
                                Row {
                                    Text(
                                        text = it.text, color = Color.White,
                                        fontSize = (Dimen.dimen.fontSizeSmall2).sp,
                                        fontWeight = FontWeight(450),
                                    )
                                    Text(
                                        text = "12:120Pm", color = Color.Transparent,
                                        fontSize = (Dimen.dimen.fontSizeExtraSmall).sp,
                                        textAlign = TextAlign.End
                                    )
                                }
                                val time = actualChatViewModel.convertLongToTime(it.timestamp)
                                Text(
                                    text = time, color = if (it.receiverId == actualChatViewModel.currentId()) Color.Gray else Color.White,
                                    fontSize = (Dimen.dimen.fontSizeExtraSmall).sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .imePadding(),
                verticalAlignment = Alignment.Bottom
            ) {
                val ik = remember {
                    mutableStateOf("")
                }
                val focusRequester  = remember {
                    FocusRequester()
                }
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Dimen.dimen.small1)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(color3),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .padding(Dimen.dimen.paddingSmall)
                        .clickable {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardAlt,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(Dimen.dimen.medium2)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (ik.value.isEmpty()) {
                            Text(
                                text = "Type a message",
                                color = Color.Gray,
                                fontSize = (Dimen.dimen.fontSizeMedium - 1).sp
                            )
                        }
                        BasicTextField(
                            value = ik.value,
                            onValueChange = {
                                ik.value = it
                            },
                            textStyle = TextStyle(
                                color = Color.LightGray,
                                fontSize = Dimen.dimen.fontSizeMedium.sp,
                                textAlign = TextAlign.Start
                            ),
                            singleLine = false,
                            cursorBrush = SolidColor(Color.LightGray),
                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                        )
                    }
                }
                val scale = remember {
                    Animatable(1f)
                }
                Box(modifier = Modifier
                    .padding(bottom = Dimen.dimen.small1)
                    .size(Dimen.dimen.medium4 - 1.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.LightGray)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                scale.animateTo(0.95f)
                                val success = tryAwaitRelease()
                                scale.animateTo(1f)
                                if (success) {
                                    if (ik.value.isNotEmpty()) {
                                        actualChatViewModel.sendMsg(id, ik.value , name)
                                        ik.value = ""
                                    }
                                }
                            }
                        )
                    }, contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = "", tint = darkBlue)
                }
                Spacer(modifier = Modifier.width(Dimen.dimen.small1))

            }
        }
    }
}