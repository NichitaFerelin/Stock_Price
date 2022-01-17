@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
  ExperimentalComposeUiApi::class
)

package com.ferelin.features.authentication

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferelin.core.ui.component.APP_TOP_PADDING
import com.ferelin.core.ui.component.ClickableIcon
import com.ferelin.core.ui.component.TextField
import com.ferelin.core.ui.theme.AppTheme
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun LoginRoute(
  deps: LoginDeps,
  onBackRoute: () -> Unit
) {
  val component = remember {
    DaggerLoginComponent.builder()
      .dependencies(deps)
      .build()
  }
  val viewModel = viewModel<LoginViewModel>(
    factory = component.viewModelFactory()
  )
  val uiState by viewModel.uiState.collectAsState()

  val context = LocalContext.current

  LoginScreen(
    uiState = uiState,
    onBackClick = onBackRoute,
    onSendCodeClick = { viewModel.onSendCodeClick(context as AppCompatActivity) },
    onPhoneChanged = viewModel::onPhoneChanged,
    onCodeChanged = viewModel::onCodeChanged
  )
}

@Composable
private fun LoginScreen(
  uiState: LoginStateUi,
  onBackClick: () -> Unit,
  onSendCodeClick: () -> Unit,
  onPhoneChanged: (String) -> Unit,
  onCodeChanged: (String) -> Unit,
) {
  Box(
    modifier = Modifier
      .statusBarsPadding()
      .fillMaxSize()
      .background(AppTheme.colors.backgroundPrimary)
  ) {
    ClickableIcon(
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(APP_TOP_PADDING),
      backgroundColor = AppTheme.colors.backgroundPrimary,
      painter = painterResource(R.drawable.ic_arrow_back_24),
      contentDescription = stringResource(R.string.descriptionBack),
      tint = AppTheme.colors.buttonPrimary,
      onClick = onBackClick
    )
    Column(
      modifier = Modifier.padding(
        top = APP_TOP_PADDING,
        start = 10.dp,
        end = 10.dp
      ),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        painter = painterResource(R.mipmap.tesla),
        contentDescription = null
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = stringResource(R.string.hintAuthentication),
        style = AppTheme.typography.body1,
        color = AppTheme.colors.textPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = stringResource(R.string.hintAuthenticationHelp),
        style = AppTheme.typography.body2,
        color = AppTheme.colors.textTertiary,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(16.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(color = Color.Magenta),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "/*TODO Not yet prepared*/\nyou can enter the suggested phone number or your own in the same format",
          style = AppTheme.typography.body1,
          color = AppTheme.colors.textPrimary
        )
      }

      val keyboardController = LocalSoftwareKeyboardController.current
      TextField(
        inputValue = uiState.inputPhone,
        placeholder = stringResource(id = R.string.hintEnterPhone),
        onValueChange = onPhoneChanged,
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(onClick = onSendCodeClick) {
        Text(
          text = stringResource(id = R.string.hintSendCode),
          style = AppTheme.typography.body1,
          color = AppTheme.colors.textPrimary
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      TextField(
        inputValue = uiState.inputCode,
        placeholder = "Enter 12345 or your code /*TODO*/",
        onValueChange = onCodeChanged,
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}