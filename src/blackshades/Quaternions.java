package blackshades;

import blackshades.PhysicsMath.Vector;
import static blackshades.Globals.*;
import static java.lang.Math.*;

public class Quaternions {
	
    public static XYZ newpoint, oldpoint, vn, vt;
    
	public float Matrix_t[][] = new float[4][4];	
	public float x, y, z, w;
	
    public Quaternions qResult, quat;
    public XYZ point1, point2, tempvec, n2;
    public Vector v;
        	
	public Quaternions(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternions() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Quaternions(Quaternions quat) {
		this.x = quat.x;
		this.y = quat.y;
		this.z = quat.z;
		this.w = quat.w;
	}
	
	public void set(Quaternions quat) {
		this.x = quat.x;
		this.y = quat.y;
		this.z = quat.z;
		this.w = quat.w;
	}
	
	public void set(float quat) {
		this.x = quat;
		this.y = quat;
		this.z = quat;
		this.w = quat;
	}

	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;}
	public void setY(float y) {this.y = y;}
	public float getZ() {return z;}
	public void setZ(float z) {this.z = z;}
	public float getW() {return w;}
	public void setW(float w) {this.w = w;}	
	
		public static class XYZ {
	
			public float x;
			public float y;
			public float z;
			
			public XYZ(float x, float y, float z) {
				this.x = x;
				this.y = y;
				this.z = z;
			}
			
			public XYZ(XYZ add) {
				this.x=add.x;
				this.y=add.y;
				this.z=add.z;
			}
			
			public XYZ() {
				this.x = 0;
				this.y = 0;
				this.z = 0;
			}
			
			public float getX() {return this.x;}
			public void setX(float x) {this.x = x;}
			public float getY() {return this.y;}
			public void setY(float y) {this.y = y;}
			public float getZ() {return this.z;}
			public void setZ(float z) {this.z = z;}
	
			public XYZ add(XYZ add){
				
				XYZ ne = new XYZ(add);
				ne.x+=this.x;
				ne.y+=this.y;
				ne.z+=this.z;
				return ne;
			}
			
			public XYZ add(float add){
				
				XYZ ne = new XYZ();
				ne.x=this.x+add;
				ne.y=this.y+add;
				ne.z=this.z+add;
				return ne;
			}
	
			public XYZ subtract(XYZ add){
				
				XYZ ne = new XYZ(add);
				ne.x=this.x-ne.x;
				ne.y=this.y-ne.y;
				ne.z=this.z-ne.z;
				return ne;
			}
			
			public XYZ subtract(float add){
				
				XYZ ne = new XYZ();
				ne.x=this.x-add;
				ne.y=this.y-add;
				ne.z=this.z-add;
				return ne;
			}
	
			public XYZ multiply(float add){
				
				XYZ ne = new XYZ();
				ne.x=x*add;
				ne.y=y*add;
				ne.z=z*add;
				return ne;
			}
	
			public XYZ multiply(XYZ add){
				
				XYZ ne = new XYZ();
				ne.x=this.x*add.x;
				ne.y=this.y*add.y;
				ne.z=this.z*add.z;
				return ne;
			}
	
			public XYZ devide(float add){
							
				XYZ ne = new XYZ();
				ne.x=this.x/add;
				ne.y=this.y/add;
				ne.z=this.z/add;
				return ne;
			}
	
			public void incrementAdd(XYZ add){
				
				this.x+=add.x;
				this.y+=add.y;
				this.z+=add.z;
			}
	
			public void incrementSubtract(XYZ add){
				
				this.x-=add.x;
				this.y-=add.y;
				this.z-=add.z;
			}
	
			public void incrementMultiply(float add){
				
				this.x*=add;
				this.y*=add;
				this.z*=add;
			}
	
			public void incrementMultiply(XYZ add){
				
				this.x*=add.x;
				this.y*=add.y;
				this.z*=add.z;
			}
	
			public void incrementDevide(float add){
				
				this.x/=add;
				this.y/=add;
				this.z/=add;
			}
	
