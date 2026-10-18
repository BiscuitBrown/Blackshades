package blackshades;

import java.io.FileNotFoundException;
import java.io.IOException;

import static blackshades.GameInitDispose.Game.*;
import static blackshades.GameLoop.Game.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			initGL();
			initGame();
			eventLoop();
			dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
