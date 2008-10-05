package marten.age;

import java.util.ArrayList;

public class ComplexModel implements SceneGraphChild {
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
	@Override
	public void activate() {
		for (SimpleModel part : parts)
			part.activate();
	}	
}
