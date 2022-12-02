package hu.elte.csapat4.models.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerrainTest {

    Terrain goldDeposit, meadow, forest, mountain, rock;

    @BeforeEach
    void setUp() {
        goldDeposit = new Terrain(MapObjectType.GOLD_DEPOSIT, new Coordinate(10, 10));
        meadow = new Terrain(MapObjectType.MEADOW, new Coordinate(10, 10));
        forest = new Terrain(MapObjectType.FOREST, new Coordinate(10, 10));
        mountain = new Terrain(MapObjectType.MOUNTAIN, new Coordinate(10, 10));
        rock = new Terrain(MapObjectType.ROCK, new Coordinate(10, 10));
    }

    @Test
    void isStanding() {
        assertTrue(goldDeposit.isStanding());
    }

    @Test
    void getActions() {
        assertTrue(goldDeposit.getActions().isEmpty());
    }

    @Test
    void addDamage() {
        int initialLife = goldDeposit.getLife();

        goldDeposit.addDamage(meadow);
        goldDeposit.addDamage(meadow);
        goldDeposit.addDamage(meadow);
        goldDeposit.addDamage(meadow);

        assertEquals(goldDeposit.getLife(), initialLife);
    }

    @Test
    void infoText() {
        assertNotNull(goldDeposit.infoText());
        assertTrue(goldDeposit.infoText().contains(goldDeposit.getType()+""));
    }
}