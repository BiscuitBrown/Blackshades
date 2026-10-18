package blackshades;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.openal.AL10.*;

import static java.lang.Math.*;

import blackshades.Support.Point;

import static blackshades.Quaternions.*;
import static blackshades.Constants.*;
import static blackshades.Skeleton.*;
import static blackshades.Sprites.*;
import static blackshades.Person.*;
import static blackshades.Support.*;
import static blackshades.Globals.*;
import static blackshades.GameInitDispose.Game.*;
import static blackshades.Support.random;

public class GameDraw {

	public static class Game extends blackshades.Game {
	/*********************> drawGLScene() <*****/
		
		public static XYZ collpoint;

			
		public static boolean drawGLScene() {	

			//Main menu

			if(mainmenu==1){

				//Setup fast sine fluctuation

				sinefluct=(float) sin(sinefluctprog);
				sinefluctprog+=multiplier*1.5f;

				glLoadIdentity();
				glClearColor(0,0,0,1);
				glDisable(GL_CLIP_PLANE0);
				glDisable(GL_FOG);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				//"Black Shades"

				glDisable(GL_TEXTURE_2D);
				glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
				glDisable(GL_CULL_FACE);
				glDisable(GL_LIGHTING);
				glBlendFunc(GL_SRC_ALPHA,GL_ONE);
				glDepthMask(false);
				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPushMatrix();										// Store The Projection Matrix
				glLoadIdentity();									// Reset The Projection Matrix
				glOrtho(0,640,0,480,-100,100);						// Set Up An Ortho Screen
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix

				//Draw smoke

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(60+sinefluct*40,335-sinefluct*9,0);
					glScalef(500-sinefluct*80,70+sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);
					glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);
					glEnable(GL_BLEND);
					glColor4f(1f,1f,1f,.4f+sinefluct/8);
					
					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();

				glPopMatrix();

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(60+sinefluct*40,335-sinefluct*9,0);
					glScalef(530-sinefluct*80,50+sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(-sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);
					glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);
					glEnable(GL_BLEND);
					glColor4f(1f,1f,1f,.4f-sinefluct/8);
					
					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();
					
				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glDepthMask(true);

				//Text

				glEnable(GL_TEXTURE_2D);
				glColor4f(0,0,0,1);
				
				text.glPrint(100,175,"Black Shades",1,2f,640f,480f);

				//"New Game"

				glDisable(GL_TEXTURE_2D);
				glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
				glDisable(GL_CULL_FACE);
				glDisable(GL_LIGHTING);
				glBlendFunc(GL_SRC_ALPHA,GL_ONE);
				glDepthMask(false);
				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPushMatrix();										// Store The Projection Matrix
				glLoadIdentity();									// Reset The Projection Matrix
				glOrtho(0,640,0,480,-100,100);						// Set Up An Ortho Screen
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix

				//Draw smoke

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(120-sinefluct*40,235+sinefluct*9,0);
					glScalef(440+sinefluct*80,70-sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);

					if(mouseoverbutton!=1)glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);

					if(mouseoverbutton==1)glBindTexture(GL_TEXTURE_2D,  flaretextureptr);

					glEnable(GL_BLEND);
					glColor4f(1f,0f,0f,.5f+sinefluct/6);
					
					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();

				glPopMatrix();

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(120-sinefluct*40,235+sinefluct*9,0);
					glScalef(460+sinefluct*80,50-sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(-sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);

					if(mouseoverbutton!=1)glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);

					if(mouseoverbutton==1)glBindTexture(GL_TEXTURE_2D,  flaretextureptr);

					glEnable(GL_BLEND);
					glColor4f(1f,0f,0f,.5f-sinefluct/6);

					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();

				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glDepthMask(true);

				//Text

				glEnable(GL_TEXTURE_2D);
				glColor4f(0,0,0,1);
								
				if(!gameinprogress) {text.glPrint(190- (gameinprogress ? 1 : 0) *10,170,"New Game",1,1.5f,640f,480f);}
				if(gameinprogress) {text.glPrint(190- (gameinprogress ? 1 : 0) *10,170,"Resume Game",1,1.5f,640f,480f);}

				//"Quit"

				glDisable(GL_TEXTURE_2D);
				glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
				glDisable(GL_CULL_FACE);
				glDisable(GL_LIGHTING);
				glBlendFunc(GL_SRC_ALPHA,GL_ONE);
				glDepthMask(false);
				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPushMatrix();										// Store The Projection Matrix
				glLoadIdentity();									// Reset The Projection Matrix
				glOrtho(0,640,0,480,-100,100);						// Set Up An Ortho Screen
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix

				//Draw smoke

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(120-sinefluct*40,112+sinefluct*9,0);
					glScalef(440+sinefluct*80,70-sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);

					if(mouseoverbutton!=2)glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);

