package marten.age.event;

public class AgeEvent {
    public AgeEvent(String id, Object ... parameters) {
        System.out.println(parameters);
    }
}
