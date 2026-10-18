package blackshades;

import blackshades.Quaternions.XYZ;
import blackshades.Skeleton.*;

import static org.lwjgl.opengl.GL11.*;

import java.nio.IntBuffer;

import static blackshades.Quaternions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.BufferUtils.*;
import static blackshades.Constants.*;
import static blackshades.Game.window;
import static blackshades.Skeleton.*;
import static blackshades.Sprites.*;
import static blackshades.Globals.*;
import static blackshades.Support.*;
import static java.lang.Math.*;
import static blackshades.Support.random;

public class Person {
	
	public static final int 
		PLAYERTYPE = 0,
		CIVILIANTYPE = 1,
		EVILTYPE = 2,
		VIPTYPE = 3,
		EVILSNIPERTYPE = 4,
		EVILASSULTRIFLETYPE = 5,
		ZOMBIETYPE = 6;
	
	public static class HitStruct{
		
		public Joint joint1;
		public Joint joint2;
		public XYZ hitlocation;
		public boolean collision;
		
		public HitStruct() {
			joint1 = new Joint();
			joint2 = new Joint();
			hitlocation = new XYZ();
			collision = false;
		}
		
		public HitStruct(HitStruct hitstruct) {
			joint1 = hitstruct.joint1;
			joint2 = hitstruct.joint2;
			hitlocation = new XYZ(hitstruct.hitlocation);
			collision = hitstruct.collision;
		}
		
		public void set(HitStruct hitstruct) {
			joint1 = hitstruct.joint1;
			joint2 = hitstruct.joint2;
			hitlocation.set(hitstruct.hitlocation);
			collision = hitstruct.collision;
		}
	}
	
	public int eaten;
	public int currentframe;
	public int targetframe;
	public int currentanimation;
	public int targetanimation;
	public float target;
	public float playerhealth;
	public int modelnum;
	public XYZ oldplayercoords;
	public XYZ playercoords;
	public XYZ playervelocity;
	public float playerrotation;
	public float playerrotation2;
	public float playerlowrotation;
	public float pathcheckdelay;
	public boolean onground;
	public boolean backwardsanim;
	public XYZ facing;
	public XYZ velocity;
	public float shotdelay;
	public boolean litup;
	
	public boolean existing;
	
	public int type;
	
	public int whichcostume;
	
	public Skeleton skeleton;
	public Animation tempanimation;
	
	public boolean freshshootkey;
	public boolean freshkickkey;
	public int bufferattack;
	public int jump_key;
	public int left_key;
	public int right_key;
	public int duck_key;
	public int shoot_key;
	public int kick_key;
	public int block_key;
	
	public float speed;
	public boolean aiming;
	public int grenphase;
	public float grenamount;
	public float aimamount;
	public float speedmult;
	public float pathsize;
	
	public int pathnum;
	public int oldpathnum;
	public int oldoldpathnum;
	public int oldoldoldpathnum;
	public XYZ pathtarget;
	public int whichblockx;
	public int whichblocky;
	
	public boolean dead;
	
	public XYZ averageloc;
	public XYZ oldaverageloc;
	
	public float lastdistancevictim;
	
	public boolean firstlongdead;
	public float longdead;
	
	public Joint bjoint1,bjoint2;
	public float bleeding;
	public float bleeddelay;
	
	public float attacktarget;
	public int attackframe;
	public int killtarget;
	public boolean killtargetvisible;
	
	public float gunrotate1, gunrotate2, gunrotate3;
	public float recoil;
	public int whichgun;
	public float oldhealth;
	public float health;
	public float maxhealth;
	public float reloading;
	public int ammo;
	public int reloads[] = new int[10];
	public boolean running;
	public boolean onpath;
	
	public int whichsound;
	public float gLoc[] = new float[3];
	
	public XYZ start, end;
	