					if(mouseoverbutton==2)glBindTexture(GL_TEXTURE_2D,  flaretextureptr);

					glEnable(GL_BLEND);
					glColor4f(1f,0f,0f,.5f+sinefluct/6);

					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();

				glPopMatrix();

				glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();								// Reset The Modelview Matrix
					glTranslatef(120-sinefluct*40,112+sinefluct*9,0);
					glScalef(460+sinefluct*80,50-sinefluct*18,1);
					glTranslatef(.5f,.5f,0f);
					glScalef(.5f,.5f,1f);
					glRotatef(-sinefluctprog*50,0,0,1);
					glEnable(GL_TEXTURE_2D);

					if(mouseoverbutton!=2)glBindTexture(GL_TEXTURE_2D,  sprites.smoketextureptr);

					if(mouseoverbutton==2)glBindTexture(GL_TEXTURE_2D,  flaretextureptr);

					glEnable(GL_BLEND);

					glColor4f(1f,0f,0f,.5f-sinefluct/6);

					glBegin(GL_TRIANGLES);
						glTexCoord2f(1.0f, 1.0f); glVertex3f( 1, 1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1,-1, 0.0f);
						glTexCoord2f(0.0f, 0.0f); glVertex3f(-1,-1, 0.0f);
						glTexCoord2f(1.0f, 0.0f); glVertex3f( 1, -1, 0.0f);
						glTexCoord2f(0.0f, 1.0f); glVertex3f(-1, 1, 0.0f);
					glEnd();

				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
				glPopMatrix();										// Restore The Old Projection Matrix
				glDepthMask(true);

				//Text
				
				glEnable(GL_TEXTURE_2D);
				glColor4f(0,0,0,1);
								
				if(!gameinprogress)text.glPrint(197-(gameinprogress?1:0)*15,87,"Quit",1,1.5f,640f,480f);
				if(gameinprogress)text.glPrint(197-(gameinprogress?1:0)*15,87,"End Game",1,1.5f,640f,480f);

				//High score
				
				glColor4f(.5f+sinefluct/5f,0f,0f,1f);
				
				if(!beatgame) text.glPrint(0,0,"High Score: "+highscore,1,.8f,640f,480f);
				if(beatgame) text.glPrint(0,0,"High Score: "+highscore+" *COMPLETED* Please vote for Black Shades at iDevGames.com!",1,.8f,640f,480f);
				
				glColor4f(.3f-sinefluct/20,.3f-sinefluct/20,.3f-sinefluct/20,1f);
				
				text.glPrint(0,750,"Java port 2026",1,.6f,640f,480f);
				
				//Mandatory udg text
				text.glPrint(500,750,"uDevGame 2002 Entry - Visit iDevGames.com for more games!",1,.6f,640f,480f);
				
				//Mouse (draw)

				glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
				glPushMatrix();										// Store The Projection Matrix
				glLoadIdentity();									// Reset The Projection Matrix
				glOrtho(0,screenwidth,0,screenheight,-100,100);		// Set Up An Ortho Screen
				glMatrixMode(GL_MODELVIEW);			
				glDisable(GL_TEXTURE_2D);

				Point mouseloc = new Point();
				
				getMouse(mouseloc);
				mouseloc.v=screenheight-mouseloc.v;
				
				glColor4f(.1f,0f,0f,1f);

				float size=5;

				glBegin(GL_TRIANGLES);
					glVertex3f(mouseloc.h,mouseloc.v,0);
					glVertex3f(mouseloc.h+2*size,mouseloc.v-2*size,0);
					glVertex3f(mouseloc.h+.5f*size,mouseloc.v-2*size,0);
				glEnd();

				glColor4f(1,0,0,1);

				glBegin(GL_QUADS);
					glVertex3f(olddrawmouse.h,olddrawmouse.v,0);
					glVertex3f(mouseloc.h,mouseloc.v,0);
					glVertex3f(mouseloc.h+2*size,mouseloc.v-2*size,0);
					glVertex3f(olddrawmouse.h+2*size,olddrawmouse.v-2*size,0);

					glVertex3f(olddrawmouse.h,olddrawmouse.v,0);
					glVertex3f(mouseloc.h,mouseloc.v,0);
					glVertex3f(mouseloc.h+.5f*size,mouseloc.v-2*size,0);
					glVertex3f(olddrawmouse.h+.5f*size,olddrawmouse.v-2f*size,0f);

					glVertex3f(olddrawmouse.h+2*size,olddrawmouse.v-2*size,0);
					glVertex3f(mouseloc.h+2f*size,mouseloc.v-2f*size,0);
					glVertex3f(mouseloc.h+.5f*size,mouseloc.v-2f*size,0);
					glVertex3f(olddrawmouse.h+.5f*size,olddrawmouse.v-2f*size,0f);
				glEnd();

