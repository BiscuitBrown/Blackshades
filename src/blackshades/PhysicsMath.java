package blackshades;
import static blackshades.Quaternions.*;
import static java.lang.StrictMath.*;

public class PhysicsMath {

	//------------------------------------------------------------------------//
	// Misc. Constants
	//------------------------------------------------------------------------//

	public static final float	g	= -32.174f;		// acceleration due to gravity, ft/s^2

	public static final float	rho = 0.0023769f;	// desity of air at sea level, slugs/ft^3

	public static final float	tol = 0.0000000001f;		// float type tolerance 

	//------------------------------------------------------------------------//
	// Misc. Functions
	//------------------------------------------------------------------------//

	//------------------------------------------------------------------------//
	// Vector Class and vector functions
	//------------------------------------------------------------------------//

	public static class Vector extends PhysicsMath {

		float x;
		float y;
		float z;

		public Vector(){
			this.x = 0;
			this.y = 0;
			this.z = 0;
		}

		public Vector(float x, float y, float z){
			
			this.x = x;
			this.y = y;
			this.z = z;

		}
		
		public Vector(Vector v){
			
			this.x = v.x;
			this.y = v.y;
			this.z = v.z;

		}
		
		public void set(Vector v){
			
			this.x = v.x;
			this.y = v.y;
			this.z = v.z;

		}
		
		public void set(float v){
			
			this.x = v;
			this.y = v;
			this.z = v;

		}

		public float magnitude() {
			return fast_sqrt(x*x + y*y + z*z);
		}

		public void normalize() {
			float m = fast_sqrt(x*x + y*y + z*z);

			if(m <= tol) m = 1;

			this.x /= m;
			this.y /= m;
			this.z /= m;
			
			if (abs(x) < tol) x = 0.0f;
			if (abs(y) < tol) y = 0.0f;
			if (abs(z) < tol) z = 0.0f;
		}

		public void reverse() {
			this.x = -x;
			this.y = -y;
			this.z = -z;
		}

		public Vector incrementAdd(Vector u){	// vector addition
			this.x += u.x;
			this.y += u.y;
			this.z += u.z;
			return this;
		}

		public Vector incrementSubtract(Vector u) {	// vector subtraction
			this.x -= u.x;
			this.y -= u.y;
			this.z -= u.z;
			return this;
		}
		
		public Vector incrementMultiply(float s){ // scalar multiply
			this.x *= s;
			this.y *= s;
			this.z *= s;
			return this;
		}
		public Vector incrementDevide(float s){	// scalar divide
			this.x /= s;
			this.y /= s;
			this.z /= s;
			return this;
		}
		
		public Vector add(Vector u, Vector v) {
			return new Vector(u.x + v.x, u.y + v.y, u.z + v.z);
		}

		public Vector subtract(){
			return new Vector(-x, -y, -z);
		}
		
		public Vector subtract(Vector u, Vector v) {
			return new Vector(u.x - v.x, u.y - v.y, u.z - v.z);
		}
		
		// Vector cross product (u cross v)
		
		public Vector xor(Vector u, Vector v) {
			return new Vector(	u.y*v.z - u.z*v.y,
								-u.x*v.z + u.z*v.x,
								u.x*v.y - u.y*v.x );
		}
		
		// Vector dot product
		
		public float multiply(Vector u, Vector v) {
			return (u.x*v.x + u.y*v.y + u.z*v.z);
		}
		
		public Vector multiply(float s, Vector u) {
			return new Vector(u.x*s, u.y*s, u.z*s);
		}
		
		public Vector multiply(Vector u, float s) {
			return new Vector(u.x*s, u.y*s, u.z*s);
		}
		
		public Vector devide(Vector u, float s) {
			return new Vector(u.x/s, u.y/s, u.z/s);
		}
		
		// triple scalar product (u dot (v cross w))

		public	float TripleScalarProduct(Vector u, Vector v, Vector w){
			return (float)(	(u.x * (v.y*w.z - v.z*w.y)) +
							(u.y * (-v.x*w.z + v.z*w.x)) +
							(u.z * (v.x*w.y - v.y*w.x)) );
			//return u*(v^w);
		}

	};

	//------------------------------------------------------------------------//
	// Matrix Class and matrix functions
	//------------------------------------------------------------------------//

	public class Matrix3x3 extends PhysicsMath {

		// elements eij: i -> row, j -> column

		float	e11, e12, e13, e21, e22, e23, e31, e32, e33;
		
		public Matrix3x3() {
			
			e11 = 0;
			e12 = 0;
			e13 = 0;
			e21 = 0;
			e22 = 0;
			e23 = 0;
			e31 = 0;
			e32 = 0;
			e33 = 0;
		}
		
