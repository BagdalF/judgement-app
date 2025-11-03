package com.judgement

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.empresa.projetofirebase.ui.screens.SignUpScreen
import com.empresa.projetofirebase.viewmodel.AuthViewModel
import com.judgement.ui.users.LoginScreen
import com.judgement.ui.users.ProfileScreen
import com.judgement.ui.users.SignUpScreen
import com.judgement.ui.users.AuthViewModel
import com.judgement.ui.components.Header
import kotlinx.coroutines.launch


class BankingAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

data class Route(val name: String, val route: String, val icon: ImageVector)

@Composable
fun AppNavigation() {
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
                    LoginScreen (onLogin = { userCheck, errorMessage ->
                        scope.launch {
                            if (userCheck != null) {
                                prefs.user = userCheck
                                prefs.isLogged = true
                                currentUser = userCheck
                                navController.navigate("home/${userCheck.id}") {
                                    popUpTo("login") { inclusive = true }
                                }
                                Toast.makeText(context, "Logged Successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                errorMessage()
                            }
                        }
                    }, onNavigateRegister = {
                        navController.navigate("register")
                    })
                }
            }
        }

        // ========================= REGISTER =========================
        composable("register") {
            Header(title = "Create Your Account")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    SignUpScreen(
                        authViewModel = AuthViewModel(),
                        onNavigateToLogin = {

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
                    currentUser?.let { HomeScreen(it) }
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
                        EditProfileScreen(profile = user)
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

                    currentUser?.let { StatementScreen(user = it) }
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

                    currentUser?.let { TransactionScreen(currentUser = it) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(authViewModel)
        }
    }

    @Composable
    fun AppNavigation(authViewModel: AuthViewModel = AuthViewModel()) {

        val navController = rememberNavController()

        val user by authViewModel.userState.collectAsStateWithLifecycle()
        val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
        val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle()


        LaunchedEffect(feedbackMsg) {
            feedbackMsg?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                authViewModel.clearFeedback()
            }
        }

        NavHost(
            navController = navController,
            startDestination = if (user != null) "profile" else "login"
        ){

            composable("signup"){
                SignUpScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }

            composable("login"){
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }

            composable("profile"){

                if(user != null){
                    ProfileScreen(
                        authViewModel = authViewModel,
                        user = user!!
                    )
                }else{
                    LaunchedEffect(Unit) {
                        navController.navigate("login"){
                            popUpTo(navController.graph.id){ inclusive = true }
                        }
                    }
                }

            }

        }

        if(isLoading){
            CircularProgressIndicator(modifier = Modifier)
        }

    }

}

