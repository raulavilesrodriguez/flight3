package com.example.flight3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flight3.ui.AirportDestination
import com.example.flight3.ui.AirportScreen
import com.example.flight3.ui.FlightDestination
import com.example.flight3.ui.FlightScreen

@Composable
fun FlightNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = AirportDestination.route,
        modifier = modifier
    ){
        composable(route = AirportDestination.route){
            AirportScreen(
                navigateToFlights = {
                    navController.navigate("${FlightDestination.route}/${it}")
                }
            )
        }
        composable(
            route = FlightDestination.routeWithArgs,
            arguments = listOf(navArgument(FlightDestination.flightIdArg){
                type = NavType.IntType
            })
        ){
            FlightScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}