	public XYZ tempbulletloc[] = new XYZ[2];
	public XYZ collisionpoint;
	public float M[] = new float[16];
	public int collide;
	public XYZ average;
	public int howmany;
	public float distancemax;
	public HitStruct hitstruct;
	public XYZ soundpoint;
	public XYZ facingdown;
	public XYZ facinghalf;
	public XYZ facingright;
	public XYZ rotatearound;
	public XYZ oldpos;
	public XYZ soundsource;
	public IntBuffer tempint;
	public XYZ temppoint1,temppoint2,tempforward;
	public float distance;
	public float LightAmbient[]=	{ 0,0,0, 1.0f};
	public float LightDiffuse[]=	{ 1,1,1, 1.0f };
	public XYZ lightpoint;
	public float LightPosition[] = new float[4];
	public XYZ normal;


	
	public Person() {
		playercoords = new XYZ();
		playervelocity = new XYZ();
		oldplayercoords = new XYZ();
		pathtarget = new XYZ();
		averageloc = new XYZ();
		oldaverageloc = new XYZ();
		facing = new XYZ();
		bjoint1 = new Joint();
		bjoint2 = new Joint();
		tempanimation = new Animation();
		start = new XYZ();
		end = new XYZ();
		collisionpoint = new XYZ();
		average = new XYZ();
		hitstruct = new HitStruct();
		tempbulletloc[0] = new XYZ();
		tempbulletloc[1] = new XYZ();
		soundpoint = new XYZ();
		facingdown = new XYZ();
		facinghalf = new XYZ();
		facingright = new XYZ();
		rotatearound = new XYZ();
		oldpos = new XYZ();
		soundsource = new XYZ();
		tempint = createIntBuffer(3);
		temppoint1 = new XYZ();
		temppoint2 = new XYZ();
		tempforward = new XYZ();
		lightpoint = new XYZ();
		normal = new XYZ();
	}
	
	
	public HitStruct bulletCollideWithPlayer(int who, XYZ start, XYZ end){
		
		hitstruct.collision=false;
		distancemax = 0;
		collide = 0;
		collisionpoint.set(0);
		
		this.start.set(start);		
		this.end.set(end);
				
		//Make bounding sphere
		howmany=0;
		for(int j=0;j<skeleton.num_joints;j++){
			average.setX(average.getX()+skeleton.joints[j].position.getX());
			average.setY(average.getY()+skeleton.joints[j].position.getY());
			average.setZ(average.getZ()+skeleton.joints[j].position.getZ());
			howmany++;
		}
		average.set(average.devide(howmany));
		distancemax=0;
		for(int j=0;j<skeleton.num_joints;j++){
			if(findDistancefast(average,skeleton.joints[j].position)>distancemax){
				distancemax=findDistancefast(average,skeleton.joints[j].position);
			}
		}
		distancemax=fast_sqrt(distancemax);
		//Collide with player
		if(!skeleton.free){
			this.start.set(this.start.subtract(playercoords));
			this.end.set(this.end.subtract(playercoords));
			if(playerrotation!=0f)this.start.set(doRotation(this.start,0,-playerrotation,0));
			if(playerrotation!=0f)this.end.set(doRotation(this.end,0,-playerrotation,0));
		}
		
		tempbulletloc[0].set(this.start);
		tempbulletloc[1].set(this.end);
		
		if(sphere_line_intersection(tempbulletloc[0].getX(),tempbulletloc[0].getY(),tempbulletloc[0].getZ(),
									tempbulletloc[1].getX(),tempbulletloc[1].getY(),tempbulletloc[1].getZ(),
									average.getX(), average.getY(), average.getZ(), distancemax)){
		for(int j=0;j<skeleton.num_joints;j++){
			if(skeleton.joints[j].hasparent&&skeleton.joints[j].visible){
				tempbulletloc[0].set(this.start);
				tempbulletloc[1].set(this.end);
				glPushMatrix();
					glLoadIdentity();
					glScalef(1,1/skeleton.joints[j].length,1);
					glRotatef(skeleton.joints[j].rotate2-90,0,0,1);
					glRotatef(skeleton.joints[j].rotate1-90,0,1,0);
					glTranslatef(	(-(skeleton.joints[j].position.getX()+skeleton.joints[j].parent.position.getX())/2),
									(-(skeleton.joints[j].position.getY()+skeleton.joints[j].parent.position.getY())/2),
									(-(skeleton.joints[j].position.getZ()+skeleton.joints[j].parent.position.getZ())/2));
					glTranslatef(tempbulletloc[0].getX(),tempbulletloc[0].getY(),tempbulletloc[0].getZ());
					glGetFloatv(GL_MODELVIEW_MATRIX,M);
					tempbulletloc[0].setX(M[12]);
					tempbulletloc[0].setY(M[13]);
					tempbulletloc[0].setZ(M[14]);
				glPopMatrix();
				glPushMatrix();
					glLoadIdentity();
					glScalef(1,1/skeleton.joints[j].length,1);
					glRotatef(skeleton.joints[j].rotate2-90,0,0,1);
					glRotatef(skeleton.joints[j].rotate1-90,0,1,0);
					glTranslatef(	(-(skeleton.joints[j].position.getX()+skeleton.joints[j].parent.position.getX())/2),
									(-(skeleton.joints[j].position.getY()+skeleton.joints[j].parent.position.getY())/2),
									(-(skeleton.joints[j].position.getZ()+skeleton.joints[j].parent.position.getZ())/2));
					glTranslatef(tempbulletloc[1].getX(),tempbulletloc[1].getY(),tempbulletloc[1].getZ());
					glGetFloatv(GL_MODELVIEW_MATRIX,M);
					tempbulletloc[1].setX(M[12]);
					tempbulletloc[1].setY(M[13]);
					tempbulletloc[1].setZ(M[14]);
				glPopMatrix();
				collide=skeletonmodels[skeleton.joints[j].modelnum].lineCheck(tempbulletloc[0],tempbulletloc[1],collisionpoint);
				if(collide!=-1)
				{
					glPushMatrix();
						glLoadIdentity();
						glTranslatef(	(skeleton.joints[j].position.getX()+skeleton.joints[j].parent.position.getX())/2,
										(skeleton.joints[j].position.getY()+skeleton.joints[j].parent.position.getY())/2,
										(skeleton.joints[j].position.getZ()+skeleton.joints[j].parent.position.getZ())/2);
						glRotatef(-skeleton.joints[j].rotate1+90,0,1,0);
						glRotatef(-skeleton.joints[j].rotate2+90,0,0,1);
						glScalef(1,skeleton.joints[j].length,1);
						glTranslatef(collisionpoint.getX(),collisionpoint.getY(),collisionpoint.getZ());
						glGetFloatv(GL_MODELVIEW_MATRIX,M);
						collisionpoint.setX(M[12]);
						collisionpoint.setY(M[13]);
						collisionpoint.setZ(M[14]);
					glPopMatrix();
					hitstruct.collision=true;
					hitstruct.hitlocation.set(collisionpoint);
					hitstruct.joint1.set(skeleton.joints[j]);
					hitstruct.joint2.set(skeleton.joints[j].parent);
				}
			}
		}
		for(int j=0;j<skeleton.num_muscles;j++){
			if(skeleton.muscles[j].visible){
				tempbulletloc[0].set(this.start);
				tempbulletloc[1].set(this.end);
				glPushMatrix();
					glLoadIdentity();
					glScalef(1,1/skeleton.muscles[j].length,1);
					glRotatef(skeleton.muscles[j].rotate3,0,1,0);
					glRotatef(skeleton.muscles[j].rotate2-90,0,0,1);
					glRotatef(skeleton.muscles[j].rotate1-90,0,1,0);
					glTranslatef(	(-(skeleton.muscles[j].parent1.position.getX()+skeleton.muscles[j].parent2.position.getX())/2),
									(-(skeleton.muscles[j].parent1.position.getY()+skeleton.muscles[j].parent2.position.getY())/2),
									(-(skeleton.muscles[j].parent1.position.getZ()+skeleton.muscles[j].parent2.position.getZ())/2));

					glTranslatef(tempbulletloc[0].getX(),tempbulletloc[0].getY(),tempbulletloc[0].getZ());
					glGetFloatv(GL_MODELVIEW_MATRIX,M);
					tempbulletloc[0].setX(M[12]);
					tempbulletloc[0].setY(M[13]);
					tempbulletloc[0].setZ(M[14]);
				glPopMatrix();
				glPushMatrix();
					glLoadIdentity();
					glScalef(1,1/skeleton.muscles[j].length,1);
					glRotatef(skeleton.muscles[j].rotate3,0,1,0);
					glRotatef(skeleton.muscles[j].rotate2-90,0,0,1);
					glRotatef(skeleton.muscles[j].rotate1-90,0,1,0);
					
					glTranslatef(	(-(skeleton.muscles[j].parent1.position.getX()+skeleton.muscles[j].parent2.position.getX())/2),
									(-(skeleton.muscles[j].parent1.position.getY()+skeleton.muscles[j].parent2.position.getY())/2),
									(-(skeleton.muscles[j].parent1.position.getZ()+skeleton.muscles[j].parent2.position.getZ())/2));
					glTranslatef(tempbulletloc[1].getX(),tempbulletloc[1].getY(),tempbulletloc[1].getZ());
					glGetFloatv(GL_MODELVIEW_MATRIX,M);
					tempbulletloc[1].setX(M[12]);
					tempbulletloc[1].setY(M[13]);
					tempbulletloc[1].setZ(M[14]);
				glPopMatrix();
				collide=skeletonmodels[skeleton.muscles[j].parent1.modelnum].lineCheck(tempbulletloc[0],tempbulletloc[1],collisionpoint);
				if(collide!=-1)
				{
					glPushMatrix();
						glLoadIdentity();
						glTranslatef(	(skeleton.muscles[j].parent1.position.getX()+skeleton.muscles[j].parent2.position.getX())/2,
										(skeleton.muscles[j].parent1.position.getY()+skeleton.muscles[j].parent2.position.getY())/2,
										(skeleton.muscles[j].parent1.position.getZ()+skeleton.muscles[j].parent2.position.getZ())/2);
						glRotatef(-skeleton.muscles[j].rotate1+90,0,1,0);
						glRotatef(-skeleton.muscles[j].rotate2+90,0,0,1);
						glRotatef(-skeleton.muscles[j].rotate3,0,1,0);
						glScalef(1,findDistance(skeleton.muscles[j].parent1.position,skeleton.muscles[j].parent2.position),1);
						glTranslatef(collisionpoint.getX(),collisionpoint.getY(),collisionpoint.getZ());
						glGetFloatv(GL_MODELVIEW_MATRIX,M);
						collisionpoint.setX(M[12]);
						collisionpoint.setY(M[13]);
						collisionpoint.setZ(M[14]);
					glPopMatrix();
					hitstruct.collision=true;
					hitstruct.hitlocation.set(collisionpoint);
					hitstruct.joint1.set(skeleton.muscles[j].parent1);
					hitstruct.joint2.set(skeleton.muscles[j].parent2);
				}
			}
		}
		}
		if(!skeleton.free){
			if(playerrotation!=0)hitstruct.hitlocation.set(doRotation(hitstruct.hitlocation,0,playerrotation,0));
			hitstruct.hitlocation.set(hitstruct.hitlocation.add(playercoords));
		}
		return new HitStruct(hitstruct);
	}
	
