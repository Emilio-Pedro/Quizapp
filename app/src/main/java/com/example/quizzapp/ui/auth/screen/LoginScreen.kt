package com.example.quizzapp.ui.auth.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzapp.R
import com.example.quizzapp.ui.auth.LoginUiState
import com.example.quizzapp.ui.auth.LoginViewModel
import com.example.quizzapp.ui.theme.QuizzappTheme


/**
 * Composable "inteligente" ou "stateful" que se conecta ao ViewModel.
 * A sua responsabilidade é obter o estado e passar os eventos para a UI.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Evento para notificar a navegação em caso de sucesso
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Recolhe o estado da UI a partir do ViewModel.
    val uiState by viewModel.uiState.collectAsState()

    // LaunchedEffect para navegar quando o login for bem-sucedido.
    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    // Chama o Composable "burro" que apenas desenha a UI.
    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onRegisterClick = viewModel::register
    )
}

/**
 * Composable "burro" ou "stateless" que apenas exibe a UI.
 * Recebe todo o estado e os eventos como parâmetros, tornando-o fácil de prever e reutilizar.
 */
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // LaunchedEffect para mostrar o Snackbar quando houver um erro.
    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Image(painter = painterResource(id = R.drawable.quizapp2),
                    contentDescription = "Logo",
                    modifier = Modifier.height(150.dp))

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    isError = uiState.error != null
                )

                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = uiState.error != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onLoginClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Login")
                        }
                        Button(
                            onClick = onRegisterClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Registar")
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Estado Padrão")
@Composable
fun LoginScreenPreview() {
    QuizzappTheme() {
        LoginContent(
            uiState = LoginUiState(email = "teste@email.com", pass = "123456"),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado de Carregamento")
@Composable
fun LoginScreenLoadingPreview() {
    QuizzappTheme {
        LoginContent(
            uiState = LoginUiState(isLoading = true),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}