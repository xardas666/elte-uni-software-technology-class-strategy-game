package hu.elte.csapat4.models.map;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate that) {
        this.x = that.x;
        this.y = that.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean neighboring(Coordinate coordinate) {
        double distance = Point2D.distance(
                coordinate.getX(),
                coordinate.getY(),
                this.getX(),
                this.getY()
        );

        return distance <= 1.7d && distance >= -1.7d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}

