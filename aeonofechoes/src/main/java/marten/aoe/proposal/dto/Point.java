package marten.aoe.proposal.dto;

public final class Point {
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
}
