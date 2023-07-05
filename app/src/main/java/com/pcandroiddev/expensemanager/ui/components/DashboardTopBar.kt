package com.pcandroiddev.expensemanager.ui.components

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
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
fun ExpensesSearchBar(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    leadingIconDesc: String = "",
    trailingIconDesc: String = "",
    onLeadingIconClicked: () -> Unit = {},
    onTrailingIconClicked: () -> Unit = {},
    isActive: (Boolean) -> Unit
) {

    var queryText by remember { mutableStateOf("") }

    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        query = queryText,
        onQueryChange = { query ->
            queryText = query
        },
        onSearch = {
            active = false
            queryText = ""
            isActive(false)
        },
        active = active,
        onActiveChange = {
            active = it
            isActive(it)
        },
        placeholder = {
            Text(
                text = "Search your expenses",
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
                        } else {
                            active = false
                            isActive(false)
                        }
                    },
                    imageVector = Icons.Outlined.ArrowBack,
                    tint = HeadingTextColor,
                    contentDescription = "Close search bar"
                )
            } else {
                if (leadingIcon == null) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        tint = HeadingTextColor,
                        contentDescription = "Expenses Search Bar"
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable { onLeadingIconClicked() },
                        imageVector = leadingIcon,
                        tint = HeadingTextColor,
                        contentDescription = leadingIconDesc
                    )
                }
            }


        },
        trailingIcon = {
            if (active) {
                if (queryText.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            queryText = ""
                        },
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Expenses Search Bar"
                    )
                }

            } else {

                if (trailingIcon != null) {
                    Icon(
                        modifier = Modifier.clickable { onTrailingIconClicked() },
                        imageVector = trailingIcon,
                        tint = HeadingTextColor,
                        contentDescription = trailingIconDesc
                    )
                }

            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = ComponentsBackgroundColor,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = HeadingTextColor
            )

        )
    ) {

    }

    BackHandler {
        queryText = ""
        active = false
        isActive(false)
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
        ExpensesSearchBar(
            isActive = {

            })

        ExpensesSearchBar(
            leadingIcon = Icons.Outlined.Menu,
            isActive = {

            })
    }
}
