package com.dlsu.mobdeve.movle

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedPage(
    userName: String,
    onBackClick: () -> Unit
) {
    val db = Firebase.firestore

    // Function to handle disconnect
    fun handleDisconnect() {
        db.collection("connections").document("current_connection")
            .set(mapOf("status" to "disconnected"))
            .addOnSuccessListener {
                onBackClick()
            }
            .addOnFailureListener { e ->
                // Handle error if needed
                Log.e("ConnectedPage", "Failed to reset connection status", e)
            }
    }

    // Function to listen for disconnection status updates
    LaunchedEffect(Unit) {
        db.collection("connections").document("current_connection")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ConnectedPage", "Listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val status = snapshot.getString("status")
                    if (status == "disconnected") {
                        onBackClick()
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light grey background
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.top_connect_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(1000.dp)
                .offset(y = -20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = -30.dp)
                        .padding(16.dp)
                        .size(30.dp)
                        .clickable { handleDisconnect() }
                )
                Text(
                    text = "Ready, Set, Connect",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = -25.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Connected Status Icon
            Image(
                painter = painterResource(id = R.drawable.connected_plug_icon),
                contentDescription = "Connected",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = -50.dp)
            )

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )

            Text(
                text = "Connected!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )

            // Disconnect button
            Button(
                onClick = { handleDisconnect() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp)),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Disconnect")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedPagePreview() {
    MovLeTheme {
        ConnectedPage(
            userName = "Juan Dela Cruz",
            onBackClick = {}
        )
    }
}
