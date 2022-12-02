package hu.elte.csapat4.models.details;

public class Cost {
    private final int gold;
    private final int wood;
    private final int meat;

    public Cost(int gold, int wood, int meat) {
        this.gold = gold;
        this.wood = wood;
        this.meat = meat;
    }

    public int getGoldCost() {
        return gold;
    }

    public int getWoodCost() {
        return wood;
    }

    public int getMeatCost() {
        return meat;
    }

    @Override
    public String toString() {
        return  "Gold: " + gold +
                ", wood: " + wood +
                ", meat: " + meat ;
    }
}
