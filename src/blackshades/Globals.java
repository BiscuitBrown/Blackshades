package blackshades;

import java.util.Random;

import blackshades.Models.*;
import blackshades.Person.*;
import blackshades.Quaternions.*;
import blackshades.Skeleton.*;

public class Globals {
	public static float
		sinefluct,
		sinefluctprog;
	
	public static Random random;

	public static double multiplier=0;

	public static int
		gSourceID[] = new int[100], // hundred source IDs
		gSampleSet[] = new int[100]; // hundred sample set ID numbers
	
	public static float
		precipitationhorz,
		precipitationvert,
		precipitationdensity;

	public static int environment;

	public static float soundscalefactor;
	public static int slomo;
	public static int oldslomo;

	public static Animation animation[] = new Animation[30];
	public static Model skeletonmodels[] = new Model[10];
	public static Model gunmodels[] = new Model[11];
	public static Costume costume[] = new Costume[10];
	public static Sprites sprites;
	public static Decals decals;
	
	public static int
		thirdperson,
		nocolors,
		visions;
	
	public static Camera camera;
	public static float rad2deg;
	public static Skeleton testskeleton;

	public static float snowdelay;

	public static float camerashake;

	public static Fog fog;

	public static int
		forwardskey,
		backwardskey,
		leftkey,
		rightkey,
		aimkey,
		psychicaimkey,
		psychickey;

	public static boolean blood;

	public static float
		fogcolorr,
		fogcolorg,
		fogcolorb,
	
		d,
		a1,a2,a3,
		total,denom,mu;
	public static XYZ n,pa1,pa2,pa3;

	public static float
		u0, u1, u2,
		v0, v1, v2,
		a, b,
		max;
	public static int i, j;
	public static boolean bInter;
	public static float
		pointv[] = new float[3],
		p1v[] = new float[3],
		p2v[] = new float[3],
		p3v[] = new float[3],
		normalv[] = new float[3];

	public static XYZ
		vel,
		midp,
		newpoint1,newpoint2;

	public static float
		oldlength,
		relaxlength;

	public static float friction;
	public static int numrepeats;
	public static float groundlevel;
	public static float offset;
	public static XYZ impact;
	public static XYZ overpoint;
	public static XYZ underpoint;
	public static int whichtri;
	public static XYZ normalrotated;
	public static boolean groundish;
}
