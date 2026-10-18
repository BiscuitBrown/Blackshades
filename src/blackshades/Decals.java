package blackshades;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import static blackshades.Quaternions.*;
import static blackshades.Constants.*;
import static blackshades.Textures.*;
import static blackshades.Globals.*;
import static java.lang.Math.*;
import blackshades.Models.*;


public class Decals {

	//Functions

	public static final int MAXDECALS = 120;

	public int 				bulletholetextureptr;
	public int 				cratertextureptr;
	public int 				bloodtextureptr[];

	public int howmanydecals;

	public int type[];

	public XYZ points[];
	public int numpoints[];
	public float texcoordsx[];
	public float texcoordsy[];
	public float alivetime[];
	public int major=0;
	public float normalv[];
	public XYZ 
		location,
		normal,
		move,
		right,
		up,
		nothing,
		axis[],
		temp;

	public Decals() {
		
		this.right = new XYZ();
		this.up = new XYZ();
		this.nothing = new XYZ();
		this.axis = new XYZ[3];
		this.temp = new XYZ();
		
		this.bloodtextureptr = new int[11];
		this.type = new int[MAXDECALS];

		this.points = new XYZ[8*MAXDECALS];
		this.numpoints = new int [MAXDECALS];
		this.texcoordsx = new float[8*MAXDECALS];
		this.texcoordsy = new float[8*MAXDECALS];
		this.alivetime = new float[MAXDECALS];
		this.normalv = new float[3];
		
		this.axis[0] = new XYZ();
		this.axis[1] = new XYZ();
		this.axis[2] = new XYZ();
		
		this.location = new XYZ();
		this.normal = new XYZ();
		this.move= new XYZ();
		
		for(int i = 0; i < 8*MAXDECALS; i++) {
			this.points[i] = new XYZ();
		}
	}

