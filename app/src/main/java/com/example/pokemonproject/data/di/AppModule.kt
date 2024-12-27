package com.example.pokemonproject.data.di

import android.content.Context
import androidx.room.Room
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

    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    @Provides
    @Singleton
    fun pokemonPokemonApi(): PokemonApi {
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokemonApi,
        pokemonDao: PokemonDAO,
        elementDao: ElementDAO
    ): PokemonRepository =
        PokemonRepositoryImpl(
            pokemonApi = api,
            pokemonDao = pokemonDao,
            elementDao = elementDao
        )

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDAO {
        return database.pokemonDao()
    }
    @Provides
    fun provideElementDao(database: PokemonDatabase): ElementDAO {
        return database.elementDao()
    }

}
