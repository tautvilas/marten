package marten.age.graphics.model;

import java.io.BufferedReader;





public interface GenericModelLoader {
	public ComplexModel load(BufferedReader reader, String path) throws Exception;
}
