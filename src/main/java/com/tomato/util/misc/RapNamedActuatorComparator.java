package com.tomato.util.misc;

import java.util.Comparator;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017年7月31日 下午4:45:59
 */
public class RapNamedActuatorComparator implements Comparator<RapNamedActuator> {

    public static final RapNamedActuatorComparator LOW_FIRST = new RapNamedActuatorComparator(true);
    public static final RapNamedActuatorComparator HIGH_FIRST = new RapNamedActuatorComparator(false);

    private final int whenFirstLow;
    private final int whenSecondLow;

    public RapNamedActuatorComparator() {
        this(true);
    }

    /**
     * @param lowFirst
     *         是否小项排序在前
     */
    public RapNamedActuatorComparator(boolean lowFirst) {
        super();
        if (lowFirst) {
            whenFirstLow = -1;
            whenSecondLow = 1;
        } else {
            whenFirstLow = 1;
            whenSecondLow = -1;
        }
    }

    @Override
    public int compare(RapNamedActuator o1, RapNamedActuator o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return whenFirstLow;
        }
        if (o2 == null) {
            return whenSecondLow;
        }
        int order1 = o1.order();
        int order2 = o2.order();
        if (order1 == order2) {
            return 0;
        } else if (order1 < order2) {
            return whenFirstLow;
        } else {
            return whenSecondLow;
        }
    }

}
