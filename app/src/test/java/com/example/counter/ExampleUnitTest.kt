package com.example.counter

import androidx.lifecycle.ViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private lateinit var viewModel: CounterViewModel

    @Before
    fun setUp() {
        viewModel = CounterViewModel()
    }

    @Test
    fun `increment increases count by 1`() {
        viewModel.increment()
        assertEquals(1, viewModel.count.value)
    }

    @Test
    fun `decrement decreases count by 1`() {
        viewModel.increment()
        viewModel.increment()
        viewModel.decrement()
        assertEquals(1, viewModel.count.value)
    }

    @Test
    fun `reset sets count to 0`() {
        viewModel.increment()
        viewModel.increment()
        viewModel.reset()
        assertEquals(0, viewModel.count.value)
    }
}