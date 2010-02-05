package marten.age.event;

public class AgeEvent {
    public Object[] parameters;
    public String id;

    public AgeEvent(String id, Object ... parameters) {
        this.parameters = parameters;
        this.id = id;
    }
}
