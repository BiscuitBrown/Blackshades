package blackshades;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.*;
import static java.lang.Math.*;

import java.io.*;

import blackshades.Models.Model;
import blackshades.Person.HitStruct;
import blackshades.Support.Point;

import static blackshades.Constants.*;
import static blackshades.Quaternions.*;
import static blackshades.Person.*;
import static blackshades.Skeleton.*;
import static blackshades.Sprites.*;
import static blackshades.Globals.*;
import static blackshades.GameInitDispose.Game.*;
import static blackshades.Support.*;
import static blackshades.Support.random;

public class GameTick {
	
	public static class Game extends blackshades.Game {
    
	/********************> tick() <*****/

    public static final int MAX_FALL_VEL = 40;
	
		public static void splat(int k){

			if(k!=0||visions==0){

				person[k].health=0;
				person[k].doAnimations(k);
				person[k].skeleton.offset=0;
				person[k].skeleton.free=true;
				person[k].longdead=1;

				person[k].bleeding=1;
				person[k].bleeddelay=1;
				person[k].bjoint1=person[k].skeleton.joints[person[k].skeleton.jointlabels[HEAD]];
				person[k].bjoint2=person[k].skeleton.joints[person[k].skeleton.jointlabels[NECK]];

				for(int j=0;j<person[k].skeleton.num_joints;j++){

					person[k].skeleton.joints[j].position.incrementAdd(person[k].skeleton.joints[j].offset);
					person[k].skeleton.joints[j].position.set(doRotation(person[k].skeleton.joints[j].position,0,person[k].playerrotation,0));
					person[k].skeleton.joints[j].position.incrementAdd(person[k].playercoords);
					person[k].skeleton.joints[j].realoldposition.set(person[k].skeleton.joints[j].position);
					person[k].skeleton.joints[j].velocity.set(0);
					person[k].skeleton.joints[j].velocity.y+=person[k].velocity.getY();
				}

				float gLoc[] = new float[3];

				gLoc[0]=person[k].skeleton.joints[person[k].skeleton.jointlabels[HEAD]].position.getX()/soundscalefactor;
				gLoc[1]=person[k].skeleton.joints[person[k].skeleton.jointlabels[HEAD]].position.getY()/soundscalefactor;
				gLoc[2]=person[k].skeleton.joints[person[k].skeleton.jointlabels[HEAD]].position.getZ()/soundscalefactor;

				alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[HEADWHACKSOUND]);
			}	
		}
		
		public static XYZ collpoint;
		public static XYZ move;
		public static XYZ nothing;
		public static XYZ underpoint;
		public static XYZ overpoint;
		public static XYZ normalrotated;
		public static XYZ pointrotated;
		public static XYZ facing;
		public static XYZ flatfacing;


