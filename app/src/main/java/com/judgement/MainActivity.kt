package com.judgement

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.judgement.ui.users.LoginView
import com.judgement.ui.users.RegisterView
import com.judgement.ui.users.AuthViewModel
import com.judgement.ui.components.Header
import kotlinx.coroutines.launch
import com.judgement.data.local.AppDatabase
import com.judgement.data.local.Users
import com.judgement.data.repository.CasesRepository
import com.judgement.data.repository.DialogsRepository
import com.judgement.data.repository.PersonsRepository
import com.judgement.data.repository.UsersRepository
import com.judgement.data.repository.VerdictsRepository
import com.judgement.ui.cases.CasesViewModel
import com.judgement.ui.cases.CasesViewModelFactory
import com.judgement.ui.cases.NewCaseView
import com.judgement.ui.components.HomeView
import com.judgement.ui.components.NavBar
import com.judgement.ui.dialogs.DialogsView
import com.judgement.ui.dialogs.DialogsViewModel
import com.judgement.ui.dialogs.DialogsViewModelFactory
import com.judgement.ui.persons.PersonsView
import com.judgement.ui.persons.PersonsViewModel
import com.judgement.ui.persons.PersonsViewModelFactory
import com.judgement.ui.theme.JudgementTheme
import com.judgement.ui.users.AuthViewModelFactory
import com.judgement.ui.users.ProfileView
import com.judgement.ui.users.UsersView
import com.judgement.ui.users.UsersViewModel
import com.judgement.ui.users.UsersViewModelFactory
import com.judgement.ui.verdicts.NewVerdictView
import com.judgement.ui.verdicts.VerdictsView
import com.judgement.ui.verdicts.VerdictsViewModel
import com.judgement.ui.verdicts.VerdictsViewModelFactory


class MainActivity : ComponentActivity() {
    private val casesViewModel: CasesViewModel by viewModels {
        CasesViewModelFactory(
            CasesRepository(
                AppDatabase.getDatabase(applicationContext).casesDAO()
            )
        )
    }
    private val dialogsViewModel: DialogsViewModel by viewModels {
        DialogsViewModelFactory(
            DialogsRepository(
                AppDatabase.getDatabase(applicationContext).dialogsDAO()
            )
        )
    }
    private val personsViewModel: PersonsViewModel by viewModels {
        PersonsViewModelFactory(
            PersonsRepository(
                AppDatabase.getDatabase(applicationContext).personsDAO()
            )
        )
    }
    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory(
            UsersRepository(
                AppDatabase.getDatabase(applicationContext).usersDAO()
            )
        )
    }
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }
    private val verdictsViewModel: VerdictsViewModel by viewModels {
        VerdictsViewModelFactory(
            VerdictsRepository(AppDatabase.getDatabase(applicationContext).verdictsDAO()),
            CasesRepository(AppDatabase.getDatabase(applicationContext).casesDAO()),
            PersonsRepository(AppDatabase.getDatabase(applicationContext).personsDAO())
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()

            JudgementTheme {
                AppNavigation(
                    casesViewModel = casesViewModel,
                    dialogsViewModel = dialogsViewModel,
                    personsViewModel = personsViewModel,
                    usersViewModel =  usersViewModel,
                    authViewModel =  authViewModel,
                    verdictsViewModel = verdictsViewModel
                )
            }
        }
    }
}

var currentUser : Users? = null

data class Route(val name: String, val route: String, val icon: ImageVector)