			public void set(float add){
				
				this.x=add;
				this.y=add;
				this.z=add;
			}
			
			public void set(XYZ add){
				
				this.x=add.x;
				this.y=add.y;
				this.z=add.z;
			}
	
			public void vec(Vector add){
				
				this.x=add.x;
				this.y=add.y;
				this.z=add.z;
			}
	
			public boolean equals(XYZ add){
				return (this.x==add.x&&this.y==add.y&&this.z==add.z);
			}
		}
	
		public static class angle_axis {
			
			public float x, y, z, angle;
			
			public angle_axis() {
				this.x = 0;
				this.y = 0;
				this.z = 0;
				this.angle = 0;
			}
			
			public angle_axis(float x, float y, float z, float angle) {
				this.x = x;
				this.y = y;
				this.z = z;
				this.angle = angle;
			}
	
			public float getX() {return x;}
			public void setX(float x) {this.x = x;}
			public float getY() {return y;}
			public void setY(float y) {this.y = y;}
			public float getZ() {return z;}
			public void setZ(float z) {this.z = z;}
			public float getAngle() {return angle;}
			public void setAngle(float angle) {	this.angle = angle;}
			
		}
	
		public static class euler {
	
			public float x, y, z;
			
			public euler() {
				this.x = 0;
				this.y = 0;
				this.z = 0;
			}
			
			public euler(euler euler) {
				this.x = euler.x;
				this.y = euler.y;
				this.z = euler.z;
			}
			
			public euler(float x, float y, float z) {
				this.x = x;
				this.y = y;
				this.z = z;
			}
	
			public float getX() {return x;}
			public void setX(float x) {this.x = x;}
			public float getY() {return y;}
			public void setY(float y) {this.y = y;}
			public float getZ() {return z;}
			public void setZ(float z) {this.z = z;}
			
		}
	
	// Functions
	public Quaternions quatMult(Quaternions q1, Quaternions q2)
	{
	        if(qResult==null) qResult= new Quaternions();
	        else qResult.set(0);
	        	
	        float a, b, c, d, e, f, g, h;
	        
	        a = (q1.w + q1.x) * (q2.w + q2.x);
	        b = (q1.z - q1.y) * (q2.y - q2.z);
	        c = (q1.w - q1.x) * (q2.y + q2.z);
	        d = (q1.y + q1.z) * (q2.w - q2.x);
	        e = (q1.x + q1.z) * (q2.x + q2.y);
	        f = (q1.x - q1.z) * (q2.x - q2.y);
	        g = (q1.w + q1.y) * (q2.w - q2.z);
	        h = (q1.w - q1.y) * (q2.w + q2.z);
	        
	        qResult.setW(b + (-e - f + g + h) / 2);
	        qResult.setX(a - (e + f + g + h) / 2);
	        qResult.setY(c + (e - f + g - h) / 2);
	        qResult.setZ(d + (e - f - g + h) / 2);
	        
	        return new Quaternions(qResult);
	}

