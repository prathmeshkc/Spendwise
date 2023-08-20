package com.pcandroiddev.expensemanager.ui.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.GreenIncomeColor
import com.pcandroiddev.expensemanager.ui.theme.RedExpenseColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.utils.Helper


@Composable
fun TransactionListItem(
    symbol: String,
    transactionResponse: TransactionResponse,
    onTransactionListItemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 9.dp, horizontal = 16.dp)
            .height(68.dp)
            .clickable { onTransactionListItemClicked() },
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
                    .background(SurfaceBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = when (transactionResponse.category) {
                        "Entertainment" -> {
                            painterResource(id = R.drawable.ic_entertainment)
                        }

                        "Food" -> {
                            painterResource(id = R.drawable.ic_food)
                        }

                        "Healthcare" -> {
                            painterResource(id = R.drawable.ic_healthcare)
                        }

                        "Housing" -> {
                            painterResource(id = R.drawable.ic_housing)
                        }

                        "Insurance" -> {
                            painterResource(id = R.drawable.ic_insurance)
                        }

                        "Miscellaneous" -> {
                            painterResource(id = R.drawable.ic_miscellaneous)
                        }

                        "Personal Spending" -> {
                            painterResource(id = R.drawable.ic_personal_spending)
                        }

                        "Savings & Debts" -> {
                            painterResource(id = R.drawable.ic_savings)
                        }

                        "Transportation" -> {
                            painterResource(id = R.drawable.ic_transport)
                        }

                        "Utilities" -> {
                            painterResource(id = R.drawable.ic_utilities)
                        }

                        else -> {
                            painterResource(id = R.drawable.ic_miscellaneous)
                        }
                    },
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "Transaction Category Image"
                )
            }

            Spacer(modifier = Modifier.padding(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center


            ) {
                Text(
                    text = if (transactionResponse.title.length > 20) {
                        "${transactionResponse.title.take(20)}..." // Truncate and add ellipsis
                    } else {
                        transactionResponse.title // Use the original title if not exceeding 20 characters
                    },
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DetailsTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = transactionResponse.category,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    color = DetailsTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    text = when (transactionResponse.transactionType) {
                        TransactionType.EXPENSE.name -> {
                            "-$symbol".plus(Helper.formatAmountWithLocale(amount = transactionResponse.amount))
                        }

                        TransactionType.INCOME.name -> {
                            "+$symbol".plus(Helper.formatAmountWithLocale(amount = transactionResponse.amount))
                        }

                        else -> {
                            Helper.formatAmountWithLocale(amount = transactionResponse.amount)
                        }
                    },
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = when (transactionResponse.transactionType) {
                        TransactionType.EXPENSE.name -> {
                            RedExpenseColor
                        }

                        TransactionType.INCOME.name -> {
                            GreenIncomeColor
                        }

                        else -> {
                            DetailsTextColor
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = transactionResponse.transactionDate,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    color = DetailsTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
fun TransactionListItemPreview() {
    TransactionListItem(
        symbol = "$",
        transactionResponse = TransactionResponse(
            "",
            "",
            "Investment Dividends",
            1234.0,
            "INCOME",
            "Medical",
            "2023-06-08",
            ""
        ),
        onTransactionListItemClicked = {

        })
}