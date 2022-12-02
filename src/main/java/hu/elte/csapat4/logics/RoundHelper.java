package hu.elte.csapat4.logics;

import hu.elte.csapat4.models.Player;
import hu.elte.csapat4.models.map.Building;
import hu.elte.csapat4.models.map.Character;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.models.map.PlayerColor;
import java.time.Clock;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;

@Log
public class RoundHelper {

    private static HashMap<Coordinate, IMapObject> actualPlayersStuff;
    private static HashMap<Coordinate, IMapObject> otherPlayersStuff;

    public static void gameStart(String player1Name, String player2Name) {
        log.info("---Game Starts---");
        log.info("---New Round 0 ---");
        GameState.setPlayer1(new Player(player1Name, PlayerColor.BLUE));
        GameState.setPlayer2(new Player(player2Name, PlayerColor.RED));
        GameState.setActualPlayer(GameState.getPlayer1());
        GameState.setMap(new GameMap().getStarterMap());
    }

    public static boolean isGameFinished() {
        getOtherPlayersStuff();
        return checkPlayerMainBuilding() && checkPlayerCharacters();
    }

    private static boolean checkPlayerCharacters() {
        int count = 0;
        for (Map.Entry<Coordinate, IMapObject> entry : otherPlayersStuff.entrySet()) {
            if (MapObjectType.isCharacter(entry.getValue().getType())) {
                count++;
            }
        }
        return count == 0;
    }

    private static boolean checkPlayerMainBuilding() {
        int count = 0;
        for (Map.Entry<Coordinate, IMapObject> entry : otherPlayersStuff.entrySet()) {
            if (MapObjectType.isBuilding(entry.getValue().getType())) {
                count++;
            }
        }
        return count == 0;
    }

    public static void newRound() {
        GameState.incrementRound();
        GameState.setActualPlayer(getNextPlayer());
        getActualPlayersStuff();
        getOtherPlayersStuff();
        building();
        production();
        resetCharacterActions();
    }

    private static void resetCharacterActions() {
        log.info("Reset character actions for " + GameState.getActualPlayer().getNameString());
        for (Map.Entry<Coordinate, IMapObject> entry : actualPlayersStuff.entrySet()) {
            if (MapObjectType.isCharacter(entry.getValue().getType())) {
                Character character = (Character) entry.getValue();
                character.resetAfterRound();
            }
        }
    }

    private static Player getNextPlayer() {
        return GameState.getActualPlayer().equals(GameState.getPlayer1()) ? GameState.getPlayer2() : GameState.getPlayer1();
    }

    private static void production() {
        log.info("Do production for " + GameState.getActualPlayer().getNameString());
        for (Map.Entry<Coordinate, IMapObject> entry : actualPlayersStuff.entrySet()) {
            if (MapObjectType.isBuilding(entry.getValue().getType())) {
                Building building = (Building) entry.getValue();
                Integer workerCount = countNeighboringProducers(building.getPosition());
                building.doProduction(workerCount);
            }
        }

    }
    
    private static Integer countNeighboringProducers(Coordinate target) {
        Integer count = 0;
        for (Map.Entry<Coordinate, IMapObject> entry : actualPlayersStuff.entrySet()) {
            if (MapObjectType.isCharacter(entry.getValue().getType())
                    && entry.getValue().getPosition().neighboring(target)
                    && MapObjectType.WORKER == entry.getValue().getType()) {
                    count++;
            }
        }
        return count;
    }

    private static Integer countNeighboringWorkers(Coordinate target) {
        Integer count = 0;
        for (Map.Entry<Coordinate, IMapObject> entry : actualPlayersStuff.entrySet()) {
            if (MapObjectType.isCharacter(entry.getValue().getType())
                    && entry.getValue().getPosition().neighboring(target)
                    && MapObjectType.WORKER == entry.getValue().getType()) {
                if (((Character) entry.getValue()).canDoActionInRound()) {
                    ((Character) entry.getValue()).didAction();
                    count++;
                }
            }
        }
        return count;
    }

    private static void building() {
        log.info("Do building for " + GameState.getActualPlayer().getNameString());
        for (Map.Entry<Coordinate, IMapObject> entry : actualPlayersStuff.entrySet()) {
            if (MapObjectType.isBuilding(entry.getValue().getType())) {
                if(!((Building) entry.getValue()).isBuilt()){
                    Building building = (Building) entry.getValue();
                    Integer workerCount = countNeighboringWorkers(building.getPosition());
                    ((Building) entry.getValue()).progress(workerCount);
                }
            }
        }
    }

    private static void getActualPlayersStuff() {
        actualPlayersStuff = new HashMap<>();
        for (Map.Entry<Coordinate, IMapObject> entry : GameState.getMap().entrySet()) {
            if (!MapObjectType.isTerrain(entry.getValue().getType())
                    && entry.getValue().getPlayer().equals(GameState.getActualPlayer())) {
                actualPlayersStuff.put(
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }
    }

    private static void getOtherPlayersStuff() {
        otherPlayersStuff = new HashMap<>();
        for (Map.Entry<Coordinate, IMapObject> entry : GameState.getMap().entrySet()) {
            if (!MapObjectType.isTerrain(entry.getValue().getType())
                    && !entry.getValue().getPlayer().equals(GameState.getActualPlayer())) {
                otherPlayersStuff.put(
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }
    }
}