		public Matrix3x3(	float r1c1, float r1c2, float r1c3, 
							float r2c1, float r2c2, float r2c3, 
							float r3c1, float r3c2, float r3c3 ) {
			
			e11 = r1c1;
			e12 = r1c2;
			e13 = r1c3;
			e21 = r2c1;
			e22 = r2c2;
			e23 = r2c3;
			e31 = r3c1;
			e32 = r3c2;
			e33 = r3c3;
		}

		public float det() {
		
			return	e11*e22*e33 - 
					e11*e32*e23 + 
					e21*e32*e13 - 
					e21*e12*e33 + 
					e31*e12*e23 - 
					e31*e22*e13;	
		}

		public Matrix3x3 Transpose() {
			return new Matrix3x3(e11,e21,e31,e12,e22,e32,e13,e23,e33);
		}

		public Matrix3x3 Inverse() {
			
			float	d = e11*e22*e33 - 
					e11*e32*e23 + 
					e21*e32*e13 - 
					e21*e12*e33 + 
					e31*e12*e23 - 
					e31*e22*e13;

			if (d == 0) d = 1;
	
			return new	Matrix3x3(	(e22*e33-e23*e32)/d,
									-(e12*e33-e13*e32)/d,
									(e12*e23-e13*e22)/d,
									-(e21*e33-e23*e31)/d,
									(e11*e33-e13*e31)/d,
									-(e11*e23-e13*e21)/d,
									(e21*e32-e22*e31)/d,
									-(e11*e32-e12*e31)/d,
									(e11*e22-e12*e21)/d );	
		}

		public Matrix3x3 incrementAdd(Matrix3x3 m){
			
			e11 += m.e11;
			e12 += m.e12;
			e13 += m.e13;
			e21 += m.e21;
			e22 += m.e22;
			e23 += m.e23;
			e31 += m.e31;
			e32 += m.e32;
			e33 += m.e33;
			
			return this;
		}

		public Matrix3x3 incrementSubtract(Matrix3x3 m){
			
			e11 -= m.e11;
			e12 -= m.e12;
			e13 -= m.e13;
			e21 -= m.e21;
			e22 -= m.e22;
			e23 -= m.e23;
			e31 -= m.e31;
			e32 -= m.e32;
			e33 -= m.e33;
			
			return this;
		}

		public Matrix3x3 incrementMultiply(float s){
			
			e11 *= s;
			e12 *= s;
			e13 *= s;
			e21 *= s;
			e22 *= s;
			e23 *= s;
			e31 *= s;
			e32 *= s;
			e33 *= s;

			return this;
		}

		public Matrix3x3 incrementDevide(float s){
			
			e11 /= s;
			e12 /= s;
			e13 /= s;
			e21 /= s;
			e22 /= s;
			e23 /= s;
			e31 /= s;
			e32 /= s;
			e33 /= s;

			return this;
		}
		
		public Matrix3x3 add(Matrix3x3 m1, Matrix3x3 m2) {
			
			return new	Matrix3x3(	m1.e11+m2.e11,
									m1.e12+m2.e12,
									m1.e13+m2.e13,
									m1.e21+m2.e21,
									m1.e22+m2.e22,
									m1.e23+m2.e23,
									m1.e31+m2.e31,
									m1.e32+m2.e32,
									m1.e33+m2.e33);
		}

		public Matrix3x3 subtract(Matrix3x3 m1, Matrix3x3 m2) {
			
			return new Matrix3x3(	m1.e11-m2.e11,
									m1.e12-m2.e12,
									m1.e13-m2.e13,
									m1.e21-m2.e21,
									m1.e22-m2.e22,
									m1.e23-m2.e23,
									m1.e31-m2.e31,
									m1.e32-m2.e32,
									m1.e33-m2.e33);
		}

		public Matrix3x3 devide(Matrix3x3 m, float s) {
			
			return new	Matrix3x3(	m.e11/s,
									m.e12/s,
									m.e13/s,
									m.e21/s,
									m.e22/s,
									m.e23/s,
									m.e31/s,
									m.e32/s,
									m.e33/s);
		}

		public Matrix3x3 multiply(Matrix3x3 m1, Matrix3x3 m2) {
			
			return new Matrix3x3(	m1.e11*m2.e11 + m1.e12*m2.e21 + m1.e13*m2.e31,
									m1.e11*m2.e12 + m1.e12*m2.e22 + m1.e13*m2.e32,
									m1.e11*m2.e13 + m1.e12*m2.e23 + m1.e13*m2.e33,
									m1.e21*m2.e11 + m1.e22*m2.e21 + m1.e23*m2.e31,
									m1.e21*m2.e12 + m1.e22*m2.e22 + m1.e23*m2.e32,
									m1.e21*m2.e13 + m1.e22*m2.e23 + m1.e23*m2.e33,
									m1.e31*m2.e11 + m1.e32*m2.e21 + m1.e33*m2.e31,
									m1.e31*m2.e12 + m1.e32*m2.e22 + m1.e33*m2.e32,
									m1.e31*m2.e13 + m1.e32*m2.e23 + m1.e33*m2.e33 );
		}

