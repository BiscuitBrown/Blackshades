package blackshades;


import java.io.*;
import java.nio.*;
import static java.lang.Math.*;

import blackshades.Models.*;
import blackshades.Quaternions.XYZ;

import static blackshades.Constants.*;
import static blackshades.Quaternions.*;
import static blackshades.Serialize.*;
import static blackshades.Globals.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.BufferUtils.*;

public class Skeleton {
	public static final int 	
		BONECONNECT = 0,
		CONSTRAINT = 1,
		MUSCLE = 2,

	//head, neck, left shoulder, left elbow, left wrist, left hand
			//right shoulder, right elbow, right wrist, right hand,
			//middle, left hip, right hip,groin
			//left knee,left ankle, left foot, right knee, right ankle, right foort
			
		HEAD = 1,
		NECK = 2,
		LEFTSHOULDER = 3,
		LEFTELBOW = 4,
		LEFTWRIST = 5,
		LEFTHAND = 6,
		RIGHTSHOULDER = 7,
		RIGHTELBOW = 8,
		RIGHTWRIST = 9,
		RIGHTHAND = 10,
		ABDOMEN = 11,
		LEFTHIP = 12,
		RIGHTHIP = 13,
		GROIN = 14,
		LEFTKNEE = 15,
		LEFTANKLE = 16,
		LEFTFOOT = 17,
		RIGHTKNEE = 18,
		RIGHTANKLE = 19,
		RIGHTFOOT = 20;

	public float offset;
	public int num_joints;
	public Joint joints[] = new Joint[MAX_JOINTS];
	public int jointlabels[] = new int [MAX_JOINTS];
	
	public int num_muscles;
	public Muscle muscles[] = new Muscle[MAX_MUSCLES];

	public int selected;

	public int forwardjoints[] = new int[3];
	public XYZ forward;

	public int lowforwardjoints[] = new int[3];
	public XYZ lowforward;
	
	public int broken;
	
	public XYZ specialforward[] = new XYZ[5];
	
	public boolean free;
	
	private InputStream files;
	
	private XYZ avgvelocity;
	
	private FloatBuffer gLoc;
	private IntBuffer tempint;
	
	public static class Joint {

		public XYZ
				position,
				oldposition,
				realoldposition,
				velocity,
				offset;
		
		public float 
				blurred,
				length,
				mass,
				rotate1,rotate2,rotate3;
		
		public boolean 
				lower,
				hasparent,
				locked,
				visible,
				existing;
		
		public Joint parent;
		
		public int 
				modelnum,
				label,
				hasgun;
		
		public Joint() {
			
			this.position = new XYZ();
			this.oldposition = new XYZ();
			this.realoldposition = new XYZ();
			this.velocity = new XYZ();
			this.offset = new XYZ();
	
			this.blurred = 0;
			this.length = 0;
			this.mass = 0;
			this.rotate1 = 0;
			this.rotate2 = 0;
			this.rotate3 = 0;
	
			this.lower = false;
			this.hasparent = false;
			this.locked = false;
			this.visible = false;
			this.existing = false;
		
			this.modelnum = 0;
			this.label = 0;
			this.hasgun = 0;
			
		}
		
		public Joint(Joint add) {
			this.set(add);
			this.lower = add.lower;
			this.hasparent = add.hasparent;
			this.locked = add.locked;
			this.visible = add.visible;
			this.existing = add.existing;

		}
		
		public void set(float num) {
			
			this.position.set(num);
			this.oldposition.set(num);
			this.realoldposition.set(num);
			this.velocity.set(num);
			this.offset.set(num);
	
			this.blurred = num;
			this.length = num;
			this.mass = num;
			this.rotate1 = num;
			this.rotate2 = num;
			this.rotate3 = num;
			
			this.modelnum = (int) num;
			this.label = (int) num;
			this.hasgun = (int) num;
			
		}
		
		public void set(Joint add) {
			
			this.position.set(add.position);
			this.oldposition.set(add.oldposition);
			this.realoldposition.set(add.realoldposition);
			this.velocity.set(add.velocity);
			this.offset.set(add.offset);
	
			this.blurred = add.blurred;
			this.length = add.length;
			this.mass = add.mass;
			this.rotate1 = add.rotate1;
			this.rotate2 = add.rotate2;
			this.rotate3 = add.rotate3;
	
			this.lower = add.lower;
			this.hasparent = add.hasparent;
			this.locked = add.locked;
			this.visible = add.visible;
			this.existing = add.existing;
	
			if(this.hasparent) {this.parent = add.parent;}
	
			this.modelnum = add.modelnum;
			this.label = add.label;
			this.hasgun = add.hasgun;
			
		}
		
		public void doConstraint() {
			
			if(hasparent){
				
				if(midp == null)midp = new XYZ();
				
				if(vel == null)vel = new XYZ();
				
				//Find midpoint
				midp.set(position.add(parent.position).devide(2));
				//Find vector from midpoint to second vector				
				vel.set(parent.position.subtract(midp));
				//Change to unit vector
				normalise(vel);
				//Apply velocity change
				velocity.incrementAdd(midp.subtract(vel.multiply(length).devide(2)).subtract(position));
				parent.velocity.incrementAdd(midp.add(vel.multiply(length).devide(2)).subtract(parent.position));
				//Move child point to within certain distance of parent point
				if(!locked)position.set(midp.subtract(vel.multiply(length).devide(2)));
				if(!parent.locked)parent.position.set(midp.add(vel.multiply(length).devide(2)));
			}
		}
		
		public boolean equals(Joint add) {
			
			return(
					this.position.equals(add.position) &&
					this.oldposition.equals(add.oldposition) &&
					this.realoldposition.equals(add.realoldposition) &&
					this.velocity.equals(add.velocity) &&
					this.offset.equals(add.offset) &&
			
					this.blurred == add.blurred &&
					this.length == add.length &&
					this.mass == add.mass &&
					this.rotate1 == add.rotate1 &&
					this.rotate2 == add.rotate2 &&
					this.rotate3 == add.rotate3 &&
			
					this.lower == add.lower &&
					this.hasparent == add.hasparent &&
					this.locked == add.locked &&
					this.visible == add.visible &&
					this.existing == add.existing &&
					
					
					this.parent == add.parent &&
			
					this.modelnum == add.modelnum &&
					this.label == add.label &&
					this.hasgun == add.hasgun
			);
			
		}
	}
	
