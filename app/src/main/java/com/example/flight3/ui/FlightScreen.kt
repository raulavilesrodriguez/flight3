package com.example.flight3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flight3.FlightTopAppBar
import com.example.flight3.InputForm
import com.example.flight3.R
import com.example.flight3.data.Airport
import com.example.flight3.ui.navigation.NavigationDestination

object FlightDestination : NavigationDestination {
    override val route = "flights_details"
    override val titleRes = R.string.flight_routes
    const val flightIdArg = "flightId"
    val routeWithArgs = "$route/{$flightIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModelRoutes: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelAirports: AirportViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val routesUiState by viewModelRoutes.routesUiState.collectAsState()
    val airChosenUiState by viewModelRoutes.airChosenUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightTopAppBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) {innerPadding ->
        RoutesBody(
            navigateBack = navigateBack,
            routesList = routesUiState.airportsRoutes,
            onValueChange = viewModelRoutes::updateUiState,
            nameValue = airChosenUiState.iataCode,
            nameWritten = viewModelRoutes.flyUiState.iataCode,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}


@Composable
private fun RoutesBody(
    navigateBack: () -> Unit,
    routesList: List<RoutesUiState>,
    onValueChange: (String) -> Unit,
    nameValue: String,
    nameWritten: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        InputForm(
            partName = nameWritten,
            modifier = Modifier
                .padding(
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = contentPadding.calculateTopPadding()
                )
                .fillMaxWidth(),
            onValueChange = onValueChange
        )
        Text(text = "El valor seteado:${nameWritten}")
        Text(text = "El de la ruta: ${nameValue}")

    }
}

@Composable
private fun RoutesList(
    routes: List<RoutesUiState>,
    addFavorites: (Int) -> Unit,
    deleteFavorite: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = routes, key = {it.destinationCode}){
            RouteItem(
                route = it,
                addFavorites = addFavorites,
                deleteFavorite = deleteFavorite,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun RouteItem(
    route: RoutesUiState,
    addFavorites: (Int) -> Unit,
    deleteFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .sizeIn(minHeight = 52.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.depart),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_small))
                )
                Row(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = route.departureCode,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                    Text(
                        text = route.nameDeparture,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                }
                Text(
                    text = stringResource(id = R.string.arrive),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_small))
                )
                Row(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = route.destinationCode,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                    Text(
                        text = route.nameDestination,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxHeight()

            ) {
                if(route.inFavorite == "true"){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_star_white),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { }
                    )
                }

            }
        }
    }
}