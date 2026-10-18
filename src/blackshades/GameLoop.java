package blackshades;

import static blackshades.Quaternions.*;
import static blackshades.Support.*;
import static blackshades.Support.random;
import static blackshades.Constants.*;
import static blackshades.Globals.*;
import static blackshades.GameTick.Game.*;
import static blackshades.GameDraw.Game.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.Math.*;


public class GameLoop {
	
	private static boolean pressed[] = new boolean[6];

	public static class Game extends blackshades.Game {
	/********************> handleKeyDown() <*****/
		
		public static void handleKeyDown() {

			XYZ facing = new XYZ();
			
			if(mainmenu==0){
				
			if(glfwGetKey(window, GLFW_KEY_L) == GLFW_PRESS && !pressed[0]) {
				if(!lasersight)lasersight=true;
				else lasersight=false; 
				pressed[0] = true;
			}
			else if(glfwGetKey(window, GLFW_KEY_L) != GLFW_PRESS && pressed[0])
				pressed[0] = false;
			
			else if(glfwGetKey(window, GLFW_KEY_K) == GLFW_PRESS && !pressed[1] && debug) {
				timeremaining=0;
				pressed[1] = true;
			}
			
			else if(glfwGetKey(window, GLFW_KEY_K) != GLFW_PRESS && pressed[1] && debug)
				pressed[1] = false;
						
			else if(glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS && !pressed[2]) {
				pressed[2] = true;
				if(debug){

					alSourcePlay(gSourceID[SOULINSOUND]);

					if(slomo==0)slomo=1;

					else slomo=0;}

				if(slomo!=0){
					alSourcef(gSourceID[KNIFESONG], AL_PITCH, .5f);
					alSourcef(gSourceID[SHOOTSONG], AL_PITCH, .5f);
					alSourcef(gSourceID[ZOMBIESONG], AL_PITCH, .5f);
				}

				if(slomo!=0){
					alSourcef(gSourceID[KNIFESONG], AL_PITCH, 1);
					alSourcef(gSourceID[SHOOTSONG], AL_PITCH, 1);
					alSourcef(gSourceID[ZOMBIESONG], AL_PITCH, 1);
				}
			}
			
			else if(glfwGetKey(window, GLFW_KEY_B) != GLFW_PRESS && pressed[2])
				pressed[2] = false;
			
			else if(glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS && glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS && !pressed[3] && debug) {
				alSourcePlay(gSourceID[SOULINSOUND]);
				paused=!paused;
				pressed[3] = true; 
			}
			
			else if(glfwGetKey(window, GLFW_KEY_B) != GLFW_PRESS && glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_PRESS && pressed[3] && debug) 
				pressed[3] = false;
			
			else if(glfwGetKey(window, GLFW_KEY_F) == GLFW_PRESS && !pressed[4] && debug) {
				
				pressed[4] = true;
				
				alSourcePlay(gSourceID[SOULOUTSOUND]);
				//Facing
				facing.set(0);
				facing.setZ(-1);

				facing.set(doRotation(facing,-camera.rotation2,0,0));
				facing.set(doRotation(facing,0,0-camera.rotation,0));

				for(int i=1;i<numpeople;i++){				
					if(!person[i].skeleton.free){
					if(findDistancefast(person[i].playercoords,person[0].playercoords)<1000){
					person[i].skeleton.free=true;
					person[i].longdead=1;

					for(int j=0;j<person[i].skeleton.num_joints;j++){
						person[i].skeleton.joints[j].position.set(doRotation(person[i].skeleton.joints[j].position,0,person[i].playerrotation,0));
						person[i].skeleton.joints[j].position.incrementAdd(person[i].playercoords);
						person[i].skeleton.joints[j].realoldposition.set(person[i].skeleton.joints[j].position);
						person[i].skeleton.joints[j].velocity.set(doRotation(person[i].skeleton.joints[j].velocity,0,person[i].playerrotation,0));
						person[i].skeleton.joints[j].velocity.incrementAdd(person[i].velocity);
						person[i].skeleton.joints[j].velocity.incrementAdd(facing.multiply(50));
						person[i].skeleton.joints[j].velocity.x+=abs(random()%20)-10;
						person[i].skeleton.joints[j].velocity.y+=abs(random()%20)-10;
						person[i].skeleton.joints[j].velocity.z+=abs(random()%20)-10;

					}}}

				}
			}
			
			else if(glfwGetKey(window, GLFW_KEY_F) != GLFW_PRESS && pressed[4] && debug)
				pressed[4] = false;
			
			else if(glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS && !pressed[5] && debug) {
				
				pressed[5] = true;
				
				if(person[0].grenphase==0){
					person[0].ammo=-1;
					person[0].whichgun++;
					person[0].grenphase=0;
					person[0].reloads[person[0].whichgun]=3;
					if(person[0].whichgun>7)person[0].whichgun=0;
				}
			}
			
			else if(glfwGetKey(window, GLFW_KEY_X) != GLFW_PRESS && pressed[5] && debug)
				pressed[5] = false;

			}

		}
		
