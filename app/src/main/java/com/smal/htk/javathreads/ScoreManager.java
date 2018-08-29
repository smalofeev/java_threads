package com.smal.htk.javathreads;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.company.java_threads.CharacterSource;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

class ScoreManager implements Function1<CharacterEvent, Unit> {
    private CharacterSource generator;
    private CharacterSource typeSource;

    private TextView scoreView;

    private int score;
    private int char2type = -1;

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

    synchronized void resetGenerator(@NonNull CharacterSource newGenerator) {
        if (generator != null) {
            generator.removeCharacterCallback(this);
        }
        generator = newGenerator;
        generator.addCharacterCallback(this);
    }

    synchronized void resetTypeSource(@NonNull CharacterSource newTypeSource) {
        if (typeSource != null) {
            typeSource.removeCharacterCallback(this);
        }
        typeSource = newTypeSource;
        typeSource.addCharacterCallback(this);
    }

    synchronized void resetScore() {
        score = 0;
        char2type = -1;
        displayScore();
    }

    synchronized void clear() {
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
            if (char2type != -1) {
                score--;
                displayScore();
            }

            char2type = characterEvent.getCharacter();
        }


        // If character is extraneous: 1-point penalty
        // If character does not match: 1-point penalty
        else {
            if (char2type != characterEvent.getCharacter()) {
                score--;
            } else {
                score++;
                char2type = -1;
            }
            displayScore();
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
