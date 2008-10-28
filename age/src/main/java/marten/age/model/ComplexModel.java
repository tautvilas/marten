package marten.age.model;

import java.util.ArrayList;

import marten.age.SceneGraphChild;
import marten.age.geometry.SimpleModel;

public class ComplexModel extends SceneGraphChild {
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
	public void activate() {
		for (SimpleModel part : parts)
			part.activate();
	}	
}
