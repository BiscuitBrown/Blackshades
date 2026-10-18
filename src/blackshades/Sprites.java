package blackshades;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import static blackshades.Constants.*;
import static blackshades.Quaternions.*;
import static blackshades.Textures.*;
import static blackshades.Globals.*;
import static blackshades.Support.*;
import static blackshades.Support.random;
import static java.lang.Math.*;

public class Sprites {
	
	public static final int 
		MAXSPRITES = 2000,

		MUZZLEFLASH_SPRITE = 1,
		SMOKE_SPRITE = 2,
		SMOKE_SPRITE_NOUP = 3,
		FLASH_SPRITE = 6,
		GRENADE_SPRITE = 7,
		PIN_SPRITE = 8,
		SPOON_SPRITE = 9,
		BLOOD_SPRITE_DOWN = 10,
		BLOOD_SPRITE_NOUP = 11,
		PARTICLES_SPRITE_DOWN = 12,
		SNOW_SPRITE = 13,
		RAIN_SPRITE = 14,
		
		BULLET = 4,
		BULLET_INSTANT = 5;
	
	public int
		flaretextureptr,
		muzzleflaretextureptr,
		smoketextureptr,
		bullettextureptr,
		bloodtextureptr,
		raintextureptr,
		snowtextureptr,
		howmanysprites;
	
	public XYZ
		location[] = new XYZ[MAXSPRITES],
		oldlocation[] = new XYZ[MAXSPRITES],
		velocity[] = new XYZ[MAXSPRITES],
		initialvelocity[] = new XYZ[MAXSPRITES];
	
	public float 
		size[] = new float[MAXSPRITES],
		initialsize[] = new float[MAXSPRITES],
		brightness[] = new float[MAXSPRITES],
		initialbrightness[] = new float[MAXSPRITES],
		color1[] = new float[MAXSPRITES],
		color2[] = new float[MAXSPRITES],
		color3[] = new float[MAXSPRITES],
		alivetime[] = new float[MAXSPRITES],
		rotation[]= new float[MAXSPRITES];
	
	public int type[] = new int[MAXSPRITES];
	public int owner[] = new int [MAXSPRITES];

	public int makeSprite(int atype, float abrightness, float acolor1, float acolor2, float acolor3, XYZ alocation, XYZ avelocity, float asize){
		type[howmanysprites]=atype;
		rotation[howmanysprites]=random()%360;
		brightness[howmanysprites]=abrightness;
		color1[howmanysprites]=acolor1;
		color2[howmanysprites]=acolor2;
		color3[howmanysprites]=acolor3;
		location[howmanysprites]= new XYZ(alocation);
		oldlocation[howmanysprites] = new XYZ(alocation);
		velocity[howmanysprites]= new XYZ(avelocity);
		size[howmanysprites]=asize;
		initialsize[howmanysprites]=asize;
		initialbrightness[howmanysprites]=abrightness;
		initialvelocity[howmanysprites] = new XYZ(avelocity);
		alivetime[howmanysprites]=0;
		owner[howmanysprites]=0;
		if(howmanysprites<MAXSPRITES-1){howmanysprites++;}
		
		return 0;
	}

	public int makeSprite(int atype, float abrightness, float acolor1, float acolor2, float acolor3, XYZ alocation, XYZ avelocity, float asize, int aowner){
		type[howmanysprites]=atype;
		rotation[howmanysprites]=random()%360;
		brightness[howmanysprites]=abrightness;
		color1[howmanysprites]=acolor1;
		color2[howmanysprites]=acolor2;
		color3[howmanysprites]=acolor3;
		location[howmanysprites]=new XYZ(alocation);
		oldlocation[howmanysprites]=new XYZ(alocation);
		velocity[howmanysprites]=new XYZ(avelocity);
		size[howmanysprites]=asize;
		initialsize[howmanysprites]=asize;
		initialbrightness[howmanysprites]=abrightness;
		initialvelocity[howmanysprites]= new XYZ(avelocity);
		alivetime[howmanysprites]=0;
		owner[howmanysprites]=aowner;
		if(howmanysprites<MAXSPRITES-1){howmanysprites++;}
		
		return 0;
	}

	public int deleteSprite(int which){
		if(which>=0){
			location[which].set(location[howmanysprites-1]);
			oldlocation[which].set(oldlocation[howmanysprites-1]);
			velocity[which].set(velocity[howmanysprites-1]);
			initialsize[which]=initialsize[howmanysprites-1];
			size[which]=size[howmanysprites-1];
			brightness[which]=brightness[howmanysprites-1];
			initialbrightness[which]=initialbrightness[howmanysprites-1];
			color1[which]=color1[howmanysprites-1];
			color2[which]=color2[howmanysprites-1];
			color3[which]=color3[howmanysprites-1];
			alivetime[which]=alivetime[howmanysprites-1];
			rotation[which]=rotation[howmanysprites-1];
			type[which]=type[howmanysprites-1];
			type[howmanysprites-1]=0;
			
			if(howmanysprites>0){howmanysprites--;}
			
		}
		
		return 0;
	}

