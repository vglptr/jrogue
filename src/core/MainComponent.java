package core;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import util.Textures;


public class MainComponent {
	GLFWFramebufferSizeCallback fbCallback;
	Window window = new Window();
    Timer timer = new Timer();
    Level level = new Level();
    
    public MainComponent() {
    	Textures.LoadTextures();
    	level.init();
		mainLoop();
	}
    
    
    private void mainLoop() {
        while (glfwWindowShouldClose(window.getId()) != GL_TRUE) {
        	timer.updateFPS();
        	timer.update();
        	glfwSetWindowTitle(window.getId(), String.valueOf(timer.getFPS()));
        	glClear(GL_COLOR_BUFFER_BIT);
        	float delta=timer.getDelta();
            level.update(delta);
            level.render(delta);
            glfwSwapBuffers(window.getId());
            glfwPollEvents();
        }

        glfwDestroyWindow(window.getId());
        Window.getKeyCallback().release();

        glfwTerminate();
        Window.getErrorCallback().release();
    }
}
