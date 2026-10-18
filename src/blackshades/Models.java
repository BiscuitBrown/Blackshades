package blackshades;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.io.*;
import java.nio.*;

import static blackshades.Quaternions.*;
import static blackshades.Serialize.*;
import static org.lwjgl.BufferUtils.*;

public class Models {

	//
	// Model Structures
	//
	
	public static class TexturedTriangle{
		public short vertex[];
		public float r,g,b;
		
		public TexturedTriangle() {
			this.vertex = new short[3];
			this.r = 0;
			this.g = 0;
			this.b = 0;
		}
		
		public TexturedTriangle(TexturedTriangle texturedTriangle) {
			
			this.vertex = new short[3];
			
			for(int i = 0; i<3 ; i++) {
			    this.vertex[i]=texturedTriangle.vertex[i];
			}
			
			this.r = texturedTriangle.r;
			this.g = texturedTriangle.g;
			this.b = texturedTriangle.b;
		}
		
		public void set(TexturedTriangle texturedTriangle) {
			for(int i = 0; i<3 ; i++) {
			    this.vertex[i]=texturedTriangle.vertex[i];
			}
			
			this.r = texturedTriangle.r;
			this.g = texturedTriangle.g;
			this.b = texturedTriangle.b;
		}
		
	}
	
	public static class Model {
	
		public static final int MAX_TEXTURED_TRIANGLE = 400;	// maximum number of texture-filled triangles in a model
		public static final int MAX_MODEL_VERTEX = MAX_TEXTURED_TRIANGLE*3;	// maximum number of vertexs
		
		public short vertexNum,triangleNum;
		
		public XYZ vertex[];
		public XYZ normals[];
		public TexturedTriangle triangles[];
		public FloatBuffer vArray;
		
		public XYZ boundingspherecenter;
		public float boundingsphereradius;
		
		public XYZ boundingboxmin,boundingboxmax;
		
		public XYZ p1,p2,move, point;
		public XYZ average;
		
		public Model() {
			this.vertexNum = 0;
			this.triangleNum = 0;
			
			this.vertex= new XYZ[MAX_MODEL_VERTEX];
			this.normals = new XYZ[MAX_TEXTURED_TRIANGLE];
			this.triangles = new TexturedTriangle[MAX_TEXTURED_TRIANGLE];
			this.vArray = createFloatBuffer(MAX_TEXTURED_TRIANGLE*27);
			
			this.boundingspherecenter = new XYZ();
			this.boundingsphereradius = 0;
			
			this.boundingboxmin = new XYZ();
			this.boundingboxmax = new XYZ();
			this.p1 = new XYZ();
			this.p2 = new XYZ();
			this.move = new XYZ();
			this.point = new XYZ();
			this.average = new XYZ();
		}
		
		public Model(Model model) {
			this.vertexNum = model.vertexNum;
			this.triangleNum = model.triangleNum;
			
			this.vertex= new XYZ[MAX_MODEL_VERTEX];

			for(int i =0; i<vertexNum; i++) {
				this.vertex[i] = new XYZ(model.vertex[i]);
			}
		
			this.normals = new XYZ[MAX_TEXTURED_TRIANGLE];
			
			for(int i =0; i<triangleNum; i++) {
				this.normals[i] =new XYZ(model.normals[i]);
			}
			
			this.triangles = new TexturedTriangle[MAX_TEXTURED_TRIANGLE];
			
			for(int i =0; i<triangleNum; i++) {
				this.triangles[i] = new TexturedTriangle(model.triangles[i]);
			}
			
			this.vArray = model.vArray.duplicate();
						
			this.boundingspherecenter = new XYZ(model.boundingspherecenter);
			this.boundingsphereradius = model.boundingsphereradius;
			
			this.boundingboxmin = new XYZ(model.boundingboxmin);
			this.boundingboxmax = new XYZ(model.boundingboxmax);
			
			this.p1 = new XYZ(model.p1);
			this.p2 = new XYZ(model.p2);
			this.move = new XYZ(model.move);
			this.point = new XYZ(model.point);
			this.average = new XYZ(model.average);
			
		}
		
		public void set(Model model) {
			this.vertexNum = model.vertexNum;
			this.triangleNum = model.triangleNum;
			
			for(int i =0; i<vertexNum; i++) {
				this.vertex[i].set(model.vertex[i]);
			}
					
			for(int i =0; i<triangleNum; i++) {
				this.normals[i] = model.normals[i];
			}
						
			for(int i =0; i<triangleNum; i++) {
				this.triangles[i].set(model.triangles[i]);
			}
			
			this.vArray = model.vArray.duplicate();
						
			this.boundingspherecenter.set(model.boundingspherecenter);
			this.boundingsphereradius = model.boundingsphereradius;
			
			this.boundingboxmin.set(model.boundingboxmin);
			this.boundingboxmax.set(model.boundingboxmax);
			
			this.p1.set(model.p1);
			this.p2.set(model.p2);
			this.move.set(model.move);
			this.point.set(model.point);
			this.average.set(model.average);
		}
		