	public class Muscle {
		
		public float 
				length,
				targetlength,
				maxlength, minlength;
				
		public Joint parent1, parent2;
		public int type;
		public boolean visible;
		
		public void doConstraint(int broken) {
			
			if(newpoint1 == null) newpoint1 = new XYZ();
			if(newpoint2 == null) newpoint2 = new XYZ();
			
			oldlength=length;
			relaxlength=findDistance(parent1.position,parent2.position);
			
			if(type==BONECONNECT)strength=1;
			if(type==CONSTRAINT)strength=0;
			
			if(strength<0)strength=0;
			if(strength>1)strength=1;
			
			length-=(length-relaxlength)*(1-strength)*multiplier*multiplier*10000;
			length-=(length-targetlength)*(strength)*multiplier*multiplier*10000;
			if(strength==0)length=relaxlength;
			
			if((relaxlength-length>0&&relaxlength-oldlength<0)||(relaxlength-length<0&&relaxlength-oldlength>0))length=relaxlength;
			
			if(length<minlength)length=minlength;
			if(length>maxlength&&broken!=1)length=maxlength;
			
			//Find midpoint
			midp.set((parent1.position.add(parent2.position)).devide(2));
			//Find vector from midpoint to second vector
			vel.set(parent2.position.subtract(midp));
			//Change to unit vector
			normalise(vel);
			//Apply velocity change
			newpoint1.set(midp.subtract(vel.multiply(length).devide(2)));
			newpoint2.set(midp.add(vel.multiply(length).devide(2)));
			parent1.velocity.incrementAdd(newpoint1.subtract(parent1.position));
			parent2.velocity.incrementAdd(newpoint2.subtract(parent2.position));
			//Move child point to within certain distance of parent point
			if(!parent1.locked)parent1.position.set(newpoint1);
			if(!parent2.locked)parent2.position.set(newpoint2);
			
		}
		
		public float rotate1,rotate2,rotate3;
		
		float strength;
	}

	public void doConstraints() {
		
		numrepeats=3;
		
		for(int j=0; j<numrepeats; j++){
			for(int i=0; i<num_joints; i++){
				joints[i].doConstraint();
			}
		}
	}

