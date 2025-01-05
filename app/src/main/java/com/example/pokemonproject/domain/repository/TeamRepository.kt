package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.data.Room.DAO.TeamDAO
import com.example.pokemonproject.data.Room.Entity.TeamEntity
import javax.inject.Inject
import javax.inject.Singleton

class TeamRepository @Inject constructor(
    private val teamDao: TeamDAO
) {
    suspend fun getAllTeams() = teamDao.getAllTeams()

    suspend fun insertTeam(team: TeamEntity) {
        teamDao.insertTeam(team)
    }

    suspend fun deleteTeam(team: TeamEntity) {
        teamDao.deleteTeam(team)
    }
}



