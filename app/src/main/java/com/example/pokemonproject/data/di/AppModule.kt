package com.example.pokemonproject.data.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonproject.data.Room.DAO.TeamDAO
import com.example.pokemonproject.data.Room.ElementDAO
import com.example.pokemonproject.data.Room.PokemonDAO
import com.example.pokemonproject.data.Room.PokemonDatabase
import com.example.pokemonproject.data.network.PokemonApi
import com.example.pokemonproject.data.repository.PokemonRepositoryImpl
import com.example.pokemonproject.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://pokeapi.co/api/v2/" // API Base URL for Pokémon data

    // Provides a Retrofit instance for API calls
    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApi::class.java)

    // Provides the Pokémon repository, combining API and local database sources
    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokemonApi,
        pokemonDao: PokemonDAO,
        elementDao: ElementDAO
    ): PokemonRepository = PokemonRepositoryImpl(
        pokemonApi = api,
        pokemonDao = pokemonDao,
        elementDao = elementDao
    )

    // Provides a Room database instance for local data storage
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_database" // Database name
        ).fallbackToDestructiveMigration() // Handle migrations for simplicity
            .build()

    // Provides the DAO for Pokémon-related database operations
    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDAO = database.pokemonDao()

    // Provides the DAO for Element-related database operations
    @Provides
    fun provideElementDao(database: PokemonDatabase): ElementDAO = database.elementDao()

    @Provides
    fun provideTeamDao(database: PokemonDatabase): TeamDAO = database.teamDao()
}
