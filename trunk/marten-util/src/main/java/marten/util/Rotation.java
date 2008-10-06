package marten.util;

/**A class to simulate rotations in three-dimensional environment by the means of quaternions.
 * @author Petras Razanskas*/
public final class Rotation {
	/**The <code>x</code> coordinate of the vector part of the quaternion.*/
	public double x = 0.0;
	/**The <code>y</code> coordinate of the vector part of the quaternion.*/
	public double y = 0.0;
	/**The <code>z</code> coordinate of the vector part of the quaternion.*/
	public double z = 0.0;
	/**The scalar part of the quaternion.*/
	public double w = 1.0;
	/**The "zero" quaternion which is equivalent to no rotation.*/
	public static final Rotation ZERO = new Rotation();
	/**An empty constructor which creates a "no rotation" quaternion.*/
	public Rotation () {}
	/**A copy constructor which creates an identical quaternion to the <b>source</b>
	 * @param source The quaternion being copied.*/
	public Rotation (Rotation source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
		this.w = source.w;
	}
	/**A constructor which sets the vector part of the quaternion to the <b>source</b> vector and leaves the scalar part zero.
	 * @param source The vector, for which the quaternion form is being created.*/
	public Rotation (Vector source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
		this.w = 0.0;
	}
	/**A constructor which sets the vector part of the quaternion to the coordinates of the <b>source</b> point and leaves the scalar part zero.
	 * @param source The point, for which the quaternion form is being created.*/
	public Rotation (Point source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
		this.w = 0.0;
	}
	/**A constructor which creates a quaternion according to what rotation it should represent.
	 * @param axis a vector around which the rotation is made.
	 * @param angle an angle of the rotation in radians, measured counterclockwise from the perspective of the end of the <b>axis</b> vector.*/
	public Rotation (Vector axis, double angle) {
		this.set(axis, angle);
	}
	/**A constructor which creates a quaternion from the four given <code>double</code> values.
	 * @param newX the <code>x</code> coordinate of the vector part of the new quaternion.
	 * @param newY the <code>y</code> coordinate of the vector part of the new quaternion.
	 * @param newZ the <code>z</code> coordinate of the vector part of the new quaternion.
	 * @param newW the scalar part of the new quaternion.*/
	public Rotation (double newX, double newY, double newZ, double newW) {
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
	}
	/**Sets the quaternion to represent a certain rotation around a given <b>axis</b> by a given <b>angle</b>.
	 * @param axis the vector, around which the rotation is carried out.
	 * @param angle the angle of the rotation in radians, measured clockwise from the perspective of the end of the <b>axis</b> vector.*/
	public synchronized void set(Vector axis, double angle) {
		this.w = Math.cos(angle / 2.0);
		Vector normal = axis.normalize();
		double sin = Math.sin(angle / 2.0);
		this.x = normal.x * sin;
		this.y = normal.y * sin;
		this.z = normal.z * sin;
	}
	/**@return the angle of the rotation represented by this quaternion.*/
	public synchronized double getAngle () {
		return 2.0 * Math.acos(this.w);
	}
	/**@return the normalized axis of the rotation represented by this quaternion.*/
	public synchronized Vector getAxis () {
		Vector temp = new Vector();
		temp.x = this.x;
		temp.y = this.y;
		temp.z = this.z;
		return temp.normalize();
	}
	/**Compares the two quaternions for equality.
	 * @param other the quaternion to which this quaternion is compared.
	 * @return <code>true</code> if the quaternions are identical, <code>false</code> otherwise.*/
	public synchronized boolean equals (Rotation other) {
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (this.w == other.w);
	}
	/**@param scaleFactor a <code>double</code> value by which the angle of the rotation is scaled.
	 * @return a quaternion, which has the same rotation axis, but a scaled rotation angle.*/
	public synchronized Rotation scale (double scaleFactor) {
		Rotation temp = new Rotation ();
		temp.set(this.getAxis(), this.getAngle() * scaleFactor);
		return temp;
	}
	@Deprecated public synchronized Rotation _scale (double scaleFactor) {
		this.set(this.getAxis(), this.getAngle() * scaleFactor);
		return new Rotation(this);
	}
	/**@param other a rotation, which is combined with this rotation.
	 * @return a quaternion which represents a rotation acquired after combining this rotation and <b>other</b> rotation in sequence.*/
	public synchronized Rotation multiply (Rotation other) {
		Rotation temp = new Rotation();
		temp.w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
		temp.x = this.x * other.w + this.w * other.x + this.y * other.z - this.z * other.y;
		temp.y = this.y * other.w + this.w * other.y + this.z * other.x - this.x * other.z;
		temp.z = this.z * other.w + this.w * other.z + this.x * other.y - this.y * other.x;
		return temp;
	}
	@Deprecated public synchronized Rotation _multiply (Rotation other) {
		Rotation temp = new Rotation();
		temp.w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
		temp.x = this.x * other.w + this.w * other.x + this.y * other.z - this.z * other.y;
		temp.y = this.y * other.w + this.w * other.y + this.z * other.x - this.x * other.z;
		temp.z = this.z * other.w + this.w * other.z + this.x * other.y - this.y * other.x;
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		this.w = temp.w;
		return temp;
	}
	@Deprecated public synchronized Rotation multiplyLimited (Rotation other) {
		Rotation temp = this.multiply(other);
		if (temp.getAngle() > Math.PI / 2)
			temp = new Rotation (temp.getAxis(), Math.PI / 2);
		return temp;
	}
	@Deprecated public synchronized Rotation _multiplyLimited (Rotation other) {
		Rotation temp = this.multiply(other);
		if (temp.getAngle() > Math.PI / 2)
			temp = new Rotation (temp.getAxis(), Math.PI / 2);
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		this.w = temp.w;		
		return temp;
	}
	/**@return a quaternion, which represents a rotation to the opposite direction from this quaternion.*/
	public synchronized Rotation inverse () {
		Rotation temp = new Rotation();
		temp.x = -this.x;
		temp.y = -this.y;
		temp.z = -this.z;
		temp.w = this.w;
		return temp;
	}
	@Deprecated public synchronized Rotation _inverse () {
		this.x *= -1.0;
		this.y *= -1.0;
		this.z *= -1.0;
		return new Rotation(this);
	}
	/**Normalizes this quaternion.
	 * This operation is very important since only quaternions with the length of 1 may represent rotations in three-dimensional space.
	 * @throws <code>RuntimeException</code> if all four fields of quaternion are set to zero.*/
	public void normalize () {
		double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
		if (length == 0.0)
			throw new RuntimeException ("Attempted to normalize rotation with all four fields set to zero.");
		this.x /= length;
		this.y /= length;
		this.z /= length;
		this.w /= length;
	}
	/**@return the <code>String</code> representation of this quaternion to facilitate the output of data.*/
	public synchronized String toString () {
		return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
	}
}
