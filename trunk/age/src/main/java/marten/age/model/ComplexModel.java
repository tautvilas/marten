package marten.age.model;

import java.util.ArrayList;

import marten.age.BasicSceneGraphChild;
import marten.age.geometry.SimpleModel;

public class ComplexModel extends BasicSceneGraphChild {
	private ArrayList<SimpleModel> parts = new ArrayList<SimpleModel>();
	public void addPart (SimpleModel newPart) {
		parts.add (newPart);
	}
	public void removePart (SimpleModel oldPart) {
		parts.remove(oldPart);
	}
	public SimpleModel getFirstPart() {
		return parts.get(0);
	}
	public ArrayList<SimpleModel> getParts() {
		return parts;
	}
	public void render() {
		for (SimpleModel part : parts)
			part.render();
	}	
}