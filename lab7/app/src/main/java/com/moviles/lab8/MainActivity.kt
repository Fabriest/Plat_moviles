package com.moviles.lab8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.lab8.ui.LoginScreen
import com.moviles.lab8.ui.MainScreen
import com.moviles.lab8.viewmodels.AuthViewModel
import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object LoginRoute
@Serializable object MainRoute

@Serializable object CharactersGraph
@Serializable object CharactersListRoute
@Serializable data class CharacterDetailRoute(val characterId: Int)

@Serializable object LocationsGraph
@Serializable object LocationsListRoute
@Serializable data class LocationDetailRoute(val locationId: Int)

@Serializable object ProfileRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
fun RickAndMortyApp() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = SplashRoute) {
        composable<SplashRoute> {
            SplashScreen(
                onNavigateToMain = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    rootNavController.navigate(LoginRoute) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                }
            )
        }

        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            )
        }

        composable<MainRoute> {
            MainScreen(rootNavController = rootNavController)
        }
    }
}

@Composable
fun SplashScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(Unit) {
        val isLoggedIn = viewModel.checkIsLoggedIn()
        if (isLoggedIn) {
            onNavigateToMain()
        } else {
            onNavigateToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}