package hu.elte.csapat4;

import hu.elte.csapat4.logics.ActionBuilder;
import hu.elte.csapat4.logics.GameState;
import hu.elte.csapat4.logics.RoundHelper;
import hu.elte.csapat4.logics.*;
import hu.elte.csapat4.models.*;
import hu.elte.csapat4.models.map.*;
import hu.elte.csapat4.models.details.Cost;
import hu.elte.csapat4.models.map.Character;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Log
public class GameTest {
    Player red = new Player("RED", PlayerColor.RED);
    Player blue = new Player("BLUE", PlayerColor.BLUE);
    ConcurrentHashMap<Coordinate, IMapObject> map = new ConcurrentHashMap<>();
    Coordinate[] coord = new Coordinate[9];

    @BeforeEach
    void setUpGame(){
        GameState.setPlayer1(red);
        GameState.setPlayer2(blue);
        GameState.setActualPlayer(GameState.getPlayer1());

        map = new ConcurrentHashMap<>();
        coord[0] = new Coordinate(0, 0);
        map.put(coord[0], new Terrain(MapObjectType.MEADOW, coord[0]));
        coord[1] = new Coordinate(0, 1);
        map.put(coord[1], new Terrain(MapObjectType.MEADOW, coord[1]));
        coord[2] = new Coordinate(0, 2);
        map.put(coord[2], new Building(MapObjectType.MAIN, GameState.getPlayer2(), coord[2]));
        coord[3] = new Coordinate(1, 0);
        map.put(coord[3], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[3]));
        coord[4] = new Coordinate(1, 1);
        map.put(coord[4], new Terrain(MapObjectType.MEADOW, coord[4]));
        coord[5] = new Coordinate(1, 2);
        map.put(coord[5], new Character(MapObjectType.WORKER, GameState.getPlayer2(), coord[5]));
        coord[6] = new Coordinate(2, 0);
        map.put(coord[6], new Building(MapObjectType.MAIN, GameState.getPlayer1(), coord[6]));
        coord[7] = new Coordinate(2, 1);
        map.put(coord[7], new Terrain(MapObjectType.MEADOW, coord[7]));
        coord[8] = new Coordinate(2, 2);
        map.put(coord[8], new Terrain(MapObjectType.MEADOW, coord[8]));