	public Quaternions toQuat(float m[][])
	{
	        // From Jason Shankel, (C) 2000.
			
			if(quat == null)quat = new Quaternions();
			else quat.set(0);
	        
	        double Tr = m[0][0] + m[1][1] + m[2][2] + 1.0, fourD;
	        double q[] = new double[4];
	        
	        int i,j,k;
	        
	        if (Tr >= 1.0)
	        {
	                fourD = 2.0*fast_sqrt((float) Tr);
	                q[3] = fourD/4.0;
	                q[0] = (m[2][1] - m[1][2]) / fourD;
	                q[1] = (m[0][2] - m[2][0]) / fourD;
	                q[2] = (m[1][0] - m[0][1]) / fourD;
	        }
	        else
	        {
	                if (m[0][0] > m[1][1])
	                {
	                        i = 0;
	                }
	                else
	                {
	                        i = 1;
	                }
	                if (m[2][2] > m[i][i])
	                {
	                        i = 2;
	                }
	                j = (i+1)%3;
	                k = (j+1)%3;
	                fourD = 2.0*fast_sqrt((float) (m[i][i] - m[j][j] - m[k][k] + 1.0));
	                q[i] = fourD / 4.0;
	                q[j] = (m[j][i] + m[i][j]) / fourD;
	                q[k] = (m[k][i] + m[i][k]) / fourD;
	                q[3] = (m[j][k] - m[k][j]) / fourD;
	        }
	        
	        quat.setX((float) q[0]);
	        quat.setY((float) q[1]);
	        quat.setZ((float) q[2]);
	        quat.setW((float) q[3]);
	        return new Quaternions(quat);
	}
	public void quat2Matrix(Quaternions quat, float m[][])
	{
	        // From the GLVelocity site (http://glvelocity.gamedev.net)
	        float fW = quat.w;
	        float fX = quat.x;
	        float fY = quat.y;
	        float fZ = quat.z;
	        float fXX = fX * fX;
	        float fYY = fY * fY;
	        float fZZ = fZ * fZ;
	        m[0][0] = 1.0f - 2.0f * (fYY + fZZ);
	        m[1][0] = 2.0f * (fX * fY + fW * fZ);
	        m[2][0] = 2.0f * (fX * fZ - fW * fY);
	        m[3][0] = 0.0f;
	        m[0][1] = 2.0f * (fX * fY - fW * fZ);
	        m[1][1] = 1.0f - 2.0f * (fXX + fZZ);
	        m[2][1] = 2.0f * (fY * fZ + fW * fX);
	        m[3][1] = 0.0f;
	        m[0][2] = 2.0f * (fX * fZ + fW * fY);
	        m[1][2] = 2.0f * (fX * fZ - fW * fX);
	        m[2][2] = 1.0f - 2.0f * (fXX + fYY);
	        m[3][2] = 0.0f;
	        m[0][3] = 0.0f;
	        m[1][3] = 0.0f;
	        m[2][3] = 0.0f;
	        m[3][3] = 1.0f;
	}
	public Quaternions toQuat(angle_axis Ang_Ax)
	{
	        // From the Quaternion Powers article on gamedev.net
			if(quat == null)quat = new Quaternions();
			else quat.set(0);
	        	        
	        quat.setX((float) (Ang_Ax.x * sin(Ang_Ax.angle / 2)));
	        quat.setY((float) (Ang_Ax.y * sin(Ang_Ax.angle / 2)));
	        quat.setZ((float) (Ang_Ax.z * sin(Ang_Ax.angle / 2)));
	        quat.setW((float) cos(Ang_Ax.angle / 2));
	        return new Quaternions(quat);
	}
	public angle_axis quat2AA(Quaternions quat)
	{
	        angle_axis Ang_Ax = new angle_axis();
	        float scale, tw;
	        
	        tw = (float)acos(quat.w) * 2;
	        scale = (float)sin(tw / 2.0);
	        Ang_Ax.setX(quat.getX() / scale);
	        Ang_Ax.setY(quat.getY() / scale);
	        Ang_Ax.setZ(quat.getZ() / scale);
	        
	        Ang_Ax.setAngle((float) (2.0 * acos(quat.w)/(float)PI*180));
	        return Ang_Ax;
	}

	public Quaternions toQuat(int In_Degrees, euler Euler)
	{
	        // From the gamasutra quaternion article
			if(quat == null)quat = new Quaternions();
			else quat.set(0);
			
	        euler newEuler = new euler(Euler);
	        float cr, cp, cy, sr, sp, sy, cpcy, spsy;
	        //If we are in Degree mode, convert to Radians
	        if (In_Degrees > 0) {
	        	newEuler.x = newEuler.x * (float)PI / 180;
	        	newEuler.y = newEuler.y * (float)PI / 180;
	        	newEuler.z = newEuler.z * (float)PI / 180;
	        }
	        //Calculate trig identities
	        //Formerly roll, pitch, yaw
	        cr = (float)(cos(newEuler.x/2));
	        cp = (float)(cos(newEuler.y/2));
	        cy = (float)(cos(newEuler.z/2));
	        sr = (float)(sin(newEuler.x/2));
	        sp = (float)(sin(newEuler.y/2));
	        sy = (float)(sin(newEuler.z/2));
	        
	        cpcy = cp * cy;
	        spsy = sp * sy;
	        quat.w = cr * cpcy + sr * spsy;
	        quat.x = sr * cpcy - cr * spsy;
	        quat.y = cr * sp * cy + sr * cp * sy;
	        quat.z = cr * cp * sy - sr * sp * cy;
	        
	        return new Quaternions(quat);
	}
	        
