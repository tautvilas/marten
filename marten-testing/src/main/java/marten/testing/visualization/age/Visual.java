package marten.testing.visualization.age;

import marten.age.SceneGraphChild;


public interface Visual {
	public SceneGraphChild createSceneGraphChild();
	
	public void updateScene();
}
