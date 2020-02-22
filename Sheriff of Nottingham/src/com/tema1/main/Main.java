package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.comparators.MapComparator;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;
import com.tema1.players.Players;
import com.tema1.stages.CreateTheBag;
import com.tema1.stages.Inspection;
import com.tema1.stages.Stages;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

public final class Main {
    public static void scoreCalculator(final Players[] players) {
        int i, j;
        Goods goods;
        //Aplica bonusul ilegalelor
        for (i = 0; i < players.length; i++) {
            for (j = 0; j < players[i].getStand().size(); j++) {
                if (players[i].getStand().get(j) > Constants.ILLID) {
                    goods = GoodsFactory.getInstance().getGoodsById(players[i].getStand().get(j));
                    for (var bonus : ((IllegalGoods) goods).getIllegalBonus().entrySet()) {
                        for (int k = 0; k < bonus.getValue(); k++) {
                            players[i].getStand().add(bonus.getKey().getId());
                        }
                    }
                }
            }
        }
        //Aplica profitul fiecarui bun
        for (i = 0; i < players.length; i++) {
            for (j = 0; j < players[i].getStand().size(); j++)
                players[i].setWallet(players[i].getWallet()
                        + GoodsFactory.getInstance().getGoodsById(
                                players[i].getStand().get(j)).getProfit());
        }
    }

    //Calculeaza bonusul
    public static void finalBonus(final Players[] players) {
        int i, j, max, queenPosition, kingPosition,
                queenPosition1, queenPosition2, queenPos1, queenPos2;
        Map<Integer, Integer>[] frequency = new HashMap[players.length];
        Map<Integer, Goods> allGoods;
        allGoods = GoodsFactory.getInstance().getAllGoods();
        Goods goods;
        LegalGoods goods2;
        //Fiecarui jucator i se creeaza un hashmap de frecvanta
        for (i = 0; i < players.length; i++) {
            if (players[i].getStand().isEmpty()) {
                frequency[i] = new HashMap<>();
                for (j = 0; j < Constants.NOCARDS; j++) {
                    frequency[i].put(j, 0);
                }
                for (j = Constants.FIRSTILL; j < Constants.NOILL; j++) {
                    frequency[i].put(j, 0);
                }

            } else {
                frequency[i] = new HashMap<>();
                for (Integer key : players[i].getStand()) {
                    frequency[i].put(key, frequency[i].getOrDefault(key, 0) + 1);
                }
            }
        }
        //Itereaza prin fiecare bun legal si compara hashmap-urile de frecventa
        for (i = 0; i < Constants.NOCARDS; i++) {
            kingPosition = -1;
            queenPos1 = -1;
            queenPos2 = -1;
            queenPosition1 = -1;
            queenPosition2 = -1;
            queenPosition = -1;
            max = 0;
            if (frequency[0].get(i) != null) {
                max = frequency[0].get(i);
            }
            for (j = 1; j < players.length; j++) {
                if (frequency[j].get(i) != null && max < frequency[j].get(i) && max >= 0) {
                    max = frequency[j].get(i);
                    kingPosition = j;
                }
            }
            goods = allGoods.get(i);
            goods2 = (LegalGoods) goods;
            if (max > 0 && kingPosition == -1) {
                kingPosition = 0;
            }
            if (kingPosition >= 0 && frequency[kingPosition].get(i) != null) {
                players[kingPosition].setWallet(players[kingPosition].getWallet()
                        + goods2.getKingBonus());
            }
            int pos1 = -1;
            for (j = 0; j < players.length; j++) {
                if (!frequency[j].isEmpty() && frequency[j].get(i) != null
                        && frequency[j].get(i) != pos1) {
                    if (j != kingPosition && frequency[j].get(i).equals(max) && max != 0) {
                        queenPosition1 = frequency[j].get(i);
                        queenPos1 = j;
                    }
                    pos1 = queenPosition1;
                }
            }
            int pos2 = -1, max2 = -1;
            for (j = 0; j < players.length; j++) {
                if (!frequency[j].isEmpty() && frequency[j].get(i) != null) {
                    if (frequency[j].get(i) < max && frequency[j].get(i) != pos2
                            && frequency[j].get(i) > max2) {
                        max2 = frequency[j].get(i);
                        queenPosition2 = frequency[j].get(i);
                        queenPos2 = j;
                    }
                    pos2 = queenPosition2;
                }
            }
            if (queenPosition1 > queenPosition2) {
                queenPosition = queenPos1;
            } else if (queenPosition1 < queenPosition2) {
                queenPosition = queenPos2;
            }

            if (queenPosition >= 0 && frequency[queenPosition].get(i) != null
                    && frequency[queenPosition].get(i) != 0) {
                players[queenPosition].setWallet(players[queenPosition].getWallet()
                        + goods2.getQueenBonus());
            }
        }
    }

