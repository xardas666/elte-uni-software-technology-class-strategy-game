package hu.elte.csapat4.logics;

import hu.elte.csapat4.models.map.ActionType;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;

public class ActionBuilder {
    private static IMapObject selected;
    private static IMapObject destination;
    private static Coordinate progressed;
    private static ActionType actionType;
    private static boolean inAction;

    public static IMapObject getSelected() {
        return selected;
    }

    public static void setSelected(IMapObject object) {
        inAction = true;
        selected = object;
    }

    public static IMapObject getDestination() {
        return destination;
    }

    public static void setDestination(IMapObject object) {
        inAction = true;
        destination = object;
    }

    public static boolean alreadyInAction() {
        return inAction;
    }

    public static ActionType getActionType() {
        return actionType;
    }

    public static void setActionType(ActionType object) {
        inAction = true;
        actionType = object;
    }

    public static Coordinate getProgressed() {
        return progressed;
    }

    public static void setProgressed(Coordinate progressed) {
        ActionBuilder.progressed = progressed;
    }

    public static void clear() {
        inAction = false;
        destination = null;
        selected = null;
        actionType = null;
        progressed = null;
    }

    public static void resolveAction() throws GameException {
        switch (actionType) {
            case MOVE:
                ActionHelper.move(selected, progressed);
                break;
            case ATTACK:
            case DRAGON_BREATH:
                ActionHelper.attack(selected, actionType, destination);
                break;
            case CREATE_WORKER:
            case CREATE_KNIGHT:
            case CREATE_ARMORED:
            case CREATE_PITCHFORK:
            case CREATE_DRAGON:
                ActionHelper.create(selected, getMapObjectType(), destination.getPosition());
                break;
            case BUILD_FARM:
            case BUILD_MINE:
            case BUILD_CABIN:
            case BUILD_BASTION:
                ActionHelper.build(selected, getMapObjectType(), destination.getPosition());
                break;
            default:
        }

    }

    private static MapObjectType getMapObjectType() {
        return MapObjectType.valueOf(actionType.name().split("_")[1]);
    }
}
