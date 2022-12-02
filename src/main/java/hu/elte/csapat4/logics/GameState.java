package hu.elte.csapat4.logics;

import hu.elte.csapat4.models.Player;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class GameState {

    private static Player player1;
    private static Player player2;
    private static Player actualPlayer;
    private static ConcurrentHashMap<Coordinate, IMapObject> map;
    private static ConcurrentHashMap<Coordinate, IMapObject> tempMap;
    private static Integer round;

    public static Player getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Player player1) {
        log.info("Player 1 is: " + player1.getNameString());
        GameState.player1 = player1;
        round = 0;
    }

    public static Player getPlayer2() {

        return player2;
    }

    public static void setPlayer2(Player player2) {
        log.info("Player 2 is: " + player2.getNameString());
        GameState.player2 = player2;
        round = 0;
    }

    public static Player getActualPlayer() {
        return actualPlayer;
    }

    public static void setActualPlayer(Player actualPlayer) {
        log.info("Actual player is: " + actualPlayer.getNameString());
        GameState.actualPlayer = actualPlayer;
    }

    public static ConcurrentHashMap<Coordinate, IMapObject> getMap() {
        return map;
    }

    public static void setMap(ConcurrentHashMap<Coordinate, IMapObject> map) {
        GameState.map = map;
    }

    public static Integer getRound() {
        return round;
    }

    public static void incrementRound() {
        round++;
        log.info("---New Round "+round+" ---");
    }

    public static ConcurrentHashMap<Coordinate, IMapObject> getTempMap() {
        if(tempMap==null){
            tempMap = new ConcurrentHashMap<Coordinate, IMapObject>();
        }
        return tempMap;
    }

}
