package com.moviles.lab8.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.moviles.lab8.viewmodels.LocationDetailViewModel
import com.moviles.lab8.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel = viewModel(),
    onLocationClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (state.isLoading) {
                LoadingLayout(onClick = { viewModel.triggerError() })
            } else if (state.hasError) {
                ErrorLayout(onRetry = { viewModel.fetchLocations() })
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.data) { location ->
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

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
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (state.isLoading) {
                LoadingLayout(onClick = { viewModel.triggerError() })
            } else if (state.hasError) {
                ErrorLayout(onRetry = { viewModel.fetchLocationDetail() })
            } else {
                state.data?.let { location ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
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
        }
    }
}