	public void doAnimations(int who){	
		
		if(target>1&&!skeleton.free){
			//Footstep sounds
			if(who==0&&slomo==0&&(targetanimation==JOGANIM||targetanimation==WALKANIM)&&(targetframe==0||targetframe==8)&&visions==0&&(onground||abs(velocity.getY())<1)){
				gLoc[0]=playercoords.getX()/soundscalefactor;
				gLoc[1]=playercoords.getY()/soundscalefactor;
				gLoc[2]=playercoords.getZ()/soundscalefactor;
				whichsound=FOOTSTEPSOUND+abs(random())%5;
				alSourcefv(gSourceID[whichsound], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[whichsound]);
			}
			if(targetanimation==ZOMBIEEATANIM&&(targetframe==3)){
				soundpoint.set(doRotation(skeleton.joints[skeleton.jointlabels[HEAD]].position,0,playerrotation,0).add(playercoords));
				gLoc[0]=soundpoint.getX()/soundscalefactor;
				gLoc[1]=soundpoint.getY()/soundscalefactor;
				gLoc[2]=soundpoint.getZ()/soundscalefactor;
				alSourcefv(gSourceID[BODYHITSOUND], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[BODYHITSOUND]);
				bleeding=1;
				bleeddelay=1;
				//OJO
				//bjoint1=skeleton.joints[skeleton.jointlabels[HEAD]];
				bjoint1.set(skeleton.joints[skeleton.jointlabels[HEAD]]);
				//bjoint2=skeleton.joints[skeleton.jointlabels[NECK]];
				bjoint2.set(skeleton.joints[skeleton.jointlabels[NECK]]);
			}
			targetframe=currentframe;
			currentanimation=targetanimation;
			if(!backwardsanim){targetframe++;
			if(targetframe>animation[currentanimation].numframes-1)targetframe=0;}
			if(backwardsanim){targetframe--;
			if(targetframe<0)targetframe=animation[currentanimation].numframes-1;}
			target=0;
			if((currentanimation==GETUPFRONTANIM||currentanimation==GETUPBACKANIM)&&targetframe==0){
				targetanimation=IDLEANIM;
			}
			if(targetanimation==DIVEANIM&&currentanimation==DIVEANIM&&targetframe==0){
				targetanimation=GETUPFRONTANIM;
				soundpoint.set(doRotation(skeleton.joints[skeleton.jointlabels[HEAD]].position,0,playerrotation,0).add(playercoords));
				gLoc[0]=soundpoint.getX()/soundscalefactor;
				gLoc[1]=soundpoint.getY()/soundscalefactor;
				gLoc[2]=soundpoint.getZ()/soundscalefactor;
				alSourcefv(gSourceID[BODYWHACKSOUND], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[BODYWHACKSOUND]);
			}
			if(currentanimation==THROWANIM&&targetframe==0){
				targetanimation=IDLEANIM;
			}
			if(currentanimation==THROWNANIM&&targetframe==0){
				skeleton.offset=0;
				skeleton.free=true;
				longdead=1;
				for(int j=0;j<skeleton.num_joints;j++){
					skeleton.joints[j].position.incrementAdd(skeleton.joints[j].offset);
					skeleton.joints[j].position.set(doRotation(skeleton.joints[j].position,0,playerrotation,0));
					skeleton.joints[j].position.incrementAdd(playercoords);
					skeleton.joints[j].realoldposition.set(skeleton.joints[j].position);
					skeleton.joints[j].velocity.set(doRotation(skeleton.joints[j].velocity,0,playerrotation,0));
				}
			}
		}
		
		if(!skeleton.free){
			if(currentanimation!=LYINGANIM){
				if(animation[targetanimation].speed[currentframe]>animation[currentanimation].speed[currentframe])
					target+=multiplier*animation[targetanimation].speed[currentframe]*speed;
				if(animation[targetanimation].speed[currentframe]<=animation[currentanimation].speed[currentframe])
					target+=multiplier*animation[currentanimation].speed[currentframe]*speed;
			}
			if(currentanimation==LYINGANIM){
				target+=multiplier*animation[targetanimation].speed[targetframe]*speed;
			}		
			if(((currentanimation==CROUCHANIM)&&(targetanimation!=CROUCHANIM))||((currentanimation!=CROUCHANIM)&&(targetanimation==CROUCHANIM)))target+=multiplier*animation[CROUCHANIM].speed[0]*2;
			if(currentanimation==IDLEANIM&&targetanimation==IDLEANIM)target-=multiplier*animation[IDLEANIM].speed[0]/2;
			
			if(target>1)currentframe=targetframe;
			
			for(int i=0;i<skeleton.num_joints;i++){
				if(currentanimation!=LYINGANIM){
					skeleton.joints[i].velocity.set((animation[currentanimation].position[i][currentframe].multiply(1-target).add(animation[targetanimation].position[i][targetframe].multiply(target)).subtract(skeleton.joints[i].position)).devide((float)multiplier));
					skeleton.joints[i].position.set(animation[currentanimation].position[i][currentframe].multiply(1-target).add(animation[targetanimation].position[i][targetframe].multiply(target)));
					if(skeleton.joints[i].hasparent&&skeleton.joints[i].visible)
					{
						skeleton.joints[i].rotate1=animation[currentanimation].rotate1[i][currentframe]*(1-target)+animation[targetanimation].rotate1[i][targetframe]*(target);
						skeleton.joints[i].rotate2=animation[currentanimation].rotate2[i][currentframe]*(1-target)+animation[targetanimation].rotate2[i][targetframe]*(target);
						skeleton.joints[i].rotate3=animation[currentanimation].rotate3[i][currentframe]*(1-target)+animation[targetanimation].rotate3[i][targetframe]*(target);
					}
					if(skeleton.muscles[i].visible)
					{
						skeleton.muscles[i].rotate1=animation[currentanimation].mrotate1[i][currentframe]*(1-target)+animation[targetanimation].mrotate1[i][targetframe]*(target);
						skeleton.muscles[i].rotate2=animation[currentanimation].mrotate2[i][currentframe]*(1-target)+animation[targetanimation].mrotate2[i][targetframe]*(target);
						skeleton.muscles[i].rotate3=animation[currentanimation].mrotate3[i][currentframe]*(1-target)+animation[targetanimation].mrotate3[i][targetframe]*(target);
					}
				}
				if(currentanimation==LYINGANIM){
					skeleton.joints[i].velocity.set((tempanimation.position[i][0].multiply(1-target).add(animation[targetanimation].position[i][targetframe].multiply(target)).subtract(skeleton.joints[i].position)).devide((float)multiplier));
					skeleton.joints[i].position.set(tempanimation.position[i][0].multiply(1-target).add(animation[targetanimation].position[i][targetframe].multiply(target)));
				}
			}
		}
		
		//Look up+down
		if(!skeleton.free&&(whichgun!=NOGUN||who==0)&&health==100&&currentanimation!=LYINGANIM&&currentanimation!=GETUPFRONTANIM&&currentanimation!=GETUPBACKANIM&&currentanimation!=DIVEANIM&&targetanimation!=DIVEANIM&&targetanimation!=THROWANIM&&targetanimation!=THROWNANIM){		
			facing.set(0);
			facingdown.set(0);
			facinghalf.set(0);
			facingright.set(0);
			
			if(who==0){
				playerrotation2=camera.rotation2;
				//Facing
				facing.set(0);
				facing.setZ(1);
				
				facinghalf.set(doRotation(facing,playerrotation2/2,0,0));
				facinghalf.set(doRotation(facinghalf,0,-7,0));
				facing.set(doRotation(facing,playerrotation2,0,0));
				facingright.set(doRotation(facing,0,-90,0));
				facingdown.set(doRotation(facing,90,0,0));
			}
			if(who!=0){
				//Facing
				facing.set(0);
				facing.setZ(1);
				
				facinghalf.set(doRotation(facing,playerrotation2/2,0,0));
				facinghalf.set(doRotation(facinghalf,0,-7,0));
				facing.set(doRotation(facing,playerrotation2,0,0));
				facingright.set(doRotation(facing,0,-90,0));
				facingdown.set(doRotation(facing,90,0,0));
			}
			
			rotatearound.set(0);
			oldpos.set(0);
			
			if(whichgun==SNIPERRIFLE){
				for(int i=0;i<skeleton.num_joints;i++){
					if(skeleton.joints[i].label!=RIGHTHAND&&skeleton.joints[i].label!=RIGHTELBOW&&skeleton.joints[i].label!=RIGHTWRIST&&skeleton.joints[i].label!=LEFTHAND&&skeleton.joints[i].label!=LEFTELBOW&&skeleton.joints[i].label!=LEFTWRIST){
					}else{
						skeleton.joints[i].position.set(animation[RIFLEHOLDANIM].position[i][0]);
						if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
					}
				}
			}
			if(whichgun==SHOTGUN){
				for(int i=0;i<skeleton.num_joints;i++){
					if(skeleton.joints[i].label!=RIGHTHAND&&skeleton.joints[i].label!=RIGHTELBOW&&skeleton.joints[i].label!=RIGHTWRIST&&skeleton.joints[i].label!=LEFTHAND&&skeleton.joints[i].label!=LEFTELBOW&&skeleton.joints[i].label!=LEFTWRIST){
					}else{
						skeleton.joints[i].position.set(animation[RIFLEHOLDANIM].position[i][0]);
						if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
					}
				}
			}	
			if(whichgun==ASSAULTRIFLE){
				for(int i=0;i<skeleton.num_joints;i++){
					if(skeleton.joints[i].label!=RIGHTHAND&&skeleton.joints[i].label!=RIGHTELBOW&&skeleton.joints[i].label!=RIGHTWRIST&&skeleton.joints[i].label!=LEFTHAND&&skeleton.joints[i].label!=LEFTELBOW&&skeleton.joints[i].label!=LEFTWRIST){
					}else{
						skeleton.joints[i].position.set(animation[RIFLEHOLDANIM].position[i][0]);
						if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)
							skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
					}
				}
			}
			if((aiming||aimamount>0||whichgun==GRENADE)&&whichgun!=NOGUN){
				if(aiming&&targetanimation!=JOGANIM){
					if(aimamount<1)aimamount+=multiplier*4;
					if(aimamount>1)aimamount=1;
				}
				if(!aiming||targetanimation==JOGANIM){
					if(aimamount>0)aimamount-=multiplier*4;
					if(aimamount<0)aimamount=0;
				}
				if(grenphase==1){
					if(grenamount<1)grenamount+=multiplier*4;
					if(grenamount>1)grenamount=1;
				}
				if(grenphase==0){
					if(grenamount>0)grenamount-=multiplier*4;
					if(grenamount<0)grenamount=0;
				}
				rotatearound.set(skeleton.joints[skeleton.jointlabels[NECK]].position);
				for(int i=0;i<skeleton.num_joints;i++){
					if(skeleton.joints[i].label!=RIGHTHAND&&skeleton.joints[i].label!=RIGHTELBOW&&skeleton.joints[i].label!=RIGHTWRIST&&skeleton.joints[i].label!=LEFTHAND&&skeleton.joints[i].label!=LEFTELBOW&&skeleton.joints[i].label!=LEFTWRIST){
					}else{
						if(whichgun==SNIPERRIFLE){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[RIFLEAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.incrementAdd(facingright.multiply(0.1f));
							skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),(playerrotation2/2-10)*aimamount,0,0)));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==SHOTGUN){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[ASSAULTRIFLEAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),(playerrotation2/2)*aimamount,0,0)));
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementAdd(facingdown.multiply(.2f));
							else skeleton.joints[i].position.incrementSubtract(facingdown.multiply(.02f));
							if(skeleton.joints[i].label==RIGHTHAND)skeleton.joints[i].position.set(skeleton.joints[skeleton.jointlabels[LEFTHAND]].position.subtract(facinghalf.multiply(2)));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==ASSAULTRIFLE){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[ASSAULTRIFLEAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),(playerrotation2/2)*aimamount,0,0)));
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementAdd(facingdown.multiply(.2f));
							else skeleton.joints[i].position.incrementSubtract(facingdown.multiply(.02f));
							if(skeleton.joints[i].label==RIGHTHAND)skeleton.joints[i].position.set(skeleton.joints[skeleton.jointlabels[LEFTHAND]].position.subtract(facinghalf.multiply(2)));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==HANDGUN1){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[PISTOLAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),(playerrotation2/2)*aimamount,0,0)));
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM){skeleton.joints[i].position.incrementAdd(facingright.multiply(.15f));skeleton.joints[i].position.incrementAdd(facingdown.multiply(.3f));}
							else skeleton.joints[i].position.incrementSubtract(facingdown.multiply(.1f));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==HANDGUN2){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[PISTOLAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),(playerrotation2/2)*aimamount,0,0)));
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM){skeleton.joints[i].position.incrementAdd(facingright.multiply(.15f));skeleton.joints[i].position.incrementAdd(facingdown.multiply(.3f));}
							else skeleton.joints[i].position.incrementSubtract(facingdown.multiply(.1f));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==GRENADE){
							aimamount=1;
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[GRENADEAIMANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(aimamount).add(oldpos.multiply(1-aimamount)));
						}
						if(whichgun==GRENADE&&grenamount>0){
							oldpos.set(skeleton.joints[i].position);
							skeleton.joints[i].position.set(animation[GRENADECHARGEANIM].position[i][0]);
							if(currentanimation==CROUCHANIM||targetanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(animation[IDLEANIM].position[skeleton.jointlabels[NECK]][0].subtract(skeleton.joints[skeleton.jointlabels[NECK]].position));
							skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(grenamount).add(oldpos.multiply(1-grenamount)));
						}
						if(thirdperson!=0||who!=0)skeleton.joints[i].position.incrementAdd(facingdown.multiply(.4f));
						if(currentanimation!=CROUCHANIM)skeleton.joints[i].position.incrementSubtract(facinghalf.multiply(recoil*.35f));
						if(currentanimation==CROUCHANIM)skeleton.joints[i].position.incrementSubtract(facinghalf.multiply(recoil*.1f));
					}
				}
			}
			//Whack
			if(attackframe>-1&&whichgun!=GRENADE){
				for(int i=0;i<skeleton.num_joints;i++){
					if(!skeleton.joints[i].lower){
						if(attackframe==animation[RIFLEHITANIM].numframes) skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(attacktarget).add(animation[RIFLEHITANIM].position[i][attackframe-1].multiply(1-attacktarget)));
						if(attackframe>0&&attackframe<animation[RIFLEHITANIM].numframes) skeleton.joints[i].position.set(animation[RIFLEHITANIM].position[i][attackframe-1].multiply(1-attacktarget).add(animation[RIFLEHITANIM].position[i][attackframe].multiply(attacktarget)));
						if(attackframe==0)skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(1-attacktarget).add(animation[RIFLEHITANIM].position[i][attackframe].multiply(attacktarget)));
					}
				}
				if(attackframe==0)attacktarget+=multiplier*animation[RIFLEHITANIM].speed[attackframe]*2;
				if(attackframe>0)attacktarget+=multiplier*animation[RIFLEHITANIM].speed[attackframe-1]*2;
				if(attacktarget>1){
					attacktarget=0;
					attackframe++;
					if(attackframe>animation[RIFLEHITANIM].numframes)attackframe=-1;
				}
			}
			//Throw GRENADE
			if(attackframe>-1&&whichgun==GRENADE&&ammo>0){
				for(int i=0;i<skeleton.num_joints;i++){
					if(!skeleton.joints[i].lower){
						if(attackframe==animation[GRENADETHROWANIM].numframes)skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(attacktarget).add(animation[GRENADETHROWANIM].position[i][attackframe-1].multiply(1-attacktarget)));
						if(attackframe>0&&attackframe<animation[GRENADETHROWANIM].numframes)skeleton.joints[i].position.set(animation[GRENADETHROWANIM].position[i][attackframe-1].multiply(1-attacktarget).add(animation[GRENADETHROWANIM].position[i][attackframe].multiply(attacktarget)));
						if(attackframe==0)skeleton.joints[i].position.set(skeleton.joints[i].position.multiply(1-attacktarget).add(animation[GRENADETHROWANIM].position[i][attackframe].multiply(attacktarget)));
					}
				}
				if(attackframe==0)attacktarget+=multiplier*animation[GRENADETHROWANIM].speed[attackframe]*2;
				if(attackframe>0)attacktarget+=multiplier*animation[GRENADETHROWANIM].speed[attackframe-1]*2;
				if(attacktarget>1){
					attacktarget=0;
					attackframe++;
					if(attackframe>animation[GRENADETHROWANIM].numframes){
						attackframe=-1;
					}
					if(attackframe==animation[GRENADETHROWANIM].numframes-1&&ammo>0){
						ammo=-1;
						sprites.makeSprite(GRENADE_SPRITE, 1, 1, 1, 1, doRotation(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,0,playerrotation,0).add(playercoords), doRotation(facing,0,playerrotation,0).multiply(velocity.add(30)), 1);
						sprites.makeSprite(SPOON_SPRITE, 1, 1, 1, 1, doRotation(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,0,playerrotation,0).add(playercoords), doRotation(facing,0,playerrotation,0).multiply(velocity.add(10)), 1);
						sprites.makeSprite(PIN_SPRITE, 1, 1, 1, 1, doRotation(skeleton.joints[skeleton.jointlabels[LEFTHAND]].position,0,playerrotation,0).add(playercoords), facing.multiply(velocity.add(.1f)), 1);
						soundsource.set(doRotation(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,0,playerrotation,0).add(playercoords));
						gLoc[0]=soundsource.getX()/soundscalefactor;
						gLoc[1]=soundsource.getY()/soundscalefactor;
						gLoc[2]=soundsource.getZ()/soundscalefactor;
						alSourcefv(gSourceID[GRENADETHROWSOUND], AL_POSITION, gLoc);
						alSourcePlay(gSourceID[GRENADETHROWSOUND]);
					}
				}
			}
			
			rotatearound.set(skeleton.joints[skeleton.jointlabels[ABDOMEN]].position);
			if(who==0)
			for(int i=0;i<skeleton.num_joints;i++){
			 	if(!skeleton.joints[i].lower)
			 		skeleton.joints[i].position.set(rotatearound.add(doRotation(skeleton.joints[i].position.subtract(rotatearound),playerrotation2/2,0,0)));
			}
			if(who==0)
			for(int i=0;i<skeleton.num_joints;i++){
			 	if(skeleton.joints[i].lower&&skeleton.joints[i].label!=GROIN)
			 		skeleton.joints[i].position.set(doRotation(skeleton.joints[i].position,0,playerlowrotation-playerrotation,0));
			}
			//head facing
			if(who==0){
			rotatearound.set(skeleton.joints[skeleton.jointlabels[NECK]].position);
			skeleton.joints[skeleton.jointlabels[HEAD]].position.set(rotatearound.add(doRotation(skeleton.joints[skeleton.jointlabels[HEAD]].position.subtract(rotatearound),playerrotation2/2,0,0)));
			}
			
			skeleton.doConstraints();
			
			//Reload
			if(whichgun!=NOGUN&&whichgun!=KNIFE){
				if(reloading>0){
					aiming=false;
					reloading-=multiplier;
				}
				if(ammo<0&&reloads[whichgun]>0&&reloading<=0){
					if(whichgun!=GRENADE){
						tempint.clear();
						gLoc[0]=playercoords.getX()/soundscalefactor;
						gLoc[1]=playercoords.getY()/soundscalefactor;
						gLoc[2]=playercoords.getZ()/soundscalefactor;
	
						alGetSourcei(gSourceID[RELOADSOUND], AL_SOURCE_STATE, tempint);

						if (tempint.get() != AL_PLAYING){
							alSourcefv(gSourceID[RELOADSOUND], AL_POSITION, gLoc);
							alSourcePlay(gSourceID[RELOADSOUND]);
						}
					}
					reloading=3;
					aiming=false;
					if(whichgun==SNIPERRIFLE)ammo=5;
					if(whichgun==ASSAULTRIFLE)ammo=25;
					if(whichgun==HANDGUN1)ammo=12;
					if(whichgun==HANDGUN2)ammo=16;
					if(whichgun==GRENADE){ammo=1; reloading=1;}
					if(whichgun==SHOTGUN)ammo=6;
					reloads[whichgun]--;
				}
				if(reloads[whichgun]==0&&whichgun==GRENADE&&ammo<=0){
					whichgun=NOGUN;	
				}
				if(reloading<0){
					reloading=0;
					aiming=true;
				}
			}
		}
	}

	public void doAnimationslite(int who){
		
		if(target>1&&!skeleton.free){
			//Footstep sounds
			if(who==0&&(targetanimation==JOGANIM||targetanimation==WALKANIM)&&(targetframe==0||targetframe==8)&&visions==0&&(onground||abs(velocity.getY())<1)){
				gLoc[0]=playercoords.getX()/soundscalefactor;
				gLoc[1]=playercoords.getY()/soundscalefactor;
				gLoc[2]=playercoords.getZ()/soundscalefactor;
				whichsound=FOOTSTEPSOUND+abs(random())%5;
				alSourcefv(gSourceID[whichsound], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[whichsound]);
			}
			if(targetanimation==ZOMBIEEATANIM&&(targetframe==3)){
				soundpoint.set(doRotation(skeleton.joints[skeleton.jointlabels[HEAD]].position,0,playerrotation,0).add(playercoords));
				gLoc[0]=soundpoint.getX()/soundscalefactor;
				gLoc[1]=soundpoint.getY()/soundscalefactor;
				gLoc[2]=soundpoint.getZ()/soundscalefactor;
				alSourcefv(gSourceID[BODYHITSOUND], AL_POSITION, gLoc);
				alSourcePlay(gSourceID[BODYHITSOUND]);
				bleeding=1;
				bleeddelay=1;
				bjoint1=skeleton.joints[skeleton.jointlabels[HEAD]];
				bjoint2=skeleton.joints[skeleton.jointlabels[NECK]];
			}
			targetframe=currentframe;
			currentanimation=targetanimation;
			if(!backwardsanim){targetframe++;
			if(targetframe>animation[currentanimation].numframes-1)targetframe=0;}
			if(backwardsanim){targetframe--;
			if(targetframe<0)targetframe=animation[currentanimation].numframes-1;}
			target=0;
			if((currentanimation==GETUPFRONTANIM||currentanimation==GETUPBACKANIM)&&targetframe==0){
				targetanimation=IDLEANIM;
			}
		}
		
		if(!skeleton.free){
			if(currentanimation!=LYINGANIM){
				if(animation[targetanimation].speed[currentframe]>animation[currentanimation].speed[currentframe])
					target+=multiplier*animation[targetanimation].speed[currentframe]*speed;
				if(animation[targetanimation].speed[currentframe]<=animation[currentanimation].speed[currentframe])
					target+=multiplier*animation[currentanimation].speed[currentframe]*speed;
			}
			if(currentanimation==LYINGANIM){
				target+=multiplier*animation[targetanimation].speed[targetframe]*speed;
			}		
			if(((currentanimation==CROUCHANIM)&&(targetanimation!=CROUCHANIM))||((currentanimation!=CROUCHANIM)&&(targetanimation==CROUCHANIM)))target+=multiplier*animation[CROUCHANIM].speed[0]*2;
			if(currentanimation==IDLEANIM&&targetanimation==IDLEANIM)target-=multiplier*animation[IDLEANIM].speed[0]/2;
			
			if(target>1)currentframe=targetframe;
		}
	}

	public void doStuff(int who){
		int moveanim = 0;

		if((targetanimation!=IDLEANIM&&targetanimation!=CROUCHANIM)||visions==1)playerlowrotation=playerrotation;
		if(targetanimation!=CROUCHANIM&&currentanimation!=CROUCHANIM){
			if(playerrotation>playerlowrotation+50){playerlowrotation=playerrotation-50; targetanimation=WALKANIM; targetframe=0; target=0;}
			if(playerrotation<playerlowrotation-50){playerlowrotation=playerrotation+50; targetanimation=WALKANIM; targetframe=0; target=0;}
		}
		if(targetanimation==CROUCHANIM||currentanimation==CROUCHANIM){
			if(playerrotation>playerlowrotation+70){playerrotation=playerlowrotation+70;}
			if(playerrotation<playerlowrotation-70){playerrotation=playerlowrotation-70;}
		}
		if(who==0)camera.rotation=180-playerrotation;
		
		if(who!=0&&visions==0){
			if(targetanimation!=WALKANIM&&targetanimation!=ZOMBIEWALKANIM)speed=1.0f*speedmult;
			if(targetanimation==WALKANIM||targetanimation==ZOMBIEWALKANIM)speed=1.8f*speedmult;
			playerlowrotation=playerrotation;
		}
		
		//Do controls
		if(who==0&&targetanimation!=DIVEANIM&&targetanimation!=THROWANIM&&targetanimation!=THROWNANIM&&currentanimation!=DIVEANIM&&currentanimation!=GETUPFRONTANIM){
			
			backwardsanim=false;
			if(visions==1)speed=40;
			if(visions==0&&targetanimation==JOGANIM)speed=2.2f;
			if(visions==0&&targetanimation!=JOGANIM)speed=1.3f;
			if(visions==0&&targetanimation==WALKANIM)speed=2.5f;
			
			if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS ||visions==1){
					moveanim=JOGANIM;
				}else{
					moveanim=WALKANIM;
				}
			if(glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS &&currentanimation==IDLEANIM&&targetanimation==IDLEANIM){
				targetanimation=CROUCHANIM;
				target=0;
			}
			if((glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) != GLFW_PRESS) && currentanimation==CROUCHANIM&&targetanimation==CROUCHANIM){
				targetanimation=IDLEANIM;
				target=0;
			}
			
			if((onground||(who==0&&visions==1))&&currentanimation!=CROUCHANIM){
				
				if (glfwGetKey(window, forwardskey) == GLFW_PRESS && (glfwGetKey(window, backwardskey) != GLFW_PRESS)) {
					if(targetanimation!=moveanim)targetframe=0;
					targetanimation=moveanim;
				}
				
				if (glfwGetKey(window, rightkey) == GLFW_PRESS && (glfwGetKey(window, leftkey) != GLFW_PRESS)){
					if(targetanimation!=moveanim)targetframe=0;
					targetanimation=moveanim;
					playerlowrotation-=90;
					if(glfwGetKey(window, forwardskey) == GLFW_PRESS)playerlowrotation+=45;
					if(glfwGetKey(window, backwardskey) == GLFW_PRESS)playerlowrotation-=235;
				}
				
				if (glfwGetKey(window, leftkey) == GLFW_PRESS && (glfwGetKey(window, rightkey) != GLFW_PRESS)){
					if(targetanimation!=moveanim)targetframe=0;
					targetanimation=moveanim;
					playerlowrotation+=90;
					if(glfwGetKey(window, forwardskey) == GLFW_PRESS)playerlowrotation-=45;
					if(glfwGetKey(window, backwardskey) == GLFW_PRESS)playerlowrotation+=235;
				}
				
				if (glfwGetKey(window, backwardskey) == GLFW_PRESS && (glfwGetKey(window, forwardskey) != GLFW_PRESS)){
					if(targetanimation!=moveanim)targetframe=0;
					targetanimation=moveanim;
					backwardsanim=true;
				}
					
				
			}
			//air control
			if(!onground){
				
				float oldplayerrotation=playerrotation;
				
				if (glfwGetKey(window, forwardskey) == GLFW_PRESS && (glfwGetKey(window, backwardskey) != GLFW_PRESS )){
					facing.set(0);
					facing.setZ(1);		
					facing.set(doRotation(facing,0,playerrotation,0));
					velocity.set(velocity.add(facing.multiply((float)multiplier*4)));
				}
				if ( glfwGetKey(window, rightkey) == GLFW_PRESS &&( glfwGetKey(window, leftkey) != GLFW_PRESS)){
					playerrotation-=90;
					
					if( glfwGetKey(window, forwardskey) == GLFW_PRESS)playerrotation+=45;
					if(glfwGetKey(window, backwardskey) == GLFW_PRESS)playerrotation-=45;
					facing.set(0);
					facing.setZ(1);		
					facing.set(doRotation(facing,0,playerrotation,0));
					velocity.set(velocity.add(facing.multiply((float)multiplier*4)));
				}
				if  (glfwGetKey(window, leftkey) == GLFW_PRESS &&( glfwGetKey(window, rightkey) != GLFW_PRESS)){
					playerrotation+=90;
					if( glfwGetKey(window, forwardskey) == GLFW_PRESS)playerrotation-=45;
					if( glfwGetKey(window, backwardskey) == GLFW_PRESS)playerrotation+=45;
					facing.set(0);
					facing.setZ(1);		
					facing.set(doRotation(facing,0,playerrotation,0));
					velocity.set(velocity.add(facing.multiply((float)multiplier*4)));
				}
				
				if (( glfwGetKey(window, backwardskey) == GLFW_PRESS) && (glfwGetKey(window, forwardskey) != GLFW_PRESS ) && (glfwGetKey(window, leftkey) != GLFW_PRESS) && ( glfwGetKey(window, rightkey) != GLFW_PRESS)){
					playerrotation+=180;
					facing.set(0);
					facing.setZ(1);		
					facing.set(doRotation(facing,0,playerrotation,0));
					velocity.set(velocity.add(facing.multiply((float)multiplier*4)));
				}
				playerrotation=oldplayerrotation;
			}
			
			if((glfwGetKey(window, forwardskey) != GLFW_PRESS) && (glfwGetKey(window, leftkey) != GLFW_PRESS ) && (glfwGetKey(window, rightkey) != GLFW_PRESS) && (glfwGetKey(window, backwardskey) != GLFW_PRESS) && (targetanimation==JOGANIM||targetanimation==WALKANIM)){
				if(!(targetanimation==WALKANIM&&(targetframe==0||targetframe==2))){
					targetanimation=IDLEANIM;
					targetframe=0;
					target=0;
				}
			}
		}
		
		
		facing.set(0);
		facing.setZ(1);
		
		facing.set(doRotation(facing,0,playerlowrotation,0));
		if(backwardsanim)facing.incrementMultiply(-1);
		
		if(onground){
			velocity.set(0);
		}
		if(((currentanimation==JOGANIM||currentanimation==ZOMBIEJOGANIM||currentanimation==DIVEANIM)&&onground)||(who==0&&visions==1&&((currentanimation==JOGANIM||currentanimation==WALKANIM||currentanimation==DIVEANIM)||(currentanimation==ZOMBIEJOGANIM||currentanimation==ZOMBIEWALKANIM)))){
			playercoords.incrementAdd(facing.multiply((float)multiplier*15*speed));
			velocity.setX(facing.getX()*15*speed);
			velocity.setZ(facing.getZ()*15*speed);
		}
		if((currentanimation==WALKANIM||currentanimation==ZOMBIEWALKANIM)&&onground&&(who!=0||visions==0)){
			playercoords.incrementAdd(facing.multiply((float)multiplier*4*speed));
			velocity.setX(facing.getX()*4*speed);
			velocity.setZ(facing.getZ()*4*speed);
		}
	}

	public void findRotationGun(XYZ start, XYZ target)
	{
		
		temppoint1.set(start);
		temppoint2.set(target);
		distance=findDistance(temppoint1,temppoint2);
		gunrotate2=(float) (asin((temppoint1.getY()-temppoint2.getY())/distance)*rad2deg);
		temppoint1.setY(0);
		temppoint2.setY(0);
		gunrotate1=(float) (acos((temppoint1.getZ()-temppoint2.getZ())/findDistance(temppoint1,temppoint2))*rad2deg);
		if(temppoint1.getX()>temppoint2.getX())gunrotate1=360-gunrotate1;
		tempforward.set(target.subtract(start));
		tempforward.set(doRotation(tempforward,-90,0,0));
		tempforward.set(doRotation(tempforward,0,gunrotate1-90,0));
		tempforward.set(doRotation(tempforward,0,0,gunrotate2-90));
		tempforward.setY(0);
		normalise(tempforward);
		gunrotate3=(float) (acos(0-tempforward.getZ())*rad2deg);
		if(0>tempforward.getX())gunrotate3=360-gunrotate3;
	}
	
	public int drawSkeleton(int who){
		//Guns
		if(whichgun==SNIPERRIFLE){
			findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,skeleton.joints[skeleton.jointlabels[LEFTHAND]].position);
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-gunrotate1+90,0,1,0);
					glRotatef(-gunrotate2+90,0,0,1);
					glRotatef(-gunrotate3,0,1,0);
					gunmodels[SNIPERRIFLEMODEL].draw();
				glPopMatrix();
		}
		
		if(whichgun==SHOTGUN){
			findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,skeleton.joints[skeleton.jointlabels[LEFTHAND]].position);
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(2,1,0,0);
					glRotatef(-gunrotate1+90+1,0,1,0);
					glRotatef(-gunrotate2+90,0,0,1);
					glRotatef(-gunrotate3,0,1,0);
					glTranslatef(0,-.4f,0);
					gunmodels[SHOTGUNMODEL].draw();
				glPopMatrix();
		}
		
		if(whichgun==ASSAULTRIFLE){
			findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,skeleton.joints[skeleton.jointlabels[LEFTHAND]].position);
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-gunrotate1+90,0,1,0);
					glRotatef(-gunrotate2+90,0,0,1);
					glRotatef(-gunrotate3,0,1,0);
					gunmodels[ASSAULTRIFLEMODEL].draw();
				glPopMatrix();
		}
		
		if(whichgun==HANDGUN1){
			if(thirdperson==0&&who==0)findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,(skeleton.joints[skeleton.jointlabels[HEAD]].position.multiply(skeleton.joints[skeleton.jointlabels[NECK]].position.multiply(.35f).add(.65f))));
			if(thirdperson!=0||who!=0)findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,(skeleton.joints[skeleton.jointlabels[HEAD]].position.multiply(.35f).add(skeleton.joints[skeleton.jointlabels[NECK]].position.multiply(.65f))));
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-gunrotate1+90-1.5f,0,1,0);
					glRotatef(-gunrotate2+90,0,0,1);
					glRotatef(-gunrotate3,0,1,0);
					glTranslatef(0,0,.15f);
					gunmodels[HANDGUNBASEMODEL].draw();
					glTranslatef(0,-recoil*.3f,0);
					gunmodels[HANDGUNSLIDEMODEL].draw();
				glPopMatrix();
		}
		
		if(whichgun==HANDGUN2){
			if(thirdperson==0&&who==0)findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,(skeleton.joints[skeleton.jointlabels[HEAD]].position.multiply(.65f).add(skeleton.joints[skeleton.jointlabels[NECK]].position.multiply(.35f))));
			if(thirdperson!=0||who!=0)findRotationGun(skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position,(skeleton.joints[skeleton.jointlabels[HEAD]].position.multiply(.35f).add(skeleton.joints[skeleton.jointlabels[NECK]].position.multiply(.65f))));
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-gunrotate1+90-1.5f,0,1,0);
					glRotatef(-gunrotate2+90,0,0,1);
					glRotatef(-gunrotate3,0,1,0);
					glTranslatef(0,0,.15f);
					gunmodels[HANDGUN2BASEMODEL].draw();
					glTranslatef(0,-recoil*.3f,0);
					gunmodels[HANDGUN2SLIDEMODEL].draw();
				glPopMatrix();
		}
		
		if(whichgun==GRENADE){
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-90,1,0,0);
					glTranslatef(0,0,.05f);
					if(reloading<=0){
						gunmodels[GRENADEBASEMODEL].draw();
						if(grenphase==0)gunmodels[GRENADEPINMODEL].draw();
						glTranslatef(0,0,.005f);
						gunmodels[GRENADESPOONMODEL].draw();
					}
				glPopMatrix();
		}
		
		if(whichgun==GRENADE){
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[LEFTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[LEFTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[LEFTHAND]].position.getZ());
					glRotatef(-90,1,0,0);
					glTranslatef(0,0,-.15f);
					if(reloading<=0){
						if(grenphase==1)gunmodels[GRENADEPINMODEL].draw();
					}
				glPopMatrix();
		}
		if(!litup){
			glDisable(GL_LIGHT1);
		}
		if(litup){
			lightpoint.set(skeleton.joints[skeleton.jointlabels[LEFTHAND]].position);
			LightPosition[0] = lightpoint.getX();
			LightPosition[1] = lightpoint.getY();
			LightPosition[2] = lightpoint.getZ();
			LightPosition[3] = 0;
			glLightfv(GL_LIGHT1, GL_POSITION, LightPosition);
			glLightfv(GL_LIGHT1, GL_AMBIENT, LightAmbient);		
			glLightfv(GL_LIGHT1, GL_DIFFUSE, LightDiffuse);	
			glEnable(GL_LIGHT1);
			
			litup=false;
		}
		
		//Find forward vectors
		if(who==0||skeleton.free||skeleton.offset!=0||whichgun!=NOGUN||currentanimation==LYINGANIM||((currentanimation==GETUPFRONTANIM||currentanimation==GETUPBACKANIM)&&targetanimation==IDLEANIM)){
		if(!(skeleton.free&&longdead<=0)){
		if(skeleton.offset!=0&&!skeleton.free){
			skeleton.offset=0;
			for(int i=0;i<skeleton.num_joints;i++){
				skeleton.joints[i].oldposition.set(skeleton.joints[i].position);
				skeleton.joints[i].position.incrementAdd(skeleton.joints[i].offset);
				if(findLengthfast(skeleton.joints[i].offset)<multiplier*multiplier*25)skeleton.joints[i].offset.set(0);
				else{
					normal.set(skeleton.joints[i].offset);
					normalise(normal);
					skeleton.offset=1;
					skeleton.joints[i].offset.incrementSubtract(normal.multiply((float)multiplier*5));
				}
			}
			skeleton.doConstraints();
		}
		crossProduct(skeleton.joints[skeleton.forwardjoints[1]].position.subtract(skeleton.joints[skeleton.forwardjoints[0]].position),skeleton.joints[skeleton.forwardjoints[2]].position.subtract(skeleton.joints[skeleton.forwardjoints[0]].position),skeleton.forward);
		normalise(skeleton.forward);
		
		crossProduct(skeleton.joints[skeleton.lowforwardjoints[1]].position.subtract(skeleton.joints[skeleton.lowforwardjoints[0]].position),skeleton.joints[skeleton.lowforwardjoints[2]].position.subtract(skeleton.joints[skeleton.lowforwardjoints[0]].position),skeleton.lowforward);
		normalise(skeleton.lowforward);
		
		//Special forwards
		if(skeleton.specialforward[0]==null) skeleton.specialforward[0] = new XYZ(skeleton.forward);
		else skeleton.specialforward[0].set(skeleton.forward);
		
		if(skeleton.specialforward[1]==null)skeleton.specialforward[1]= new XYZ(skeleton.joints[skeleton.jointlabels[RIGHTSHOULDER]].position.add(skeleton.joints[skeleton.jointlabels[RIGHTWRIST]].position));
		else skeleton.specialforward[1].set(skeleton.joints[skeleton.jointlabels[RIGHTSHOULDER]].position.add(skeleton.joints[skeleton.jointlabels[RIGHTWRIST]].position));
		
		skeleton.specialforward[1].set(skeleton.joints[skeleton.jointlabels[RIGHTELBOW]].position.subtract(skeleton.specialforward[1].devide(2)));
		skeleton.specialforward[1].incrementAdd(skeleton.forward.multiply(.2f));
		normalise(skeleton.specialforward[1]);
		
		if(skeleton.specialforward[2]==null)skeleton.specialforward[2]= new XYZ(skeleton.joints[skeleton.jointlabels[LEFTSHOULDER]].position.add(skeleton.joints[skeleton.jointlabels[LEFTWRIST]].position));
		else skeleton.specialforward[2].set(skeleton.joints[skeleton.jointlabels[LEFTSHOULDER]].position.add(skeleton.joints[skeleton.jointlabels[LEFTWRIST]].position));
		
		skeleton.specialforward[2].set(skeleton.joints[skeleton.jointlabels[LEFTELBOW]].position.subtract(skeleton.specialforward[2].devide(2)));
		skeleton.specialforward[2].incrementAdd(skeleton.forward.multiply(.2f));
		normalise(skeleton.specialforward[2]);

		if(who==0&&aimamount>0&&health==100&&whichgun!=NOGUN){
			//Facing
			facing.set(0);
			facingdown.set(0);
			//Facing
			facing.setZ(1);
			
			facing.set(doRotation(facing,camera.rotation2,0,0));
			facingdown.set(doRotation(facing,90,0,0));
			skeleton.specialforward[1].set(skeleton.specialforward[1].multiply(1-aimamount).add(facingdown.multiply(aimamount)));
			skeleton.specialforward[2].set(skeleton.specialforward[2].multiply(1-aimamount).add(facingdown.multiply(aimamount)));
		}
		
		if(skeleton.specialforward[3]==null)skeleton.specialforward[3]= new XYZ(skeleton.joints[skeleton.jointlabels[RIGHTHIP]].position.add(skeleton.joints[skeleton.jointlabels[RIGHTANKLE]].position));
		else skeleton.specialforward[3].set(skeleton.joints[skeleton.jointlabels[RIGHTHIP]].position.add(skeleton.joints[skeleton.jointlabels[RIGHTANKLE]].position));
		
		skeleton.specialforward[3].set(skeleton.specialforward[3].devide(2).subtract(skeleton.joints[skeleton.jointlabels[RIGHTKNEE]].position));
		skeleton.specialforward[3].incrementAdd(skeleton.lowforward.multiply(.2f));
		normalise(skeleton.specialforward[3]);
		
		if(skeleton.specialforward[4]==null)skeleton.specialforward[4]= new XYZ(skeleton.joints[skeleton.jointlabels[LEFTHIP]].position.add(skeleton.joints[skeleton.jointlabels[LEFTANKLE]].position));
		else skeleton.specialforward[4].set(skeleton.joints[skeleton.jointlabels[LEFTHIP]].position.add(skeleton.joints[skeleton.jointlabels[LEFTANKLE]].position));
		skeleton.specialforward[4].set(skeleton.specialforward[4].devide(2).subtract(skeleton.joints[skeleton.jointlabels[LEFTKNEE]].position));
		skeleton.specialforward[4].incrementAdd(skeleton.lowforward.multiply(.2f));
		normalise(skeleton.specialforward[4]);

		//Find joint rotations
		for(int i=0;i<skeleton.num_joints;i++){
			if(skeleton.joints[i].hasparent&&skeleton.joints[i].visible)
			{
				skeleton.findRotationJoint(i);
			}
		}
		for(int i=0;i<skeleton.num_muscles;i++){
			if(skeleton.muscles[i].visible)
			{
				skeleton.findRotationMuscle(i);
			}
		}}}
		for(int i=0;i<skeleton.num_joints;i++){
			if(skeleton.joints[i].hasparent&&skeleton.joints[i].visible)
			{
				glPushMatrix();
					glTranslatef(	(skeleton.joints[i].position.getX()+skeleton.joints[i].parent.position.getX())/2,
									(skeleton.joints[i].position.getY()+skeleton.joints[i].parent.position.getY())/2,
									(skeleton.joints[i].position.getZ()+skeleton.joints[i].parent.position.getZ())/2);
					glRotatef(-skeleton.joints[i].rotate1+90,0,1,0);
					glRotatef(-skeleton.joints[i].rotate2+90,0,0,1);
					glRotatef(-skeleton.joints[i].rotate3,0,1,0);
					if(skeleton.joints[i].modelnum==0&&(who!=0||thirdperson!=0))skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].headcolor[0],costume[whichcostume].headcolor[1],costume[whichcostume].headcolor[2]);
					if(skeleton.joints[i].modelnum==0&&(who==0&&thirdperson!=0))skeletonmodels[9].draw();
					if(skeleton.joints[i].modelnum==1&&(who!=0||thirdperson!=0))skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].chestcolor[0],costume[whichcostume].chestcolor[1],costume[whichcostume].chestcolor[2]);
					if(skeleton.joints[i].modelnum==2)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].abdomencolor[0],costume[whichcostume].abdomencolor[1],costume[whichcostume].abdomencolor[2]);
					if(skeleton.joints[i].modelnum==3)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].upperarmcolor[0],costume[whichcostume].upperarmcolor[1],costume[whichcostume].upperarmcolor[2]);
					if(skeleton.joints[i].modelnum==4)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].lowerarmcolor[0],costume[whichcostume].lowerarmcolor[1],costume[whichcostume].lowerarmcolor[2]);
					if(skeleton.joints[i].modelnum==5)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].handcolor[0],costume[whichcostume].handcolor[1],costume[whichcostume].handcolor[2]);
					if(skeleton.joints[i].modelnum==6)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].upperlegcolor[0],costume[whichcostume].upperlegcolor[1],costume[whichcostume].upperlegcolor[2]);
					if(skeleton.joints[i].modelnum==7)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].lowerlegcolor[0],costume[whichcostume].lowerlegcolor[1],costume[whichcostume].lowerlegcolor[2]);
					if(skeleton.joints[i].modelnum==8)skeletonmodels[skeleton.joints[i].modelnum].draw(costume[whichcostume].footcolor[0],costume[whichcostume].footcolor[1],costume[whichcostume].footcolor[2]);
				glPopMatrix();
			}
		}
		
		for(int i=0;i<skeleton.num_muscles;i++){
			if(skeleton.muscles[i].visible)
			{
				glPushMatrix();
					glTranslatef(	(skeleton.muscles[i].parent1.position.getX()+skeleton.muscles[i].parent2.position.getX())/2,
									(skeleton.muscles[i].parent1.position.getY()+skeleton.muscles[i].parent2.position.getY())/2,
									(skeleton.muscles[i].parent1.position.getZ()+skeleton.muscles[i].parent2.position.getZ())/2);
					glRotatef(-skeleton.muscles[i].rotate1+90,0,1,0);
					glRotatef(-skeleton.muscles[i].rotate2+90,0,0,1);
					glRotatef(-skeleton.muscles[i].rotate3,0,1,0);
					if(skeleton.muscles[i].parent1.modelnum==0&&(who!=0||thirdperson!=0))skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].headcolor[0],costume[whichcostume].headcolor[1],costume[whichcostume].headcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==0&&(who==0&&thirdperson!=0))skeletonmodels[9].draw();
					if(skeleton.muscles[i].parent1.modelnum==1&&(who!=0||thirdperson!=0))skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].chestcolor[0],costume[whichcostume].chestcolor[1],costume[whichcostume].chestcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==2)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].abdomencolor[0],costume[whichcostume].abdomencolor[1],costume[whichcostume].abdomencolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==3)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].upperarmcolor[0],costume[whichcostume].upperarmcolor[1],costume[whichcostume].upperarmcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==4)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].lowerarmcolor[0],costume[whichcostume].lowerarmcolor[1],costume[whichcostume].lowerarmcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==5)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].handcolor[0],costume[whichcostume].handcolor[1],costume[whichcostume].handcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==6)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].upperlegcolor[0],costume[whichcostume].upperlegcolor[1],costume[whichcostume].upperlegcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==7)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].lowerlegcolor[0],costume[whichcostume].lowerlegcolor[1],costume[whichcostume].lowerlegcolor[2]);
					if(skeleton.muscles[i].parent1.modelnum==8)skeletonmodels[skeleton.muscles[i].parent1.modelnum].draw(costume[whichcostume].footcolor[0],costume[whichcostume].footcolor[1],costume[whichcostume].footcolor[2]);
					
			glPopMatrix();
			}
		}
		if(whichgun==KNIFE){
			glPushMatrix();
					glTranslatef(	skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getX(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getY(),
									skeleton.joints[skeleton.jointlabels[RIGHTHAND]].position.getZ());
					glRotatef(-skeleton.joints[skeleton.jointlabels[RIGHTHAND]].rotate1+90-1.5f,0,1,0);
					glRotatef(-skeleton.joints[skeleton.jointlabels[RIGHTHAND]].rotate2+90,0,0,1);
					glRotatef(-skeleton.joints[skeleton.jointlabels[RIGHTHAND]].rotate3,0,1,0);
					glTranslatef(0,-.2f,0);
					gunmodels[KNIFEMODEL].draw();
				glPopMatrix();
		}
		if(skeleton.offset!=0&&!skeleton.free){
			for(int i=0;i<skeleton.num_joints;i++){
				skeleton.joints[i].position.set(skeleton.joints[i].oldposition);
			}
		}
		
		glDisable(GL_LIGHT1);
		
		return 0;
	}
	
	public static class Costume {
		public float
			headcolor[] = new float[3],
			handcolor[] = new float[3],
			footcolor[] = new float[3],
			upperarmcolor[] = new float[3],
			lowerarmcolor[] = new float[3],
			upperlegcolor[] = new float[3],
			lowerlegcolor[] = new float[3],
			abdomencolor[] = new float[3],
			chestcolor[] = new float[3];
	}

}
