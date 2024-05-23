package com.example.first_week_mission.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.first_week_mission.R
import com.example.first_week_mission.databinding.ActivityPokemonDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonDetailViewModel by viewModels()
    private lateinit var binding: ActivityPokemonDetailBinding

    private var like: Boolean = false

    private val statAdapter: PokemonInfoRecyclerViewAdapter = PokemonInfoRecyclerViewAdapter()

    private val abilityAdapter: PokemonInfoRecyclerViewAdapter = PokemonInfoRecyclerViewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvStat.layoutManager = LinearLayoutManager(this)
        binding.rvStat.adapter = statAdapter

        binding.rvAbility.layoutManager = LinearLayoutManager(this)
        binding.rvAbility.adapter = abilityAdapter

        initActionbar()
        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        setLikeImage(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_like -> {
                viewModel.toggleLike()
                return true
            }
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setLikeImage(menu: Menu?) {
        if (this.like) {
            menu?.findItem(R.id.menu_like)?.icon = ContextCompat.getDrawable(this, R.drawable.favorite_24dp)
        } else {
            menu?.findItem(R.id.menu_like)?.icon = ContextCompat.getDrawable(this, R.drawable.favorite_border_24dp)
        }
    }

    private fun observe() {
        viewModel.state.onEach {
            when(it) {
                is PokemonDetailViewModel.UiState.Fail -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                PokemonDetailViewModel.UiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.contentScrollView.isVisible = false
                }
                is PokemonDetailViewModel.UiState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.contentScrollView.isVisible = true

                    val data = it.data
                    supportActionBar?.title = data.name

                    Glide.with(this).load(data.imageUrl).into(binding.ivPokemon)

                    like = data.like
                    invalidateOptionsMenu()

                    binding.tvTypes.text = "${data.type.joinToString("/")} 타입 포켓몬"
                    binding.tvDescription.text = data.description
                    statAdapter.data = data.stat.map {stat ->
                        "${stat.name}\n기본 스탯 : ${stat.baseStat}"
                    }

                    abilityAdapter.data = data.ability
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.event.onEach {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }.launchIn(lifecycleScope)
    }

    private fun initActionbar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }
}