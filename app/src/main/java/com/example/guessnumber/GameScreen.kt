package com.example.guessnumber

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun GameScreen(onRestart: () -> Unit) {
    var numeroSecreto by remember { mutableStateOf(Random.nextInt(0, 101)) }
    var intentosRestantes by remember { mutableStateOf(3) }
    var entrada by remember { mutableStateOf("") }
    var pista by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeFinal by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Adivina un número entre 0 y 100", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = entrada,
            onValueChange = { entrada = it },
            label = { Text("Tu número") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val numeroUsuario = entrada.toIntOrNull()
            if (numeroUsuario == null || numeroUsuario !in 0..100) {
                pista = "Número inválido. Ingresa un valor entre 0 y 100."
                return@Button
            }

            if (numeroUsuario == numeroSecreto) {
                mensajeFinal = "¡Correcto! El número era $numeroSecreto."
                mostrarDialogo = true
            } else {
                intentosRestantes--

                if (intentosRestantes == 0) {
                    mensajeFinal = "Has perdido. El número era $numeroSecreto."
                    mostrarDialogo = true
                } else {
                    pista = if (numeroUsuario < numeroSecreto) {
                        "Pista: El número es mayor. Intentos restantes: $intentosRestantes"
                    } else {
                        "Pista: El número es menor. Intentos restantes: $intentosRestantes"
                    }
                }
            }

            entrada = ""
        }) {
            Text("Adivinar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pista.isNotEmpty()) {
            Text(text = pista)
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Resultado") },
                text = { Text(mensajeFinal) },
                confirmButton = {
                    Button(onClick = {

                        numeroSecreto = Random.nextInt(0, 101)
                        intentosRestantes = 3
                        entrada = ""
                        pista = ""
                        mostrarDialogo = false
                        onRestart()
                    }) {
                        Text("Volver al inicio")
                    }
                }
            )
        }
    }
}
