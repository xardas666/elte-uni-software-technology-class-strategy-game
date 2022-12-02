package hu.elte.csapat4.models.map;

import com.google.common.collect.Lists;

public enum MapObjectType {
    WORKER, KNIGHT, ARMORED, PITCHFORK, DRAGON, BASTION, MAIN,
    FARM, MINE, CABIN, FOREST, GOLD_DEPOSIT, MEADOW, MOUNTAIN, ROCK;

    public static boolean isBuilding(MapObjectType mapObjectType) {
        return Lists.newArrayList(
                BASTION, MAIN, FARM, MINE, CABIN
        ).contains(mapObjectType);
    }

    public static boolean isCharacter(MapObjectType mapObjectType) {
        return Lists.newArrayList(
                WORKER, KNIGHT, ARMORED, PITCHFORK, DRAGON
        ).contains(mapObjectType);
    }

    public static boolean isTerrain(MapObjectType mapObjectType) {
        return Lists.newArrayList(
                FOREST, GOLD_DEPOSIT, MEADOW, MOUNTAIN, ROCK
        ).contains(mapObjectType);
    }
}
