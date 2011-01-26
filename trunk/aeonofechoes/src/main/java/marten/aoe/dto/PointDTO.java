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
}
