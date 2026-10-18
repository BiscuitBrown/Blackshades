package blackshades;

import blackshades.Quaternions.*;
import blackshades.Support.*;

import java.nio.file.Files;

import blackshades.Models.*;

public class Game {
	
	public static final int NUM_BLOCKS = 100;
    public static final int BLOCK_SPACING = 360;
    public static final int MAX_PEOPLE = 90;
    public static final int MAX_PEOPLE_BLOCK = 20;

	//Eventloop
	public static  Boolean	gQuit;
	public static  float gamespeed;
	public static  double multiplier2,multiplier3,multiplier4,multiplier5,end,start,timetaken,framespersecond, fps;
	public static  float sps;
	public static  int maxfps;

	//Graphics
	public static  int screenwidth,screenheight;
	public static  float viewdistance;
	
	//Mouse
	public static  Point mouseloc;
	public static  Point oldmouseloc;

	public static  float mouserotation,mouserotation2;
	public static  float oldmouserotation,oldmouserotation2;
	public static  float mousesensitivity;
	public static  float usermousesensitivity;
	
	//keyboard
	
	public static  boolean tabkeydown;
	
	//Project Specific
	public static  int cityrotation[][] = new int[NUM_BLOCKS][NUM_BLOCKS];
	public static  int citytype[][] = new int[NUM_BLOCKS][NUM_BLOCKS];
	public static  int citypeoplenum[][] = new int[NUM_BLOCKS][NUM_BLOCKS];
	public static  boolean drawn[][] = new boolean[NUM_BLOCKS][NUM_BLOCKS];
	public static  int onblockx,onblocky;
	public static  boolean cubetest;
	public static  boolean disttest;
	public static  boolean oldbutton;
	
	public static  boolean initialized;
	
	public static  float flashamount;
	public static  float flashr,flashg,flashb;
	
	public static  int enemystate;
	
	public static  int cycle;
	
	public static  boolean whacked;
	
	public static  float losedelay;
	
	public static  XYZ bodycoords;
	
	public static  Frustum frustum;
	public static  Model blocks[] = new Model[4];
	public static  Model blockwalls[] = new Model[4];
	public static  Model blockcollide[] = new Model[4];
	public static  Model blocksimplecollide[] = new Model[4];
	public static  Model blockroofs[] = new Model[4];
	public static  Model blockocclude;
	public static  Model sidewalkcollide;
	public static  Model street;
	public static  Model bigStreet;
	public static  Model path;
	public static  Model blocksimple;
	public static  XYZ boundingpoints[] = new XYZ[8];
	public static  Files files;
	public static  Text text;
	public static  int goodkills;
	public static  int badkills;
	public static  int civkills;
	public static  int machinegunsoundloop;
	
	public static  boolean lasersight;
	public static  boolean debug;
	public static  boolean vblsync;
	
	public static  boolean blur;
	public static  boolean blurness;
	
	public static  boolean paused;
	
	public static  int mainmenu;
	
	public static  boolean reloadtoggle;
	
	public static  boolean aimtoggle;

	public static  Point olddrawmouse;

	public static  XYZ vipgoal;
	
	public static  XYZ aimer[] = new XYZ[2];
	
	public static  double eqn[] = new double[4];
	
	public static  float oldrot,oldrot2;
	
	public static  XYZ lastshot[] = new XYZ[2];
	public static  boolean zoom;
	public static  boolean oldzoom;
	
	public static  int numpeople;
	public static  float spawndelay;
	
	public static  boolean customlevels;
	
	public static  boolean musictoggle;
	public static  boolean fullscreen;
	
	public static  float psychicpower;
	
	public static  int type;

	public static  boolean slomokeydown;
	
	public static  int mouseoverbutton;
	public static  int oldmouseoverbutton;
	
	public static  Person person[] = new Person[MAX_PEOPLE];
	
	public static  int 				personspritetextureptr;
	public static  int 				deadpersonspritetextureptr;
	public static  int 				scopetextureptr;
	public static  int 				flaretextureptr;
	
	public static  boolean killedinnocent;
	public static  boolean gameinprogress;
	public static  boolean beatgame;
	public static  boolean mainmenuness;
	public static  int murderer;
	public static  float timeremaining;
	public static  int whichsong;
	public static  int oldscore;
	public static  int highscore;
	public static  int score;
	public static  int mission;
	public static  int nummissions;
	public static  int numpossibleguns;
	public static  int possiblegun[] = new int[6];
	public static  int evilprobability;
	public static  float difficulty;
	public static  boolean azertykeyboard;
	public static  boolean oldvisionkey;
	
	public static long window;

}
