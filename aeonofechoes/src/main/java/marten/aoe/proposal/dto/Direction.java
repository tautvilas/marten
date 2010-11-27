package marten.aoe.proposal.dto;

public enum Direction {
    N(0, 1, 1),
    NE(1, 0, 1),
    SE(1, -1, 0),
    S(0, -1, -1),
    SW(-1, -1, 0),
    NW(-1, 0, 1);
    private int dx;
    private int dy0;
    private int dy1;
    private Direction (int dx, int dy0, int dy1) {
        this.dx = dx;
        this.dy0 = dy0;
        this.dy1 = dy1;
    }
    public Point adjust (Point point) {
        if (point.getX() % 2 == 0) {
            return new Point(point.getX() + dx, point.getY() + dy0);
        }
        else {
            return new Point(point.getX() + dx, point.getY() + dy1);
        }
    }
}
