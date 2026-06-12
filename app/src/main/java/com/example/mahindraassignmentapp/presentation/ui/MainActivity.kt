package com.example.mahindraassignmentapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mahindraassignmentapp.Constants
import com.example.mahindraassignmentapp.R
import com.example.mahindraassignmentapp.Utils
import com.example.mahindraassignmentapp.data.model.Notification
import com.example.mahindraassignmentapp.presentation.NotificationViewModel
import com.example.mahindraassignmentapp.presentation.UIState
import com.example.mahindraassignmentapp.ui.theme.MahindraAssignmentAppTheme
import com.example.mahindraassignmentapp.ui.theme.bgColor
import com.example.mahindraassignmentapp.ui.theme.blueTextColor
import com.example.mahindraassignmentapp.ui.theme.darkBlueTextColor
import com.example.mahindraassignmentapp.ui.theme.darkGreyTextColor
import com.example.mahindraassignmentapp.ui.theme.darkRedTextColor
import com.example.mahindraassignmentapp.ui.theme.redTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahindraAssignmentAppTheme {
                val viewModel :NotificationViewModel= hiltViewModel()
                LaunchedEffect(Unit) {
                    viewModel.getNotifications()
                }
                Scaffold(
                    containerColor = bgColor,
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        NotificationList(viewModel = viewModel)
                    }

                }
            }
        }
    }
}
@Composable
fun NotificationList(viewModel: NotificationViewModel){
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {

            val totalItems =
                listState.layoutInfo.totalItemsCount
            val lastVisibleItem =
                listState.layoutInfo
                    .visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0
            totalItems > 0 &&
                    lastVisibleItem >= totalItems - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMoreNotifications()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    when(uiState){
        is UIState.Idle->{
            EmptyState("No notifications found")
        }
        is UIState.Loading ->{
            LoadingState()

        }
        is UIState.Error ->{
            ErrorState(
                message = (uiState as UIState.Error).message,
                onRetry = {
                    viewModel.getNotifications()
                }
            )

        }
        is UIState.Success ->{
            val successState = uiState as UIState.Success

            val notifList = successState.notifications

            LazyColumn (state=listState ){
                items(
                    items =notifList,
                    key = {it.id}
                ){item->
                    NotificationItem(item)
                }
                if (successState.isLoadingMore) {

                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun NotificationItem( item:Notification){
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context= LocalContext.current
    val bgColor = if (item.tag == "Critical") redTextColor else bgColor
    OutlinedCard(
        colors =CardDefaults.cardColors(
        containerColor = Color.White),
        border = BorderStroke(1.dp, bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)){

        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                WarningIcon()
                NotificationRow(item=item,
                    onTitleClick = {
                        expanded = !expanded
                    })
            }
            AnimatedVisibility(
                visible = expanded
            ) {
                Text(
                    text = item.body,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = Constants.TRACK_MSG,
                fontWeight = FontWeight.Bold, color = darkBlueTextColor, modifier = Modifier.clickable {
                Utils().goToMap(context=context)
            } )
        }
    }

}

@Composable
fun NotificationRow(
    item :Notification,
    onTitleClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.title,
            modifier = Modifier.widthIn(min = 14.dp, max = 140.dp).clickable {
                onTitleClick()
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.width(4.dp))

        TagText(item.tag)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text =item.time,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            color = darkGreyTextColor
        )
    }
}
@Composable
fun TagText(
    text: String
) {
    Box(
        modifier = Modifier
            .background(
                color = if (text == "Critical")
                    redTextColor
                else
                    bgColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (text == "Critical")
                darkRedTextColor
            else
                blueTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}
@Composable
fun WarningIcon() {
    Box(
        modifier = Modifier
            .size(35.dp)
            .background(
                redTextColor,
                CircleShape
            )
       ,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_warning),
            contentDescription = "Warning",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Black
        )
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            color = redTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}
