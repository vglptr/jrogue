package core;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import org.lwjgl.glfw.GLFW;

public class Controls {

	public static int dirX=0;
	public static int dirY=0;
	
	public static void handleKeys(long window, int key, int scancode, int action, int mods) {
		switch(key) {
		case GLFW.GLFW_KEY_ESCAPE:
            glfwSetWindowShouldClose(window, GL_TRUE);
            break;
		case GLFW.GLFW_KEY_UP:
        	if(action == GLFW.GLFW_PRESS)
        		dirY=-1;
        	else if(action == GLFW.GLFW_RELEASE)
        		dirY=0;
        	break;
		case GLFW.GLFW_KEY_DOWN:
        	if(action == GLFW.GLFW_PRESS)
        		dirY=1;
        	else if(action == GLFW.GLFW_RELEASE)
        		dirY=0;
        	break;
		case GLFW.GLFW_KEY_RIGHT:
        	if(action == GLFW.GLFW_PRESS)
        		dirX=-1;
        	else if(action == GLFW.GLFW_RELEASE)
        		dirX=0;
        	break;
		case GLFW.GLFW_KEY_LEFT:
        	if(action == GLFW.GLFW_PRESS)
        		dirX=1;
        	else if(action == GLFW.GLFW_RELEASE)
        		dirX=0;
        	break;
        }
	}
}
