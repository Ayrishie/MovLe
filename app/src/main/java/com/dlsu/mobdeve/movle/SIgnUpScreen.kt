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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUp: (email: String, fullName: String, username: String, password: String) -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light grey background
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background), // Use your box image resource
            contentDescription = "Box Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(8.dp, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly // Evenly space the elements
        ) {
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
                        .size(30.dp) // Adjust size as needed
                        .clickable { onBackClick() }
                )
                Text(
                    text = "GET",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = 10.dp) // Adjust y-offset as needed
                        .offset(x = 80.dp) // Adjust x-offset as needed
                )
                Text(
                    text = "STARTED",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = -76.dp) // Adjust y-offset as needed
                        .offset(x = 80.dp) // Adjust x-offset as needed
                )
            }

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White, // White background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp))
            )

            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White, // White background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp))
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White, // White background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp))
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White, // White background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp))
            )

            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Repeat Password") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White, // White background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    placeholderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Log.d("SignUpScreen", "Create Account button clicked")
                    if (password == repeatPassword) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign up successful, save additional user data to Firestore
                                    val userId = auth.currentUser?.uid
                                    val user = hashMapOf(
                                        "fullName" to fullName,
                                        "username" to username,
                                        "email" to email
                                    )
                                    if (userId != null) {
                                        db.collection("users").document(userId)
                                            .set(user)
                                            .addOnSuccessListener {
                                                // Store the username to email mapping
                                                db.collection("usernames").document(username)
                                                    .set(mapOf("email" to email))
                                                    .addOnSuccessListener {
                                                        Log.d("SignUpScreen", "User data added successfully")
                                                        onSignUp(email, fullName, username, password)
                                                    }
                                                    .addOnFailureListener { e ->
                                                        errorMessage = e.message
                                                        Log.w("SignUpScreen", "Error adding username mapping", e)
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                errorMessage = e.message
                                                Log.w("SignUpScreen", "Error adding user data", e)
                                            }
                                    }
                                } else {
                                    // Sign up failed
                                    errorMessage = task.exception?.message
                                    Log.w("SignUpScreen", "Error creating user", task.exception)
                                }
                            }
                    } else {
                        errorMessage = "Passwords do not match"
                        Log.w("SignUpScreen", "Passwords do not match")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp)), // Added shadow
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // Red background color
            ) {
                Text("Create Account")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    MovLeTheme {
        SignUpScreen(
            onSignUp = { email, fullName, username, password -> /* Handle sign up */ },
            onBackClick = { /* Handle back click */ }
        )
    }
}
