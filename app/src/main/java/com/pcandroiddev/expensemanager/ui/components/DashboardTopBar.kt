package com.pcandroiddev.expensemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor

@Composable
fun DashboardTopBar(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(ComponentsBackgroundColor),


        ) {


        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            FilterDropDown()

            ToggleThemeButton(isDarkMode = isDarkMode)

        }

    }
}

@Composable
private fun ToggleThemeButton(isDarkMode: Boolean) {
    Icon(
        modifier = Modifier
            .padding(end = 12.dp)
            .clickable { /*TODO*/ },
        imageVector = if (isDarkMode) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
        contentDescription = "Change Theme",
        tint = Color.White
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropDown() {

    val filters = listOf("All Transactions", "All Incomes", "All Expenses")

    var isExpanded by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf(filters[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .width(220.dp),
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded
        }
    ) {

        TextField(
            value = selectedFilter,
            onValueChange = { },
            textStyle = TextStyle(
                color = DetailsTextColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = ComponentsBackgroundColor,
                unfocusedBorderColor = ComponentsBackgroundColor,
                focusedTrailingIconColor = DetailsTextColor,
                unfocusedTrailingIconColor = DetailsTextColor
            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .width(190.dp)
                .height(155.dp)
                .background(SurfaceBackgroundColor),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = filter,
                            color = DetailsTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    onClick = {
                        selectedFilter = filter
                        isExpanded = false
                    })
            }
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardExpensesSearchBar(
    modifier: Modifier = Modifier,
    onLogoutOptionClicked: () -> Unit = {},
    onTransactionFilterClicked: (String) -> Unit,
    isActive: (Boolean) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchButtonClicked: () -> Unit,
    content: @Composable() (ColumnScope.() -> Unit)
) {

    var queryText by remember { mutableStateOf("") }

    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        query = queryText,
        onQueryChange = { query ->
            queryText = query
            onSearchTextChanged(queryText)
        },
        onSearch = {
            onSearchButtonClicked()

        },
        active = active,
        onActiveChange = {
            active = it
            isActive(active)
            if (!active) {
                queryText = ""
                onSearchTextChanged(queryText)
            }
        },
        placeholder = {
            Text(
                text = "Search your transactions",
                style = TextStyle(
                    color = HeadingTextColor,

                    )
            )
        },
        leadingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (queryText.isNotEmpty()) {
                            queryText = ""
                            onSearchTextChanged(queryText)
                            active = false
                            isActive(active)
                        } else {
                            active = false
                            isActive(active)
                        }
                    },
                    imageVector = Icons.Outlined.ArrowBack,
                    tint = HeadingTextColor,
                    contentDescription = "Close search bar"
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    tint = HeadingTextColor,
                    contentDescription = "Expenses Search Bar"
                )
            }


        },
        trailingIcon = {

            if (active) {
                if (queryText.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            queryText = ""
                            onSearchTextChanged(queryText)
                        },
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Expenses Search Bar"
                    )
                }

            } else {
                Row {

                    //TODO: Add a filter icon button

                    /*DashboardFilterButton(
                        onTransactionFilterClicked = { selectedTransactionFilter ->
                            onTransactionFilterClicked(selectedTransactionFilter)
                        })*/

                    DashboardSearchBarOptionsMenu(
                        onLogoutOptionClicked = {
                            onLogoutOptionClicked()
                        }
                    )
                }
            }


        },
        colors = SearchBarDefaults.colors(
            containerColor = ComponentsBackgroundColor,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = HeadingTextColor,
                cursorColor = FABColor
            )

        )
    ) {
        content()
    }
}

/*
@Composable
fun DashboardFilterButton(
    onTransactionFilterClicked: (String) -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    val transactionFiltersList = listOf("Daily", "Weekly", "Monthly", "Yearly")
    var selectedTransactionFilter by remember {
        mutableStateOf(transactionFiltersList[2])
    }

    IconButton(onClick = { openDialog = true }) {
        Icon(
            imageVector = Icons.Outlined.FilterAlt,
            contentDescription = "Filter Transactions",
            tint = HeadingTextColor
        )
    }

    if (openDialog) {
        Dialog(onDismissRequest = { openDialog = false }) {
            Surface(
                color = ComponentsBackgroundColor
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Show Transactions",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            color = DetailsTextColor
                        )
                    )
                    transactionFiltersList.forEach { transactionFilter ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = selectedTransactionFilter == transactionFilter,
                                    onClick = {
                                        selectedTransactionFilter = transactionFilter
                                        onTransactionFilterClicked(selectedTransactionFilter)
                                        openDialog = false
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTransactionFilter == transactionFilter,
                                onClick = {
                                    selectedTransactionFilter = transactionFilter
                                    onTransactionFilterClicked(selectedTransactionFilter)
                                    openDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = BottomNavigationBarItemSelectedColor,

                                    )
                            )
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = transactionFilter,
                                style = TextStyle(
                                    color = DetailsTextColor,
                                    fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                                )
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { openDialog = false }) {
                        Text(
                            text = "CANCEL",
                            style = TextStyle(
                                color = BottomNavigationBarItemSelectedColor,
                            )
                        )
                    }
                }
            }
        }
    }
}
*/

@Composable
fun DashboardSearchBarOptionsMenu(
    onLogoutOptionClicked: () -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.wrapContentSize()) {

        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More Options",
                tint = HeadingTextColor
            )
        }

        DropdownMenu(
            modifier = Modifier.background(SurfaceBackgroundColor),
            expanded = expanded, onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Logout",
                        color = HeadingTextColor,
                        fontFamily = FontFamily(Font(R.font.inter_regular))
                    )
                },
                onClick = {
                    expanded = false
                    onLogoutOptionClicked()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = "More Option Menu",
                        tint = HeadingTextColor
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun DashboardTopBarPreview() {
    Column {
        DashboardTopBar(isDarkMode = true)
        Spacer(modifier = Modifier.padding(12.dp))
        DashboardTopBar(isDarkMode = false)
        Spacer(modifier = Modifier.padding(12.dp))
        FilterDropDown()
        Spacer(modifier = Modifier.padding(12.dp))
        DashboardExpensesSearchBar(
            isActive = {

            },
            onTransactionFilterClicked = {

            },
            onSearchTextChanged = {

            },
            onSearchButtonClicked = {

            },
            content = {

            })

        DashboardExpensesSearchBar(
            isActive = {

            },
            onTransactionFilterClicked = {

            },
            onSearchTextChanged = {

            },
            onSearchButtonClicked = {

            },
            content = {

            })

        DashboardSearchBarOptionsMenu(onLogoutOptionClicked = {

        })

       /* DashboardFilterButton(onTransactionFilterClicked = {

        })
*/

    }
}
