package tile;

import core.Level;


public class TestTile1 extends Tile {
	private static int shaderProgram;
	private static int tex;
	private static int uniModel;
	private static int uniView;
	private static int uniProjection;
	
	public String getTextureName() {
		return "a";
	}
	
	public int getMeshResolution() {
		return 10;
	}
	
	public TileType getType() {
		return TileType.A;
	}
	
	public int getTexture() {
		return tex;
	}
	
	public TestTile1() {
	}
	
	public void update(float delta, Level level) {		
	}

	protected void setTexture(int _tex) {
		tex=_tex;
	}

	protected int getUniModel() {
		return uniModel;
	}

	protected void setUniModel(int _uniModel) {
		uniModel=_uniModel;
	}

	protected int getShaderProgram() {
		return shaderProgram;
	}

	protected void setShaderProgram(int _shaderProgram) {
		shaderProgram=_shaderProgram;
	}

	protected int getUniView() {
		return uniView;
	}

	protected void setUniView(int _uniView) {
		uniView=_uniView;
	}

	protected int getUniProjection() {
		return uniProjection;
	}

	protected void setUniProjection(int _uniProjection) {
		uniProjection=_uniProjection;
	}
}

