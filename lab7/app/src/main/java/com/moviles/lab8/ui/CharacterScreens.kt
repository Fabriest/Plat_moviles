package com.moviles.lab8.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.moviles.lab8.data.Character
import com.moviles.lab8.viewmodels.CharacterDetailViewModel
import com.moviles.lab8.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = viewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingLayout(onClick = { viewModel.triggerError() })
        return
    }

    if (state.hasError) {
        ErrorLayout(onRetry = { viewModel.fetchCharacters() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                actions = {
                    IconButton(onClick = { viewModel.toggleFilter() }) {
                        Icon(
                            imageVector = if (state.filterFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Filtrar favoritos",
                            tint = if (state.filterFavorites) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val displayedList = if (state.filterFavorites) state.data.filter { it.isFavorite } else state.data

        if (displayedList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No hay elementos a mostrar", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                items(displayedList) { character ->
                    CharacterItem(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(character.id, character.isFavorite) }
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.size(64.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = character.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "${character.species} - ${character.status}", color = Color.Gray)
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (character.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val character = state.data

    if (state.isLoading) {
        LoadingLayout(onClick = { viewModel.triggerError() })
        return
    }

    if (state.hasError || character == null) {
        ErrorLayout(onRetry = { viewModel.fetchCharacterDetail() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(character.isFavorite) }) {
                        Icon(
                            imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (character.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.size(200.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = character.name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(32.dp))

            DetailRow(label = "Species:", value = character.species)
            DetailRow(label = "Status:", value = character.status)
            DetailRow(label = "Gender:", value = character.gender)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp, color = Color.Gray)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}