				glPopMatrix();

				olddrawmouse=new Point(mouseloc);
				
				//Flash

				if(flashamount>0){

					if(flashamount>1)flashamount=1;

					flashamount-=multiplier;

					if(flashamount<0)flashamount=0;
					
					glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
					glDisable(GL_CULL_FACE);
					glDisable(GL_LIGHTING);
					glDepthMask(false);
					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPushMatrix();										// Store The Projection Matrix
					glLoadIdentity();									// Reset The Projection Matrix
					glOrtho(0,screenwidth,0,screenheight,-100,100);		// Set Up An Ortho Screen
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();									// Reset The Modelview Matrix
					glScalef(screenwidth,screenheight,1);
					glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
					glEnable(GL_BLEND);
					glColor4f(flashr,flashg,flashb,flashamount);

					glBegin(GL_QUADS);
						glVertex3f(0,		0, 	 0.0f);
						glVertex3f(256,	0, 	 0.0f);
						glVertex3f(256,	256, 0.0f);
						glVertex3f(0, 	256, 0.0f);
					glEnd();

					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glEnable(GL_DEPTH_TEST);							// Enables Depth Testing
					glEnable(GL_CULL_FACE);
					glDisable(GL_BLEND);
					glDepthMask(true);

				}	
			}

			//If in-game

			if(mainmenu!=1){
				
				//If flashing to fix menu bug, go back to menu after a frame

				if(mainmenu==2)mainmenu=1;

				glLoadIdentity();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glEnable(GL_DEPTH_TEST);
				glDisable(GL_CLIP_PLANE0);

				//Visions

				sinefluct=(float) sin(sinefluctprog);

				sinefluctprog+=multiplier*3;

				if(visions==0){

					//environment light

					fog = new Fog();
					
					if(environment==SUNNY_ENVIRONMENT){

						viewdistance=2000;	
						fogcolorr=.5f;
						fogcolorg=.5f;
						fogcolorb=.8f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.1f);
					}

					if(environment==FOGGY_ENVIRONMENT){

						viewdistance=500;	
						fogcolorr=.5f;
						fogcolorg=.5f;
						fogcolorb=.5f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.2f);

					}

					if(environment==NIGHT_ENVIRONMENT){

						viewdistance=500;	
						fogcolorr=.15f;
						fogcolorg=.15f;
						fogcolorb=.3f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.2f);
					}

					if(environment==SNOWY_ENVIRONMENT){

						viewdistance=800;	
						fogcolorr=.5f;
						fogcolorg=.5f;
						fogcolorb=.5f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.2f);
					}

					if(environment==RAINY_ENVIRONMENT){

						viewdistance=700;	
						fogcolorr=.3f;
						fogcolorg=.3f;
						fogcolorb=.3f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.2f);
					}

					if(environment==FIREY_ENVIRONMENT){

						viewdistance=600;	
						fogcolorr=.3f;
						fogcolorg=.1f;
						fogcolorb=0f;
						fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.2f);
					}

					glClearColor(fogcolorr,fogcolorg,fogcolorb,1);

					if(environment==SUNNY_ENVIRONMENT){

						float LightAmbient[]= { fogcolorr/4f, fogcolorg/4f, fogcolorb/4f, 1.0f};
						float LightDiffuse[]= { fogcolorr*1.6f, fogcolorg*1.6f, fogcolorr*1.6f, 1.0f };
						glLightfv(GL_LIGHT0, GL_AMBIENT, LightAmbient);		
						glLightfv(GL_LIGHT0, GL_DIFFUSE, LightDiffuse);	
					}

					if(environment!=SUNNY_ENVIRONMENT){

						float LightAmbient[]=	{ fogcolorr*.8f, fogcolorg*.8f, fogcolorb*.8f, 1.0f};
						float LightDiffuse[]=	{ fogcolorr*.8f, fogcolorg*.8f, fogcolorr*.8f, 1.0f };
						glLightfv(GL_LIGHT0, GL_AMBIENT, LightAmbient);		
						glLightfv(GL_LIGHT0, GL_DIFFUSE, LightDiffuse);	

					}			

					glEnable(GL_LIGHT0);	

					//Change fov if zooming with scope

					if(!zoom)reSizeGLScene(90,.1f);

					if(zoom)reSizeGLScene(10,.6f);

					nocolors=0;
				}

				if(visions==1){

					//light

					float LightAmbient[]=		{ 0f, 0f, 0f, 1.0f};
					float LightDiffuse[]=		{ .1f+sinefluct/5f, 0f, 0f, 1.0f };

					glLightfv(GL_LIGHT0, GL_AMBIENT, LightAmbient);		
					glLightfv(GL_LIGHT0, GL_DIFFUSE, LightDiffuse);		
					glEnable(GL_LIGHT0);	

					fogcolorr=(sinefluct/4+.5f);
					fogcolorg=0;
					fogcolorb=0;

					fog.setFog(fogcolorr,fogcolorg,fogcolorb,0f,viewdistance*.8f*.5f*(sinefluct/4f+.3f),sinefluct/3f+.7f);
					glClearColor(fogcolorr,fogcolorg,fogcolorb,1);
					reSizeGLScene(120-sinefluct*20f,.3f);
					glRotatef(sinefluct*10f,0f,0f,.1f);
					nocolors=1;

					//Pitch higher if moving for effect

					if(person[0].currentanimation==IDLEANIM)alSourcef(gSourceID[VISIONSOUND], AL_PITCH, 1);

					if(person[0].currentanimation!=IDLEANIM)alSourcef(gSourceID[VISIONSOUND], AL_PITCH, 2);
				}
		
				//Camera

				float bluramount=.1f* (blurness?1:0);

				blur=!blur;

				//Set rotation/position

				if(thirdperson!=0)glTranslatef(camera.targetoffset.getX(),camera.targetoffset.getY(),camera.targetoffset.getZ());

				if(thirdperson!=2&&(!person[0].skeleton.free||thirdperson!=0)){

					glRotatef(camera.visrotation2+-bluramount/2+(blur?1:0)*bluramount,1,0,0);
					glRotatef(camera.visrotation+-bluramount/2+(blur?1:0)*bluramount,0,1,0);
				}

				if(thirdperson==0&&person[0].skeleton.free){

					glRotatef(person[0].skeleton.joints[(person[0].skeleton.jointlabels[HEAD])].rotate3,0,1,0);
					glRotatef(180-(person[0].skeleton.joints[(person[0].skeleton.jointlabels[HEAD])].rotate2+90),0,0,1);
					glRotatef(person[0].skeleton.joints[(person[0].skeleton.jointlabels[HEAD])].rotate1+90,0,1,0);
				}

				if(thirdperson==2){
					glRotatef(oldrot2+-bluramount/2+(float)(blur?1:0)*bluramount,1,0,0);
					glRotatef(oldrot+-bluramount/2+(float)(blur?1:0)*bluramount,0,1,0);
				}

				//Shake camera if grenade explosion

				if(camerashake>0){

					if(!(!person[0].aiming||person[0].whichgun==GRENADE||thirdperson!=0)){
						camerashake=0;
					}
					glTranslatef((float)(random()%100)/100*camerashake,(float)(random()%100)/100*camerashake,(float)(random()%100)/100*camerashake);

				}

				camera.apply();

				glPushMatrix();
					glClipPlane(GL_CLIP_PLANE0, eqn);
					glDisable(GL_CLIP_PLANE0);
				glPopMatrix();

				frustum.getFrustum();

				float LightPosition[]=	{ -.5f, 1f, -.8f, 0.0f };

				glLightfv(GL_LIGHT0, GL_POSITION, LightPosition);

				glDisable(GL_TEXTURE_2D);
				glEnable(GL_FOG);
				glEnable(GL_COLOR_MATERIAL);
				glEnable(GL_CULL_FACE);
				glDepthMask(true);

				//Draw street

				glPushMatrix();
					glDepthMask(false);
					glDisable(GL_DEPTH_TEST);
					glEnable(GL_LIGHTING);
					glTranslatef(camera.position.getX(),0,camera.position.getZ());
					glScalef(viewdistance*5/100,1,viewdistance*5/100);
					
					if(visions==0)street.draw(.22f,.22f,.22f);

					if(visions==1)street.draw(0,0,0);

					glEnable(GL_DEPTH_TEST);
					glDepthMask(true);
				glPopMatrix();

				if(visions==0)glEnable(GL_LIGHTING);

				if(visions==1)glDisable(GL_LIGHTING);
				
				//Draw blocks

				glEnable(GL_BLEND);

				XYZ move = new XYZ();

				int beginx,endx;
				int beginz,endz;
				int distsquared;

				//Only nearby blocks

				beginx=(int) ((camera.position.getX()-viewdistance+BLOCK_SPACING/2)/BLOCK_SPACING-2);

				if(beginx<0)beginx=0;

				beginz=(int) ((camera.position.getZ()-viewdistance+BLOCK_SPACING/2)/BLOCK_SPACING-2);

				if(beginz<0)beginz=0;

				

				endx=(int) ((camera.position.getX()+viewdistance+BLOCK_SPACING/2)/BLOCK_SPACING+2);

				if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;

				endz=(int) ((camera.position.getZ()+viewdistance+BLOCK_SPACING/2)/BLOCK_SPACING+2);

				if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

				

				boolean draw;

				int whichtri;

				if(collpoint == null) collpoint = new XYZ();
				else collpoint.set(0);

				for(int i=beginx;i<=endx;i++){
					for(int j=beginz;j<=endz;j++){
						drawn[i][j]=true;
					}
				}

				if(beginx<endx&&beginz<endz)

				for(int i=beginx;i<=endx;i++){

					for(int j=beginz;j<=endz;j++){

						draw=true;

						//Only draw if visible

						distsquared=(int) (((i)*BLOCK_SPACING-camera.position.getX())*((i)*BLOCK_SPACING-camera.position.getX())+((j)*BLOCK_SPACING-camera.position.getZ())*((j)*BLOCK_SPACING-camera.position.getZ()));

						if(distsquared>(viewdistance*viewdistance+BLOCK_SPACING*BLOCK_SPACING	))draw=false;

						if(draw&&citytype[i][j]!=3&&frustum.cubeInFrustum((i)*BLOCK_SPACING,0,(j)*BLOCK_SPACING,BLOCK_SPACING)==0)draw=false;

						if(draw&&citytype[i][j]!=3&&frustum.sphereInFrustum(blocks[citytype[i][j]].boundingspherecenter.getX()+(i)*BLOCK_SPACING,blocks[citytype[i][j]].boundingspherecenter.getY(),blocks[citytype[i][j]].boundingspherecenter.getZ()+(j)*BLOCK_SPACING,blocks[citytype[i][j]].boundingsphereradius)==0)draw=false;

						if(draw){
							glPushMatrix();
								glTranslatef(i*BLOCK_SPACING,0,j*BLOCK_SPACING);
								glRotatef(cityrotation[i][j]*90,0,1,0);
								blocks[citytype[i][j]].draw();								
							glPopMatrix();
						}

						if(!draw){
							move.setY(0);
							move.setX(i*BLOCK_SPACING);
							move.setZ(j*BLOCK_SPACING);

							if(findDistancefast(move,camera.position)<300000)drawn[i][j]=false;
						}
					}
				}

				//Decals

				decals.draw();

				//Occluding blocks

				beginx=(int) ((camera.position.getX()+BLOCK_SPACING/2)/BLOCK_SPACING-2);

				if(beginx<0)beginx=0;

				beginz=(int) ((camera.position.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING-2);

				if(beginz<0)beginz=0;

				

				endx=(int) ((camera.position.getX()+BLOCK_SPACING/2)/BLOCK_SPACING+2);

				if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;

				endz=(int) ((camera.position.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING+2);

				if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

				

				float M[] = new float[16];

				XYZ drawpoint = new XYZ();

				float size=20;

				

				//Draw people

				if(visions==1)fog.setFog(fogcolorr,fogcolorg,fogcolorb,0f,viewdistance*.8f*.5f*(-sinefluct/4f+.3f),-sinefluct/3f+.7f);

				glColor4f(1,1,1,1);
				glEnable(GL_COLOR_MATERIAL);
				glEnable(GL_BLEND);

				for(int i=0;i<numpeople;i++){

					draw=true;

					if(!person[i].skeleton.free){

						if(person[i].whichblockx>=0&&person[i].whichblockx<NUM_BLOCKS&&person[i].whichblocky>=0&&person[i].whichblocky<NUM_BLOCKS){

							if(!drawn[person[i].whichblockx][person[i].whichblocky])draw=false;

						}else draw=false;

						if(draw)

						if(frustum.cubeInFrustum(person[i].playercoords.getX(),person[i].playercoords.getY(),person[i].playercoords.getZ(),5) == 0)draw=false;

						if(draw)

						if(findDistancefast(person[i].playercoords,camera.position)>1000000)draw=false;

						if(draw)

						for(int j=beginx;j<=endx;j++){

							for(int k=beginz;k<=endz;k++){

								if(draw){

									move.setY(0);
									move.setX(j*BLOCK_SPACING);
									move.setZ(k*BLOCK_SPACING);

									if(findDistancefast(move,camera.position)<100000){
										whichtri=blockocclude.lineCheck2(camera.position,person[i].playercoords,collpoint,move,0);
										if(whichtri!=-1)draw=false;

									}

								}

							}

						}

						if(draw){
							
							move.setY(0);
							move.setX(person[i].whichblockx*BLOCK_SPACING);
							move.setZ(person[i].whichblocky*BLOCK_SPACING);
							whichtri=blockocclude.lineCheck2(camera.position,person[i].playercoords,collpoint,move,0);

							if(whichtri!=-1)draw=false;

						}

						if(i==0)draw=true;
					}

					if(person[i].skeleton.free){

						if(draw)

						if(person[i].skeleton.broken==0&&frustum.cubeInFrustum(person[i].averageloc.getX(),person[i].averageloc.getY(),person[i].averageloc.getZ(),5)==0)draw=false;

						if(draw)

						if(findDistancefast(person[i].averageloc,camera.position)>1000000)draw=false;

						if(draw)

						if(person[i].skeleton.joints[0].position.getY()<-2)draw=false;

						for(int j=beginx;j<=endx;j++){

							for(int k=beginz;k<=endz;k++){

								if(draw){

									move.setY(0);
									move.setX(j*BLOCK_SPACING);
									move.setZ(k*BLOCK_SPACING);

									if(findDistancefast(move,camera.position)<100000){
										whichtri=blockocclude.lineCheck2(camera.position,person[i].averageloc,collpoint,move,0);
										if(whichtri!=-1)draw=false;

									}

								}

							}

						}

						if(draw){

							move.setY(0);
							move.setX(person[i].whichblockx*BLOCK_SPACING);
							move.setZ(person[i].whichblocky*BLOCK_SPACING);
							whichtri=blockocclude.lineCheck2(camera.position,person[i].averageloc,collpoint,move,0);

							if(whichtri!=-1)draw=false;

						}
						if(i==0)draw=true;
					}

					if(draw&&person[i].existing){

						
						if((findDistancefast(person[i].playercoords,camera.position)<100000+(zoom?1:0)*3000000&&!person[i].skeleton.free)||(findDistancefast(person[i].averageloc,camera.position)<100000+(zoom?1:0)*3000000&&person[i].skeleton.free)){

							glPushMatrix();

								if(!person[i].skeleton.free){
									glTranslatef(person[i].playercoords.getX(),person[i].playercoords.getY(),person[i].playercoords.getZ());
									glRotatef(person[i].playerrotation,0,1,0);					
									if(i!=0||visions==0)person[i].doAnimations(i);
									if(i==0&&visions==1)person[i].doAnimationslite(i);
								}

								if(visions==1)nocolors=1;

								if(visions==1&&person[i].type==EVILTYPE)nocolors=2;

								if(visions==1&&person[i].type==VIPTYPE)nocolors=3;

								if(!(visions==1&&i==0)&&!(zoom&&i==0))person[i].drawSkeleton(i);

							glPopMatrix();

						}

						else{

							glPushMatrix();

								if(!person[i].skeleton.free)person[i].doAnimationslite(i);

								glColor4f(1,1,1,1);
								glEnable(GL_BLEND);
								glDisable(GL_CULL_FACE);
								glEnable(GL_TEXTURE_2D);
								glDisable(GL_LIGHTING);
								glDepthMask(false);
								glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

								if(!person[i].skeleton.free){
									glBindTexture(GL_TEXTURE_2D, personspritetextureptr);
									glTranslatef(person[i].playercoords.getX(),person[i].playercoords.getY()+size/2f*.3f,person[i].playercoords.getZ());
								}

								if(person[i].skeleton.free){
									glBindTexture(GL_TEXTURE_2D, deadpersonspritetextureptr);
									glTranslatef(person[i].averageloc.getX(),person[i].averageloc.getY()+size/2f*.3f,person[i].averageloc.getZ());
								}

								glGetFloatv(GL_MODELVIEW_MATRIX,M);
								drawpoint.setX(M[12]);
								drawpoint.setY(M[13]);
								drawpoint.setZ(M[14]);
								glLoadIdentity();
								glTranslatef(drawpoint.getX(), drawpoint.getY(), drawpoint.getZ());

								glBegin(GL_TRIANGLES);
									glTexCoord2f(1.0f, 1.0f); glVertex3f( .3f*size, .3f*size, 0.0f);
									glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size, .3f*size, 0.0f);
									glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size,-.3f*size, 0.0f);
									glTexCoord2f(0.0f, 0.0f); glVertex3f(-.3f*size,-.3f*size, 0.0f);
									glTexCoord2f(1.0f, 0.0f); glVertex3f( .3f*size, -.3f*size, 0.0f);
									glTexCoord2f(0.0f, 1.0f); glVertex3f(-.3f*size, .3f*size, 0.0f);
								glEnd();

							glPopMatrix();
							glDepthMask(true);
							glDisable(GL_TEXTURE_2D);
							glEnable(GL_CULL_FACE);

							if(visions!=1)glEnable(GL_LIGHTING);
						}
					}

					if(!person[i].skeleton.free &&!draw)person[i].doAnimationslite(i);

					if(!person[i].existing)
						if(!draw||findDistancefast(person[i].playercoords,camera.position)>10000){person[i].existing=true;}
				}

				glDisable(GL_COLOR_MATERIAL);
				glDisable(GL_BLEND);

				//Attacker psychicness
				
				for(int i=0;i<numpeople;i++){

					if(person[i].killtarget>-1&&person[i].killtargetvisible&&!person[i].skeleton.free&&!person[person[i].killtarget].skeleton.free){
						sprites.makeSprite(BULLET_INSTANT, (SHOTDELAYAMOUNT/difficulty-person[i].shotdelay)/SHOTDELAYAMOUNT/difficulty/2, 1, person[i].shotdelay/SHOTDELAYAMOUNT/difficulty, person[i].shotdelay/SHOTDELAYAMOUNT/difficulty, doRotation(person[i].skeleton.joints[person[i].skeleton.jointlabels[LEFTHAND]].position,0,person[i].playerrotation,0).add(person[i].playercoords), person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[ABDOMEN]].position.add(person[person[i].killtarget].playercoords), person[i].shotdelay*2);
					}

					if(person[i].killtarget>-1&&person[i].killtargetvisible&&!person[i].skeleton.free&&person[person[i].killtarget].skeleton.free){
						sprites.makeSprite(BULLET_INSTANT, (SHOTDELAYAMOUNT/difficulty-person[i].shotdelay)/SHOTDELAYAMOUNT/difficulty/2, 1, person[i].shotdelay/SHOTDELAYAMOUNT/difficulty, person[i].shotdelay/SHOTDELAYAMOUNT/difficulty, doRotation(person[i].skeleton.joints[person[i].skeleton.jointlabels[LEFTHAND]].position,0,person[i].playerrotation,0).add(person[i].playercoords), person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[ABDOMEN]].position, person[i].shotdelay*2);
					}

				}

				//Sprites

				glEnable(GL_CLIP_PLANE0);
				sprites.draw();

				glDisable(GL_CLIP_PLANE0);

				glDisable(GL_FOG);

				//Zoom

				glAlphaFunc(GL_GREATER, 0.01f);

				if(zoom){
					glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
					glDisable(GL_CULL_FACE);
					glDisable(GL_LIGHTING);
					glDepthMask(false);
					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPushMatrix();										// Store The Projection Matrix
					glLoadIdentity();									// Reset The Projection Matrix
					glOrtho(0,screenwidth,0,screenheight,-100,100);		// Set Up An Ortho Screen
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();									// Reset The Modelview Matrix
					glScalef(screenwidth,screenheight,1);
					glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
					glEnable(GL_BLEND);
					glEnable(GL_TEXTURE_2D);
					glColor4f(.5f,.5f,.5f,1f);
					glBindTexture(GL_TEXTURE_2D, scopetextureptr);
					
					glBegin(GL_QUADS);
						glTexCoord2f(0,0);
						glVertex3f(0,		0, 	 0.0f);
						glTexCoord2f(1,0);
						glVertex3f(1,	0, 	 0.0f);
						glTexCoord2f(1,1);
						glVertex3f(1,	1, 0.0f);
						glTexCoord2f(0,1);
						glVertex3f(0, 	1, 0.0f);
					glEnd();

					glDisable(GL_TEXTURE_2D);
					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glEnable(GL_DEPTH_TEST);							// Enables Depth Testing
					glEnable(GL_CULL_FACE);
					glDisable(GL_BLEND);
					glDepthMask(true);
				}	

				//Flash

				if(flashamount>0){

					if(flashamount>1)flashamount=1;

					flashamount-=multiplier;

					if(flashamount<0)flashamount=0;

					glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
					glDisable(GL_CULL_FACE);
					glDisable(GL_LIGHTING);
					glDepthMask(false);
					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPushMatrix();										// Store The Projection Matrix
					glLoadIdentity();									// Reset The Projection Matrix
					glOrtho(0,screenwidth,0,screenheight,-100,100);		// Set Up An Ortho Screen
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();									// Reset The Modelview Matrix
					glScalef(screenwidth,screenheight,1);
					glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
					glEnable(GL_BLEND);
					glColor4f(flashr,flashg,flashb,flashamount);

					glBegin(GL_QUADS);
						glVertex3f(0,		0, 	 0.0f);
						glVertex3f(256,	0, 	 0.0f);
						glVertex3f(256,	256, 0.0f);
						glVertex3f(0, 	256, 0.0f);
					glEnd();

					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glEnable(GL_DEPTH_TEST);							// Enables Depth Testing
					glEnable(GL_CULL_FACE);
					glDisable(GL_BLEND);
					glDepthMask(true);
				}	

				if(person[0].skeleton.free&&thirdperson!=2){

					glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
					glDisable(GL_CULL_FACE);
					glDisable(GL_LIGHTING);
					glDepthMask(false);
					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPushMatrix();										// Store The Projection Matrix
					glLoadIdentity();									// Reset The Projection Matrix
					glOrtho(0,screenwidth,0,screenheight,-100,100);		// Set Up An Ortho Screen
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPushMatrix();										// Store The Modelview Matrix
					glLoadIdentity();									// Reset The Modelview Matrix
					glScalef(screenwidth,screenheight,1);
					glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
					glEnable(GL_BLEND);
					glColor4f(0,0,0,1-person[0].longdead);
					
					glBegin(GL_QUADS);
						glVertex3f(0,		0, 	 0.0f);
						glVertex3f(256,	0, 	 0.0f);
						glVertex3f(256,	256, 0.0f);
						glVertex3f(0, 	256, 0.0f);
					glEnd();

					glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
					glPopMatrix();										// Restore The Old Projection Matrix
					glEnable(GL_DEPTH_TEST);							// Enables Depth Testing
					glEnable(GL_CULL_FACE);
					glDisable(GL_BLEND);
					glDepthMask(true);
				}	
				
				//Text

				glEnable(GL_TEXTURE_2D);
				glColor4f(1,1,1,1);
								
				if(!debug){
				
				switch(person[0].whichgun) {
				
					case NOGUN:
						text.glPrint(10,90,"UNARMED",1,.8f,640f,480f); break;
						
					case KNIFE:
						text.glPrint(10,90,"KNIFE",1,.8f,640f,480f); break;
					
					case ASSAULTRIFLE:
						text.glPrint(10,90,"ASSAULT RIFLE",1,.8f,640f,480f); break;
					
					case SHOTGUN:
						text.glPrint(10,90,"SHOTGUN",1,.8f,640f,480f); break;
					
					case SNIPERRIFLE:
						text.glPrint(10,90,"SNIPER RIFLE",1,.8f,640f,480f); break;
					
					case GRENADE:
						text.glPrint(10,90,"HAND GRENADE",1,.8f,640f,480f); break;
					
					case HANDGUN1:
						text.glPrint(10,90,"MAGNUM",1,.8f,640f,480f); break;
					
					case HANDGUN2:
						text.glPrint(10,90,"HANDGUN",1,.8f,640f,480f); break;
				}
				
				if(person[0].whichgun!=NOGUN&&person[0].whichgun!=KNIFE&&person[0].whichgun!=GRENADE){

					glColor4f(0,1,0,1);
					text.glPrint(10,30,"Magazines",0,.8f,640f,480f);
					glColor4f(1,1,1,1);									
					text.glPrint(10,10,"0"+(person[0].reloads[person[0].whichgun]),1,1,640,480);

					if(person[0].ammo>=0){
												
						glColor4f(0,1,0,1);
						text.glPrint(10,65,"Ammo",0,.8f,640f,480f);
						glColor4f(1,1,1,1);
												
						if(person[0].ammo>=10) {text.glPrint(10,40,person[0].ammo+"",1,1,640,480);}
						if(person[0].ammo<10) {text.glPrint(10,40,"0"+person[0].ammo,1,1,640,480);}
						
					}
				}
				
				if(person[0].whichgun==GRENADE){
					text.glPrint(10,20,"Grenades Left: "+(int)(person[0].reloads[person[0].whichgun]+1),0,.8f,640f,480f);
				}

				glColor4f(1,1,0,1);
				text.glPrint(725,575,"Score",0,.8f,640f,480f);
				glColor4f(1,1,1,1);
				text.glPrint(580,440,""+score,1,1,640,480);

				glColor4f(1,1,0,1);
				text.glPrint(50,575,"Time Remaining",0,.8f,640f,480f);
				glColor4f(1,1,1,1);

				if((int)timeremaining%60>=10) {
					text.glPrint((int)72.5,440,((int)timeremaining/60)+":"+((int)timeremaining%60),1,1f,640f,480f);
				}

				if((int)timeremaining%60<10) {
					text.glPrint((int)72.5,440,((int)timeremaining/60)+":0"+((int)timeremaining%60),1,1f,640f,480f);
				}
				}

				if(debug){
					
				text.glPrint(10,30,"The framespersecond is "+((int)(fps))+" out of maximum "+(maxfps << 1)+".",0,.8f,(float)screenwidth,(float)screenheight);

				switch(enemystate) {
					case 0:
						text.glPrint(10,20,"Enemies are in random assassination mode.",0,.8f,(float)screenwidth,(float)screenheight); break;
					case 1:
						text.glPrint(10,20,"Enemies are in passive mode.",0,.8f,(float)screenwidth,(float)screenheight); break;
					case 2:
						text.glPrint(10,20,"Enemies are in DIE!!!! mode.",0,.8f,(float)screenwidth,(float)screenheight); break;
				}
				
				text.glPrint(10,60,"You have pointlessly shot or beaten "+civkills+" unarmed civilians.",0,.8f, (float) screenwidth,(float)screenheight);
				text.glPrint(10,70,"You have incapacitated "+goodkills+" assassins.",0,.8f,(float)screenwidth,(float)screenheight);
				text.glPrint(10,80,"You have allowed "+badkills+" successful murders.",0,.8f,(float)screenwidth,(float)screenheight);

				}

				/*
				sprintf (string, "");

				text.glPrint(10,80,string,0,.8,screenwidth,screenheight);
				*/	

			}

			return true;
		}

	}

}
