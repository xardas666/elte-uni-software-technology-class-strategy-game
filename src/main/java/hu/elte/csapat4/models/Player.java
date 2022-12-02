package hu.elte.csapat4.models;


import com.google.common.base.Objects;
import hu.elte.csapat4.models.details.Cost;
import hu.elte.csapat4.models.map.MaterialType;
import hu.elte.csapat4.models.map.PlayerColor;
import hu.elte.csapat4.settings.ModelSettings;

public class Player {
    private String name;
    private Integer wood;
    private Integer gold;
    private Integer food;
    private PlayerColor color;

    public Player(String name, PlayerColor color) {
        this.name = name;
        wood = ModelSettings.getStartingQuantityForMaterial(MaterialType.WOOD);
        gold = ModelSettings.getStartingQuantityForMaterial(MaterialType.GOLD);
        food = ModelSettings.getStartingQuantityForMaterial(MaterialType.FOOD);
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getWood() {
        return wood;
    }

    public int getGold() {
        return gold;
    }

    public Integer getFood() {
        return food;
    }

    public void addMaterial(MaterialType materialType, Integer quantity) {
        switch (materialType) {
            case FOOD:
                food += quantity;
                break;
            case GOLD:
                gold += quantity;
                break;
            case WOOD:
                wood += quantity;
                break;
            default:
                throw new RuntimeException("Ismeretlen anyag!");
        }
    }

    public void takeMaterial(Cost cost) {
        food -= cost.getMeatCost();
        gold -= cost.getGoldCost();
        wood -= cost.getWoodCost();
    }

    public boolean hasEnoughtMaterial(Cost cost) {
        return getFood() >= cost.getMeatCost()
                && getGold() >= cost.getGoldCost()
                && getWood() >= cost.getWoodCost();

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equal(getName(), player.getName())  && getColor() == player.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getColor());
    }

    public PlayerColor getColor() {
        return color;
    }

    public String getNameString() {
        return name + " ("+color.name()+")";
    }

    public String getResourceString() {
        return " Gold: " + gold +
                " Wood: " + wood +
                " Food: " + food;
    }
}