		public Matrix3x3 multiply(Matrix3x3 m, float s) {
			
			return new	Matrix3x3(	m.e11*s,
									m.e12*s,
									m.e13*s,
									m.e21*s,
									m.e22*s,
									m.e23*s,
									m.e31*s,
									m.e32*s,
									m.e33*s);
		}

		public	Matrix3x3 multiply(float s, Matrix3x3 m) {
			
			return new	Matrix3x3(	m.e11*s,
									m.e12*s,
									m.e13*s,
									m.e21*s,
									m.e22*s,
									m.e23*s,
									m.e31*s,
									m.e32*s,
									m.e33*s);

		}

		public Vector multiply(Matrix3x3 m, Vector u) {
			
			return new Vector(	m.e11*u.x + m.e12*u.y + m.e13*u.z,
								m.e21*u.x + m.e22*u.y + m.e23*u.z,
								m.e31*u.x + m.e32*u.y + m.e33*u.z);	
		}

		public Vector multiply(Vector u, Matrix3x3 m) {
			
			return new Vector(	u.x*m.e11 + u.y*m.e21 + u.z*m.e31,
								u.x*m.e12 + u.y*m.e22 + u.z*m.e32,
								u.x*m.e13 + u.y*m.e23 + u.z*m.e33);
		}
	};
	
	
	//------------------------------------------------------------------------//
	// Quaternion Class and Quaternion functions
	//------------------------------------------------------------------------//

	public class Quaternion extends PhysicsMath {

		public float n;	// number (scalar) part
		public Vector v = new Vector();	// vector part: v.x, v.y, v.z

		public Quaternion() {
			
			n = 0;
			v.x = 0;
			v.y =  0;
			v.z = 0;
		}

		public Quaternion(float e0, float e1, float e2, float e3) {
			
			n = e0;
			v.x = e1;
			v.y = e2;
			v.z = e3;
		}

		public float Magnitude() {
			return (float) fast_sqrt(n*n + v.x*v.x + v.y*v.y + v.z*v.z);
		}

		
		public Vector GetVector() { return new Vector(v.x, v.y, v.z); }

		public float GetScalar() { return n; }

		
		public Quaternion incrementAdd(Quaternion q) {
			
			n += q.n;
			v.x += q.v.x;
			v.y += q.v.y;
			v.z += q.v.z;
			return this;

		}

		public Quaternion incrementSubtract(Quaternion q) {
			
			n -= q.n;
			v.x -= q.v.x;
			v.y -= q.v.y;
			v.z -= q.v.z;
			return this;

		}

		public Quaternion incrementMultiply(float s) {
			
			n *= s;
			v.x *= s;
			v.y *= s;
			v.z *= s;
			return this;

		}

		public Quaternion incrementDevide(float s) {
			
			n /= s;
			v.x /= s;
			v.y /= s;
			v.z /= s;
			return this;
		}

		public final Quaternion firstComplement() { return new Quaternion(n, -v.x, -v.y, -v.z);}

	};
	
	public Quaternion add(Quaternion q1, Quaternion q2) {
		
		return new Quaternion(	q1.n + q2.n,
								q1.v.x + q2.v.x,
								q1.v.y + q2.v.y,
								q1.v.z + q2.v.z);
	}

	public Quaternion subtract(Quaternion q1, Quaternion q2) {
		
		return new Quaternion(	q1.n - q2.n,
								q1.v.x - q2.v.x,
								q1.v.y - q2.v.y,
								q1.v.z - q2.v.z);
	}

	public Quaternion multiply(Quaternion q1, Quaternion q2) {
		
		return new Quaternion(	q1.n*q2.n - q1.v.x*q2.v.x - q1.v.y*q2.v.y - q1.v.z*q2.v.z,
								q1.n*q2.v.x + q1.v.x*q2.n + q1.v.y*q2.v.z - q1.v.z*q2.v.y,
								q1.n*q2.v.y + q1.v.y*q2.n + q1.v.z*q2.v.x - q1.v.x*q2.v.z,
								q1.n*q2.v.z + q1.v.z*q2.n + q1.v.x*q2.v.y - q1.v.y*q2.v.x);							
		
	}

