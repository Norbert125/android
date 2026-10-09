package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Login(modifier: Modifier = Modifier) {
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

        // Bejelentkezés gomb
        Button(
            onClick = {
                isSubmitted = true
                if (isEmailValid && isPasswordValid) {
                    // Sikeres validáció esetén ide jön a logika
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Belépés")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyApplicationTheme() {
        Login()
    }
}