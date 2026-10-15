package com.example.progr3ssapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progr3ssapp.ui.theme.Progr3ssAppTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Állapotok a beviteli mezőkhöz
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validáció (min. 6 karakter mindkét mezőben)
    val isEmailValid = email.length >= 6
    val isPasswordValid = password.length >= 6

    // Jelzi, hogy a felhasználó megpróbált-e már belépni (hogy ne mutasson hibát gépelés előtt)
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bejelentkezés",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Felhasználónév / Email mező
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Felhasználónév / Email") },
            singleLine = true,
            isError = isSubmitted && !isEmailValid,
            supportingText = {
                if (isSubmitted && !isEmailValid) {
                    Text("A felhasználónévnek legalább 6 karakternek kell lennie!")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Jelszó mező
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Jelszó") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isSubmitted && !isPasswordValid,
            supportingText = {
                if (isSubmitted && !isPasswordValid) {
                    Text("A jelszónak legalább 6 karakterből kell állnia!")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Bejelentkezés gomb: sikeres validáció után a főoldalra navigál
        Button(
            onClick = {
                isSubmitted = true
                if (isEmailValid && isPasswordValid) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Belépés")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text(text = "Nincs még fiókod? Regisztrálj!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Progr3ssAppTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
    }
}
