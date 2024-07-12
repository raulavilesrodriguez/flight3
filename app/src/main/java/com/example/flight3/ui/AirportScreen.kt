package com.example.flight3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.flight3.data.Airport
import com.example.flight3.ui.navigation.NavigationDestination
import com.example.flight3.ui.theme.Flight3Theme
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

object AirportDestination : NavigationDestination {
    override val route = "airports"
    override val titleRes = R.string.airports
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportScreen(
    navigateToFlights: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AirportViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val nameUiState by viewModel.nameUiState.collectAsState()
    val airportUiState by viewModel.airportUiState.collectAsState()
    val favoritesUiState by viewModel.favoritesUiState.collectAsState()
    val userTextsUiState by viewModel.userTextsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightTopAppBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) {innerPadding ->
        AirportBody(
            airportList = airportUiState.airportList,
            onValueChange = viewModel::updateUiState,
            nameValue = nameUiState.partName,
            onAirportClick = navigateToFlights,
            favoriteList = favoritesUiState.favorites,
            deleteFavorites = {departure, destination ->
                coroutineScope.launch {
                    viewModel.deleteFavorite(departure, destination)
                }
            },
            userTextList =userTextsUiState.filteredText,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun AirportBody(
    airportList: List<Airport>,
    onValueChange: (String) -> Unit,
    nameValue: String,
    onAirportClick: (Int) -> Unit,
    favoriteList: List<FavoritesUiState>,
    deleteFavorites: (String, String) -> Unit,
    userTextList: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        InputForm(
            partName = nameValue,
            modifier = Modifier
                .padding(
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = contentPadding.calculateTopPadding()
                )
                .fillMaxWidth(),
            onValueChange = onValueChange
        )

        if(nameValue != "") {
            Box(
                modifier = modifier
            ){
                AirportList(
                    airportList = airportList,
                    onAirportClick = {
                        onAirportClick(it.id)
                                     },
                    contentPadding = contentPadding
                )
            }
        } else {
            Text(text = "here favorites")
            FavoritesList(
                favorites = favoriteList,
                deleteFavorite = deleteFavorites,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
private fun AirportList(
    airportList: List<Airport>,
    onAirportClick: (Airport) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = airportList, key = {it.id}){
            AirportItem(
                airport = it,
                modifier = Modifier
                    .clickable { onAirportClick(it) }
            )
        }
    }
}

@Composable
private fun AirportItem(
    airport: Airport,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = airport.iataCode,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = airport.name,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        )
    }
}

@Composable
private fun FavoritesList(
    favorites: List<FavoritesUiState>,
    deleteFavorite: (String, String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = favorites, key = {it.id}){
            FavoriteItem(
                favorite = it,
                deleteFavorite = deleteFavorite,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun FavoriteItem(
    favorite: FavoritesUiState,
    deleteFavorite: (String, String) -> Unit,
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
                        text = favorite.departureCode,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                    Text(
                        text = favorite.nameDeparture,
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
                        text = favorite.destinationCode,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                    Text(
                        text = favorite.nameDestination,
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
                Image(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { deleteFavorite(favorite.departureCode, favorite.destinationCode) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AirportListPreview() {
    Flight3Theme {
        Surface {
            AirportList(airportList = listOf(
                Airport(1, "Francisco Carneiro", "OPO", 5053134),
                Airport(2, "Stockholm Arlanda Airport", "ARN", 7494765),
                Airport(3, "Dublin Airport", "DUB", 32907673),
                Airport(4, "Brussels Airport", "BRU", 26360003)
            ),
                onAirportClick = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AirportItemPreview(){
    Flight3Theme {
        Surface {
            AirportItem(airport = Airport(1, "Antonio Jose de Sucre", "MAJ", 1000))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteItemPreview(){
    Flight3Theme {
        Surface {
            FavoriteItem(favorite =
            FavoritesUiState(1,"Guayaquil Airport",
                "Quito Airport","GYE", "UIO"), {_, _ ->})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesListPreview(){
    Flight3Theme {
        Surface {
            FavoritesList(favorites = listOf(
                FavoritesUiState(1, "Francisco Carneiro Airport", "Warsaw Chopin Airport", "OPO", "WAW"),
                FavoritesUiState(2, "Francisco Carneiro Airport", "Dublin Airport", "OPO", "DUB"),
                FavoritesUiState(3, "Leonardo da Vinci International Airport", "Humbuerto Delgado Airport", "FCO", "LIS"),
                FavoritesUiState(5, "Vienna International Airport", "Sofia Airport", "VIE", "SOF")
            ),
                deleteFavorite = {_, _ ->},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}
