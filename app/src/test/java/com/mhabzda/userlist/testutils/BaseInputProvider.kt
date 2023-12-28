package com.mhabzda.userlist.testutils

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

abstract class BaseInputProvider : ArgumentsProvider {
    abstract val listInput: List<Any>

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return listInput
            .toTypedArray()
            .map { element -> Arguments { arrayOf(element) } }
            .stream()
    }
}
