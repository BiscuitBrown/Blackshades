package blackshades;
import static org.lwjgl.opengl.GL11.*;

public class Fog {
	
	public float fogColor[] = new float[4];
	public int fogMode;
	public float fogDensity;
	public float fogStart;
	public float fogEnd;

	public void setFog(float colorR, float colorG, float colorB, float fStart, float fEnd, float Density)
	{
		fogColor[0]=colorR;
		fogColor[1]=colorG;
		fogColor[2]=colorB;
		fogColor[3]=1;
		fogStart=fStart;
		fogEnd=fEnd;
		fogDensity=Density;
		fogMode=GL_LINEAR;

		glFogi(GL_FOG_MODE,fogMode);
		glFogfv(GL_FOG_COLOR,fogColor);
		glFogf(GL_FOG_DENSITY,fogDensity);
		glFogi(GL_FOG_HINT,GL_DONT_CARE);
		glFogf(GL_FOG_START,fogStart);
		glFogf(GL_FOG_END,fogEnd);
		
		glEnable(GL_FOG);
	}

	public void tempFog(float colorR, float colorG, float colorB)
	{
		float tempfogColor[] = new float[4];
		tempfogColor[0]=colorR;
		tempfogColor[1]=colorG;
		tempfogColor[2]=colorB;
		tempfogColor[3]=1;

		glFogi(GL_FOG_MODE,fogMode);
		glFogfv(GL_FOG_COLOR,tempfogColor);
		glFogf(GL_FOG_DENSITY,fogDensity);
		glFogi(GL_FOG_HINT,GL_DONT_CARE);
		glFogf(GL_FOG_START,fogStart);
		glFogf(GL_FOG_END,fogEnd);
		
		glEnable(GL_FOG);
	}

	public void resetFog()
	{
		glFogi(GL_FOG_MODE,fogMode);
		glFogfv(GL_FOG_COLOR,fogColor);
		glFogf(GL_FOG_DENSITY,fogDensity);
		glFogi(GL_FOG_HINT,GL_DONT_CARE);
		glFogf(GL_FOG_START,fogStart);
		glFogf(GL_FOG_END,fogEnd);
		
		glEnable(GL_FOG);
	}


}
