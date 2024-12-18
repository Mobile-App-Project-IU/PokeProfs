package com.example.pokemonproject.data.di


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
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "example_database"
//        ).build()
//    }
//    @Provides
//    fun provideExampleDao(database: AppDatabase): PokemonDao {
//        return database.pokemonDao()
//    }
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
    fun providePokemonRepository(api: PokemonApi): PokemonRepository =
        PokemonRepositoryImpl(pokemonApi = api)

}
