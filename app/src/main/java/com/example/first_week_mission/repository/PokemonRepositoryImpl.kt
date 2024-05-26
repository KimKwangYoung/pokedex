package com.example.first_week_mission.repository

import com.example.first_week_mission.domain.model.Pokemon
import com.example.first_week_mission.domain.model.PokemonDetail
import com.example.first_week_mission.local.AppDatabase
import com.example.first_week_mission.local.dao.LikeDao
import com.example.first_week_mission.local.entity.Like
import com.example.first_week_mission.service.PokemonApiService
import com.example.first_week_mission.domain.model.PokemonSummary
import com.example.first_week_mission.domain.model.Stat
import com.example.first_week_mission.model.AbilityResponse
import com.example.first_week_mission.model.StatResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApiService,
    database: AppDatabase
): PokemonRepository  {

    private var page = 0

    private val perPage = 10

    private val likeDao: LikeDao = database.dao()

    private var cacheLike = emptyList<Like>()

    private var cacheData = emptyList<Pokemon>()

    private val listeners = mutableMapOf<Int, PokemonRepository.DataChangeListener>()

    override suspend fun loadPokemon() {
        if (page == 0) {
            cacheLike = likeDao.getLikes()
        }

        val loadedData = loadMore()
        val new = ArrayList(cacheData)
        new.addAll(loadedData)
        cacheData = new

        notifyDataChanged()
    }
    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        val data = cacheData.first { it.id == id }

        if (data is PokemonDetail) {
            return data
        }

        val summary = data as PokemonSummary
        val detail = loadPokemonDetailData(summary)
        val cache = ArrayList(cacheData)
        val index = cache.indexOfFirst { it.id == id }
        cache[index] = detail
        cacheData = cache

        return detail
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
            like = cacheLike.find { it.id == summary.id } != null
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
                    like = cacheLike.find { it.id == i } != null
                )

                uiModel
            }

            tasks.add(task)
        }

        tasks.awaitAll().sortedBy { it.id }
    }

    override suspend fun like(id: Int) {
        likeDao.insertLike(Like(id = id))
        cacheLike = likeDao.getLikes()
        changeLike(id)
        notifyDataChanged()
    }

    override suspend fun unlike(id: Int) {
        likeDao.deleteLike(Like(id = id))
        cacheLike = likeDao.getLikes()
        changeLike(id)
        notifyDataChanged()
    }

    override fun addDataChangeListener(receiver: Any, listener: PokemonRepository.DataChangeListener) {
        listeners[receiver.hashCode()] = listener
    }

    override fun clear() {
        listeners.clear()
    }

    private fun changeLike(id: Int) {
        val mutable = ArrayList(cacheData)
        val element = mutable.first { it.id == id }
        val index = mutable.indexOfFirst { it.id == id }
        val isLike = cacheLike.find { it.id == id } != null

        mutable[index] = when(element) {
            is PokemonSummary -> {
                element.copy(like = isLike)
            }
            is PokemonDetail -> {
                element.copy(like = isLike)
            }
        }

        cacheData = mutable
    }

    private fun notifyDataChanged() {
        listeners.values.forEach {
            it.onDataChanged(cacheData)
        }
    }

    override fun removeDataChangeListener(receiver: Any) {
        listeners.remove(receiver.hashCode())
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