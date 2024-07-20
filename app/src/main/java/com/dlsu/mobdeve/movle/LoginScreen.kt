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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onConnectClick: () -> Unit // Add this parameter
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8B0000)) // Adjusted background color to match the design
    ) {
        // Lights background image
        Image(
            painter = painterResource(id = R.drawable.lights_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        )

        // Popcorn background image, placed at the bottom
        Image(
            painter = painterResource(id = R.drawable.popcorn_login),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1000.dp) // Adjust height as needed
                .offset(y = 200.dp) // Adjust offset as needed to move it down
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.movle_logo), // Use your logo image resource
                contentDescription = "Logo",
                modifier = Modifier.size(350.dp) // Adjusted logo size
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)) // Add shadow to the card
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user), // Your user icon
                                contentDescription = null,
                                modifier = Modifier.size(24.dp) // Adjusted icon size
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(50.dp)),
                        shape = RoundedCornerShape(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_password), // Your password icon
                                contentDescription = null,
                                modifier = Modifier.size(24.dp) // Adjusted icon size
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(50.dp)),
                        shape = RoundedCornerShape(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("Remember me")
                        }
                        Text(
                            text = "Forgot password?",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                onForgotPasswordClick()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Button for login
                    Button(
                        onClick = {
                            Log.d("LoginScreen", "Login button clicked")
                            db.collection("users")
                                .whereEqualTo("username", username)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        Log.d("LoginScreen", "Firestore document retrieved successfully")
                                        for (document in documents) {
                                            val email = document.getString("email")
                                            if (email != null) {
                                                Log.d("LoginScreen", "Email retrieved: $email")
                                                loginViewModel.login(email, password, {
                                                    Log.d("LoginScreen", "Login successful")
                                                    // Handle successful login
                                                    onConnectClick()
                                                }) { error ->
                                                    Log.e("LoginScreen", "Login failed: $error")
                                                    errorMessage = error
                                                }
                                            } else {
                                                errorMessage = "Username does not exist"
                                                Log.e("LoginScreen", "Username does not have an associated email")
                                            }
                                        }
                                    } else {
                                        errorMessage = "Username does not exist"
                                        Log.e("LoginScreen", "Username does not exist in Firestore")
                                    }
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = e.message
                                    Log.e("LoginScreen", "Firestore retrieval failed", e)
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(50.dp)), // Add shadow to the button
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)) // Adjusted button color
                    ) {
                        Text("Login")
                    }

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Don't have an account?")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Create an account",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                onCreateAccountClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleLoginScreenPreview() {
    MovLeTheme {
        LoginScreen(
            onCreateAccountClick = {},
            onForgotPasswordClick = {},
            onConnectClick = {} // Add this parameter in the preview
        )
    }
}
