package com.example.progr3ssapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progr3ssapp.ui.theme.Progr3ssAppTheme

@Composable
fun HomeScreen(
    onNavigateToProfile: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Üdv a főoldalon!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigateToProfile("user_456", "Jane Doe") }) {
            Text(text = "Profil megtekintése")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Progr3ssAppTheme {
        HomeScreen(onNavigateToProfile = { _, _ -> })
    }
}
