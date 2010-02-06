package marten.age.graphics.util;

/**A class used to pass sine function into the body of another function.
 * @author Petras Razanskas*/
public final class Sine implements FunctionR2R {
	/**Calculates the sine of <b>t</b> (in radians)
	 * @param t The angle in radians
	 * @return The sine of the angle <b>t</b>*/
	public double calculate(double t) {
		return Math.sin(t);
	}
}
