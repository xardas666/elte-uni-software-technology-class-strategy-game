package hu.elte.csapat4.models.map;

import hu.elte.csapat4.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterTest {

    Character testCharacter, dragon, worker, pitchfork, armored;

    @BeforeEach
    void setup() {
        testCharacter = new Character(
                MapObjectType.KNIGHT,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );

        dragon = new Character(
                MapObjectType.DRAGON,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );

        worker = new Character(
                MapObjectType.WORKER,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );
        pitchfork = new Character(
                MapObjectType.PITCHFORK,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );
        armored = new Character(
                MapObjectType.ARMORED,
                new Player("", PlayerColor.BLUE),
                new Coordinate(10, 10)
        );
    }

    @Test
    void isStanding() {
        assertTrue(testCharacter.isStanding());
        testCharacter.addDamage(dragon);
        testCharacter.addDamage(dragon);
        testCharacter.addDamage(dragon);
        assertFalse(testCharacter.isStanding());
    }

    @Test
    void getActions() {
        assertFalse(testCharacter.getActions().isEmpty());
        testCharacter.didAction();
        assertTrue(testCharacter.getActions().isEmpty());
    }

    @Test
    void resetAfterRound() {
        assertTrue(testCharacter.canDoActionInRound());
        testCharacter.didAction();
        assertFalse(testCharacter.canDoActionInRound());
        testCharacter.resetAfterRound();
        assertTrue(testCharacter.canDoActionInRound());
    }

    @Test
    void addDamage() {
        int initialLife = testCharacter.getLife();
        testCharacter.addDamage(dragon);
        testCharacter.addDamage(dragon);
        testCharacter.addDamage(dragon);
        assertTrue(initialLife > testCharacter.getLife());
    }

    @Test
    void attack() {
        worker.attack(testCharacter);
        worker.attack(testCharacter);
        worker.attack(testCharacter);

        testCharacter.attack(worker);
        testCharacter.attack(worker);
        testCharacter.attack(worker);
        testCharacter.attack(worker);
        testCharacter.attack(worker);

        assertFalse(worker.isStanding());

        testCharacter.attack(worker);
    }

    @Test
    void move() {
        assertTrue(testCharacter.canMove());

        Coordinate fel = new Coordinate(10, 9);
        Coordinate le = new Coordinate(10, 10);
        Coordinate jobbra = new Coordinate(9, 10);
        Coordinate balra = new Coordinate(10, 10);

        testCharacter.move(fel);
        assertEquals(testCharacter.getPosition(), fel);

        testCharacter.move(le);
        assertEquals(testCharacter.getPosition(), le);

        testCharacter.move(jobbra);
        assertEquals(testCharacter.getPosition(), jobbra);

        testCharacter.move(balra);
        assertEquals(testCharacter.getPosition(), balra);
    }

    @Test
    void canMove() {
        assertTrue(testCharacter.canMove());

        for (int x = 0; x < 100; x++) {
            testCharacter.move(new Coordinate(x, 0));
        }

        assertFalse(testCharacter.canMove());
    }

    @Test
    void canDoActionInRound() {
        assertTrue(testCharacter.canDoActionInRound());
        testCharacter.didAction();
        assertFalse(testCharacter.canDoActionInRound());
    }

    @Test
    void didAction() {
        assertTrue(testCharacter.canDoActionInRound());
        testCharacter.didAction();
        assertFalse(testCharacter.canDoActionInRound());
    }

    @Test
    void infoText() {
        assertNotNull(testCharacter.infoText());
        assertTrue(testCharacter.infoText().contains(testCharacter.getLife() + ""));
        assertTrue(testCharacter.infoText().contains(testCharacter.getType() + ""));
        assertTrue(testCharacter.infoText().contains(testCharacter.getShield() + ""));
        assertTrue(testCharacter.infoText().contains(testCharacter.getLife() + ""));
        assertTrue(testCharacter.infoText().contains(testCharacter.getLife() + ""));
    }

    @Test
    void misc() {
        assertNotEquals(worker, dragon);
        assertEquals(worker, worker);
        assertNotNull(testCharacter.getPlayer());
        assertTrue(worker.hashCode() > 0 || worker.hashCode() <= 0);
    }
}