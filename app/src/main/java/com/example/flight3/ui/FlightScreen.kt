package com.example.flight3.ui

import android.util.Log
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flight3.FlightTopAppBar
import com.example.flight3.InputForm
import com.example.flight3.R
import com.example.flight3.ui.navigation.NavigationDestination
import com.example.flight3.ui.theme.Flight3Theme
import kotlinx.coroutines.launch

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
    viewModelFlight: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory),

){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val routesUiState by viewModelFlight.routesUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightTopAppBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) {innerPadding ->
        Log.d("FlightScreen", "Routes: ${routesUiState.airportsRoutes}")
        RoutesBody(
            navigateBack = {
                           coroutineScope.launch {
                               navigateBack()
                           }
            },
            routesList = routesUiState.airportsRoutes,
            onValueChange = viewModelFlight::updateUiState,
            nameWritten = viewModelFlight.flyUiState.iataCode,
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
    nameWritten: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    var isInitialized by rememberSaveable{ mutableStateOf(false) }

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
            onValueChange = {
                onValueChange(it)
                if(it.isEmpty() && isInitialized){
                    navigateBack()
                }
                isInitialized = true
            }
        )
        Text(text = stringResource(id = R.string.flight_routes, nameWritten),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        if (routesList.isNotEmpty()) {
            Log.d("RoutesBody", "Routes List: $routesList")
            RoutesList(
                routes = routesList,
                addFavorites = {},
                deleteFavorite = {},
                contentPadding = contentPadding
            )
        } else {
            Text(text = stringResource(id = R.string.warning_routes))
        }
    }
}

@Composable
private fun RoutesList(
    routes: List<RoutesUiState>,
    addFavorites: () -> Unit,
    deleteFavorite: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = routes, key = {it.destinationCode}){
            Log.d("RoutesList", "Route Item: $it")
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
private fun Veamos(
    route: RoutesUiState,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
      Text(
          text = route.destinationCode,
          modifier = Modifier
              .padding(dimensionResource(id = R.dimen.padding_small))
      )
      Text(
          text = route.nameDestination,
          modifier = Modifier
              .padding(dimensionResource(id = R.dimen.padding_small))
      )
    }
}
@Composable
private fun RouteItem(
    route: RoutesUiState,
    addFavorites: () -> Unit,
    deleteFavorite: () -> Unit,
    modifier: Modifier = Modifier
){
    var addOrDelete by rememberSaveable { mutableStateOf(route.inFavorite == "true") }
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
                if(addOrDelete){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable {

                                addOrDelete = false
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_star_grey),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable {

                                addOrDelete = true
                            }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RouteItemPreview(){
    Flight3Theme{
        Surface {
            RouteItem(
                route = RoutesUiState(
                    "Leonardo da Vinci International Airport",
                    "FCO",
                    "Humbuerto Delgado Airport",
                    "LIS",
                    "true"
                    ),
                addFavorites = {},
                deleteFavorite = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutesListPreview(){
    Flight3Theme{
        Surface {
            RoutesList(
                routes = listOf(
                    RoutesUiState("Francisco Carneiro Airport", "OPO", "Warsaw Chopin Airport","WAW", "false"),
                    RoutesUiState("Francisco Carneiro Airport", "OPO", "Munich International Airport","MUC", "true"),
                    RoutesUiState("Francisco Carneiro Airport", "OPO", "Dublin Airport","DUB", "false"),
                    RoutesUiState("Francisco Carneiro Airport", "OPO", "Humberto Delgado Airport","LIS", "true")
                ),
                addFavorites = {},
                deleteFavorite = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}