	public Quaternion multiply(Quaternion q, float s) {
		
		return new Quaternion(q.n*s, q.v.x*s, q.v.y*s, q.v.z*s);
	}

	public Quaternion multiply(float s, Quaternion q) {
		
		return new Quaternion(q.n*s, q.v.x*s, q.v.y*s, q.v.z*s);
	}

	public Quaternion multiply(Quaternion q, Vector v) {
		
		return new Quaternion(	-(q.v.x*v.x + q.v.y*v.y + q.v.z*v.z),
								q.n*v.x + q.v.y*v.z - q.v.z*v.y,
								q.n*v.y + q.v.z*v.x - q.v.x*v.z,
								q.n*v.z + q.v.x*v.y - q.v.y*v.x);
	}

	public Quaternion multiply(Vector v, Quaternion q) {
	
		return new Quaternion(	-(q.v.x*v.x + q.v.y*v.y + q.v.z*v.z),
								q.n*v.x + q.v.z*v.y - q.v.y*v.z,
								q.n*v.y + q.v.x*v.z - q.v.z*v.x,
								q.n*v.z + q.v.y*v.x - q.v.x*v.y);
	}

	public Quaternion devide(Quaternion q, float s) {
		
		return new Quaternion(q.n/s, q.v.x/s, q.v.y/s, q.v.z/s);
		
	}

	public float QGetAngle(Quaternion q) {return (float) (2*acos(q.n));}

	public Vector QGetAxis(Quaternion q) {
		
		Vector v;
		float m;

		v = q.GetVector();
		m = v.magnitude();		

		if (m <= tol) {return new Vector(); }

		return v.devide(v, m);	
	}

	public Quaternion QRotate(Quaternion q1, Quaternion q2) {
		
		return multiply(q1, q2).multiply(q2, q1.firstComplement());
		//return	q1*q2*(~q1);
	}

	public Vector QVRotate(Quaternion q, Vector v) {
		
		Quaternion t;

		t = multiply(q,v).firstComplement();
		
		// t = q*v*(~q);

		return	t.GetVector();
	}

	public Quaternion makeQFromEulerAngles(float x, float y, float z) {
		
		Quaternion	q = new Quaternion();

		double	roll = toRadians(x);

		double	pitch = toRadians(y);

		double	yaw =toRadians(z);

		
		double	cyaw, cpitch, croll, syaw, spitch, sroll;

		double	cyawcpitch, syawspitch, cyawspitch, syawcpitch;

		cyaw = cos(0.5f * yaw);
		cpitch = cos(0.5f * pitch);
		croll = cos(0.5f * roll);
		syaw = sin(0.5f * yaw);
		spitch = sin(0.5f * pitch);
		sroll = sin(0.5f * roll);

		cyawcpitch = cyaw*cpitch;
		syawspitch = syaw*spitch;
		cyawspitch = cyaw*spitch;
		syawcpitch = syaw*cpitch;

		q.n = (float) (cyawcpitch * croll + syawspitch * sroll);
		q.v.x = (float) (cyawcpitch * sroll - syawspitch * croll); 
		q.v.y = (float) (cyawspitch * croll + syawcpitch * sroll);
		q.v.z = (float) (syawcpitch * croll - cyawspitch * sroll);

		return q;
	}

	public Vector makeEulerAnglesFromQ(Quaternion q) {
		
		double	r11, r21, r31, r32, r33, r12, r13;

		double	q00, q11, q22, q33;

		double	tmp;

		Vector	u = new Vector();


		q00 = q.n * q.n;
		q11 = q.v.x * q.v.x;
		q22 = q.v.y * q.v.y;
		q33 = q.v.z * q.v.z;

		r11 = q00 + q11 - q22 - q33;
		r21 = 2 * (q.v.x*q.v.y + q.n*q.v.z);
		r31 = 2 * (q.v.x*q.v.z - q.n*q.v.y);
		r32 = 2 * (q.v.y*q.v.z + q.n*q.v.x);
		r33 = q00 - q11 - q22 + q33;

		tmp = abs(r31);

		if(tmp > 0.999999)

		{
			r12 = 2 * (q.v.x*q.v.y - q.n*q.v.z);
			r13 = 2 * (q.v.x*q.v.z + q.n*q.v.y);

			u.x = (float) toDegrees(0.0f); //roll
			u.y = (float) toDegrees(-(PI/2) * r31/tmp); // pitch
			u.z = (float) toDegrees( atan2(-r12, -r31*r13)); // yaw

			return u;
		}

		u.x = (float) toDegrees( atan2(r32, r33)); // roll
		u.y = (float) toDegrees( asin(-r31));		 // pitch
		u.z = (float) toDegrees( atan2(r21, r11)); // yaw

		return u;
		
	}

}
