package com.example.first_week_mission.repository

import com.example.first_week_mission.service.PokemonApiService
import com.example.first_week_mission.ui.model.PokemonUiModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApiService
): PokemonRepository  {

    override suspend fun loadPokemon(fromID: Int, toID: Int): List<PokemonUiModel> = coroutineScope{
        val size = toID - fromID + 1
        val tasks = ArrayList<Deferred<PokemonUiModel>>(size)

        for (i in fromID..toID) {
            val task = async {

                val pokemonInfo = api.getPokemonInfo(i)
                val pokemonForm = api.getPokemonForm(i)

                val koreanName = pokemonInfo.names.find { it.language.name == "ko" }?.name ?: "알 수 없음"
                val description = pokemonInfo.descriptions.find { it.language.name == "ko" }?.description ?: "해당 포켓몬에 대한 설명이 없습니다."

                val uiModel = PokemonUiModel(
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