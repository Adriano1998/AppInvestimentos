package com.br.brqinvestimentos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule


open class BaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun baseSetUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}