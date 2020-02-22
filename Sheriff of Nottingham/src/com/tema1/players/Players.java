package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public final class Players {
    private String role;
    private int wallet;
    private String strategy;
    private int goodsType;
    private List<Integer> cards = new ArrayList<>();
    private List<Integer> bag = new ArrayList<>();
    private List<Integer> stand = new ArrayList<>();
    private List<Integer> justIllegals = new ArrayList<>();


    public List<Integer> getJustIllegals() {
        return justIllegals;
    }

    public void setJustIllegals(final List<Integer> justIllegals) {
        this.justIllegals = justIllegals;
    }

    public int getGoodsType() {

        return goodsType;
    }

    public void setGoodsType(final int goodsType) {
        this.goodsType = goodsType;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(final int wallet) {
        this.wallet = wallet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(final List<Integer> cards) {
        this.cards = cards;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(final String strategy) {
        this.strategy = strategy;
    }

    public  List<Integer> getBag() {
        return bag;
    }

    public void setBag(final List<Integer> bag) {
        this.bag = bag;
    }

    public List<Integer> getStand() {
        return stand;
    }

    public void setStand(final List<Integer> stand) {
        this.stand = stand;
    }

    public void copyForIllegals() {
        int i;
        for (i = 0; i < this.getCards().size(); i++)
            this.getJustIllegals().add(i, this.getCards().get(i));
    }


    public void addonStand() {
        int initStandSize, i;
        if (this.getStand().isEmpty())
            this.setStand(this.getBag());
        else {
            //adaugare pe taraba
            initStandSize = this.getStand().size();
            for (i = 0; i < this.getBag().size(); i++) {
                this.getStand().add(initStandSize + i, this.getBag().get(i));
            }
        }
    }

    public void sortEquals() {
        int i, j;
        for (i = 0; i < this.getCards().size() - 1; i++) {
            if (!this.getCards().get(i).equals(this.getCards().get(i + 1))) {
                for (j = i + 2; j < this.getCards().size(); j++) {
                    if (this.getCards().get(i).equals(this.getCards().get(j))) {
                        Collections.swap(this.getCards(), j - 1, j);
                    }
                }
            }
        }
        for (i = 0; i < this.getCards().size() - 1; i++) {
            for (j = i + 1; j < this.getCards().size(); j++) {
                if (GoodsFactory.getInstance().
                        getGoodsById(this.getCards().get(i)).getProfit()
                        == GoodsFactory.getInstance()
                        .getGoodsById(this.getCards().get(j)).getProfit()
                        && this.getCards().get(i) < this.getCards().get(j)) {
                    Collections.swap(this.getCards(), i, j);
                }
            }
        }
    }
}
