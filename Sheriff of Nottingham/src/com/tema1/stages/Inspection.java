package com.tema1.stages;

import com.tema1.common.Constants;
import com.tema1.players.Players;

public class Inspection extends Stages {
    public final void stage(final Players[] players, final int parityRound) {
        int i, j, k, noAppIllegal;
        //BASIC
        for (i = 0; i < players.length; i++) {
            if ((players[i].getRole().equals("sheriff")
                    && players[i].getStrategy().equals("basic"))) {
                //daca seriful are mai putin de 16 banuti se opreste, daca nu verifica
                if (players[i].getWallet() < Constants.MINMONEY) {
                    break;
                }
                for (j = 0; j < players.length; j++) {
                    if (j != i) {
                        verifyGoodsBasic(players[i], players[j]);
                        players[j].addonStand();
                    }
                }
            }
            //GREEDY
            if ((players[i].getRole().equals("sheriff")
                    && players[i].getStrategy().equals("greedy"))) {
                for (j = 0; j < players.length; j++) {
                    noAppIllegal = 0;
                    if (j != i) {
                        for (k = 0; k < players[j].getBag().size(); k++) {
                            if (players[j].getBag().get(k) > Constants.ILLID) {
                                noAppIllegal++;
                            }
                        }
                        //apeleaza metodele de verificare, conform cerintei
                        if (players[j].getStrategy().equals("bribed")) {
                            if (players[j].getWallet() == 0 || noAppIllegal == 0) {
                                verifyGoodsBasic(players[i], players[j]);
                            } else {
                                greedyVerifiesBribed(players[i], players[j], noAppIllegal);
                            }
                        } else {
                            verifyGoodsBasic(players[i], players[j]);
                        }
                        players[j].addonStand();
                    }
                }
            }
            //BRIBED
            if ((players[i].getRole().equals("sheriff")
                    && players[i].getStrategy().equals("bribed"))) {
                //2 jucatori, seriful pe prima pozitie
                if (players.length == Constants.LENGTH2 && i == 0) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[i + 1].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[i + 1]);
                        players[i + 1].addonStand();
                    }
                }
                //2 jucatori, seriful pe ultima pozitie
                if (players.length == Constants.LENGTH2 && i == Constants.FIRST) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[i - 1].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[i - 1]);
                        players[i - 1].addonStand();
                    }
                }
                //mai mult de 2 jucatori, seriful pe prima pozitie
                if (players.length > Constants.LENGTH2 && i == 0) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[players.length - 1].addonStand();
                        players[i + 1].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[players.length - 1]);
                        players[players.length - 1].addonStand();
                        verifyGoodsBasic(players[i], players[i + 1]);
                        players[i + 1].addonStand();
                    }
                }
                //mai mult de 2 jucatori, seriful pe pozitia din mijloc
                if (players.length > Constants.LENGTH2 && i == players.length / 2) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[players.length / 2 - 1].addonStand();
                        players[players.length / 2 + 1].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[players.length / 2 - 1]);
                        players[players.length / 2 - 1].addonStand();
                        verifyGoodsBasic(players[i], players[players.length / 2 + 1]);
                        players[players.length / 2 + 1].addonStand();
                    }
                    //mai mult de 3 jucatori
                } else if (players.length > Constants.LENGTH3 && i != 0
                        && i != players.length - 1) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[i - 1].addonStand();
                        players[i + 1].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[i - 1]);
                        players[i - 1].addonStand();
                        verifyGoodsBasic(players[i], players[i + 1]);
                        players[i + 1].addonStand();
                    }
                }

                if (players.length > Constants.LENGTH2 && i == (players.length - 1)) {
                    if (players[i].getWallet() == 0
                            || players[i].getWallet() < Constants.MINMONEY2) {
                        players[0].addonStand();
                        players[players.length - 2].addonStand();
                    } else {
                        verifyGoodsBasic(players[i], players[0]);
                        players[0].addonStand();
                        verifyGoodsBasic(players[i], players[players.length - 2]);
                        players[players.length - 2].addonStand();
                    }
                }
                //ia mita de la ceilalti daca ofera
                if (players.length > Constants.LENGTH3) {
                    for (j = 0; j < players.length; j++) {
                        if (i == 0) {
                            //singurul care ofera mita este bribed
                            if (j != i && j != (players.length - 1) && j != (i + 1)
                                    && players[j].getStrategy().equals("bribed")) {
                                noAppIllegal = 0;
                                for (k = 0; k < players[j].getBag().size(); k++) {
                                    if (players[j].getBag().get(k) > Constants.ILLID) {
                                        noAppIllegal++;
                                    }
                                }
                                if (noAppIllegal > 0
                                        && players[j].getWallet() >= Constants.MINMONEY3) {
                                    greedyVerifiesBribed(players[i], players[j], noAppIllegal);
                                }
                            }
                        } else if (i == players.length - 1) {
                            if (j != i && j != (players.length - 2) && j != 0
                                    && players[j].getStrategy().equals("bribed")) {
                                noAppIllegal = 0;
                                for (k = 0; k < players[j].getBag().size(); k++) {
                                    if (players[j].getBag().get(k) > Constants.ILLID) {
                                        noAppIllegal++;
                                    }
                                }
                                if (noAppIllegal > 0
                                        && players[j].getWallet() >= Constants.MINMONEY3) {
                                    greedyVerifiesBribed(players[i], players[j], noAppIllegal);
                                }
                            }
                        } else if (i != (players.length - 1) && i != 0) {
                            if (j != i && j != (i - 1) && j != (i + 1)
                                    && players[j].getStrategy().equals("bribed")) {
                                noAppIllegal = 0;
                                for (k = 0; k < players[j].getBag().size(); k++) {
                                    if (players[j].getBag().get(k) > Constants.ILLID) {
                                        noAppIllegal++;
                                    }
                                }
                                //se apeleaza metoda de verificare cu mita
                                if (noAppIllegal > 0
                                        && players[j].getWallet() >= Constants.MINMONEY3) {
                                    greedyVerifiesBribed(players[i], players[j], noAppIllegal);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
