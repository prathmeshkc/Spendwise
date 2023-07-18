package com.pcandroiddev.expensemanager.ui.screens.network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor

@Composable
fun NetworkStateScreen(
    networkState: String
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBackgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.no_internet_animation))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            Text(
                text = networkState,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = DetailsTextColor,
                    textAlign = TextAlign.Center
                )
            )
        }

    }
}

@Preview
@Composable
fun NetworkStateScreenPreview() {
    NetworkStateScreen("Network Lost")
}
