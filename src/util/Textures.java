package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import tile.TileType;

public class Textures {
	private static HashMap<TileType,String> tileTex = new HashMap<TileType,String>();
	private static HashMap<TileType,Integer> textures = new HashMap<TileType,Integer>();
	
	public static int getTexture(TileType tiletype) {
		return textures.get(tiletype);
	}
	
	public static void LoadTextures() {
		//Pair tile types and texture names
		tileTex.put(TileType.A, "a");
		tileTex.put(TileType.B, "b");
		
		Set<String> s = new HashSet<String>();
		for(String entry : tileTex.values()) {
			s.add(entry);
		}
		
		for(TileType entry : tileTex.keySet()) {
			textures.put(entry, null);
		}
		
		for(String entry : s) {
			loadTexture(entry);
		}
	}
	
	private static void loadTexture(String texName) {
		BufferedImage image=null;
		try {
			image = ImageIO.read(new File(texName+".png"));
		} catch (IOException e) {
			System.out.println("Couldn't load image: " + texName);
			System.exit(1);
		}

		int pixels[] = new int[image.getWidth() * image.getHeight()];
	    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 3);

	    for(int y = 0; y < image.getHeight(); y++){
	        for(int x = 0; x < image.getWidth(); x++){
	            int pixel = pixels[y * image.getWidth() + x];
	            buffer.put((byte) ((pixel >> 16) & 0xFF));
	            buffer.put((byte) ((pixel >> 8) & 0xFF));
	            buffer.put((byte) (pixel & 0xFF));
	        }
	    }

	    buffer.flip();  
	    
		int texture = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, image.getWidth(), image.getHeight(), 
	    		0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);

	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    
	    for(TileType entry : tileTex.keySet()) {
	    	if(tileTex.get(entry).equals(texName)) {
	    		textures.put(entry, texture);
	    	}
	    }	   
	}
}
