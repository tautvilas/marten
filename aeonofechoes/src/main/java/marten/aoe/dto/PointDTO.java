package marten.aoe.dto;

import java.io.Serializable;

public final class PointDTO implements Serializable {
    private static final long serialVersionUID = 7434596421152359098L;
    private final int x;
    private final int y;
    public PointDTO (int x, int y ) {
        this.x = x;
        this.y = y;
    }
    public final int getX () {
        return this.x;
    }
    public final int getY () {
        return this.y;
    }
    @Override public String toString() {
        return this.x + " " + this.y;
    }
    @Override public final int hashCode () {
        // Somewhat evil code follows
        int realY = this.y;
        int invertedY = 0;
        for (int index = 0; index < Integer.SIZE; index++) {
            invertedY <<= 1;
            invertedY |= (realY & 1);
            realY >>= 1;
        }
        return this.x ^ invertedY;
    }
    @Override public final boolean equals (Object other) {
        // Evil code follows
        if (other != null && other instanceof PointDTO) {
            PointDTO otherPoint = (PointDTO)other;
            return this.x == otherPoint.x && this.y == otherPoint.y;
        }
        return false;
    }
}
