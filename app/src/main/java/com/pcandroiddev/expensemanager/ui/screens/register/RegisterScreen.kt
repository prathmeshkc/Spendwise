package com.pcandroiddev.expensemanager.ui.screens.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor

private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen() {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 36.dp, bottom = 60.dp)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = "Create New Account",
                style = TextStyle(
                    color = DetailsTextColor,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold))
                )
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = "Please provide all the required information",
                style = TextStyle(
                    color = DetailsTextColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_light))
                )
            )

            //TODO: handle errorStatus from the ViewModel
            EmailTextField(
                modifier = Modifier
                    .padding(top = 109.dp),
                onTextChanged = {

                })

            //TODO: handle errorStatus from the ViewModel
            PasswordTextFieldComponent(
                modifier = Modifier
                    .padding(top = 43.dp),
                onTextChanged = {

                })

            RegisterLoginButtonComponent(onButtonClicked = {

            })

            Text(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                text = "OR",
                style = TextStyle(
                    color = DetailsTextColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_light)),
                )
            )

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "SIGN IN WITH: ",
                    style = TextStyle(
                        color = DetailsTextColor,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter_light)),
                    )
                )

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Sign Up with Google",
                        tint = Color.Unspecified
                    )
                }
            }


        }
    }

}


@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    errorStatus: Boolean = false,
    onTextChanged: (String) -> Unit
) {

    var emailText by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                }
            },
        value = emailText,
        onValueChange = {
            emailText = it
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        label = {
            Text(
                text = "Email",
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        placeholder = {
            Text(
                text = "example@domain.com",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = HeadingTextColor
            )
        },
        supportingText = {
            if (isFocused && !errorStatus) {
                Text(
                    text = "Email must not be empty",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                tint = HeadingTextColor,
                contentDescription = "Email Icon"
            )
        },
        trailingIcon = {
            if (isFocused && !errorStatus) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Email Empty Error"
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ComponentsBackgroundColor,
            unfocusedContainerColor = ComponentsBackgroundColor,
            disabledContainerColor = ComponentsBackgroundColor,
            errorContainerColor = ComponentsBackgroundColor,
            cursorColor = Color.White,
            focusedIndicatorColor = FABColor,
            unfocusedIndicatorColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = DetailsTextColor,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


        )
}


@Composable
fun PasswordTextFieldComponent(
    modifier: Modifier = Modifier,
    errorStatus: Boolean = false,
    onTextChanged: (String) -> Unit,
) {

    val localFocusManager = LocalFocusManager.current

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                }
            },
        label = {
            Text(
                text = "Password",
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password,
        onValueChange = {
            password = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                tint = HeadingTextColor,
                contentDescription = "Password Icon"
            )
        },
        trailingIcon = {

            val iconImage = if (passwordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = iconImage,
                    tint = HeadingTextColor,
                    contentDescription = description
                )
            }

        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = isFocused && !errorStatus,
        colors = TextFieldDefaults.colors(
            focusedTextColor = HeadingTextColor,
            unfocusedTextColor = HeadingTextColor,
            errorTextColor = HeadingTextColor,
            focusedContainerColor = ComponentsBackgroundColor,
            unfocusedContainerColor = ComponentsBackgroundColor,
            disabledContainerColor = ComponentsBackgroundColor,
            errorContainerColor = ComponentsBackgroundColor,
            cursorColor = Color.White,
            focusedIndicatorColor = FABColor,
            unfocusedIndicatorColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = DetailsTextColor,
        )
    )
}

@Composable
fun RegisterLoginButtonComponent(
    onButtonClicked: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth(),
        text = {
            Text(
                text = "SIGN UP",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsTextColor
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.White,
                contentDescription = "Register Account"
            )
        },
        onClick = {
            onButtonClicked.invoke()
//            ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
            Log.d(TAG, "RegisterLoginButtonComponent Clicked")
        },
        containerColor = FABColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp
        )
    )
}


@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
    /*Column {
        EmailTextField(onTextChanged = {})
        PasswordTextFieldComponent(onTextChanged = {})
        RegisterLoginButtonComponent {

        }
    }*/
}