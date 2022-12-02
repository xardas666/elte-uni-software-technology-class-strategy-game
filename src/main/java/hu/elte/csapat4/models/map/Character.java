package hu.elte.csapat4.models.map;

import com.google.common.base.Objects;
import hu.elte.csapat4.models.Player;
import hu.elte.csapat4.models.details.CharacterDetails;
import hu.elte.csapat4.settings.ModelSettings;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Log
public class Character implements IMapObject {
    //From detail
    private final int hit;
    private final int shield;
    private final int distance;
    private int distanceInRound;
    private boolean canDoActionInRound;

    private final ArrayList<ActionType> actions;
    private final Player player;
    private final MapObjectType mapObjectType;

    private int life;
    private int originalLife;
    private Coordinate position;

    public Character(MapObjectType mapObjectType, Player player, Coordinate position) {
        this.player = player;
        this.position = position;
        this.mapObjectType = mapObjectType;

        CharacterDetails details = (CharacterDetails) ModelSettings.getDetailFor(mapObjectType);
        hit = details.getHit();
        shield = details.getShield();
        distance = details.getDistance();
        life = details.getLife();
        originalLife = details.getLife();
        actions = details.getActions();

        this.distanceInRound = distance;
        this.canDoActionInRound = true;
    }

    @Override
    public boolean isStanding() {
        return life >= 0;
    }

    @Override
    public ArrayList<ActionType> getActions() {
        if (canDoActionInRound) {
            return actions;
        } else {
            return new ArrayList<>();
        }

    }

    public void resetAfterRound() {
        this.distanceInRound = distance;
        this.canDoActionInRound = true;
    }

    @Override
    public void addDamage(IMapObject attackerMapObject) {
        Character attacker = ((Character) attackerMapObject);

        int roll = ThreadLocalRandom.current().nextInt(0, 20);
        roll += attackerMapObject.getHitPoints();
        if (shield <= roll) {
            log.info(infoText() + " takes damage: " + attacker.getHitPoints() + "!");
            life -= attacker.getHitPoints();
        } else {
            log.info(attackerMapObject.infoText() + " missed the target!");
        }

    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public int getShield() {
        return shield;
    }

    @Override
    public int getHitPoints() {
        return hit;
    }

    @Override
    public MapObjectType getType() {
        return mapObjectType;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Coordinate getPosition() {
        return position;
    }

    public void attack(IMapObject attackee) {
        log.info(infoText() + "attacked " + attackee.infoText() + "!");
        if (canDoActionInRound) {
            attackee.addDamage(this);
            didAction();
        }
    }

    public boolean move(Coordinate position) {
        if (distanceInRound >= 0) {
            this.position = new Coordinate(position);
            this.distanceInRound -= 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean canMove() {
        return distanceInRound > 0;
    }


    public boolean canDoActionInRound() {
        return canDoActionInRound;
    }

    public void didAction() {
        log.info(infoText() + " made an action!");
        this.canDoActionInRound = false;
    }

    @Override
    public String infoText() {
        return mapObjectType.name() +
                " hit: " + hit +
                ", shield: " + shield +
                ", distance: " + distance + "/" + distanceInRound +
                ", canDoActionInRound: " + canDoActionInRound +
                ", life: " + originalLife+ "/"+ life +
                ", at: " + position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return hit == character.hit && getShield() == character.getShield() && distance == character.distance && distanceInRound == character.distanceInRound && canDoActionInRound == character.canDoActionInRound && getLife() == character.getLife() && Objects.equal(getActions(), character.getActions()) && Objects.equal(getPlayer(), character.getPlayer()) && mapObjectType == character.mapObjectType && Objects.equal(getPosition(), character.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hit, getShield(), distance, distanceInRound, canDoActionInRound, getActions(), getPlayer(), mapObjectType, getLife(), getPosition());
    }
}
