package com.tema1.strategies;

import com.tema1.common.Constants;
import com.tema1.comparators.IdComparator;
import com.tema1.comparators.ProfitComparator;
import com.tema1.goods.GoodsFactory;
import com.tema1.players.Players;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class CreateTheBagStrategies extends Strategies {

    private ProfitComparator profitComparator = new ProfitComparator();
    private IdComparator idComparator = new IdComparator();
    private int i, sem;

    public final IdComparator getIdComparator() {
        return idComparator;
    }

    public final void setIdComparator(final IdComparator idComparator) {
        this.idComparator = idComparator;
    }

    public final  ProfitComparator getProfitComparator() {
        return profitComparator;
    }

    public final void setProfitComparator(final ProfitComparator profitComparator) {
        this.profitComparator = profitComparator;
    }

    public final int getI() {
        return i;
    }

    public final void setI(final int i) {
        this.i = i;
    }

    public final int getSem() {
        return sem;
    }

    public final  void setSem(final int sem) {
        this.sem = sem;
    }

    public final void basic(final Players player) {
        List<Integer> bag = new ArrayList<>();
        if (player.getRole().equals("trader")) {
            for (i = 0; i < player.getCards().size(); i++) {
                sem = 1;
                if (player.getCards().get(i) < Constants.NOCARDS) {
                    sem = 0;
                    break;
                }
            }

            if (sem == 1) {
                //daca sunt doar ilegale sortam dupa profit
                Collections.sort(player.getCards(), profitComparator);
                bag.add(0, player.getCards().get(0));
                //elimin cartea bagata deja in sac din setul de carti
                player.getCards().remove(0);
                player.setBag(bag);
                player.setGoodsType(0);
            }

            if (sem == 0) {
                //se copiaza intr-o alta lista doar cartile legale
                List<Integer> legalsOnly = new ArrayList<>();
                int h = 0;
                for (i = 0; i < player.getCards().size(); i++) {
                    if (player.getCards().get(i) < Constants.NOCARDS) {
                        legalsOnly.add(h, player.getCards().get(i));
                        h++;
                    }
                }
                player.setCards(legalsOnly);
                //crearea map-ului de frecvente
                Map<Integer, Integer> frequency = new HashMap<>();
                for (Integer freq : player.getCards()) {
                    frequency.put(freq, frequency.getOrDefault(freq, 0) + 1);
                }
                Map<Integer, Integer> maxGoodsFreq = new HashMap<>();
                int maxFreq = (Collections.max(frequency.values()));
                for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
                    if (entry.getValue() == maxFreq) {
                        maxGoodsFreq.put(entry.getKey(), entry.getValue());
                    }
                }
                //calcularea freventei maxime
                i = 0;
                for (Integer key : maxGoodsFreq.keySet()) {
                    if (key < Constants.NOCARDS) {
                        bag.add(i, key);
                        i++;
                    }
                }
                player.setBag(bag);
                Collections.sort(bag, profitComparator);
                //sortare in functie de profit si id daca este cazul
                if (bag.size() > 1) {
                    Collections.sort(bag, profitComparator);
                    //Raman elementele cu profitul maxim
                    for (i = 1; i < bag.size(); i++) {
                        if (GoodsFactory.getInstance().getGoodsById(bag.get(i)).getProfit()
                                < GoodsFactory.getInstance().getGoodsById(bag.get(0)).getProfit()) {
                            bag.remove(i);
                            i = 0;
                        }
                    }

                    if (bag.size() > 1) {
                        Collections.sort(bag, idComparator);
                        for (i = 1; i < bag.size(); i++) {
                            if (GoodsFactory.getInstance().getGoodsById(bag.get(i)).getId()
                                    < GoodsFactory.getInstance().getGoodsById(bag.get(0)).getId()) {
                                bag.remove(i);
                                i = 0;
                            }
                        }
                        //adaugare in sac
                        for (i = 1; i < maxGoodsFreq.get(bag.get(0)); i++) {
                            if (maxGoodsFreq.get(bag.get(0)) - 1 > Constants.NO)
                                break;
                            bag.add(i, bag.get(0));
                        }


                    } else {
                        //adaugare in sac
                        for (i = 1; i < maxGoodsFreq.get(bag.get(0)); i++) {
                            if (maxGoodsFreq.get(bag.get(0)) - 1 > Constants.NO)
                                break;
                            bag.add(i, bag.get(0));
                        }
                        //daca este frecventa 2
                    }
                } else {
                    if (!bag.isEmpty()) {
                        for (i = 1; i < maxGoodsFreq.get(bag.get(0)); i++) {
                            if ((maxGoodsFreq.get(bag.get(0)) - 1) > Constants.NO)
                                break;
                            bag.add(i, bag.get(0));
                        }
                        //daca este frecventa 2
                    }
                }
                player.setBag(bag);
                if (!bag.isEmpty())
                    player.setGoodsType(bag.get(0));
            }

        }

    }

    public final void greedy(final Players player, final int parityRound) {
        List<Integer> bag = new ArrayList<>();
        int sem = 0;
        for (i = 0; i < player.getCards().size(); i++) {
            if (player.getCards().get(i) < Constants.NOCARDS) {
                sem = 1;
            }
        }
        player.copyForIllegals();
        //aplica strategia basic si verifica daca runda este para.
        if (player.getRole().equals("trader")) {
            basic(player);
            if (parityRound % 2 == 0 && player.getBag().size() < Constants.MAXCARDS) {
                Collections.sort(player.getJustIllegals(), profitComparator);
                if (sem == 0) {
                    player.getJustIllegals().remove(0);
                }
                if (!player.getJustIllegals().isEmpty()
                        && player.getJustIllegals().get(0) > Constants.ILLID) {
                    player.getBag().add(player.getBag().size(), player.getJustIllegals().get(0));

                }
            }
        }
    }

    public final void bribed(final Players player) {
        int j, sem, dim = 0, dim2 = 0;
        int noApp = 0, illegals, legals;
        Integer x1, x2;
        List<Integer> bag = new ArrayList<>();
        List<Integer> legalsComp;
        int currentWallet;
        if (player.getRole().equals("trader")) {
            currentWallet = player.getWallet();
            //daca nu mai are bani suficienti aplica basic
            if (player.getWallet() < Constants.WALLET) {
                basic(player);
            } else {
                Collections.sort(player.getCards(), profitComparator);
                player.sortEquals();
                //numara aparitiile bunurilor ilegale
                for (i = 0; i < player.getCards().size(); i++) {
                    if (player.getCards().get(i) > Constants.ILLID) {
                        noApp++;
                    }
                }
                //calculeaza numarul de bunuri ilegale pe care si le permite
                if (noApp < Constants.NOCARDS
                        && currentWallet % Constants.WALLET != Constants.RESULT) {
                    illegals = currentWallet / Constants.WALLET - 1;
                } else {
                    illegals = currentWallet / Constants.WALLET;
                }
                //daca sunt doar ilegale in sac
                if (noApp == Constants.NOCARDS) {
                    if (currentWallet < Constants.MINMONEY2
                            || currentWallet % Constants.WALLET == Constants.RESULT) {
                        illegals = currentWallet / Constants.WALLET;
                    } else {
                        illegals = currentWallet / Constants.WALLET - 1;
                    }
                }
                //daca isi permite mai multe ilegale decat are
                if (illegals > noApp) {
                    illegals = noApp;
                    legals = Constants.DIMCARDS2 - noApp;
                } else if (noApp == Constants.DIMCARDS2) {
                    legals = 0;
                } else {
                    legals = (currentWallet - illegals * Constants.WALLET) / Constants.RESULT;
                    int rest;
                    rest = currentWallet - Constants.WALLET * illegals - Constants.RESULT * legals;
                    if (rest == 0)
                        legals--;
                }
                //daca nu exista bunuri ilegale in sac
                if (noApp == 0) {
                    basic(player);
                } else if (noApp > Constants.DIMCARDS2 && illegals < Constants.DIMCARDS2) {
                    //adauga cate ilegale isi permite, desi are mai multe in sac
                    bag.addAll(player.getCards().subList(0, illegals));
                    player.setBag(bag);
                    player.setGoodsType(0);
                } else if (noApp >= Constants.DIMCARDS2 && (illegals == Constants.DIMCARDS2
                        || illegals > Constants.DIMCARDS2)) {
                    //adauga 8 ilegale in sac, deoarece are si isi permite mai multe
                    bag.addAll(player.getCards().subList(0, 8));
                    player.setBag(bag);
                    player.setGoodsType(0);
                } else if (noApp < Constants.DIMCARDS2) {
                    //daca are mai putin de 8 ilegale completeaza
                    int bagPos = 0;
                    if (illegals >= noApp) {
                        bag.addAll(player.getCards().subList(0, noApp));
                        illegals = noApp;
                        currentWallet -= illegals * Constants.WALLET;
                        legals = currentWallet / Constants.RESULT;
                        bagPos = noApp;
                    }
                    if (illegals != 0 && illegals < noApp) {
                        bag.addAll(player.getCards().subList(0, illegals));
                        bagPos = illegals;
                    }
                    int position;
                    /*daca isi permite mai putine ilegale decat are, calculeaza pozitia de la care
                     sa adauge legale */
                    if (legals >= Constants.DIMCARDS2 - noApp) {
                        legalsComp = player.getCards().subList(noApp, Constants.DIMCARDS2);
                        position = 8 - noApp;
                    } else {
                        legalsComp = player.getCards().subList(noApp, noApp + legals);
                        position = legals;
                    }
                    //completeaza dupa criterii sacul
                    for (i = 0; i < position; i++) {
                        x1 = legalsComp.get(i);
                        sem = 0;
                        if (i == position - 1) {
                            bag.add(bagPos + dim, x1);
                        }
                        //e posibil ca ultima carte sa nu fie bagata in bag
                        for (j = i + 1; j < position; j++) {
                            x2 = legalsComp.get(j);
                            if (profitComparator.compare(x1, x2) == 0
                                    && idComparator.compare(x1, x2) > 0) {
                                if (player.getWallet() - Constants.RESULT != 0) {
                                    bag.add(bagPos + dim, x2);
                                    dim++;
                                    sem = 1;
                                }
                            } else if (profitComparator.compare(x1, x2) == 0
                                    && idComparator.compare(x1, x2) == 0) {
                                if (player.getWallet() - Constants.RESULT != 0) {
                                    bag.add(bagPos + dim, x1);
                                    dim++;
                                    sem = 1;
                                    break;
                                }
                            }
                            if (sem == 0) {
                                if (player.getWallet() - Constants.RESULT != 0) {
                                    bag.add(bagPos + dim, x1);
                                    dim++;
                                    sem = 1;
                                }
                            }
                        }
                    }
                    player.setBag(bag);
                    player.setGoodsType(0);
                }

            }

        }

    }
}



