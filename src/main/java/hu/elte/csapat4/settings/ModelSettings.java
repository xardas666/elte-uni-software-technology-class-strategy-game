package hu.elte.csapat4.settings;

import com.google.common.collect.Lists;
import hu.elte.csapat4.models.details.BuildingDetails;
import hu.elte.csapat4.models.details.CharacterDetails;
import hu.elte.csapat4.models.details.Cost;
import hu.elte.csapat4.models.details.Detail;
import hu.elte.csapat4.models.details.TerrainDetails;
import hu.elte.csapat4.models.map.ActionType;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.models.map.MaterialType;

import java.util.ArrayList;
import java.util.HashMap;

import static hu.elte.csapat4.models.map.ActionType.ATTACK;
import static hu.elte.csapat4.models.map.ActionType.BUILD_BASTION;
import static hu.elte.csapat4.models.map.ActionType.BUILD_CABIN;
import static hu.elte.csapat4.models.map.ActionType.BUILD_FARM;
import static hu.elte.csapat4.models.map.ActionType.BUILD_MINE;
import static hu.elte.csapat4.models.map.ActionType.CREATE_ARMORED;
import static hu.elte.csapat4.models.map.ActionType.CREATE_DRAGON;
import static hu.elte.csapat4.models.map.ActionType.CREATE_KNIGHT;
import static hu.elte.csapat4.models.map.ActionType.CREATE_PITCHFORK;
import static hu.elte.csapat4.models.map.ActionType.CREATE_WORKER;
import static hu.elte.csapat4.models.map.ActionType.DRAGON_BREATH;
import static hu.elte.csapat4.models.map.ActionType.MOVE;

public class ModelSettings {

    private static HashMap<MapObjectType, Detail> mapObjectDetails;
    private static HashMap<MaterialType, Integer> materialDetails;

    public static Detail getDetailFor(MapObjectType mapObjectType) {
        mapObjectDetails = new HashMap<>();

        addCharacter(MapObjectType.WORKER, RangeType.VL, RangeType.VL, RangeType.M, RangeType.L, RangeType.L, Lists.newArrayList(MOVE, ATTACK, BUILD_CABIN, BUILD_FARM, BUILD_MINE, BUILD_BASTION));
        addCharacter(MapObjectType.PITCHFORK, RangeType.L, RangeType.VL, RangeType.M, RangeType.L, RangeType.L, Lists.newArrayList(MOVE, ATTACK));
        addCharacter(MapObjectType.ARMORED, RangeType.M, RangeType.H, RangeType.L, RangeType.H, RangeType.M, Lists.newArrayList(MOVE, ATTACK));
        addCharacter(MapObjectType.KNIGHT, RangeType.H, RangeType.H, RangeType.H, RangeType.H, RangeType.H, Lists.newArrayList(MOVE, ATTACK));
        addCharacter(MapObjectType.DRAGON, RangeType.VH, RangeType.H, RangeType.VH, RangeType.VH, RangeType.VH, Lists.newArrayList(MOVE, ATTACK, DRAGON_BREATH));

        addBuilding(MapObjectType.BASTION,RangeType.L, RangeType.H, RangeType.M, Lists.newArrayList(CREATE_KNIGHT, CREATE_ARMORED, CREATE_PITCHFORK, CREATE_DRAGON));
        addBuilding(MapObjectType.MAIN, RangeType.NULL, RangeType.H, RangeType.NULL, Lists.newArrayList(CREATE_WORKER));
        addBuilding(MapObjectType.FARM, RangeType.VL, RangeType.M, RangeType.L, Lists.newArrayList());
        addBuilding(MapObjectType.MINE, RangeType.VL, RangeType.M, RangeType.L, Lists.newArrayList());
        addBuilding(MapObjectType.CABIN, RangeType.VL, RangeType.M, RangeType.L, Lists.newArrayList());

        addTerrain(MapObjectType.GOLD_DEPOSIT);
        addTerrain(MapObjectType.FOREST);
        addTerrain(MapObjectType.MEADOW);
        addTerrain(MapObjectType.MOUNTAIN);
        addTerrain(MapObjectType.ROCK);

        return mapObjectDetails.get(mapObjectType);
    }

    public static Integer getStartingQuantityForMaterial(MaterialType materialType) {
        materialDetails = new HashMap<>();

        materialDetails.put(MaterialType.GOLD, 1000000);
        materialDetails.put(MaterialType.WOOD, 1000000);
        materialDetails.put(MaterialType.FOOD, 1000000);

        return materialDetails.get(materialType);
    }

    private static void addTerrain(MapObjectType type){
        mapObjectDetails.put(type, new TerrainDetails(Lists.newArrayList()));
    }

    private static void addBuilding(MapObjectType type, RangeType buildingTime, RangeType life, RangeType cost, ArrayList<ActionType> actions) {
        mapObjectDetails.put(
                type,
                new BuildingDetails(
                        buildingTime.getValue(),
                        life.getValue(),
                        new Cost(cost.getValue(),cost.getValue(),cost.getValue()),
                        actions)
        );
    }

    private static void addCharacter(MapObjectType type, RangeType hit, RangeType shield, RangeType distance, RangeType life, RangeType cost, ArrayList<ActionType> actions) {
        Cost calculatedCost;
        if (type == MapObjectType.WORKER) {
            calculatedCost = new Cost(
                    cost.getValue(),
                    cost.getValue(),
                    cost.getValue()
            );
        } else {
            calculatedCost = new Cost(
                    cost.getValue() * RangeType.L.getValue(),
                    cost.getValue() * RangeType.L.getValue(),
                    cost.getValue() * RangeType.L.getValue()
            );
        }

        mapObjectDetails.put(
                type,
                new CharacterDetails(
                        hit.getValue(),
                        shield.getValue(),
                        distance.getValue(),
                        life.getValue(),
                        calculatedCost,
                        actions)
        );
    }


}