		public static void tick() throws IOException {
			
			if(mainmenu!=0){
				
					 
				if ( glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS )
					mainmenu=0;
				
				if(mouseloc == null)
					mouseloc = new Point();
				
				getMouse(mouseloc);
				
				float mousex=mouseloc.h;
				float mousey=mouseloc.v;

				mousex=mouseloc.h*640/screenwidth;
				mousey=480-mouseloc.v*480/screenheight;

				oldmouseoverbutton=mouseoverbutton;
				mouseoverbutton=0;

				if(mousex>120&&mousex<560&&mousey>235&&mousey<305){
					mouseoverbutton=1;
				}

				if(mousex>120&&mousex<560&&mousey>112&&mousey<182){
					mouseoverbutton=2;
				}

				if((button()&&mouseoverbutton==1&&!gameinprogress&&!oldbutton)||!mainmenuness){
					
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				    glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
					
					glfwSetCursorPos(window, screenwidth / 2, screenheight / 2);
					
					if(environment==RAINY_ENVIRONMENT)alSourcePlay(gSourceID[RAINSOUND]);
					if(environment!=RAINY_ENVIRONMENT)alSourcePause(gSourceID[RAINSOUND]);

					alSourceStop(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
					alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);

					if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
					if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
					if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;

					alSourcef(gSourceID[whichsong], AL_PITCH, 1);
					alSourcePlay(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
					alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);

					flashamount=1;
					flashr=1;flashg=1;flashb=1;
					mainmenu=0;
					
					alSourcePlay(gSourceID[SOULINSOUND]);

					mission=0;

					initGame();

					gameinprogress=true;
				}

				if((button()&&mouseoverbutton==1&&gameinprogress&&!oldbutton)||!mainmenuness){
					
					glfwSetInputMode(window,GLFW_CURSOR, GLFW_CURSOR_DISABLED);

					flashamount=1;
					flashr=1;flashg=1;flashb=1;
					mainmenu=0;
					
					if(oldmouseloc == null)
						oldmouseloc = new Point();
					
					moveMouse(oldmouseloc);

					//if(!visions){

						if(environment==RAINY_ENVIRONMENT)alSourcePlay(gSourceID[RAINSOUND]);
						if(environment!=RAINY_ENVIRONMENT)alSourcePause(gSourceID[RAINSOUND]);

						alSourceStop(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
						alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);

						if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
						if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
						if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;

						alSourcePlay(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
						alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);

					//}

					alSourcePlay(gSourceID[SOULINSOUND]);

					if(visions!=0)alSourcePlay(gSourceID[VISIONSOUND]);
					
					slomo = oldslomo;

				}
				

				if(button()&&mouseoverbutton==2&&!gameinprogress&&!oldbutton){

					flashamount=1;
					flashr=1;flashg=0;flashb=0;
					alSourcePlay(gSourceID[LOSESOUND]);
					gQuit = true;
					
					if(score>highscore){
						
						PrintStream fileStream = new PrintStream(new File("Highscore"));
						
						highscore=score;
						
						fileStream.println(highscore);
						fileStream.println(beatgame ? 1:0);
						fileStream.close();

					}
				}

				if(button()&&mouseoverbutton==2&&gameinprogress&&!oldbutton){

					flashamount=1;
					flashr=1;flashg=1;flashb=1;
					alSourcePlay(gSourceID[LOSESOUND]);
					gameinprogress=false;

					if(score>highscore){
						
						PrintStream fileStream = new PrintStream(new File("Highscore"));

						highscore=score;
						
						fileStream.println(highscore);
						fileStream.println(beatgame ? 1:0);
						fileStream.close();
					}
				}

				oldbutton=button();
			}

			if(mainmenu==0){

				if(facing == null) facing = new XYZ();
				facing.set(0);
				
				if(flatfacing == null) flatfacing = new XYZ();
				flatfacing.set(0);
					
				float speed=10;

				if(person[1].health<=0||person[0].health<=0||killedinnocent){
					losedelay-=multiplier/6;
				}

				if(person[1].health>0&&person[0].health>0&&!killedinnocent)timeremaining-=multiplier*25/40;

				if(timeremaining<=0){

					flashamount=1;
					flashr=0;flashg=1;flashb=0;
					mission++;
					alSourcePlay(gSourceID[SOULOUTSOUND]);
					score+=100+(mission*50);

					if(mission>=nummissions){

						beatgame=true;
						mainmenu=1;
						gameinprogress=false;
						alSourcePause(gSourceID[RAINSOUND]);

						alSourceStop(gSourceID[VISIONSOUND]);
						alSourceStop(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
						alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);
						whichsong=MAINMENUSONG;
						alSourceStop(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_PITCH, 1);
						alSourcePlay(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
						alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);
						
						if(!fullscreen)
							glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

						if(score>highscore){

							highscore=score;
							
							PrintStream fileStream = new PrintStream(new File("Highscore"));
							
							fileStream.println(highscore);
							fileStream.println(beatgame ? 1 : 0);
							
							fileStream.close();
						}
					}

					if(mainmenu==0){
						
						System.gc();

						initGame();

						if(environment==RAINY_ENVIRONMENT)alSourcePlay(gSourceID[RAINSOUND]);
						if(environment!=RAINY_ENVIRONMENT)alSourcePause(gSourceID[RAINSOUND]);

						alSourceStop(gSourceID[VISIONSOUND]);
						alSourceStop(gSourceID[whichsong]);

						if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
						if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
						if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;

						alSourcef(gSourceID[whichsong], AL_PITCH, 1);
						alSourcePlay(gSourceID[whichsong]);
						alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
						alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);

					}
				}

				if(losedelay<=0){

					flashamount=1;
					flashr=0;flashg=0;flashb=0;

					if(person[murderer].health>0)score=oldscore-200;
					if(person[murderer].health<=0)score=oldscore-100;

					System.gc();
					
					initGame();

					alSourceStop(gSourceID[whichsong]);
					
					if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
					if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
					if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;

					alSourcef(gSourceID[whichsong], AL_PITCH, 1);
					alSourcePlay(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
					alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);
					alSourcePlay(gSourceID[SOULINSOUND]);
				}

				//Sprites

				sprites.doStuff();
				
				//Decals

				decals.doStuff();

				//Facing

				facing.set(0);
				facing.setZ(-1);

				facing.set(doRotation(facing,-camera.rotation2,0,0));
				facing.set(doRotation(facing,0,0-camera.rotation,0));

				flatfacing.set(facing);
				flatfacing.setY(0);
				normalise(flatfacing);
									
				if ( glfwGetKey(window, psychickey) == GLFW_PRESS && !oldvisionkey ) {
					oldvisionkey=true;
					visions++;
					
					if(bodycoords == null) bodycoords = new XYZ();

					if(visions==2)visions=0;

					if(visions==0){

						flashamount=1;
						flashr=1;flashg=1;flashb=1;

						if(person[0].playercoords.getX()!=bodycoords.getX()||person[0].playercoords.getZ()!=bodycoords.getZ()){

							XYZ towards = new XYZ();

							towards.set(person[0].playercoords.subtract(bodycoords));

							normalise(towards);
							camera.rotation=- (float) asin(0f-towards.getX());
							camera.rotation*=360/6.28;

							if(towards.getZ()>0)camera.rotation=180-camera.rotation;
							
							camera.visrotation=camera.rotation;
							camera.oldrotation=camera.rotation;
							camera.oldoldrotation=camera.rotation;

						}

						person[0].playercoords.set(bodycoords); 
						person[0].oldplayercoords.set(bodycoords);
						person[0].velocity.set(0);

						alSourcePlay(gSourceID[SOULINSOUND]);
						alSourceStop(gSourceID[VISIONSOUND]);				

						alSourcef(gSourceID[KNIFESONG], AL_PITCH, 1);
						alSourcef(gSourceID[SHOOTSONG], AL_PITCH, 1);
						alSourcef(gSourceID[ZOMBIESONG], AL_PITCH, 1);
					}

						if(visions==1){

							alSourceStop(gSourceID[VISIONSOUND]);
							flashamount=1;
							flashr=1;flashg=0;flashb=0;
							bodycoords.set(person[0].oldplayercoords);
							alSourcePlay(gSourceID[SOULOUTSOUND]);
							alSourcef(gSourceID[KNIFESONG], AL_PITCH, .5f);
							alSourcef(gSourceID[SHOOTSONG], AL_PITCH,.5f);
							alSourcef(gSourceID[ZOMBIESONG], AL_PITCH,.5f);
							alSourcePlay(gSourceID[VISIONSOUND]);
						}
					}
					
					if ( glfwGetKey(window, psychickey) != GLFW_PRESS ) 
						oldvisionkey=false;
					
					
					if ( glfwGetKey(window, GLFW_KEY_TAB) == GLFW_PRESS && !tabkeydown&&debug) {
						thirdperson++;
						if(thirdperson>2)thirdperson=0;
						tabkeydown=true;
					}
					
					if ( glfwGetKey(window, GLFW_KEY_TAB) != GLFW_PRESS && debug) 
						tabkeydown=false;
					
					if ( glfwGetKey(window, aimkey) == GLFW_PRESS && !aimtoggle) {
						person[0].aiming=!person[0].aiming;
						aimtoggle=true;
					}
					
					if ( glfwGetKey(window, aimkey) != GLFW_PRESS)
						aimtoggle=false;
					 
					if ( glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS && !reloadtoggle) {
						if(person[0].reloads[person[0].whichgun]>0&&person[0].reloading<=0)person[0].ammo=-1;
						reloadtoggle=true;
					}
					
					if (glfwGetKey(window, GLFW_KEY_R) != GLFW_PRESS)
						reloadtoggle=false;
					
					if (glfwGetKey(window, psychicaimkey) == GLFW_PRESS && !slomokeydown&&slomo==0) {
						alSourcePlay(gSourceID[SOULOUTSOUND]);
						slomo=2;
						flashamount=.5f;
						flashr=1;flashg=0;flashb=0;
						slomokeydown=true;
						score-=20;
						alSourcef(gSourceID[KNIFESONG], AL_PITCH, .5f);
						alSourcef(gSourceID[SHOOTSONG], AL_PITCH, .5f);
						alSourcef(gSourceID[ZOMBIESONG], AL_PITCH, .5f);
					}
					
					if (glfwGetKey(window, psychicaimkey) != GLFW_PRESS)
						slomokeydown=false;								

				//Mouse look

				if((person[0].aimamount<=0&&person[0].targetanimation!=CROUCHANIM)){
					camera.rotation=camera.visrotation;
					camera.rotation2=camera.visrotation2;
					mousesensitivity=usermousesensitivity;
				}

				if(person[0].aimamount>=1&&!zoom){
					mousesensitivity=usermousesensitivity*.8f;
				}

				if(slomo==2){
					mousesensitivity*=.6f;
				}
				
				getMouseRel(mouseloc);

				mouserotation=(mouseloc.h/1.3888f)*mousesensitivity;
				mouserotation2=(mouseloc.v/1.3888f)*mousesensitivity;

				if(abs(mouseloc.h)<400)camera.rotation+=mouserotation;
				if(abs(mouseloc.v)<200)camera.rotation2+=mouserotation2;
				if(mouseloc.h>400)camera.rotation+=mouserotation-(500/1.3888*mousesensitivity);
				if(mouseloc.h<-400)camera.rotation+=mouserotation+(500/1.3888*mousesensitivity);

				if(mouseloc.v>200)camera.rotation2+=mouserotation2-(300/1.3888*mousesensitivity);
				if(mouseloc.v<-200)camera.rotation2+=mouserotation2+(300/1.3888*mousesensitivity);

				if(camera.rotation2>89){camera.rotation2=89;}
				if(camera.rotation2<-89){camera.rotation2=-89;}

				//Smooth

				camera.rotation=(camera.oldoldrotation+((camera.rotation-camera.oldoldrotation)*.7f+(camera.oldrotation-camera.oldoldrotation)*.3f));
				camera.rotation2=(camera.oldoldrotation2+((camera.rotation2-camera.oldoldrotation2)*.7f+(camera.oldrotation2-camera.oldoldrotation2)*.3f));

				if(camera.visrotation<camera.rotation-7)camera.visrotation=camera.rotation-7;
				if(camera.visrotation>camera.rotation+7)camera.visrotation=camera.rotation+7;
				if(camera.visrotation2<camera.rotation2-15)camera.visrotation2=camera.rotation2-15;
				if(camera.visrotation2>camera.rotation2+15)camera.visrotation2=camera.rotation2+15;

				if(zoom||person[0].aimamount<=0||person[0].whichgun==NOGUN||visions!=0||person[0].whichgun==GRENADE||person[0].whichgun==KNIFE){
					camera.visrotation=camera.rotation;
					camera.visrotation2=camera.rotation2;
				}

				oldzoom=zoom;

				camera.oldoldrotation=camera.oldrotation;
				camera.oldoldrotation2=camera.oldrotation2;
				camera.oldrotation=camera.rotation;
				camera.oldrotation2=camera.rotation2;
				
				//Check collision with buildings

				int beginx,endx;
				int beginz,endz;
				int distsquared;
				float howmuchintersect;
				int whichtri;
				float olddist;
				int pointnum;
				float[] depth = {0};
				boolean inblock;
				
				if(collpoint == null) collpoint = new XYZ();
				collpoint.set(0);
				
				if(move == null) move = new XYZ();
				move.set(0);
				
				if(nothing == null) nothing = new XYZ();
				nothing.set(0);
				
				if(underpoint == null) underpoint = new XYZ();
				underpoint.set(0);
				
				if(overpoint == null) overpoint = new XYZ();
				overpoint.set(0);
				
				if(normalrotated == null) normalrotated = new XYZ();
				normalrotated.set(0);
				
				if(pointrotated == null) pointrotated = new XYZ();
				pointrotated.set(0);
				
				person[0].playerrotation=(180-camera.rotation);

				//Check people collisions
				
				for(int k=0;k<numpeople;k++){
					// SBF - backing up the old coordinates makes
					// the most sense here.
					person[k].oldplayercoords.set(person[k].playercoords);
					person[k].doStuff(k);

					if(!person[k].skeleton.free){

					if(true){

					person[k].onground=false;
					overpoint.set(person[k].playercoords);
					overpoint.y+=3000;
					underpoint.set(person[k].playercoords);
					underpoint.y-=3000;

					beginx=(int) ((person[k].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING);
					if(beginx<0)beginx=0;
					beginz=(int) ((person[k].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING);
					if(beginz<0)beginz=0;

					endx=(int) ((person[k].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING+1);
					if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;
					endz=(int) ((person[k].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING+1);
					if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

					if(k!=0){
						/* TODO: huh? */
						beginx=person[k].whichblockx;
						beginz=person[k].whichblocky;
						endx=person[k].whichblockx;
						endz=person[k].whichblocky;
					}

					if(beginx<=endx&&beginz<=endz)
					for(int i=beginx;i<=endx;i++) {
						for(int j=beginz;j<=endz;j++){
							inblock=false;

							//Ground collision

							move.set(0);
							move.setX(i*BLOCK_SPACING);
							move.setZ(j*BLOCK_SPACING);
							whichtri=sidewalkcollide.lineCheck2(overpoint,underpoint,collpoint,move,cityrotation[i][j]*90);

							if(whichtri!=-1&&person[k].playercoords.getY()<=collpoint.getY()&&person[k].velocity.getY()<=0){
								person[k].playercoords.setY(collpoint.getY());
								person[k].onground=true;
								if(person[k].velocity.getY()<-MAX_FALL_VEL)splat(k);
								else person[k].velocity.setY(0);
							}

							if(whichtri!=-1){
								inblock=true;
								if(k==0){onblockx=i;onblocky=j;}
							}

							//Wall collision

							if(k==0){
								if(inblock){
									for(int l=0;l<8;l++){
										move.set(0);
										move.setX(i*BLOCK_SPACING);
										move.setZ(j*BLOCK_SPACING);
										whichtri=blockwalls[citytype[i][j]].lineCheck3(person[k].oldplayercoords.add(boundingpoints[l]),person[k].playercoords.add(boundingpoints[l]),collpoint,move,cityrotation[i][j]*90,depth);

										if(whichtri!=-1){
											normalrotated.set(doRotation(blockwalls[citytype[i][j]].normals[whichtri],0,cityrotation[i][j]*90,0));
											person[k].playercoords.incrementAdd(normalrotated.multiply(-(dotproduct(normalrotated,person[k].playercoords.subtract(person[k].oldplayercoords)))-depth[0]));
										}
									}

									for(int l=0;l<8;l++){
										pointnum=k+1;
										if(pointnum>3)pointnum=0;
										move.set(0);
										move.setX(i*BLOCK_SPACING);
										move.setZ(j*BLOCK_SPACING);
										whichtri=blockwalls[citytype[i][j]].lineCheck3(person[k].playercoords.add(boundingpoints[l]),person[k].playercoords.add(boundingpoints[pointnum]),collpoint,move,cityrotation[i][j]*90,depth);

										if(whichtri!=-1){
											normalrotated.set(doRotation(blockwalls[citytype[i][j]].normals[whichtri],0,cityrotation[i][j]*90,0));
											person[k].playercoords.incrementAdd(normalrotated);
										}
									}
								}

								//Roof collision

								if(inblock&&person[k].playercoords.getY()>30){

									if(!person[k].onground){
										move.set(0);
										move.setX(i*BLOCK_SPACING);
										move.setZ(j*BLOCK_SPACING);
										whichtri=blockroofs[citytype[i][j]].lineCheck2(overpoint,underpoint,collpoint,move,cityrotation[i][j]*90);

										if(whichtri!=-1&&person[k].playercoords.getY()<=collpoint.getY()&&person[k].velocity.getY()<=0){
											person[k].playercoords.setY(collpoint.getY());
											person[k].onground=true;

											if(person[k].velocity.getY()<-MAX_FALL_VEL)splat(k);
											else person[k].velocity.setY(0);

										}
										if(whichtri!=-1)inblock=true;
									}

								}
							}
						}		//#if 0	// DDOI
					}

					if(person[k].playercoords.getY()<=0){
						person[k].onground=true;
						person[k].playercoords.setY(0);

						if(person[k].velocity.getY()<-MAX_FALL_VEL)splat(k);
						else person[k].velocity.setY(0);
						if(k==0) {onblockx=-1;onblocky=-1;}

					}

					// SBF - this is definately in the wrong spot!
					//person[k].oldplayercoords=person[k].playercoords;

					}

					}

				}
				
					
				if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
					if(visions==0&&person[0].targetanimation==JOGANIM&&person[0].currentanimation==JOGANIM&&!person[0].backwardsanim&&person[0].playerrotation==person[0].playerlowrotation){
						person[0].targetanimation=DIVEANIM;
						person[0].targetframe=0;
						person[0].target=0;
						person[0].aimamount=0;
					}
				}
				

				//Camera

				camera.oldposition.set(camera.position);
				camera.targetoffset.set(0);
				camera.targetoffset.setZ(-5);

				//Spawn people

				spawndelay-=multiplier;
				int blockspawnx = 0,blockspawny = 0;
				int cyclenum = 0;
				
				

				while((citypeoplenum[blockspawnx][blockspawny]>=MAX_PEOPLE_BLOCK&&cyclenum<10)||blockspawnx==0){

					blockspawnx=(int) (((person[0].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
					blockspawny=(int) (((person[0].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
					cyclenum++;
				}

				if(cyclenum<10){	

				if(spawndelay<0&&numpeople<MAX_PEOPLE){

					if(type==RANDOMSHOOT_TYPE){

						if(abs(random() %evilprobability)==0)person[numpeople].type=EVILTYPE;
						else person[numpeople].type=CIVILIANTYPE;
					}

					if(type==ZOMBIE_TYPE){
						person[numpeople].type=ZOMBIETYPE;
					}

				

					if(person[numpeople].type!=CIVILIANTYPE&&blockspawnx==person[1].whichblockx&&blockspawny==person[1].whichblocky){

						while((citypeoplenum[blockspawnx][blockspawny]>=MAX_PEOPLE_BLOCK&&cyclenum<10)||blockspawnx==0||(blockspawnx==person[1].whichblockx&&blockspawny==person[1].whichblocky)){

							blockspawnx=(int) (((person[0].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
							blockspawny=(int) (((person[0].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
							cyclenum++;
						}
					}

					person[numpeople].playerrotation=0;
					person[numpeople].whichcostume=CASUALCOSTUMES+abs(random())%NUMCASUAL;

					person[numpeople].whichblockx=blockspawnx;
					person[numpeople].whichblocky=blockspawny;

					person[numpeople].pathnum=-1;
					person[numpeople].oldpathnum=-1;
					person[numpeople].oldoldpathnum=-1;
					person[numpeople].oldoldoldpathnum=-1;

					while(person[numpeople].pathnum<0||person[numpeople].pathnum>=path.vertexNum||person[numpeople].pathnum==1){
						person[numpeople].pathnum=random()%path.vertexNum;
					}

					person[numpeople].pathtarget.setX(path.vertex[person[numpeople].pathnum].getX());
					person[numpeople].pathtarget.setZ(path.vertex[person[numpeople].pathnum].getZ());			
					person[numpeople].pathsize=(float) (.98d+(abs(random()%20))/400);
					person[numpeople].pathtarget.incrementMultiply(person[numpeople].pathsize);
					person[numpeople].pathtarget.x+=person[numpeople].whichblockx*BLOCK_SPACING;
					person[numpeople].pathtarget.z+=person[numpeople].whichblocky*BLOCK_SPACING;
					
					person[numpeople].playercoords.set(person[numpeople].pathtarget);
					person[numpeople].oldplayercoords.set(person[numpeople].playercoords);
					person[numpeople].skeleton.free=false;

					person[numpeople].targetanimation=WALKANIM;

					if(person[numpeople].type==ZOMBIETYPE)person[numpeople].targetanimation=ZOMBIEWALKANIM;

					person[numpeople].speed=1;
					person[numpeople].existing=false;
					person[numpeople].speedmult=.8f+ (abs(random()%20))/50;
					person[numpeople].health=100;	
					person[numpeople].maxhealth=100;	
					person[numpeople].playerrotation2=0;

					person[numpeople].lastdistancevictim=200000;					

					if(person[numpeople].skeleton.broken!=0) person[numpeople].skeleton.load("Data/Skeleton/Basic Figure");
					
					if(numpeople==1)person[numpeople].type=VIPTYPE;

					person[numpeople].killtarget=-1;

					if(person[numpeople].type==EVILTYPE){person[numpeople].existing=true; person[numpeople].pathsize=1.04f; person[numpeople].whichgun=NOGUN; person[numpeople].aiming=true; person[numpeople].killtarget=-1; person[numpeople].speedmult=1+.3f*difficulty;}
					if(person[numpeople].type==ZOMBIETYPE){person[numpeople].existing=true; person[numpeople].pathsize=1.04f; person[numpeople].whichgun=NOGUN; person[numpeople].aiming=false; person[numpeople].killtarget=-1; person[numpeople].speedmult=0.7f+.2f*difficulty;}
					else {person[numpeople].whichgun=NOGUN; person[numpeople].aiming=false; person[numpeople].killtarget=-1;}
					
					if(person[numpeople].type==VIPTYPE){person[numpeople].existing=true;}
					if(enemystate==2)person[numpeople].killtarget=1;

					numpeople++;
					citypeoplenum[blockspawnx][blockspawny]++;
					spawndelay=.1f;

				}

				if(spawndelay<0&&numpeople>=MAX_PEOPLE){

					if(cycle>=MAX_PEOPLE)cycle=0;
					
					cyclenum=0;

					while(cyclenum<MAX_PEOPLE&&(person[cycle].type==PLAYERTYPE||person[cycle].type==VIPTYPE||(abs(person[cycle].whichblockx-((person[0].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING))<2&&abs(person[cycle].whichblocky-((person[0].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING))<2))){
						cycle++;
						cyclenum++;
						if(cycle>=MAX_PEOPLE)cycle=0;
					}

					if(cycle<MAX_PEOPLE&&cyclenum<MAX_PEOPLE){

						if(type==RANDOMSHOOT_TYPE){
							if(abs(random()%evilprobability)==0)person[cycle].type=EVILTYPE;
							else person[cycle].type=CIVILIANTYPE;
						}

						if(type==ZOMBIE_TYPE){
							person[cycle].type=ZOMBIETYPE;
						}

						if(person[cycle].type!=CIVILIANTYPE&&blockspawnx==person[1].whichblockx&&blockspawny==person[1].whichblocky){

							while((citypeoplenum[blockspawnx][blockspawny]>=MAX_PEOPLE_BLOCK&&cyclenum<10)||blockspawnx==0||(blockspawnx==person[1].whichblockx&&blockspawny==person[1].whichblocky)){
								blockspawnx=(int) (((person[0].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
								blockspawny=(int) (((person[0].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING)+random()%2);
								cyclenum++;
								
							}
						}

						person[cycle].playerrotation=0;

						person[cycle].whichcostume=CASUALCOSTUMES+abs(random())%NUMCASUAL;

						citypeoplenum[person[cycle].whichblockx][person[cycle].whichblocky]--;
						person[cycle].whichblockx=blockspawnx;
						person[cycle].whichblocky=blockspawny;

						person[cycle].pathnum=-1;
						person[cycle].oldpathnum=-1;
						person[cycle].oldoldpathnum=-1;
						person[cycle].oldoldoldpathnum=-1;

						while(person[cycle].pathnum<0||person[cycle].pathnum>=path.vertexNum||person[cycle].pathnum==1){
							person[cycle].pathnum=random()%path.vertexNum;
						}

						person[cycle].pathtarget.setX(path.vertex[person[cycle].pathnum].getX());
						person[cycle].pathtarget.setZ(path.vertex[person[cycle].pathnum].getZ());			
						person[cycle].pathsize=.98f + (float) (abs(random()%20))/400;
						person[cycle].pathtarget.incrementMultiply(person[cycle].pathsize);
						person[cycle].pathtarget.x+=person[cycle].whichblockx*BLOCK_SPACING;
						person[cycle].pathtarget.z+=person[cycle].whichblocky*BLOCK_SPACING;
						
						person[cycle].playercoords.set(person[cycle].pathtarget);
						person[cycle].oldplayercoords.set(person[cycle].playercoords);
						person[cycle].skeleton.free=false;

						person[cycle].targetanimation=WALKANIM;
						if(person[cycle].type==ZOMBIETYPE)person[cycle].targetanimation=ZOMBIEWALKANIM;
						person[cycle].speed=1;
						person[cycle].existing=false;
						person[cycle].speedmult=.8f + (abs(random()%20))/50;
						person[cycle].health=100;
						person[cycle].maxhealth=100;
						person[cycle].playerrotation2=0;
						
						if(person[cycle].skeleton.broken!=0)person[cycle].skeleton.load("Data/Skeleton/Basic Figure");
						
						if(enemystate==1)person[cycle].killtarget=-1;
						
						if(person[cycle].type==EVILTYPE){person[cycle].existing=true;  person[cycle].pathsize=1.04f; person[cycle].whichgun=NOGUN; person[cycle].aiming=true; person[cycle].killtarget=-1; person[cycle].speedmult=1+.3f*difficulty;}
						
						if(person[cycle].type==ZOMBIETYPE){person[cycle].existing=true; person[cycle].pathsize=1.04f; person[cycle].whichgun=NOGUN; person[cycle].aiming=false; person[cycle].killtarget=-1; person[cycle].speedmult=.7f+.2f*difficulty;}
						
						else {person[cycle].whichgun=NOGUN; person[cycle].aiming=false; person[cycle].killtarget=-1;}

						person[cycle].lastdistancevictim=200000;

						if(enemystate==2)person[cycle].killtarget=1;

						if(numpeople<MAX_PEOPLE)numpeople++;
						citypeoplenum[blockspawnx][blockspawny]++;
						cycle++;

						spawndelay=.1f;
					}
				}}

				XYZ towards = new XYZ();
				XYZ finaltarget = new XYZ();
				XYZ blah = new XYZ();
				int closesttarget = 0;
				float leastdistance = 0.0f;

				XYZ bleedloc = new XYZ();
				XYZ vel = new XYZ();

				float tooclose;
				float toofar;

				//People

				for(int i=0;i<numpeople;i++){

					if(person[i].health<0&&person[i].longdead<0&&!person[i].firstlongdead){
						if(person[i].type!=ZOMBIETYPE){
							overpoint.set(person[i].skeleton.joints[person[i].skeleton.jointlabels[ABDOMEN]].position);
							overpoint.y+=3000;
							underpoint.set(person[i].skeleton.joints[person[i].skeleton.jointlabels[ABDOMEN]].position);
							underpoint.y-=3000;
							move.set(0);
							move.setX(person[i].whichblockx*BLOCK_SPACING);
							move.setZ(person[i].whichblocky*BLOCK_SPACING);
							
							XYZ temp = new XYZ();
							whichtri=sidewalkcollide.lineCheck2(overpoint,underpoint,temp,move,cityrotation[person[i].whichblockx][person[i].whichblocky]*90);

							XYZ normish = new XYZ();

							normish.setY(1);

							if(whichtri>=0){
								decals.makeDecal(BLOODPOOL,temp,12,normish, whichtri, sidewalkcollide, move, cityrotation[person[i].whichblockx][person[i].whichblocky]*90);
							}

							if(whichtri==-1){
								temp.set(person[i].skeleton.joints[person[i].skeleton.jointlabels[ABDOMEN]].position);
								temp.setY(-.5f);
								move.set(0);
								decals.makeDecal(BLOODPOOL,temp,12,normish, 0, sidewalkcollide, move, 0);
							}

							person[i].firstlongdead=true;
						}
					}

					if(person[i].health<100&&person[i].type!=ZOMBIETYPE){
					 		person[i].health-=multiplier*120;
					}

					if(person[i].health<100&&person[i].type==ZOMBIETYPE){
					 		person[i].health+=multiplier*10;
					 		if(person[i].health>person[i].maxhealth)person[i].health=person[i].maxhealth;
					}

					if(person[i].health<100&&person[i].type==ZOMBIETYPE&&person[i].skeleton.free){
					 		person[i].health+=multiplier*10;
					 		if(person[i].health>person[i].maxhealth)person[i].health=person[i].maxhealth;
					}

					if(person[i].health<40&&person[i].type==ZOMBIETYPE){
					 		person[i].speedmult-=multiplier/20;

					 		if(person[i].speedmult<.6f){
								person[i].speedmult=.6f;
								person[i].killtarget=-1;
							}

					}

					if(person[i].health>=40&&person[i].type==ZOMBIETYPE){
					 		person[i].speedmult+=multiplier/40;

					 		if(person[i].speedmult>.7f+difficulty*.2f){
								person[i].speedmult=.7f+difficulty*.2f;
								person[i].killtarget=1;

							}
					}

					

					if(person[i].maxhealth<100&&person[i].type==ZOMBIETYPE){
					 		person[i].maxhealth+=multiplier*2;
					 		if(person[i].maxhealth>100)person[i].maxhealth=100;
					}

					if(person[i].bleeding>0){
						person[i].bleeding-=multiplier;
						person[i].bleeddelay-=multiplier*10;
						
						if(person[i].bleeddelay<=0){

							person[i].bleeddelay=1;

							if(!person[i].skeleton.free){

								bleedloc.set(doRotation((person[i].bjoint1.position.add(person[i].bjoint2.position)).devide(2),0,person[i].playerrotation,0).add(person[i].playercoords));

							}

							if(person[i].skeleton.free){

								bleedloc.set(person[i].bjoint1.position.add(person[i].bjoint2.position).devide(2));

							}

							vel.set(0);

							sprites.makeSprite(BLOOD_SPRITE_DOWN, .6f, 1, .2f, .2f,bleedloc, vel, 3*person[i].bleeding);

						}
					}

					if(!person[i].skeleton.free){

						//Gun

						if(person[i].type==PLAYERTYPE||person[i].type==EVILTYPE){

							if(i==0){

								if(person[i].whichgun==SHOTGUN)person[i].recoil-=multiplier*4;
							 	if(person[i].whichgun==SNIPERRIFLE)person[i].recoil-=multiplier*2;
							 	if(person[i].whichgun==HANDGUN1)person[i].recoil-=multiplier*5;
							 	if(person[i].whichgun==HANDGUN2)person[i].recoil-=multiplier*5;
							 	if(person[i].whichgun==ASSAULTRIFLE)person[i].recoil-=multiplier*10;
						 	}

						 	if(i!=0){

								if(person[i].whichgun==SHOTGUN)person[i].recoil-=multiplier*1;
							 	if(person[i].whichgun==SNIPERRIFLE)person[i].recoil-=multiplier*1;
							 	if(person[i].whichgun==HANDGUN1)person[i].recoil-=multiplier*2;
							 	if(person[i].whichgun==HANDGUN2)person[i].recoil-=multiplier*2;
							 	if(person[i].whichgun==ASSAULTRIFLE)person[i].recoil-=multiplier*10;
						 	}

						 	if(person[i].recoil<0)person[i].recoil=0;

							if(i==0){

								if(zoom){

									mousesensitivity=.05f*usermousesensitivity;
									if(person[i].targetanimation!=CROUCHANIM||person[i].currentanimation!=CROUCHANIM||!person[i].aiming){
										zoom=false;
									}

									if(visions==1)zoom=false;
								}

								if(person[i].currentanimation==CROUCHANIM&&person[i].targetanimation==CROUCHANIM&&person[i].aiming&&person[i].whichgun==SNIPERRIFLE){
									zoom=true;
									if(zoom&&!oldzoom)camera.rotation2-=6;
								}

								if(!zoom)mousesensitivity=1*usermousesensitivity;
								if(person[i].whichgun!=SNIPERRIFLE)zoom=false;
							}
						}

						//Zombie eat

						if(person[i].killtarget> -1&&i>0&&person[person[i].killtarget].eaten==i&&person[i].targetanimation==ZOMBIEEATANIM){

					 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[HEAD]].locked=true;
					 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[RIGHTSHOULDER]].locked=true;

					 		for(int k=0;k<2;k++){

					 			person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[HEAD]].position.set(doRotation(person[i].skeleton.joints[person[i].skeleton.jointlabels[RIGHTHAND]].position,0,person[i].playerrotation,0).add(person[i].playercoords));
						 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[HEAD]].velocity.set(0);
						 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[RIGHTSHOULDER]].position.set(doRotation(person[i].skeleton.joints[person[i].skeleton.jointlabels[LEFTHAND]].position,0,person[i].playerrotation,0).add(person[i].playercoords));
						 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[RIGHTSHOULDER]].velocity.set(0);
						 		person[person[i].killtarget].skeleton.doConstraints();
								person[person[i].killtarget].skeleton.doConstraints(blocksimplecollide[citytype[person[i].whichblockx][person[i].whichblocky]],move,cityrotation[person[i].whichblockx][person[i].whichblocky]*90);

							}

							person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[HEAD]].locked=false;
					 		person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[RIGHTSHOULDER]].locked=false;

					 		person[person[i].killtarget].longdead=1;
					 	}

						if(i>0&&enemystate!=1&&person[i].type==ZOMBIETYPE&&person[i].speedmult>.7){

							if(findDistancefast(person[i].playercoords,person[1].playercoords)<20000)person[i].killtarget=1;
							else person[i].killtarget=-1;
						}

						if(i>0&&enemystate!=1&&person[i].type==ZOMBIETYPE&&person[i].speedmult<.7){
							
							person[i].killtarget=-1;
						}

						boolean realcheck = false;

						//Pathfinding
					 	if(i>0&&person[i].targetanimation!=GETUPFRONTANIM&&person[i].targetanimation!=THROWNANIM&&person[i].targetanimation!=GETUPBACKANIM&&person[i].currentanimation!=GETUPFRONTANIM&&person[i].currentanimation!=GETUPBACKANIM){

					 		person[i].pathcheckdelay-=multiplier;

							// Realcheck tells us
							// a) we've got close to the end of our path or
							// b) we're moving away from our target
					 		realcheck=(abs(person[i].playercoords.getX()-person[i].pathtarget.getX())<1&&abs(person[i].playercoords.getZ()-person[i].pathtarget.getZ())<1)
							  ||findDistancefast(person[i].playercoords,person[i].pathtarget)>findDistancefast(person[i].oldplayercoords,person[i].pathtarget);
					 		
							if(person[i].targetanimation==IDLEANIM&&!person[i].killtargetvisible){
								person[i].targetanimation=WALKANIM;

								if(person[i].type==ZOMBIETYPE)person[i].targetanimation=ZOMBIEWALKANIM;
								realcheck=true;
							}
							
							if((realcheck||((person[i].killtarget>-1&&person[i].type!=CIVILIANTYPE)&&person[i].pathcheckdelay<=0)||person[i].killtargetvisible)){
								
						 		person[i].pathcheckdelay=1.2f;
						 		
						 		if((person[i].killtarget==-1||person[i].type==CIVILIANTYPE)&&!person[i].running){

							 		person[i].killtargetvisible=false;
							 		leastdistance=2000000;

							 		for(int j=0;j<path.vertexNum;j++){

							 			person[i].pathtarget.setX(path.vertex[j].getX());
										person[i].pathtarget.setZ(path.vertex[j].getZ());		
										person[i].pathtarget.setY(path.vertex[j].getY());		
										person[i].pathtarget.incrementMultiply(person[i].pathsize);
										person[i].pathtarget.x+=person[i].whichblockx*BLOCK_SPACING;
										person[i].pathtarget.z+=person[i].whichblocky*BLOCK_SPACING;

										if(findDistancefast(person[i].playercoords,person[i].pathtarget)<leastdistance&&j!=1&&j!=person[i].oldpathnum&&j!=person[i].oldoldpathnum&&j!=person[i].oldoldoldpathnum){
											leastdistance=findDistancefast(person[i].playercoords,person[i].pathtarget);
											closesttarget=j;
										}	
							 		}

							 		if(closesttarget>=0&&closesttarget<path.vertexNum){

								 		person[i].oldoldoldpathnum=person[i].oldoldpathnum;
								 		person[i].oldoldpathnum=person[i].oldpathnum;
								 		person[i].oldpathnum=person[i].pathnum;
								 		person[i].pathnum=closesttarget;
										person[i].pathtarget.setX(path.vertex[person[i].pathnum].getX());
										person[i].pathtarget.setZ(path.vertex[person[i].pathnum].getZ());		
										person[i].pathtarget.incrementMultiply(person[i].pathsize);
										person[i].pathtarget.x+=person[i].whichblockx*BLOCK_SPACING;
										person[i].pathtarget.z+=person[i].whichblocky*BLOCK_SPACING;
									}

								}

								if(person[i].running&&realcheck){

									person[i].killtargetvisible=false;
									person[i].targetanimation=JOGANIM;

									//Dead target?

									if(person[person[i].killtarget].health<=0){
									 	person[i].running=false;
									 }
									 person[i].killtarget=1;

									 //If pathfind

									if(realcheck){
							 		leastdistance=2000000;
							 		person[i].lastdistancevictim=0;
							 		closesttarget=-1;

							 		//Check best path

							 		closesttarget=person[i].pathnum;

							 		//Check other blocks?

							 		if ((closesttarget==person[i].pathnum)){
							 			beginx=person[i].whichblockx-2;
										if(beginx<0)beginx=0;
										beginz=person[i].whichblocky-2;
										if(beginz<0)beginz=0;

										endx=person[i].whichblockx+2;
										if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;
										endz=person[i].whichblocky+2;
										if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;
										
										leastdistance=2000000;

							 			for(int l=beginx;l<=endx;l++){

											for(int m=beginx;m<=endx;m++){

										 		for(int j=0;j<path.vertexNum;j++){

										 			person[i].pathtarget.setX(path.vertex[j].getX());
													person[i].pathtarget.setY(path.vertex[j].getY());
													person[i].pathtarget.setZ(path.vertex[j].getZ());		
													person[i].pathtarget.incrementMultiply(person[i].pathsize);
													person[i].pathtarget.x+=l*BLOCK_SPACING;
													person[i].pathtarget.z+=m*BLOCK_SPACING;

													if(findDistancefast(person[i].playercoords,person[i].pathtarget)<leastdistance&&findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords)>findDistancefast(person[i].playercoords,person[person[i].killtarget].playercoords)&&j!=1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[person[i].whichblockx][person[i].whichblocky])==-1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[l][m])==-1){

														person[i].lastdistancevictim=findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords);
														leastdistance=findDistancefast(person[i].playercoords,person[i].pathtarget);
														closesttarget=j;
														finaltarget.set(person[i].pathtarget);
														person[i].whichblockx=l;
														person[i].whichblocky=m;
							 						}
										 		}
										 		}
										 	}
										 }

							  		if(closesttarget!=-1){

										person[i].pathnum=closesttarget;
										person[i].pathtarget.set(finaltarget);
									}
									}
								}

								//Assassin
								
								if((person[i].killtarget>-1&&person[i].type!=CIVILIANTYPE)&&!person[i].running){

									//Dead target?
									
									if(person[person[i].killtarget].health<=0&&person[i].type==EVILTYPE){

									 	person[i].playerrotation2=0;
									 	person[i].whichgun=NOGUN;
									 	person[i].targetanimation=WALKANIM;
									 	person[i].lastdistancevictim=200000;
									 	person[i].pathnum=-1;
									 	enemystate=1;
									 	person[i].killtarget=-1;
									 	realcheck=true;
									 }

									 if(person[i].type==ZOMBIETYPE&&person[person[i].killtarget].health<=0){

										if(person[person[i].killtarget].eaten!=i){

											person[i].playerrotation2=0;
									 		person[i].targetanimation=ZOMBIEWALKANIM;
									 		person[i].lastdistancevictim=200000;
									 		person[i].pathnum=-1;
									 		realcheck=true;
									 		person[i].killtarget=-1;
									 	}

									 	if(person[i].killtarget > -1 && person[person[i].killtarget].eaten==i&&person[i].targetanimation!=ZOMBIEEATANIM){

									 		person[i].targetanimation=ZOMBIEEATANIM;
											person[i].targetframe=0;
											person[i].target=0;
									 	}
									 	enemystate=1;
									 }

									 if(person[i].killtarget >-1 && person[person[i].killtarget].health>0){

									 if(person[person[i].killtarget].skeleton.free){
									 	person[person[i].killtarget].playercoords.set(person[person[i].killtarget].averageloc);
									 }

									 //If pathfind

									if(realcheck){

							 		leastdistance=2000000;
							 		person[i].lastdistancevictim=2000000;
							 		closesttarget=-1;

							 		//Check best path

							 		for(int j=0;j<path.vertexNum;j++){

							 			person[i].pathtarget.setX(path.vertex[j].getX());
										person[i].pathtarget.setZ(path.vertex[j].getZ());		
										person[i].pathtarget.setY(path.vertex[j].getY());		
										person[i].pathtarget.incrementMultiply(person[i].pathsize);

										person[i].pathtarget.x+=person[i].whichblockx*BLOCK_SPACING;
										person[i].pathtarget.z+=person[i].whichblocky*BLOCK_SPACING;

										if(findDistancefast(person[i].playercoords,person[i].pathtarget)<leastdistance&&findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords)<person[i].lastdistancevictim&&j!=1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[person[i].whichblockx][person[i].whichblocky])==-1){

											leastdistance=findDistancefast(person[i].playercoords,person[i].pathtarget);
											person[i].lastdistancevictim=findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords);
											closesttarget=j;
											finaltarget.set(person[i].pathtarget);
										}	
							 		}

							 		leastdistance=2000000;

							 		for(int j=0;j<path.vertexNum;j++){

							 			person[i].pathtarget.setX(path.vertex[j].getX());
										person[i].pathtarget.setZ(path.vertex[j].getZ());		
										person[i].pathtarget.setY(path.vertex[j].getY());		
										person[i].pathtarget.incrementMultiply(person[i].pathsize);
										
										person[i].pathtarget.x+=person[i].whichblockx*BLOCK_SPACING;
										person[i].pathtarget.z+=person[i].whichblocky*BLOCK_SPACING;

										if(findDistancefast(person[i].playercoords,person[i].pathtarget)<leastdistance&&findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords)<person[i].lastdistancevictim&&j!=1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[person[i].whichblockx][person[i].whichblocky])==-1){

											leastdistance=findDistancefast(person[i].playercoords,person[i].pathtarget);
											person[i].lastdistancevictim=findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords);
											closesttarget=j;
											finaltarget.set(person[i].pathtarget);
										}	
							 		}

							 		//Check other blocks?

							 		if((closesttarget==person[i].pathnum)||closesttarget==-1){

							 			beginx=person[i].whichblockx-2;

										if(beginx<0)beginx=0;

										beginz=person[i].whichblocky-2;

										if(beginz<0)beginz=0;

										

										endx=person[i].whichblockx+2;

										if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;

										endz=person[i].whichblocky+2;

										if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

										

										leastdistance=2000000;

							 			for(int l=beginx;l<=endx;l++){

											for(int m=beginx;m<=endx;m++){

										 		if(l!=person[i].whichblockx||m!=person[i].whichblocky){

										 		for(int j=0;j<path.vertexNum;j++){

										 			person[i].pathtarget.setX(path.vertex[j].getX());
													person[i].pathtarget.setY(path.vertex[j].getY());
													person[i].pathtarget.setZ(path.vertex[j].getZ());		
													person[i].pathtarget.incrementMultiply(person[i].pathsize);
													person[i].pathtarget.x+=l*BLOCK_SPACING;
													person[i].pathtarget.z+=m*BLOCK_SPACING;

													if(findDistancefast(person[i].playercoords,person[i].pathtarget)<leastdistance&&findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords)<findDistancefast(person[i].playercoords,person[person[i].killtarget].playercoords)&&j!=1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[l][m])==-1&&blocksimple.lineCheck2(person[i].playercoords,person[i].pathtarget,blah,move,cityrotation[person[i].whichblockx][person[i].whichblocky])==-1){

														leastdistance=findDistancefast(person[i].playercoords,person[i].pathtarget);
														closesttarget=j;
														finaltarget.set(person[i].pathtarget);
														person[i].whichblockx=l;
														person[i].whichblocky=m;

							 						}

										 		}

										 		}

										 	}

										 }

							 		}

							 		if(closesttarget!=-1){

							 			person[i].onpath=true;
										person[i].pathnum=closesttarget;
										person[i].pathtarget.set(finaltarget);
									}

									}

									//Check killtargetvisible

									person[i].killtargetvisible=true;

									if(person[person[i].killtarget].health<=0)person[i].killtargetvisible=false;
									if(closesttarget!=-1&&findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)>30000)person[i].killtargetvisible=false;
							 		if(person[i].killtarget==0&&visions==1)person[i].killtargetvisible=false;

							 		if(person[i].killtargetvisible){

								 		beginx=person[i].whichblockx-2;
										if(beginx<0)beginx=0;
										beginz=person[i].whichblocky-2;
										if(beginz<0)beginz=0;

										endx=person[i].whichblockx+2;
										if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;
										endz=person[i].whichblocky+2;
										if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

								 		for(int l=beginx;l<=endx;l++){

											for(int m=beginx;m<=endx;m++){

										 		move.setX(l*BLOCK_SPACING);
												move.setZ(m*BLOCK_SPACING);
												move.setY(-3);

												if(person[i].killtargetvisible){
											 		if(blocksimple.lineCheck2(person[i].playercoords,person[person[i].killtarget].playercoords,blah,move,cityrotation[l][m])!=-1){
						 								person[i].killtargetvisible=false;
						 							}
						 						}
										 	}
										 }

										}

										if(person[i].type==EVILTYPE){

											 if(!person[i].killtargetvisible&&person[i].targetanimation==IDLEANIM){
											 	person[i].targetanimation=JOGANIM;
											 }

											 if(!person[i].killtargetvisible){
											 	person[i].aiming=false;
											 }

											 if(person[i].killtargetvisible){

											 	person[i].onpath=false;
											 	person[i].lastdistancevictim=200000;
											 	person[i].pathnum=-1;

											 	if(person[i].whichgun==NOGUN){
											 		person[i].whichgun=possiblegun[abs(random.nextInt()%numpossibleguns)];
											 		person[i].reloads[person[i].whichgun]=1;
											 		if(person[i].whichgun==KNIFE)person[i].speedmult=.8f+.5f*difficulty;

											 	}

											 	if(!person[i].aiming)person[i].shotdelay=SHOTDELAYAMOUNT/difficulty;

											 	person[i].aiming=true;

											 	if(person[i].reloading>0)person[i].aiming=false;
											 	if(person[i].whichgun==HANDGUN1||person[i].whichgun==HANDGUN2)person[i].playerrotation2=-10;
											 	if(person[i].whichgun==ASSAULTRIFLE||person[i].whichgun==SNIPERRIFLE||person[i].whichgun==SHOTGUN)person[i].playerrotation2=20;

											 	tooclose=1300;
											 	toofar=3000;

											 	if(person[i].whichgun==SHOTGUN){
											 		tooclose=1400;
											 		toofar=5000;
											 	}

											 	if(person[i].whichgun==ASSAULTRIFLE){
											 		tooclose=5000;
											 		toofar=9000;
											 	}

											 	if(person[i].whichgun==SNIPERRIFLE){
											 		tooclose=10000;
											 		toofar=20000;
											 	}

											 	if(person[i].whichgun==KNIFE){
											 		tooclose=20;
											 		toofar=20000;
											 	}

											 	if(findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)>toofar)
												 	person[i].targetanimation=JOGANIM;

											 	if((findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)<=tooclose&&!person[person[i].killtarget].skeleton.free)||(tooclose>200&&findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)<=200)||(tooclose<=200&&findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)<tooclose)){

											 		if(person[i].targetanimation!=IDLEANIM){
											 			
												 		person[i].targetanimation=IDLEANIM;
														person[i].targetframe=0;
														person[i].target=0;
													}

													if(person[i].whichgun==KNIFE&&person[person[i].killtarget].health==100){

														murderer=i;
														person[i].attacktarget=0;
														person[i].attackframe=0;

														float gLoc[] = new float[3];

														gLoc[0]=(camera.position.getX()+((person[i].playercoords.getX()+flatfacing.getX())-camera.position.getX())/2)/soundscalefactor;
														gLoc[1]=(camera.position.getY()+((person[i].playercoords.getY()+flatfacing.getY())-camera.position.getY())/2)/soundscalefactor;
														gLoc[2]=(camera.position.getZ()+((person[i].playercoords.getZ()+flatfacing.getZ())-camera.position.getZ())/2)/soundscalefactor;

														if(person[person[i].killtarget].type!=ZOMBIETYPE)

														{

															alSourcefv(gSourceID[KNIFESLASHSOUND], AL_POSITION, gLoc);
															alSourcePlay(gSourceID[KNIFESLASHSOUND]);

															person[person[i].killtarget].bjoint1=person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[NECK]];
															person[person[i].killtarget].bjoint2=person[person[i].killtarget].skeleton.joints[person[person[i].killtarget].skeleton.jointlabels[NECK]];
															person[person[i].killtarget].bleeding=1;
															person[person[i].killtarget].bleeddelay=1;
															person[person[i].killtarget].health-=20;
															person[person[i].killtarget].targetanimation=CHESTPAINANIM;
															person[person[i].killtarget].targetframe=0;
															person[person[i].killtarget].target=0;
															person[person[i].killtarget].longdead=1;
														}
													}
												}

											 	finaltarget.set(person[person[i].killtarget].playercoords);

											 }

										}

										if(person[i].type==ZOMBIETYPE&&person[person[i].killtarget].health>0){

											 if(!person[i].killtargetvisible&&person[i].targetanimation==IDLEANIM){
											 	person[i].targetanimation=ZOMBIEJOGANIM;
											 }

											 if(!person[i].killtargetvisible){
											 	person[i].aiming=false;
											 }

											 if(person[i].killtargetvisible){

											 	person[i].onpath=false;
											 	person[i].lastdistancevictim=200000;
											 	person[i].pathnum=-1;

											 	if(!person[i].aiming)person[i].shotdelay=SHOTDELAYAMOUNT/difficulty;
											 	if(findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)>20||person[i].targetanimation!=IDLEANIM)
												 	person[i].targetanimation=ZOMBIEJOGANIM;

											 	if(findDistancefast(person[i].playercoords, person[person[i].killtarget].playercoords)<=20){

											 		murderer=i;
											 		person[person[i].killtarget].health=0;
											 		person[person[i].killtarget].eaten=i;
												}

											 	finaltarget.set(person[person[i].killtarget].playercoords);

											 }
										}

									 if(person[i].killtargetvisible||realcheck)person[i].pathtarget.set(finaltarget);
									 if(realcheck)person[i].lastdistancevictim=findDistancefast(person[i].pathtarget,person[person[i].killtarget].playercoords);	 

									}
								}								

								if(person[i].targetanimation!=ZOMBIEEATANIM||person[i].type!=ZOMBIETYPE){

									towards.set(person[i].playercoords.subtract(person[i].pathtarget));
									normalise(towards);
									person[i].playerrotation=(float) (asin(0-towards.getX())*360/6.28);

									if(towards.getZ()>0)person[i].playerrotation=180-person[i].playerrotation;
								}
							}
					 	}

					 	person[i].whichblockx=(int) ((person[i].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING);
					 	person[i].whichblocky=(int) ((person[i].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING);

						if(!person[i].onground)person[i].velocity.y+=multiplier*GRAVITY;
					 	if(!person[i].onground&&(i!=0||visions!=1))person[i].playercoords.incrementAdd(person[i].velocity.multiply((float)multiplier));

					 	//Death by bleeding/shock

				 		if(person[i].health<=0){

				 			person[i].skeleton.offset=0;
							person[i].skeleton.free=true;
							person[i].longdead=1;

							for(int j=0;j<person[i].skeleton.num_joints;j++){

								person[i].skeleton.joints[j].position.incrementAdd(person[i].skeleton.joints[j].offset);
								person[i].skeleton.joints[j].position.set(doRotation(person[i].skeleton.joints[j].position,0,person[i].playerrotation,0));
								person[i].skeleton.joints[j].position.incrementAdd(person[i].playercoords);
								person[i].skeleton.joints[j].realoldposition.set(person[i].skeleton.joints[j].position);
								person[i].skeleton.joints[j].velocity.set(doRotation(person[i].skeleton.joints[j].velocity,0,person[i].playerrotation,0));
								person[i].skeleton.joints[j].velocity.incrementAdd(person[i].velocity);
								person[i].skeleton.joints[j].velocity.incrementAdd(person[i].facing.multiply(4));
							}
						}
					 }

				 	//Rag doll

					if(person[i].skeleton.free&&person[i].longdead>0){

						person[i].whichblockx=(int) ((person[i].skeleton.joints[0].position.getX()+BLOCK_SPACING/2)/BLOCK_SPACING);
						person[i].whichblocky=(int) ((person[i].skeleton.joints[0].position.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING);

						move.set(0);

						move.setX(person[i].whichblockx*BLOCK_SPACING);
						move.setZ(person[i].whichblocky*BLOCK_SPACING);

						person[i].skeleton.doGravity();
						
						if(person[i].averageloc.getY()<=50)person[i].skeleton.doConstraints(blocksimplecollide[citytype[person[i].whichblockx][person[i].whichblocky]],move,cityrotation[person[i].whichblockx][person[i].whichblocky]*90);
						if(person[i].averageloc.getY()>50)person[i].skeleton.doConstraints(blockcollide[citytype[person[i].whichblockx][person[i].whichblocky]],move,cityrotation[person[i].whichblockx][person[i].whichblocky]*90);

						person[i].oldaverageloc.set(person[i].averageloc);
						person[i].averageloc.set(0);

						for(int j=0;j<person[i].skeleton.num_joints;j++){
							person[i].averageloc.incrementAdd(person[i].skeleton.joints[j].position);
						}

						person[i].averageloc.incrementDevide(person[i].skeleton.num_joints);
						person[i].playercoords.set(person[i].averageloc);

						if(person[i].longdead<multiplier/2&&person[i].longdead>0)person[i].drawSkeleton(i);
						if(findDistancefast(person[i].averageloc,person[i].oldaverageloc)<.2*multiplier)person[i].longdead-=multiplier/2;
					}

					if(person[i].skeleton.free&&person[i].longdead<=0&&person[i].health>0&&person[i].longdead!=-1){

						person[i].longdead=1;
						person[i].skeleton.free=false;
						person[i].currentanimation=LYINGANIM;
						person[i].target=0;
						person[i].targetframe=0;

						//Get up from front or back?

						if(person[i].skeleton.forward.getY()>0)
						person[i].targetanimation=GETUPFRONTANIM;

						else
						person[i].targetanimation=GETUPBACKANIM;

						//Find playercoords

						person[i].playercoords.set(person[i].averageloc);

						for(int j=0;j<person[i].skeleton.num_joints;j++){
							if(person[i].skeleton.joints[j].position.getY()>person[i].playercoords.getY())person[i].playercoords.setY(person[i].skeleton.joints[j].position.getY());
						}

						//Find orientation

						XYZ firsttop = new XYZ(person[i].skeleton.joints[person[i].skeleton.jointlabels[NECK]].position.subtract(person[i].skeleton.joints[person[i].skeleton.jointlabels[GROIN]].position));

					 	normalise(firsttop);

						person[i].playerrotation=(float) acos(0-firsttop.getZ());
						person[i].playerrotation*=360/6.28;

						if(0>firsttop.getX())person[i].playerrotation=360-person[i].playerrotation;

						person[i].playerrotation*=-1;
						person[i].playervelocity.set(0);

						if(person[i].targetanimation==GETUPFRONTANIM)person[i].playerrotation+=180;

						for(int j=0;j<person[i].skeleton.num_joints;j++){
							if(person[i].tempanimation.position[j][0] == null) person[i].tempanimation.position[j][0] = new XYZ(); 
							person[i].tempanimation.position[j][0].set(person[i].skeleton.joints[j].position.subtract(person[i].playercoords));
							person[i].tempanimation.position[j][0].set(doRotation(person[i].tempanimation.position[j][0],0,-person[i].playerrotation,0));
						}
					}
				}

				//GRENADE

				if(button()&&person[0].whichgun==GRENADE&&person[0].ammo>0&&person[0].reloading<=0&&person[0].attackframe<0&&person[0].targetanimation!=CROUCHANIM){

					if(person[0].grenphase==0){

						XYZ soundsource=doRotation(person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]].position,0,person[0].playerrotation,0).add(person[0].playercoords);

						float gLoc[] = new float[3];

						gLoc[0]=soundsource.getX()/soundscalefactor;
						gLoc[1]=soundsource.getY()/soundscalefactor;
						gLoc[2]=soundsource.getZ()/soundscalefactor;

						alSourcefv(gSourceID[PINPULLSOUND], AL_POSITION, gLoc);
						alSourcePlay(gSourceID[PINPULLSOUND]);
						person[0].grenphase=1;

					}
				}

				if(!button()&&person[0].whichgun==GRENADE){

					if(person[0].grenphase==1){

						person[0].grenphase=0;
						person[0].attackframe=0;
						person[0].attacktarget=0;
						person[0].killtarget=0;
					}
				}

				if(person[0].targetanimation==CROUCHANIM){

					if(person[0].grenphase==1){

						person[0].grenphase=0;

						XYZ soundsource=doRotation(person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]].position,0,person[0].playerrotation,0).add(person[0].playercoords);

						float gLoc[] = new float[3];

						gLoc[0]=soundsource.getX()/soundscalefactor;
						gLoc[1]=soundsource.getY()/soundscalefactor;
						gLoc[2]=soundsource.getZ()/soundscalefactor;
						alSourcefv(gSourceID[PINREPLACESOUND], AL_POSITION, gLoc);
						alSourcePlay(gSourceID[PINREPLACESOUND]);
					}
				}

				//Get gun

				int temp;
				int temp2;
				boolean switched=false;

				if(button()&&!oldbutton&&(!person[0].aiming||person[0].whichgun==GRENADE||person[0].whichgun==NOGUN||person[0].whichgun==KNIFE)&&person[0].currentanimation==CROUCHANIM){

					for(int i=0;i<MAX_PEOPLE;i++){

						if(!switched&&person[i].skeleton.free&&findDistancefast(person[0].playercoords,person[i].averageloc)<200){

							float gLoc[] = new float[3];

							gLoc[0]=person[0].playercoords.getX()/soundscalefactor;
							gLoc[1]=person[0].playercoords.getY()/soundscalefactor;
							gLoc[2]=person[0].playercoords.getZ()/soundscalefactor;

							alSourcefv(gSourceID[CLICKSOUND], AL_POSITION, gLoc);
							alSourcePlay(gSourceID[CLICKSOUND]);

							temp=person[0].whichgun;
							temp2=person[0].ammo;

							person[0].whichgun=person[i].whichgun;
							person[0].ammo=person[i].ammo;
							person[i].whichgun=temp;
							person[i].ammo=temp2;
							person[0].aiming=true;
							person[0].aimamount=0;

							switched=true;

						}
					}
				}

				//Throw

				if(button()&&person[0].attackframe<0&&((person[0].whichgun==NOGUN||!person[0].aiming)&&person[0].whichgun!=KNIFE)&&person[0].currentanimation!=CROUCHANIM&&person[0].targetanimation!=CROUCHANIM&&person[0].targetanimation!=THROWANIM&&visions==0){

					if(person[0].targetanimation==IDLEANIM||person[0].targetanimation==WALKANIM){

						boolean attacking=false;
						person[0].killtarget=-1;
						float closedistance=-1;

						for(int i=1;i<numpeople;i++){				

							if(!person[i].skeleton.free&&(person[i].whichgun!=NOGUN)&&findDistancefast(person[i].playercoords,person[0].playercoords.add(flatfacing))<12){

								attacking=true;
								
								if(person[0].killtarget==-1||findDistancefast(person[i].playercoords,person[0].playercoords)<closedistance){
									person[0].killtarget=i;
									closedistance=findDistancefast(person[i].playercoords,person[0].playercoords);
								}
							}
						}

						if(attacking){

							score+=150;

							person[0].aiming=true;
							person[0].whichgun=person[person[0].killtarget].whichgun;
							person[0].ammo=person[person[0].killtarget].ammo;
							person[person[0].killtarget].whichgun=NOGUN;
							person[person[0].killtarget].killtarget=-1;
							person[0].targetframe=0;
							person[0].targetanimation=THROWANIM;
							person[0].target=1;
							person[0].speed=1;
							person[person[0].killtarget].targetframe=0;
							person[person[0].killtarget].targetanimation=THROWNANIM;
							person[person[0].killtarget].target=1;
							person[person[0].killtarget].playercoords.set(person[0].playercoords);
							person[person[0].killtarget].playerrotation=person[0].playerrotation;
							person[person[0].killtarget].speed=person[0].speed;
							person[person[0].killtarget].speedmult=1;
						}
					}
				}

				//Gun whacking

				if(button()&&(!person[0].aiming||person[0].ammo<=0||person[0].whichgun==NOGUN||person[0].whichgun==KNIFE||person[0].targetanimation==JOGANIM)&&person[0].currentanimation!=CROUCHANIM&&person[0].targetanimation!=THROWANIM&&person[0].whichgun!=GRENADE&&person[0].targetanimation!=CROUCHANIM&&visions==0){

					if(person[0].attackframe==-1||(person[0].killtarget > -1&&person[person[0].killtarget].skeleton.free)){

						boolean attacking=false;
						
						person[0].killtarget=-1;

						float closedistance=-1;

						for(int i=1;i<numpeople;i++){				

							if(person[i].existing&&person[i].type!=VIPTYPE&&!person[i].skeleton.free&&findDistancefast(person[i].playercoords,person[0].playercoords.add(flatfacing))<12+(person[0].whichgun==KNIFE ? 1:0)*10){

								if(person[0].killtarget==-1||findDistancefast(person[i].playercoords,person[0].playercoords)<closedistance){

									attacking=true;
									person[0].killtarget=i;
									closedistance=findDistancefast(person[i].playercoords,person[0].playercoords);
								}
							}
						}

						if(attacking){

							person[0].attacktarget=0;
							person[0].attackframe=0;
						}

						whacked=false;
					}
				}

				XYZ velocity = new XYZ();

				if(person[0].attackframe>1||(person[0].attackframe>=0&&person[0].currentanimation==JOGANIM)){

					//Temporal
					
					if(person[0].killtarget > -1 && !person[person[0].killtarget].skeleton.free&&person[0].killtarget!=0&&(!person[0].aiming||person[0].whichgun==NOGUN||person[0].whichgun==KNIFE||person[0].targetanimation==JOGANIM)){

						float gLoc[] = new float[3];

						gLoc[0]=(camera.position.getX()+((person[0].playercoords.getX()+flatfacing.getX())-camera.position.getX())/2)/soundscalefactor;
						gLoc[1]=(camera.position.getY()+((person[0].playercoords.getY()+flatfacing.getY())-camera.position.getY())/2)/soundscalefactor;
						gLoc[2]=(camera.position.getZ()+((person[0].playercoords.getZ()+flatfacing.getZ())-camera.position.getZ())/2)/soundscalefactor;

						if(person[person[0].killtarget].type!=ZOMBIETYPE)
						{
							if(person[0].whichgun!=KNIFE){

								alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[HEADWHACKSOUND]);
							}

							if(person[0].whichgun==KNIFE){

								alSourcefv(gSourceID[KNIFESLASHSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[KNIFESLASHSOUND]);

								person[person[0].killtarget].bjoint1=person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]];
								person[person[0].killtarget].bjoint2=person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]];
								person[person[0].killtarget].bleeding=1;
								person[person[0].killtarget].bleeddelay=1;
								person[0].bjoint1=person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]];
								person[0].bjoint2=person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]];
								person[0].bleeding=1;
								person[0].bleeddelay=1;

								velocity.set(doRotation(flatfacing,0,70,0).multiply(50).add(person[0].velocity.multiply(2)));
								velocity.y+=30;

								sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.3f), 2);
								sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.2f), 3);
								sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.1f), 4);					

							}

							person[person[0].killtarget].health-=100;
							person[person[0].killtarget].skeleton.free=true;
							person[person[0].killtarget].longdead=1;

							for(int j=0;j<person[person[0].killtarget].skeleton.num_joints;j++){

								person[person[0].killtarget].skeleton.joints[j].position.set(doRotation(person[person[0].killtarget].skeleton.joints[j].position,0,person[person[0].killtarget].playerrotation,0));
								person[person[0].killtarget].skeleton.joints[j].position.incrementAdd(person[person[0].killtarget].playercoords);
								person[person[0].killtarget].skeleton.joints[j].realoldposition.set(person[person[0].killtarget].skeleton.joints[j].position);
								person[person[0].killtarget].skeleton.joints[j].velocity.set(person[person[0].killtarget].velocity);
								person[person[0].killtarget].skeleton.joints[j].velocity.x+=abs(random()%10)-5;
								person[person[0].killtarget].skeleton.joints[j].velocity.y+=abs(random()%10)-5;
								person[person[0].killtarget].skeleton.joints[j].velocity.z+=abs(random()%10)-5;
							}

							if(person[0].whichgun!=KNIFE){

								person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[HEAD]].velocity.incrementAdd(doRotation(flatfacing,0,40,0).multiply(50));
								person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[HEAD]].velocity.incrementAdd(person[0].velocity.multiply(2));
							}
						}

						else

						{
							if(!whacked){

								whacked=true;

								if(person[0].whichgun!=KNIFE){

									alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);
									alSourcePlay(gSourceID[HEADWHACKSOUND]);
								}

								if(person[0].whichgun==KNIFE){

									alSourcefv(gSourceID[KNIFESLASHSOUND], AL_POSITION, gLoc);
									alSourcePlay(gSourceID[KNIFESLASHSOUND]);

									person[person[0].killtarget].bjoint1=person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]];
									person[person[0].killtarget].bjoint2=person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]];
									person[person[0].killtarget].bleeding=1;
									person[person[0].killtarget].bleeddelay=1;

									person[0].bjoint1=person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]];
									person[0].bjoint2=person[0].skeleton.joints[person[0].skeleton.jointlabels[RIGHTHAND]];

									person[0].bleeding=1;
									person[0].bleeddelay=1;

									velocity.set(doRotation(flatfacing,0,70,0).multiply(50).add(person[0].velocity.multiply(2)));
									velocity.y+=30;

									sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.3f), 2);
									sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.2f), 3);
									sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,doRotation(person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[NECK]].position,0,person[person[0].killtarget].playerrotation,0).add(person[person[0].killtarget].playercoords), velocity.multiply(.1f), 4);					
								}

								person[person[0].killtarget].health-=200;
								person[person[0].killtarget].maxhealth-=20;
								person[person[0].killtarget].skeleton.free=true;
								person[person[0].killtarget].longdead=1;

								for(int j=0;j<person[person[0].killtarget].skeleton.num_joints;j++){

									person[person[0].killtarget].skeleton.joints[j].position.set(doRotation(person[person[0].killtarget].skeleton.joints[j].position,0,person[person[0].killtarget].playerrotation,0));
									person[person[0].killtarget].skeleton.joints[j].position.incrementAdd(person[person[0].killtarget].playercoords);
									person[person[0].killtarget].skeleton.joints[j].realoldposition.set(person[person[0].killtarget].skeleton.joints[j].position);
									person[person[0].killtarget].skeleton.joints[j].velocity.set(person[person[0].killtarget].velocity);
									person[person[0].killtarget].skeleton.joints[j].velocity.x+=abs(random()%10)-5;
									person[person[0].killtarget].skeleton.joints[j].velocity.y+=abs(random()%10)-5;
									person[person[0].killtarget].skeleton.joints[j].velocity.z+=abs(random()%10)-5;
								}

								if(person[0].whichgun!=KNIFE){

									person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[HEAD]].velocity.incrementAdd(doRotation(flatfacing,0,40,0).multiply(50));
									person[person[0].killtarget].skeleton.joints[person[person[0].killtarget].skeleton.jointlabels[HEAD]].velocity.incrementAdd(person[0].velocity.multiply(2));
								}
							}
						}
					}
				}

				

				//Tackle

				if(person[0].currentanimation==DIVEANIM&&visions==0){

					for(int i=1;i<numpeople;i++){				

						if(!person[i].skeleton.free&&findDistancefast(person[i].playercoords,person[0].playercoords.add(flatfacing))<22){

							float gLoc[] = new float[3];

							gLoc[0]=(camera.position.getX()+((person[0].playercoords.getX()+flatfacing.getX())-camera.position.getX())/2)/soundscalefactor;
							gLoc[1]=(camera.position.getY()+((person[0].playercoords.getY()+flatfacing.getY())-camera.position.getY())/2)/soundscalefactor;
							gLoc[2]=(camera.position.getZ()+((person[0].playercoords.getZ()+flatfacing.getZ())-camera.position.getZ())/2)/soundscalefactor;
							
							alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);
							alSourcePlay(gSourceID[HEADWHACKSOUND]);

							person[i].skeleton.free=true;
							person[i].longdead=1;

							for(int j=0;j<person[i].skeleton.num_joints;j++){

								person[i].skeleton.joints[j].position.set(doRotation(person[i].skeleton.joints[j].position,0,person[i].playerrotation,0));
								person[i].skeleton.joints[j].position.incrementAdd(person[i].playercoords);
								person[i].skeleton.joints[j].realoldposition.set(person[i].skeleton.joints[j].position);
								person[i].skeleton.joints[j].velocity.set(person[0].velocity);
								person[i].skeleton.joints[j].velocity.setY(-10);
								person[i].skeleton.joints[j].velocity.x+=abs(random()%10)-5;
								person[i].skeleton.joints[j].velocity.y+=abs(random()%10)-5;
								person[i].skeleton.joints[j].velocity.z+=abs(random()%10)-5;
							}
						}
					}
				}
				
				//Fire/wing

				XYZ wallhit = new XYZ();
				XYZ start = new XYZ();
				XYZ finalwallhit = new XYZ();

				boolean firing;
				float inaccuracy = 0.0f;
				int numshots = 0;
				XYZ hitnorm = new XYZ();
				XYZ hitmove = new XYZ();
				int hitpoly=0;
				float hitrotation = 0.0f;

				Model model = null;

				for(int j=0;j<numpeople;j++){

					if(person[j].type==PLAYERTYPE||person[j].type==EVILTYPE){

					firing=false;

					if(person[j].ammo>0&&person[j].reloading<=0&&person[j].targetanimation!=JOGANIM){

						if(button()&&!oldbutton&&j==0)firing=true;
						if(j!=0)firing=false;
						if(j!=0&&person[j].whichgun!=NOGUN&&person[j].whichgun!=KNIFE&&person[j].killtargetvisible&&person[j].shotdelay<0)firing=true;
						if(j!=0&&person[j].whichgun!=NOGUN&&person[j].whichgun!=KNIFE&&person[j].killtargetvisible&&person[j].shotdelay>0)person[j].shotdelay-=multiplier*.9;
						if(person[j].skeleton.free||person[j].targetanimation==GETUPFRONTANIM||person[j].targetanimation==GETUPBACKANIM)person[j].shotdelay=SHOTDELAYAMOUNT/difficulty;
						if(j!=0&&person[j].whichgun!=NOGUN&&person[j].whichgun!=KNIFE&&person[j].killtargetvisible)person[j].aiming=true;
						if(person[0].whichgun!=ASSAULTRIFLE)oldbutton=true;
					}

					if(person[j].ammo==0&&person[j].reloads[person[j].whichgun]>0){

						person[j].ammo=-1;
						person[j].aiming=false;
						firing=false;
					}

					if(person[0].ammo<=0&&button()&&!oldbutton&&person[0].targetanimation!=JOGANIM&&person[0].whichgun!=NOGUN&&person[0].whichgun!=KNIFE&&person[0].whichgun!=GRENADE&&person[0].aiming){

						oldbutton=true;

						float gLoc[] = new float[3];

						gLoc[0]=person[0].playercoords.getX()/soundscalefactor;
						gLoc[1]=person[0].playercoords.getY()/soundscalefactor;
						gLoc[2]=person[0].playercoords.getZ()/soundscalefactor;

						alSourcefv(gSourceID[CLICKSOUND], AL_POSITION, gLoc);
						alSourcePlay(gSourceID[CLICKSOUND]);
					}

					if(j==0&&visions==1&&firing){

						firing=false;
						alSourcePlay(gSourceID[SOULINSOUND]);
					}

					if(person[j].aimamount<1)firing=false;

					if(firing){

						if(person[j].whichgun==HANDGUN1||person[j].whichgun==HANDGUN2)inaccuracy=8;
						if(person[j].whichgun==ASSAULTRIFLE)inaccuracy=6;
						if(person[j].whichgun==SNIPERRIFLE)inaccuracy=2;
						if(person[j].whichgun==SHOTGUN)inaccuracy=6;
						
						if(person[j].killtarget>-1 && person[person[j].killtarget].skeleton.free)inaccuracy*=3;

						person[j].shotdelay=SHOTDELAYAMOUNT/difficulty;

						if(person[j].aiming&&person[j].recoil<=0){

							//Firing

							person[j].litup=true;
							person[j].recoil=1;
							XYZ end = new XYZ(), aim = new XYZ(), oldend = new XYZ();

							HitStruct hitstruct = new HitStruct(),temphitstruct = new HitStruct();

							float olddistance = 0.0f;
							float distance = 0.0f;
							float totalarea = 0.0f;

							int whichhit= -1;

							if(person[j].whichgun==SHOTGUN)numshots=7;
							if(person[j].whichgun!=SHOTGUN)numshots=1;
							if(person[j].whichgun!=GRENADE)person[j].ammo--;

							for(int p=0;p<numshots;p++){

							if(!zoom||j!=0){

								if(person[j].whichgun==ASSAULTRIFLE)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),0,person[j].playerrotation-2.5f,0));
								if(person[j].whichgun==SNIPERRIFLE)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),0,person[j].playerrotation+4,0));
								if(person[j].whichgun==SHOTGUN)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),2+(float)(random()%1000)/500,0,0));
								if(person[j].whichgun==SHOTGUN)aim.set(doRotation(aim,0,person[j].playerrotation-1+(float)(random()%1000)/500,0));
								
								if(person[j].whichgun==HANDGUN1&&thirdperson==0&&j==0)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.65f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.35f))),0,person[j].playerrotation-.9f,0));
								if(person[j].whichgun==HANDGUN1&&(thirdperson!=0||j!=0))aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.35f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.65f))),0,person[j].playerrotation-.9f,0));
								if(person[j].whichgun==HANDGUN2&&thirdperson==0&&j==0)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.65f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.35f))),0,person[j].playerrotation-.9f,0));
								if(person[j].whichgun==HANDGUN2&&(thirdperson!=0||j!=0))aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.35f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.65f))),0,person[j].playerrotation-.9f,0));
							}

							if(zoom&&j==0){

								start.set(camera.position);
								aim.set(facing);
							}

							normalise(aim);

							if(person[j].whichgun==SNIPERRIFLE){

								start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
								float gLoc[] = new float[3];
								
								start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.35f));

								gLoc[0]=(camera.position.getX()+(start.getX()-camera.position.getX())/4)/soundscalefactor;
								gLoc[1]=(camera.position.getY()+(start.getY()-camera.position.getY())/4)/soundscalefactor;
								gLoc[2]=(camera.position.getZ()+(start.getZ()-camera.position.getZ())/4)/soundscalefactor;

								alSourcefv(gSourceID[RIFLESOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[RIFLESOUND]);

								if(j==0){
									if(person[j].currentanimation!=CROUCHANIM)camera.rotation2-=7;
									if(person[j].currentanimation==CROUCHANIM)camera.rotation2-=3;
								}
							}

							if(person[j].whichgun==SHOTGUN){

								start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
								float gLoc[] = new float[3];

								start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.35f));

								if(p==numshots-1){

								gLoc[0]=(camera.position.getX()+(start.getX()-camera.position.getX())/4)/soundscalefactor;
								gLoc[1]=(camera.position.getY()+(start.getY()-camera.position.getY())/4)/soundscalefactor;
								gLoc[2]=(camera.position.getZ()+(start.getZ()-camera.position.getZ())/4)/soundscalefactor;

								alSourcefv(gSourceID[SHOTGUNSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[SHOTGUNSOUND]);

								if(j==0){
									if(person[j].currentanimation!=CROUCHANIM)camera.rotation2-=7;
									if(person[j].currentanimation==CROUCHANIM)camera.rotation2-=3;
								}
								}
							}

							if(person[j].whichgun==HANDGUN1){

								start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTWRIST])].position,0,person[j].playerrotation,0)));
								start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.55f));

								float gLoc[] = new float[3];

								gLoc[0]=(camera.position.getX()+(start.getX()-camera.position.getX())/4)/soundscalefactor;
								gLoc[1]=(camera.position.getY()+(start.getY()-camera.position.getY())/4)/soundscalefactor;
								gLoc[2]=(camera.position.getZ()+(start.getZ()-camera.position.getZ())/4)/soundscalefactor;

								alSourcefv(gSourceID[PISTOL1SOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[PISTOL1SOUND]);

								if(j==0){
									if(person[j].currentanimation!=CROUCHANIM)camera.rotation2-=6;
									if(person[j].currentanimation==CROUCHANIM)camera.rotation2-=4;
								}
							}

							if(person[j].whichgun==HANDGUN2){

								start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTWRIST])].position,0,person[j].playerrotation,0)));
								start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.55f));

								float gLoc[] = new float[3];

								gLoc[0]=(camera.position.getX()+(start.getX()-camera.position.getX())/4)/soundscalefactor;
								gLoc[1]=(camera.position.getY()+(start.getY()-camera.position.getY())/4)/soundscalefactor;
								gLoc[2]=(camera.position.getZ()+(start.getZ()-camera.position.getZ())/4)/soundscalefactor;

								alSourcefv(gSourceID[PISTOL2SOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[PISTOL2SOUND]);

								if(j==0){

									if(person[j].currentanimation!=CROUCHANIM)camera.rotation2-=5;
									if(person[j].currentanimation==CROUCHANIM)camera.rotation2-=3;
								}
							}

							if(person[j].whichgun==ASSAULTRIFLE){

								start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
								start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.25f));

								float gLoc[] = new float[3];

								gLoc[0]=(camera.position.getX()+(start.getX()-camera.position.getX())/4)/soundscalefactor;
								gLoc[1]=(camera.position.getY()+(start.getY()-camera.position.getY())/4)/soundscalefactor;
								gLoc[2]=(camera.position.getZ()+(start.getZ()-camera.position.getZ())/4)/soundscalefactor;

								alSourcefv(gSourceID[MACHINEGUNSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[MACHINEGUNSOUND]);

								if(j==0){

									if(person[j].currentanimation!=CROUCHANIM){
										camera.rotation2-=2.3;
										camera.rotation+=(random()%100)/50;
									}

									if(person[j].currentanimation==CROUCHANIM){
										camera.rotation2-=1.5;
										camera.rotation+=(random()%100)/60;
									}
								}
							}

							end.set(start.add(aim.multiply(1000)));

							if(debug){
								
								int a =0;
								if ( (glfwGetKey(window, GLFW_KEY_G) == GLFW_PRESS) && a==0) 
									sprites.makeSprite(GRENADE_SPRITE, 1, 1, 1, 1, start, aim.multiply(200), 1.01f);
								
							}
																
								 if( (glfwGetKey(window, GLFW_KEY_G) != GLFW_PRESS) || j!=0 || !debug){
									
									int bulletstrength=1;
									int firstpass=-1;
									boolean penetrate;

									for(int m=0;m<bulletstrength;m++){

										//People

										whichhit=-1;
										for(int i=0;i<numpeople;i++){
											
											if(i!=j&&i!=firstpass&&person[i].existing){

												temphitstruct.set(person[i].bulletCollideWithPlayer(i, start, end));	

												if(temphitstruct.collision){

													distance=findDistancefast(start,temphitstruct.hitlocation);
													
													if(distance<olddistance||whichhit==-1){

														end.set(temphitstruct.hitlocation);
														olddistance=distance;
														hitstruct.set(temphitstruct);
														whichhit=i;
													}
												}
											}
										}

										//Blocks

										wallhit.set(0);
										beginx=(int) ((person[j].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING-3);
										if(beginx<0)beginx=0;
										beginz=(int) ((person[j].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING-3);
										if(beginz<0)beginz=0;

										endx=(int) ((person[j].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING+3);
										if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;
										endz=(int) ((person[j].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING+3);
										if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

										if(beginx<endx&&beginz<endz) finalwallhit.set(0);

										for(int i=beginx;i<=endx;i++) {
											for(int j1=beginz;j1<=endz;j1++){

												move.set(0);
												move.setX(i*BLOCK_SPACING);
												move.setZ(j1*BLOCK_SPACING);
												whichtri=blocks[citytype[i][j1]].lineCheck2(start,end,wallhit,move,cityrotation[i][j1]*90);

												if(whichtri!=-1){

													whichhit=-1;
													end.set(wallhit);
													finalwallhit.set(wallhit);
													hitnorm.set(doRotation(blocks[citytype[i][j1]].normals[whichtri],0,cityrotation[i][j1]*90,0));
													hitmove.set(move);
													hitrotation=cityrotation[i][j1]*90;
													hitpoly=whichtri;
													model=blocks[citytype[i][j1]];
													if(j1==0&&blocks[citytype[i][j1]].normals[whichtri].getY()>.9f)bulletstrength=2;
												}
											}
										}

										wallhit.set(0);
										wallhit.setX(camera.position.getX());
										wallhit.setZ(camera.position.getZ());
										whichtri=bigStreet.lineCheck2(start,end,wallhit,wallhit,0);

										if(whichtri!=-1){

											end.y-=.5;
											end.set(wallhit);
											finalwallhit.set(wallhit);
											bulletstrength=2;
											hitnorm.set(0);
											hitnorm.setY(1);
											hitmove.set(0);
											hitrotation=0;
										}

										if(m==0){

											if(j==0&&slomo==2){

												soundscalefactor=SOUNDSCALEFACTORDEFAULT;
												
												alSourceStop(gSourceID[whichsong]);
												alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
												alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);

												if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
												if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
												if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;

												alSourcef(gSourceID[whichsong], AL_PITCH, 1);
												alSourcePlay(gSourceID[whichsong]);
												alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
												alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);

												slomo=0;

												if(whichhit==-1)alSourcePlay(gSourceID[DISGUISEKILLSOUND]);
												flashamount=.5f;
												flashr=1;flashg=1;flashb=1;
											}
										}

										//Impact

										oldend.set(end);

										//with person

										if(whichhit!=-1&&whichhit!=firstpass){

											if(j==0)person[whichhit].dead=true;

											if(whichhit==1){
												murderer=j;
											}

											if(person[whichhit].health==100&&j==0){
												
												if(person[whichhit].type==CIVILIANTYPE)civkills++;
												if(person[whichhit].type==EVILTYPE)goodkills++;
												
											}

											if(person[whichhit].health==100&&j!=0){
												badkills++;
											}

											//penetrate

												penetrate=abs(random()%2)==1;

												if(numshots>1)penetrate=false;
												if(penetrate){bulletstrength=2;

												firstpass=whichhit;
												end.set(start.add(aim.multiply(1000)));}

											if(person[j].whichgun==ASSAULTRIFLE)person[whichhit].health-=20;
											if(person[j].whichgun==ASSAULTRIFLE&&person[whichhit].type==ZOMBIETYPE)person[whichhit].health-=60;
											if(person[j].whichgun==HANDGUN1){

												if(person[whichhit].type!=ZOMBIETYPE)person[whichhit].health-=100;
												if(person[whichhit].type==ZOMBIETYPE)person[whichhit].health-=100;
												person[whichhit].doAnimations(whichhit);
											}

											if(person[j].whichgun==HANDGUN2)person[whichhit].health-=20;
											if(person[j].whichgun==HANDGUN2&&person[whichhit].type==ZOMBIETYPE)person[whichhit].health-=60;
											if(person[j].whichgun==SNIPERRIFLE&&m!=0)person[whichhit].health-=30;
											if(person[j].whichgun==SHOTGUN)person[whichhit].health-=60;
											if(person[j].whichgun==SNIPERRIFLE&&m==0){

												if(person[whichhit].type!=ZOMBIETYPE)person[whichhit].health-=100;
												if(person[whichhit].type==ZOMBIETYPE)person[whichhit].health-=120;
												person[whichhit].doAnimations(whichhit);
											}

											if(hitstruct.joint1.modelnum==HEADMODEL&&person[whichhit].type!=ZOMBIETYPE){
												person[whichhit].health-=60;
											}

											if(person[whichhit].type==ZOMBIETYPE)person[whichhit].speedmult-=.05;
											if(person[whichhit].type==ZOMBIETYPE)person[whichhit].maxhealth-=10;

											if(whichhit==0){

												bulletstrength=1;
												person[0].health=100;
												
												flashr=0;
												flashg=0;
												flashb=0;

												flashamount=1;
												float gLoc[] = new float[3];

												gLoc[0]=hitstruct.hitlocation.getX()/soundscalefactor;
												gLoc[1]=hitstruct.hitlocation.getY()/soundscalefactor;
												gLoc[2]=hitstruct.hitlocation.getZ()/soundscalefactor;

												alSourcefv(gSourceID[BODYWHACKSOUND], AL_POSITION, gLoc);
												alSourcePlay(gSourceID[BODYWHACKSOUND]);
											}

											person[whichhit].longdead=1;

											if(person[whichhit].health<=0){

												person[whichhit].skeleton.offset=0;

												if(!person[whichhit].skeleton.free){

													person[whichhit].skeleton.free=true;
													totalarea=0;

													for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

														person[whichhit].skeleton.joints[j1].position.set(doRotation(person[whichhit].skeleton.joints[j1].position,0,person[whichhit].playerrotation,0));
														person[whichhit].skeleton.joints[j1].position.incrementAdd(person[whichhit].playercoords);
														person[whichhit].skeleton.joints[j1].realoldposition.set(person[whichhit].skeleton.joints[j1].position);
														person[whichhit].skeleton.joints[j1].velocity.set(person[whichhit].velocity);
														person[whichhit].skeleton.joints[j1].velocity.x+=(abs(random()%20)-10)/2;
														person[whichhit].skeleton.joints[j1].velocity.y+=(abs(random()%20)-10)/2;
														person[whichhit].skeleton.joints[j1].velocity.z+=(abs(random()%20)-10)/2;
													}
												}

												for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

													if(findDistancefast(person[whichhit].skeleton.joints[j1].position,hitstruct.hitlocation)<200){
														totalarea+=(200/findDistancefast(person[whichhit].skeleton.joints[j1].position,hitstruct.hitlocation));
													}
												}

												for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

													if(findDistancefast(person[whichhit].skeleton.joints[j1].position,hitstruct.hitlocation)<200){
														person[whichhit].skeleton.joints[j1].velocity.incrementAdd(aim.multiply((200/findDistancefast(person[whichhit].skeleton.joints[j1].position,hitstruct.hitlocation))/totalarea*200));
													}
												}
											}

											if(person[whichhit].health>0){

												if(!person[whichhit].killtargetvisible&&person[whichhit].type!=ZOMBIETYPE&&person[whichhit].currentanimation!=GETUPFRONTANIM&person[whichhit].currentanimation!=GETUPBACKANIM){

													if(hitstruct.joint1.modelnum==HEADMODEL)person[whichhit].targetanimation=HEADPAINANIM;
													if(hitstruct.joint1.modelnum==CHESTMODEL)person[whichhit].targetanimation=CHESTPAINANIM;
													if(hitstruct.joint1.modelnum==ABDOMENMODEL)person[whichhit].targetanimation=STOMACHPAINANIM;

													if(hitstruct.joint1.label==RIGHTELBOW||hitstruct.joint1.label==RIGHTSHOULDER||hitstruct.joint1.label==RIGHTWRIST||hitstruct.joint1.label==RIGHTHAND)person[whichhit].targetanimation=RIGHTARMPAINANIM;
													if(hitstruct.joint1.label==LEFTELBOW||hitstruct.joint1.label==LEFTSHOULDER||hitstruct.joint1.label==LEFTWRIST||hitstruct.joint1.label==LEFTHAND)person[whichhit].targetanimation=LEFTARMPAINANIM;
													if(hitstruct.joint1.label==RIGHTKNEE||hitstruct.joint1.label==RIGHTHIP||hitstruct.joint1.label==RIGHTANKLE||hitstruct.joint1.label==RIGHTFOOT)person[whichhit].targetanimation=RIGHTLEGPAINANIM;
													if(hitstruct.joint1.label==LEFTKNEE||hitstruct.joint1.label==LEFTHIP||hitstruct.joint1.label==LEFTANKLE||hitstruct.joint1.label==LEFTFOOT)person[whichhit].targetanimation=LEFTLEGPAINANIM;

													person[whichhit].targetframe=0;
													person[whichhit].target=0;
												}

												person[whichhit].skeleton.offset=1;

												for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

													if(findDistancefast(doRotation(person[whichhit].skeleton.joints[j1].position,0,person[whichhit].playerrotation,0).add(person[whichhit].playercoords),hitstruct.hitlocation)<200){
														totalarea+=(200/findDistancefast(doRotation(person[whichhit].skeleton.joints[j1].position,0,person[whichhit].playerrotation,0).add(person[whichhit].playercoords),hitstruct.hitlocation));
													}
												}

												float offsetlength;

												for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

													if(findDistancefast(doRotation(person[whichhit].skeleton.joints[j1].position,0,person[whichhit].playerrotation,0).add(person[whichhit].playercoords),hitstruct.hitlocation)<200){
														person[whichhit].skeleton.joints[j1].offset.incrementAdd(doRotation(aim.multiply((200/findDistancefast(doRotation(person[whichhit].skeleton.joints[j1].position,0,person[whichhit].playerrotation,0).add(person[whichhit].playercoords),hitstruct.hitlocation))/totalarea*10),0,-person[whichhit].playerrotation,0));
													}

													offsetlength=findLengthfast(person[whichhit].skeleton.joints[j1].offset);

													if(offsetlength>36){

														normalise(person[whichhit].skeleton.joints[j1].offset);
														person[whichhit].skeleton.joints[j1].offset.incrementMultiply(6);

													}

												}

											}

											if(hitstruct.joint1.modelnum==HEADMODEL&&person[whichhit].health<=0){

												for(int j1=0;j1<person[whichhit].skeleton.num_joints;j1++){

													if(person[whichhit].skeleton.joints[j1]==hitstruct.joint1||person[whichhit].skeleton.joints[j1]==hitstruct.joint2){

														if(j1!=person[whichhit].skeleton.jointlabels[ABDOMEN]&&j1!=person[whichhit].skeleton.jointlabels[GROIN]&&j1!=person[whichhit].skeleton.jointlabels[NECK]){

															sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[whichhit].skeleton.joints[j1].position, person[whichhit].skeleton.joints[j1].velocity.devide(3), 9);
															sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[whichhit].skeleton.joints[j1].position, doRotation(person[whichhit].skeleton.joints[j1].velocity.devide(3),random()%360,random()%360,0).devide(5), 5);
															sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[whichhit].skeleton.joints[j1].position, doRotation(person[whichhit].skeleton.joints[j1].velocity.devide(3),random()%360,random()%360,0).devide(5), 5);
															sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[whichhit].skeleton.joints[j1].position, doRotation(person[whichhit].skeleton.joints[j1].velocity.devide(3),random()%360,random()%360,0).devide(5), 5);
															sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[whichhit].skeleton.joints[j1].position, doRotation(person[whichhit].skeleton.joints[j1].velocity.devide(3),random()%360,random()%360,0).devide(5), 5);

															person[whichhit].skeleton.deleteJoint(j1);
															person[whichhit].skeleton.broken=1;
															person[whichhit].health=-10000;
															person[whichhit].skeleton.joints[j1].existing=false;

															if(person[whichhit].type==ZOMBIETYPE)score+=300;
														}
													}
												}
											}

											XYZ velocity2 = new XYZ();
											velocity2.set(aim.multiply(-8));

											//blood

											if(hitstruct.joint1.modelnum!=HEADMODEL){
												
											if(person[j].whichgun==SNIPERRIFLE)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(0), 5);
											if(person[j].whichgun==SNIPERRIFLE&&penetrate)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(-3), 7);
											if(person[j].whichgun==SHOTGUN)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(0), 5);
											if(person[j].whichgun==SHOTGUN&&penetrate)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(-3), 7);
											if(person[j].whichgun==ASSAULTRIFLE)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(0), 3);
											if(person[j].whichgun==ASSAULTRIFLE&&penetrate)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(-3), 7);
											if(person[j].whichgun==HANDGUN1)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(0), 3);
											if(person[j].whichgun==HANDGUN1&&penetrate)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(-3), 4);
											if(person[j].whichgun==HANDGUN2)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(0), 3);
											if(person[j].whichgun==HANDGUN2&&penetrate)sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, 0, 0, hitstruct.hitlocation, velocity2.multiply(-3), 4);

											}else{

												sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, .2f, .2f, hitstruct.hitlocation, velocity2.multiply(0), 6);
												sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, .5f, .5f, hitstruct.hitlocation, velocity2.multiply(-2), 7);
												sprites.makeSprite(BLOOD_SPRITE_NOUP, 1, 1, .2f, .2f, hitstruct.hitlocation, velocity2.multiply(-3), 10);
											}

											person[whichhit].bjoint1.set(hitstruct.joint1);
											person[whichhit].bjoint2.set(hitstruct.joint2);
											person[whichhit].bleeding=1;
											person[whichhit].bleeddelay=1;

											float gLoc[] = new float[3];

											gLoc[0]=(camera.position.getX()+(hitstruct.hitlocation.getX()-camera.position.getX())/4)/soundscalefactor;
											gLoc[1]=(camera.position.getY()+(hitstruct.hitlocation.getY()-camera.position.getY())/4)/soundscalefactor;
											gLoc[2]=(camera.position.getZ()+(hitstruct.hitlocation.getZ()-camera.position.getZ())/4)/soundscalefactor;

											if(hitstruct.joint1.modelnum!=HEADMODEL){

												if(thirdperson==0)alSourcef(gSourceID[BODYHITSOUND], AL_MIN_GAIN, 1);
												if(thirdperson!=0)alSourcef(gSourceID[BODYHITSOUND], AL_MIN_GAIN, .1f);

												alSourcefv(gSourceID[BODYHITSOUND], AL_POSITION, gLoc);
												alSourcePlay(gSourceID[BODYHITSOUND]);
											}

											if(hitstruct.joint1.modelnum==HEADMODEL){
												
												if(thirdperson==0)alSourcef(gSourceID[HEADSHOTSOUND], AL_MIN_GAIN, 1);
												if(thirdperson!=0)alSourcef(gSourceID[HEADSHOTSOUND], AL_MIN_GAIN, .1f);
												
												alSourcefv(gSourceID[HEADSHOTSOUND], AL_POSITION, gLoc);
												alSourcePlay(gSourceID[HEADSHOTSOUND]);
											}
										}//with wall

										if(oldend.equals(finalwallhit)){

											decals.makeDecal(BULLETHOLE, finalwallhit,.7f,hitnorm, hitpoly, model, hitmove, hitrotation);

											XYZ velocity2 = new XYZ();
											
											velocity2.set(aim.multiply(-4));
											velocity2.set(hitnorm.multiply(3));

											if(person[j].whichgun==SNIPERRIFLE){

												sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, finalwallhit, velocity2, 10);
												sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, finalwallhit, velocity2, 2);
											}

											if(person[j].whichgun==SHOTGUN){

												sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, finalwallhit, velocity2, 5);
												sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, finalwallhit, velocity2, .8f);
											}

											if(person[j].whichgun==ASSAULTRIFLE){

												sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, finalwallhit, velocity2, 6);
												sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, finalwallhit, velocity2, 1);
											}

											if(person[j].whichgun==HANDGUN1){

												sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, finalwallhit, velocity2, 6);
												sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, finalwallhit, velocity2, 1);
											}

											if(person[j].whichgun==HANDGUN2){

												sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, finalwallhit, velocity2, 6);
												sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, finalwallhit, velocity2, 1);
											}

											float gLoc[] = new float[3];

											gLoc[0]=finalwallhit.getX()/soundscalefactor;
											gLoc[1]=finalwallhit.getY()/soundscalefactor;
											gLoc[2]=finalwallhit.getZ()/soundscalefactor;

											alSourcefv(gSourceID[WALLHITSOUND], AL_POSITION, gLoc);
											alSourcePlay(gSourceID[WALLHITSOUND]);
										}
										
										if(lastshot[0] == null) {
											lastshot[0] = new XYZ(start);
										}
										else {
											lastshot[0].set(start);
										}
										
										if(lastshot[1] == null) {
											lastshot[1] = new XYZ(oldend);
										}
										else {
											lastshot[1].set(oldend);
										}
										
										velocity.set(aim.multiply(8));

										if(person[j].whichgun!=SNIPERRIFLE&&person[j].whichgun!=SHOTGUN&&p==numshots-1)sprites.makeSprite(SMOKE_SPRITE, .3f, 1, 1, 1, start.add(aim.multiply(1.5f)), velocity, 3);
										if(person[j].whichgun==SHOTGUN&&p==numshots-1)sprites.makeSprite(SMOKE_SPRITE, .4f, 1, 1, 1, start.add(aim.multiply(1.5f)), velocity, 5);
										if(person[j].whichgun==SNIPERRIFLE&&!zoom)sprites.makeSprite(SMOKE_SPRITE, .3f, 1, 1, 1, start.add(aim.multiply(2.2f)), velocity, 4);
										if(j!=0||!zoom)sprites.makeSprite(BULLET, .07f, 1, 1, .7f, lastshot[0].add(aim.multiply(1)), lastshot[1], .2f);

										//Nearby bullet whoosh		

										long dot_ta,dot_tb;
										XYZ nearest = new XYZ();

										XYZ a = lastshot[0];
										a.incrementAdd(aim.multiply(1));

										XYZ b = lastshot[1];
										XYZ c = camera.position;

										dot_ta = (long) ((c.getX() - a.getX())*(b.getX() - a.getX()) + (c.getY() - a.getY())*(b.getY() - a.getY()) + (c.getZ() - a.getZ())*(b.getZ() - a.getZ()));
										dot_tb = (long) ((c.getX() - b.getX())*(a.getX() - b.getX()) + (c.getY() - b.getY())*(a.getY() - b.getY()) + (c.getZ() - b.getZ())*(a.getZ() - b.getZ()));

										if (!(dot_ta <= 0)&&!(dot_tb <= 0)){

												nearest.setX(a.getX() + ((b.getX() - a.getX()) * dot_ta)/(dot_ta + dot_tb));
												nearest.setY(a.getY() + ((b.getY() - a.getY()) * dot_ta)/(dot_ta +dot_tb));
												nearest.setZ(a.getZ() + ((b.getZ() - a.getZ()) * dot_ta)/(dot_ta +dot_tb));
										}

										if(nearest.x!=0){

											if(findDistancefast(nearest,camera.position)<10&&(thirdperson==2||j!=0)){

												float gLoc[] = new float[3];

												gLoc[0]=(camera.position.getX()+(nearest.getX()-camera.position.getX()))/soundscalefactor;
												gLoc[1]=(camera.position.getY()+(nearest.getY()-camera.position.getY()))/soundscalefactor;
												gLoc[2]=(camera.position.getZ()+(nearest.getZ()-camera.position.getZ()))/soundscalefactor;

												alSourcefv(gSourceID[NEARBULLETSOUND], AL_POSITION, gLoc);
												alSourcePlay(gSourceID[NEARBULLETSOUND]);
											}
										}
									}
								} 
							}
						}
					}
					
					}
				}
				
				if(!button())oldbutton=false;

				if(lasersight&&person[0].whichgun!=GRENADE){

					for(int j=0;j<numpeople;j++){

						if(person[j].type==PLAYERTYPE){

							if(person[j].aiming){

								//Firing

								XYZ end = new XYZ(), aim = new XYZ(), oldend = new XYZ();
								HitStruct hitstruct = new HitStruct(),temphitstruct =new HitStruct();

								float olddistance = 0;
								float distance;
								int whichhit=-1;

								if(!zoom||j!=0){

									if(person[j].whichgun==ASSAULTRIFLE)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),0,person[j].playerrotation-2.5f,0));
									if(person[j].whichgun==SNIPERRIFLE)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),0,person[j].playerrotation+4,0));
									if(person[j].whichgun==SHOTGUN)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position.subtract(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position),0,person[j].playerrotation+4,0));
									if(person[j].whichgun==HANDGUN1&&thirdperson==0&&j==0)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.65f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.35f))),0,person[j].playerrotation-.9f,0));
									if(person[j].whichgun==HANDGUN1&&(thirdperson!=0||j!=0))aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.35f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.65f))),0,person[j].playerrotation-.9f,0));
									if(person[j].whichgun==HANDGUN2&&thirdperson==0&&j==0)aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.65f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.35f))),0,person[j].playerrotation-.9f,0));
									if(person[j].whichgun==HANDGUN2&&(thirdperson!=0||j!=0))aim.set(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTHAND])].position.subtract(person[j].skeleton.joints[person[j].skeleton.jointlabels[HEAD]].position.multiply(.35f).add(person[j].skeleton.joints[person[j].skeleton.jointlabels[NECK]].position.multiply(.65f))),0,person[j].playerrotation-.9f,0));
								}

								if(zoom&&j==0){
									start.set(camera.position);
									aim.set(facing);
								}

								normalise(aim);

								if(person[j].whichgun==SNIPERRIFLE){
									start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
									start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(0-.4f));
								}

								if(person[j].whichgun==SHOTGUN){
									start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
									start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(0-.4f));
								}

								if(person[j].whichgun==HANDGUN1){
									start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTWRIST])].position,0,person[j].playerrotation,0)));
									start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.55f-.4f));
								}

								if(person[j].whichgun==HANDGUN2){

									start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[RIGHTWRIST])].position,0,person[j].playerrotation,0)));
									start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.55f-.4f));
								}

								if(person[j].whichgun==ASSAULTRIFLE){

									start.set(person[j].playercoords.add(doRotation(person[j].skeleton.joints[(person[j].skeleton.jointlabels[LEFTHAND])].position,0,person[j].playerrotation,0)));
									start.incrementSubtract(doRotation(doRotation(doRotation(aim,0,-person[j].playerrotation,0),90,0,0),0,person[j].playerrotation,0).multiply(.25f-.4f));
								}

								end.set(start.add(aim.multiply(1000)));

								//Blocks

								wallhit.set(0);
								beginx=(int) ((person[j].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING-2);
								if(beginx<0)beginx=0;
								beginz=(int) ((person[j].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING-2);
								if(beginz<0)beginz=0;

								endx=(int) ((person[j].playercoords.getX()+BLOCK_SPACING/2)/BLOCK_SPACING+2);
								if(endx>NUM_BLOCKS-1)endx=NUM_BLOCKS-1;
								endz=(int) ((person[j].playercoords.getZ()+BLOCK_SPACING/2)/BLOCK_SPACING+2);
								if(endz>NUM_BLOCKS-1)endz=NUM_BLOCKS-1;

								if(beginx<endx&&beginz<endz)finalwallhit.set(0);

								for(int i=beginx;i<=endx;i++) {
									for(int j1=beginz;j1<=endz;j1++){

										move.set(0);
										move.setX(i*BLOCK_SPACING);
										move.setZ(j1*BLOCK_SPACING);

										whichtri=blocks[citytype[i][j1]].lineCheck2(start,end,wallhit,move,cityrotation[i][j1]*90);

										if(whichtri!=-1){
											end.set(wallhit);
											finalwallhit.set(wallhit);
										}
									}
								}

								wallhit.set(0);
								wallhit.setX(camera.position.getX());
								wallhit.setZ(camera.position.getZ());
								whichtri=bigStreet.lineCheck2(start,end,wallhit,wallhit,0);

								if(whichtri!=-1){
									end.set(wallhit);
									finalwallhit.set(wallhit);
								}

								//People

								whichhit=-1;

								for(int i=0;i<numpeople;i++){

									if(i!=j&&findDistancefast(person[j].playercoords,person[i].playercoords)<20000){

										temphitstruct.set(person[i].bulletCollideWithPlayer(i, start, end));	

										if(temphitstruct.collision){

											distance=findDistancefast(start,temphitstruct.hitlocation);
											if(distance<olddistance||whichhit==-1){

												end.set(temphitstruct.hitlocation);
												olddistance=distance;
												hitstruct.set(temphitstruct);
												whichhit=i;
											}
										}
									}
								}

								if(aimer[0] == null) aimer[0] = new XYZ(start);
								else aimer[0].set(start);
								
								if(aimer[1] == null) aimer[1] = new XYZ(end);
								else aimer[1].set(end);

								sprites.makeSprite(BULLET_INSTANT, .4f, 1, 0, 0, aimer[0], aimer[1], .2f);

							}

						}
				}
				}

				//Snow

				snowdelay-=multiplier;

				while(snowdelay<0&&environment==SNOWY_ENVIRONMENT){

					snowdelay+=1/precipitationdensity*2;

					velocity.set(0);
					velocity.setY(-5);

					start.set(camera.position);
					start.y+=precipitationvert;
					start.x+=random()%(int)precipitationhorz;
					start.z+=random()%(int)precipitationhorz;

					sprites.makeSprite(SNOW_SPRITE, 1, 1, 1, 1, start, velocity, 1.01f);

				}

				while(snowdelay<0&&environment==RAINY_ENVIRONMENT){

					snowdelay+=1/precipitationdensity/4;
					velocity.set(0);
					velocity.setY(-100);

					start.set(camera.position);
					start.y+=precipitationvert;
					start.x+=random()%(int)precipitationhorz*.5;
					start.z+=random()%(int)precipitationhorz*.5;

					sprites.makeSprite(RAIN_SPRITE, .5f, 1, 1, 1, start, velocity, 2.00f);
				}			

				//GRENADE collision

				int wherex,wherey,whichsound,severwhat;
				boolean impact;

				for(int i=0;i<sprites.howmanysprites;i++){

					if(sprites.type[i]==GRENADE_SPRITE||sprites.type[i]==SPOON_SPRITE||sprites.type[i]==PIN_SPRITE){

						impact=false;

						if(sprites.type[i]!=GRENADE_SPRITE){
							sprites.brightness[i]-=multiplier*.2;
						}

								

						if(findLengthfast(sprites.velocity[i])>0){

							wherex=(int) ((sprites.location[i].getX()+BLOCK_SPACING/2)/BLOCK_SPACING);
							wherey=(int) ((sprites.location[i].getZ()+BLOCK_SPACING/2)/BLOCK_SPACING);

							move.set(0);

							move.setX(wherex*BLOCK_SPACING);
							move.setZ(wherey*BLOCK_SPACING);

							whichtri=blocks[citytype[wherex][wherey]].lineCheck2(sprites.oldlocation[i],sprites.location[i],wallhit,move,cityrotation[wherex][wherey]*90);

							if(whichtri!=-1){
								
								impact=true;
								normalrotated.set(doRotation(blocks[citytype[wherex][wherey]].normals[whichtri],0,cityrotation[wherex][wherey]*90,0));

								if(sprites.size[i]>1)decals.makeDecal(CRATER, wallhit,9,normalrotated, whichtri, blocks[citytype[wherex][wherey]], move, cityrotation[wherex][wherey]*90);
								sprites.location[i].set(wallhit.add(normalrotated.multiply(.02f)));

								reflectVector(sprites.velocity[i],normalrotated);
								sprites.velocity[i].incrementMultiply(.3f);

								if(sprites.type[i]==GRENADE_SPRITE){

									float gLoc[] = new float[3];

									gLoc[0]=(((sprites.location[i].getX())-camera.position.getX())/findLengthfast(sprites.velocity[i])*5+camera.position.getX())/soundscalefactor;
									gLoc[1]=(((sprites.location[i].getY())-camera.position.getY())/findLengthfast(sprites.velocity[i])*5+camera.position.getY())/soundscalefactor;
									gLoc[2]=(((sprites.location[i].getZ())-camera.position.getZ())/findLengthfast(sprites.velocity[i])*5+camera.position.getZ())/soundscalefactor;

									whichsound=abs(random()%2);

									alSourcefv(gSourceID[BOUNCESOUND+whichsound], AL_POSITION, gLoc);

									if(sprites.size[i]<=1)alSourcePlay(gSourceID[BOUNCESOUND+whichsound]);
								}

								if(findLengthfast(sprites.velocity[i])<=10)sprites.velocity[i].set(0);

							}

							if(sprites.location[i].getY()<0){

								impact=true;
								sprites.velocity[i].y*=-1;
								sprites.velocity[i].incrementMultiply(.3f);
								sprites.location[i].setY(0);

								if(sprites.type[i]==GRENADE_SPRITE){

									if(sprites.size[i]>1){

										move.set(0);
										sprites.location[i].setY(-.5f);
										XYZ normish = new XYZ();
										normish.setY(1);
										decals.makeDecal(CRATER, sprites.location[i],9,normish, 0, blocks[citytype[wherex][wherey]], move, 0);
									}

									float gLoc[] = new float[3];

									gLoc[0]=(((sprites.location[i].getX())-camera.position.getX())/findLengthfast(sprites.velocity[i])*5+camera.position.getX())/soundscalefactor;
									gLoc[1]=(((sprites.location[i].getY())-camera.position.getY())/findLengthfast(sprites.velocity[i])*5+camera.position.getY())/soundscalefactor;
									gLoc[2]=(((sprites.location[i].getZ())-camera.position.getZ())/findLengthfast(sprites.velocity[i])*5+camera.position.getZ())/soundscalefactor;

									whichsound=abs(random()%2);
									alSourcefv(gSourceID[BOUNCESOUND+whichsound], AL_POSITION, gLoc);

									if(sprites.size[i]<=1)alSourcePlay(gSourceID[BOUNCESOUND+whichsound]);			
								}

								if(findLengthfast(sprites.velocity[i])<=10)sprites.velocity[i].set(0);
							}
							
							if(sprites.type[i]==GRENADE_SPRITE&&findLengthfast(sprites.velocity[i])>20){

								HitStruct hitstruct;

								for(int j=0;j<numpeople;j++){

									if((j!=0||sprites.brightness[i]<.9)&&person[j].existing){

										hitstruct=person[j].bulletCollideWithPlayer(j, sprites.oldlocation[i], sprites.location[i]);	

										if(hitstruct.collision){

											impact=true;

											sprites.location[i].set(hitstruct.hitlocation);

											float gLoc[] = new float[3];

											gLoc[0]=(((sprites.location[i].getX())-camera.position.getX())/findLengthfast(sprites.velocity[i])*5+camera.position.getX())/soundscalefactor;
											gLoc[1]=(((sprites.location[i].getY())-camera.position.getY())/findLengthfast(sprites.velocity[i])*5+camera.position.getY())/soundscalefactor;
											gLoc[2]=(((sprites.location[i].getZ())-camera.position.getZ())/findLengthfast(sprites.velocity[i])*5+camera.position.getZ())/soundscalefactor;

											if(!person[j].skeleton.free){

											if((hitstruct.joint1.label==HEAD||hitstruct.joint2.label==HEAD)&&person[j].type!=ZOMBIETYPE){

												alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);

												if(sprites.size[i]<=1)alSourcePlay(gSourceID[HEADWHACKSOUND]);

												person[j].skeleton.free=true;

												person[j].longdead=1;

												for(int k=0;k<person[j].skeleton.num_joints;k++){

													person[j].skeleton.joints[k].position.set(doRotation(person[j].skeleton.joints[k].position,0,person[j].playerrotation,0));
													person[j].skeleton.joints[k].position.incrementAdd(person[j].playercoords);
													person[j].skeleton.joints[k].realoldposition.set(person[j].skeleton.joints[k].position);
													person[j].skeleton.joints[k].velocity.set(person[j].velocity);
													person[j].skeleton.joints[k].velocity.x+=abs(random()%10)-5;
													person[j].skeleton.joints[k].velocity.y+=abs(random()%10)-5;
													person[j].skeleton.joints[k].velocity.z+=abs(random()%10)-5;
												}

												hitstruct.joint1.velocity.incrementAdd(sprites.velocity[i]);
												hitstruct.joint2.velocity.incrementAdd(sprites.velocity[i]);

												if(person[j].type==CIVILIANTYPE)civkills++;
												if(person[j].type==EVILTYPE)goodkills++;

											}else{

												float totalarea = 0;
												alSourcefv(gSourceID[BODYWHACKSOUND], AL_POSITION, gLoc);

												if(sprites.size[i]<=1)alSourcePlay(gSourceID[BODYWHACKSOUND]);
												person[j].skeleton.offset=1;

												for(int k=0;k<person[j].skeleton.num_joints;k++){

													if(findDistancefast(doRotation(person[j].skeleton.joints[k].position,0,person[j].playerrotation,0).add(person[j].playercoords),hitstruct.hitlocation)<200){
														totalarea+=(200/findDistancefast(doRotation(person[j].skeleton.joints[k].position,0,person[j].playerrotation,0).add(person[j].playercoords),hitstruct.hitlocation));
													}
												}

												float offsetlength;

												for(int k=0;k<person[j].skeleton.num_joints;k++){

													if(findDistancefast(doRotation(person[j].skeleton.joints[k].position,0,person[j].playerrotation,0).add(person[j].playercoords),hitstruct.hitlocation)<200){
														person[j].skeleton.joints[k].offset.incrementAdd(doRotation(sprites.velocity[i].multiply(.1f).multiply((200/findDistancefast(doRotation(person[j].skeleton.joints[k].position,0,person[j].playerrotation,0).add(person[j].playercoords),hitstruct.hitlocation))/totalarea*10),0,-person[j].playerrotation,0));
													}

													offsetlength=findLengthfast(person[j].skeleton.joints[k].offset);

													if(offsetlength>9){

														normalise(person[j].skeleton.joints[k].offset);
														person[j].skeleton.joints[k].offset.incrementMultiply(3);
													}
												}
											}}
											sprites.velocity[i].incrementMultiply(-.3f);
										}
									}
								}
							}
							sprites.oldlocation[i].set(sprites.location[i]);
						}

						//Explode

						if(sprites.type[i]==GRENADE_SPRITE){

							sprites.brightness[i]-=multiplier*.3;

							if(sprites.brightness[i]<=0||(impact&&sprites.size[i]>1)){

								sprites.brightness[i]=0;
								sprites.makeSprite(BLOOD_SPRITE_DOWN, 1, 1, 1, 1, sprites.location[i], facing.multiply(0), 60);
								sprites.makeSprite(MUZZLEFLASH_SPRITE, 1, 1, 1, 1, sprites.location[i], facing.multiply(0), 9);

								float gLoc[] = new float[3];

								gLoc[0]=(((sprites.location[i].getX())-camera.position.getX())/3+camera.position.getX())/soundscalefactor;
								gLoc[1]=(((sprites.location[i].getY())-camera.position.getY())/3+camera.position.getY())/soundscalefactor;
								gLoc[2]=(((sprites.location[i].getZ())-camera.position.getZ())/3+camera.position.getZ())/soundscalefactor;

								alSourcefv(gSourceID[EXPLOSIONSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[EXPLOSIONSOUND]);

								XYZ relation = new XYZ();

								camerashake=1-findDistance(person[0].playercoords,sprites.location[i])/200;

								//if(!sprites.size[i]>1){

									overpoint.set(sprites.location[i]);
									overpoint.y+=3000;
									underpoint.set(sprites.location[i]);
									underpoint.y-=3000;
									move.set(0);

									wherex=(int) ((sprites.location[i].getX()+BLOCK_SPACING/2)/BLOCK_SPACING);
									wherey=(int) ((sprites.location[i].getZ()+BLOCK_SPACING/2)/BLOCK_SPACING);
									move.setX(wherex*BLOCK_SPACING);
									move.setZ(wherey*BLOCK_SPACING);

									XYZ temp3 = new XYZ();
									
									whichtri=sidewalkcollide.lineCheck2(overpoint,underpoint,temp3,move,cityrotation[wherex][wherey]*90);

									XYZ normish = new XYZ();

									normish.setY(1);

									if(whichtri>=0){
										decals.makeDecal(CRATER, sprites.location[i],9,normish, 0, sidewalkcollide, move, cityrotation[wherex][wherey]*90);
									}

									if(whichtri==-1){
										temp3.set(sprites.location[i]);
										temp3.setY(-.5f);

										move.set(0);
										decals.makeDecal(CRATER, sprites.location[i],9,normish, 0, sidewalkcollide, move, 0);
									}

								//}

								for(int k=0;k<numpeople;k++){		

									if(person[k].existing&&(person[k].longdead!=-1||!person[k].skeleton.free)){		
									if((findDistancefast(person[k].playercoords,sprites.location[i])<700&&!person[k].skeleton.free)||(findDistancefast(person[k].averageloc,sprites.location[i])<700&&person[k].skeleton.free)){
									if(!person[k].skeleton.free){
									if(person[k].type==CIVILIANTYPE)civkills++;
									if(person[k].type==EVILTYPE)goodkills++;

									person[k].skeleton.free=true;
									person[k].killtargetvisible=false;

									if((findDistancefast(person[k].playercoords,sprites.location[i])<600&&!person[k].skeleton.free)||(findDistancefast(person[k].averageloc,sprites.location[i])<600&&person[k].skeleton.free)||person[k].type==PLAYERTYPE){
										person[k].health-=100;
										person[k].bleeding=1;
									}

									person[k].doAnimations(k);
									person[k].longdead=1;

									person[k].bleeddelay=1;
									person[k].bjoint1.set(person[k].skeleton.joints[person[k].skeleton.jointlabels[HEAD]]);
									person[k].bjoint2.set(person[k].skeleton.joints[person[k].skeleton.jointlabels[NECK]]);

									for(int j=0;j<person[k].skeleton.num_joints;j++){

										person[k].skeleton.joints[j].position.set(doRotation(person[k].skeleton.joints[j].position,0,person[k].playerrotation,0));
										person[k].skeleton.joints[j].position.incrementAdd(person[k].playercoords);
										person[k].skeleton.joints[j].realoldposition.set(person[k].skeleton.joints[j].position);
										person[k].skeleton.joints[j].velocity.set(doRotation(person[k].skeleton.joints[j].velocity,0,person[k].playerrotation,0));
										person[k].skeleton.joints[j].velocity.incrementAdd(person[k].velocity);
										person[k].skeleton.joints[j].velocity.x+=abs(random()%20)-10;
										person[k].skeleton.joints[j].velocity.y+=abs(random()%20)-10;
										person[k].skeleton.joints[j].velocity.z+=abs(random()%20)-10;

									}}

									for(int j=0;j<person[k].skeleton.num_joints;j++){

										relation.set(person[k].skeleton.joints[j].position.subtract(sprites.location[i]));
										normalise(relation);

										if(findDistance(person[k].skeleton.joints[j].position,sprites.location[i])>1)person[k].skeleton.joints[j].velocity.incrementAdd(relation.devide(findDistance(person[k].skeleton.joints[j].position,sprites.location[i])*300));
										else person[k].skeleton.joints[j].velocity.incrementAdd(relation.multiply(300));
									}

									person[k].longdead=1;

									for(int j=0;j<person[k].skeleton.num_joints;j++){

										//Sever stuff

										if(findLengthfast(person[k].skeleton.joints[j].velocity)>1500&&person[k].skeleton.joints[j].existing&&abs(random()%3)!=1){
											sprites.makeSprite(BLOOD_SPRITE_DOWN, .8f, 1, .2f, .2f,person[k].skeleton.joints[j].position, person[k].skeleton.joints[j].velocity.devide(3), 9);
											person[k].skeleton.deleteJoint(j);
											person[k].skeleton.broken=2;
											person[k].health=-10000;
											person[k].skeleton.joints[j].existing=false;
										}

									}

									}

									}

								}

											

							}

						}
					}
				}

			//camera shake
		
			camerashake-=multiplier;
			if(camerashake<0)camerashake=0;
		
			//camera position
		
			XYZ average = new XYZ();
			
			if(!zoom)average.set(person[0].skeleton.joints[(person[0].skeleton.jointlabels[HEAD])].position.multiply(person[0].aimamount/2+.5f).add(person[0].skeleton.joints[(person[0].skeleton.jointlabels[NECK])].position.multiply((1-person[0].aimamount)/2)));			
			if(zoom)average.set(person[0].skeleton.joints[(person[0].skeleton.jointlabels[RIGHTHAND])].position);
		
			if(!person[0].skeleton.free&&thirdperson!=2)camera.position.set(person[0].playercoords.add(doRotation(average,0,person[0].playerrotation,0)));
			if(person[0].skeleton.free&&thirdperson!=2)camera.position.set(average);	
		
			//Restraints
		
			if(camera.position.getY()<.1)camera.position.setY(.1f);
		
			if(thirdperson!=2){
				oldrot=camera.visrotation;
				oldrot2=camera.visrotation2;
			}
		
			
		
			//Kill count
		
			for(int i=0;i<numpeople;i++){
		
				if(person[i].oldhealth>0&&person[i].health<=0){
		
					if(i==1)alSourcePlay(gSourceID[LOSESOUND]);	
		
					if(person[i].type==CIVILIANTYPE){
		
						alSourcePlay(gSourceID[DISGUISEKILLSOUND]);
						score-=300;	
					}
		
					if(person[i].type==EVILTYPE){
		
						alSourcePlay(gSourceID[SOULINSOUND]);	
						score+=75;
						if(person[i].whichgun==KNIFE)score+=50;
					}
					person[i].firstlongdead=false;
				}
				person[i].oldhealth=person[i].health;
			}
		
			if(slomo==2){
		
				psychicpower-=multiplier*15;
		
				if(psychicpower<0){
		
					soundscalefactor=SOUNDSCALEFACTORDEFAULT;
					alSourceStop(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
					alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);
					if(person[0].whichgun==KNIFE)whichsong=KNIFESONG;
					if(person[0].whichgun!=KNIFE)whichsong=SHOOTSONG;
					if(type==ZOMBIE_TYPE)whichsong=ZOMBIESONG;
					alSourcef(gSourceID[whichsong], AL_PITCH, 1);
					alSourcePlay(gSourceID[whichsong]);
					alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
					alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);
					slomo=0;
					alSourcePlay(gSourceID[SOULINSOUND]);
					psychicpower=0;
					flashamount=.5f;
					flashr=1;flashg=1;flashb=1;
		
				}
		
			}
		
			psychicpower+=multiplier*5;
			if(psychicpower>10)psychicpower=10;
		
			//3d sound
		
			int tempInt;
			float gLoc[] = new float[3];
		
			gLoc[0]=camera.position.getX()/soundscalefactor;
			gLoc[1]=camera.position.getY()/soundscalefactor;
			gLoc[2]=camera.position.getZ()/soundscalefactor;
		
			//Set listener position
		
			alListenerfv(AL_POSITION, gLoc);
		
			//Set orientation with forward and up vectors
		
			XYZ upvector = new XYZ();
			upvector.setZ(-1);
	
			upvector.set(doRotation(upvector,-camera.rotation2+90,0,0));
			upvector.set(doRotation(upvector,0,0-camera.rotation,0));
	
			float ori[] = new float[6];
		   	ori[0] = -facing.getX();
		   	ori[1] = facing.getY();
		   	ori[2] = facing.getZ();
		   	ori[3] = -upvector.getX();
		   	ori[4] = upvector.getY();
		   	ori[5] = upvector.getZ();
			alListenerfv(AL_ORIENTATION, ori);
	
			if(person[0].currentanimation==THROWANIM||person[0].currentanimation==DIVEANIM||paused){
				moveMouse(oldmouseloc);
				getMouse(mouseloc);
			}
			
			oldmouseloc.set(mouseloc);
			if(score<0)score=0;

			
			}
		}
		
	
	}
}
	


