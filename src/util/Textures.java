package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Textures {
	private static HashMap<String,Integer> textures = new HashMap<String,Integer>();
	
	public static int loadTexture(String texName) {
		if(textures.containsKey(texName))
			return textures.get(texName);
		
		BufferedImage image=null;
		try {
			image = ImageIO.read(new File("textures/"+texName+".png"));
		} catch (IOException e) {
			System.out.println("Couldn't load image: " + texName);
			System.exit(1);
		}

		int pixels[] = new int[image.getWidth() * image.getHeight()];
	    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

	    for(int y = 0; y < image.getHeight(); y++){
	        for(int x = 0; x < image.getWidth(); x++){
	            int pixel = pixels[y * image.getWidth() + x];
	            buffer.put((byte) ((pixel >> 16) & 0xFF));
	            buffer.put((byte) ((pixel >> 8) & 0xFF));
	            buffer.put((byte) (pixel & 0xFF));
	            buffer.put((byte) ((pixel >> 24) & 0xFF));
	        }
	    }

	    buffer.flip();  
	    
		int texture = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 
	    		0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    
	    for(String entry : textures.keySet()) {	    	
	    	textures.put(entry, texture);
	    }	 
	    
	    return texture;
	}
}
