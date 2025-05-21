package com.example.guessnumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdivinaNumeroApp()
        }
    }
}

@Composable
fun AdivinaNumeroApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(onStart = { navController.navigate("game") })
        }
        composable("game") {
            GameScreen(onRestart = { navController.popBackStack("start", inclusive = false) })
        }
    }
}
