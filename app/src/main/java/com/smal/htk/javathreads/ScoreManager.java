package com.smal.htk.javathreads;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.company.java_threads.CharacterSource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

class ScoreManager implements Function1<CharacterEvent, Unit> {
    private CharacterSource generator;
    private CharacterSource typeSource;

    private TextView scoreView;

    private int score;
    private int char2type = -1;

    private Lock scoreLock = new ReentrantLock();

    ScoreManager(CharacterSource generator, CharacterSource typeSource, @NonNull TextView sourceView) {
        if (generator != null) {
            generator.addCharacterCallback(this);
        }
        this.generator = generator;
        if (typeSource != null) {
            typeSource.addCharacterCallback(this);
        }
        this.typeSource = typeSource;

        this.scoreView = sourceView;
    }

    ScoreManager(@NonNull TextView sourceView) {
        this(null, null, sourceView);
    }

    void resetGenerator(@NonNull CharacterSource newGenerator) {
        try {
            scoreLock.lock();

            if (generator != null) {
                generator.removeCharacterCallback(this);
            }
            generator = newGenerator;
            generator.addCharacterCallback(this);
        } finally {
            scoreLock.unlock();
        }

    }

    void resetTypeSource(@NonNull CharacterSource newTypeSource) {
        try {
            scoreLock.lock();

            if (typeSource != null) {
                typeSource.removeCharacterCallback(this);
            }
            typeSource = newTypeSource;
            typeSource.addCharacterCallback(this);
        } finally {
            scoreLock.unlock();
        }
    }

    void resetScore() {
        try {
            scoreLock.lock();
            score = 0;
            char2type = -1;
            displayScore();
        } finally {
            scoreLock.unlock();
        }
    }

    void clear() {
        try {
            scoreLock.lock();
            if (generator != null) {
                generator.removeCharacterCallback(this);
            }
            generator = null;
            if (typeSource != null) {
                typeSource.removeCharacterCallback(this);
            }
            typeSource = null;
            score = 0;
            char2type = -1;
        } finally {
            scoreLock.unlock();
        }

        scoreView.post(() -> {
            if (scoreView != null) {
                scoreView.setText("");
            }
        });
    }

    @Override
    public synchronized Unit invoke(CharacterEvent characterEvent) {

        // Previous character not typed correctly: 1-point penalty
        if (characterEvent.getSource() == generator) {
            try {
                scoreLock.lock();
                if (char2type != -1) {
                    score--;
                    displayScore();
                }

                char2type = characterEvent.getCharacter();
            } finally {
                scoreLock.unlock();
            }
        }


        // If character is extraneous: 1-point penalty
        // If character does not match: 1-point penalty
        else {
            try {
                scoreLock.lock();
                if (char2type != characterEvent.getCharacter()) {
                    score--;
                } else {
                    score++;
                    char2type = -1;
                }
                displayScore();
            } finally {
                scoreLock.unlock();
            }
        }

        return Unit.INSTANCE;

    }

    private void displayScore() {
        scoreView.post(() -> {
            if (scoreView != null) {
                scoreView.setText("Current score: " + score);
            }
        });

    }
}
