package com.example.flight3.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flight3.R
import com.example.flight3.ui.navigation.NavigationDestination

object FlightDestination : NavigationDestination {
    override val route = "flights_details"
    override val titleRes = R.string.flight_routes
    const val flightIdArg = "flightId"
    val routeWithArgs = "$route/{$flightIdArg}"
}

@Composable
fun FlightScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
){

}