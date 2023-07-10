package com.pcandroiddev.expensemanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.GreenIncomeColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.RedExpenseColor
import com.pcandroiddev.expensemanager.ui.theme.TotalBalanceColor

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    labelText: String,
    amountText: String
) {

    Card(
        modifier = modifier
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = labelText,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = HeadingTextColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "$".plus(amountText),
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                color = TotalBalanceColor
            )
        }


    }

}

@Composable
fun TotalIncomeCard(
    modifier: Modifier = Modifier,
    amountText: String
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top,
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .width(32.dp)
                        .height(32.dp),
                    painter = painterResource(id = R.drawable.transaction_type_income),
                    contentDescription = "Total Income"
                )
            }


            Text(
                modifier = Modifier
                    .padding(start = 21.dp)
                    .fillMaxWidth(),
                text = "TOTAL INCOME",
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = HeadingTextColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth(),
                text = "+ $".plus(amountText),
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                color = GreenIncomeColor
            )
        }
    }
}

@Composable
fun TotalExpenseCard(
    modifier: Modifier = Modifier,
    amountText: String
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top,
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .width(32.dp)
                        .height(32.dp),
                    painter = painterResource(id = R.drawable.transaction_type_expense),
                    contentDescription = "Total Expense"
                )
            }


            Text(
                modifier = Modifier
                    .padding(start = 21.dp)
                    .fillMaxWidth(),
                text = "TOTAL EXPENSE",
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = HeadingTextColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth(),
                text = "- $".plus(amountText),
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                color = RedExpenseColor
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TotalCardsPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TotalBalanceCard(
            labelText = "TOTAL BALANCE",
            amountText = "1807.00"
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TotalIncomeCard(
                modifier = Modifier
                    .weight(1f)
                    .height(124.dp),
                amountText = "3574.00"
            )
            TotalExpenseCard(
                modifier = Modifier
                    .weight(1f)
                    .height(124.dp),
                amountText = "1767.00"
            )
        }
    }


}
