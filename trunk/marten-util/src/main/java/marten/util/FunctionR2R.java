package marten.util;

/**An interface to help emulating higher order functions, when a <code>double</code> to <code>double</code> function needs to be passed into the body of another function.
 * @author Petras Razanskas*/
public interface FunctionR2R {
	/**The function that is "passed into" the body of another function.*/
	double calculate (double t);
}
