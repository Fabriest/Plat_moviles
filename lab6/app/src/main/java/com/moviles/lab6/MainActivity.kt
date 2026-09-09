package com.moviles.lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContadorAvanzadoLayout()
                }
            }
        }
    }
}

data class Movimiento(val valor: Int, val esIncremento: Boolean)

@Composable
fun ContadorAvanzadoLayout() {
    var contador by remember { mutableStateOf(0) }
    var totalIncrementos by remember { mutableStateOf(0) }
    var totalDecrementos by remember { mutableStateOf(0) }
    var valorMaximo by remember { mutableStateOf(0) }
    var valorMinimo by remember { mutableStateOf(0) }
    var totalCambios by remember { mutableStateOf(0) }


    val historial = remember { mutableStateListOf<Movimiento>() }

    val colorIncremento = Color(0xFF2E7D32)
    val colorDecremento = Color(0xFFC62828)
    val colorBotonPrimario = Color(0xFF3F51B5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 24.dp)
    ) {

        Text(
            text = "Fabricio Estrada",
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    contador--
                    totalDecrementos++
                    totalCambios++
                    if (contador < valorMinimo) valorMinimo = contador
                    historial.add(Movimiento(contador, false))
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorBotonPrimario)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Menos", tint = Color.White)
            }

            Text(
                text = contador.toString(),
                fontSize = 80.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            IconButton(
                onClick = {
                    contador++
                    totalIncrementos++
                    totalCambios++
                    if (contador > valorMaximo) valorMaximo = contador
                    historial.add(Movimiento(contador, true))
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorBotonPrimario)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        EstadisticaFila(etiqueta = "Total incrementos:", valor = totalIncrementos.toString())
        EstadisticaFila(etiqueta = "Total decrementos:", valor = totalDecrementos.toString())
        EstadisticaFila(etiqueta = "Valor máximo:", valor = valorMaximo.toString())
        EstadisticaFila(etiqueta = "Valor mínimo:", valor = valorMinimo.toString())
        EstadisticaFila(etiqueta = "Total cambios:", valor = totalCambios.toString())

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Historial:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historial) { movimiento ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (movimiento.esIncremento) colorIncremento else colorDecremento),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = movimiento.valor.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                contador = 0
                totalIncrementos = 0
                totalDecrementos = 0
                valorMaximo = 0
                valorMinimo = 0
                totalCambios = 0
                historial.clear()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorBotonPrimario)
        ) {
            Text(
                text = "Reiniciar",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun EstadisticaFila(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = valor, fontSize = 20.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewContadorAvanzado() {
    MaterialTheme {
        ContadorAvanzadoLayout()
    }
}