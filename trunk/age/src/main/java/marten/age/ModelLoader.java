package marten.age;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;




public class ModelLoader {
	private BufferedReader reader;
	private GenericModelLoader loader;
	private String path;
	
	public ModelLoader(String filename) throws FileNotFoundException {
		File inputFile = new File(filename);
		DataInputStream stream = new DataInputStream(new FileInputStream(inputFile));
		reader = new BufferedReader(new InputStreamReader(stream));
		
		String[] words = filename.split("\\.");
		String filetype = words[words.length - 1];
			
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
		path = sb.toString();
	}
	
	public ComplexModel load() throws Exception {
		return loader.load(reader, path);
	}
}
