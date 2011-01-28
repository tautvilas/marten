package marten.aoe.dto;

import java.io.Serializable;

public final class UnitDTO implements Serializable {
    private static final long serialVersionUID = 8927124760274010740L;
    private final String name;
    public UnitDTO (String name) {
        this.name = name;
    }
    public final String getName () {
        return this.name;
    }
}
