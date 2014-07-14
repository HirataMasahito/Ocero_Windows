import game.ai.AiBase;
import game.ai.impl.Human;
import game.ai.impl.SteadyR;
import game.controller.GameController;

import common.Common.Stone;

public class TestMain {
	public static void main(String args[]) {
		AiBase player1 = new Human(Stone.BLACK);
		AiBase player2 = new SteadyR(Stone.WHITE);
		GameController gc = new GameController(player1, player2);
		gc.GameStart();

	}

}