		//Functions
		public void updateVertexArray(){
						
			for(int i=0;i<triangleNum;i++){
												
				vArray.put(i*27+0, vertex[triangles[i].vertex[0]].x);
				vArray.put(i*27+1, vertex[triangles[i].vertex[0]].y);
				vArray.put(i*27+2, vertex[triangles[i].vertex[0]].z);
				vArray.put(i*27+3, normals[i].x);
				vArray.put(i*27+4, normals[i].y);
				vArray.put(i*27+5, normals[i].z);
				vArray.put(i*27+6, triangles[i].r);
				vArray.put(i*27+7, triangles[i].g);
				vArray.put(i*27+8, triangles[i].b);
				
				vArray.put(i*27+9, vertex[triangles[i].vertex[1]].x);
				vArray.put(i*27+10, vertex[triangles[i].vertex[1]].y);
				vArray.put(i*27+11, vertex[triangles[i].vertex[1]].z);
				vArray.put(i*27+12, normals[i].x);
				vArray.put(i*27+13, normals[i].y);
				vArray.put(i*27+14, normals[i].z);
				vArray.put(i*27+15, triangles[i].r);
				vArray.put(i*27+16, triangles[i].g);
				vArray.put(i*27+17, triangles[i].b);
				
				vArray.put(i*27+18, vertex[triangles[i].vertex[2]].x);
				vArray.put(i*27+19, vertex[triangles[i].vertex[2]].y);
				vArray.put(i*27+20, vertex[triangles[i].vertex[2]].z);
				vArray.put(i*27+21, normals[i].x);
				vArray.put(i*27+22, normals[i].y);
				vArray.put(i*27+23, normals[i].z);
				vArray.put(i*27+24, triangles[i].r);
				vArray.put(i*27+25, triangles[i].g);
				vArray.put(i*27+26, triangles[i].b);
				
			}
			
			int howmany = 0;
			average.set(0);
			
			boundingboxmin.set(20000);
			boundingboxmax.set(-20000);
			
			for(int i=0;i<vertexNum;i++){
				howmany++;
				average.set(average.add(vertex[i]));
				if(vertex[i].x<boundingboxmin.x)boundingboxmin.x=vertex[i].x;
				if(vertex[i].y<boundingboxmin.y)boundingboxmin.y=vertex[i].y;
				if(vertex[i].z<boundingboxmin.z)boundingboxmin.z=vertex[i].z;
				if(vertex[i].x>boundingboxmax.x)boundingboxmax.x=vertex[i].x;
				if(vertex[i].y>boundingboxmax.y)boundingboxmax.y=vertex[i].y;
				if(vertex[i].z>boundingboxmax.z)boundingboxmax.z=vertex[i].z;
			}
			average.set(average.devide(howmany));
			boundingspherecenter.set(average);
			boundingsphereradius=0;
			
			for(int i=0;i<vertexNum;i++){
				if(findDistancefast(average,vertex[i])>boundingsphereradius)boundingsphereradius=findDistancefast(average,vertex[i]);
			}
			boundingsphereradius=fast_sqrt(boundingsphereradius);
					
		}
	
		public boolean load(String name) throws IOException {
						
			InputStream file = Models.class.getClassLoader().getResourceAsStream(name);
												
            // read model settings
            vertexNum = readShort(file, 1);
            triangleNum = readShort(file, 1);

            // read the model data
            readXYZ(file, vertexNum, vertex);
            readTexturedTriangle(file, triangleNum, triangles);
            
			file.close();
			
			for(int i=0;i<triangleNum;i++){
				normals[i] = new XYZ(); 
			}
				
			updateVertexArray();
			
			average.set(0);
			int howmany = 0;
			
			for(int i=0;i<vertexNum;i++){
				howmany++;
				average.set(average.add(vertex[i]));
			}
			average.set(average.devide(howmany));
			boundingspherecenter.set(average);
			boundingsphereradius=0;
			for(int i=0;i<vertexNum;i++){
				if(findDistancefast(average,vertex[i])>boundingsphereradius)boundingsphereradius=findDistancefast(average,vertex[i]);
			}
			boundingsphereradius=fast_sqrt(boundingsphereradius);
						
			return true;
		}
	
