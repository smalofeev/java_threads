package com.smal.htk.javathreads;

import com.company.java_threads.CharacterSource;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.Vector;

public class CharacterEventHandler {

    private Vector<Function1<? super CharacterEvent, Unit>> listeners = new Vector<>();

    public void addCharacterCallback(Function1<? super CharacterEvent, Unit> callback) {
        listeners.add(callback);
    }

    public void removeCharacterCallback(Function1<? super CharacterEvent, Unit> callback) {
        listeners.remove(callback);
    }

    public void fireNewCharacter(CharacterSource source, char character) {
        CharacterEvent event = new CharacterEvent(source, character);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).invoke(event);
        }
    }

}
