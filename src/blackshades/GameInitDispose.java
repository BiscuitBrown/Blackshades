package blackshades;

import java.io.*;
import java.nio.*;
import java.util.*;
import static java.lang.Math.*;

import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.AL10.*;

import org.lwjgl.glfw.*;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.*;

import blackshades.Skeleton.*;
import blackshades.Models.*;
import blackshades.Person.*;

import static blackshades.Support.*;
import static blackshades.Quaternions.*;
import static blackshades.Constants.*;
import static blackshades.Textures.*;
import static blackshades.Person.*;
import static blackshades.Globals.*;

import static blackshades.Game.*;
import static blackshades.Support.random;

public class GameInitDispose {	
	
	public static final int 
		SOURCE_ID_SIZE = 100,
		SKELETON_MODELS_SIZE = 10,
    	GUN_MODELS_SIZE = 11,
    	COSTUME_SIZE = 10,
    	ANIMATION_SIZE = 30;
	
	
	

	public static void loadSounds(boolean musictoggle) throws IOException {
		
		STBVorbisInfo freqBuffer1 = STBVorbisInfo.malloc();
		
		ShortBuffer buffer;

		// EAX test

		// unsigned long ulEAXVal;
		// long lGlobalReverb;

		// initialize OpenAL

		long device = alcOpenDevice((ByteBuffer)null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);

		// load up some audio data...
		// generate ten OpenAL sample sets and two sources
		
		// Can call "alc" functions at any time
		
		long context = alcCreateContext(device, (IntBuffer)null);
		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
		alGenSources(gSourceID);
		alGenBuffers(gSampleSet);
		
		
		
		
		buffer = readVorbis("Data/Sounds/underwater.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[VISIONSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[VISIONSOUND], AL_BUFFER, gSampleSet[VISIONSOUND]);
		cleanupVorbis(gSourceID[VISIONSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/SoulIn.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[SOULINSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[SOULINSOUND], AL_BUFFER, gSampleSet[SOULINSOUND]);
		cleanupVorbis(gSourceID[SOULINSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/SoulOut.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[SOULOUTSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[SOULOUTSOUND], AL_BUFFER, gSampleSet[SOULOUTSOUND]);
		cleanupVorbis(gSourceID[SOULOUTSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/footstep1.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[FOOTSTEPSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[FOOTSTEPSOUND], AL_BUFFER, gSampleSet[FOOTSTEPSOUND]);
		cleanupVorbis(gSourceID[FOOTSTEPSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/footstep2.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[FOOTSTEPSOUND+1], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[FOOTSTEPSOUND+1], AL_BUFFER, gSampleSet[FOOTSTEPSOUND+1]);
		cleanupVorbis(gSourceID[FOOTSTEPSOUND+1], buffer);
		
		buffer = readVorbis("Data/Sounds/footstep3.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[FOOTSTEPSOUND+2], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[FOOTSTEPSOUND+2], AL_BUFFER, gSampleSet[FOOTSTEPSOUND+2]);
		cleanupVorbis(gSourceID[FOOTSTEPSOUND+2], buffer);
		
		buffer = readVorbis("Data/Sounds/footstep4.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[FOOTSTEPSOUND+3], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[FOOTSTEPSOUND+3], AL_BUFFER, gSampleSet[FOOTSTEPSOUND+3]);
		cleanupVorbis(gSourceID[FOOTSTEPSOUND+3], buffer);
		
		buffer = readVorbis("Data/Sounds/footstep5.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[FOOTSTEPSOUND+4], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[FOOTSTEPSOUND+4], AL_BUFFER, gSampleSet[FOOTSTEPSOUND+4]);
		cleanupVorbis(gSourceID[FOOTSTEPSOUND+4], buffer);
		
		buffer = readVorbis("Data/Sounds/bodyland.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[BODYLANDSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[BODYLANDSOUND], AL_BUFFER, gSampleSet[BODYLANDSOUND]);
		cleanupVorbis(gSourceID[BODYLANDSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/headland.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[HEADLANDSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[HEADLANDSOUND], AL_BUFFER, gSampleSet[HEADLANDSOUND]);
		cleanupVorbis(gSourceID[HEADLANDSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/sniperrifle.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[RIFLESOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[RIFLESOUND], AL_BUFFER, gSampleSet[RIFLESOUND]);
		cleanupVorbis(gSourceID[RIFLESOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/BodyHit.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[BODYHITSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[BODYHITSOUND], AL_BUFFER, gSampleSet[BODYHITSOUND]);
		cleanupVorbis(gSourceID[BODYHITSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/WallHit.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[WALLHITSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[WALLHITSOUND], AL_BUFFER, gSampleSet[WALLHITSOUND]);
		cleanupVorbis(gSourceID[WALLHITSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/machinegun.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[MACHINEGUNSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[MACHINEGUNSOUND], AL_BUFFER, gSampleSet[MACHINEGUNSOUND]);
		cleanupVorbis(gSourceID[MACHINEGUNSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/Nearbullet.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[NEARBULLETSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[NEARBULLETSOUND], AL_BUFFER, gSampleSet[NEARBULLETSOUND]);
		cleanupVorbis(gSourceID[NEARBULLETSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/riflewhack.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[HEADWHACKSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[HEADWHACKSOUND], AL_BUFFER, gSampleSet[HEADWHACKSOUND]);
		cleanupVorbis(gSourceID[HEADWHACKSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/headshot.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[HEADSHOTSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[HEADSHOTSOUND], AL_BUFFER, gSampleSet[HEADSHOTSOUND]);
		cleanupVorbis(gSourceID[HEADSHOTSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/reload.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[RELOADSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[RELOADSOUND], AL_BUFFER, gSampleSet[RELOADSOUND]);
		cleanupVorbis(gSourceID[RELOADSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/click.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[CLICKSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[CLICKSOUND], AL_BUFFER, gSampleSet[CLICKSOUND]);
		cleanupVorbis(gSourceID[CLICKSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/SW.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[PISTOL1SOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[PISTOL1SOUND], AL_BUFFER, gSampleSet[PISTOL1SOUND]);
		cleanupVorbis(gSourceID[PISTOL1SOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/glock.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[PISTOL2SOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[PISTOL2SOUND], AL_BUFFER, gSampleSet[PISTOL2SOUND]);
		cleanupVorbis(gSourceID[PISTOL2SOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/pinpull.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[PINPULLSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[PINPULLSOUND], AL_BUFFER, gSampleSet[PINPULLSOUND]);
		cleanupVorbis(gSourceID[PINPULLSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/pinreplace.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[PINREPLACESOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[PINREPLACESOUND], AL_BUFFER, gSampleSet[PINREPLACESOUND]);
		cleanupVorbis(gSourceID[PINREPLACESOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/handlerelease.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[GRENADETHROWSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[GRENADETHROWSOUND], AL_BUFFER, gSampleSet[GRENADETHROWSOUND]);
		cleanupVorbis(gSourceID[GRENADETHROWSOUND], buffer);

		buffer = readVorbis("Data/Sounds/bounce.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[BOUNCESOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[BOUNCESOUND], AL_BUFFER, gSampleSet[BOUNCESOUND]);
		cleanupVorbis(gSourceID[BOUNCESOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/bounce2.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[BOUNCE2SOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[BOUNCE2SOUND], AL_BUFFER, gSampleSet[BOUNCE2SOUND]);
		cleanupVorbis(gSourceID[BOUNCE2SOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/explosion.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[EXPLOSIONSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[EXPLOSIONSOUND], AL_BUFFER, gSampleSet[EXPLOSIONSOUND]);
		cleanupVorbis(gSourceID[EXPLOSIONSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/headland.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[BODYWHACKSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[BODYWHACKSOUND], AL_BUFFER, gSampleSet[BODYWHACKSOUND]);
		cleanupVorbis(gSourceID[BODYWHACKSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/rain.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[RAINSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[RAINSOUND], AL_BUFFER, gSampleSet[RAINSOUND]);
		cleanupVorbis(gSourceID[RAINSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/Lose.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[LOSESOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[LOSESOUND], AL_BUFFER, gSampleSet[LOSESOUND]);
		cleanupVorbis(gSourceID[LOSESOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/DisguiseKill.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[DISGUISEKILLSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[DISGUISEKILLSOUND], AL_BUFFER, gSampleSet[DISGUISEKILLSOUND]);
		cleanupVorbis(gSourceID[DISGUISEKILLSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/knifeslash.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[KNIFESLASHSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[KNIFESLASHSOUND], AL_BUFFER, gSampleSet[KNIFESLASHSOUND]);
		cleanupVorbis(gSourceID[KNIFESLASHSOUND], buffer);
		
		buffer = readVorbis("Data/Sounds/shotgun.ogg", freqBuffer1 = STBVorbisInfo.malloc());
		alBufferData(gSampleSet[SHOTGUNSOUND], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
		alSourcei(gSourceID[SHOTGUNSOUND], AL_BUFFER, gSampleSet[SHOTGUNSOUND]);
		cleanupVorbis(gSourceID[SHOTGUNSOUND], buffer);

		if(musictoggle){

			buffer = readVorbis("Data/Sounds/mainmenusong.ogg", freqBuffer1 = STBVorbisInfo.malloc());
			alBufferData(gSampleSet[MAINMENUSONG], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
			alSourcei(gSourceID[MAINMENUSONG], AL_BUFFER, gSampleSet[MAINMENUSONG]);
			cleanupVorbis(gSourceID[MAINMENUSONG], buffer);
			
			buffer = readVorbis("Data/Sounds/shootsong.ogg", freqBuffer1 = STBVorbisInfo.malloc());
			alBufferData(gSampleSet[SHOOTSONG], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
			alSourcei(gSourceID[SHOOTSONG], AL_BUFFER, gSampleSet[SHOOTSONG]);
			cleanupVorbis(gSourceID[SHOOTSONG], buffer);
			
			buffer = readVorbis("Data/Sounds/zombiesong.ogg", freqBuffer1 = STBVorbisInfo.malloc());
			alBufferData(gSampleSet[ZOMBIESONG], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
			alSourcei(gSourceID[ZOMBIESONG], AL_BUFFER, gSampleSet[ZOMBIESONG]);
			cleanupVorbis(gSourceID[ZOMBIESONG], buffer);
			
			buffer = readVorbis("Data/Sounds/knifesong.ogg", freqBuffer1 = STBVorbisInfo.malloc());
			alBufferData(gSampleSet[KNIFESONG], freqBuffer1.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, buffer, freqBuffer1.sample_rate());
			alSourcei(gSourceID[KNIFESONG], AL_BUFFER, gSampleSet[KNIFESONG]);
			cleanupVorbis(gSourceID[KNIFESONG], buffer);
		}
				
		FloatBuffer gLoc = createFloatBuffer(3);
		
		gLoc.put(0,0);
		gLoc.put(1,0);
		gLoc.put(2,0);

		alSourcefv(gSourceID[VISIONSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[VISIONSOUND], AL_LOOPING, 1);
		alSourcef(gSourceID[VISIONSOUND], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[SOULINSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[SOULINSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[SOULINSOUND], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[SOULOUTSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[SOULOUTSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[SOULOUTSOUND], AL_MIN_GAIN, 1);

		for(int i=0;i<5;i++){
			alSourcefv(gSourceID[FOOTSTEPSOUND+i], AL_POSITION, gLoc);
			alSourcei(gSourceID[FOOTSTEPSOUND+i], AL_LOOPING, 0);
			alSourcef(gSourceID[FOOTSTEPSOUND+i], AL_MIN_GAIN, 0);
		}	

		alSourcefv(gSourceID[BODYLANDSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[BODYLANDSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[BODYLANDSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[HEADLANDSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[HEADLANDSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[HEADLANDSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[RIFLESOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[RIFLESOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[RIFLESOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[BODYHITSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[BODYHITSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[BODYHITSOUND], AL_MIN_GAIN, .1f);

		alSourcefv(gSourceID[WALLHITSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[WALLHITSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[WALLHITSOUND], AL_MIN_GAIN, 0);
		alSourcef(gSourceID[WALLHITSOUND], AL_MAX_GAIN, .6f);

		alSourcefv(gSourceID[MACHINEGUNSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[MACHINEGUNSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[MACHINEGUNSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[NEARBULLETSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[NEARBULLETSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[NEARBULLETSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[HEADWHACKSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[HEADWHACKSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[HEADWHACKSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[HEADSHOTSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[HEADSHOTSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[HEADSHOTSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[RELOADSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[RELOADSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[RELOADSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[CLICKSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[CLICKSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[CLICKSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[PISTOL1SOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[PISTOL1SOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[PISTOL1SOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[PISTOL2SOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[PISTOL2SOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[PISTOL2SOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[PINPULLSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[PINPULLSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[PINPULLSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[PINREPLACESOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[PINREPLACESOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[PINREPLACESOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[GRENADETHROWSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[GRENADETHROWSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[GRENADETHROWSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[BOUNCESOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[BOUNCESOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[BOUNCESOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[BOUNCE2SOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[BOUNCE2SOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[BOUNCE2SOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[EXPLOSIONSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[EXPLOSIONSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[EXPLOSIONSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[BODYWHACKSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[BODYWHACKSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[BODYWHACKSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[RAINSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[RAINSOUND], AL_LOOPING, 1);
		alSourcef(gSourceID[RAINSOUND], AL_MIN_GAIN, .3f);

		alSourcefv(gSourceID[LOSESOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[LOSESOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[LOSESOUND], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[DISGUISEKILLSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[DISGUISEKILLSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[DISGUISEKILLSOUND], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[KNIFESLASHSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[KNIFESLASHSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[KNIFESLASHSOUND], AL_MIN_GAIN,0);

		alSourcefv(gSourceID[SHOTGUNSOUND], AL_POSITION, gLoc);
		alSourcei(gSourceID[SHOTGUNSOUND], AL_LOOPING, 0);
		alSourcef(gSourceID[SHOTGUNSOUND], AL_MIN_GAIN, 0);

		alSourcefv(gSourceID[KNIFESONG], AL_POSITION, gLoc);
		alSourcei(gSourceID[KNIFESONG], AL_LOOPING, 1);
		alSourcef(gSourceID[KNIFESONG], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[MAINMENUSONG], AL_POSITION, gLoc);
		alSourcei(gSourceID[MAINMENUSONG], AL_LOOPING, 1);
		alSourcef(gSourceID[MAINMENUSONG], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[ZOMBIESONG], AL_POSITION, gLoc);
		alSourcei(gSourceID[ZOMBIESONG], AL_LOOPING, 1);
		alSourcef(gSourceID[ZOMBIESONG], AL_MIN_GAIN, 1);

		alSourcefv(gSourceID[SHOOTSONG], AL_POSITION, gLoc);
		alSourcei(gSourceID[SHOOTSONG], AL_LOOPING, 1);
		alSourcef(gSourceID[SHOOTSONG], AL_MIN_GAIN, 1);

	}
			
		public static void loadingScreen(float percent) {

			float size=1;
			frustum = new Frustum();

			glLoadIdentity();

			//Clear to black

			glClearColor(0,0,0,1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//Background

			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
			glDisable(GL_CULL_FACE);
			glDisable(GL_LIGHTING);
			glDepthMask(false);
			glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
			glPushMatrix();										// Store The Projection Matrix
			glLoadIdentity();									// Reset The Projection Matrix
			glOrtho(0,640,0,480,-100,100);						// Set Up An Ortho Screen
			glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
			glPushMatrix();										// Store The Modelview Matrix

			for(int i=19;i>=0;i--){

				glLoadIdentity();								// Reset The Modelview Matrix
				glTranslatef(120-i*1,190-i*1,0);
				glScalef(400+i*2,30+i*2,1);

				glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
				glEnable(GL_BLEND);

				if(i!=0)glColor4f(1-(float)i/20-percent/100,1-(float)i/20-percent/100,1-(float)i/20-percent/100,1);
				if(i==0)glColor4f(0,0,0,1);

				glBegin(GL_QUADS);
					glVertex3f(0,		0, 	 0.0f);
					glVertex3f(1,	0, 	 0.0f);
					glVertex3f(1,	1, 0.0f);
					glVertex3f(0, 	1, 0.0f);
				glEnd();

			}

			glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
			glPopMatrix();										// Restore The Old Projection Matrix
			glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
			glPopMatrix();										// Restore The Old Projection Matrix
			glDisable(GL_BLEND);
			glDepthMask(true);		

			//Progress

			glDisable(GL_DEPTH_TEST);							// Disables Depth Testing
			glDisable(GL_CULL_FACE);
			glDisable(GL_LIGHTING);
			glDisable(GL_TEXTURE_2D);
			glDepthMask(false);
			glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
			glPushMatrix();										// Store The Projection Matrix
			glLoadIdentity();									// Reset The Projection Matrix
			glOrtho(0,640,0,480,-100,100);						// Set Up An Ortho Screen
			glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
			glPushMatrix();										// Store The Modelview Matrix

			for(int i=19;i>=0;i--){
				glLoadIdentity();								// Reset The Modelview Matrix
				glTranslatef(120,190,0);

				if(4*percent+i*2<400)glScalef(4*percent+i*2,30,1);

				if(4*percent+i*2>=400)glScalef(400,30,1);

				glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
				glEnable(GL_BLEND);
				glColor4f(1,0,0,.1f);

				glBegin(GL_QUADS);
					glVertex3f(0,		0, 	 0.0f);
					glVertex3f(1,	0, 	 0.0f);
					glVertex3f(1,	1, 0.0f);
					glVertex3f(0, 	1, 0.0f);
				glEnd();

			}

			glMatrixMode(GL_PROJECTION);						// Select The Projection Matrix
			glPopMatrix();										// Restore The Old Projection Matrix
			glMatrixMode(GL_MODELVIEW);							// Select The Modelview Matrix
			glPopMatrix();										// Restore The Old Projection Matrix
			glDisable(GL_BLEND);
			glDepthMask(true);
			
			//Text

			glEnable(GL_TEXTURE_2D);

			glColor4f(.6f-.6f*percent/100,0,0,1);
												
			text.glPrint(250,195,"LOADING...",1,1,640,480);
				
			glfwSwapBuffers(window);
		}
		
		public static class Game extends blackshades.Game{
		
		public static void initGame() throws FileNotFoundException, IOException	{

			//Setup loading screen
			
			float loadingscreenamount=0;
			float loadingscreenamounttotal=200;

			if(initialized)loadingscreenamounttotal=20;

			if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

			//Set up rain and snow

			precipitationhorz=60;
			precipitationvert=40;
			precipitationdensity=25;

			//Bodyguard stats
			
			if ( camera == null ) {camera = new Camera();}
			
			if (person[0] == null ) {person[0] = new Person();}
			person[0].playercoords.set(camera.position);
			person[0].oldplayercoords.set(person[0].playercoords);
			person[0].type=PLAYERTYPE;
			person[0].existing=true;

			for(int i=0;i<10;i++){
				person[0].reloads[i]=0;
			}

			//Level setup

			killedinnocent=false; //Haven't shot any civilians yet...

			if(customlevels){ //Load custom levels

				nummissions=1; //Default level in case of load failure
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=HANDGUN1;
				possiblegun[1]=HANDGUN2;
				possiblegun[2]=SHOTGUN;
				numpossibleguns=3;
				evilprobability=6;
				person[0].whichgun=KNIFE;
				person[0].reloads[person[0].whichgun]=6;	
				
				if(!gameinprogress)score=0;

				timeremaining=50;
				difficulty=.8f;
				
				File file = new File("customlevels.txt");
		        if (file.createNewFile()) {
		        	customlevels=false;
		        }
		        else {
		        	
		        	Scanner myReader = new Scanner(file);
		            myReader.useLocale(Locale.US);
		            
		            String word;
		            
		            int n =0;
		            		        	
		        	while (myReader.hasNextLine()) {
		        		word = myReader.nextLine();
		        		if(word.length() == 0 && (n<mission)) {
		        			n++;
		        		}
		        		else if(mission == n) {
		        			if(word.matches(".*(Number of missions:).*")) nummissions = myReader.nextInt();
			        		if(word.matches(".*(Level type:).*")) type = myReader.nextInt();
			        		if(word.matches(".*(Environment:).*")) environment = myReader.nextInt();
			        		if(word.matches(".*(How many different guns can assassins have:).*")) numpossibleguns = myReader.nextInt();
			        		if(word.matches(".*(List them:).*")) {
				        		word= myReader.nextLine();
				        		if(word.matches("^[0-9]$")) {
				        			for(int i = 0; i<possiblegun.length && word.matches("^[0-9]$"); i++, word = myReader.nextLine())
				        				possiblegun[i] = Integer.parseInt(word);
				        		}
			        		}
			        		if(word.matches(".*(One out of how many civilians is an assassin:).*")) evilprobability = myReader.nextInt();
			        		if(word.matches(".*(Starting gun:).*")) person[0].whichgun = myReader.nextInt();
			        		if(word.matches(".*(How many magazines/grenades:).*")) person[0].reloads[person[0].whichgun] = myReader.nextInt();
			        		if(word.matches(".*(Time requirement:).*")) timeremaining = myReader.nextInt();
			        		if(word.matches(".*(Difficulty:).*")) difficulty = myReader.nextFloat();
			        		if(n == mission) n--;
		        			
		        		}
						
		            }
		            myReader.close();
		        }

			}

			if(!customlevels){ //Setup hardcoded default levels

			if(mission==0){		
				environment=SUNNY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=HANDGUN1;
				possiblegun[1]=HANDGUN2;
				possiblegun[2]=SHOTGUN;
				numpossibleguns=3;
				evilprobability=6;
				person[0].whichgun=ASSAULTRIFLE;
				person[0].reloads[person[0].whichgun]=6;	

				if(!gameinprogress)score=0;

				timeremaining=50;
				difficulty=.6f;
			}

			if(mission==1){		
				environment=SNOWY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=KNIFE;
				possiblegun[1]=ASSAULTRIFLE;
				numpossibleguns=2;
				evilprobability=5;
				person[0].whichgun=HANDGUN2;
				person[0].reloads[person[0].whichgun]=3;	

				if(!gameinprogress)score=0;

				timeremaining=40;
				difficulty=.6f;

			}

			if(mission==2){		
				environment=FOGGY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=SNIPERRIFLE;
				numpossibleguns=1;
				evilprobability=5;
				person[0].whichgun=SNIPERRIFLE;
				person[0].reloads[person[0].whichgun]=4;	

				if(!gameinprogress)score=0;

				timeremaining=50;
				difficulty=0.9f;

			}

			if(mission==3){		
				environment=FIREY_ENVIRONMENT;
				type=ZOMBIE_TYPE;
				numpossibleguns=0;
				evilprobability=5;
				person[0].whichgun=SHOTGUN;
				person[0].reloads[person[0].whichgun]=5;	

				if(!gameinprogress)score=0;

				timeremaining=35;
				difficulty=.7f;
			}

			if(mission==4){	
				environment=SNOWY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=SNIPERRIFLE;
				possiblegun[1]=ASSAULTRIFLE;
				numpossibleguns=2;
				evilprobability=5;
				person[0].whichgun=GRENADE;
				person[0].reloads[person[0].whichgun]=20;	

				if(!gameinprogress)score=0;

				timeremaining=30;
				difficulty=.5f;
			}

			if(mission==5){	
				environment=RAINY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=HANDGUN1;
				possiblegun[1]=SHOTGUN;
				possiblegun[2]=ASSAULTRIFLE;
				numpossibleguns=3;
				evilprobability=6;
				person[0].whichgun=KNIFE;
				person[0].reloads[person[0].whichgun]=3;	

				if(!gameinprogress)score=0;

				timeremaining=40;
				difficulty=.8f;
			}

			if(mission==6){		
				environment=NIGHT_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[1]=HANDGUN1;
				possiblegun[2]=HANDGUN2;
				possiblegun[3]=SHOTGUN;
				numpossibleguns=3;
				evilprobability=5;
				person[0].whichgun=HANDGUN1;
				person[0].reloads[person[0].whichgun]=4;	

				if(!gameinprogress)score=0;

				timeremaining=30;
				difficulty=1;
			}

			if(mission==7){		
				environment=FIREY_ENVIRONMENT;
				type=ZOMBIE_TYPE;
				person[0].whichgun=ASSAULTRIFLE;
				person[0].reloads[person[0].whichgun]=5;	

				if(!gameinprogress)score=0;

				timeremaining=30;
				difficulty=1;
			}

			if(mission==8){		
				environment=RAINY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=HANDGUN1;
				possiblegun[1]=HANDGUN2;
				possiblegun[2]=SHOTGUN;
				possiblegun[3]=SNIPERRIFLE;
				possiblegun[4]=ASSAULTRIFLE;
				numpossibleguns=5;
				evilprobability=5;
				person[0].whichgun=NOGUN;
				person[0].reloads[person[0].whichgun]=3;	

				if(!gameinprogress)score=0;

				timeremaining=40;
				difficulty=.8f;
			}

			if(mission==9){		
				environment=SNOWY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=KNIFE;
				possiblegun[1]=HANDGUN1;
				possiblegun[2]=HANDGUN2;
				possiblegun[3]=SHOTGUN;
				possiblegun[4]=SNIPERRIFLE;
				possiblegun[5]=ASSAULTRIFLE;
				numpossibleguns=6;
				evilprobability=4;
				person[0].whichgun=HANDGUN1;
				person[0].reloads[person[0].whichgun]=3;	

				if(!gameinprogress)score=0;

				timeremaining=90;
				difficulty=1;
			}

			if(mission==10){		
				environment=NIGHT_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=SNIPERRIFLE;
				numpossibleguns=1;
				evilprobability=5;
				person[0].whichgun=SNIPERRIFLE;
				person[0].reloads[person[0].whichgun]=4;	

				if(!gameinprogress)score=0;

				timeremaining=30;
				difficulty=1.3f;
			}

			if(mission==11){		
				environment=SUNNY_ENVIRONMENT;
				type=RANDOMSHOOT_TYPE;
				possiblegun[0]=KNIFE;
				possiblegun[1]=SNIPERRIFLE;
				numpossibleguns=2;
				evilprobability=4;
				person[0].whichgun=KNIFE;

				if(!gameinprogress)score=0;

				timeremaining=30;
				difficulty=1.5f;
			}

			if(mission==12){		
				environment=FIREY_ENVIRONMENT;
				type=ZOMBIE_TYPE;
				possiblegun[0]=KNIFE;
				possiblegun[1]=SNIPERRIFLE;
				person[0].whichgun=HANDGUN2;
				person[0].reloads[person[0].whichgun]=10;	

				if(!gameinprogress)score=0;

				timeremaining=60;
				difficulty=1.5f;
			}

			nummissions=13;

			}

			//Setup fast radian to degree conversion

			rad2deg= 56.54866776f;
			visions=0;

			//Setup bounding cylinder model

			float boundingscale=3;

			if(!initialized){	
				boundingpoints[0] = new XYZ();
				boundingpoints[0].z=boundingscale;
				boundingpoints[0].y=0;

				for(int i=1;i<8;i++){
					boundingpoints[i]=doRotation(boundingpoints[0],0,i*360/7,0);
				}
			}

			civkills=0;
			badkills=0;
			goodkills=0;
			enemystate=2;

			if(!initialized){	
				if(!azertykeyboard){

					forwardskey=GLFW_KEY_W;
					backwardskey=GLFW_KEY_S;
					leftkey=GLFW_KEY_A;
					rightkey=GLFW_KEY_D;
					aimkey=GLFW_KEY_Q;
					psychicaimkey=GLFW_KEY_E;
					psychickey=GLFW_KEY_Z;
				}

				if(azertykeyboard){

					forwardskey=GLFW_KEY_Z;
					backwardskey=GLFW_KEY_S;
					leftkey=GLFW_KEY_Q;
					rightkey=GLFW_KEY_D;
					aimkey=GLFW_KEY_A;
					psychicaimkey=GLFW_KEY_E;
					psychickey=GLFW_KEY_W;
				}

				soundscalefactor=SOUNDSCALEFACTORDEFAULT; //Setup sound falloff
				gQuit=false;

				//Sounds

				loadSounds(musictoggle);

				//Play correct song

				if(environment==RAINY_ENVIRONMENT)alSourcePlay(gSourceID[RAINSOUND]);

				if(environment!=RAINY_ENVIRONMENT)alSourcePause(gSourceID[RAINSOUND]);

				alSourceStop(gSourceID[whichsong]);
				alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 0);
				alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 0);
				whichsong=MAINMENUSONG;
				alSourceStop(gSourceID[whichsong]);
				alSourcef(gSourceID[whichsong], AL_PITCH, 1);
				alSourcePlay(gSourceID[whichsong]);
				alSourcef(gSourceID[whichsong], AL_MIN_GAIN, 1);
				alSourcef(gSourceID[whichsong], AL_MAX_GAIN, 1);	
			}

			loadingscreenamount+=5;
			if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

			olddrawmouse = new Point();
			
			//Setup random seed
			random = new Random();									
		
			gamespeed=1;

			//Setup camera

			camera.position.set(0);
			camera.position.x=NUM_BLOCKS/2*BLOCK_SPACING+BLOCK_SPACING/2;
			camera.position.z=NUM_BLOCKS/2*BLOCK_SPACING+BLOCK_SPACING/2;
			camera.position.y=30;
			camera.oldposition.set(camera.position);
			numpeople=1;

			//Setup path to walk around blocks
			
			if (path == null) {path = new Model();}
			path.load("Data/Models/Path.solid");
			path.rotate(90,0,0);
			path.scale(.8f,.8f,.8f);
			path.calculateNormals();

			loadingscreenamount+=5;

			if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

			person[0].speedmult=1.3f;

			//Add vip
			
			if(person[numpeople] == null) person[numpeople] = new Person();

			person[numpeople].playerrotation=0;

			person[numpeople].whichcostume=VIPCOSTUME; 

			person[numpeople].whichblockx=(int) ((person[0].playercoords.x+BLOCK_SPACING/2)/BLOCK_SPACING);
			person[numpeople].whichblocky=(int) ((person[0].playercoords.x+BLOCK_SPACING/2)/BLOCK_SPACING);

			person[numpeople].pathnum=-1;
			person[numpeople].oldpathnum=-1;
			person[numpeople].oldoldpathnum=-1;
			person[numpeople].oldoldoldpathnum=-1;

			while(person[numpeople].pathnum<0||person[numpeople].pathnum>=path.vertexNum||person[numpeople].pathnum==1){
				person[numpeople].pathnum=(int) (random()%path.vertexNum);
			}
			
			person[numpeople].pathtarget.x=path.vertex[person[numpeople].pathnum].x;
			person[numpeople].pathtarget.z=path.vertex[person[numpeople].pathnum].z;			
			person[numpeople].pathsize=.98f +(float)(abs(random()%20))/400;
			person[numpeople].pathtarget.incrementMultiply(person[numpeople].pathsize);
			person[numpeople].pathtarget.x+=person[numpeople].whichblockx*BLOCK_SPACING;
			person[numpeople].pathtarget.z+=person[numpeople].whichblocky*BLOCK_SPACING;
			
			person[numpeople].playercoords.set(person[numpeople].pathtarget);
			person[numpeople].oldplayercoords.set(person[numpeople].playercoords);

			person[0].playercoords.set(person[numpeople].playercoords);
			person[0].playercoords.x+=1;
			person[0].playercoords.z+=1;
			person[0].oldplayercoords.set(person[0].playercoords);
			
			if(person[numpeople].skeleton == null) {person[numpeople].skeleton = new Skeleton();}
			
			person[numpeople].skeleton.free=false;

			person[numpeople].targetanimation=WALKANIM;
			person[numpeople].speed=1;
			person[numpeople].existing=false;
			person[numpeople].speedmult=1;

			if(type==ZOMBIE_TYPE)person[numpeople].speedmult=.8f;

			person[numpeople].health=100;	
			person[numpeople].playerrotation2=0;//20;

			person[numpeople].lastdistancevictim=200000;

			if(person[numpeople].skeleton.broken !=0)person[numpeople].skeleton.load("Data/Skeleton/Basic Figure");

			person[numpeople].type=VIPTYPE;
			
			person[numpeople].whichgun=NOGUN; person[numpeople].aiming=false; person[numpeople].killtarget=-1;
			
			person[numpeople].existing=true;

			citypeoplenum[person[numpeople].whichblockx][person[numpeople].whichblocky]++;
			numpeople++;
			spawndelay=.1f;

			XYZ vipdistance = new XYZ();
			vipdistance.x=10000000;
			vipgoal = new XYZ(person[1].playercoords.add(doRotation(vipdistance,0,random()%360,0)));

			//Setup block models	

			if(!initialized){

				blocks[0] = new Model();
				blocks[0].load("Data/Models/Block1.solid");
				blocks[0].rotate(90,0,0);
				blocks[0].scale(.8f,.8f,.8f);
				blocks[0].calculateNormals();

				blocks[1] = new Model();
				blocks[1].load("Data/Models/Block2.solid");
				blocks[1].rotate(90,0,0);
				blocks[1].scale(.8f,.8f,.8f);
				blocks[1].calculateNormals();

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				blocks[2] = new Model();
				blocks[2].load("Data/Models/Block3.solid");
				blocks[2].rotate(90,0,0);
				blocks[2].scale(.8f,.8f,.8f);
				blocks[2].calculateNormals();

				
				blocks[3] = new Model();
				blocks[3].load("Data/Models/Block4.solid");
				blocks[3].rotate(90,0,0);
				blocks[3].scale(.8f,.8f,.8f);
				blocks[3].calculateNormals();
				
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				sidewalkcollide = new Model();
				sidewalkcollide.load("Data/Models/Lowheightcollide.solid");
				sidewalkcollide.rotate(90,0,0);
				sidewalkcollide.scale(.8f,.8f,.8f);
				sidewalkcollide.calculateNormals();

				blockwalls[0] = new Model();
				blockwalls[0].load("Data/Models/Block1collide.solid");
				blockwalls[0].rotate(90,0,0);
				blockwalls[0].scale(.8f,.75f,.8f);
				blockwalls[0].calculateNormals();

				blockwalls[1] = new Model();
				blockwalls[1].load("Data/Models/Block2collide.solid");
				blockwalls[1].rotate(90,0,0);
				blockwalls[1].scale(.8f,.75f,.8f);
				blockwalls[1].calculateNormals();

				blockwalls[2] = new Model();
				blockwalls[2].load("Data/Models/Block3collide.solid");
				blockwalls[2].rotate(90,0,0);
				blockwalls[2].scale(.8f,.75f,.8f);
				blockwalls[2].calculateNormals();

				blockwalls[3] = new Model();
				blockwalls[3].load("Data/Models/Block4collide.solid");
				blockwalls[3].rotate(90,0,0);
				blockwalls[3].scale(.8f,.75f,.8f);
				blockwalls[3].calculateNormals();

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				blockroofs[0] = new Model();
				blockroofs[0].load("Data/Models/Highblock1collide.solid");
				blockroofs[0].rotate(90,0,0);
				blockroofs[0].scale(.8f,.8f,.8f);
				blockroofs[0].calculateNormals();

				blockroofs[1] = new Model();
				blockroofs[1].load("Data/Models/Highblock2collide.solid");
				blockroofs[1].rotate(90,0,0);
				blockroofs[1].scale(.8f,.8f,.8f);
				blockroofs[1].calculateNormals();

				blockroofs[2] = new Model();
				blockroofs[2].load("Data/Models/Highblock3collide.solid");
				blockroofs[2].rotate(90,0,0);
				blockroofs[2].scale(.8f,.8f,.8f);
				blockroofs[2].calculateNormals();

				blockroofs[3] = new Model();
				blockroofs[3].load("Data/Models/Highblock4collide.solid");
				blockroofs[3].rotate(90,0,0);
				blockroofs[3].scale(.8f,.8f,.8f);
				blockroofs[3].calculateNormals();

				blockcollide[0] = new Model();
				blockcollide[0].load("Data/Models/Block1complete.solid");
				blockcollide[0].rotate(90,0,0);
				blockcollide[0].scale(.8f,.8f,.8f);
				blockcollide[0].calculateNormals();

				blockcollide[1] = new Model();
				blockcollide[1].load("Data/Models/Block2complete.solid");
				blockcollide[1].rotate(90,0,0);
				blockcollide[1].scale(.8f,.8f,.8f);
				blockcollide[1].calculateNormals();

				blockcollide[2] = new Model();
				blockcollide[2].load("Data/Models/Block3complete.solid");
				blockcollide[2].rotate(90,0,0);
				blockcollide[2].scale(.8f,.8f,.8f);
				blockcollide[2].calculateNormals();

				blockcollide[3] = new Model();
				blockcollide[3].load("Data/Models/Block4complete.solid");
				blockcollide[3].rotate(90,0,0);
				blockcollide[3].scale(.8f,.8f,.8f);
				blockcollide[3].calculateNormals();

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				blocksimplecollide[0] = new Model();
				blocksimplecollide[0].load("Data/Models/lowsimplecollide1.solid");
				blocksimplecollide[0].rotate(90,0,0);
				blocksimplecollide[0].scale(.8f,.8f,.8f);
				blocksimplecollide[0].calculateNormals();

				blocksimplecollide[1] = new Model();
				blocksimplecollide[1].load("Data/Models/lowsimplecollide2.solid");
				blocksimplecollide[1].rotate(90,0,0);
				blocksimplecollide[1].scale(.8f,.8f,.8f);
				blocksimplecollide[1].calculateNormals();

				blocksimplecollide[2] = new Model();
				blocksimplecollide[2].load("Data/Models/lowsimplecollide3.solid");
				blocksimplecollide[2].rotate(90,0,0);
				blocksimplecollide[2].scale(.8f,.8f,.8f);
				blocksimplecollide[2].calculateNormals();

				blocksimplecollide[3] = new Model();
				blocksimplecollide[3].load("Data/Models/lowsimplecollide4.solid");
				blocksimplecollide[3].rotate(90,0,0);
				blocksimplecollide[3].scale(.8f,.8f,.8f);
				blocksimplecollide[3].calculateNormals();

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				blockocclude = new Model();
				blockocclude.load("Data/Models/BlockOcclude.solid");
				blockocclude.rotate(90,0,0);
				blockocclude.scale(.8f,.8f,.8f);
				blockocclude.calculateNormals();

				blocksimple = new Model();
				blocksimple.load("Data/Models/Blocksimple.solid");
				blocksimple.rotate(90,0,0);
				blocksimple.scale(.8f,2,.8f);
				blocksimple.calculateNormals();

				street = new Model();
				street.load("Data/Models/streetsubdivided2.solid");
				street.rotate(90,0,0);
				street.scale(.01f,.01f,.01f);
				street.calculateNormals();
				
				bigStreet = new Model(street);
				bigStreet.scale(10000,10000,10000);

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);			

				path = new Model();
				path.load("Data/Models/Path.solid");
				path.rotate(90,0,0);
				path.scale(.8f,.8f,.8f);
				path.calculateNormals();

				//Fix block radius
				
				for(int i=0;i<blocks.length;i++){
					blocks[i].boundingspherecenter.x=0;
					blocks[i].boundingspherecenter.z=0;
					blocks[i].boundingsphereradius=0;

					for(int x=i;x<26 ; x++){

						if(findDistancefast(blocks[i].boundingspherecenter,blocks[i].vertex[x])>blocks[i].boundingsphereradius ) {
							blocks[i].boundingsphereradius=findDistancefast(blocks[i].boundingspherecenter,blocks[i].vertex[x]);
						}
					}
					blocks[i].boundingsphereradius=(float) sqrt(blocks[i].boundingsphereradius);
				}
			}	

			mousesensitivity=1;

			//init city block rotations

			for(int i=0;i<NUM_BLOCKS ;i++){

				for(int j=0;j<NUM_BLOCKS;j++){

					cityrotation[i][j]=random()%4;
					citytype[i][j]=random()%4;
					citypeoplenum[i][j]=0;

					if(citytype[i][j]<0)citytype[i][j]=0;

					if(citytype[i][j]>3)citytype[i][j]=3;
				}
			}

				if(!initialized){

				//Load player model
				
				skeletonmodels[0] = new Model();
				skeletonmodels[0].load("Data/Models/Head.solid");
				skeletonmodels[0].rotate(90,0,0);
				skeletonmodels[0].scale(.02f,.02f,.02f);
				skeletonmodels[0].calculateNormals();

				skeletonmodels[1] = new Model();
				skeletonmodels[1].load("Data/Models/Chest.solid");
				skeletonmodels[1].rotate(90,0,0);
				skeletonmodels[1].scale(.02f,.02f,.02f);
				skeletonmodels[1].calculateNormals();

				skeletonmodels[2] = new Model();
				skeletonmodels[2].load("Data/Models/Abdomen.solid");
				skeletonmodels[2].rotate(90,0,0);
				skeletonmodels[2].scale(.02f,.02f,.02f);
				skeletonmodels[2].calculateNormals();

				skeletonmodels[3] = new Model();
				skeletonmodels[3].load("Data/Models/Upper arm.solid");
				skeletonmodels[3].rotate(90,0,0);
				skeletonmodels[3].scale(.02f,.02f,.02f);
				skeletonmodels[3].calculateNormals();

				skeletonmodels[4] = new Model();
				skeletonmodels[4].load("Data/Models/Lower arm.solid");
				skeletonmodels[4].rotate(90,0,0);
				skeletonmodels[4].scale(.02f,.02f,.02f);
				skeletonmodels[4].calculateNormals();

				skeletonmodels[5] = new Model();
				skeletonmodels[5].load("Data/Models/Hand.solid");
				skeletonmodels[5].rotate(90,0,0);
				skeletonmodels[5].scale(.02f,.02f,.02f);
				skeletonmodels[5].calculateNormals();

				skeletonmodels[6] = new Model();
				skeletonmodels[6].load("Data/Models/Upper leg.solid");
				skeletonmodels[6].rotate(90,0,0);
				skeletonmodels[6].scale(.02f,.02f,.02f);
				skeletonmodels[6].calculateNormals();

				skeletonmodels[7] = new Model();
				skeletonmodels[7].load("Data/Models/Lower leg.solid");
				skeletonmodels[7].rotate(90,0,0);
				skeletonmodels[7].scale(.02f,.02f,.02f);
				skeletonmodels[7].calculateNormals();

				skeletonmodels[8] = new Model();
				skeletonmodels[8].load("Data/Models/Foot.solid");
				skeletonmodels[8].rotate(90,0,0);
				skeletonmodels[8].scale(.02f,.02f,.02f);
				skeletonmodels[8].calculateNormals();

				skeletonmodels[9] = new Model();
				skeletonmodels[9].load("Data/Models/Shades.solid");
				skeletonmodels[9].rotate(90,0,0);
				skeletonmodels[9].scale(.02f,.02f,.02f);
				skeletonmodels[9].calculateNormals();

				//Load gun models

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);			

				gunmodels[SNIPERRIFLEMODEL] = new Model();
				gunmodels[SNIPERRIFLEMODEL].load("Data/Models/sniperrifle.solid");
				gunmodels[SNIPERRIFLEMODEL].rotate(0,0,90);
				gunmodels[SNIPERRIFLEMODEL].scale(.001f,.001f,.001f);
				gunmodels[SNIPERRIFLEMODEL].calculateNormals();

				gunmodels[ASSAULTRIFLEMODEL] = new Model();
				gunmodels[ASSAULTRIFLEMODEL].load("Data/Models/AssaultRifle.solid");
				gunmodels[ASSAULTRIFLEMODEL].rotate(0,0,90);
				gunmodels[ASSAULTRIFLEMODEL].scale(.01f,.01f,.01f);
				gunmodels[ASSAULTRIFLEMODEL].calculateNormals();

				gunmodels[HANDGUNBASEMODEL] = new Model();
				gunmodels[HANDGUNBASEMODEL].load("Data/Models/Handgunbase.solid");
				gunmodels[HANDGUNBASEMODEL].rotate(0,0,90);
				gunmodels[HANDGUNBASEMODEL].rotate(180,0,0);
				gunmodels[HANDGUNBASEMODEL].scale(.014f,.014f,.014f);
				gunmodels[HANDGUNBASEMODEL].calculateNormals();
				gunmodels[HANDGUNBASEMODEL].multColor(.6f);
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				gunmodels[HANDGUNSLIDEMODEL] = new Model();
				gunmodels[HANDGUNSLIDEMODEL].load("Data/Models/Handgunslide.solid");
				gunmodels[HANDGUNSLIDEMODEL].rotate(0,0,90);
				gunmodels[HANDGUNSLIDEMODEL].rotate(180,0,0);
				gunmodels[HANDGUNSLIDEMODEL].scale(.014f,.014f,.014f);
				gunmodels[HANDGUNSLIDEMODEL].calculateNormals();
				gunmodels[HANDGUNSLIDEMODEL].multColor(.6f);

				gunmodels[HANDGUN2BASEMODEL] = new Model();
				gunmodels[HANDGUN2BASEMODEL].load("Data/Models/Glockbase.solid");
				gunmodels[HANDGUN2BASEMODEL].rotate(0,0,90);
				gunmodels[HANDGUN2BASEMODEL].rotate(180,0,0);
				gunmodels[HANDGUN2BASEMODEL].scale(.014f,.014f,.014f);
				gunmodels[HANDGUN2BASEMODEL].calculateNormals();
				gunmodels[HANDGUN2BASEMODEL].multColor(.6f);

				gunmodels[HANDGUN2SLIDEMODEL] = new Model();
				gunmodels[HANDGUN2SLIDEMODEL].load("Data/Models/Glockslide.solid");
				gunmodels[HANDGUN2SLIDEMODEL].rotate(0,0,90);
				gunmodels[HANDGUN2SLIDEMODEL].rotate(180,0,0);
				gunmodels[HANDGUN2SLIDEMODEL].scale(.014f,.014f,.014f);
				gunmodels[HANDGUN2SLIDEMODEL].calculateNormals();
				gunmodels[HANDGUN2SLIDEMODEL].multColor(.6f);
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				gunmodels[GRENADEBASEMODEL] = new Model();
				gunmodels[GRENADEBASEMODEL].load("Data/Models/Grenadebase.solid");
				gunmodels[GRENADEBASEMODEL].rotate(0,0,90);
				gunmodels[GRENADEBASEMODEL].rotate(180,0,0);
				gunmodels[GRENADEBASEMODEL].scale(.014f,.014f,.014f);
				gunmodels[GRENADEBASEMODEL].calculateNormals();

				gunmodels[GRENADEPINMODEL] = new Model();
				gunmodels[GRENADEPINMODEL].load("Data/Models/Grenadepin.solid");
				gunmodels[GRENADEPINMODEL].rotate(0,0,90);
				gunmodels[GRENADEPINMODEL].rotate(180,0,0);
				gunmodels[GRENADEPINMODEL].scale(.014f,.014f,.014f);
				gunmodels[GRENADEPINMODEL].calculateNormals();

				gunmodels[GRENADESPOONMODEL] = new Model();
				gunmodels[GRENADESPOONMODEL].load("Data/Models/Grenadespoon.solid");
				gunmodels[GRENADESPOONMODEL].rotate(0,0,90);
				gunmodels[GRENADESPOONMODEL].rotate(180,0,0);
				gunmodels[GRENADESPOONMODEL].scale(.014f,.014f,.014f);
				gunmodels[GRENADESPOONMODEL].calculateNormals();

				gunmodels[KNIFEMODEL] = new Model();
				gunmodels[KNIFEMODEL].load("Data/Models/Knife.solid");
				gunmodels[KNIFEMODEL].rotate(0,0,90);
				gunmodels[KNIFEMODEL].rotate(180,0,0);
				gunmodels[KNIFEMODEL].scale(.014f,.014f,.014f);
				gunmodels[KNIFEMODEL].calculateNormals();

				gunmodels[SHOTGUNMODEL] = new Model();
				gunmodels[SHOTGUNMODEL].load("Data/Models/Shotgun.solid");
				gunmodels[SHOTGUNMODEL].rotate(0,0,90);
				gunmodels[SHOTGUNMODEL].scale(.001f,.001f,.001f);
				gunmodels[SHOTGUNMODEL].calculateNormals();
				gunmodels[SHOTGUNMODEL].multColor(.6f);
				}

				loadingscreenamount+=5;

				if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);			

				//Setup costumes

				float headcolor[] = new float[3];
				float footcolor[] = new float[3];
				float handcolor[] = new float[3];
				float topcolor[] = new float[3];
				float bottomcolor[] = new float[3];

				//Police

				headcolor[0]=(float)240/255;
				headcolor[1]=(float)183/255;
				headcolor[2]=(float)132/255;

				footcolor[0]=(float)119/255;
				footcolor[1]=(float)68/255;
				footcolor[2]=(float)18/255;

				handcolor[0]=(float)240/255;
				handcolor[1]=(float)183/255;
				handcolor[2]=(float)132/255;

				topcolor[0]=(float)14/255;
				topcolor[1]=(float)18/255;
				topcolor[2]=(float)195/255;

				bottomcolor[0]=(float)14/255;
				bottomcolor[1]=(float)18/255;
				bottomcolor[2]=(float)195/255;

				//Greenish skin if zombies

				if(type==ZOMBIE_TYPE){

					headcolor[0]=(float)223/255;
					headcolor[1]=(float)243/255;
					headcolor[2]=(float)197/255;

					handcolor[0]=(float)223/255;
					handcolor[1]=(float)243/255;
					handcolor[2]=(float)197/255;
				}

				costume[POLICECOSTUME] = new Costume();
				costume[POLICECOSTUME].headcolor[0]=headcolor[0];
				costume[POLICECOSTUME].headcolor[1]=headcolor[1];
				costume[POLICECOSTUME].headcolor[2]=headcolor[2];

				costume[POLICECOSTUME].handcolor[0]=handcolor[0];
				costume[POLICECOSTUME].handcolor[1]=handcolor[1];
				costume[POLICECOSTUME].handcolor[2]=handcolor[2];

				costume[POLICECOSTUME].chestcolor[0]=topcolor[0];
				costume[POLICECOSTUME].chestcolor[1]=topcolor[1];
				costume[POLICECOSTUME].chestcolor[2]=topcolor[2];

				costume[POLICECOSTUME].abdomencolor[0]=topcolor[0];
				costume[POLICECOSTUME].abdomencolor[1]=topcolor[1];
				costume[POLICECOSTUME].abdomencolor[2]=topcolor[2];

				costume[POLICECOSTUME].upperarmcolor[0]=topcolor[0];
				costume[POLICECOSTUME].upperarmcolor[1]=topcolor[1];
				costume[POLICECOSTUME].upperarmcolor[2]=topcolor[2];

				costume[POLICECOSTUME].lowerarmcolor[0]=topcolor[0];
				costume[POLICECOSTUME].lowerarmcolor[1]=topcolor[1];
				costume[POLICECOSTUME].lowerarmcolor[2]=topcolor[2];

				costume[POLICECOSTUME].upperlegcolor[0]=bottomcolor[0];
				costume[POLICECOSTUME].upperlegcolor[1]=bottomcolor[1];
				costume[POLICECOSTUME].upperlegcolor[2]=bottomcolor[2];

				costume[POLICECOSTUME].lowerlegcolor[0]=bottomcolor[0];
				costume[POLICECOSTUME].lowerlegcolor[1]=bottomcolor[1];
				costume[POLICECOSTUME].lowerlegcolor[2]=bottomcolor[2];

				costume[POLICECOSTUME].footcolor[0]=footcolor[0];
				costume[POLICECOSTUME].footcolor[1]=footcolor[1];
				costume[POLICECOSTUME].footcolor[2]=footcolor[2];

				//casual

				topcolor[0]=(float)14/255;
				topcolor[1]=(float)200/255;
				topcolor[2]=(float)30/255;

				bottomcolor[0]=(float)14/255;
				bottomcolor[1]=(float)18/255;
				bottomcolor[2]=(float)195/255;

				costume[CASUALCOSTUMES] = new Costume();
				costume[CASUALCOSTUMES].headcolor[0]=headcolor[0];
				costume[CASUALCOSTUMES].headcolor[1]=headcolor[1];
				costume[CASUALCOSTUMES].headcolor[2]=headcolor[2];

				costume[CASUALCOSTUMES].handcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES].handcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES].handcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES].chestcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES].chestcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES].chestcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES].abdomencolor[0]=topcolor[0];
				costume[CASUALCOSTUMES].abdomencolor[1]=topcolor[1];
				costume[CASUALCOSTUMES].abdomencolor[2]=topcolor[2];

				costume[CASUALCOSTUMES].upperarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES].upperarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES].upperarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES].lowerarmcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES].lowerarmcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES].lowerarmcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES].upperlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES].upperlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES].upperlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES].lowerlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES].lowerlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES].lowerlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES].footcolor[0]=footcolor[0];
				costume[CASUALCOSTUMES].footcolor[1]=footcolor[1];
				costume[CASUALCOSTUMES].footcolor[2]=footcolor[2];

				//casual 2

				topcolor[0]=(float)140/255;
				topcolor[1]=(float)55/255;
				topcolor[2]=(float)4/255;

				bottomcolor[0]=(float)14/255;
				bottomcolor[1]=(float)18/255;
				bottomcolor[2]=(float)135/255;

				costume[CASUALCOSTUMES+1] = new Costume(); 
				costume[CASUALCOSTUMES+1].headcolor[0]=headcolor[0];
				costume[CASUALCOSTUMES+1].headcolor[1]=headcolor[1];
				costume[CASUALCOSTUMES+1].headcolor[2]=headcolor[2];

				costume[CASUALCOSTUMES+1].handcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES+1].handcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES+1].handcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES+1].chestcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+1].chestcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+1].chestcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+1].abdomencolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+1].abdomencolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+1].abdomencolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+1].upperarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+1].upperarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+1].upperarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+1].lowerarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+1].lowerarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+1].lowerarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+1].upperlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+1].upperlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+1].upperlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+1].lowerlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+1].lowerlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+1].lowerlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+1].footcolor[0]=footcolor[0];
				costume[CASUALCOSTUMES+1].footcolor[1]=footcolor[1];
				costume[CASUALCOSTUMES+1].footcolor[2]=footcolor[2];

				//casual 3

				topcolor[0]=(float)134/255;
				topcolor[1]=(float)80/255;
				topcolor[2]=(float)3/255;

				bottomcolor[0]=(float)30/255;
				bottomcolor[1]=(float)30/255;
				bottomcolor[2]=(float)30/255;
			
				footcolor[0]=(float)20/255;
				footcolor[1]=(float)20/255;
				footcolor[2]=(float)20/255;
				
				costume[CASUALCOSTUMES+2] = new Costume();
				costume[CASUALCOSTUMES+2].headcolor[0]=headcolor[0];
				costume[CASUALCOSTUMES+2].headcolor[1]=headcolor[1];
				costume[CASUALCOSTUMES+2].headcolor[2]=headcolor[2];

				costume[CASUALCOSTUMES+2].handcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES+2].handcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES+2].handcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES+2].chestcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+2].chestcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+2].chestcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+2].abdomencolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+2].abdomencolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+2].abdomencolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+2].upperarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+2].upperarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+2].upperarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+2].lowerarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+2].lowerarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+2].lowerarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+2].upperlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+2].upperlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+2].upperlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+2].lowerlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+2].lowerlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+2].lowerlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+2].footcolor[0]=footcolor[0];
				costume[CASUALCOSTUMES+2].footcolor[1]=footcolor[1];
				costume[CASUALCOSTUMES+2].footcolor[2]=footcolor[2];

				//casual 4

				topcolor[0]=(float)228/255;
				topcolor[1]=(float)220/255;
				topcolor[2]=(float)0/255;

				bottomcolor[0]=(float)20/255;
				bottomcolor[1]=(float)20/255;
				bottomcolor[2]=(float)20/255;

				footcolor[0]=(float)119/255;
				footcolor[1]=(float)68/255;
				footcolor[2]=(float)18/255;

				costume[CASUALCOSTUMES+3] = new Costume();
				costume[CASUALCOSTUMES+3].headcolor[0]=headcolor[0];
				costume[CASUALCOSTUMES+3].headcolor[1]=headcolor[1];
				costume[CASUALCOSTUMES+3].headcolor[2]=headcolor[2];

				costume[CASUALCOSTUMES+3].handcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES+3].handcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES+3].handcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES+3].chestcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+3].chestcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+3].chestcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+3].abdomencolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+3].abdomencolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+3].abdomencolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+3].upperarmcolor[0]=topcolor[0];
				costume[CASUALCOSTUMES+3].upperarmcolor[1]=topcolor[1];
				costume[CASUALCOSTUMES+3].upperarmcolor[2]=topcolor[2];

				costume[CASUALCOSTUMES+3].lowerarmcolor[0]=handcolor[0];
				costume[CASUALCOSTUMES+3].lowerarmcolor[1]=handcolor[1];
				costume[CASUALCOSTUMES+3].lowerarmcolor[2]=handcolor[2];

				costume[CASUALCOSTUMES+3].upperlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+3].upperlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+3].upperlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+3].lowerlegcolor[0]=bottomcolor[0];
				costume[CASUALCOSTUMES+3].lowerlegcolor[1]=bottomcolor[1];
				costume[CASUALCOSTUMES+3].lowerlegcolor[2]=bottomcolor[2];

				costume[CASUALCOSTUMES+3].footcolor[0]=footcolor[0];
				costume[CASUALCOSTUMES+3].footcolor[1]=footcolor[1];
				costume[CASUALCOSTUMES+3].footcolor[2]=footcolor[2];

				if(!initialized){

				//vip

				topcolor[0]=(float)235/255;
				topcolor[1]=(float)235/255;
				topcolor[2]=(float)235/255;

				bottomcolor[0]=(float)200/255;
				bottomcolor[1]=(float)200/255;
				bottomcolor[2]=(float)200/255;

				footcolor[0]=(float)119/255;
				footcolor[1]=(float)68/255;
				footcolor[2]=(float)18/255;

				headcolor[0]=(float)240/255;
				headcolor[1]=(float)183/255;
				headcolor[2]=(float)132/255;

				footcolor[0]=(float)119/255;
				footcolor[1]=(float)68/255;
				footcolor[2]=(float)18/255;

				handcolor[0]=(float)240/255;
				handcolor[1]=(float)183/255;
				handcolor[2]=(float)132/255;

				costume[VIPCOSTUME] = new Costume();
				costume[VIPCOSTUME].headcolor[0]=headcolor[0];
				costume[VIPCOSTUME].headcolor[1]=headcolor[1];
				costume[VIPCOSTUME].headcolor[2]=headcolor[2];

				costume[VIPCOSTUME].handcolor[0]=handcolor[0];
				costume[VIPCOSTUME].handcolor[1]=handcolor[1];
				costume[VIPCOSTUME].handcolor[2]=handcolor[2];

				costume[VIPCOSTUME].chestcolor[0]=topcolor[0];
				costume[VIPCOSTUME].chestcolor[1]=topcolor[1];
				costume[VIPCOSTUME].chestcolor[2]=topcolor[2];

				costume[VIPCOSTUME].abdomencolor[0]=topcolor[0];
				costume[VIPCOSTUME].abdomencolor[1]=topcolor[1];
				costume[VIPCOSTUME].abdomencolor[2]=topcolor[2];

				costume[VIPCOSTUME].upperarmcolor[0]=topcolor[0];
				costume[VIPCOSTUME].upperarmcolor[1]=topcolor[1];
				costume[VIPCOSTUME].upperarmcolor[2]=topcolor[2];

				costume[VIPCOSTUME].lowerarmcolor[0]=topcolor[0];
				costume[VIPCOSTUME].lowerarmcolor[1]=topcolor[1];
				costume[VIPCOSTUME].lowerarmcolor[2]=topcolor[2];

				costume[VIPCOSTUME].upperlegcolor[0]=bottomcolor[0];
				costume[VIPCOSTUME].upperlegcolor[1]=bottomcolor[1];
				costume[VIPCOSTUME].upperlegcolor[2]=bottomcolor[2];

				costume[VIPCOSTUME].lowerlegcolor[0]=bottomcolor[0];
				costume[VIPCOSTUME].lowerlegcolor[1]=bottomcolor[1];
				costume[VIPCOSTUME].lowerlegcolor[2]=bottomcolor[2];

				costume[VIPCOSTUME].footcolor[0]=footcolor[0];
				costume[VIPCOSTUME].footcolor[1]=footcolor[1];
				costume[VIPCOSTUME].footcolor[2]=footcolor[2];

				//Bodyguard

				topcolor[0]=(float)50/255;
				topcolor[1]=(float)50/255;
				topcolor[2]=(float)50/255;

				bottomcolor[0]=(float)30/255;
				bottomcolor[1]=(float)30/255;
				bottomcolor[2]=(float)30/255;

				footcolor[0]=(float)20/255;
				footcolor[1]=(float)20/255;
				footcolor[2]=(float)20/255;

				costume[BODYGUARDCOSTUME] = new Costume();
				costume[BODYGUARDCOSTUME].headcolor[0]=headcolor[0];
				costume[BODYGUARDCOSTUME].headcolor[1]=headcolor[1];
				costume[BODYGUARDCOSTUME].headcolor[2]=headcolor[2];

				costume[BODYGUARDCOSTUME].handcolor[0]=handcolor[0];
				costume[BODYGUARDCOSTUME].handcolor[1]=handcolor[1];
				costume[BODYGUARDCOSTUME].handcolor[2]=handcolor[2];

				costume[BODYGUARDCOSTUME].chestcolor[0]=topcolor[0];
				costume[BODYGUARDCOSTUME].chestcolor[1]=topcolor[1];
				costume[BODYGUARDCOSTUME].chestcolor[2]=topcolor[2];

				costume[BODYGUARDCOSTUME].abdomencolor[0]=topcolor[0];
				costume[BODYGUARDCOSTUME].abdomencolor[1]=topcolor[1];
				costume[BODYGUARDCOSTUME].abdomencolor[2]=topcolor[2];

				costume[BODYGUARDCOSTUME].upperarmcolor[0]=topcolor[0];
				costume[BODYGUARDCOSTUME].upperarmcolor[1]=topcolor[1];
				costume[BODYGUARDCOSTUME].upperarmcolor[2]=topcolor[2];

				costume[BODYGUARDCOSTUME].lowerarmcolor[0]=topcolor[0];
				costume[BODYGUARDCOSTUME].lowerarmcolor[1]=topcolor[1];
				costume[BODYGUARDCOSTUME].lowerarmcolor[2]=topcolor[2];

				costume[BODYGUARDCOSTUME].upperlegcolor[0]=bottomcolor[0];
				costume[BODYGUARDCOSTUME].upperlegcolor[1]=bottomcolor[1];
				costume[BODYGUARDCOSTUME].upperlegcolor[2]=bottomcolor[2];

				costume[BODYGUARDCOSTUME].lowerlegcolor[0]=bottomcolor[0];
				costume[BODYGUARDCOSTUME].lowerlegcolor[1]=bottomcolor[1];
				costume[BODYGUARDCOSTUME].lowerlegcolor[2]=bottomcolor[2];

				costume[BODYGUARDCOSTUME].footcolor[0]=footcolor[0];
				costume[BODYGUARDCOSTUME].footcolor[1]=footcolor[1];
				costume[BODYGUARDCOSTUME].footcolor[2]=footcolor[2];

				//Load animations

				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				
				testskeleton = new Skeleton();
				animation[IDLEANIM] = new Animation();
				animation[JOGANIM] = new Animation();
				animation[PISTOLAIMANIM] = new Animation();
				testskeleton.load("Data/Skeleton/Basic Figure");
				animation[IDLEANIM].load("Data/Animations/Breathe");
				animation[JOGANIM].load("Data/Animations/Run");
				animation[PISTOLAIMANIM].load("Data/Animations/PistolAim");
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				animation[WALKANIM] = new Animation();
				animation[RIFLEHOLDANIM] = new Animation();
				animation[RIFLEAIMANIM] = new Animation();
				animation[ASSAULTRIFLEAIMANIM] = new Animation();
				animation[CROUCHANIM] = new Animation();
				animation[WALKANIM].load("Data/Animations/Walk");
				animation[RIFLEHOLDANIM].load("Data/Animations/Riflehold");
				animation[RIFLEAIMANIM].load("Data/Animations/Rifleaim");
				animation[ASSAULTRIFLEAIMANIM].load("Data/Animations/Assaultrifleaim");
				animation[CROUCHANIM].load("Data/Animations/Crouch");
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				animation[HEADPAINANIM] = new Animation();
				animation[CHESTPAINANIM] =new Animation();
				animation[STOMACHPAINANIM] = new Animation();
				animation[RIGHTARMPAINANIM] = new Animation();
				animation[LEFTARMPAINANIM] = new Animation();
				animation[HEADPAINANIM].load("Data/Animations/Headshot");
				animation[CHESTPAINANIM].load("Data/Animations/Chestshot");
				animation[STOMACHPAINANIM].load("Data/Animations/Stomachshot");
				animation[RIGHTARMPAINANIM].load("Data/Animations/Rightarmshot");
				animation[LEFTARMPAINANIM].load("Data/Animations/Leftarmshot");
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				animation[RIGHTLEGPAINANIM] = new Animation();
				animation[LEFTLEGPAINANIM] = new Animation();
				animation[RIFLEHITANIM] = new Animation();
				animation[GRENADEAIMANIM] = new Animation();
				animation[GRENADECHARGEANIM] = new Animation();
				animation[RIGHTLEGPAINANIM].load("Data/Animations/Rightlegshot");
				animation[LEFTLEGPAINANIM].load("Data/Animations/Leftlegshot");
				animation[RIFLEHITANIM].load("Data/Animations/Riflehit");
				animation[GRENADEAIMANIM].load("Data/Animations/Grenadeaim");
				animation[GRENADECHARGEANIM].load("Data/Animations/grenadecharge");
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				animation[GRENADETHROWANIM] = new Animation();
				animation[ZOMBIEEATANIM] = new Animation();
				animation[ZOMBIEJOGANIM] = new Animation();
				animation[ZOMBIEWALKANIM] = new Animation();
				animation[GRENADETHROWANIM].load("Data/Animations/Grenadethrow");
				animation[ZOMBIEEATANIM].load("Data/Animations/Zombiemunch");
				animation[ZOMBIEJOGANIM].load("Data/Animations/ZombieRun");
				animation[ZOMBIEWALKANIM].load("Data/Animations/Zombiewalk");
				loadingscreenamount+=5;

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				animation[GETUPFRONTANIM] = new Animation();
				animation[GETUPBACKANIM] = new Animation();
				animation[DIVEANIM] = new Animation();
				animation[THROWANIM] = new Animation();
				animation[THROWNANIM] = new Animation();
				animation[GETUPFRONTANIM].load("Data/Animations/Getupfromfront");
				animation[GETUPBACKANIM].load("Data/Animations/Getupfromback",180);
				animation[DIVEANIM].load("Data/Animations/Dive");
				animation[THROWANIM].load("Data/Animations/Aikidothrow");
				animation[THROWNANIM].load("Data/Animations/Aikidothrown");

			}

			

			//Setup people

			for(int i=0;i<MAX_PEOPLE;i++){
				if(i==0){
					person[i].whichcostume=BODYGUARDCOSTUME;
				}		

				if(i>1){
					if(person[i] == null) person[i] = new Person();
					person[i].whichcostume=CASUALCOSTUMES+abs(random())%NUMCASUAL;
				}	

				//person[i].firstlongdead=0;

				person[i].dead=false;
				person[i].health=100;
				if(person[i].skeleton == null)person[i].skeleton = new Skeleton();
				person[i].skeleton.free=false;
				person[i].ammo=0;
				if (person[i].velocity == null)person[i].velocity = new XYZ();
				else person[i].velocity.set(0);

				//Load skeleton structure

				if(!initialized)person[i].skeleton.load("Data/Skeleton/Basic Figure");

				if(i%5==0){
					loadingscreenamount+=5;
					if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				}
			}			

			loadingscreenamount+=5;

			if(!initialized)loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

			if(initialized)person[0].skeleton.load("Data/Skeleton/Basic Figure");

			person[0].attackframe=-1;
			spawndelay=0;

			if(fog == null)fog = new Fog();
			fog.setFog(fogcolorr,fogcolorg,fogcolorb,0,viewdistance*.8f,.1f);

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	

			//light

			float LightAmbient[]=		{ .3f, .3f, .3f, 1.0f};
			float LightDiffuse[]=		{ 1, 1, 1, 1.0f };

			glLightfv(GL_LIGHT0, GL_AMBIENT, LightAmbient);		
			glLightfv(GL_LIGHT0, GL_DIFFUSE, LightDiffuse);		
			glEnable(GL_LIGHT0);	

			loadingscreenamount+=5;

			//Load some textures

			if(!initialized){

				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);

				personspritetextureptr = loadPersonSpriteTexture("Data/Textures/Personsprite.tga");
				deadpersonspritetextureptr = loadPersonSpriteTexture("Data/Textures/Deadpersonsprite.tga");
				scopetextureptr = loadPersonSpriteTexture("Data/Textures/Scope.tga");
				flaretextureptr = loadPersonSpriteTexture("Data/Textures/Flare.tga");
				
				sprites = new Sprites();
				decals = new Decals();

				sprites.loadFlareTexture("Data/Textures/HitFlash.tga");
				sprites.loadMuzzleFlareTexture("Data/Textures/MuzzleFlash.tga");
				sprites.loadSmokeTexture("Data/Textures/Smoke.tga");
				sprites.loadBloodTexture("Data/Textures/Blood.tga");
				sprites.loadRainTexture("Data/Textures/rain.tga");
				sprites.loadSnowTexture("Data/Textures/snow.tga");
				decals.loadBulletHoleTexture("Data/Textures/BulletHole.tga");
				decals.loadCraterTexture("Data/Textures/Crater.tga");
				decals.loadBloodTexture("Data/Textures/Blood/Blood1.tga",0);
				decals.loadBloodTexture("Data/Textures/Blood/Blood2.tga",1);
				decals.loadBloodTexture("Data/Textures/Blood/Blood3.tga",2);
				decals.loadBloodTexture("Data/Textures/Blood/Blood4.tga",3);
				decals.loadBloodTexture("Data/Textures/Blood/Blood5.tga",4);
				decals.loadBloodTexture("Data/Textures/Blood/Blood6.tga",5);
				decals.loadBloodTexture("Data/Textures/Blood/Blood7.tga",6);
				decals.loadBloodTexture("Data/Textures/Blood/Blood8.tga",7);
				decals.loadBloodTexture("Data/Textures/Blood/Blood9.tga",8);
				decals.loadBloodTexture("Data/Textures/Blood/Blood10.tga",9);
				decals.loadBloodTexture("Data/Textures/Blood/Blood11.tga",10);
			}

			//Setup clip plane equation

			eqn[0]=0;
			eqn[1]=1;
			eqn[2]=0;
			eqn[3]=0;

			glClearColor(fogcolorr,fogcolorg,fogcolorb,1);

			//Draw city one frame to fix evil menu bug

			if(!initialized)mainmenu=2;

			if(!initialized){
				loadingScreen(loadingscreenamount/loadingscreenamounttotal*100);
				flashamount=1;
				flashr=1;flashg=1;flashb=1;
				alSourcePlay(gSourceID[SOULINSOUND]);
			}

			initialized=true;

			loadingscreenamount+=5;

			//Sync to refresh rate

			if(vblsync){	
				int swapInt = 1;
			}

			/*

			for(int i=0;i<sprites.howmanysprites;i++){
				sprites.DeleteSprite(0);
			}

			for(int i=0;i<decals.howmanydecals;i++){
				decals.DeleteDecal(0);
			}*/

			decals.howmanydecals=0;
			sprites.howmanysprites=0;

			losedelay=1;
			
			oldscore=score;

		}
		
		/*********************> initGL() <
		 * @throws IOException 
		 * @throws FileNotFoundException *****/

		public static int initGL() throws FileNotFoundException, IOException {

			//Config

			if(!initialized){

				//Default config in case config is not found	
				
				// STUB_FUNCTION;
				
				screenwidth = 800;
				screenheight = 600;
				usermousesensitivity=.7f;
				debug=false;
				vblsync=true;
				blood = true;
				blurness = false;
				mainmenuness=true;
			    customlevels=false;
			    musictoggle=true;
			    fullscreen=false;

				//If no config, write one
			    
			    File file = new File("config.txt");
		        if (file.createNewFile()) {
		        	PrintStream fileStream = new PrintStream(file);
				      
			    	fileStream.println( "Screenwidth:");
			    	fileStream.println( screenwidth);
			    	fileStream.println( "Screenheight:");
			    	fileStream.println( screenheight);
			    	fileStream.println( "Mouse sensitivity:");
			    	fileStream.println( usermousesensitivity);
			    	fileStream.println( "Show fps and other info:");
			    	fileStream.println( debug ? 1:0);
			    	fileStream.println( "VBL sync:");
			    	fileStream.println( vblsync ? 1:0);
			    	fileStream.println( "Blood:");
			    	fileStream.println( blood ? 1:0);
			    	fileStream.println( "Blur:");
			    	fileStream.println( blurness ? 1:0);
			    	fileStream.println( "Main Menu:");
			    	fileStream.println( mainmenuness ? 1:0);
			    	fileStream.println( "Custom levels:");
			    	fileStream.println( customlevels ? 1:0);
			    	fileStream.println( "Music:");
			    	fileStream.println( musictoggle ? 1:0);
			    	fileStream.println( "azerty keyboard:");
			    	fileStream.println(azertykeyboard ? 1 : 0);
			    	fileStream.println( "Fullscreen:");
			    	fileStream.print( fullscreen ? 1 : 0);
			    	
			    	fileStream.close();
			    	
		        } else {
		        	
		            Scanner myReader = new Scanner(file);
		            myReader.useLocale(Locale.US);
		            
		            while (myReader.hasNextLine()) {		            	
		          
		            	switch(myReader.nextLine()) {
		            	
			            	case "Screenwidth:":
			            		screenwidth = myReader.nextInt(); break;	
			            		
			            	case "Screenheight:":
			            		screenheight = myReader.nextInt(); break;
			            		
			            	case "Mouse sensitivity:":
			            		usermousesensitivity = myReader.nextFloat(); break;
			            		
			            	case "Show fps and other info:":
			            		debug = myReader.nextInt()==1; break;
			            	
			            	case "VBL sync:":
			            		vblsync = myReader.nextInt()==1; break;
			            		
			            	case "Blood:":
			            		blood = myReader.nextInt()==1; break;
			            	
			            	case "Blur:":
			            		blurness = myReader.nextInt()==1; break;
			            	
			            	case "Main Menu:":
			            		mainmenuness = myReader.nextInt()==1; break;
			            	
			            	case "Custom levels:":
			            		customlevels = myReader.nextInt()==1; break;
			            		
			            	case "Music:":
			            		musictoggle = myReader.nextInt()==1; break;
			            		
			            	case "azerty keyboard:":
			            		azertykeyboard = myReader.nextInt()==1; break;
			            	
			            	case "Fullscreen:":
			            		fullscreen = myReader.nextInt()==1; break;
		            	}
		            }
		        	
		            myReader.close();
		        }

				//Read high score
		        
		        file = new File("Highscore");

				/* TODO */
				
				if(file.createNewFile()) {
					highscore=0;
			        beatgame=false;
			        
			        PrintStream fileStream = new PrintStream(file);
			        
			        fileStream.println(highscore);
			        fileStream.println(beatgame ? 1 : 0);
			        
			        fileStream.close();
				}
				else {
					
			        Scanner myReader = new Scanner(file);

		            myReader.useLocale(Locale.US);

					highscore = myReader.nextInt();
					beatgame = myReader.nextInt()==1;
					
					myReader.close();
				}
				
				sps=40;
				maxfps=90;

				disttest=true;
				cubetest=true;
			}

			//Setup screen
			
			GLFWErrorCallback.createPrint(System.err).set();
			
			// Initialize GLFW.
			if ( !glfwInit() )
				throw new IllegalStateException("Unable to initialize GLFW");
			
			// Configure GLFW
			glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); 
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable
			glfwWindowHint(GLFW_RED_BITS, 8);
			glfwWindowHint(GLFW_GREEN_BITS, 8);
			glfwWindowHint(GLFW_BLUE_BITS, 8);
			glfwWindowHint(GLFW_DEPTH_BITS, 16);
			glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
		        
			if(screenwidth<640||screenheight<480) {
				glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
				window = glfwCreateWindow(640, 480, "Black Shades", NULL, NULL);	
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
				
			} else if(fullscreen) {
				GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
				screenwidth = mode.width();
				screenheight = mode.height();
				window = glfwCreateWindow(screenwidth, screenheight, "Black Shades", glfwGetPrimaryMonitor(), NULL);
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			}
			else {
				glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
				window = glfwCreateWindow(screenwidth, screenheight, "Black Shades", NULL, NULL);
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

			}
			
			if ( window == NULL )
				throw new RuntimeException("Failed to create the GLFW window");
			
			// Make the OpenGL context current
			glfwMakeContextCurrent(window);
						
			// Enable v-sync
			glfwSwapInterval(vblsync ? 1:0);
			
			// Sets window icon, maybe GLFW fixes this in Wayland in future releases
			loadIcon(window);
			
			// Make the window visible
			glfwShowWindow(window);
			
			GL.createCapabilities();

			text = new Text();
			text.LoadFontTexture("Data/Textures/Font.tga");
			text.BuildFont();

			glAlphaFunc(GL_GREATER, 0.01f);
			glDepthFunc(GL_LESS);

			glPolygonOffset(-8,0);
			glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );

			return 1;

		}
		
		//***************> Dispose() <******/

		public static void dispose(){
						
			//Delete sound sources
			
			alDeleteSources(gSourceID);
			
			// Unloads the OpenGL native library.
			destroy();
			
			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
		
		//***************> ResizeGLScene() <******/

		public static void reSizeGLScene(float fov, float near) {

			if (screenheight==0)										
			{
				screenheight=1;									
			}

			glViewport(0,0,screenwidth,screenheight);						

			glMatrixMode(GL_PROJECTION);						
			glLoadIdentity();								

			gluPerspective(fov,(float)screenwidth/(float)screenheight,near,viewdistance);

			glMatrixMode(GL_MODELVIEW);							
			glLoadIdentity();						
		}
		
		public static void gluPerspective(double fovy, double aspect, double zNear, double zFar) {
			
			float fH = (float) (tan(fovy / 360 * PI) * zNear);
			float fW = (float) (fH * aspect);
			glFrustum(-fW, fW, -fH, fH, zNear, zFar );
		    
		}	
		

		public static int loadPersonSpriteTexture(String fileName) throws IOException {
			int textureid = loadTexture(fileName);
			glBindTexture(GL_TEXTURE_2D, textureid);
			glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			return textureid;
		}
	}
}
