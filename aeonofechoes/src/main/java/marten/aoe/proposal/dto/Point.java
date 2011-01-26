package marten.aoe.proposal.dto;

import java.io.Serializable;

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
}
