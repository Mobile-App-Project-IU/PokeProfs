package com.example.pokemonproject.data.di

import android.app.Application
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
//    fun providePokemonRepository(api: PokemonApi): PokemonRepository =
//        PokemonRepositoryImpl(pokemonApi = api)
    fun providePokemonRepository(
        api: PokemonApi,
        pokemonDao: PokemonDAO,
        elementDao: ElementDAO
    ): PokemonRepository{
      return  PokemonRepositoryImpl(
          pokemonApi = api,
          pokemonDao = pokemonDao,
          elementDao = elementDao
      )
    }
    @Singleton
    @Provides
    fun providePokemonDatabase(app: Application): PokemonDatabase {
        return Room.databaseBuilder(
            app,
            PokemonDatabase::class.java,
            "Pokemon_db"
        ).build()
    }
    @Singleton
    @Provides
    fun providePokemonDao(db: PokemonDatabase): PokemonDAO{
        return db.pokemonDao()
    }

    @Singleton
    @Provides
    fun provideElementDao(db: PokemonDatabase): ElementDAO {
        return db.elementDao()
    }
}