    //sorteaza HashMap-ul de carti
    public static HashMap sortMap(final HashMap map) {
        List sort = new LinkedList(map.entrySet());
        final MapComparator mapComparator = new MapComparator();
        Collections.sort(sort, mapComparator);
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = sort.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic
        Players[] players = new Players[gameInput.getPlayerNames().size()];
        CreateTheBag createTheBag = new CreateTheBag();
        Inspection inspection = new Inspection();
        Stages stages = new Stages();
        int i, j, k, dimension, h, init, ii;
        //Setarea caracteristicilor fiecarui jucator
        for (i = 0; i < gameInput.getPlayerNames().size(); i++) {
            players[i] = new Players();
            players[i].setStrategy(gameInput.getPlayerNames().get(i));
            players[i].setWallet(Constants.INITMONEY);
            players[i].setRole("trader");
        }
        dimension = 0;
        if (gameInput.getRounds() <= Constants.ROUNDS && gameInput.getPlayerNames().size() != 0) {
            for (i = 1; i <= gameInput.getRounds(); i++) {
                for (j = 0; j < gameInput.getPlayerNames().size(); j++) {
                    players[j].setRole("sheriff");
                    //impartire carti
                    for (k = 0; k < gameInput.getPlayerNames().size(); k++) {
                        if (players[k].getRole().equals("trader")) {
                            init = 0;
                            for (h = dimension; h <= dimension + Constants.DIMCARDS; h++) {
                                players[k].getCards().add(init, gameInput.getAssetIds().get(h));
                                init++;
                            }
                            dimension += Constants.NOCARDS;
                        }
                    }
                    //Fiecara jucator isi face sacul
                    createTheBag.stage(players, i);
                    //Seriful inspecteaza fiecare jucator
                    inspection.stage(players, i);
                    //Adauga cartile confiscate la sfarsitul listei initiale de carti
                    stages.confiscatedGoods(gameInput.getAssetIds());

                    for (ii = 0; ii < players.length; ii++) {
                        players[ii].getJustIllegals().clear();
                    }
                    //Ardere carti la sfarsitul fiecarei subrunde
                    for (ii = 0; ii < players.length; ii++) {
                        players[ii].getCards().clear();
                    }
                    players[j].setRole("trader");
                }
                //Ardere carti la sfarsitul rundelor
                for (ii = 0; ii < players.length; ii++) {
                    players[ii].getCards().clear();
                }
            }
            //Adaugare scor
            scoreCalculator(players);
            //Adaugare bonus
            finalBonus(players);
            //Afisare clasament
            HashMap<Integer, Integer> score = new HashMap<>();
            List<Integer> money = new ArrayList<>();
            for (i = 0; i < gameInput.getPlayerNames().size(); i++) {
                money.add(i, players[i].getWallet());
            }
            for (i = 0; i < gameInput.getPlayerNames().size(); i++)
                score.put(i, money.get(i));
            Collections.sort(money, Collections.reverseOrder());
            HashMap sorted = sortMap(score);
            Set set2 = sorted.entrySet();
            Iterator iterator2 = set2.iterator();
            int m3;
            while (iterator2.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator2.next();
                System.out.print(me2.getKey());
                m3 = (int) me2.getKey();
                System.out.print(" " + players[m3].getStrategy().toUpperCase() + " ");
                System.out.println(me2.getValue());

            }


        }
    }
}
