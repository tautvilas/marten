package marten.testing.util;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.model.ComplexModel;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.Rotation;
import marten.age.graphics.transform.RotationGroup;
import marten.age.graphics.transform.SymetricScaleGroup;
import marten.age.graphics.transform.TranslationGroup;

// Implements Visual, Collidable, Physical turi but.
public class Body {
	
	private ComplexModel model = null;
	
	private TranslationGroup translationGroup = new TranslationGroup();
	private RotationGroup rotationGroup = new RotationGroup();
	private SymetricScaleGroup scaleGroup = new SymetricScaleGroup();
	
	private boolean hidden = false;
	private boolean graphCreated = false;
	
	private Rotation heading = new Rotation();
	private Point position = new Point();
	private double radius = 1.0;
	
	public Body(ComplexModel model) {
		this.model = model;
		createSceneGraph();
	}
	
	public void setHeading(Rotation r) {
	    this.heading = r;
	    rotationGroup.setRotation(this.heading);
	}
	
	public Rotation getHeading() {
	    return this.heading;
	}
	
	public Point getPosition() {
	    return this.position;
	}
	
	public void createSceneGraph() {
		if (model == null) {
			throw new RuntimeException("VisualSpaceObject: model not set");
		}
		scaleGroup.setScaleFactor(this.radius);
		translationGroup.setPosition(this.position);
		rotationGroup.setRotation(this.heading);
		scaleGroup.addChild(model);
		rotationGroup.addChild(scaleGroup);
		translationGroup.addChild(rotationGroup);
		
		graphCreated = true;
	}

	public void hide(boolean hide) {
		if (!hidden && hide) {
			rotationGroup.removeChild(model);
			hidden = true;
		} else if (hidden && !hide) {
			rotationGroup.addChild(model);
			hidden = false;
		}
	}

	/*
	public void display() {
		if (!graphCreated) createSceneGraph();
		translationGroup.setCoordinates(this.position);
		rotationGroup.setRotation(this.heading);
		translationGroup.activate();
	}
	*/

	public SceneGraphChild getSceneGraph() {
		if (!graphCreated) createSceneGraph();
		return translationGroup;
	}
}