	public Quaternions qNormalize(Quaternions quat)
	{
		if(this.quat == null)this.quat = new Quaternions(quat);
		else this.quat.set(quat);
        float norm;
        norm =  this.quat.x * this.quat.x + 
        		this.quat.y * this.quat.y + 
                this.quat.z * this.quat.z + 
                this.quat.w * this.quat.w;
        
        this.quat.setX((float)(this.quat.x / norm));
        this.quat.setY((float)(this.quat.y / norm));
        this.quat.setZ((float)(this.quat.z / norm));
        this.quat.setW((float)(this.quat.w / norm));
        
        return new Quaternions(this.quat);
	}

	public XYZ quat2Vector(Quaternions quat)
	{
		if(this.quat == null)this.quat = new Quaternions(quat);
		else this.quat.set(quat);
	
		qNormalize(this.quat);

		float fW = this.quat.w;
		float fX = this.quat.x;
		float fY = this.quat.y;
		float fZ = this.quat.z;
		
		if(tempvec == null)tempvec = new XYZ();
		else tempvec.set(0);

		tempvec.setX(2.0f*(fX*fZ-fW*fY));
		tempvec.setY(2.0f*(fY*fZ+fW*fX));
		tempvec.setZ(1.0f-2.0f*(fX*fX+fY*fY));
		
		return tempvec;
	}
	
	public static void crossProduct(XYZ P, XYZ Q, XYZ V){
		V.x = P.y * Q.z - P.z * Q.y;
		V.y = P.z * Q.x - P.x * Q.z;
		V.z = P.x * Q.y - P.y * Q.x;
	}

	public static void normalise(XYZ vectory) {
		float d = fast_sqrt(vectory.x*vectory.x+vectory.y*vectory.y+vectory.z*vectory.z);
		if(d==0){return;}
		vectory.x /= d;
		vectory.y /= d;
		vectory.z /= d;
	}

	public static float fast_sqrt (float arg)
	{	
		return (float) sqrt((double) arg);
	}

	public float normaldotproduct(XYZ point1, XYZ point2){
		if(this.point1 == null)this.point1 = new XYZ();
		else this.point1.set(0);
		
		if(this.point2 == null)this.point2 = new XYZ();
		else this.point2.set(0);
		
		float returnvalue;
		normalise(this.point1);
		normalise(this.point2);
		returnvalue=(this.point1.x*this.point2.x+this.point1.y*this.point2.y+this.point1.z*this.point2.z);
		return returnvalue;
	}	

