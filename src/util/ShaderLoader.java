package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShaderLoader {
	public static String readShaderFile(String fileName) {
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return content;
	}
}
