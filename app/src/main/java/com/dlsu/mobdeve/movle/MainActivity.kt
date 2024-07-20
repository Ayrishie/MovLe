package com.dlsu.mobdeve.movle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInit", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Firebase initialization failed", e)
        }

        setContent {
            MovLeTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var connectedUserName by remember { mutableStateOf("") }

                // Listen for real-time updates to the connection status
                LaunchedEffect(Unit) {
                    val db = Firebase.firestore
                    db.collection("connections").document("current_connection")
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.e("MainActivity", "Listen failed", e)
                                return@addSnapshotListener
                            }

                            if (snapshot != null && snapshot.exists()) {
                                val status = snapshot.getString("status")
                                val userName = snapshot.getString("userName")
                                if (status == "connected" && userName != null) {
                                    connectedUserName = userName
                                    currentScreen = Screen.Connected(userName)
                                } else if (status == "disconnected") {
                                    currentScreen = Screen.Connect
                                }
                            }
                        }
                }

                when (currentScreen) {
                    is Screen.Login -> LoginScreen(
                        onCreateAccountClick = { currentScreen = Screen.SignUp },
                        onForgotPasswordClick = { currentScreen = Screen.ForgotPassword },
                        onConnectClick = { currentScreen = Screen.Connect } // Connect screen after login
                    )
                    is Screen.SignUp -> SignUpScreen(
                        onSignUp = { email, fullName, username, password ->
                            // Handle sign-up logic here
                            currentScreen = Screen.Login
                        },
                        onBackClick = { currentScreen = Screen.Login }
                    )
                    is Screen.ForgotPassword -> ForgotPasswordScreen(
                        onBackClick = { currentScreen = Screen.Login }
                    )
                    is Screen.Connect -> ConnectPage(
                        onBackClick = { currentScreen = Screen.Login },
                        onSendCodeClick = {
                            // Handle send code logic here
                            // Example: currentScreen = Screen.Connected(userName)
                        },
                        onReceiveCodeClick = { userName ->
                            // Handle receive code logic here
                            connectedUserName = userName
                            currentScreen = Screen.Connected(userName)
                        }
                    )
                    is Screen.Connected -> ConnectedPage(
                        userName = connectedUserName,
                        onBackClick = { currentScreen = Screen.Connect }
                    )
                }
            }
        }
    }
}

// Sealed class to represent different screens
sealed class Screen {
    object Login : Screen()
    object SignUp : Screen()
    object ForgotPassword : Screen()
    object Connect : Screen()
    data class Connected(val userName: String) : Screen()
}
