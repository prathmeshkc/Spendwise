package com.pcandroiddev.expensemanager.ui.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.screens.register.ClickableLoginTextComponent
import com.pcandroiddev.expensemanager.ui.screens.register.DividerTextComponent
import com.pcandroiddev.expensemanager.ui.screens.register.PasswordTextFieldComponent
import com.pcandroiddev.expensemanager.ui.screens.register.RegisterLoginButtonComponent
import com.pcandroiddev.expensemanager.ui.screens.register.SimpleTextField
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.LoginUIEvent
import com.pcandroiddev.expensemanager.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


private const val TAG = "LoginScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    eventString: String? = null,
    onRegisterTextClicked: () -> Unit,
    onLoginSuccessful: () -> Unit
) {

    val signInState = loginViewModel.singInState.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = SurfaceBackgroundColor
        ) {
            Column(
                modifier = Modifier
//                    .padding(start = 30.dp, end = 30.dp, top = 26.dp, bottom = 30.dp)
                    .padding(horizontal = 16.dp, vertical = 30.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp),
                        painter = painterResource(id = R.drawable.ic_app),
                        contentDescription = ""
                    )

                    Column {
                        Text(
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
                    }
                }

                SimpleTextField(
                    modifier = Modifier
                        .padding(top = 95.dp),
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
                        .padding(top = 30.dp),
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

                /*DividerTextComponent(
                    modifier = Modifier
                        .padding(top = 20.dp)
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
    */
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

                if (isBottomSheetOpen) {
                    ModalBottomSheet(
                        containerColor = ComponentsBackgroundColor,
                        tonalElevation = 8.dp,
                        onDismissRequest = { isBottomSheetOpen = false },
                        dragHandle = {
                            Column(
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 20.dp
                                )
                            ) {
                                Image(
                                    painterResource(id = R.drawable.ic_app),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .width(30.dp)
                                        .height(30.dp)
                                        .align(Alignment.CenterHorizontally),
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = "Verify your account",
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                                        fontSize = 22.sp,
                                        color = DetailsTextColor
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = "Account activation link has been sent to the email address you provided",
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.inter_light)),
                                        fontSize = 18.sp,
                                        color = DetailsTextColor
                                    )
                                )

                            }
                        }
                    ) {


                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                R.raw.email_verification
                            )
                        )
                        LottieAnimation(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(200.dp)
                                .height(200.dp)
                                .align(Alignment.CenterHorizontally),
                            composition = composition,
                            isPlaying = true,
                            iterations = LottieConstants.IterateForever
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }


            }
        }
    }

    LaunchedEffect(key1 = eventString != null) {
        if(eventString == "Please Verify Your Email!") {
            isBottomSheetOpen = true
        }
    }

    LaunchedEffect(key1 = signInState.value?.verify) {
        val verify = signInState.value?.verify
        if (verify != null && verify == "Please Verify Your Email!") {
            Log.d(TAG, "LoginScreen/verify: $verify")
//            Toast.makeText(context, verify, Toast.LENGTH_LONG).show()
            isBottomSheetOpen = true
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Please verify your email!",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(key1 = signInState.value?.isSuccess) {
        val success = signInState.value?.isSuccess
        if (success != null && success == "Sign In Success!") {
            Log.d(TAG, "LoginScreen/isSuccess: $success")
            isBottomSheetOpen = false
            onLoginSuccessful()
        }

    }

    LaunchedEffect(key1 = signInState.value?.isError) {

        val error = signInState.value?.isError
        if (!error.isNullOrBlank()) {
            Log.d(TAG, "LoginScreen/isError: $error")
//            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            isBottomSheetOpen = false
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    duration = SnackbarDuration.Long
                )
            }


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
