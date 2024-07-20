package com.dlsu.mobdeve.movle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.dlsu.mobdeve.movle.ui.theme.MovLeTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val db = Firebase.firestore // Define the Firestore instance
    private lateinit var auth: FirebaseAuth // Define Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            FirebaseApp.initializeApp(this)
            auth = FirebaseAuth.getInstance()
            Log.d("FirebaseInit", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Firebase initialization failed", e)
        }

        setContent {
            MovLeTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var connectedUserName by remember { mutableStateOf("") }
                var currentUser by remember { mutableStateOf(auth.currentUser) }
                var currentUserName by remember { mutableStateOf("") }
                var currentUserId by remember { mutableStateOf("") }

                LaunchedEffect(currentUser) {
                    currentUser?.let { user ->
                        db.collection("users").document(user.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    currentUserName = document.getString("username") ?: ""
                                    currentUserId = user.uid
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("MainActivity", "Failed to retrieve user data", e)
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
                        onReceiveCodeClick = { otherUserName ->
                            connectedUserName = otherUserName
                            currentScreen = Screen.Connected(connectedUserName)
                        },
                        currentUserId = currentUserId, // Pass current user ID
                        currentUserName = currentUserName // Pass current user name
                    )
                    is Screen.Connected -> ConnectedPage(
                        userName = connectedUserName,
                        onBackClick = { currentScreen = Screen.Connect },
                        onDisconnectClick = {
                            // Handle disconnect logic here
                            db.collection("connections").document("current_connection")
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("MainActivity", "Connection status deleted")
                                    currentScreen = Screen.Connect
                                }
                                .addOnFailureListener { e ->
                                    Log.e("MainActivity", "Failed to delete connection status", e)
                                }
                        }
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
