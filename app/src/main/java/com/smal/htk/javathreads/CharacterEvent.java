package com.smal.htk.javathreads;


import com.company.java_threads.CharacterSource;

public class CharacterEvent {
    private final CharacterSource source;
    private char character;

    public CharacterEvent(CharacterSource source, char character) {
        this.source = source;
        this.character = character;
    }

    public CharacterSource getSource() {
        return source;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }
}