		public void scale(float xscale,float yscale,float zscale)
		{
			for(int i=0; i<vertexNum; i++){
				vertex[i].x*=xscale;
				vertex[i].y*=yscale;
				vertex[i].z*=zscale;
			}
			updateVertexArray();
		}
	
		public void multColor(float howmuch)
		{
			for(int i=0; i<triangleNum; i++){
				triangles[i].r*=howmuch;
				triangles[i].g*=howmuch;
				triangles[i].b*=howmuch;
			}
			updateVertexArray();
		}
	
		public void scaleNormals(float xscale,float yscale,float zscale)
		{
			for(int i=0; i<vertexNum; i++){
				normals[i].x*=xscale;
				normals[i].y*=yscale;
				normals[i].z*=zscale;
			}
			updateVertexArray();
		}
	
		public void translate(float xtrans,float ytrans,float ztrans)
		{
			for(int i=0; i<vertexNum; i++){
				vertex[i].x+=xtrans;
				vertex[i].y+=ytrans;
				vertex[i].z+=ztrans;
			}
			updateVertexArray();
		}
	
		public void rotate(float xang,float yang,float zang)
		{
			for(int i=0; i<vertexNum; i++){
				vertex[i].set(doRotation(vertex[i],xang,yang,zang));
			}
			updateVertexArray();
		}
	
	
		public void calculateNormals()
		{
			for(int i=0;i<triangleNum;i++){
				crossProduct(vertex[triangles[i].vertex[1]].subtract(vertex[triangles[i].vertex[0]]),vertex[triangles[i].vertex[2]].subtract(vertex[triangles[i].vertex[0]]),normals[i]);
				normalise(normals[i]);
			}
			updateVertexArray();
		}
	
		public int nocolors;
		
		public void draw()
		{
			if(nocolors==0){
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_NORMAL_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			glVertexPointer(3, GL_FLOAT, 9*Float.BYTES , vArray.position(0));
			glNormalPointer(GL_FLOAT, 9*Float.BYTES , vArray.position(3));
			glColorPointer(3,GL_FLOAT, 9*Float.BYTES ,vArray.position(6));
			glDrawArrays(GL_TRIANGLES, 0, triangleNum*3);
			}
			if(nocolors!=0){
				glBufferData(GL_ARRAY_BUFFER, vArray, GL_STATIC_DRAW);
				glColor4f(0,0,0,1);
				glEnableClientState(GL_VERTEX_ARRAY);
				glEnableClientState(GL_NORMAL_ARRAY);
				glDisableClientState(GL_COLOR_ARRAY);
				glVertexPointer(3, GL_FLOAT, 9*Float.BYTES, vArray.position(0));
				glNormalPointer(GL_FLOAT, 9*Float.BYTES, vArray.position(3));
				glDrawArrays(GL_TRIANGLES, 0, triangleNum*3);
			}
		}
	
		public void draw(float r, float g, float b)
		{
			if(nocolors==0)glColor4f(r,g,b,1);
			if(nocolors==1)glColor4f(0,0,0,1);
			if(nocolors==2)glColor4f(1,0,0,1);
			if(nocolors==3)glColor4f(0,0,1,1);
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_NORMAL_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			glVertexPointer(3, GL_FLOAT, 9*Float.BYTES ,vArray.position(0));
			glNormalPointer(GL_FLOAT, 9*Float.BYTES ,vArray.position(3));
			glDrawArrays(GL_TRIANGLES, 0, triangleNum*3);
		}
	
		public void draw(float r, float g, float b, float o)
		{
			if(nocolors==0)glColor4f(r,g,b,o);
			if(nocolors==1)glColor4f(0,0,0,1);
			if(nocolors==2)glColor4f(1,0,0,1);
			if(nocolors==3)glColor4f(1,1,1,1);
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_NORMAL_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			glVertexPointer(3, GL_FLOAT, 9*Float.BYTES ,vArray.position(0));
			glNormalPointer(GL_FLOAT, 9*Float.BYTES ,vArray.position(3));
			glDrawArrays(GL_TRIANGLES, 0, triangleNum*3);
		}
	
