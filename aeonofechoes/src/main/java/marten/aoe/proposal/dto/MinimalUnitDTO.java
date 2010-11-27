package marten.aoe.proposal.dto;

import java.io.Serializable;

public final class MinimalUnitDTO implements Serializable {
    private static final long serialVersionUID = 8927124760274010740L;
    private final String name;
    public MinimalUnitDTO (String name) {
        this.name = name;
    }
    public final String getName () {
        return this.name;
    }
}
