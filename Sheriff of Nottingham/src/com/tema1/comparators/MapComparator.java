package com.tema1.comparators;

import java.util.Comparator;
import java.util.Map;

public final class MapComparator implements Comparator<Object> {
    @Override
    public int compare(final Object o1, final Object o2) {
        return ((Comparable) ((Map.Entry) (o2)).getValue())
                .compareTo(((Map.Entry) (o1)).getValue());
    }

}
