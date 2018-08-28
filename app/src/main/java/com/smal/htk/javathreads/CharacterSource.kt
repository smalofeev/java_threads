package com.company.java_threads

import com.smal.htk.javathreads.CharacterEvent

interface CharacterSource {

    fun addCharacterCallback(listener: (event: CharacterEvent) -> Unit)

    fun removeCharacterCallback(listener: (event: CharacterEvent) -> Unit)

    fun nextCharacter()
}