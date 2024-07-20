package com.dlsu.mobdeve.movle

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
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()//irish

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light grey background
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.forgotbackground), // Use your background image resource
            contentDescription = "Background",
            contentScale = ContentScale.Fit, // Adjust contentScale to Fit to see if it resolves the issue
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
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
                        .offset(y = (-30).dp)
                        .padding(16.dp)
                        .size(30.dp) // Adjust size as needed
                        .clickable { onBackClick() }
                )
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = 10.dp) // Adjust y-offset as needed
                        .offset(x = 80.dp) // Adjust x-offset as needed
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_forgotpass), // Use your key icon resource
                contentDescription = "Key Icon",
                modifier = Modifier.size(150.dp) // Adjust size as needed
            )

            Text(
                text = "Trouble logging in?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )

            Text(
                text = "Enter your email and we'll send you a link to reset your password.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                successMessage = "Password reset link sent to your email."
                                errorMessage = null
                            } else {
                                errorMessage = task.exception?.message
                                successMessage = null
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp)), // Added shadow
                shape = RoundedCornerShape(50.dp), // Rounded corners
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // Red background color
            ) {
                Text("Reset Password")
            }

            successMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = Color.Green)
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
fun ForgotPasswordScreenPreview() {
    MovLeTheme {
        ForgotPasswordScreen(onBackClick = { /* Handle back click */ })
    }
}