@Composable
fun AppNavigation(
    casesViewModel: CasesViewModel,
    dialogsViewModel: DialogsViewModel,
    personsViewModel: PersonsViewModel,
    usersViewModel: UsersViewModel,
    authViewModel: AuthViewModel,
    verdictsViewModel: VerdictsViewModel
) {
    val navController = rememberNavController()
    val context =   LocalContext.current


    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableIntStateOf(1) }
    var lastSelectedItem by remember { mutableIntStateOf(1) }

    val routes = listOf(
        Route("Verdicts", "verdicts/", Icons.Filled.Approval),
        Route("Home", "home/", Icons.Filled.Home),
        Route("Profile", "profile/", Icons.Filled.AccountCircle)
    )

    NavHost(navController = navController, startDestination = "login") {

        // ========================= LOGIN =========================
        composable("login") {
            Header(title = "Login")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    LoginView (onLogin = { email, senha ->
                        scope.launch {
                            try {
                                authViewModel.login(email = email, senha = senha, userViewModel = usersViewModel)
                                
                                // Only navigate if we have a current user
                                if (currentUser != null) {
                                    navController.navigate("home/${currentUser!!.id}") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to load user profile", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                            }
                        }
                    }, onNavigateRegister = {
                        navController.navigate("register")
                    },
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
                    RegisterView(
                        authViewModel = authViewModel,
                        userViewModel = usersViewModel,
                        onNavigateLogin = {
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
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + param)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    if (currentUser!!.isAdmin) {
                        HomeView(
                            currentUser = currentUser!!,
                            onNewCase = { navController.navigate("new_case") },
                            onManageUsers = { navController.navigate("users") },
                            onManagePersons = { navController.navigate("persons") },
                            onManageDialogs = { navController.navigate("dialogs") }
                        )
                    } else {
                        HomeView(
                            currentUser = currentUser!!,
                            onNewCase = { navController.navigate("new_case") }
                        )
                    }
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
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + param)
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
                    ProfileView(
                        profile = currentUser!!,
                        authViewModel = authViewModel,
                        userViewModel = usersViewModel,
                        onSignOut = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }



        // ========================= NEW CASE =========================
        composable("new_case") { backstackEntry ->
            LaunchedEffect(currentUser) {
                if (currentUser == null)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "New Case",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    NewCaseView(
                        caseViewModel = casesViewModel,
                        personsViewModel = personsViewModel,
                        onAcceptCase = { case ->
                            casesViewModel.onSalvar()
                            navController.navigate("new_verdict")
                        },
                        onRejectCase = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }



        // ========================= VERDICTS =========================
        composable("verdicts/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(currentUser) {
                if (currentUser == null)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }

                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }

                if (!currentUser!!.isAdmin) {
                    navController.navigate("home/${currentUser!!.id}") { popUpTo("home/${currentUser!!.id}") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
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

                    VerdictsView(verdictViewModel = verdictsViewModel)
                }
            }
        }

        // ========================= PERSONS =========================
        composable("persons") { backstackEntry ->
            LaunchedEffect(currentUser) {
                    if (currentUser == null)
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }

                if (!currentUser!!.isAdmin) {
                    navController.navigate("home/${currentUser!!.id}") { popUpTo("home/${currentUser!!.id}") { inclusive = true } }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Manage Persons",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    PersonsView(personViewModel = personsViewModel)
                }
            }
        }

        // ========================= DIALOGS =========================
        composable("dialogs") { backstackEntry ->
            LaunchedEffect(currentUser) {
                    if (currentUser == null)
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }

                if (!currentUser!!.isAdmin) {
                    navController.navigate("home/${currentUser!!.id}") {
                        popUpTo("home/${currentUser!!.id}") {
                            inclusive = true
                        }
                    }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Manage Dialogs",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    DialogsView(dialogViewModel = dialogsViewModel)
                }
            }
        }

        // ========================= USERS =========================
        composable("users") { backstackEntry ->
            LaunchedEffect(currentUser) {
                    if (currentUser == null)
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }

                if (!currentUser!!.isAdmin) {
                    navController.navigate("home/${currentUser!!.id}") {
                        popUpTo("home/${currentUser!!.id}") {
                            inclusive = true
                        }
                    }
                    return@LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Manage Users",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    UsersView(userViewModel = usersViewModel)
                }
            }
        }

        // ========================= NEW VERDICT =========================
        composable("new_verdict") {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id)
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Deliver Verdict",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    NewVerdictView(
                        casesViewModel = casesViewModel,
                        dialogsViewModel = dialogsViewModel,
                        verdictsViewModel = verdictsViewModel,
                        onVerdictDelivered = {
                            casesViewModel.clearCurrentCase()
                            navController.navigate("home/${currentUser!!.id}") {
                                popUpTo("new_verdict") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

    }
}