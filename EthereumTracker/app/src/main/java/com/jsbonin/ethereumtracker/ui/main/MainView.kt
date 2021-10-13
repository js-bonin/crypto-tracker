package com.jsbonin.ethereumtracker.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsbonin.ethereumtracker.R
import com.jsbonin.ethereumtracker.ui.theme.EthereumTrackerTheme
import com.jsbonin.ethereumtracker.ui.theme.black
import com.jsbonin.ethereumtracker.ui.theme.ethereumGold
import com.jsbonin.ethereumtracker.ui.theme.ethereumGrey
import com.jsbonin.ethereumtracker.viewmodel.MainViewModel

@Composable
fun MainView(viewModel: MainViewModel) {
    val ethPriceUSD = viewModel.ethereumPriceUSD().collectAsState(initial = "")
    EthereumTrackerTheme {
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .background(color = black)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.TopCenter),
                painter = painterResource(id = R.drawable.image_hero),
                contentDescription = "hero",
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter),
                painter = painterResource(id = R.drawable.image_finance_transparent),
                contentDescription = "defi",
                contentScale = ContentScale.Inside
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 226.dp)
                    .background(color = ethereumGold, shape = RoundedCornerShape(12.dp))
                    .height(48.dp)
                    .width(160.dp)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                text = ethPriceUSD.value,
                color = ethereumGrey,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}