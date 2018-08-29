package com.smal.htk.javathreads;

import com.company.java_threads.CharacterSource;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

class RandomCharacterGenerator implements Runnable, CharacterSource {
    private static char[] chars;
    static {
        final String CHAR_ARRAY = "abcdefghijklmnopqrstuvwxyz0123456789";
        chars = CHAR_ARRAY.toCharArray();
    }

    private final Random random;
    private final CharacterEventHandler handler;
    private volatile boolean isDone = false;

    RandomCharacterGenerator() {
        this.random = new Random();
        this.handler = new CharacterEventHandler();
    }

    @Override
    public void addCharacterCallback(@NotNull Function1<? super CharacterEvent, Unit> callback) {
        handler.addCharacterCallback(callback);
    }

    @Override
    public void removeCharacterCallback(@NotNull Function1<? super CharacterEvent, Unit> callback) {
        handler.removeCharacterCallback(callback);
    }

    @Override
    public void nextCharacter() {
        handler.fireNewCharacter(this, chars[random.nextInt(chars.length)]);
    }

    @Override
    public void run() {
        while (!isDone) {
            nextCharacter();
            try {
                Thread.sleep(getPauseTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    private int getPauseTime( ) {
        return (int) (Math.max(1000, 5000 * random.nextDouble( )));
    }

}