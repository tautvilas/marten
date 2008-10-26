package marten.age;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public final class ModelLoader {

	public static final ComplexModel load(String filename) throws Exception {
		File inputFile = new File(filename);
		DataInputStream stream = new DataInputStream(new FileInputStream(inputFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String[] words = filename.split("\\.");
		String filetype = words[words.length - 1];
		
		GenericModelLoader loader;
		if (filetype.compareToIgnoreCase("obj") == 0) {
			loader = new ObjModelLoader();
		} else {
			throw new RuntimeException("No loader available for " + filetype + " files");
		}
		
		StringBuilder sb = new StringBuilder();
		words = filename.split("/");
		for (int i = 0; i < words.length - 1; i++) {
			sb.append(words[i]);
			sb.append("/");
		}
		String path = sb.toString();
		
		return loader.load(reader, path);
	}
}
