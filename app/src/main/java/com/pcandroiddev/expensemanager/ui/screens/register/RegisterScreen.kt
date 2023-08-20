package com.pcandroiddev.expensemanager.ui.screens.register

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.DisabledButtonColor
import com.pcandroiddev.expensemanager.ui.theme.DisabledTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.LinkColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.RegisterUIEvent
import com.pcandroiddev.expensemanager.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

private const val TAG = "RegisterScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onLoginTextClicked: () -> Unit,
    onRegistrationSuccessful: () -> Unit,
    onBackPressedCallback: () -> Unit
) {
    val signUpState = registerViewModel.signUpState.collectAsState(initial = null)
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
                            text = "Create New Account",
                            style = TextStyle(
                                color = DetailsTextColor,
                                fontSize = 25.sp,
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
                    }
                }

                SimpleTextField(
                    modifier = Modifier
                        .padding(top = 60.dp),
                    label = "Name",
                    placeholder = "Full Name",
                    leadingIcon = Icons.Outlined.Person,
                    errorStatus = registerViewModel.registerUIState.value.nameError,
                    onTextChanged = {
                        registerViewModel.onEventChange(
                            event = RegisterUIEvent.NameChanged(name = it)
                        )
                    }
                )

                SimpleTextField(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    label = "Email",
                    placeholder = "Email",
                    leadingIcon = Icons.Outlined.Email,
                    errorStatus = registerViewModel.registerUIState.value.emailError,
                    onTextChanged = {
                        registerViewModel.onEventChange(
                            event = RegisterUIEvent.EmailChanged(email = it)
                        )
                    })

                PasswordTextFieldComponent(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    errorStatus = registerViewModel.registerUIState.value.passwordError,
                    onTextChanged = {
                        registerViewModel.onEventChange(
                            event = RegisterUIEvent.PasswordChanged(password = it)
                        )
                    })

                //TODO: Use LaunchedEffect to observe the sign up status
                RegisterLoginButtonComponent(
                    label = "SIGN UP",
                    isEnable = registerViewModel.allValidationPassed.value,
                    onButtonClicked = {
                        registerViewModel.onEventChange(event = RegisterUIEvent.RegisterButtonClicked)
                    })


                /*DividerTextComponent(
                    modifier = Modifier
                        .padding(top = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    IconButton(
                        onClick = {
                            //TODO: Implement Google Sign-In
    //                    registerViewModel.onEventChange(event = RegisterUIEvent.GoogleSignUpClicked)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Sign Up with Google",
                            tint = Color.Unspecified
                        )
                    }
                }*/

                ClickableLoginTextComponent(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    tryingToLogin = true,
                    onTextSelected = {
//                    ExpenseManagerRouter.navigateTo(destination = Screen.LoginScreen)
                        onLoginTextClicked()
                    })

                if (signUpState.value?.isLoading == true) {
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

        LaunchedEffect(key1 = signUpState.value?.verify) {
            val verify = signUpState.value?.verify
            if (verify != null && verify == "Please Verify Your Email!") {
                Log.d(TAG, "RegisterScreen/verify: $verify")
                isBottomSheetOpen = true
            }
        }

        LaunchedEffect(key1 = signUpState.value?.isSuccess) {
            val success = signUpState.value?.isSuccess
            if (success != null && success == "Sign Up Success!") {
                Log.d(TAG, "RegisterScreen/isSuccess: $success")
//            onRegistrationSuccessful()
                isBottomSheetOpen = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Registration Successful!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        LaunchedEffect(key1 = signUpState.value?.isError) {
            val error = signUpState.value?.isError
            if (!error.isNullOrBlank()) {
                Log.d(TAG, "RegisterScreen/isError: $error")
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

    BackHandler {
        onBackPressedCallback()
    }


}


@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
    onTextChanged: (String) -> Unit
) {

    var text by remember {
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
        value = text,
        onValueChange = {
            text = it
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        label = {
            Text(
                text = label,
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = HeadingTextColor
            )
        },
        supportingText = {
            if (isFocused && !errorStatus.first) {
                Text(
                    text = errorStatus.second,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus.first,
        trailingIcon = {
            if (isFocused && !errorStatus.first) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "$label Empty Error"
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                tint = HeadingTextColor,
                contentDescription = "$label Icon"
            )
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
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
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
        isError = isFocused && !errorStatus.first,
        supportingText = {
            if (isFocused && !errorStatus.first) {
                Text(
                    text = errorStatus.second,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = DetailsTextColor,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterLoginButtonComponent(
    label: String,
    isEnable: Boolean = false,
    onButtonClicked: () -> Unit
) {

    val controller = LocalSoftwareKeyboardController.current

    Button(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnable,
        onClick = {
            controller?.hide()
            onButtonClicked.invoke()
//            ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
            Log.d(TAG, "RegisterLoginButtonComponent Clicked")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = FABColor,
            disabledContainerColor = DisabledButtonColor,
            contentColor = DetailsTextColor,
            disabledContentColor = DisabledTextColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp
        )
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,

            )
    }
}

@Composable
fun ClickableLoginTextComponent(
    modifier: Modifier = Modifier,
    tryingToLogin: Boolean = false,
    onTextSelected: (String) -> Unit
) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = DetailsTextColor,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )
        ) {
            append(initialText)
        }

        withStyle(
            style = SpanStyle(
                color = LinkColor,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )
        ) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}

@Composable
fun DividerTextComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = HeadingTextColor,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = "OR",
            fontSize = 16.sp,
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_medium))
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = HeadingTextColor,
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailModalBottomSheet() {

    var isBottomSheetOpen by remember {
        mutableStateOf(true)
    }

    if (isBottomSheetOpen) {
        ModalBottomSheet(
            containerColor = ComponentsBackgroundColor,
            tonalElevation = 8.dp,
            onDismissRequest = { isBottomSheetOpen = true },
            dragHandle = {
                Column(modifier = Modifier.padding(20.dp)) {
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


            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.email_verification))
            LottieAnimation(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(20.dp)
                    .height(20.dp)
                    .align(Alignment.CenterHorizontally),
                composition = composition,
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )

        }
    }
}


@Preview
@Composable
fun RegisterScreenPreview() {
    /*RegisterScreen(
        onLoginTextClicked = {

        },
        onRegistrationSuccessful = {

        },
        onBackPressedCallback = {

        }
    )*/

//    VerifyEmailModalBottomSheet()

}