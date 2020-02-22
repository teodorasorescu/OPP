package com.tema1.comparators;

import com.tema1.goods.GoodsFactory;

import java.util.Comparator;


public final class IdComparator implements Comparator<Integer> {
    @Override
    public int compare(final Integer x1, final Integer x2) {
        return GoodsFactory.getInstance().getGoodsById(x2).getId()
                - GoodsFactory.getInstance().getGoodsById(x1).getId();
    }
}
