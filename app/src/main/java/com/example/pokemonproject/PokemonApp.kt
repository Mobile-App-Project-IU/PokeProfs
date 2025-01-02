package com.example.pokemonproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is the main application class for the Pokemon app.
 * By annotating this class with @HiltAndroidApp, we integrate
 * the Hilt dependency injection framework into the app, enabling
 * DI for all components such as Activities, Fragments, and ViewModels.
 */
@HiltAndroidApp
class PokemonApp : Application() {

    // This class is where you can initialize any global resources
    // or configurations needed across the application.
    // Currently, there is no additional logic required.
}
