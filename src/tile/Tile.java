package tile;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import core.Level;
import core.Window;

import math.Matrix4f;
import util.Position;
import util.ShaderLoader;
import util.Textures;
import util.VertexGenerator;

public abstract class Tile {
	
	protected int uniProjection;
	protected Position pos;
	protected TileType type;
	protected int shaderProgram;
	private int tex;
	
	public Position getPosition() {
		return pos;
	}
	
	public void setPosition(Position pos) {
		this.pos=pos;
	}
	
	public abstract TileType getType();	
	
	int vao;
	int vbo;
	int vertexShader;
	int fragmentShader;
	
	private static final int tilesPerScreen = 5;
	int resolutionx = 10;
	int resolutiony = 10;
	float squareSize = 2/(float)resolutionx/(float)tilesPerScreen;
	float ratio = 640f / 480f;
	
	public Tile() {
		
	}
	
	public void init(int x, int y) {
		pos=new Position(x, y);
		type=getType();
		tex=Textures.getTexture(type);
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
		
		int uniModel = glGetUniformLocation(shaderProgram, "model");
		Matrix4f model = Matrix4f.translate(pos.x*squareSize*resolutionx, pos.y*squareSize*resolutionx, 0);
		glUniformMatrix4(uniModel, false, model.getBuffer());
		
		int posAttrib = glGetAttribLocation(shaderProgram, "position");
		glEnableVertexAttribArray(posAttrib);
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 20, 0);

		int texAttrib = glGetAttribLocation(shaderProgram, "texcoord");
		glEnableVertexAttribArray(texAttrib);
		glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 20, 12);
		
		uniProjection = glGetUniformLocation(shaderProgram, "projection");
		Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
		glUniformMatrix4(uniProjection, false, projection.getBuffer());
		
		GL30.glUniform1ui(glGetUniformLocation(shaderProgram, "tex"), 0);
	}
	
	public abstract void update(float delta, Level level);
	
	public void render(float delta, Level level) {
	
		glUseProgram(shaderProgram);
		
		int uniView = glGetUniformLocation(shaderProgram, "view");
	    Matrix4f view = Matrix4f.translate(level.cameraX, level.cameraY, 0);
	    glUniformMatrix4(uniView, false, view.getBuffer());
	    
	    float newRatio = Window.getWidth() / (float)Window.getHeight();
	    if(ratio != newRatio) {
	    	ratio = newRatio;
	    	Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
	    	glUniformMatrix4(uniProjection, false, projection.getBuffer());
	    }
	    
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		
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
