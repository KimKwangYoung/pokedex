package com.kky.pokedex.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kky.pokedex.feature.main.R
import com.kky.pokedex.feature.main.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding
    private val pokemonAdapter = PokemonListAdapter { pokemon, like ->
        mainViewModel.setLike(pokemon, like)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvPokemon.adapter = pokemonAdapter
        binding.rvPokemon.layoutManager = LinearLayoutManager(this)

        binding.swOnlyLike.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.showOnlyLike(isChecked)
        }

        binding.rvPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemCount = recyclerView.adapter!!.itemCount

                Log.d("MainActivity", "onScrolled :: ${mainViewModel.dataFlow.value::class}")

                if (itemCount - lastVisibleItemPosition == 1) {
                    if (mainViewModel.dataFlow.value != MainViewModel.MainUiState.Loading &&
                        (mainViewModel.dataFlow.value is MainViewModel.MainUiState.Success && !(mainViewModel.dataFlow.value as MainViewModel.MainUiState.Success).showOnlyLike)
                    ) {
                        mainViewModel.loadPokemon()
                    }
                }
            }
        })

        observe()

        if (!mainViewModel.initialized) {
            mainViewModel.loadPokemon()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.dataFlow.collect {
                    binding.progressBar.isVisible = it is MainViewModel.MainUiState.Loading

                    when (it) {
                        MainViewModel.MainUiState.Loading -> {}

                        is MainViewModel.MainUiState.Success -> {
                            pokemonAdapter.showOnlyLike(it.showOnlyLike)
                            pokemonAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.effect.collect {
                    when (it) {
                        is MainViewModel.MainEvent.Fail -> Toast.makeText(
                            this@MainActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        MainViewModel.MainEvent.Loading -> Toast.makeText(
                            this@MainActivity,
                            "로딩 중...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}