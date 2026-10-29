package com.moviles.lab8.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.lab8.data.Location
import com.moviles.lab8.viewmodels.LocationDetailViewModel
import com.moviles.lab8.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel = viewModel(),
    onLocationClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingLayout(onClick = { viewModel.triggerError() })
        return
    }

    if (state.hasError) {
        ErrorLayout(onRetry = { viewModel.fetchLocations() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Locations") })
        }
    ) { paddingValues ->
        if (state.data.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No hay locaciones disponibles", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                items(state.data) { location ->
                    LocationItem(
                        location = location,
                        onClick = { onLocationClick(location.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: Location, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = location.type, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val location = state.data

    if (state.isLoading) {
        LoadingLayout(onClick = { viewModel.triggerError() })
        return
    }

    if (state.hasError || location == null) {
        ErrorLayout(onRetry = { viewModel.fetchLocationDetail() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(32.dp))

            DetailRow(label = "Type:", value = location.type)
            DetailRow(label = "Dimension:", value = location.dimension)
        }
    }
}