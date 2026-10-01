package com.moviles.lab8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable


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

    NavHost(navController = rootNavController, startDestination = LoginRoute) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginClick = {
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
fun MainScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = "Characters") },
                    label = { Text("Characters") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(CharactersGraph) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Place, contentDescription = "Locations") },
                    label = { Text("Locations") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(LocationsGraph) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(ProfileRoute) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(paddingValues)
        ) {

            navigation<CharactersGraph>(startDestination = CharactersListRoute) {
                composable<CharactersListRoute> {
                    CharactersScreen(
                        onCharacterClick = { id ->
                            bottomNavController.navigate(CharacterDetailRoute(characterId = id))
                        }
                    )
                }
                composable<CharacterDetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<CharacterDetailRoute>()
                    CharacterDetailScreen(
                        characterId = route.characterId,
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = LocationsListRoute) {
                composable<LocationsListRoute> {
                    LocationsScreen(
                        onLocationClick = { id ->
                            bottomNavController.navigate(LocationDetailRoute(locationId = id))
                        }
                    )
                }
                composable<LocationDetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<LocationDetailRoute>()
                    LocationDetailScreen(
                        locationId = route.locationId,
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }


            composable<ProfileRoute> {
                ProfileScreen(
                    onLogoutClick = {
                        rootNavController.navigate(LoginRoute) {
                            popUpTo<MainRoute> { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(1f))

        AsyncImage(
            model = "https://upload.wikimedia.org/wikipedia/commons/b/b1/Rick_and_Morty.svg",
            contentDescription = "Logo Rick and Morty",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Fabricio Estrada - 25230",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(onCharacterClick: (Int) -> Unit) {
    val characters = CharacterDb().getAllCharacters()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(characters) { character ->
                CharacterListItem(character = character, onClick = { onCharacterClick(character.id) })
            }
        }
    }
}

@Composable
fun CharacterListItem(character: Character, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = character.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "${character.species} - ${character.status}", color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(characterId: Int, onBackClick: () -> Unit) {
    val character = CharacterDb().getCharacterById(characterId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = character.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(48.dp))

            DetailRow(label = "Species:", value = character.species)
            DetailRow(label = "Status:", value = character.status)
            DetailRow(label = "Gender:", value = character.gender)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(onLocationClick: (Int) -> Unit) {
    val locations = LocationDb().getAllLocations()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(locations) { location ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationClick(location.id) }
                        .padding(16.dp)
                ) {
                    Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = location.type, color = Color.Gray)
                }
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(locationId: Int, onBackClick: () -> Unit) {
    val location = LocationDb().getLocationById(locationId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(32.dp))

            DetailRow(label = "ID:", value = location.id.toString())
            DetailRow(label = "Type:", value = location.type)
            DetailRow(label = "Dimension:", value = location.dimension)
        }
    }
}

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Fabricio Estrada", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Carné: 25230", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp)
    }
}