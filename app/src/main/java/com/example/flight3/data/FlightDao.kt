package com.example.flight3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flight3.ui.FavoritesUiState
import com.example.flight3.ui.RoutesUiState
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Query("SELECT * FROM airport WHERE name LIKE '%' || :partName || '%' OR iata_code LIKE '%' || :partName || '%'")
    fun getAirports(partName: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE id = :id")
    fun getAirport(id: Int): Flow<Airport>

    @Query("SELECT " +
            "    ad.name AS nameDeparture, " +
            "    ad.iata_code AS departureCode, " +
            "    aa.name AS nameDestination, " +
            "    aa.iata_code AS destinationCode, " +
            "    CASE " +
            "        WHEN f.destination_code = aa.iata_code AND f.departure_code = ad.iata_code THEN 'true' " +
            "        ELSE 'false' " +
            "    END AS inFavorite, " +
            "    CASE " +
            "        WHEN f.destination_code = aa.iata_code AND f.departure_code = ad.iata_code THEN f.id " +
            "        ELSE 0 " +
            "    END AS idFavorite " +
            "FROM " +
            "    airport ad " +
            "INNER JOIN " +
            "    airport aa ON aa.id != :id " +
            "LEFT JOIN " +
            "    favorite f ON f.destination_code = aa.iata_code AND f.departure_code = ad.iata_code " +
            "WHERE " +
            "    ad.id = :id " +
            "ORDER BY " +
            "    aa.passengers DESC")
    fun getFlights(id: Int): Flow<List<RoutesUiState>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT f.id AS id,\n" +
            "       ad.name AS nameDeparture,\n" +
            "       aa.name AS nameDestination,\n" +
            "       f.departure_code AS departureCode,\n" +
            "       f.destination_code AS destinationCode\n" +
            "  FROM favorite f\n" +
            "       INNER JOIN\n" +
            "       airport ad ON f.departure_code = ad.iata_code\n" +
            "       INNER JOIN\n" +
            "       airport aa ON f.destination_code = aa.iata_code")
    fun getFavorites(): Flow<List<FavoritesUiState>>

}