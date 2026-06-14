package rs.bg.goran;

import rs.bg.goran.engineTester.GameEngine;

public class GameMain {

    public static void main(String[] args) {
	Thread tread = new Thread(GameEngine.of());
	tread.start();
    }
}
