package hu.elte.csapat4.logics;

import hu.elte.csapat4.models.details.BuildingDetails;
import hu.elte.csapat4.models.details.Detail;
import hu.elte.csapat4.models.map.ActionType;
import hu.elte.csapat4.models.map.Building;
import hu.elte.csapat4.models.map.Character;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.settings.ModelSettings;
import lombok.extern.java.Log;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

@Log
public class ActionHelper {

    private static HashMap<Coordinate, IMapObject> terrainObjects;

    public static void build(IMapObject builder, MapObjectType buildingType, Coordinate buildingFuturePosition) throws GameException {
        BuildingDetails details = (BuildingDetails) ModelSettings.getDetailFor(buildingType);

        if (!builder.getPosition().neighboring(buildingFuturePosition)) {
            throw new GameException("Out of reach!");
        }
        if (isFieldOccupied(buildingFuturePosition)) {
            throw new GameException("Field is not free!");
        }
        if (!GameState.getActualPlayer().hasEnoughtMaterial(details.getCost())) {
            throw new GameException("Not enough material!!");
        }

        //ha cabin bánya, farm
        //kikötés hgogy legyen az adott terrain a szomszédságába
        if (buildingType == MapObjectType.CABIN || buildingType == MapObjectType.MINE) {
            getTerrainObjects();
            MapObjectType mustNeighbor;

            if (buildingType == MapObjectType.MINE) {
                mustNeighbor = MapObjectType.GOLD_DEPOSIT;
            } else {
                mustNeighbor = MapObjectType.FOREST;
            }

            boolean notOk = true;
            for (Map.Entry<Coordinate, IMapObject> terrainObject : terrainObjects.entrySet()) {
                if (buildingFuturePosition.neighboring(terrainObject.getValue().getPosition())
                        && terrainObject.getValue().getType() == mustNeighbor) {
                    notOk = false;
                }
            }
            if (notOk) {
                throw new GameException("Must build next to a " + mustNeighbor.name() + "!");
            }
        }

        GameState.getActualPlayer().takeMaterial(details.getCost());
        GameState.getMap().put(buildingFuturePosition, new Building(buildingType, GameState.getActualPlayer(), buildingFuturePosition));
        ((Character) builder).didAction();
    }

    public static void move(IMapObject mapObject, Coordinate coordinate) throws GameException {
        if (coordinate != null && mapObject != null) {
            Coordinate oldPosition = new Coordinate(mapObject.getPosition());
            log.info("Move action " + mapObject.infoText() + " moves to " + coordinate.toString());
            if (!((Character) mapObject).canDoActionInRound()) {
                throw new GameException("Already made an other action!");
            }
            if (isFieldOccupied(coordinate, mapObject)) {
                if (mapObject.getType() == MapObjectType.DRAGON) {
                    if (MapObjectType.isTerrain(GameState.getMap().get(coordinate).getType())
                            && GameState.getMap().get(coordinate).getType() != MapObjectType.MEADOW) {
                        GameState.getTempMap().put(new Coordinate(coordinate), GameState.getMap().get(coordinate));
                        GameState.getMap().remove(coordinate);

                    } else {
                        throw new GameException("Field is not free!");
                    }
                } else {
                    throw new GameException("Field is not free!");
                }
            }

            if (!((Character) mapObject).canMove()) {
                throw new GameException("Not enough movement points!!");
            } else {
                log.info("Move action succeds!");
                ((Character) mapObject).move(coordinate);
                GameState.getMap().remove(oldPosition);
                GameState.getMap().put(mapObject.getPosition(), mapObject);
            }          
            for (Map.Entry<Coordinate, IMapObject> entry : GameState.getTempMap().entrySet()) {
                if (GameState.getMap().get(entry.getKey()) == null) {
                    GameState.getMap().put(entry.getKey(), GameState.getTempMap().get(entry.getKey()));
                    GameState.getTempMap().remove(entry.getKey());
                }
            }

        }
    }

    public static void create(IMapObject creator, MapObjectType characterType, Coordinate destination) throws GameException {
        Detail details = ModelSettings.getDetailFor(characterType);

        if (!((Building)creator).isBuilt()) {
            throw new GameException("Building is under construction!");
        }

        if (isFieldOccupied(destination)) {
            throw new GameException("Field is not free!");
        }

        if (!creator.getPosition().neighboring(destination)) {
            throw new GameException("Out of reach!");
        }

        if (!GameState.getActualPlayer().hasEnoughtMaterial(details.getCost())) {
            throw new GameException("Not enough material!!");
        }

        log.info("Create action succeds!");
        GameState.getActualPlayer().takeMaterial(details.getCost());
        GameState.getMap().put(destination, new Character(characterType, GameState.getActualPlayer(), destination));
    }

    public static void attack(IMapObject attacker, ActionType attackType, IMapObject attackee) throws GameException {
        if (!((Character) attacker).canDoActionInRound()) {
            throw new GameException("Already made an other action!");
        }

        if (attacker.getPlayer().equals(attackee.getPlayer())) {
            throw new GameException("Friendly Fire! Are you sure?");
        }

        if (attacker.getType() == MapObjectType.DRAGON && attackType == ActionType.DRAGON_BREATH) {
            if (!dragonSpecialAttackNeighboring(attacker.getPosition(), attackee.getPosition())) {
                throw new GameException("Out of reach even with dragon breath!");
            }
        } else {
            if (!attacker.getPosition().neighboring(attackee.getPosition())) {
                throw new GameException("Out of reach!");
            }
        }

        ((Character) attacker).attack(attackee);
        ((Character) attacker).didAction();

        if (!attackee.isStanding()) {
            GameState.getMap().remove(attackee.getPosition());
        }
    }

    private static boolean dragonSpecialAttackNeighboring(Coordinate dragonPosition, Coordinate attackeePosition) {
        double distance = Point2D.distance(
                dragonPosition.getX(),
                dragonPosition.getY(),
                attackeePosition.getX(),
                attackeePosition.getY()
        );

        return distance <= 3.7d && distance >= -3.7d;
    }

    private static boolean isFieldOccupied(Coordinate coordinate, IMapObject mapObject) {
        if (GameState.getMap().get(coordinate) != null) {
            if (GameState.getMap().get(coordinate).equals(mapObject)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean isFieldOccupied(Coordinate coordinate) {
        return GameState.getMap().get(coordinate) != null;
    }

    private static void getTerrainObjects() {
        terrainObjects = new HashMap<>();
        terrainObjects.clear();
        for (Map.Entry<Coordinate, IMapObject> entry : GameState.getMap().entrySet()) {
            if (MapObjectType.isTerrain(entry.getValue().getType())) {
                terrainObjects.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
