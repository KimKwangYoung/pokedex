package com.kky.pokedex.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kky.pokedex.main.MainScreen
import com.kky.pokedex.main.detail.DetailScreen

@Composable
fun PokedexNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.Main,
    ) {
        composable<Route.Main> {
            MainScreen(
                onClickItem = { pokemon ->
                    navController.navigate(
                        Route.Detail(pokemon.id)
                    )
                },
            )
        }

        composable<Route.Detail> {
            DetailScreen(onBackButtonClick = { navController.popBackStack() })
        }
    }
}
