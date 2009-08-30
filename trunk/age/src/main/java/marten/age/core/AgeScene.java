package marten.age.core;

import marten.age.event.AgeEvent;

public interface AgeScene {
    /* Method for initializing the app */
    public void init();

    /* This method is called each time an event happens */
    public void handle(AgeEvent e);

    /* Game "business" logic should go here */
    public void compute();

    /* All the graphics related code goes here */
    public void render();

    /* Method for destroying unwanted stuff */
    public void cleanup();
}
