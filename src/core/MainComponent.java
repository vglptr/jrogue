package core;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import tile.Tile;


public class MainComponent {
	GLFWFramebufferSizeCallback fbCallback;
	Window window = new Window();
	Tile t;
    Timer timer = new Timer();
    
    public MainComponent() {
		t = new Tile();
		mainLoop();
	}
    
    private void update(float delta) {
    	t.update(delta);
    }
    
    private void render() {
    	t.render(timer.getDelta());
    }
    
    
    private void mainLoop() {
        while (glfwWindowShouldClose(window.getId()) != GL_TRUE) {
        	timer.updateFPS();
        	timer.update();
        	glfwSetWindowTitle(window.getId(), String.valueOf(timer.getFPS()));
            update(timer.getDelta());
            render();
            glfwSwapBuffers(window.getId());
            glfwPollEvents();
        }

        glfwDestroyWindow(window.getId());
        Window.getKeyCallback().release();

        glfwTerminate();
        Window.getErrorCallback().release();
    }
}