	public void loadFlareTexture(String fileName) throws IOException
	{
		flaretextureptr = loadTexture(fileName);
	}

	public void loadMuzzleFlareTexture(String fileName) throws IOException
	{
		muzzleflaretextureptr = loadTexture(fileName);
	}

	public void loadSmokeTexture(String fileName) throws IOException
	{
		smoketextureptr = loadTexture(fileName);
	}

	public void loadBloodTexture(String fileName) throws IOException
	{
		bloodtextureptr = loadTexture(fileName);
	}

	public void loadRainTexture(String fileName) throws IOException
	{
		raintextureptr = loadTexture(fileName);
	}

	public void loadSnowTexture(String fileName) throws IOException
	{
		snowtextureptr = loadTexture(fileName);
	}

	public void loadBulletTexture(String fileName) throws IOException
	{
		bullettextureptr = loadTexture(fileName);
	}


	public void doStuff()
	{
		for(int i=0;i<howmanysprites;i++){
			if(type[i]!=BULLET&&type[i]!=BULLET_INSTANT&&type[i]!=GRENADE_SPRITE&&type[i]!=PIN_SPRITE&&type[i]!=GRENADE_SPRITE)oldlocation[i]=location[i];
			if(type[i]==MUZZLEFLASH_SPRITE)brightness[i]-=multiplier*10;
			if(type[i]==FLASH_SPRITE)brightness[i]-=multiplier*10;
			if(type[i]==SMOKE_SPRITE||type[i]==SMOKE_SPRITE_NOUP||type[i]==BLOOD_SPRITE_DOWN||type[i]==PARTICLES_SPRITE_DOWN)brightness[i]-=multiplier*.5;
			if(type[i]==BLOOD_SPRITE_NOUP)brightness[i]-=multiplier*.9;
			if(type[i]==SMOKE_SPRITE)size[i]=(float) (initialsize[i]*abs(sqrt(sqrt(abs(initialbrightness[i]-brightness[i])))));//velocity[i].y+=multiplier*20;
			if(type[i]==BLOOD_SPRITE_NOUP||type[i]==PARTICLES_SPRITE_DOWN)size[i]=(float) (initialsize[i]*(initialbrightness[i]-brightness[i]+.4)*(initialbrightness[i]-brightness[i]+.4));//velocity[i].y+=multiplier*20;
			if(type[i]==BLOOD_SPRITE_NOUP||type[i]==PARTICLES_SPRITE_DOWN)velocity[i]=initialvelocity[i].multiply(brightness[i]);//velocity[i].y+=multiplier*20;
			if(type[i]==BULLET)brightness[i]-=multiplier;
			if(type[i]==SNOW_SPRITE)rotation[i]+=multiplier*50;
			if(type[i]==SMOKE_SPRITE)rotation[i]+=multiplier*30;
			if(type[i]==SMOKE_SPRITE_NOUP)rotation[i]+=multiplier*30;
			if(type[i]==BULLET_INSTANT){
				brightness[i]=0;
			}
			if(type[i]==GRENADE_SPRITE||type[i]==SPOON_SPRITE||type[i]==PIN_SPRITE){
				if(findLengthfast(velocity[i])>0){
					velocity[i].y+=GRAVITY*multiplier;
					rotation[i]+=multiplier*2;
				}
			}
			if(type[i]==BLOOD_SPRITE_DOWN||type[i]==PARTICLES_SPRITE_DOWN){
				velocity[i].y+=GRAVITY*multiplier;
			}
			
			if(type[i]!=BULLET_INSTANT&&type[i]!=BULLET)location[i]=location[i].add(velocity[i].multiply((float)multiplier));
			
			alivetime[i]+=multiplier;
			if(brightness[i]<=0)deleteSprite(i);
			if(type[i]==SNOW_SPRITE&&(location[i].y<-.1||environment!=SNOWY_ENVIRONMENT))deleteSprite(i);
			if(type[i]==RAIN_SPRITE&&(location[i].y<-.1||environment!=RAINY_ENVIRONMENT))deleteSprite(i);
			
			if(type[i]==SNOW_SPRITE){
				if(location[i].x<camera.position.x-precipitationhorz){location[i].x+=precipitationhorz*2;}
				if(location[i].z<camera.position.z-precipitationhorz){location[i].z+=precipitationhorz*2;}
				if(location[i].y<camera.position.y-precipitationvert){location[i].y+=precipitationvert*2;snowdelay+=1/precipitationdensity*2;}
				if(location[i].x>camera.position.x+precipitationhorz){location[i].x-=precipitationhorz*2;}
				if(location[i].z>camera.position.z+precipitationhorz){location[i].z-=precipitationhorz*2;}
				if(location[i].y>camera.position.y+precipitationvert){location[i].y-=precipitationvert*2;}
			}
			if(type[i]==RAIN_SPRITE){
				if(location[i].x<camera.position.x-precipitationhorz*.5){location[i].x+=precipitationhorz;}
				if(location[i].z<camera.position.z-precipitationhorz*.5){location[i].z+=precipitationhorz;}
				if(location[i].y<camera.position.y-precipitationvert){location[i].y+=precipitationvert*2;snowdelay+=1/precipitationdensity/4;}
				if(location[i].x>camera.position.x+precipitationhorz*.5){location[i].x-=precipitationhorz;}
				if(location[i].z>camera.position.z+precipitationhorz*.5){location[i].z-=precipitationhorz;}
				if(location[i].y>camera.position.y+precipitationvert){location[i].y-=precipitationvert*2;}
			}
		}
	}

