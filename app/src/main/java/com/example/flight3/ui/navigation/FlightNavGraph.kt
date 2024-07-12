package com.example.flight3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flight3.ui.AirportDestination
import com.example.flight3.ui.AirportScreen
import com.example.flight3.ui.AirportViewModel
import com.example.flight3.ui.AppViewModelProvider
import com.example.flight3.ui.FlightDestination
import com.example.flight3.ui.FlightScreen

@Composable
fun FlightNavHost(
    navController: NavHostController,
    viewModel: AirportViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
                },
                viewModel = viewModel
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
                    resetAirportsView(viewModel, navController)
                }
            )
        }
    }
}

/**
 * Reset the [nameUiState] and pops up
 */
private fun resetAirportsView(
    viewModel: AirportViewModel,
    navController: NavHostController
){
    viewModel.resetInputView()
    navController.popBackStack(AirportDestination.route, inclusive = false)
}