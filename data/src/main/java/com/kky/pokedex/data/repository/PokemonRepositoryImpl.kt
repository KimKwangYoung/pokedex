package com.kky.pokedex.data.repository

import android.util.Log
import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.domain.model.PokemonDetail
import com.kky.pokedex.domain.model.PokemonSummary
import com.kky.pokedex.domain.model.Stat
import com.kky.pokedex.local.dao.LikeDao
import com.kky.pokedex.local.model.LikeEntity
import com.kky.pokedex.network.model.AbilityResponse
import com.kky.pokedex.network.model.StatResponse
import com.kky.pokedex.network.service.PokemonApiService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApiService,
    private val likeDao: LikeDao
): PokemonRepository {

    private var page = 0

    private val perPage = 10

    private val cacheData = MutableStateFlow<List<Pokemon>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun flowPokemon(): Flow<List<Pokemon>> = cacheData.flatMapLatest { pokemon ->
        likeDao.getLikes().map {
            Log.d("PokemonRepository", it.toString())
            it to pokemon
        }
    }.map { (likes, data) ->
        data.map { item ->
            val like = likes.firstOrNull { it.id == item.id } != null
            when(item) {
                is PokemonSummary -> item.copy(like = like)
                is PokemonDetail -> item.copy(like = like)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun flowPokemonDetail(id: Int): Flow<PokemonDetail> = flow {
        val existed = cacheData.value.first { it.id == id }

        if (existed is PokemonDetail) {
            emit(existed)
        } else {
            val summary = existed as PokemonSummary
            val detail = loadPokemonDetailData(summary)
            val cache = ArrayList(cacheData.value)
            val index = cache.indexOfFirst { it.id == id }
            cache[index] = detail
            cacheData.value = cache

            emit(detail)
        }
    }.flatMapLatest { existed ->
        likeDao.getLikes()
            .map { like ->
                existed.copy(
                    like = like.find { it.id == existed.id } != null
                )
            }
    }

    override suspend fun loadPokemon() {
        val loadedData = loadMore()
        val new = ArrayList(cacheData.value)
        new.addAll(loadedData)
        cacheData.value = new
    }

    private suspend fun loadPokemonDetailData(summary: PokemonSummary): PokemonDetail = coroutineScope {
        val pokemon = api.getPokemon(summary.id)

        val abilityTasks = ArrayList<Deferred<AbilityResponse>>(pokemon.abilities.size)
        val statTasks = ArrayList<Deferred<StatResponse>>(pokemon.stats.size)

        pokemon.abilities.forEach {
            abilityTasks.add(
                async { api.getAbility(it.ability.name) }
            )
        }

        pokemon.stats.forEach {
            statTasks.add(
                async { api.getStat(it.stat.name) }
            )
        }

        val abilities = abilityTasks.awaitAll().map { response ->
            response.flavorTexts.first { it.language.name == "ko" }.flavorText
        }

        val stats = statTasks.awaitAll().map { response ->
            val baseStat: Int = pokemon.stats.first { it.stat.name == response.name }.baseStat
            val name: String = response.names.first { it.language.name == "ko" }.name
            Stat(
                baseStat = baseStat,
                name = name
            )
        }

        PokemonDetail(
            pokemonSummary = summary,
            weight = pokemon.weight,
            height = (pokemon.height / 10).toFloat(),
            stat = stats,
            ability = abilities,
            like = summary.like
        )
    }

    private suspend fun loadMore(): List<PokemonSummary> = coroutineScope {
        val size = perPage
        val tasks = ArrayList<Deferred<PokemonSummary>>(size)

        val start = page * 10 + 1
        page++
        var end = page * 10

        //1세대 포켓몬만 로딩
        if (end > 151) {
            end = 151
        }

        for (i in start..end) {
            val task = async {

                val pokemonInfo = api.getPokemonInfo(i)
                val pokemonForm = api.getPokemonForm(i)

                val koreanName = pokemonInfo.names.find { it.language.name == "ko" }?.name ?: "알 수 없음"
                val description = pokemonInfo.descriptions.find { it.language.name == "ko" }?.flavorText ?: "해당 포켓몬에 대한 설명이 없습니다."

                val uiModel = PokemonSummary(
                    id = i,
                    name = koreanName,
                    description = description,
                    imageUrl = pokemonForm.images.defaultImage,
                    type = pokemonForm.types.map { typeMapping[it.type.name] ?: "알 수 없음" },
                    like = false
                )

                uiModel
            }

            tasks.add(task)
        }

        tasks.awaitAll().sortedBy { it.id }
    }

    override suspend fun like(id: Int) {
        likeDao.insertLike(LikeEntity(id = id))
    }

    override suspend fun unlike(id: Int) {
        likeDao.deleteLike(LikeEntity(id = id))
    }

    private val typeMapping: Map<String, String> = mapOf(
        "normal" to "노멀",
        "fighting" to "격투",
        "flying" to "비행",
        "poison" to "독",
        "ground" to "땅",
        "rock" to "바위",
        "bug" to "벌레",
        "ghost" to "고스트",
        "steel" to "강철",
        "fire" to "불",
        "water" to "물",
        "grass" to "풀",
        "electric" to "전기",
        "psychic" to "에스퍼",
        "ice" to "얼음",
        "dragon" to "드래곤",
        "dark" to "악",
        "fairy" to "페어리",
        "stellar" to "스텔라",
        "unknown" to "알 수 없음"
    )
}