	public void draw()
	{
		float M[] = new float[16];
		XYZ begProj,endProj,avgProj,persp = new XYZ();
		XYZ between;
		float oolen,dx,dy;
		float bulletsize;
		XYZ point = new XYZ();
		glAlphaFunc(GL_GREATER, 0.01f);
		
		//glEnable(GL_POLYGON_OFFSET_FILL);
		
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
		glAlphaFunc(GL_GREATER, 0.01f);
		for(int i=0;i<howmanysprites;i++){
			if(type[i]==MUZZLEFLASH_SPRITE){fog.tempFog(0,0,0); glBindTexture(GL_TEXTURE_2D, muzzleflaretextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE);}
			if(type[i]==FLASH_SPRITE){fog.tempFog(0,0,0); glBindTexture(GL_TEXTURE_2D, flaretextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE);}
			if(type[i]==SMOKE_SPRITE||type[i]==BLOOD_SPRITE_NOUP){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  smoketextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			if(type[i]==BLOOD_SPRITE_DOWN||type[i]==BLOOD_SPRITE_NOUP){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  smoketextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			if(type[i]==PARTICLES_SPRITE_DOWN){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  bloodtextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			if(type[i]==SNOW_SPRITE){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  snowtextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			if(type[i]==RAIN_SPRITE){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  raintextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			if(type[i]==BULLET||type[i]==BULLET_INSTANT){fog.resetFog(); glBindTexture(GL_TEXTURE_2D,  bullettextureptr);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);}
			
			glPushMatrix();
			if(type[i]==MUZZLEFLASH_SPRITE||type[i]==FLASH_SPRITE||type[i]==SMOKE_SPRITE||type[i]==BLOOD_SPRITE_NOUP||type[i]==SNOW_SPRITE||type[i]==PARTICLES_SPRITE_DOWN||((type[i]==BLOOD_SPRITE_NOUP||type[i]==BLOOD_SPRITE_DOWN)&&blood)){
				glColor4f(color1[i]*fogcolorr*1.6f,color2[i]*fogcolorg*1.6f,color3[i]*fogcolorb*1.6f,brightness[i]);
				glTranslatef(location[i].x,location[i].y,location[i].z);
				if(type[i]==SNOW_SPRITE){
					glTranslatef(sinefluct*rotation[i]/400,0,sinefluct*rotation[i]/400);
				}
				glGetFloatv(GL_MODELVIEW_MATRIX,M);
				point.x=M[12];
				point.y=M[13];
				point.z=M[14];
				glLoadIdentity();
				glTranslatef(point.x, point.y, point.z);
				glRotatef(rotation[i],0,0,1);
				
				glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( .3f*size[i], .3f*size[i], 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size[i], .3f*size[i], 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size[i],-.3f*size[i], 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-.3f*size[i],-.3f*size[i], 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size[i], -.3f*size[i], 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size[i], .3f*size[i], 0.0f);
					glEnd();
				
			}
			
			if(type[i]==RAIN_SPRITE){
				glColor4f(color1[i]*fogcolorr*1.6f,color2[i]*fogcolorg*1.6f,color3[i]*fogcolorb*1.6f,brightness[i]);
				glTranslatef(location[i].x,location[i].y,location[i].z);
				glGetFloatv(GL_MODELVIEW_MATRIX,M);
				for( int k=0; k<3; k+=2 ) 
					for( int j=0; j<3; j++ ) {
						if ( k==j )
							M[k*4+j] = 1.0f;
						else
							M[k*4+j] = 0.0f;
					}
				
				glLoadMatrixf(M);
				glScalef(.2f,(float) (abs(velocity[i].y)*multiplier),.2f);
				glBegin(GL_TRIANGLES);
					glTexCoord2f(1.0f, 1.0f); glVertex3f( .3f*size[i], .3f*size[i], 0.0f);
					glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size[i], .3f*size[i], 0.0f);
					glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size[i],-.3f*size[i], 0.0f);
					glTexCoord2f(0.0f, 0.0f); glVertex3f(-.3f*size[i],-.3f*size[i], 0.0f);
					glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size[i], -.3f*size[i], 0.0f);
					glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size[i], .3f*size[i], 0.0f);
				glEnd();
			}
			
			if(type[i]==GRENADE_SPRITE||type[i]==SPOON_SPRITE||type[i]==PIN_SPRITE){
				glTranslatef(location[i].x,location[i].y+.2f,location[i].z);
				glDisable(GL_BLEND);
				glEnable(GL_FOG);
				glEnable(GL_CULL_FACE);
				glDisable(GL_TEXTURE_2D);
				glEnable(GL_LIGHTING);
				glEnable(GL_COLOR_MATERIAL);
				glDepthMask(true);
				glRotatef(rotation[i],1f,.6f,.3f);
				
				if(type[i]==GRENADE_SPRITE)gunmodels[GRENADEBASEMODEL].draw();
				if(type[i]==SPOON_SPRITE)gunmodels[GRENADESPOONMODEL].draw();
				if(type[i]==PIN_SPRITE)gunmodels[GRENADEPINMODEL].draw();
				
				glEnable(GL_BLEND);
				glDisable(GL_FOG);
				glDisable(GL_CULL_FACE);
				glEnable(GL_TEXTURE_2D);
				glDisable(GL_LIGHTING);
				glDisable(GL_COLOR_MATERIAL);
				glDepthMask(false);
			}
			
			if(type[i]==BULLET||type[i]==BULLET_INSTANT){
				bulletsize=size[i];
				glPushMatrix();
					begProj=location[i];
					endProj=velocity[i];
					glPushMatrix();
						glTranslatef(begProj.getX(),begProj.getY(),begProj.getZ());
						glGetFloatv(GL_MODELVIEW_MATRIX,M);
						begProj.setX(M[12]);
						begProj.setY(M[13]);
						begProj.setZ(M[14]);
					glPopMatrix();
					glPushMatrix();
						glTranslatef(endProj.getX(),endProj.getY(),endProj.getZ());
						glGetFloatv(GL_MODELVIEW_MATRIX,M);
						endProj.setX(M[12]);
						endProj.setY(M[13]);
						endProj.setZ(M[14]);
					glPopMatrix();
					avgProj=endProj.add(begProj).devide(2);
					
					dx	= endProj.getX() - begProj.getX();
					dy	= endProj.getY() - begProj.getY();
					oolen= 1/fast_sqrt(dx*dx+dy*dy)*0.5f;
					persp.set(0);
					persp.x=-dy*oolen; persp.y=dx*oolen;
					
					glColor4f(color1[i]*fogcolorr*1.6f,color2[i]*fogcolorg*1.6f,color3[i]*fogcolorb*1.6f,brightness[i]);
					
					glPushMatrix();
						glLoadIdentity();
						glBegin(GL_TRIANGLES);
							glTexCoord2f(0.0f, 1.0f); glVertex3f(endProj.x-persp.x*bulletsize/2, endProj.y-persp.y*bulletsize/2, endProj.z);
							glTexCoord2f(1.0f, 0.0f); glVertex3f(begProj.x+persp.x*bulletsize/2, begProj.y+persp.y*bulletsize/2, begProj.z);
							glTexCoord2f(0.0f, 0.0f); glVertex3f(begProj.x-persp.x*bulletsize/2, begProj.y-persp.y*bulletsize/2, begProj.z);
							glTexCoord2f(1.0f, 0.0f); glVertex3f(begProj.x+persp.x*bulletsize/2, begProj.y+persp.y*bulletsize/2, begProj.z);
							glTexCoord2f(0.0f, 1.0f); glVertex3f(endProj.x-persp.x*bulletsize/2, endProj.y-persp.y*bulletsize/2, endProj.z);
							glTexCoord2f(1.0f, 1.0f); glVertex3f(endProj.x+persp.x*bulletsize/2, endProj.y+persp.y*bulletsize/2, endProj.z);
						glEnd();
					glPopMatrix();
				glPopMatrix();	
			}
			glPopMatrix();
		}
		fog.resetFog(); 
		glDepthMask(true);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		//glDisable(GL_POLYGON_OFFSET_FILL);
		
	}


}