		/********************> eventLoop() <
		 * @throws IOException *****/

		public static void eventLoop() throws IOException {

			int colaccuracy;
			float oldmult;
			gQuit = false;
			
			double previousTime = glfwGetTime();
			int frameCount = 0;
			
			while ( !gQuit && !glfwWindowShouldClose(window) ) {
				
			    // Measure speed
			    double currentTime = glfwGetTime();
			    frameCount++;
			    // If a second has passed.
			    if ( currentTime - previousTime >= 1.0 )
			    {
			    	fps = frameCount;

			        frameCount = 0;
			        previousTime = currentTime;
			    }

			    // Rest of your game loop...
																
				glfwPollEvents();
				
				if(!vblsync)start = glfwGetTimerValue();
				else start = glfwGetTime();
																
				colaccuracy=(int) (sps/framespersecond)+1;
				
				if(colaccuracy>sps){colaccuracy=(int) sps;}

				oldmult=(float) multiplier;
				multiplier/=colaccuracy;
				
				for(int i=0;i<(int)(colaccuracy+.5);i++)
					tick();	

				multiplier=oldmult;

				if (drawGLScene())
					glfwSwapBuffers(window);
				
				else
					gQuit = true; 

				oldmult=(float) multiplier;	

				if(!vblsync)end = glfwGetTimerValue();
				else end = glfwGetTime();
				timetaken=end-start;
				
				if(!vblsync) framespersecond=600000000/timetaken;
				else framespersecond=.6/timetaken;
																				
				while(framespersecond>=maxfps){					
					if(!vblsync)end = glfwGetTimerValue();
					else end = glfwGetTime();
					timetaken=end-start;
					if(!vblsync)framespersecond=600000000/timetaken;
					else framespersecond=.6/timetaken;
				}
							
				multiplier5=multiplier4;
				multiplier4=multiplier3;
				multiplier3=multiplier2;
				multiplier2=1/framespersecond;
				multiplier=(multiplier2+multiplier3+multiplier4+multiplier5)/4;

				if(multiplier>1)multiplier=1;
				if(multiplier<.00001)multiplier=.00001;
				if(visions==1&&mainmenu==0)multiplier/=3;

				if(slomo!=0)multiplier*=.2;
				if(paused)multiplier=0;
				
				handleKeyDown();
	            
    			if ( (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) && (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) ) {	    			
	    			gQuit = true;

	    			File file = new File("Data/Highscore");
					Scanner myReader = new Scanner(file);
		            myReader.useLocale(Locale.US);
		            
					if(score>highscore){

						highscore=score;
						
						highscore = myReader.nextInt();
						beatgame = myReader.nextInt()==1;

						myReader.close();

					}
    			}
				
    			else if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS && mainmenu == 0) {
    				
    				oldslomo = slomo;
    				
    				slomo = 0;
    				if(!fullscreen)
    					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

					alSourcePause(gSourceID[RAINSOUND]);

					mainmenu=1;
					alSourcePlay(gSourceID[SOULOUTSOUND]);
					flashamount=1;
					flashr=1;flashg=1;flashb=1;
					alSourceStop(gSourceID[VISIONSOUND]);
					whichsong=MAINMENUSONG;
					alSourceStop(gSourceID[KNIFESONG]);
					alSourceStop(gSourceID[SHOOTSONG]);
					alSourceStop(gSourceID[ZOMBIESONG]);
					alSourceStop(gSourceID[MAINMENUSONG]);
					alSourcef(gSourceID[KNIFESONG], AL_MIN_GAIN, 0);
					alSourcef(gSourceID[SHOOTSONG], AL_MIN_GAIN, 0);
					alSourcef(gSourceID[ZOMBIESONG], AL_MIN_GAIN, 0);
					alSourcef(gSourceID[MAINMENUSONG], AL_MIN_GAIN, 0);
					alSourcePlay(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
					
				}
	    		
			}
		}
	}
}
