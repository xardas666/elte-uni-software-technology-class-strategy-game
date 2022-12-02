package hu.elte.csapat4.settings;

import java.util.concurrent.ThreadLocalRandom;

public enum RangeType {
    VH(41,50)
    ,H(31,40)
    ,M(21,30)
    ,L(11,20)
    ,VL(2,10)
    ,NULL(0,1);

    private final int min;
    private final int max;

    RangeType(int min, int max){
        this.max = max;
        this.min = min;
    }

    public int getValue(){
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
