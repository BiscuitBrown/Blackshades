package blackshades;

import java.io.*;
import static java.nio.ByteOrder.*;
import static java.nio.ByteBuffer.*;

import blackshades.Models.*;
import blackshades.Quaternions.*;

public class Serialize {
	/* these all read big-endian data */
		
    public static boolean readBool(InputStream inputStream, int count) throws IOException {
    	
    	boolean b = false;
    	
    	int i = 0;
    	
        while (i < count) {
            byte[] buf = new byte[1];
            if (inputStream.read(buf) != 1) {
            	throw new IOException("Failed to read 1 byte");
            }
            b = ((buf[0] & 0xFF ) != 0);
            i++;
        }
        
        return b;
    }

    public static short readShort(InputStream in, int count) throws IOException {
    	
    	short var = 0;
    	int i = 0;
    	
        while (i < count) {
            byte[] buf = new byte[2];
            if (in.read(buf) != 2) {
            	 throw new IOException("Failed to read 2 bytes");
           }
            var = wrap(buf).order(BIG_ENDIAN).getShort();
            //var = (short)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));
            i++;
        }
        return var;
    }
    
    public static void readShort(InputStream in, int count, short[] var) throws IOException {
    	
    	int i=0;
    	
        while (i < count) {
            byte[] buf = new byte[2];
            if (in.read(buf) != 2) {
            	 throw new IOException("Failed to read 2 bytes");
           }
            var[i] = wrap(buf).order(BIG_ENDIAN).getShort();
            //var[i] = (short)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));
            i++;
        }
    }

    public static int readInt(InputStream in, int count) throws IOException {
    	
    	int var=0;
    	int i = 0;
    	
        while (i < count) {
            byte[] buf = new byte[4];
            if (in.read(buf) != 4) {
            	throw new IOException("Failed to read 4 bytes");
            }
            var = wrap(buf).order(BIG_ENDIAN).getInt();
            //var = (int)(((buf[0] & 0xFF ) << 24) | ((buf[1] & 0xFF ) << 16) | ((buf[2] & 0xFF ) << 8) | (buf[3] & 0xFF));
            i++;
        }
        
        return var;
    }

    public static float readFloat(InputStream in, int count) throws IOException {
    	
    	float var = 0;
    	    	
    	int i = 0;
    	
        while (i < count) {
        	var = Float.intBitsToFloat(readInt(in, 1));
        	i++;

        }
        
        return var;
    }

    public static void readXYZ(InputStream in, int count, XYZ [] xyz) throws IOException {
    	
    	int i = 0;
    	
    	while (i < count){
        	xyz[i] = new XYZ();
        	xyz[i].x = readFloat(in, 1);
            xyz[i].y = readFloat(in, 1);
            xyz[i].z = readFloat(in, 1);
            i++;
        }
    }
    
    public static XYZ readXYZ(InputStream in, int count) throws IOException {
    	XYZ xyz = new XYZ();
    	
    	int i = 0;
        while (i < count) {
        	xyz.x = readFloat(in, 1);
            xyz.y = readFloat(in, 1);
            xyz.z = readFloat(in, 1);
            i++;
        }
        
        return xyz;
    }

    public static void readTexturedTriangle(InputStream in, int count, TexturedTriangle[] tt) throws IOException {
    	int i = 0;
    	
    	while (i < count) {
        	short pad;
            tt[i] = new TexturedTriangle();
            readShort(in, 3, tt[i].vertex);
            pad = readShort(in, 1);
            tt[i].r = readFloat(in, 1);
            tt[i].g = readFloat(in, 1);
            tt[i].b = readFloat(in, 1);
            i++;
        }
    }
   
}
