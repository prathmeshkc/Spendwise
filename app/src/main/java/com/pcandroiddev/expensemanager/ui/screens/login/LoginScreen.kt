package com.pcandroiddev.expensemanager.ui.screens.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.screens.register.ClickableLoginTextComponent
import com.pcandroiddev.expensemanager.ui.screens.register.PasswordTextFieldComponent
import com.pcandroiddev.expensemanager.ui.screens.register.RegisterLoginButtonComponent
import com.pcandroiddev.expensemanager.ui.screens.register.SimpleTextField
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.LoginUIEvent
import com.pcandroiddev.expensemanager.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onRegisterTextClicked: () -> Unit,
    onLoginSuccessful: () -> Unit
) {

    val signInState = loginViewModel.singInState.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 26.dp, bottom = 30.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = "Welcome Back!",
                style = TextStyle(
                    color = DetailsTextColor,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold))
                )
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = "Login to access your transactions",
                style = TextStyle(
                    color = DetailsTextColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_light))
                )
            )

            SimpleTextField(
                modifier = Modifier
                    .padding(top = 50.dp),
                label = "Email",
                placeholder = "Email",
                leadingIcon = Icons.Outlined.Email,
                errorStatus = loginViewModel.loginUIState.value.emailError,
                onTextChanged = {
                    loginViewModel.onEventChange(
                        event = LoginUIEvent.EmailChanged(email = it)
                    )
                })

            PasswordTextFieldComponent(
                modifier = Modifier
                    .padding(top = 43.dp),
                errorStatus = loginViewModel.loginUIState.value.passwordError,
                onTextChanged = {
                    loginViewModel.onEventChange(
                        event = LoginUIEvent.PasswordChanged(password = it)
                    )
                })

            RegisterLoginButtonComponent(
                label = "SIGN IN",
                isEnable = loginViewModel.allValidationPassed.value,
                onButtonClicked = {
                    loginViewModel.onEventChange(event = LoginUIEvent.LoginButtonClicked)
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

                IconButton(onClick = {
//                    loginViewModel.onEventChange(event = LoginUIEvent.GoogleSignInClicked)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Sign In with Google",
                        tint = Color.Unspecified
                    )
                }
            }

            ClickableLoginTextComponent(
                modifier = Modifier
                    .padding(top = 20.dp),
                tryingToLogin = false,
                onTextSelected = {
                    onRegisterTextClicked()
                })

            if (signInState.value?.isLoading == true) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .heightIn(min = 6.dp),
                    color = FABColor
                )
            }


        }
    }

//TODO: See if there's any need for two coroutine scope in bot LE
    LaunchedEffect(key1 = signInState.value?.isSuccess) {

        val success = signInState.value?.isSuccess
        if (success != null && success == "Sign In Success!") {
            Log.d(TAG, "LoginScreen/isSuccess: $success")
            onLoginSuccessful()
        }

    }

    LaunchedEffect(key1 = signInState.value?.isError) {

        val error = signInState.value?.isError
        if (!error.isNullOrBlank()) {
            Log.d(TAG, "LoginScreen/isError: $error")
        }

    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    /*LoginScreen(
        onRegisterTextClicked = {

        },
        onLoginSuccessful = {

        }
    )*/
    LinearProgressIndicator(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .heightIn(min = 6.dp),
        color = FABColor
    )
}