	public void doConstraints(Model collide, XYZ move, float rotation) {
		
		friction=20;
		numrepeats=2;
		groundlevel=0;
		
		move.y+=.35;
		
		if(overpoint == null) overpoint = new XYZ();
		if(underpoint == null) underpoint = new XYZ();
		if(impact == null) impact = new XYZ();
		
		for(int j=0; j<numrepeats; j++){
			
			for(int i=0; i<num_joints; i++){
				
				if(joints[i].existing||i==jointlabels[LEFTHAND]||i==jointlabels[RIGHTHAND]){
				//Length constraints
				joints[i].doConstraint();
				//Ground constraint
				offset=0;
				overpoint.set(joints[i].position);
				overpoint.y+=10;
				underpoint.set(joints[i].position);
				underpoint.y-=offset;
				whichtri=collide.lineCheck2(overpoint,underpoint,impact,move,rotation);
				
				if(whichtri > -1 && collide.normals[whichtri].y<=.8)whichtri=collide.lineCheck2(joints[i].realoldposition,joints[i].position,impact,move,rotation);
				
				if(joints[i].position.y<=groundlevel+offset||whichtri!=-1){
					if(whichtri==-1||(whichtri!=-1&&collide.normals[whichtri].y>.8)){
						if(whichtri==-1)joints[i].position.y=groundlevel+offset;
						if(whichtri!=-1){
							joints[i].position.set(impact);
							joints[i].position.y+=offset;
						}
						joints[i].velocity.y*=-.3;
						joints[i].velocity.x*=.3;
						joints[i].velocity.z*=.3;
					}
					offset=.2f;
					
					if(whichtri!=-1&&collide.normals[whichtri].y<=.8){
						normalrotated.set(doRotation(collide.normals[whichtri],0,rotation,0));
						joints[i].position.set(impact.add(normalrotated.multiply(offset)));
						reflectVector(joints[i].velocity,normalrotated);
						joints[i].velocity.incrementMultiply(.3f);
					}
					
					if(broken<=1){
						avgvelocity.set(0);
											
						for(int k=0; k<num_joints; k++){
							avgvelocity.incrementAdd(joints[k].velocity);
						}
						
						avgvelocity.incrementDevide(rotation);
											
						if(joints[i].label==HEAD&&(findLengthfast(joints[i].velocity)>2||findLengthfast(avgvelocity)>2)) {
							
							avgvelocity.incrementAdd(joints[i].velocity);
							
							gLoc.rewind();
							
							gLoc.put(joints[i].position.x/soundscalefactor);
							gLoc.put(joints[i].position.y/soundscalefactor);
							gLoc.put(joints[i].position.z/soundscalefactor);
							
							alGetSourcei(gSourceID[HEADLANDSOUND], AL_SOURCE_STATE, tempint);
							
							tempint.rewind();
													
							if (tempint.get() != AL_PLAYING){
								gLoc.rewind();
								alSourcef(gSourceID[HEADLANDSOUND], AL_MIN_GAIN, findLengthfast(avgvelocity)*2/findDistancefast(joints[i].position,camera.position)*soundscalefactor*2);
								alSourcef(gSourceID[HEADLANDSOUND], AL_MAX_GAIN, findLengthfast(avgvelocity)*2/findDistancefast(joints[i].position,camera.position)*soundscalefactor*2);
								alSourcefv(gSourceID[HEADLANDSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[HEADLANDSOUND]);
								tempint.clear();
							}
						}
						
						avgvelocity.set(0);
						
						for(int k=0; k<num_joints; k++){
							avgvelocity.incrementAdd(joints[k].velocity);
						}
						
						avgvelocity.incrementDevide(num_joints);
		
						if((joints[i].label==ABDOMEN)&&(findLengthfast(joints[i].velocity)>2||findLengthfast(avgvelocity)>2)){
							
							avgvelocity.incrementAdd(joints[i].velocity);
							
							gLoc.rewind();
						
							gLoc.put(joints[i].position.x/soundscalefactor);
							gLoc.put(joints[i].position.y/soundscalefactor);
							gLoc.put(joints[i].position.z/soundscalefactor);
														
							alGetSourcei(gSourceID[BODYLANDSOUND], AL_SOURCE_STATE, tempint);
							
							tempint.rewind();
													
							if (tempint.get() != AL_PLAYING) {
								gLoc.rewind();
								alSourcef(gSourceID[BODYLANDSOUND], AL_MIN_GAIN, findLengthfast(joints[i].velocity)*1/findDistancefast(joints[i].position,camera.position)*soundscalefactor*2);
								alSourcef(gSourceID[BODYLANDSOUND], AL_MAX_GAIN, findLengthfast(joints[i].velocity)*1/findDistancefast(joints[i].position,camera.position)*soundscalefactor*2);
								alSourcefv(gSourceID[BODYLANDSOUND], AL_POSITION, gLoc);
								alSourcePlay(gSourceID[BODYLANDSOUND]);
								tempint.clear();
								
							}
						}
					}
				}}
			}
			if(num_muscles != 0)
			for(int i=0; i<num_muscles; i++){
				//Length constraints
				muscles[i].doConstraint(broken);
			}
		}
		
		for(int i=0; i<num_joints; i++){
			joints[i].realoldposition.set(joints[i].position);
		}
		
		//Add velocity
		for(int i=0; i<num_joints; i++){
			if(joints[i].existing||i==jointlabels[LEFTHAND]||i==jointlabels[RIGHTHAND])joints[i].position.set(joints[i].position.add(joints[i].velocity.multiply((float) multiplier)));
		}
	}

	public void doGravity()
	{
		for(int i=0; i<num_joints; i++){
			joints[i].velocity.y+=GRAVITY*multiplier;
		}
	}

	public void draw(int muscleview)
	{
		float jointcolor[] = new float[4];

		if(muscleview!=2){
			jointcolor[0]=0;
			jointcolor[1]=0;
			jointcolor[2]=.5f;
			jointcolor[3]=1;
		}
		
		if(muscleview==2){
			jointcolor[0]=0;
			jointcolor[1]=0;
			jointcolor[2]=0;
			jointcolor[3]=.5f;
		}
		//Calc motionblur-ness
		for(int i=0; i<num_joints; i++){
			joints[i].oldposition.set(joints[i].position);
			joints[i].blurred=findDistance(joints[i].position,joints[i].oldposition)*100;
			if(joints[i].blurred<1)joints[i].blurred=1;
		}

		//Do Motionblur
		glDepthMask(false);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		glBegin(GL_QUADS);
			for(int i=0; i<num_joints; i++){
				if(joints[i].hasparent){
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].blurred);
					glVertex3f(joints[i].position.x,joints[i].position.y,joints[i].position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].parent.blurred);
					glVertex3f(joints[i].parent.position.x,joints[i].parent.position.y,joints[i].parent.position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].parent.blurred);
					glVertex3f(joints[i].parent.oldposition.x,joints[i].parent.oldposition.y,joints[i].parent.oldposition.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].blurred);
					glVertex3f(joints[i].oldposition.x,joints[i].oldposition.y,joints[i].oldposition.z);
				}
			}
			for(int i=0; i<num_muscles; i++){
				if(muscles[i].type==BONECONNECT){
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent2.blurred);
					glVertex3f(muscles[i].parent1.position.x,muscles[i].parent1.position.y,muscles[i].parent1.position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent2.blurred);
					glVertex3f(muscles[i].parent2.position.x,muscles[i].parent2.position.y,muscles[i].parent2.position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent2.blurred);
					glVertex3f(muscles[i].parent2.oldposition.x,muscles[i].parent2.oldposition.y,muscles[i].parent2.oldposition.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent1.blurred);
					glVertex3f(muscles[i].parent1.oldposition.x,muscles[i].parent1.oldposition.y,muscles[i].parent1.oldposition.z);
				}
			}
			glEnd();

			glBegin(GL_LINES);
			for(int i=0; i<num_joints; i++){
				if(joints[i].hasparent){
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].blurred);
					glVertex3f(joints[i].position.x,joints[i].position.y,joints[i].position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/joints[i].parent.blurred);
					glVertex3f(joints[i].parent.position.x,joints[i].parent.position.y,joints[i].parent.position.z);
				}
			}
			for(int i=0; i<num_muscles; i++){
				if(muscles[i].type==BONECONNECT){
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent1.blurred);
					glVertex3f(muscles[i].parent1.position.x,muscles[i].parent1.position.y,muscles[i].parent1.position.z);
					glColor4f(jointcolor[0],jointcolor[1],jointcolor[2],jointcolor[3]/muscles[i].parent2.blurred);
					glVertex3f(muscles[i].parent2.position.x,muscles[i].parent2.position.y,muscles[i].parent2.position.z);
				}
			}
			glColor3f(.6f,.6f,0f);
			if(muscleview==1)
			for(int i=0; i<num_muscles; i++){
				if(muscles[i].type!=BONECONNECT){
					glVertex3f(muscles[i].parent1.position.x,muscles[i].parent1.position.y,muscles[i].parent1.position.z);
					glVertex3f(muscles[i].parent2.position.x,muscles[i].parent2.position.y,muscles[i].parent2.position.z);
				}
			}	
			glEnd();
		
		if(muscleview!=2){
			glPointSize(3);
			glBegin(GL_POINTS);
				for(int i=0; i<num_joints; i++){
					if(i!=selected)glColor4f(0,0,.5f,1);
					if(i==selected)glColor4f(1,1,0,1);
					if(joints[i].locked&&i!=selected)glColor4f(1,0,0,1);
					glVertex3f(joints[i].position.x,joints[i].position.y,joints[i].position.z);
				}
			glEnd();
		}
			
		//Set old position to current position
		if(muscleview==2) {
			for(int i=0; i<num_joints; i++){
				joints[i].oldposition.set(joints[i].position);
			}
		}
		glDepthMask(true);
	}

	public void addJoint(float x, float y, float z, int which)
	{
		if(num_joints<MAX_JOINTS-1){
			joints[num_joints].position.x=x;
			joints[num_joints].position.y=y;
			joints[num_joints].position.z=z;
			joints[num_joints].locked=false;
			
			if(which>=num_joints||which<0)joints[num_joints].hasparent=false;
			if(which<num_joints&&which>=0){
				joints[num_joints].parent=joints[which];
				joints[num_joints].hasparent=true;
				joints[num_joints].length=findDistance(joints[num_joints].position,joints[num_joints].parent.position);
			}	
			num_joints++;
		}
	}

	public void deleteJoint(int whichjoint)
	{
		if(whichjoint<num_joints&&whichjoint>=0){
			for(int i=0;i<num_muscles;i++){
				while(muscles[i].parent1==joints[whichjoint]&&i<num_muscles)deleteMuscle(i);
				while(muscles[i].parent2==joints[whichjoint]&&i<num_muscles)deleteMuscle(i);
			}
			for(int i=0;i<num_joints;i++){
				
				if(joints[i].parent == joints[whichjoint])joints[i].hasparent=false;
			}
			joints[whichjoint].hasparent=false;
		}
	}

	public void deleteMuscle(int whichmuscle)
	{
		if(whichmuscle<num_muscles){
			muscles[whichmuscle].minlength=muscles[num_muscles-1].minlength;
			muscles[whichmuscle].maxlength=muscles[num_muscles-1].maxlength;
			muscles[whichmuscle].strength=muscles[num_muscles-1].strength;
			muscles[whichmuscle].parent1.set(muscles[num_muscles-1].parent1);
			muscles[whichmuscle].parent2.set(muscles[num_muscles-1].parent2);
			muscles[whichmuscle].length=muscles[num_muscles-1].length;
			muscles[whichmuscle].visible=muscles[num_muscles-1].visible;
			muscles[whichmuscle].type=muscles[num_muscles-1].type;
			muscles[whichmuscle].targetlength=muscles[num_muscles-1].targetlength;
			
			num_muscles--;
		}
	}

	public void setJoint(float x, float y, float z, int which, int whichjoint)
	{
		if(whichjoint<num_joints){
			joints[whichjoint].position.setX(x);
			joints[whichjoint].position.setY(y);
			joints[whichjoint].position.setZ(z);
			
			if(which>=num_joints||which<0)joints[whichjoint].hasparent=false;
			if(which<num_joints&&which>=0){
				joints[whichjoint].parent=joints[which];
				joints[whichjoint].hasparent=true;
				joints[whichjoint].length=findDistance(joints[whichjoint].position,joints[whichjoint].parent.position);
			}	
		}
	}

	public void addMuscle(int attach1,int attach2,float minlength,float maxlength,int type)
	{
		if(num_muscles<MAX_MUSCLES-1&&attach1<num_joints&&attach1>=0&attach2<num_joints&&attach2>=0&&attach1!=attach2){
			muscles[num_muscles].parent1=joints[attach1];
			muscles[num_muscles].parent2=joints[attach2];
			muscles[num_muscles].length=findDistance(muscles[num_muscles].parent1.position,muscles[num_muscles].parent2.position);
			muscles[num_muscles].targetlength=findDistance(muscles[num_muscles].parent1.position,muscles[num_muscles].parent2.position);
			muscles[num_muscles].strength=.7f;
			muscles[num_muscles].type=type;
			muscles[num_muscles].minlength=minlength;
			muscles[num_muscles].maxlength=maxlength;
			
			num_muscles++;
		}
	}

	public void musclesSet()
	{
		for(int i=0;i<num_muscles;i++){
			muscles[i].length=findDistance(muscles[i].parent1.position,muscles[i].parent2.position);
		}
	}

	public void findRotationJoint(int which)
	{
		XYZ temppoint1,temppoint2,tempforward = new XYZ();
		float distance;
		
		temppoint1= new XYZ(joints[which].position);		
		temppoint2 = new XYZ(joints[which].parent.position);
		distance=findDistance(temppoint1,temppoint2);
		joints[which].rotate2=(float) (asin((temppoint1.y-temppoint2.y)/distance)*rad2deg);
		temppoint1.y=0;
		temppoint2.y=0;
		joints[which].rotate1=(float) (acos((temppoint1.z-temppoint2.z)/findDistance(temppoint1,temppoint2))*rad2deg);
		if(temppoint1.x>temppoint2.x)joints[which].rotate1=360-joints[which].rotate1;
		if(joints[which].label==HEAD)tempforward.set(specialforward[0]);
		else if(joints[which].label==RIGHTSHOULDER||joints[which].label==RIGHTELBOW||joints[which].label==RIGHTWRIST||joints[which].label==RIGHTHAND)tempforward.set(specialforward[1]);
		else if(joints[which].label==LEFTSHOULDER||joints[which].label==LEFTELBOW||joints[which].label==LEFTWRIST||joints[which].label==LEFTHAND)tempforward.set(specialforward[2]);
		else if(joints[which].label==RIGHTHIP||joints[which].label==RIGHTKNEE||joints[which].label==RIGHTANKLE)tempforward.set(specialforward[3]);
		else if(joints[which].label==LEFTHIP||joints[which].label==LEFTKNEE||joints[which].label==LEFTANKLE)tempforward.set(specialforward[4]);
		else if(!joints[which].lower)tempforward.set(forward);
		else if(joints[which].lower)tempforward.set(lowforward);
		tempforward.set(doRotation(tempforward,0,joints[which].rotate1-90,0));
		tempforward.set(doRotation(tempforward,0,0,joints[which].rotate2-90));
		tempforward.y=0;
		normalise(tempforward);
		joints[which].rotate3=(float) (acos(0-tempforward.z)*rad2deg);
		if(0>tempforward.x)joints[which].rotate3=360-joints[which].rotate3;
	}

	public void findRotationMuscle(int which)
	{
		XYZ temppoint1,temppoint2,tempforward = new XYZ();
		float distance;
		
		temppoint1= new XYZ(muscles[which].parent1.position);
		temppoint2= new XYZ(muscles[which].parent2.position);
		distance=findDistance(temppoint1,temppoint2);
		muscles[which].rotate2=(float) (asin((temppoint1.y-temppoint2.y)/distance)*rad2deg);
		temppoint1.y=0;
		temppoint2.y=0;
		muscles[which].rotate1=(float) acos((temppoint1.z-temppoint2.z)/findDistance(temppoint1,temppoint2));
		muscles[which].rotate1*=360/6.28;
		
		if(temppoint1.x>temppoint2.x)muscles[which].rotate1=360-muscles[which].rotate1;
		if(muscles[which].parent1.label==HEAD)tempforward.set(specialforward[0]);
		else if(muscles[which].parent1.label==RIGHTSHOULDER||muscles[which].parent1.label==RIGHTELBOW||muscles[which].parent1.label==RIGHTWRIST)tempforward.set(specialforward[1]);
		else if(muscles[which].parent1.label==LEFTSHOULDER||muscles[which].parent1.label==LEFTELBOW||muscles[which].parent1.label==LEFTWRIST)tempforward.set(specialforward[2]);
		else if(muscles[which].parent1.label==RIGHTHIP||muscles[which].parent1.label==RIGHTKNEE||muscles[which].parent1.label==RIGHTANKLE)tempforward.set(specialforward[3]);
		else if(muscles[which].parent1.label==LEFTHIP||muscles[which].parent1.label==LEFTKNEE||muscles[which].parent1.label==LEFTANKLE)tempforward.set(specialforward[4]);
		else if(!muscles[which].parent1.lower)tempforward.set(forward);
		else if(muscles[which].parent1.lower)tempforward.set(lowforward);
		
		tempforward.set(doRotation(tempforward,0,muscles[which].rotate1-90,0));
		tempforward.set(doRotation(tempforward,0,0,muscles[which].rotate2-90));
		tempforward.y=0;
		normalise(tempforward);
		muscles[which].rotate3=(float) (acos(0-tempforward.z)*rad2deg);
		
		for(int i=0;i<num_joints;i++){
			if(joints[i]==muscles[which].parent1){
				joints[i].rotate1=muscles[which].rotate1;
				joints[i].rotate2=muscles[which].rotate2;
				joints[i].rotate3=muscles[which].rotate3;
			}
		}
		if(0>tempforward.x)muscles[which].rotate3=360-muscles[which].rotate3;
	}
	
	public static class Animation {
	
		public InputStream files;
		public int numframes;
		public boolean canbeoverridden;
		public boolean ismodified[][] = new boolean[MAX_JOINTS][MAX_FRAMES];
		public XYZ position[][] = new XYZ[MAX_JOINTS][MAX_FRAMES];
		public boolean onground[][] = new boolean[MAX_JOINTS][MAX_FRAMES];
		public XYZ forward[] = new XYZ[MAX_FRAMES];
		
		public float
			twist[][] = new float[MAX_JOINTS][MAX_FRAMES],
			twist2[][] = new float[MAX_JOINTS][MAX_FRAMES],
			speed[] = new float[MAX_FRAMES],
			gunrotation[] = new float[MAX_FRAMES],
			rotate1[][] = new float[MAX_JOINTS][MAX_FRAMES],rotate2[][] = new float[MAX_JOINTS][MAX_FRAMES],rotate3[][] = new float[MAX_JOINTS][MAX_FRAMES],
			mrotate1[][] = new float[MAX_JOINTS][MAX_FRAMES],mrotate2[][] = new float[MAX_JOINTS][MAX_FRAMES],mrotate3[][] = new float[MAX_JOINTS][MAX_FRAMES];
		
		public void load(String fileName) throws IOException
		{
			
			files = Models.class.getClassLoader().getResourceAsStream(fileName);
						
			if(files!=null){
				numframes = readInt(files, 1);
				for(int i=0;i<numframes;i++){
					for(int j=0;j<MAX_JOINTS;j++){
						position[j][i] = readXYZ(files, 1);
					}
					for(int j=0;j<MAX_JOINTS;j++){
						twist[j][i] = readFloat(files, 1);
					}
					for(int j=0;j<MAX_JOINTS;j++){
						onground[j][i] = readBool(files, 1);
					}
					speed[i] = readFloat(files, 1);
					gunrotation[i] = readFloat(files, 1);
				}
				for(int i=0;i<numframes;i++){
					for(int j=0;j<MAX_JOINTS;j++){
						twist2[j][i] = readFloat(files, 1);
					}
				}
			}
			
			files.close();
									
			for(int j=0;j<numframes;j++){
				for(int i=0;i<testskeleton.num_joints;i++){
					testskeleton.joints[i].position.set(position[i][j]);	
				}
				
				//Find forward vectors
				crossProduct(testskeleton.joints[testskeleton.forwardjoints[1]].position.subtract(testskeleton.joints[testskeleton.forwardjoints[0]].position),testskeleton.joints[testskeleton.forwardjoints[2]].position.subtract(testskeleton.joints[testskeleton.forwardjoints[0]].position),testskeleton.forward);
				normalise(testskeleton.forward);
				
				crossProduct(testskeleton.joints[testskeleton.lowforwardjoints[1]].position.subtract(testskeleton.joints[testskeleton.lowforwardjoints[0]].position),testskeleton.joints[testskeleton.lowforwardjoints[2]].position.subtract(testskeleton.joints[testskeleton.lowforwardjoints[0]].position),testskeleton.lowforward);
				normalise(testskeleton.lowforward);
				
				//Special forwards
				if(testskeleton.specialforward[0] == null)testskeleton.specialforward[0] = new XYZ();
				
				testskeleton.specialforward[0].set(testskeleton.forward);
				
				if(testskeleton.specialforward[1] == null) testskeleton.specialforward[1] = new XYZ(); 
				testskeleton.specialforward[1].set(testskeleton.joints[testskeleton.jointlabels[RIGHTSHOULDER]].position.add(testskeleton.joints[testskeleton.jointlabels[RIGHTWRIST]].position));
				testskeleton.specialforward[1].set(testskeleton.joints[testskeleton.jointlabels[RIGHTELBOW]].position.subtract(testskeleton.specialforward[1].devide(2)));
				testskeleton.specialforward[1].incrementAdd(testskeleton.forward.multiply(.2f));
				normalise(testskeleton.specialforward[1]);
				
				if(testskeleton.specialforward[2] == null) testskeleton.specialforward[2] = new XYZ(); 
				testskeleton.specialforward[2].set(testskeleton.joints[testskeleton.jointlabels[LEFTSHOULDER]].position.add(testskeleton.joints[testskeleton.jointlabels[LEFTWRIST]].position));
				testskeleton.specialforward[2].set(testskeleton.joints[testskeleton.jointlabels[LEFTELBOW]].position.subtract(testskeleton.specialforward[2].devide(2)));
				testskeleton.specialforward[2].incrementAdd(testskeleton.forward.multiply(.2f));
				normalise(testskeleton.specialforward[2]);
				
				if(testskeleton.specialforward[3] == null) testskeleton.specialforward[3] = new XYZ(); 
				testskeleton.specialforward[3].set(testskeleton.joints[testskeleton.jointlabels[RIGHTHIP]].position.add(testskeleton.joints[testskeleton.jointlabels[RIGHTANKLE]].position));
				testskeleton.specialforward[3].set(testskeleton.specialforward[3].devide(2).subtract(testskeleton.joints[testskeleton.jointlabels[RIGHTKNEE]].position));
				testskeleton.specialforward[3].incrementAdd(testskeleton.lowforward.multiply(.2f));
				normalise(testskeleton.specialforward[3]);
				
				if(testskeleton.specialforward[4] == null) testskeleton.specialforward[4] = new XYZ(); 
				testskeleton.specialforward[4].set(testskeleton.joints[testskeleton.jointlabels[LEFTHIP]].position.add(testskeleton.joints[testskeleton.jointlabels[LEFTANKLE]].position));
				testskeleton.specialforward[4].set(testskeleton.specialforward[4].devide(2).subtract(testskeleton.joints[testskeleton.jointlabels[LEFTKNEE]].position));
				testskeleton.specialforward[4].incrementAdd(testskeleton.lowforward.multiply(.2f));
				normalise(testskeleton.specialforward[4]);

				//Find joint rotations
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						testskeleton.findRotationJoint(i);
					}
				}
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						testskeleton.findRotationMuscle(i);
					}
				}
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						mrotate1[i][j]=testskeleton.muscles[i].rotate1;
						mrotate2[i][j]=testskeleton.muscles[i].rotate2;
						mrotate3[i][j]=testskeleton.muscles[i].rotate3;
						if(j!=0&&mrotate3[i][j]>mrotate3[i][j-1]+180)mrotate3[i][j]-=360;
						if(j!=0&&mrotate3[i][j]<mrotate3[i][j-1]-180)mrotate3[i][j]+=360;
						if(j!=0&&mrotate2[i][j]>mrotate2[i][j-1]+180)mrotate2[i][j]-=360;
						if(j!=0&&mrotate2[i][j]<mrotate2[i][j-1]-180)mrotate2[i][j]+=360;
						if(j!=0&&mrotate1[i][j]>mrotate1[i][j-1]+180)mrotate1[i][j]-=360;
						if(j!=0&&mrotate1[i][j]<mrotate1[i][j-1]-180)mrotate1[i][j]+=360;
					}
				}
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						rotate1[i][j]=testskeleton.joints[i].rotate1;
						rotate2[i][j]=testskeleton.joints[i].rotate2;
						rotate3[i][j]=testskeleton.joints[i].rotate3;
						if(j!=0&&rotate3[i][j]>rotate3[i][j-1]+180)rotate3[i][j]-=360;
						if(j!=0&&rotate3[i][j]<rotate3[i][j-1]-180)rotate3[i][j]+=360;
						if(j!=0&&rotate2[i][j]>rotate2[i][j-1]+180)rotate2[i][j]-=360;
						if(j!=0&&rotate2[i][j]<rotate2[i][j-1]-180)rotate2[i][j]+=360;
						if(j!=0&&rotate1[i][j]>rotate1[i][j-1]+180)rotate1[i][j]-=360;
						if(j!=0&&rotate1[i][j]<rotate1[i][j-1]-180)rotate1[i][j]+=360;
					}
				}
			}
			
			for(int k=0;k<2;k++)
			for(int j=0;j<numframes;j++){
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						if(j!=0&&mrotate3[i][j]>mrotate3[i][j-1]+180)mrotate3[i][j]-=360;
						if(j!=0&&mrotate3[i][j]<mrotate3[i][j-1]-180)mrotate3[i][j]+=360;
						if(j!=0&&mrotate2[i][j]>mrotate2[i][j-1]+180)mrotate2[i][j]-=360;
						if(j!=0&&mrotate2[i][j]<mrotate2[i][j-1]-180)mrotate2[i][j]+=360;
						if(j!=0&&mrotate1[i][j]>mrotate1[i][j-1]+180)mrotate1[i][j]-=360;
						if(j!=0&&mrotate1[i][j]<mrotate1[i][j-1]-180)mrotate1[i][j]+=360;
						
						if(j==0&&mrotate3[i][j]>mrotate3[i][numframes-1]+180)mrotate3[i][j]-=360;
						if(j==0&&mrotate3[i][j]<mrotate3[i][numframes-1]-180)mrotate3[i][j]+=360;
						if(j==0&&mrotate2[i][j]>mrotate2[i][numframes-1]+180)mrotate2[i][j]-=360;
						if(j==0&&mrotate2[i][j]<mrotate2[i][numframes-1]-180)mrotate2[i][j]+=360;
						if(j==0&&mrotate1[i][j]>mrotate1[i][numframes-1]+180)mrotate1[i][j]-=360;
						if(j==0&&mrotate1[i][j]<mrotate1[i][numframes-1]-180)mrotate1[i][j]+=360;
					}
				}
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						if(j!=0&&rotate3[i][j]>rotate3[i][j-1]+180)rotate3[i][j]-=360;
						if(j!=0&&rotate3[i][j]<rotate3[i][j-1]-180)rotate3[i][j]+=360;
						if(j!=0&&rotate2[i][j]>rotate2[i][j-1]+180)rotate2[i][j]-=360;
						if(j!=0&&rotate2[i][j]<rotate2[i][j-1]-180)rotate2[i][j]+=360;
						if(j!=0&&rotate1[i][j]>rotate1[i][j-1]+180)rotate1[i][j]-=360;
						if(j!=0&&rotate1[i][j]<rotate1[i][j-1]-180)rotate1[i][j]+=360;
						
						if(j==0&&rotate3[i][j]>rotate3[i][numframes-1]+180)rotate3[i][j]-=360;
						if(j==0&&rotate3[i][j]<rotate3[i][numframes-1]-180)rotate3[i][j]+=360;
						if(j==0&&rotate2[i][j]>rotate2[i][numframes-1]+180)rotate2[i][j]-=360;
						if(j==0&&rotate2[i][j]<rotate2[i][numframes-1]-180)rotate2[i][j]+=360;
						if(j==0&&rotate1[i][j]>rotate1[i][numframes-1]+180)rotate1[i][j]-=360;
						if(j==0&&rotate1[i][j]<rotate1[i][numframes-1]-180)rotate1[i][j]+=360;
					}
				}
			}
		}

		public void load(String fileName, float rotate) throws IOException
		{
			
			InputStream files = Models.class.getClassLoader().getResourceAsStream(fileName);
						
			if(files!=null){
				numframes = readInt(files, 1);
				for(int i=0;i<numframes;i++){
					for(int j=0;j<MAX_JOINTS;j++){
						position[j][i] = readXYZ(files, 1);
						position[j][i]=doRotation(position[j][i],0,rotate,0);
					}
					for(int j=0;j<MAX_JOINTS;j++){
						twist[j][i] = readFloat(files, 1);
					}
					for(int j=0;j<MAX_JOINTS;j++){
						onground[j][i] = readBool(files, 1);
					}
					speed[i] = readFloat(files, 1);
					gunrotation[i] = readFloat(files, 1);
				}
				for(int i=0;i<numframes;i++){
					for(int j=0;j<MAX_JOINTS;j++){
						twist2[j][i] = readFloat(files, 1);
					}
				}
			}
			files.close();
			
			for(int j=0;j<numframes;j++){
				for(int i=0;i<testskeleton.num_joints;i++){
					testskeleton.joints[i].position.set(position[i][j]);	
				}
				//Find forward vectors
				crossProduct(testskeleton.joints[testskeleton.forwardjoints[1]].position.subtract(testskeleton.joints[testskeleton.forwardjoints[0]].position),testskeleton.joints[testskeleton.forwardjoints[2]].position.subtract(testskeleton.joints[testskeleton.forwardjoints[0]].position),testskeleton.forward);
				normalise(testskeleton.forward);
				
				crossProduct(testskeleton.joints[testskeleton.lowforwardjoints[1]].position.subtract(testskeleton.joints[testskeleton.lowforwardjoints[0]].position),testskeleton.joints[testskeleton.lowforwardjoints[2]].position.subtract(testskeleton.joints[testskeleton.lowforwardjoints[0]].position),testskeleton.lowforward);
				normalise(testskeleton.lowforward);
				
				//Special forwards
				testskeleton.specialforward[0].set(testskeleton.forward);
				
				testskeleton.specialforward[1].set(testskeleton.joints[testskeleton.jointlabels[RIGHTSHOULDER]].position.add(testskeleton.joints[testskeleton.jointlabels[RIGHTWRIST]].position));
				testskeleton.specialforward[1].set(testskeleton.joints[testskeleton.jointlabels[RIGHTELBOW]].position.subtract(testskeleton.specialforward[1].devide(2)));
				testskeleton.specialforward[1].incrementAdd(testskeleton.forward.multiply(.2f));
				normalise(testskeleton.specialforward[1]);
				
				testskeleton.specialforward[2].set(testskeleton.joints[testskeleton.jointlabels[LEFTSHOULDER]].position.add(testskeleton.joints[testskeleton.jointlabels[LEFTWRIST]].position));
				testskeleton.specialforward[2].set(testskeleton.joints[testskeleton.jointlabels[LEFTELBOW]].position.subtract(testskeleton.specialforward[2].devide(2)));
				testskeleton.specialforward[2].incrementAdd(testskeleton.forward.multiply(.2f));
				normalise(testskeleton.specialforward[2]);
				
				testskeleton.specialforward[3].set(testskeleton.joints[testskeleton.jointlabels[RIGHTHIP]].position.add(testskeleton.joints[testskeleton.jointlabels[RIGHTANKLE]].position));
				testskeleton.specialforward[3].set(testskeleton.specialforward[3].devide(2).subtract(testskeleton.joints[testskeleton.jointlabels[RIGHTKNEE]].position));
				testskeleton.specialforward[3].incrementAdd(testskeleton.lowforward.multiply(.2f));
				normalise(testskeleton.specialforward[3]);
				
				testskeleton.specialforward[4].set(testskeleton.joints[testskeleton.jointlabels[LEFTHIP]].position.add(testskeleton.joints[testskeleton.jointlabels[LEFTANKLE]].position));
				testskeleton.specialforward[4].set(testskeleton.specialforward[4].devide(2).subtract(testskeleton.joints[testskeleton.jointlabels[LEFTKNEE]].position));
				testskeleton.specialforward[4].incrementAdd(testskeleton.lowforward.multiply(.2f));
				normalise(testskeleton.specialforward[4]);

				//Find joint rotations
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						testskeleton.findRotationJoint(i);
					}
				}
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						testskeleton.findRotationMuscle(i);
					}
				}
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						mrotate1[i][j]=testskeleton.muscles[i].rotate1;
						mrotate2[i][j]=testskeleton.muscles[i].rotate2;
						mrotate3[i][j]=testskeleton.muscles[i].rotate3;
						if(j!=0&&mrotate3[i][j]>mrotate3[i][j-1]+180)mrotate3[i][j]-=360;
						if(j!=0&&mrotate3[i][j]<mrotate3[i][j-1]-180)mrotate3[i][j]+=360;
						if(j!=0&&mrotate2[i][j]>mrotate2[i][j-1]+180)mrotate2[i][j]-=360;
						if(j!=0&&mrotate2[i][j]<mrotate2[i][j-1]-180)mrotate2[i][j]+=360;
						if(j!=0&&mrotate1[i][j]>mrotate1[i][j-1]+180)mrotate1[i][j]-=360;
						if(j!=0&&mrotate1[i][j]<mrotate1[i][j-1]-180)mrotate1[i][j]+=360;
					}
				}
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						rotate1[i][j]=testskeleton.joints[i].rotate1;
						rotate2[i][j]=testskeleton.joints[i].rotate2;
						rotate3[i][j]=testskeleton.joints[i].rotate3;
						if(j!=0&&rotate3[i][j]>rotate3[i][j-1]+180)rotate3[i][j]-=360;
						if(j!=0&&rotate3[i][j]<rotate3[i][j-1]-180)rotate3[i][j]+=360;
						if(j!=0&&rotate2[i][j]>rotate2[i][j-1]+180)rotate2[i][j]-=360;
						if(j!=0&&rotate2[i][j]<rotate2[i][j-1]-180)rotate2[i][j]+=360;
						if(j!=0&&rotate1[i][j]>rotate1[i][j-1]+180)rotate1[i][j]-=360;
						if(j!=0&&rotate1[i][j]<rotate1[i][j-1]-180)rotate1[i][j]+=360;
					}
				}
			}
			
			for(int k=0;k<2;k++)
			for(int j=0;j<numframes;j++){
				for(int i=0;i<testskeleton.num_muscles;i++){
					if(testskeleton.muscles[i].visible)
					{
						if(j!=0&&mrotate3[i][j]>mrotate3[i][j-1]+180)mrotate3[i][j]-=360;
						if(j!=0&&mrotate3[i][j]<mrotate3[i][j-1]-180)mrotate3[i][j]+=360;
						if(j!=0&&mrotate2[i][j]>mrotate2[i][j-1]+180)mrotate2[i][j]-=360;
						if(j!=0&&mrotate2[i][j]<mrotate2[i][j-1]-180)mrotate2[i][j]+=360;
						if(j!=0&&mrotate1[i][j]>mrotate1[i][j-1]+180)mrotate1[i][j]-=360;
						if(j!=0&&mrotate1[i][j]<mrotate1[i][j-1]-180)mrotate1[i][j]+=360;
						
						if(j==0&&mrotate3[i][j]>mrotate3[i][numframes-1]+180)mrotate3[i][j]-=360;
						if(j==0&&mrotate3[i][j]<mrotate3[i][numframes-1]-180)mrotate3[i][j]+=360;
						if(j==0&&mrotate2[i][j]>mrotate2[i][numframes-1]+180)mrotate2[i][j]-=360;
						if(j==0&&mrotate2[i][j]<mrotate2[i][numframes-1]-180)mrotate2[i][j]+=360;
						if(j==0&&mrotate1[i][j]>mrotate1[i][numframes-1]+180)mrotate1[i][j]-=360;
						if(j==0&&mrotate1[i][j]<mrotate1[i][numframes-1]-180)mrotate1[i][j]+=360;
					}
				}
				for(int i=0;i<testskeleton.num_joints;i++){
					if(testskeleton.joints[i].hasparent&&testskeleton.joints[i].visible)
					{
						if(j!=0&&rotate3[i][j]>rotate3[i][j-1]+180)rotate3[i][j]-=360;
						if(j!=0&&rotate3[i][j]<rotate3[i][j-1]-180)rotate3[i][j]+=360;
						if(j!=0&&rotate2[i][j]>rotate2[i][j-1]+180)rotate2[i][j]-=360;
						if(j!=0&&rotate2[i][j]<rotate2[i][j-1]-180)rotate2[i][j]+=360;
						if(j!=0&&rotate1[i][j]>rotate1[i][j-1]+180)rotate1[i][j]-=360;
						if(j!=0&&rotate1[i][j]<rotate1[i][j-1]-180)rotate1[i][j]+=360;
						
						if(j==0&&rotate3[i][j]>rotate3[i][numframes-1]+180)rotate3[i][j]-=360;
						if(j==0&&rotate3[i][j]<rotate3[i][numframes-1]-180)rotate3[i][j]+=360;
						if(j==0&&rotate2[i][j]>rotate2[i][numframes-1]+180)rotate2[i][j]-=360;
						if(j==0&&rotate2[i][j]<rotate2[i][numframes-1]-180)rotate2[i][j]+=360;
						if(j==0&&rotate1[i][j]>rotate1[i][numframes-1]+180)rotate1[i][j]-=360;
						if(j==0&&rotate1[i][j]<rotate1[i][numframes-1]-180)rotate1[i][j]+=360;
					}
				}
			}
		}
	}

	public void load(String fileName) throws IOException
	{
		int parentID;
		boolean what;
		boolean lock;
		float placeholder[] = new float[3];
		
		forward = new XYZ();
		lowforward = new XYZ();
		
		avgvelocity = new XYZ();
		
		gLoc = createFloatBuffer(3);
		tempint = createIntBuffer(3);
		
		files = Models.class.getClassLoader().getResourceAsStream(fileName);
		
			
		if(files!=null){
			num_joints = readInt(files, 1);
			for(int i=0;i<num_joints;i++){
				if(joints[i] == null)joints[i] = new Joint();
				else joints[i].set(0);
				joints[i].position = readXYZ(files, 1);
				joints[i].length = readFloat(files, 1);
				joints[i].mass = readFloat(files, 1);
				joints[i].hasparent = readBool(files, 1);
				joints[i].locked = readBool(files, 1);
				joints[i].modelnum = readInt(files, 1);
				joints[i].visible = readBool(files, 1);
				what = readBool(files, 1);
				joints[i].label = readInt(files, 1);
				joints[i].hasgun = readInt(files, 1);
				joints[i].lower = readBool(files, 1);
				parentID = readInt(files, 1);
				if(joints[i].hasparent) {
					if(joints[parentID]==null)joints[i].parent = joints[parentID]=new Joint();
					else joints[i].parent=joints[parentID];
				}
				joints[i].velocity = new XYZ();
				joints[i].oldposition = new XYZ(joints[i].position);
				joints[i].existing=true;
			}

			num_muscles = readInt(files, 1);
			for(int i=0;i<num_muscles;i++){
				muscles[i] = new Muscle();
				muscles[i].length = readFloat(files, 1);
				muscles[i].targetlength = readFloat(files, 1);
				muscles[i].minlength = readFloat(files, 1);
				muscles[i].maxlength = readFloat(files, 1);
				muscles[i].strength = readFloat(files, 1);
				muscles[i].type = readInt(files, 1);
				muscles[i].visible = readBool(files, 1);
				parentID = readInt(files, 1);
				muscles[i].parent1= joints[parentID];
				parentID = readInt(files, 1);
				muscles[i].parent2=joints[parentID];
			}

			for(int j=0;j<3;j++){
				forwardjoints[j] = readInt(files, 1);
			}
			for(int j=0;j<3;j++){
				lowforwardjoints[j] = readInt(files, 1);
			}
		}
		files.close();
		
		for(int i=0;i<num_joints;i++){
			for(int j=0;j<num_joints;j++){
				if(joints[i].label==j)jointlabels[j]=i;
			}
		}
		
		broken=0;	
	}


}