	public static boolean pointInTriangle(Vector p, Vector normal, float p11, float p12, float p13, float p21, float p22, float p23, float p31, float p32, float p33) {
		
		bInter=false;
		
		pointv[0]=p.x;
		pointv[1]=p.y;
		pointv[2]=p.z;
		
		
		p1v[0]=p11;
		p1v[1]=p12;
		p1v[2]=p13;
		
		p2v[0]=p21;
		p2v[1]=p22;
		p2v[2]=p23;
		
		p3v[0]=p31;
		p3v[1]=p32;
		p3v[2]=p33;
		
		normalv[0]=normal.x;
		normalv[1]=normal.y;
		normalv[2]=normal.z;
		
		max = max(max(abs(normalv[0]), abs(normalv[1])), abs(normalv[2]));
		
		if (max == abs(normalv[0])) {i = 1; j = 2;} // y, z
		if (max == abs(normalv[1])) {i = 0; j = 2;} // x, z
		if (max == abs(normalv[2])) {i = 0; j = 1;} // x, y
		
		u0 = pointv[i] - p1v[i];
		v0 = pointv[j] - p1v[j];
		u1 = p2v[i] - p1v[i];
		v1 = p2v[j] - p1v[j];
		u2 = p3v[i] - p1v[i];
		v2 = p3v[j] - p1v[j];

		if (u1 > -1.0e-05f && u1 < 1.0e-05f)// == 0.0f)
		{
			b = u0 / u2;
			if (0.0f <= b && b <= 1.0f)
			{
				a = (v0 - b * v2) / v1;
				bInter = ((a >= 0.0f) && (( a + b ) <= 1.0f));
			}
		}
		else
		{
			b = (v0 * u1 - u0 * v1) / (v2 * u1 - u2 * v1);
			if (0.0f <= b && b <= 1.0f)
			{
				a = (u0 - b * u2) / u1;
				bInter = ((a >= 0.0f) && (( a + b ) <= 1.0f ));
			}
		}

		return bInter;
	}

	public boolean lineFacet(Vector p1,Vector p2,Vector pa,Vector pb,Vector pc,Vector p) {
	   float d;
	   float a1,a2,a3;
	   float total,denom,mu;
	   
	   if(this.v == null)this.v = new Vector();
	   else this.v.set(0);
	   
	   //Vector pa1,pa2,pa3;

	   //Calculate the parameters for the plane 
	   this.v.x = (pb.y - pa.y)*(pc.z - pa.z) - (pb.z - pa.z)*(pc.y - pa.y);
	   this.v.y = (pb.z - pa.z)*(pc.x - pa.x) - (pb.x - pa.x)*(pc.z - pa.z);
	   this.v.z = (pb.x - pa.x)*(pc.y - pa.y) - (pb.y - pa.y)*(pc.x - pa.x);
	   this.v.normalize();
	   
	   d = - this.v.x * pa.x - this.v.y * pa.y - this.v.z * pa.z;

	   //Calculate the position on the line that intersects the plane 
	   denom = this.v.x * (p2.x - p1.x) + this.v.y * (p2.y - p1.y) + this.v.z * (p2.z - p1.z);
	   if (abs(denom) < 0.0000001)        // Line and plane don't intersect 
	      return false;
	   
	   mu = - (d + this.v.x * p1.x + this.v.y * p1.y + this.v.z * p1.z) / denom;
	   p.x = p1.x + mu * (p2.x - p1.x);
	   p.y = p1.y + mu * (p2.y - p1.y);
	   p.z = p1.z + mu * (p2.z - p1.z);
	   
	   if (mu < 0 || mu > 1)   // Intersection not along line segment 
	      return false;
	    
	   return (!pointInTriangle( p, this.v, pa.x, pa.y, pa.z, pb.x, pb.y, pb.z, pc.x, pc.y, pc.z));	   
	}
	
	public static boolean pointInTriangle(XYZ p, XYZ normal, XYZ p1, XYZ p2, XYZ p3) {
		
		bInter=false;
			
		pointv[0]=p.x;
		pointv[1]=p.y;
		pointv[2]=p.z;
		
		
		p1v[0]=p1.x;
		p1v[1]=p1.y;
		p1v[2]=p1.z;
		
		p2v[0]=p2.x;
		p2v[1]=p2.y;
		p2v[2]=p2.z;
		
		p3v[0]=p3.x;
		p3v[1]=p3.y;
		p3v[2]=p3.z;
		
		normalv[0]=normal.x;
		normalv[1]=normal.y;
		normalv[2]=normal.z;
		
		max = max(max(abs(normalv[0]), abs(normalv[1])), abs(normalv[2]));
	
		if (max == abs(normalv[0])) {i = 1; j = 2;} // y, z
		if (max == abs(normalv[1])) {i = 0; j = 2;} // x, z
		if (max == abs(normalv[2])) {i = 0; j = 1;} // x, y
		
		u0 = pointv[i] - p1v[i];
		v0 = pointv[j] - p1v[j];
		u1 = p2v[i] - p1v[i];
		v1 = p2v[j] - p1v[j];
		u2 = p3v[i] - p1v[i];
		v2 = p3v[j] - p1v[j];

		if (u1 > -1.0e-05f && u1 < 1.0e-05f)// == 0.0f)
		{
			b = u0 / u2;
			if (0.0f <= b && b <= 1.0f)
			{
				a = (v0 - b * v2) / v1;
				if ((a >= 0.0f) && (( a + b ) <= 1.0f))
					bInter = true;
			}
		}
		else
		{
			b = (v0 * u1 - u0 * v1) / (v2 * u1 - u2 * v1);
			if (0.0f <= b && b <= 1.0f)
			{
				a = (u0 - b * u2) / u1;
				if ((a >= 0.0f) && (( a + b ) <= 1.0f ))
					bInter = true;
			}
		}

		return bInter;
	}

