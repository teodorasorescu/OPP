package com.tema1.stages;

import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;
import com.tema1.players.Players;

import java.util.ArrayList;
import java.util.List;


public class Stages {
    public void stage(final Players[] players, final int parityRound) {
    }

    private List<Integer> confiscated = new ArrayList<>();

    public List<Integer> getConfiscated() {
        return confiscated;
    }

    public void setConfiscated(final List<Integer> confiscated) {
        this.confiscated = confiscated;
    }

    public final void confiscatedGoods(final List<Integer> assetIds) {
        confiscated.addAll(assetIds);
    }

    public final void verifyGoodsBasic(final Players player1, final Players player2) {
        int profit, penalty, i, j;
        //player 1 - serif
        //player2 - comerciant
        int sem;
        sem = 0;
        //daca avem cel putin un ilegal
        for (j = 0; j < player2.getBag().size(); j++) {
            if (player2.getBag().get(j) > Constants.ILLID) {
                sem = 1;
                break;
            }
        }

        for (i = 0; i < player2.getBag().size(); i++) {
            //daca nu avem ilegale si spune adevarul
            if (sem == 0 && player2.getGoodsType() == player2.getBag().get(i)) {
                profit = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                profit = player2.getWallet() + profit;
                player2.setWallet(profit);
                profit = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                penalty = player1.getWallet() - profit;
                player1.setWallet(penalty);
                player2.getStand().add(player2.getBag().get(i));

            } else if (sem == 1 && player2.getGoodsType() == player2.getBag().get(i)) {
                //daca avem carti amestecate, iar cele legale sunt declarate corect
                player2.getStand().add(player2.getBag().get(i));
            }
            if (sem == 1 && player2.getGoodsType() != player2.getBag().get(i)) {
                //daca avem amestecate si nu e nimic declarat corect
                penalty = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                penalty = player2.getWallet() - penalty;
                player2.setWallet(penalty);
                penalty = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                profit = player1.getWallet() + penalty;
                player1.setWallet(profit);
                if (confiscated.isEmpty()) {
                    confiscated.add(0, player2.getBag().get(i));
                } else {
                    confiscated.add(confiscated.size(), player2.getBag().get(i));
                }
            } else if (sem == 0 && player2.getGoodsType() != player2.getBag().get(i)) {
                //daca avem legale si nu sunt declarate corect
                penalty = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                penalty = player2.getWallet() - penalty;
                player2.setWallet(penalty);
                penalty = GoodsFactory.getInstance().
                        getGoodsById(player2.getBag().get(i)).getPenalty();
                profit = player1.getWallet() + penalty;
                player1.setWallet(profit);
                if (confiscated.isEmpty()) {
                    confiscated.add(0, player2.getBag().get(i));
                } else {
                    confiscated.add(confiscated.size(), player2.getBag().get(i));
                }
            }
        }
        player2.getBag().clear();
    }

    public final void greedyVerifiesBribed(final Players player1, final Players player2,
                                           final int noAppIllegal) {
        int i;
        //player 1 - serif
        //player 2 - comerciant
        //seteaza mita, conform numarului de aparitii a bunurilor ilegale din sac
        if (noAppIllegal == 1 || noAppIllegal == 2) {
            player1.setWallet(player1.getWallet() + Constants.LEGALSPENALTY);
            player2.setWallet(player2.getWallet() - Constants.LEGALSPENALTY);
        }
        if (noAppIllegal > 2) {
            player1.setWallet(player1.getWallet() + Constants.ILLEGALSPENALTY);
            player2.setWallet(player2.getWallet() - Constants.ILLEGALSPENALTY);
        }

    }

}