	public int makeDecal(int atype, XYZ location, float size, XYZ normal, int poly, Model model, XYZ move, float rotation){
		this.major=0;
		
		this.location.set(location);
		this.normal.set(normal);
		this.move.set(move);

		this.axis[0].set(0);
		this.axis[0].setX(1);
		this.axis[1].set(0);
		this.axis[1].setY(1);
		this.axis[2].set(0);
		this.axis[2].setZ(1);

		normalv[0]=abs(this.normal.getX());
		normalv[1]=abs(this.normal.getY());
		normalv[2]=abs(this.normal.getZ());


		if(normalv[1]>normalv[major])major=1;
		if(normalv[2]>normalv[major])major=2;

		if (normalv[0] == 1 || normalv[1] == 1 || normalv[2] == 1)
		{
		if ((major == 0 && this.normal.getX() > 0) || major == 1){
			right.set(0);
			right.setZ(-1);}
		else if (major == 0){
			right.set(0);
			right.setZ(1);}
	    else {
	    	right.set(0);
			right.setX(this.normal.getZ());}
	   	}
	   	else
	    	crossProduct(axis[major], this.normal, right);

	    crossProduct(this.normal,right, up);
	    normalise(up);
	    normalise(right);

		float count;
		float count2;
		float countinc=1/size;
		if(countinc<.01f)countinc=.01f;
		if(countinc>.2f)countinc=.2f;
		float normaloffset=.02f;
		int good;

		numpoints[howmanydecals]=0;
	    points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right).subtract(up).multiply(size / 3)))  /*+ normal/100*/;
	    texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    if((this.move.getX()==0&&this.move.getZ()==0&&rotation==0)||
	    lineFacetd(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)), points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)), model.vertex[model.triangles[poly].vertex[0]], model.vertex[model.triangles[poly].vertex[1]], model.vertex[model.triangles[poly].vertex[2]],this.normal,temp)!=0
	    )numpoints[howmanydecals]++;
	    else {
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right).subtract(up.multiply(count)).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	good=-1;
	    	count=1-countinc;

	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right.multiply(count)).subtract(up).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	if(good==-1){
	    		good=-1;
		    	count2=1-countinc;
		    	while(good==-1&&count2>-1){
		    		count=1-countinc;
			    	while(good==-1&&count>-1){
			    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f-count2/2;
			    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
			    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right.multiply(count2)).subtract(up.multiply(count)).multiply(size / 3)));
			    		count-=countinc;
			    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
			    	}
			    	count2-=countinc;
			    }
		    	if(good!=-1)numpoints[howmanydecals]++;
	    	}
	    }

	    points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right).subtract(up).multiply(size / 3))) /*+ this.normal/100*/;
	    texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    if((this.move.getX()==0&&this.move.getY()==0&&this.move.getZ()==0&&rotation==0)||
	    lineFacetd(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)), model.vertex[model.triangles[poly].vertex[0]], model.vertex[model.triangles[poly].vertex[1]], model.vertex[model.triangles[poly].vertex[2]],this.normal,temp)!=0
	    )numpoints[howmanydecals]++;
	    else {
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right.multiply(count)).subtract(up).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right).subtract(up.multiply(count)).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	if(good==-1){
	    		good=-1;
		    	count2=1-countinc;
		    	while(good==-1&&count2>-1){
		    		count=1-countinc;
			    	while(good==-1&&count>-1){
			    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f+count2/2;
			    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
			    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right.multiply(count2)).subtract(up.multiply(count)).multiply(size / 3)));
			    		count-=countinc;
			    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
			    	}
			    	count2-=countinc;
			    }
		    	if(good!=-1)numpoints[howmanydecals]++;
	    	}
	    }
	    
	    points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right).add(up).multiply(size/3))) /*+ this.normal/100*/;
	    texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    if((this.move.x==0&&this.move.y==0&&this.move.z==0&&rotation==0)||
	    lineFacetd(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)), model.vertex[model.triangles[poly].vertex[0]], model.vertex[model.triangles[poly].vertex[1]], model.vertex[model.triangles[poly].vertex[2]],this.normal,temp)!=0
	    )numpoints[howmanydecals]++;
	    else {
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right).add(up.multiply(count)).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right.multiply(count)).add(up).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	if(good==-1){
	    		good=-1;
		    	count2=1-countinc;
		    	while(good==-1&&count2>-1){
		    		count=1-countinc;
			    	while(good==-1&&count>-1){
			    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f+count2/2;
			    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
			    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.add(right.multiply(count2)).add(up.multiply(count)).multiply(size / 3)));
			    		count-=countinc;
			    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
			    	}
			    	count2-=countinc;
			    }
		    	if(good!=-1)numpoints[howmanydecals]++;
	    	}
	    }
	    
	    points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right).add(up).multiply(size / 3))) /*+ this.normal/100*/;
		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    if((this.move.getX()==0&&this.move.getY()==0&&this.move.getZ()==0&&rotation==0)||
	    lineFacetd(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)), model.vertex[model.triangles[poly].vertex[0]], model.vertex[model.triangles[poly].vertex[1]], model.vertex[model.triangles[poly].vertex[2]],this.normal,temp)!=0
	    )numpoints[howmanydecals]++;
	    else {
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f-count/2;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = 1;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right.multiply(count)).add(up).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	good=-1;
	    	count=1-countinc;
	    	while(good==-1&&count>-1){
	    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = 0;
	    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
	    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right).add(up.multiply(count)).multiply(size / 3)));
	    		count-=countinc;
	    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
	    	}
	    	if(good!=-1)numpoints[howmanydecals]++;
	    	if(good==-1){
	    		good=-1;
		    	count2=1-countinc;
		    	while(good==-1&&count2>-1){
		    		count=1-countinc;
			    	while(good==-1&&count>-1){
			    		texcoordsx[howmanydecals*8+numpoints[howmanydecals]] = .5f-count2/2;
			    		texcoordsy[howmanydecals*8+numpoints[howmanydecals]] = .5f+count/2;
			    		points[howmanydecals*8+numpoints[howmanydecals]].set(this.location.add(nothing.subtract(right.multiply(count2)).add(up.multiply(count)).multiply(size / 3)));
			    		count-=countinc;
			    		good=model.lineCheck2(points[howmanydecals*8+numpoints[howmanydecals]].add(this.normal.devide(25)),points[howmanydecals*8+numpoints[howmanydecals]].subtract(this.normal.devide(25)),temp,this.move,rotation);
			    	}
			    	count2-=countinc;
			    }
		    	if(good!=-1)numpoints[howmanydecals]++;
	    	}
	    }
	    for(int i=0;i<numpoints[howmanydecals];i++){
	    	 points[howmanydecals*8+i].incrementAdd(this.normal.multiply(normaloffset));
	    }
	    
	    type[howmanydecals]=atype;
		alivetime[howmanydecals]=0;
		if(howmanydecals<MAXDECALS){howmanydecals++;}

		return 0;
	}

	public int deleteDecal(int which){
		if(which>=0){
			numpoints[which]=numpoints[howmanydecals-1];
			alivetime[which]=alivetime[howmanydecals-1];
			type[which]=type[howmanydecals-1];
			for(int i=0;i<numpoints[which];i++){
				points[which*8+i].set(points[howmanydecals*8-8+i]);
			    texcoordsx[which*8+i] = texcoordsx[howmanydecals*8-8+i];
			    texcoordsy[which*8+i] = texcoordsy[howmanydecals*8-8+i];
		    }
			if(howmanydecals>0){howmanydecals--;}
		}

		return 0;
	}

	public void loadBulletHoleTexture(String fileName) throws IOException
	{
		bulletholetextureptr = loadTexture(fileName);
	}

	public void loadBloodTexture(String fileName, int which) throws IOException
	{
		bloodtextureptr[which] = loadTexture(fileName);
	}

	public void loadCraterTexture(String fileName) throws IOException
	{
		cratertextureptr = loadTexture(fileName);
	}

	public void doStuff()
	{
		for(int i=0;i<howmanydecals;i++){
			alivetime[i]+=multiplier;
			if(alivetime[i]>10&&(type[i]==BULLETHOLE||type[i]==CRATER))deleteDecal(i);
			if(alivetime[i]>20&&(type[i]==BLOODPOOL))deleteDecal(i);
		}
	}

	public void draw()
	{
		glAlphaFunc(GL_GREATER, 0.01f);

		float bloodpoolspeed=1;

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_LIGHTING);
		glDepthMask(false);
		glAlphaFunc(GL_GREATER, 0.01f);
		glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP );
		glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP );
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_POLYGON_OFFSET_FILL);

		for(int i=0;i<howmanydecals;i++){
			if(type[i]==BULLETHOLE) glBindTexture(GL_TEXTURE_2D, bulletholetextureptr);
			if(type[i]==CRATER)glBindTexture(GL_TEXTURE_2D, cratertextureptr);
			if(type[i]!=BLOODPOOL)glColor4f(1,1,1,10-alivetime[i]);

			if(type[i]==BLOODPOOL&&alivetime[i]<bloodpoolspeed*.2)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[0]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.2&&alivetime[i]<bloodpoolspeed*.4)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[1]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.4&&alivetime[i]<bloodpoolspeed*.6)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[2]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.6&&alivetime[i]<bloodpoolspeed*.8)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[3]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.8&&alivetime[i]<bloodpoolspeed*1)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[4]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1&&alivetime[i]<bloodpoolspeed*1.2)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[5]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.2&&alivetime[i]<bloodpoolspeed*1.4)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[6]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.4&&alivetime[i]<bloodpoolspeed*1.6)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[7]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.6&&alivetime[i]<bloodpoolspeed*1.8)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[8]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.8&&alivetime[i]<bloodpoolspeed*2.0)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[9]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*2.0)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[10]);
			if(type[i]==BLOODPOOL&&alivetime[i]<bloodpoolspeed*2.0)glColor4f(1f,1f,1f,1.5f-(alivetime[i]*5/bloodpoolspeed-(int)(alivetime[i]*5/bloodpoolspeed)));
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*2.0)glColor4f(1,1,1,20-alivetime[i]);

			glPushMatrix();
			glBegin(GL_TRIANGLE_FAN);
				for(int j=0;j<numpoints[i];j++){
					glTexCoord2f(texcoordsx[i*8+j], texcoordsy[i*8+j]); glVertex3f(points[i*8+j].getX(),points[i*8+j].getY(),points[i*8+j].getZ());
				}
			glEnd();
			glPopMatrix();

			if(type[i]==BLOODPOOL&&alivetime[i]<bloodpoolspeed*2.0){
				if(type[i]==BLOODPOOL&&alivetime[i]<bloodpoolspeed*.2)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[1]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.2&&alivetime[i]<bloodpoolspeed*.4)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[2]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.4&&alivetime[i]<bloodpoolspeed*.6)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[3]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.6&&alivetime[i]<bloodpoolspeed*.8)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[4]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*.8&&alivetime[i]<bloodpoolspeed*1)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[5]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1&&alivetime[i]<bloodpoolspeed*1.2)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[6]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.2&&alivetime[i]<bloodpoolspeed*1.4)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[7]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.4&&alivetime[i]<bloodpoolspeed*1.6)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[8]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.6&&alivetime[i]<bloodpoolspeed*1.8)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[9]);
			if(type[i]==BLOODPOOL&&alivetime[i]>=bloodpoolspeed*1.8&&alivetime[i]<bloodpoolspeed*2.0)glBindTexture(GL_TEXTURE_2D, bloodtextureptr[10]);
			if(type[i]==BLOODPOOL)glColor4f(1,1,1,alivetime[i]*5/bloodpoolspeed-(int)(alivetime[i]*5/bloodpoolspeed));

				glPushMatrix();
				glBegin(GL_TRIANGLE_FAN);
					for(int j=0;j<numpoints[i];j++){
						glTexCoord2f(texcoordsx[i*8+j], texcoordsy[i*8+j]); glVertex3f(points[i*8+j].getX(),points[i*8+j].getY(),points[i*8+j].getZ());
					}
				glEnd();
				glPopMatrix();
			}
		}
		glDepthMask(true);
		glDisable(GL_TEXTURE_2D);
		glColor4f(1,1,1,1);
		glEnable(GL_CULL_FACE);
		glDisable(GL_POLYGON_OFFSET_FILL);
		glDepthFunc(GL_LEQUAL);
	}


}