	public boolean lineFacet(XYZ p1,XYZ p2,XYZ pa,XYZ pb,XYZ pc,XYZ p) {
		
	   float d;
	   float a1,a2,a3;
	   float total,denom,mu;
	   //XYZ n = new XYZ(),pa1,pa2,pa3;
	   
	   if(this.n2 == null)this.n2 = new XYZ();
	   else this.n2.set(0);

	   //Calculate the parameters for the plane 
	   n2.x = (pb.y - pa.y)*(pc.z - pa.z) - (pb.z - pa.z)*(pc.y - pa.y);
	   n2.y = (pb.z - pa.z)*(pc.x - pa.x) - (pb.x - pa.x)*(pc.z - pa.z);
	   n2.z = (pb.x - pa.x)*(pc.y - pa.y) - (pb.y - pa.y)*(pc.x - pa.x);
	   normalise(n2);
	   d = - n2.x * pa.x - n2.y * pa.y - n2.z * pa.z;

	   //Calculate the position on the line that intersects the plane 
	   denom = n2.x * (p2.x - p1.x) + n2.y * (p2.y - p1.y) + n2.z * (p2.z - p1.z);
	   
	   if (abs(denom) < 0.0000001)        // Line and plane don't intersect 
	      return false;
	   
	   mu = - (d + n2.x * p1.x + n2.y * p1.y + n2.z * p1.z) / denom;
	   p.x = p1.x + mu * (p2.x - p1.x);
	   p.y = p1.y + mu * (p2.y - p1.y);
	   p.z = p1.z + mu * (p2.z - p1.z);
	   
	   if (mu < 0 || mu > 1)   // Intersection not along line segment 
	      return false;
	    
	   return(!pointInTriangle(p,n2, pa, pb, pc));
	}

	public static float 
			d,
			a1,a2,a3,
			total,denom,mu;
				
	//public static XYZ pa1,pa2,pa3,n;

	public static float lineFacetd(XYZ p1,XYZ p2,XYZ pa,XYZ pb,XYZ pc,XYZ p) {

	   //Calculate the parameters for the plane 
	   n.x = (pb.y - pa.y)*(pc.z - pa.z) - (pb.z - pa.z)*(pc.y - pa.y);
	   n.y = (pb.z - pa.z)*(pc.x - pa.x) - (pb.x - pa.x)*(pc.z - pa.z);
	   n.z = (pb.x - pa.x)*(pc.y - pa.y) - (pb.y - pa.y)*(pc.x - pa.x);
	   normalise(n);
	   d = - n.x * pa.x - n.y * pa.y - n.z * pa.z;

	   //Calculate the position on the line that intersects the plane 
	   denom = n.x * (p2.x - p1.x) + n.y * (p2.y - p1.y) + n.z * (p2.z - p1.z);
	   if (abs(denom) < 0.0000001)        // Line and plane don't intersect 
	      return 0;
	   mu = - (d + n.x * p1.x + n.y * p1.y + n.z * p1.z) / denom;
	   p.x = p1.x + mu * (p2.x - p1.x);
	   p.y = p1.y + mu * (p2.y - p1.y);
	   p.z = p1.z + mu * (p2.z - p1.z);
	   if (mu < 0 || mu > 1)   // Intersection not along line segment 
	      return 0;
	    
	   if(!pointInTriangle(p,n, pa, pb, pc)){return 0;}
	   
	   return 1;
	}
	
