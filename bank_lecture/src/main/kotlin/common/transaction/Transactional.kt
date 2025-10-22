package org.example.common.transaction

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface Runner {
    fun <T> run(function: () -> T?) : T?
    fun <T> readOnly(function: () -> T?) : T?
}

@Component
private class Advice: Runner {
    @Transactional
    override fun <T> run(function: () -> T?): T? = function()

    @Transactional
    override fun <T> readOnly(function: () -> T?): T? = function()
}

@Component
class Transactional(
    private val advice: Runner
) {
    fun <T> run(function: () -> T?) : T? = advice.run(function)
    fun <T> readOnly(function: () -> T?): T? = advice.run(function)
}