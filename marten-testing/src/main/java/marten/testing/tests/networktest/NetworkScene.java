package marten.testing.tests.networktest;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;

public class NetworkScene extends AgeScene {

    private Flatland flatland = null;

    public NetworkScene() {
        this.flatland = new Flatland();
    }

    @Override
    public void compute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        flatland.render();
    }

}
