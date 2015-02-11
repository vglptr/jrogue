package util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VertexGenerator {
	public static FloatBuffer generateMesh(int x, int y, float squareSize) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(36 * x * y);
		for(int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				vertices.put(i*squareSize).put(j*squareSize).put(0);
				vertices.put(1).put(0).put(0);

				vertices.put(i*squareSize + squareSize).put(j*squareSize).put(0);
				vertices.put(0).put(1).put(0);
				
				vertices.put(i*squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(0).put(0).put(1);
				
				vertices.put(i*squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(0).put(0).put(1);
				
				vertices.put(i*squareSize + squareSize).put(j*squareSize).put(0);
				vertices.put(0).put(1).put(0);
				
				vertices.put(i*squareSize + squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(1).put(0).put(0);
			}
		}
		vertices.flip();
		return vertices;
	}
}
