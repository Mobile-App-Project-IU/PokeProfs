package com.example.pokemonproject.data.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokemonproject.data.Room.Entity.ElementEntity
import com.example.pokemonproject.data.Room.Entity.PokemonEntity

@Database(entities = [PokemonEntity::class, ElementEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    // Abstract methods to get DAOs
    abstract fun pokemonDao(): PokemonDAO
    abstract fun elementDao(): ElementDAO

//    companion object {
//        // Singleton instance of the database
//        @Volatile
//        private var INSTANCE: PokemonDatabase? = null
//
//        // Function to get the database instance
//        fun getDatabase(context: Context): PokemonDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    PokemonDatabase::class.java,
//                    "pokemon_database"
//                ).fallbackToDestructiveMigration().build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
