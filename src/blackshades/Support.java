package blackshades;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;
import static blackshades.Game.*;
import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;
import java.nio.*;
import static blackshades.Globals.*;

import static org.lwjgl.BufferUtils.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Support {
		
	public static class Point extends GLFWCursorPosCallback { 
		public float h, v;
		
		public Point() {h = 0; v = 0;}
		
		public Point(Point p) {
			set(p);
		}
		public void set(Point p) {
			this.h = p.h;
			this.v = p.v;
		}
		
		public void invoke(long window, double x, double y) {
			this.h =(float) x;
			this.v = (float) y;
		}
		
	}
		
	
	public static int random() {
		return (random.nextInt() % 65535) - 32767;
	}
	
	
	public static void getMouse(Point p) {
				
		DoubleBuffer mouseX = createDoubleBuffer(1);
		DoubleBuffer mouseY = createDoubleBuffer(1);
		
		glfwGetCursorPos(window, mouseX, mouseY);

		mouseX.rewind();
		mouseY.rewind();

		p.h = (float) mouseX.get();
		p.v = (float) mouseY.get();
						
	}
	
	public static boolean button() {
		return glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1)==GLFW_PRESS;
	}
	
	public static void moveMouse(Point p) {
		glfwSetCursorPos(window, p.h, p.v);
	}
	
	public static void moveMouse(float h, float v, Point p) {
		glfwSetCursorPos(window,h, v);
		getMouse(p);
	}
	
	public static void getMouseRel(Point p) {
		
		DoubleBuffer mouseX = createDoubleBuffer(1);
		DoubleBuffer mouseY = createDoubleBuffer(1);
			
		glfwGetCursorPos(window, mouseX, mouseY);
		
		p.h = (float) mouseX.get();
		p.v = (float) mouseY.get();
		
		// There is no equivalent to SDL_GetRelativeMouseState in GLFW, so we have to set the mouse position to 0.
		glfwSetCursorPos(window, 0, 0);
						
	}
	
	
	private int bufferId = 0;

    private ShortBuffer pcm;

    public void soundBuffer(String filePath) throws IOException {
        this.bufferId = alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            pcm = readVorbis(filePath, info);

            // Copy to buffer
            alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
    }

    public static void cleanupVorbis(int bufferId, ShortBuffer pcm) {
        alDeleteBuffers(bufferId);
        if (pcm != null) {
            MemoryUtil.memFree(pcm);
        }
        /*
        Free the OGG buffer
        */
    }
    
    public static ShortBuffer readVorbis(String filePath, STBVorbisInfo info) throws IOException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer error = stack.mallocInt(1);
            byte[] file = Support.class.getClassLoader().getResourceAsStream(filePath).readAllBytes();
            
            ByteBuffer vorbisSound = createByteBuffer(file.length);
            vorbisSound.put(file);
            vorbisSound.rewind();
            
            long decoder = stb_vorbis_open_memory(vorbisSound, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer result = MemoryUtil.memAllocShort(lengthSamples * channels);

            result.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, result) * channels);
            stb_vorbis_close(decoder);

            return result;
        }
    }
}
