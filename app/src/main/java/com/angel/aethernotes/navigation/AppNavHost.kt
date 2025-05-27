package com.angel.aethernotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angel.aethernotes.pages.HomeScreen
import com.angel.aethernotes.pages.LoginScreen
import com.angel.aethernotes.pages.SignUpScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController:NavHostController = rememberNavController(),
    startDestination:String = LOGIN_URL
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier){
        composable(LOGIN_URL){
            LoginScreen(navController = navController)
        }
        composable(SIGNUP_URL){
            SignUpScreen(navController = navController)
        }
        composable(HOME_URL){
            HomeScreen(navController = navController)
        }
        composable(ADD_NOTES_URL){
            AddProductsScreen(navController = navController)
        }
        composable(VIEW_NOTES_URL){
            ViewProductsScreen(navController = navController)
        }
    }
}