	public static float lineFacetd(XYZ p1,XYZ p2,XYZ pa,XYZ pb,XYZ pc, XYZ n, XYZ p) {
		
	   //Calculate the parameters for the plane 
	   d = - n.x * pa.x - n.y * pa.y - n.z * pa.z;

	   //Calculate the position on the line that intersects the plane 
	   denom = n.x * (p2.x - p1.x) + n.y * (p2.y - p1.y) + n.z * (p2.z - p1.z);
	   if (abs(denom) < 0.0000001)        // Line and plane don't intersect 
	      return 0;
	   mu = - (d + n.x * p1.x + n.y * p1.y + n.z * p1.z) / denom;
	   p.x = p1.x + mu * (p2.x - p1.x);
	   p.y = p1.y + mu * (p2.y - p1.y);
	   p.z = p1.z + mu * (p2.z - p1.z);
	   if (mu < 0 || mu > 1)   // Intersection not along line segment 
	      return 0;
	   	   
	   if(!pointInTriangle(p,n, pa, pb, pc)){return 0;}
	   return 1;
	}
	
	public static void reflectVector(XYZ vel, XYZ n) {
			
		if(vn == null)vn = new XYZ();
		else vn.set(0);
		
		if(vt == null)vt = new XYZ();
		else vt.set(0);
		
		float dotprod;
		   
		dotprod=dotproduct(n,vel);
		vn.x=n.x*dotprod;
		vn.y=n.y*dotprod;
		vn.z=n.z*dotprod;
		
		vt.x=vel.x-vn.x;
		vt.y=vel.y-vn.y;
		vt.z=vel.z-vn.z;
			
		vel.x = vt.x - vn.x;
		vel.y = vt.y - vn.y;
		vel.z = vt.z - vn.z;
	}

	public static float dotproduct(XYZ point1, XYZ point2) {
		
		float returnvalue;
		returnvalue=(point1.x*point2.x+point1.y*point2.y+point1.z*point2.z);
		return returnvalue;
	}

	public static float findDistance(XYZ point1, XYZ point2){
		return(fast_sqrt((point1.x-point2.x)*(point1.x-point2.x)+(point1.y-point2.y)*(point1.y-point2.y)+(point1.z-point2.z)*(point1.z-point2.z)));
	}

	public static float findLength(XYZ point1){
		return(fast_sqrt((point1.x)*(point1.x)+(point1.y)*(point1.y)+(point1.z)*(point1.z)));
	}


	public static float findLengthfast(XYZ point1){
		return((point1.x)*(point1.x)+(point1.y)*(point1.y)+(point1.z)*(point1.z));
	}

	public static float findDistancefast(XYZ point1, XYZ point2){
		return((point1.x-point2.x)*(point1.x-point2.x)+(point1.y-point2.y)*(point1.y-point2.y)+(point1.z-point2.z)*(point1.z-point2.z));
	}
	
