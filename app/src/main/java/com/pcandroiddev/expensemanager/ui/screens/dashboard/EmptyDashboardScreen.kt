package com.pcandroiddev.expensemanager.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
fun EmptyDashboardScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_list_animation))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier.size(150.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No Transactions Yet !",
                fontFamily = FontFamily(Font(R.font.inter_light)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = DetailsTextColor
            )
        }
    }
}

@Preview
@Composable
fun EmptyDashboardScreenPreview() {
    EmptyDashboardScreen()
}