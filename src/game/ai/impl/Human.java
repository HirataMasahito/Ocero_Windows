package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import common.Common.Stone;
import common.Pos;


/**
 * 人の手で操作するためのクラス。
 * TODO 環境に応じて組み替える必要がある。
 *
 */
public class Human extends AiBase {

	public Human(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord,Pos clickPos) {

		//クリックした場所が設置可能ならその座標を返す
		while(! bord.CanSet(clickPos,getMyColor())){
			return null;
		}

		return clickPos;
	}

}
