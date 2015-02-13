package util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VertexGenerator {
	public static FloatBuffer generateMesh(int x, int y, float squareSize) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(30 * x * y);
		float texStepX=1/(float)x;
		float texStepY=1/(float)y;
		
		for(int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				vertices.put(i*squareSize).put(j*squareSize).put(0);
				vertices.put(i*texStepX).put(1-j*texStepY);

				vertices.put(i*squareSize + squareSize).put(j*squareSize).put(0);
				vertices.put(i*texStepX+texStepX).put(1-j*texStepY);
				
				vertices.put(i*squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(i*texStepX).put(1-(j*texStepY+texStepY));
				
				vertices.put(i*squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(i*texStepX).put(1-(j*texStepY+texStepY));
				
				vertices.put(i*squareSize + squareSize).put(j*squareSize).put(0);
				vertices.put(i*texStepX+texStepX).put(1-j*texStepY);
				
				vertices.put(i*squareSize + squareSize).put(j*squareSize + squareSize).put(0);
				vertices.put(i*texStepX+texStepX).put(1-(j*texStepY+texStepY));
			}
		}
		vertices.flip();
		return vertices;
	}
}
