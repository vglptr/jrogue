package tile;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import math.Matrix4f;
import util.ShaderLoader;
import util.VertexGenerator;
import core.Window;

public class Tile {
	private int uniModel;
	private int uniProjection;
	//private float previousAngle = 0f;
//	private float angle = 0f;
//	private float anglePerSecond = 50f;
	
	int vao;
	int vbo;
	int vertexShader;
	int fragmentShader;
	int shaderProgram;
	
	int resolutionx = 10;
	int resolutiony = 10;
	float squareSize = 0.1f;
	float ratio = 640f / 480f;
	
	public Tile() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		FloatBuffer vertices = VertexGenerator.generateMesh(resolutionx, resolutiony, squareSize);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		String vertexSource = ShaderLoader.readShaderFile("src/tile/tile.vert");
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);

		String fragmentSource = ShaderLoader.readShaderFile("src/tile/tile.frag");
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);
		
		int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
		    throw new RuntimeException(glGetShaderInfoLog(vertexShader));
		}
		
		status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
		}
		
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);
		
		status = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (status != GL_TRUE) {
		    throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
		}
		
		glUseProgram(shaderProgram);
		
		int posAttrib = glGetAttribLocation(shaderProgram, "position");
		glEnableVertexAttribArray(posAttrib);
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 24, 0);

		int colAttrib = glGetAttribLocation(shaderProgram, "color");
		glEnableVertexAttribArray(colAttrib);
		glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 24, 12);
		
		int uniModel = glGetUniformLocation(shaderProgram, "model");
		Matrix4f model = new Matrix4f();
		glUniformMatrix4(uniModel, false, model.getBuffer());

		int uniView = glGetUniformLocation(shaderProgram, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4(uniView, false, view.getBuffer());

		uniProjection = glGetUniformLocation(shaderProgram, "projection");
		Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
		glUniformMatrix4(uniProjection, false, projection.getBuffer());
		
		glClear(GL_COLOR_BUFFER_BIT);
		glDrawArrays(GL_TRIANGLES, 0, resolutionx * resolutiony * 3);
	}
	
	public void update(float delta) {
//	    previousAngle = angle;
//	    angle += delta * anglePerSecond;

	    int uniView = glGetUniformLocation(shaderProgram, "view");
	    Matrix4f view = Matrix4f.translate(Window.x * 0.1f, Window.y * 0.1f, 0);
	    glUniformMatrix4(uniView, false, view.getBuffer());
	}
	
	public void render(float alpha) {
	    glClear(GL_COLOR_BUFFER_BIT);
	    
	    float newRatio = Window.getWidth() / (float)Window.getHeight();
	    if(ratio != newRatio) {
	    	ratio = newRatio;
	    	Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
	    	glUniformMatrix4(uniProjection, false, projection.getBuffer());
	    }
	    
//	    float lerpAngle = (1f - alpha) * previousAngle + alpha * angle;
//	    Matrix4f model = Matrix4f.rotate(lerpAngle, 0f, 0f, 1f);
//	    glUniformMatrix4(uniModel, false, model.getBuffer());
	    
	    glDrawArrays(GL_TRIANGLES, 0, resolutionx * resolutiony * 6);
	}
	
	public void destroy() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(shaderProgram);
	}
}
