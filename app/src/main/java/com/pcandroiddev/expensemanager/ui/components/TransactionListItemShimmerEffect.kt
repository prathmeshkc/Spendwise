package com.pcandroiddev.expensemanager.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor


@Composable
fun TransactionListItemShimmerEffect() {

    val shimmerColors = listOf(
        SurfaceBackgroundColor.copy(alpha = 0.6f),
        SurfaceBackgroundColor.copy(alpha = 0.2f),
        SurfaceBackgroundColor.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    TransactionListShimmerItem(brush = brush)

}

@Composable
fun TransactionListShimmerItem(brush: Brush) {
    Card(
        modifier = Modifier
            .padding(vertical = 9.dp, horizontal = 16.dp)
            .height(68.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, start = 13.dp)
                    .size(48.dp)
                    .background(brush)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(width = 160.dp, height = 16.dp)
                        .background(brush)

                )

                Spacer(modifier = Modifier.padding(top = 5.dp))

                Box(
                    modifier = Modifier
                        .size(width = 160.dp, height = 16.dp)
                        .background(brush)

                )
            }


            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 17.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {

                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 16.dp)
                        .background(brush)

                )

                Spacer(modifier = Modifier.padding(top = 5.dp))

                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 16.dp)
                        .background(brush)

                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionListItemShimmerEffectPreview() {
    TransactionListItemShimmerEffect()
}
