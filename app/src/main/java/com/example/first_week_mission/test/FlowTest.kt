package com.example.first_week_mission.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

@OptIn(DelicateCoroutinesApi::class)
fun main(): Unit = runBlocking {
    loadData().launchIn(scope)

    delay(3000)
    likeFlow = flow {
        emit(testLike)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun loadData() = flow {
    val data = getPerson()
    emit(data)
}.flatMapLatest { persons ->
    flow2().map { like ->
        like to persons
    }
}.map { (like, persons) ->
    persons.mapIndexed { index, person ->
        Person(
            name = person.name,
            like = like[index]
        )
    }
}.onEach {
    println(it)
}

val scope = CoroutineScope(Dispatchers.IO)
suspend fun getPerson() = listOf(
    Person(name = "김광영"), Person(name = "강지원"), Person(name = "김일태"), Person(name = "김준영"),
)

val defaultLike = listOf(true, false, true, false)
val testLike = listOf(false, true, false, true)

var callCount = 0

var likeFlow = flow {
    emit(defaultLike)
}

fun flow2(): Flow<List<Boolean>> = likeFlow

data class Person(
    val name: String,
    val like: Boolean = false
)