package com.pcandroiddev.expensemanager.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.GreenIncomeColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor

/**
 * Variables required are
 * 1. Transaction Category Image
 * 2. Transaction Title
 * 3. Transaction Category
 * 4. Transaction Amount
 * 5. Transaction is Income/Expense
 * 6. Transaction Data
 */


@Composable
fun TransactionListItem() {
    Card(
        modifier = Modifier
            .padding(vertical = 9.dp, horizontal = 16.dp)
            .height(68.dp)
            .clickable { /*TODO*/ },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 13.dp)
                        .size(48.dp)
                        .background(SurfaceBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.Home,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = "Transaction Category Image"
                    )
                }

                Spacer(modifier = Modifier.padding(21.dp))

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center


                ) {
                    Text(
                        text = "Checkup",
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DetailsTextColor
                    )

                    Text(
                        text = "Medical",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        color = DetailsTextColor
                    )
                }


                Column(
                    modifier = Modifier
                        .padding(end = 17.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,


                    ) {
                    Text(
                        text = "+$1750.0",
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = GreenIncomeColor
                    )

                    Text(
                        text = "May 19, 2023",
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        color = DetailsTextColor
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun TransactionListItemPreview() {
    TransactionListItem()
}