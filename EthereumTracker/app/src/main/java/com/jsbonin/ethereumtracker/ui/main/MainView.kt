package com.jsbonin.ethereumtracker.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsbonin.ethereumtracker.R
import com.jsbonin.ethereumtracker.ui.theme.EthereumTrackerTheme
import com.jsbonin.ethereumtracker.ui.theme.black
import com.jsbonin.ethereumtracker.ui.theme.ethereumGold
import com.jsbonin.ethereumtracker.ui.theme.ethereumLightGrey
import com.jsbonin.ethereumtracker.ui.theme.ethereumLightTeal
import com.jsbonin.ethereumtracker.ui.theme.white
import com.jsbonin.ethereumtracker.viewmodel.MainViewModel

@Composable
fun MainView(viewModel: MainViewModel) {
    val ethPriceUSD = viewModel.ethereumPriceUSD().collectAsState(initial = "")
    val ethPriceColor = viewModel.ethereumPriceColor().collectAsState(initial = white)

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
            TickerView(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 226.dp),
                priceFlow = ethPriceUSD,
                priceColorFlow = ethPriceColor,
                baseLogoPainter = painterResource(id = R.drawable.ic_logo_eth),
                quoteLogoPainter = painterResource(id = R.drawable.ic_logo_usdt)
            )

        }

    }
}

@Composable
fun TickerView(
    modifier: Modifier,
    priceFlow: State<String>,
    priceColorFlow: State<Color>,
    baseLogoPainter: Painter,
    quoteLogoPainter: Painter
) {
    Box(
        modifier = modifier
            .border(4.dp, color = ethereumGold, shape = RoundedCornerShape(12.dp))
            .height(56.dp)
            .wrapContentWidth()
            .background(color = black, shape = RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .border(4.dp, color = ethereumGold, shape = RoundedCornerShape(12.dp))
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(48.dp)
                .background(color = ethereumLightGrey, shape = RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .border(4.dp, color = ethereumGold, shape = RoundedCornerShape(12.dp))
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(48.dp)
                .background(color = ethereumLightGrey, shape = RoundedCornerShape(12.dp))
        )
        Row(
            modifier = Modifier
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                painter = baseLogoPainter,
                contentDescription = "base logo",
                contentScale = ContentScale.Inside
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp),
                text = priceFlow.value,
                color = priceColorFlow.value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier
                    .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                painter = quoteLogoPainter,
                contentDescription = "quote logo",
                contentScale = ContentScale.Inside
            )
        }
    }
}

@Preview
@Composable
fun TickerPreview() {
    TickerView(
        Modifier,
        remember { mutableStateOf("3600.85") },
        remember { mutableStateOf(ethereumLightTeal) },
        baseLogoPainter = painterResource(id = R.drawable.ic_logo_eth),
        quoteLogoPainter = painterResource(id = R.drawable.ic_logo_usdt)
    )
}