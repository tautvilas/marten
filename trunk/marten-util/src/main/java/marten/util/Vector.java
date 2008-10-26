package marten.util;

/**A class to simulate the three-dimensional vectors.
 * @author Petras Razanskas*/
public final class Vector {
	/**A zero vector*/
	public static final Vector ZERO = new Vector();
	/**A unitary vector, pointing along the positive X axis*/
	public static final Vector X_AXIS = new Vector(1.0, 0.0, 0.0);
	/**A unitary vector, pointing along the positive Y axis*/
	public static final Vector Y_AXIS = new Vector(0.0, 1.0, 0.0);
	/**A unitary vector, pointing along the positive Z axis*/
	public static final Vector Z_AXIS = new Vector(0.0, 0.0, 1.0);
	/**The <code>x</code> coordinate of the vector.*/
	public double x = 0.0;
	/**The <code>y</code> coordinate of the vector.*/
	public double y = 0.0;
	/**The <code>z</code> coordinate of the vector.*/
	public double z = 0.0;
	/**An empty constructor which creates a zero vector.*/
	public Vector () {}
	/**A copy contructor, which creates an identical vector to the <b>source</b>
	 * @param source the source vector, copy of which is being made.*/
	public Vector (Vector source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	/**A constructor, which creates a vector, which points from the origin point <code>(0,0,0)</code> to the <b>source</b> point.
	 * @param source the source point, to which the vector being created points.*/
	public Vector (Point source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	/**A constructor, which creates a vector, codirectional to the <b>source</b> rotation axis.
	 * @param source the source rotation, axis of which is used to create this vector.*/
	public Vector (Rotation source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	/**A constructor, which creates a vector from the given three <code>double</code> values.
	 * @param newX the <code>x</code> coordinate of the new vector.
	 * @param newY the <code>y</code> coordinate of the new vector.
	 * @param newZ the <code>z</code> coordinate of the new vector.*/
	public Vector (double newX, double newY, double newZ) {
		this.x = newX;
		this.y = newY;
		this.z = newZ;
	}
	/**A constructor, which creates a vector pointing from the <b>start</b> point to the <b>finish</b> point.
	 * @param start the starting point, from which the vector points.
	 * @param finish the final point, to which the vector points.*/
	public Vector (Point start, Point finish) {
		this.x = finish.x - start.x;
		this.y = finish.y - start.y;
		this.z = finish.z - start.z;
	}
	/**Compares two vectors for the equality.
	 * @param other the vector, which is compared to this vector.
	 * @return <code>true</code> if the vectors are identical, <code>false</code> otherwise.*/
	public synchronized boolean equals (Vector other) {
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
	}
	/**Compares two vectors for the approximate equality.
	 * @param other the vector, which is compared to this vector.
	 * @param epsilon the <code>double</code> value, by which the respective coordinates may be different for the vectors to be still considered equal.
	 * @return <code>true</code> if the maximum difference between the respective coordinates is less than <b>epsilon</b>, <code>false</code> otherwise.
	 * @throws <code>RuntimeException</code> when <b>epsilon</b> is negative.*/
	public synchronized boolean approximatelyEquals (Vector other, double epsilon) {
		if (epsilon < 0.0)
			throw new RuntimeException ("Attempted approximate comparison with epsilon less than zero.");
		return (Math.abs(this.x - other.x) <= epsilon) && (Math.abs(this.y - other.y) <= epsilon) && (Math.abs(this.z - other.z) <= epsilon); 			
	}
	/**@param scaleFactor the <code>double</code> value, signifying how much the resulting vector will be longer than this vector.
	 * @return a vector, which is collinear to this vector, only <b>scaleFactor</b> times longer. The vectors are codirectional if the <b>scaleFactor</b> is positive, opposite otherwise.*/
	public synchronized Vector scale (double scaleFactor) {
		return new Vector (this.x * scaleFactor, this.y * scaleFactor, this.z * scaleFactor);
	}
	/**@param scaleX the warp factor along <code>x</code> axis.
	 * @param scaleY the warp factor along <code>y</code> axis.
	 * @param scaleZ the warp factor along <code>z</code> axis.
	 * @return a vector, which is warped along the coordinate axis.*/
	public synchronized Vector scaleAssymetrically (double scaleX, double scaleY, double scaleZ) {
		return new Vector (this.x * scaleX, this.y * scaleY, this.z * scaleZ);
	}
	/**@param other the vector, which is added to this vector.
	 * @return the sum of this and <b>other</b> vectors.*/
	public synchronized Vector add (Vector other) {
		return new Vector (this.x + other.x, this.y + other.y, this.z + other.z);
	}
	/**@param other the vector, which is dot-multiplied with this vector.
	 * @return the dot-product of this and <b>other</b> vectors.*/
	public synchronized double dot (Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;	
	}
	/**@return the square of the length of this vector.*/
	public synchronized double lengthSquared () {
		return this.dot(this);
	}
	/**@return the actual length of this vector. Use {@link #lengthSquared()} if you need this value for comparison since it is calculated more effectively.*/
	public synchronized double length () {
		return Math.sqrt(this.lengthSquared());
	}
	/**@param other the vector which is cross-multiplied with this vector.
	 * @return the cross product of this and <b>other</b> vectors.*/
	public synchronized Vector cross (Vector other) {
		return new Vector (this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
	}
	/**@param rotation the quaternion that defines, how this vector should be rotated.
	 * @return the rotated vector.*/
	public synchronized Vector rotate (Rotation other) {
		Rotation temp = new Rotation (this);
		temp = other.multiply(temp);
		temp = temp.multiply(other.inverse());		
		return new Vector(temp);
	}	
	/**@return the vector with length of 1 and codirectional with this vector.*/
	public synchronized Vector normalize() {
		double length = this.length();
		if (length == 0.0)
			return new Vector();
		return this.scale(1 / length);		
	}
	/**@return a vector that is of the same length but points in an exactly opposite direction than this vector.*/
	public synchronized Vector negate() {
		return this.scale(-1.0);
	}
	/**@param other a vector to which the angle is measured.
	 * @return the angle between this and <b>other</b> vectors as a <code>double</code> value in radians.*/
	public synchronized double angle (Vector other) {
		return Math.acos(this.normalize().dot(other.normalize()));
	}
	/**@return a string representation of this vector to facilitate the output.*/
	@Override public synchronized String toString () {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
