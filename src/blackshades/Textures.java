package blackshades;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.BufferUtils.*;

public class Textures {
	
	public static int loadTexture(String filename) throws IOException{
		
		byte[] file = Textures.class.getClassLoader().getResourceAsStream(filename).readAllBytes();
		
        // Generate texture on GPU
        int texID = glGenTextures();
                
        glBindTexture(GL_TEXTURE_2D, texID);
        
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        IntBuffer width = createIntBuffer(1);
        IntBuffer height = createIntBuffer(1);
        IntBuffer channels = createIntBuffer(1);
        
		//well, our textures are upside down. Fixing it here.
        stbi_set_flip_vertically_on_load(true);
        
        ByteBuffer texture = createByteBuffer(file.length);
        texture.put(file);
        texture.rewind();
        
        ByteBuffer image = stbi_load_from_memory(texture, width, height, channels, STBI_default);
                    	    	
        if (image != null) {
        	
        	image.flip();
        	        	
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 
                		0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                                
            } else {
                assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (Texture) Could not load image '" + filename + "'";
        }
        stbi_image_free(image);
        
        return texID;
    }
	
	public static void loadIcon(long window) throws IOException {
		
		MemoryStack stack = stackPush();
		
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		
        byte[] file = Textures.class.getClassLoader().getResourceAsStream("Data/BSicon_32x32x32.png").readAllBytes();
        
        ByteBuffer icon = createByteBuffer(file.length);
        icon.put(file);
        icon.rewind();
                		
		//Data.BSicon_32x32x32
        										
		ByteBuffer icon32 = stbi_load_from_memory(icon, w, h, comp, 4);
						
		glfwSetWindowIcon(window, GLFWImage.malloc(0, stack)
				.width(w.get(0))
				.height(h.get(0))
				.pixels(icon32)
		);

		w = stack.mallocInt(1);
		h = stack.mallocInt(1);
		comp = stack.mallocInt(1);
		
		file = Textures.class.getClassLoader().getResourceAsStream("Data/BSicon_128x128x32.png").readAllBytes();
		
		icon = createByteBuffer(file.length);
		icon.put(file);
        icon.rewind();
		
		ByteBuffer icon128 = stbi_load_from_memory(icon, w, h, comp, 4);
		
		glfwSetWindowIcon(window, GLFWImage.malloc(1, stack)
			.width(w.get(0))
			.height(h.get(0))
			.pixels(icon128)
		);

		stbi_image_free(icon32);
		stbi_image_free(icon128);
		
	}

}