	public static XYZ doRotation(XYZ thePoint, float xang, float yang, float zang) {
		
		if(newpoint == null)newpoint = new XYZ();
		else newpoint.set(0);
		
		XYZ thenewPoint = new XYZ(thePoint);
		
		// float x, float y, float z
		
		//XYZ thePointNew = new XYZ(thePoint);
		
		if(xang != 0) {
			xang*=6.283185;
			xang/=360;
		}
		
		if(yang != 0) {
			yang*=6.283185;
			yang/=360;
		}
		
		if(zang != 0) {
			zang*=6.283185;
			zang/=360;
		}
		
		
		if(yang != 0) {
			newpoint.z=(float) (thenewPoint.z*cos(yang)-thenewPoint.x*sin(yang));
			newpoint.x=(float) (thenewPoint.z*sin(yang)+thenewPoint.x*cos(yang));
			thenewPoint.z=newpoint.z;
			thenewPoint.x=newpoint.x;
		}
		
		if(zang != 0) {
			newpoint.x=(float) (thenewPoint.x*cos(zang)-thenewPoint.y*sin(zang));
			newpoint.y=(float) (thenewPoint.y*cos(zang)+thenewPoint.x*sin(zang));
			thenewPoint.x=newpoint.x;
			thenewPoint.y=newpoint.y;
		}
		
		if(xang != 0) {
			newpoint.y= (float) (thenewPoint.y*cos(xang)-thenewPoint.z*sin(xang));
			newpoint.z= (float) (thenewPoint.y*sin(xang)+thenewPoint.z*cos(xang));
			thenewPoint.z=newpoint.z;
			thenewPoint.y=newpoint.y;	
		}
				
		return thenewPoint;
	}

	public static float square( float f ) { return (f*f) ;}

	public static boolean sphere_line_intersection (
	    float x1, float y1 , float z1,
	    float x2, float y2 , float z2,
	    float x3, float y3 , float z3, float r )
	{

		 // x1,y1,z1  P1 coordinates (point of line)
		 // x2,y2,z2  P2 coordinates (point of line)
		 // x3,y3,z3, r  P3 coordinates and radius (sphere)
		 // x,y,z   intersection coordinates
		 //
		 // This function returns a pointer array which first index indicates
		 // the number of intersection point, followed by coordinate pairs.

		float x , y , z;
		float a, b, c, mu, i ;
		
		if(x1>x3+r&&x2>x3+r)return false;
		if(x1<x3-r&&x2<x3-r)return false;
		if(y1>y3+r&&y2>y3+r)return false;
		if(y1<y3-r&&y2<y3-r)return false;
		if(z1>z3+r&&z2>z3+r)return false;
		if(z1<z3-r&&z2<z3-r)return false;
		
		a =  square(x2 - x1) + square(y2 - y1) + square(z2 - z1);
		
		b =  2* ( (x2 - x1)*(x1 - x3)
		     + (y2 - y1)*(y1 - y3)
		     + (z2 - z1)*(z1 - z3) ) ;
		
		c =  square(x3) + square(y3) +
		     square(z3) + square(x1) +
		     square(y1) + square(z1) -
		     2* ( x3*x1 + y3*y1 + z3*z1 ) - square(r) ;
		
		i =   b * b - 4 * a * c ;

		 if ( i < 0.0 )
		 {
		  // no intersection
		  return false;
		 }

		return true;
	}

	public static XYZ doRotationRadian(XYZ thePoint, float xang, float yang, float zang){
		
		if(newpoint == null)newpoint = new XYZ();
		else newpoint.set(0);
		
		if(oldpoint == null)oldpoint = new XYZ(thePoint);
		else oldpoint.set(thePoint);
		
		if(yang!=0){
			
			newpoint.z=(float) (oldpoint.z*cos(yang)-oldpoint.x*sin(yang));
			newpoint.x=(float) (oldpoint.z*sin(yang)+oldpoint.x*cos(yang));
			oldpoint.z=newpoint.z;
			oldpoint.x=newpoint.x;
		}
		
		if(zang!=0){
			
			newpoint.x=(float) (oldpoint.x*cos(zang)-oldpoint.y*sin(zang));
			newpoint.y=(float) (oldpoint.y*cos(zang)+oldpoint.x*sin(zang));
			oldpoint.x=newpoint.x;
			oldpoint.y=newpoint.y;
		}
		
		if(xang!=0){
			
			newpoint.y=oldpoint.y*(float)cos(xang)-oldpoint.z*(float)sin(xang);
			newpoint.z=oldpoint.y*(float)sin(xang)+oldpoint.z*(float)cos(xang);
			oldpoint.z=newpoint.z;
			oldpoint.y=newpoint.y;	
		}
		
		return new XYZ(oldpoint);
		
	}
}

