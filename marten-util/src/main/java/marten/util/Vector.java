package marten.util;

public final class Vector {
	public static final Vector ZERO = new Vector();
	public double x = 0.0, y = 0.0, z = 0.0;
	public Vector () {}
	public Vector (Vector source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	public Vector (double newX, double newY, double newZ) {
		this.x = newX;
		this.y = newY;
		this.z = newZ;
	}
	public Vector (Point start, Point finish) {
		this.x = finish.x - start.x;
		this.y = finish.y - start.y;
		this.z = finish.z - start.z;
	}
	public synchronized boolean equals (Vector other) {
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
	}
	public synchronized boolean approximatelyEquals (Vector other, double epsilon) {
		if (epsilon < 0.0)
			throw new RuntimeException ("Attempted approximate comparison with epsilon less than zero.");
		return (this.x > other.x * (1 - epsilon)) && (this.x < other.x * (1 + epsilon)) && (this.y > other.y * (1 - epsilon)) && (this.y < other.y * (1 + epsilon)) && (this.z > other.z * (1 - epsilon)) && (this.z < other.z * (1 + epsilon)); 			
	}
	public synchronized Vector scale (double scaleFactor) {
		Vector temp = new Vector (this);
		temp.x *= scaleFactor;
		temp.y *= scaleFactor;
		temp.z *= scaleFactor;
		return temp;
	}
	public synchronized Vector _scale (double scaleFactor) {
		this.x *= scaleFactor;
		this.y *= scaleFactor;
		this.z *= scaleFactor;
		return new Vector (this);
	}
	public synchronized Vector scaleAssymetrically (double scaleX, double scaleY, double scaleZ) {
		Vector temp = new Vector (this);
		temp.x *= scaleX;
		temp.y *= scaleY;
		temp.z *= scaleZ;
		return temp;
	}
	public synchronized Vector _scaleAssymetrically (double scaleX, double scaleY, double scaleZ) {
		this.x *= scaleX;
		this.y *= scaleY;
		this.z *= scaleZ;
		return new Vector (this);
	}
	public synchronized Vector add (Vector other) {
		Vector temp = new Vector (this);
		temp.x += other.x;
		temp.y += other.y;
		temp.z += other.z;
		return temp;
	}
	public synchronized Vector _add (Vector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return new Vector (this);
	}
	public synchronized double dot (Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;	
	}
	public synchronized double lengthSquared () {
		return this.dot(this);
	}
	public synchronized double length () {
		return Math.sqrt(this.lengthSquared());
	}
	public synchronized Vector cross (Vector other) {
		Vector temp = new Vector ();
		temp.x = this.y * other.z - this.z * other.y;
		temp.y = this.z * other.x - this.x * other.z;
		temp.z = this.x * other.y - this.y * other.x;
		return temp;
	}
	public synchronized Vector _cross (Vector other) {
		Vector temp = new Vector ();
		temp.x = this.y * other.z - this.z * other.y;
		temp.y = this.z * other.x - this.x * other.z;
		temp.z = this.z * other.y - this.y * other.x;
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		return temp;
	}
	public synchronized Vector rotate (Rotation other) {
		Rotation temp = new Rotation (this.x, this.y, this.z, 0.0);
		temp = other.multiply(temp);
		temp._multiply(other.inverse());		
		return new Vector(temp.x, temp.y, temp.z);
	}	
	public synchronized Vector _rotate (Rotation other) {
		Rotation temp = new Rotation (this.x, this.y, this.z, 0.0);
		temp = other.multiply(temp);
		temp._multiply(other.inverse());		
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		return new Vector(temp.x, temp.y, temp.z);
	}
	public synchronized Vector normalize() {
		double length = this.length();
		if (length == 0.0)
			return new Vector();
		return this.scale(1 / length);		
	}
	public synchronized Vector _normalize() {
		double length = this.length();
		if (length == 0.0)
			return new Vector();
		return this._scale(1 / length);	
	}
	public synchronized Vector negate() {
		return this.scale(-1.0);
	}
	public synchronized Vector _negate() {
		return this._scale(-1.0);
	}
	public synchronized double angle (Vector other) {
		return Math.acos(this.normalize().dot(other.normalize()));
	}
	@Override public synchronized String toString () {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