        GameState.setMap(map);
    }

    // új kör
    @Test
    void newRoundTest() {
        Player player1 = GameState.getActualPlayer();

        RoundHelper.newRound();

        assumeFalse(GameState.getActualPlayer().equals(player1));
        assumeTrue(((Character)map.get(coord[5])).canDoActionInRound());
    }

    // játék vége
    @Test
    void isGameOverTest() {
        map.replace(coord[0], new Character(MapObjectType.DRAGON, GameState.getPlayer1(), coord[0]));
        map.replace(coord[3], new Character(MapObjectType.DRAGON, GameState.getPlayer1(), coord[3]));

        IMapObject mapObject1 = map.get(coord[0]);
        IMapObject mapObject2 = map.get(coord[3]);

        while(map.get(coord[2]) != null && map.get(coord[5]) != null){
            RoundHelper.newRound();
            RoundHelper.newRound();

            ActionBuilder.clear();
            ActionBuilder.setSelected(mapObject1);
            ActionBuilder.setActionType(ActionType.DRAGON_BREATH);
            ActionBuilder.setDestination(map.get(coord[2]));

            try{
                ActionBuilder.resolveAction();
            }catch(GameException e){}

            ActionBuilder.clear();
            ActionBuilder.setSelected(mapObject2);
            ActionBuilder.setActionType(ActionType.DRAGON_BREATH);
            ActionBuilder.setDestination(map.get(coord[5]));

            try{
                ActionBuilder.resolveAction();
            }catch(GameException e){}
        }

        assumeTrue(RoundHelper.isGameFinished());
    }

    // kaszárnya bárhova
    @Test
    void buildBastionTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));
   
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[4]).getType() == MapObjectType.BASTION);
    }

    // munkahelyek csak az adott resource mellé
    @Test
    void buildProducerTest() {
        map.replace(coord[0], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[0]));
        map.replace(coord[6], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[6]));

        map.replace(coord[2], new Terrain(MapObjectType.MEADOW, coord[2]));
        map.replace(coord[5], new Terrain(MapObjectType.GOLD_DEPOSIT, coord[5]));
        map.replace(coord[8], new Terrain(MapObjectType.FOREST, coord[8]));

        IMapObject mapObject1 = map.get(coord[0]);
        IMapObject mapObject2 = map.get(coord[3]);
        IMapObject mapObject3 = map.get(coord[6]);

        map.remove(coord[1]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject1);
        ActionBuilder.setActionType(ActionType.BUILD_FARM);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[1]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){ }

        assumeTrue(map.get(coord[1]).getType() == MapObjectType.FARM);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject2);
        ActionBuilder.setActionType(ActionType.BUILD_MINE);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){ }

        assumeTrue(map.get(coord[4]).getType() == MapObjectType.MINE);

        map.remove(coord[7]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject3);
        ActionBuilder.setActionType(ActionType.BUILD_CABIN);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[7]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){ }

        assumeTrue(map.get(coord[7]).getType() == MapObjectType.CABIN);
    }

    @Test
    void buildProducerWrongPlaceTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_CABIN);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(((Character)mapObject).canDoActionInRound());
        }else{
            assumeTrue(false);
        }
    }

    // túl messze
    @Test
    void buildOutOfReachTest() {
        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(map.get(coord[8]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeFalse(map.get(coord[8]).getType() == MapObjectType.BASTION);
        }else{
            assumeTrue(false);
        }
    }

    // nem üres hely
    @Test
    void buildNotEmptyTest() {
        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(map.get(coord[6]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeFalse(map.get(coord[6]).getType() == MapObjectType.BASTION);
            assumeTrue(map.get(coord[6]).getType() == MapObjectType.MAIN);
        }else{
            assumeTrue(false);
        }
    }

    // nincs elég alapanyag
    @Test
    void buildNotEnoughMaterialTest() {
        red.takeMaterial(new Cost(red.getGold(), red.getWood(), red.getFood()));

        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));
   
        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }

        if(b){
            assumeTrue(map.get(coord[4]) == null);
        }else{
            assumeTrue(false);
        }
    }

    // épitkezés megy körönként
    @Test
    void buildRoundTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));
   
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[4]).getType() == MapObjectType.BASTION);
        assumeFalse(((Building)map.get(coord[4])).isBuilt());

        while(!((Building)map.get(coord[4])).isBuilt()){
            RoundHelper.newRound();
            RoundHelper.newRound();
        }

        assumeTrue(((Building)map.get(coord[4])).isBuilt());
    }

    // építés alatt nem használható
    @Test
    void unusableWhileBuildTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[4]));
   
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[4]).getType() == MapObjectType.BASTION);
        assumeFalse(((Building)map.get(coord[4])).isBuilt());

        RoundHelper.newRound();
        RoundHelper.newRound();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeFalse(((Building)map.get(coord[4])).isBuilt());

        mapObject = map.get(coord[4]);

        map.remove(coord[7]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_WORKER);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[7]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }

        if(b){
            assumeTrue(map.get(coord[7]) == null);
        }else{
            assumeTrue(false);
        }
    }

    // amennyi paraszt annyi * resource
    @Test
    void producerOneWorkerTest() {
        map.replace(coord[4], new Building(MapObjectType.FARM, red, coord[4]));
        ((Building)map.get(coord[4])).progress(20);

        int foodBefore = red.getFood();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeFalse(foodBefore == red.getFood());
    }

    @Test
    void producerMultipleWorkersTest() {
        map.replace(coord[4], new Building(MapObjectType.FARM, red, coord[4]));
        ((Building)map.get(coord[4])).progress(20);

        int foodBefore = red.getFood();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeFalse(foodBefore == red.getFood());
        int oneWorker = red.getFood() - foodBefore;
        foodBefore = red.getFood();

        map.replace(coord[7], new Character(MapObjectType.WORKER, red, coord[7]));

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeFalse(foodBefore == red.getFood());
        int twoWorkers = red.getFood() - foodBefore;
        assumeTrue(twoWorkers > oneWorker);
    }

    // nincs paraszt nincs termelés
    @Test
    void producerNoWorkerTest() {
        map.replace(coord[8], new Building(MapObjectType.FARM, red, coord[8]));
        ((Building)map.get(coord[8])).progress(20);

        int foodBefore = red.getFood();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeTrue(foodBefore == red.getFood());
    }

    // összes alapanyag tesztje
    @Test
    void producerAllMaterialsTest() {
        map.replace(coord[0], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[0]));
        map.replace(coord[6], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[6]));

        map.replace(coord[2], new Terrain(MapObjectType.MEADOW, coord[2]));
        map.replace(coord[5], new Terrain(MapObjectType.GOLD_DEPOSIT, coord[5]));
        map.replace(coord[8], new Terrain(MapObjectType.FOREST, coord[8]));

        map.replace(coord[1], new Building(MapObjectType.FARM, red, coord[1]));
        ((Building)map.get(coord[1])).progress(20);
        map.replace(coord[4], new Building(MapObjectType.MINE, red, coord[4]));
        ((Building)map.get(coord[4])).progress(20);
        map.replace(coord[7], new Building(MapObjectType.CABIN, red, coord[7]));
        ((Building)map.get(coord[7])).progress(20);

        int foodBefore = red.getFood();
        int woodBefore = red.getWood();
        int goldBefore = red.getGold();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeTrue(foodBefore < red.getFood());
        assumeTrue(woodBefore < red.getWood());
        assumeTrue(goldBefore < red.getGold());
    }

    // ha még nincs kész, nem termel
    @Test
    void producerNotBuildYetTest() {
        map.replace(coord[4], new Building(MapObjectType.FARM, red, coord[4]));

        int foodBefore = red.getFood();

        RoundHelper.newRound();
        RoundHelper.newRound();

        assumeTrue(foodBefore == red.getFood());
    }

    // mozgáspont fogy
    @Test
    void moveOneStepTest() {
        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);

        map.remove(coord[4]);

        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));
        assumeTrue(((Character)mapObject).canMove());
    }

    // mozgáspont kifogy -> nincs mozgás
    @Test
    void moveTooManyStepsTest() {
        GameState.setPlayer1(red);
        GameState.setPlayer2(blue);
        GameState.setActualPlayer(GameState.getPlayer1());

        map = new ConcurrentHashMap<>();

        GameState.setMap(map);

        Coordinate[] newCoords = new Coordinate[22];
        for(int i = 1; i < 22; i++){
            newCoords[i-1] = new Coordinate(i, 0);
        }

        Coordinate c = new Coordinate(0, 0);
        map.put(c, new Character(MapObjectType.ARMORED, red, c));
        IMapObject mapObject = map.get(c);

        int i = 0;
        boolean b = false;
        while(!b && i < 22){
            ActionBuilder.clear();
            ActionBuilder.setSelected(mapObject);
            ActionBuilder.setActionType(ActionType.MOVE);
            ActionBuilder.setProgressed(newCoords[i]);

            System.out.println(newCoords[i].getX() + " " + newCoords[i].getY());

            try{
                ActionBuilder.resolveAction();
            }catch(GameException e){
                b = true;
            }

            i++;
        }

        if(b){
            assumeFalse(((Character)mapObject).canMove());
        }else{
            assumeTrue(false);
        }
    }

    // már végzett más akciót
    @Test
    void moveAlreadyDoneAcionTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[0]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[0]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[0]).getType() == MapObjectType.BASTION);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(mapObject.getPosition().equals(coord[3]));
            assumeTrue(((Character)mapObject).canMove());
        }else{
            assumeTrue(false);
        }
    }

    // akadályra nem lehet lépni
    @Test
    void moveObstacleTest() {
        map.replace(coord[4], new Terrain(MapObjectType.MOUNTAIN, coord[4]));

        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeFalse(mapObject.getPosition().equals(coord[4]));
            assumeTrue(((Character)mapObject).canMove());
        }else{
            assumeTrue(false);
        }
    }

    // sárkány tud akadályra lépni
    @Test
    void moveObstacleDragonTest() {
        map.replace(coord[4], new Terrain(MapObjectType.FOREST, coord[4]));
        map.replace(coord[3], new Character(MapObjectType.DRAGON, GameState.getPlayer1(), coord[3]));

        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));
        assumeTrue(((Character)mapObject).canMove());
    }

    // a karakter elkészül az adott helyen, az adott játékoshoz
    @Test
    void createWorkerTest() {
        IMapObject mapObject = map.get(coord[6]);

        map.remove(coord[7]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_WORKER);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[7]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[7]).getType() == MapObjectType.WORKER);
        assumeTrue(map.get(coord[7]).getPlayer() == GameState.getActualPlayer());
    }

    // támadó játékosok készítése
    @Test
    void createSoildersTest() {
        map = new ConcurrentHashMap<>();

        map.put(coord[4], new Building(MapObjectType.CABIN, red, coord[4]));
        ((Building)map.get(coord[4])).progress(30);

        GameState.setMap(map);

        IMapObject mapObject = map.get(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_PITCHFORK);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[1]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[1]).getType() == MapObjectType.PITCHFORK);
        assumeTrue(map.get(coord[1]).getPlayer() == GameState.getActualPlayer());

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_ARMORED);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[3]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[3]).getType() == MapObjectType.ARMORED);
        assumeTrue(map.get(coord[3]).getPlayer() == GameState.getActualPlayer());

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_KNIGHT);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[5]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[5]).getType() == MapObjectType.KNIGHT);
        assumeTrue(map.get(coord[5]).getPlayer() == GameState.getActualPlayer());

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_DRAGON);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[7]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[7]).getType() == MapObjectType.DRAGON);
        assumeTrue(map.get(coord[7]).getPlayer() == GameState.getActualPlayer());
    }

    // túl messze van épülettől
    @Test
    void createOutOfReachTest() {
        IMapObject mapObject = map.get(coord[6]);

        map.remove(coord[8]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_WORKER);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[8]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(map.get(coord[8]) == null);
        }else{
            assumeTrue(false);
        }
    }

    // nem üres hely 
    @Test
    void createNotEmptyTest() {
        IMapObject mapObject = map.get(coord[6]);

        map.replace(coord[7], new Terrain(MapObjectType.MOUNTAIN, coord[7]));

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_WORKER);
        ActionBuilder.setDestination(map.get(coord[7]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeFalse(map.get(coord[7]).getType() == MapObjectType.WORKER);
            assumeFalse(map.get(coord[7]).getPlayer() == GameState.getActualPlayer());
        }else{
            assumeTrue(false);
        }
    }

    // nincs elég alapanyag
    @Test
    void createNotEnoughMaterialTest() {
        red.takeMaterial(new Cost(red.getGold(), red.getWood(), red.getFood()));

        IMapObject mapObject = map.get(coord[6]);

        map.remove(coord[7]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.CREATE_WORKER);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[7]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(map.get(coord[7]) == null);
        }else{
            assumeTrue(false);
        }
    }

    // roll 20 + hit ha nagyobb mint a támadott shield-je akkor sebez
    @Test
    void attackTest() {
        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[5]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeFalse(((Character)mapObject).canDoActionInRound());
    }

    // épület támadása
    @Test
    void attackBuildingTest() {
        map.replace(coord[2], new Character(MapObjectType.WORKER, blue, coord[2]));
        map.replace(coord[5], new Building(MapObjectType.MAIN, blue, coord[5]));

        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));

        int hpBefore = map.get(coord[5]).getLife();

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[5]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[5]).getLife() < hpBefore);
        assumeFalse(((Character)mapObject).canDoActionInRound());
    }

    @Test
    void attackShieldTest() {
        map.replace(coord[5], new Character(MapObjectType.ARMORED, blue, coord[5]));

        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));

        int hpBefore = map.get(coord[5]).getLife();

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[5]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeFalse(((Character)mapObject).canDoActionInRound());
        assumeTrue(hpBefore == map.get(coord[5]).getLife());
    }

    // játékos saját karaktert támad
    @Test
    void attackSamePlayerTest() {
        map.replace(coord[5], new Character(MapObjectType.WORKER, GameState.getPlayer1(), coord[5]));

        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[4]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.MOVE);
        ActionBuilder.setProgressed(coord[4]);

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(mapObject.getPosition().equals(coord[4]));

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[5]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(((Character)mapObject).canDoActionInRound());
        }else{
            assumeTrue(false);
        }
    }

    // már végzett más akciót
    @Test
    void attackAlreadyDoneAcionTest() {
        map.replace(coord[4], new Character(MapObjectType.WORKER, GameState.getPlayer2(), coord[4]));

        IMapObject mapObject = map.get(coord[3]);

        map.remove(coord[0]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.BUILD_BASTION);
        ActionBuilder.setDestination(new Terrain(MapObjectType.MEADOW, coord[0]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeTrue(map.get(coord[0]).getType() == MapObjectType.BASTION);
        assumeFalse(((Character)mapObject).canDoActionInRound());

        int hpBefore = map.get(coord[5]).getLife();

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[4]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(hpBefore == map.get(coord[5]).getLife());
        }else{
            assumeTrue(false);
        }
    }

    // túl messze van ellenfél
    @Test
    void attackOutOfReachTest() {
        IMapObject mapObject = map.get(coord[3]);

        int hpBefore = map.get(coord[5]).getLife();

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.ATTACK);
        ActionBuilder.setDestination(map.get(coord[5]));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }
        
        if(b){
            assumeTrue(((Character)mapObject).canDoActionInRound());
            assumeTrue(hpBefore == map.get(coord[5]).getLife());
        }else{
            assumeTrue(false);
        }
    }

    // sárkány különleges támadása
    @Test
    void attackDragonTest() {
        map.replace(coord[3], new Character(MapObjectType.DRAGON, GameState.getPlayer1(), coord[3]));
        map.replace(coord[5], new Character(MapObjectType.DRAGON, GameState.getPlayer2(), coord[5]));

        IMapObject mapObject = map.get(coord[3]);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.DRAGON_BREATH);
        ActionBuilder.setDestination(map.get(coord[5]));

        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){}

        assumeFalse(((Character)mapObject).canDoActionInRound());
    }

    // sárkány különleges támadása, de túl távolról
    @Test
    void attackOutOfReachDragonTest() {
        GameState.setPlayer1(red);
        GameState.setPlayer2(blue);
        GameState.setActualPlayer(GameState.getPlayer1());

        map = new ConcurrentHashMap<>();

        GameState.setMap(map);

        Coordinate[] newCoords = new Coordinate[22];
        for(int i = 1; i < 22; i++){
            newCoords[i-1] = new Coordinate(i, 0);
        }

        Coordinate c2 = new Coordinate(22, 0);
        map.put(c2, new Character(MapObjectType.WORKER, blue, c2));

        Coordinate c1 = new Coordinate(0, 0);
        map.put(c1, new Character(MapObjectType.DRAGON, red, c1));
        IMapObject mapObject = map.get(c1);

        ActionBuilder.clear();
        ActionBuilder.setSelected(mapObject);
        ActionBuilder.setActionType(ActionType.DRAGON_BREATH);
        ActionBuilder.setDestination(map.get(c2));

        boolean b = false;
        try{
            ActionBuilder.resolveAction();
        }catch(GameException e){
            b = true;
        }

        if(b){
            assumeTrue(((Character)mapObject).canDoActionInRound());
        }else{
            assumeTrue(false);
        }
    }
}

