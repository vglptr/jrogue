package tile;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
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
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import math.Matrix4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import util.Position;
import util.ShaderLoader;
import util.Textures;
import util.VertexGenerator;
import core.Level;
import core.Window;

public abstract class Tile {			
	protected Position pos = new Position(0,0);

	public Position getPosition() {
		return pos;
	}
	
	public void setPosition(Position pos) {
		this.pos=pos;
		model=Matrix4f.translate(pos.x, pos.y, 0);
	}
	
	protected abstract TileType getType();
	protected abstract int getMeshResolution();
	protected abstract String getTextureName();
	protected abstract int getTexture();
	protected abstract void setTexture(int _tex);
	protected abstract int getUniModel();
	protected abstract void setUniModel(int _uniModel);
	protected abstract int getShaderProgram();
	protected abstract void setShaderProgram(int _shaderProgram);
	protected abstract int getUniView();
	protected abstract void setUniView(int _uniView);
	protected abstract int getUniProjection();
	protected abstract void setUniProjection(int _uniProjection);
	
	protected static float squareSize;
	public Matrix4f model;
	
	
	public Tile() {	
		squareSize = 1/getMeshResolution();
	}
	
	public void initTileType() {
		int vao;
		int vbo;
		int vertexShader;
		int fragmentShader;
		
		setTexture(Textures.loadTexture(getTextureName()));
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		FloatBuffer vertices = VertexGenerator.generateMesh(getMeshResolution(), getMeshResolution(), squareSize);
	
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
		
		int shaderProgram = glCreateProgram();
		setShaderProgram(shaderProgram);
		
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
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 20, 0);

		int texAttrib = glGetAttribLocation(shaderProgram, "texcoord");
		glEnableVertexAttribArray(texAttrib);
		glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 20, 12);
		
		GL30.glUniform1ui(glGetUniformLocation(shaderProgram, "tex"), 0);
		
		setUniModel(glGetUniformLocation(shaderProgram, "model"));
		setUniView(glGetUniformLocation(shaderProgram, "view"));
		setUniProjection(glGetUniformLocation(shaderProgram, "projection"));
	}

	public void init(int x, int y) {
		setPosition(new Position(x,y));
	}
	
	public abstract void update(float delta, Level level);
	
	public void render(float delta, Level level) {	
		glUseProgram(getShaderProgram());
		
		glUniformMatrix4(getUniModel(), false, model.getBuffer());
		
	    Matrix4f view = Matrix4f.translate(level.cameraX, level.cameraY, 0);
	    glUniformMatrix4(getUniView(), false, view.getBuffer());
	        
    	float zoom=level.tilesPerScreen;
    	Matrix4f projection = Matrix4f.orthographic(Window.getRatio()*-zoom/2.0f, Window.getRatio()*zoom/2.0f, -zoom/2.0f, zoom/2.0f, -1f, 1f);
    	glUniformMatrix4(getUniProjection(), false, projection.getBuffer());
	    
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture());
		
	    glDrawArrays(GL_TRIANGLES, 0, getMeshResolution() * getMeshResolution() * 6);
	}
	
	public void destroy() {
		/*glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(shaderProgram);*/
	}
	
	public boolean isWall() {
		return getType()==TileType.B;
	}
}
