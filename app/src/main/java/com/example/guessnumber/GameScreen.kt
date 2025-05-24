package com.example.guessnumber

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameScreen(onRestart: () -> Unit) {
    var numeroSecreto by remember { mutableStateOf(Random.nextInt(0, 101)) }
    var intentosRestantes by remember { mutableStateOf(3) }
    var entrada by remember { mutableStateOf("") }
    var pista by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeFinal by remember { mutableStateOf("") }
    var tiempoRestante by remember { mutableStateOf(60) } // Tiempo en segundos
    var tiempoAgotado by remember { mutableStateOf(false) }

    // Inicia cuenta regresiva al cargar la pantalla o reiniciar
    LaunchedEffect(key1 = mostrarDialogo) {
        if (!mostrarDialogo) {
            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--
            }
            if (!mostrarDialogo) {
                tiempoAgotado = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Adivina un número entre 0 y 100", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tiempo restante: $tiempoRestante segundos")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = entrada,
            onValueChange = { entrada = it },
            label = { Text("Tu número") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val numeroUsuario = entrada.toIntOrNull()
                if (numeroUsuario == null || numeroUsuario !in 0..100) {
                    pista = "Número inválido. Ingresa un valor entre 0 y 100. y 1 min"
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
            },
            enabled = tiempoRestante > 0 && !mostrarDialogo && !tiempoAgotado
        ) {
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
                        tiempoRestante = 60
                        tiempoAgotado = false
                        mostrarDialogo = false
                        onRestart()
                    }) {
                        Text("Volver al inicio")
                    }
                }
            )
        }

        if (tiempoAgotado && !mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Tiempo agotado") },
                text = { Text("Se acabó el tiempo. El número era $numeroSecreto.") },
                confirmButton = {
                    Button(onClick = {
                        numeroSecreto = Random.nextInt(0, 101)
                        intentosRestantes = 3
                        entrada = ""
                        pista = ""
                        tiempoRestante = 60
                        tiempoAgotado = false
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