		public void draw(float r, float g, float b, float x, float y, float z)
		{
			if(nocolors==0)glColor4f(r,g,b,1);
			if(nocolors==1)glColor4f(0,0,0,1);
			if(nocolors==2)glColor4f(1,0,0,1);
			if(nocolors==3)glColor4f(1,1,1,1);
			glNormal3f(x,y,z);
			glEnableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_NORMAL_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			glVertexPointer(3, GL_FLOAT, 9*Float.BYTES , vArray.position(0));
			glDrawArrays(GL_TRIANGLES, 0, triangleNum*3);
		}
	
	
		public int lineCheck(XYZ p1,XYZ p2, XYZ p)
		{
		  	int j;
			float distance;
			float olddistance=9999999.0f;
			int intersecting=0;
			int firstintersecting=-1;
			point.set(0);
			this.p1.set(p1);
			this.p2.set(p2);
			if(sphere_line_intersection(this.p1.x,this.p1.y,this.p1.z,
										this.p2.x,this.p2.y,this.p2.z,
										boundingspherecenter.x,boundingspherecenter.y,boundingspherecenter.z,
										boundingsphereradius))
			for (j=0;j<triangleNum;j++){
				intersecting=(int) lineFacetd(this.p1,this.p2,vertex[triangles[j].vertex[0]],vertex[triangles[j].vertex[1]],vertex[triangles[j].vertex[2]],normals[j],point);
				if (intersecting == 0) continue;
				distance=(point.x-this.p1.x)*(point.x-this.p1.x)+(point.y-this.p1.y)*(point.y-this.p1.y)+(point.z-this.p1.z)*(point.z-this.p1.z);
				if((distance<olddistance||firstintersecting==-1)&&intersecting!=0){olddistance=distance; firstintersecting=j; p.set(point);}
			}
			return firstintersecting;
		}
		
		public int lineCheck2(XYZ p1,XYZ p2, XYZ p, XYZ move, float rotate)
		{
		  	this.p1.set(p1);
		  	this.p2.set(p2);
		  	this.move.set(move);
		  	int j;
			float distance;
			float olddistance=9999999.0f;
			int intersecting=0;
			int firstintersecting=-1;
			point.set(0);
			this.p1.incrementSubtract(this.move);
			this.p2.incrementSubtract(this.move);
			if(rotate!=0)this.p1.set(doRotation(this.p1,0,-rotate,0));
			if(rotate!=0)this.p2.set(doRotation(this.p2,0,-rotate,0));
			if(sphere_line_intersection(this.p1.x,this.p1.y,this.p1.z,
										this.p2.x,this.p2.y,this.p2.z,
										boundingspherecenter.x,boundingspherecenter.y,boundingspherecenter.z,
										boundingsphereradius))
			for (j=0;j<triangleNum;j++){
				intersecting=(int) lineFacetd(this.p1,this.p2,vertex[triangles[j].vertex[0]],vertex[triangles[j].vertex[1]],vertex[triangles[j].vertex[2]],normals[j],point);
				
				if (intersecting == 0) continue;
				
				distance=(point.x-this.p1.x)*(point.x-this.p1.x)+(point.y-this.p1.y)*(point.y-this.p1.y)+(point.z-this.p1.z)*(point.z-this.p1.z);
				
				if((distance<olddistance||firstintersecting==-1)&&intersecting!=0){
					olddistance=distance; 
					firstintersecting=j; 
					p.set(point);
			    }
			}
			
			if(rotate!=0)p.set(doRotation(p,0,rotate,0));
			p.incrementAdd(this.move);
			return firstintersecting;
		}
	
		public int lineCheck3(XYZ p1,XYZ p2, XYZ p, XYZ move, float rotate, float[] d)
		{
		  	int j;
			float distance;
			float olddistance=9999999.0f;
			int intersecting=0;
			int firstintersecting=-1;
			point.set(0);
			this.p1.set(p1);
			this.p2.set(p2);
			this.move.set(move);
			
			this.p1.incrementSubtract(this.move);		
			this.p2.incrementSubtract(this.move);			
			this.p1.set(doRotation(this.p1,0,-rotate,0));
			this.p2.set(doRotation(this.p2,0,-rotate,0));
			if(sphere_line_intersection(this.p1.x,this.p1.y,this.p1.z,
										this.p2.x,this.p2.y,this.p2.z,
										boundingspherecenter.x,boundingspherecenter.y,boundingspherecenter.z,
										boundingsphereradius))
			for (j=0;j<triangleNum;j++){
				intersecting=(int) lineFacetd(this.p1,this.p2,vertex[triangles[j].vertex[0]],vertex[triangles[j].vertex[1]],vertex[triangles[j].vertex[2]],normals[j],point);
				if (intersecting == 0) continue;
				distance=(point.x-this.p1.x)*(point.x-this.p1.x)+(point.y-this.p1.y)*(point.y-this.p1.y)+(point.z-this.p1.z)*(point.z-this.p1.z);
				if((distance<olddistance||firstintersecting==-1)&&intersecting!=0){olddistance=distance; firstintersecting=j; p.set(point);}
			}
			d[0]=intersecting;
			p.set(doRotation(p,0,rotate,0));
			p.incrementAdd(this.move);
			return firstintersecting;
		}
	}
}
