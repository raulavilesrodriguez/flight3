package com.example.flight3.data

import com.example.flight3.ui.FavoritesUiState
import com.example.flight3.ui.RoutesUiState
import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightDao: FlightDao): FlightRepository {
    override fun getAirportsStream(partName: String): Flow<List<Airport>> = flightDao.getAirports(partName)

    override fun getAirportStream(id: Int): Flow<Airport?> = flightDao.getAirport(id)

    override fun getFlightsStream(id: Int): Flow<List<RoutesUiState>?> = flightDao.getFlights(id)

    override suspend fun insertFavorite(favorite: Favorite) = flightDao.insertFavorite(favorite)

    override suspend fun deleteFavorite(departure: String, destination:String) = flightDao.deleteFavorite(departure, destination)

    override fun getFavoritesStream(): Flow<List<FavoritesUiState>> = flightDao.getFavorites()
}