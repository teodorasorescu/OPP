package com.tema1.stages;

import com.tema1.players.Players;
import com.tema1.strategies.CreateTheBagStrategies;

public class CreateTheBag extends Stages {
    public final void stage(final Players[] players, final int parityRound) {
        int i;
        CreateTheBagStrategies[] strategy = new CreateTheBagStrategies[players.length];
        for (i = 0; i < players.length; i++) {
            strategy[i] = new CreateTheBagStrategies();
            strategy[i].setStrategy(players[i].getStrategy());
            //daca jucatorul este comerciant si are strategia basic
            if (strategy[i].getStrategy().equals("basic")
                    && players[i].getRole().equals("trader")) {
                strategy[i].basic(players[i]);
            } else if (strategy[i].getStrategy().equals("greedy")
                    && players[i].getRole().equals("trader")) {
                //daca jucatorul este comerciant si are strategia greedy
                strategy[i].greedy(players[i], parityRound);
            } else if (strategy[i].getStrategy().equals("bribed")
                    && players[i].getRole().equals("trader")) {
                //daca jucatorul este comerciant si are strategia bribed
                strategy[i].bribed(players[i]);
            }
        }
    }
}
