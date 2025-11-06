package com.judgement

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.judgement.ui.users.LoginScreen
import com.judgement.ui.users.ProfileScreen
import com.judgement.ui.users.SignUpScreen
import com.judgement.ui.users.AuthViewModel
import com.judgement.ui.components.Header
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import com.judgement.data.local.Users
import com.judgement.ui.users.EditProfileScreen
import com.judgement.ui.users.UsersViewModel


data class Route(val name: String, val route: String, val icon: ImageVector)

@Composable
fun AppNavigation(currentUser: Users?, authViewModel: AuthViewModel, usersViewModel: UsersViewModel) {
    val navController = rememberNavController()
    val context =   LocalContext.current

    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableIntStateOf(0) }
    var lastSelectedItem by remember { mutableIntStateOf(0) }

    val routes = listOf(
        Route("Home", "home/", Icons.Filled.Home),
        Route("Profile", "profile/", Icons.Filled.Person),
        Route("Bank Statement", "statement/", Icons.Filled.Settings),
        Route("Transaction", "transaction/", Icons.Filled.Info)
    )

    NavHost(navController = navController, startDestination = "login") {

        // ========================= LOGIN =========================
        composable("login") {
            Header(title = "Login")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    LoginScreen (onLogin = {
                        scope.launch {
                            navController.navigate("home/${usersViewModel.onGetCurrentUser(authViewModel.uiState.value.sessionId!!)?.id.toString()}") {
                                popUpTo("login") { inclusive = true }
                            }
                            Toast.makeText(context, "Logged Successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }, onNavigateRegister = {
                        navController.navigate("register")
                    },
                        authViewModel = authViewModel,
                        usersViewModel = usersViewModel
                    )
                }
            }
        }

        // ========================= REGISTER =========================
        composable("register") {
            Header(title = "Create Your Account")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    SignUpScreen(
                        authViewModel = authViewModel,
                        onNavigateToLogin = {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        // ========================= HOME =========================
        composable("home/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            LaunchedEffect(param) {
                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Home",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

//                    currentUser?.let { HomeScreen(it) }
                }
            }
        }

        // ========================= PROFILE =========================
        composable("profile/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    currentUser?.let { user ->
//                        ProfileScreen(
//                            authViewModel = authViewModel,
//                            user = auth.currentUser!!,
//                            onSignOut = {
//                                navController.navigate("login") {
//                                    popUpTo(navController.graph.id) { inclusive = true }
//                                }
//                            }
//                        )
                    }
                }
            }
        }

        // ========================= STATEMENT =========================
        composable("statement/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

//                    currentUser?.let { StatementScreen(user = it) }
                }
            }
        }

        // ========================= TRANSACTION =========================
        composable("transaction/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

//                    currentUser?.let { TransactionScreen(currentUser = it) }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    routes: List<Route>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(100.dp)
    ) {
        routes.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.name) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.DarkGray,
                    unselectedIconColor = Color.DarkGray,
                    unselectedTextColor = Color.DarkGray,
                    indicatorColor = Color(0xFF1976D2)
                )
            )
        }
    }
}




class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private var currentUser : Users? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authViewModel.uiState.value.sessionId != null) {
            currentUser = usersViewModel.onGetCurrentUser(authViewModel.uiState.value.sessionId!!)
        }

        setContent {
            enableEdgeToEdge()
            AppNavigation(currentUser, authViewModel, usersViewModel)
        }
    }
}

//    @Composable
//    fun AppNavigation(authViewModel: AuthViewModel = AuthViewModel()) {
//        val navController = rememberNavController()
//        val sessionId = authViewModel.uiState.collectAsState().value.sessionId
//        val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
//        val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle()
//
//        LaunchedEffect(feedbackMsg) {
//            feedbackMsg?.let {
//                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
//                authViewModel.clearFeedback()
//            }
//        }

//        NavHost(
//            navController = navController,
//            startDestination = if (currentUser != null) "profile" else "login"
//        ) {
//            composable("signup") {
//                SignUpScreen(
//                    authViewModel = authViewModel,
//                    onNavigateToLogin = {
//                        navController.navigate("login") {
//                            popUpTo("signup") { inclusive = true }
//                        }
//                    }
//                )
//            }

//            composable("login") {
//                LoginScreen(
//                    onLogin = {}
//                    authViewModel = authViewModel,
//                    onNavigateToSignUp = { navController.navigate("signup") }
//                )
//            }

//            composable("profile") {
//                if (auth.currentUser != null) {
//                    ProfileScreen(
//                        authViewModel = authViewModel,
//                        user = auth.currentUser!!,
//                        onSignOut = {
//                            auth.signOut()
//                            navController.navigate("login") {
//                                popUpTo(navController.graph.id) { inclusive = true }
//                            }
//                        }
//                    )
//                } else {
//                    LaunchedEffect(Unit) {
//                        navController.navigate("login") {
//                            popUpTo(navController.graph.id) { inclusive = true }
//                        }
//                    }
//                }
//            }
//        }

//        if (isLoading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//
//}

