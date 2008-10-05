package marten.testing.tests;

import marten.testing.ConsoleTest;

public class CpuBenchmark implements ConsoleTest {

	@Override
	public void run() {
		long forIterationTime = 1000000000;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++) {}
			long finishTime = System.currentTimeMillis();
			long tempForIterationTime = finishTime - startTime;
			if (tempForIterationTime < forIterationTime)
				forIterationTime = tempForIterationTime;
		}
		System.out.println ("Empty \'for\' cycle iteration: ~" + forIterationTime + " nanoseconds (carnifex - 1 ns)");
		long intAdditionTime = 1000000000;
		@SuppressWarnings("unused")
		long a = 365454236745l, b = 254186165544l, c;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++)
				c = a + b;
			long finishTime = System.currentTimeMillis();
			long tempIntAdditionTime = finishTime - startTime - forIterationTime;
			if (tempIntAdditionTime < intAdditionTime)
				intAdditionTime = tempIntAdditionTime;
		}
		System.out.println ("Integer addition: ~" + intAdditionTime + " nanoseconds (carnifex - 1 ns)");
		a = 54884546;
		b = 44114;
		long intMultiplicationTime = 1000000000;
		for (int i = 0; i < 1000; i++) {
		long startTime = System.currentTimeMillis();
		for (int ii = 0; ii < 1000000; ii++)
			c = a * b;
		long finishTime = System.currentTimeMillis();
		long tempIntMultiplicationTime = finishTime - startTime - forIterationTime;
		if (tempIntMultiplicationTime < intMultiplicationTime)
			intMultiplicationTime = tempIntMultiplicationTime;
		}
		System.out.println ("Integer multiplication: ~" + intMultiplicationTime + " nanoseconds (carnifex - 7 ns)");
		@SuppressWarnings({ "unused", "unused" })
		double d, e, f;
		long randomGenerationTime = 1000000000;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++)
				d = Math.random();
			long finishTime = System.currentTimeMillis();
			long tempRandomGenerationTime = finishTime - startTime - forIterationTime;
			if (tempRandomGenerationTime < randomGenerationTime)
				randomGenerationTime = tempRandomGenerationTime;
		}
		System.out.println ("Random double generation: ~" + randomGenerationTime + " nanoseconds (carnifex - 224 ns)");
		long doubleAdditionTime = 1000000000;
		d = 0.64464846774;
		e = 3.14145952485;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++)
				f = d + e;
			long finishTime = System.currentTimeMillis();
			long tempDoubleAdditionTime = finishTime - startTime - forIterationTime;
			if (tempDoubleAdditionTime < doubleAdditionTime)
				doubleAdditionTime = tempDoubleAdditionTime;
		}
		System.out.println ("Double addition: ~" + doubleAdditionTime + " nanoseconds (carnifex - 1 ns)");
		long doubleMultiplicationTime = 1000000000;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++)
				f = d * e;			
			long finishTime = System.currentTimeMillis();
			long tempDoubleMultiplicationTime = finishTime - startTime - forIterationTime;
			if (tempDoubleMultiplicationTime < doubleMultiplicationTime)
				doubleMultiplicationTime = tempDoubleMultiplicationTime;
		}
		System.out.println ("Double multiplication: ~" + doubleMultiplicationTime + " nanoseconds (carnifex - 1 ns)");
		long doubleTrigonometricTime = 1000000000;
		for (int i = 0; i < 1000; i++) {
			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < 1000000; ii++)
				f = Math.sin(d);
			long finishTime = System.currentTimeMillis();
			long tempDoubleTrigonometricTime = finishTime - startTime - forIterationTime;
			if (tempDoubleTrigonometricTime < doubleTrigonometricTime)
				doubleTrigonometricTime = tempDoubleTrigonometricTime;
		}
		System.out.println ("Double trigonometric function: ~" + doubleTrigonometricTime + " nanoseconds (carnifex - 61 ns)");		
	}

}
