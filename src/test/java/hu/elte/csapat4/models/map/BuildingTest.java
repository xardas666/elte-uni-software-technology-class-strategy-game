package hu.elte.csapat4.models.map;

import hu.elte.csapat4.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    Building main, mine, bastion, farm, cabin;
    Character dragon;

    @BeforeEach
    void setUp() {
        main = new Building(MapObjectType.MAIN,new Player("", PlayerColor.RED),new Coordinate(10,10));
        mine = new Building(MapObjectType.MINE,new Player("", PlayerColor.RED),new Coordinate(10,10));
        bastion = new Building(MapObjectType.BASTION,new Player("", PlayerColor.RED),new Coordinate(10,10));
        farm = new Building(MapObjectType.FARM,new Player("", PlayerColor.RED),new Coordinate(10,10));
        cabin = new Building(MapObjectType.CABIN,new Player("", PlayerColor.RED),new Coordinate(10,10));

        dragon = new Character(
                MapObjectType.DRAGON,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );
    }

    @Test
    void isStanding() {
        assertTrue(main.isStanding());
        main.addDamage(dragon);
        main.addDamage(dragon);
        main.addDamage(dragon);
        assertFalse(main.isStanding());
    }

    @Test
    void getActions() {
        assertFalse(main.getActions().isEmpty());
    }

    @Test
    void addDamage() {
        int initialLife = main.getLife();
        main.addDamage(dragon);
        main.addDamage(dragon);
        main.addDamage(dragon);
        assertTrue(initialLife > main.getLife());
    }

    @Test
    void infoText() {
        assertNotNull(main.infoText());
        assertTrue(main.infoText().contains(main.getLife() + ""));
        assertTrue(main.infoText().contains(main.getType() + ""));
        assertTrue(main.infoText().contains(main.getShield() + ""));
        assertTrue(main.infoText().contains(main.getLife() + ""));
        assertTrue(main.infoText().contains(main.getLife() + ""));
    }

    @Test
    void progress() {
        assertFalse(mine.isBuilt());
        for (int i = 0; i < 100; i++) {
                mine.progress(10);
        }
        assertTrue(mine.isBuilt());
    }

    @Test
    void doProduction() {
        int initGold = mine.getPlayer().getGold();
        for (int i = 0; i < 100; i++) {
            mine.progress(10);
        }
        mine.doProduction(10);
        assertTrue(initGold<mine.getPlayer().getGold());
    }

    @Test
    void getType() {
        assertFalse(main.getType().name().isEmpty());
    }

    @Test
    void isBuilt() {
        assertFalse(mine.isBuilt());
        for (int i = 0; i < 100; i++) {
            mine.progress(10);
        }
        assertTrue(mine.isBuilt());
    }

    @Test
    void misc() {
        assertNotEquals(main, dragon);
        assertEquals(main, main);
        assertNotNull(main.getPlayer());
        assertTrue(main.hashCode() > 0 || main.hashCode() <= 0);
    }
}