package com.example.pokemonproject.data.Room.DAO

import androidx.room.*
import com.example.pokemonproject.data.Room.Entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Query("SELECT * FROM teams")
    fun getAllTeams(): Flow<List<TeamEntity>>

    @Delete
    suspend fun deleteTeam(team: TeamEntity)
}


