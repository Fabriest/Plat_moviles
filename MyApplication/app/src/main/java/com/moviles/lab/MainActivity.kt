package com.moviles.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
            PortadaLayout()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiVistaPrevia() {
    PortadaLayout()
}

@Composable
fun PortadaLayout(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .border(8.dp, Color(0xFF1B5E20))
            .padding(32.dp)
    ) {


        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Universidad del Valle\nde Guatemala",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Programación de plataformas\nmóviles, Sección 30",
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(64.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "INTEGRANTES",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )


                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "Juan Durini", fontSize = 18.sp)
                    Text(text = "Cristiano Ronaldo", fontSize = 18.sp)
                    Text(text = "Lionel Messi", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CATEDRÁTICO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Juan Carlos Durini",
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Fabricio Estrada",
                fontSize = 20.sp
            )
            Text(
                text = "25230",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}