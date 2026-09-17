package tech.timoapp.wavy.corutines.tasks

import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CoroutinesTest {

    @Test
    fun task3(): Unit = runBlocking {
        var counter = 0

        val counterJob = launch {
            while (isActive) {
                delay(500)
                counter++
            }
        }

        launch {
            delay(2000)
            counterJob.cancelAndJoin()
            println(counter)
        }
    }

    @Test
    fun task4(): Unit = runBlocking {
        val jobA = async {
            return@async "A"
        }

        val jobB = async {
            return@async "B"
        }

        val jobC = async {
            return@async "C"
        }

        val list = listOf(jobA.await(), jobB.await(), jobC.await())
        println(list)
    }

    @Test
    fun task5(): Unit = runBlocking {
        val myFlow = flow {
            for (i in 1..5) {
                delay(1000)
                emit(i)
            }
        }

        myFlow.filter { it % 2 == 0 }.map { "Number: $it" }.collect { println(it) }
    }

    @Test
    fun task6(): Unit = runBlocking {
        val numberFlow = flowOf(1, 2, 3)
        val stringFlow = flowOf("A", "B", "C")

        numberFlow.zip(stringFlow) { number, string -> number to string }.collect { println(it) }
    }
}