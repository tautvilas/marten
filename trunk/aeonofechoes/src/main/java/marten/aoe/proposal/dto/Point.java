package marten.aoe.proposal.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Point implements Serializable {
    private static final long serialVersionUID = 7434596421152359098L;
    private final int x;
    private final int y;
    public Point (int x, int y ) {
        this.x = x;
        this.y = y;
    }
    public final int getX () {
        return this.x;
    }
    public final int getY () {
        return this.y;
    }
    public final Point adjacent (Direction direction) {
        return direction.adjust(this);
    }
    public final int distanceTo (Point other) {
        int minimumEstimate = other.x - this.x;
        minimumEstimate *= (minimumEstimate < 0 ? -1 : 1);
        int minY = this.y - minimumEstimate / 2;
        int maxY = this.y + minimumEstimate / 2;
        if (minimumEstimate % 2 != 0) {
            if (this.x % 2 == 0) {
                --minY;
            }
            else {
                ++maxY;
            }
        }
        if (other.y > maxY) {
            return minimumEstimate + other.y - maxY;
        }
        if (other.y < minY) {
            return minimumEstimate + minY - other.y;
        }
        return minimumEstimate;
    }
    public final List<Point> neighbors (int distance) {
        List<Point> answer = new ArrayList<Point>();
        for (int x = this.x - distance; x <= this.x + distance; ++x) {
            for (int y = this.y - distance; y <= this.y + distance; ++y) {
                Point candidate = new Point(x, y);
                if (this.distanceTo(candidate) <= distance) {
                    answer.add(candidate);
                }
            }
        }
        return answer;
    }
}
