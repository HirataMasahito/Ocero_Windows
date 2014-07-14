package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common.Stone;
import common.Point;
import common.Pos;

/***
 * サンプルAI
 * MaxStoneAiをちょっと改良
 * 自分→敵→自分と討ったとき一番よさそうなところを探す
 */
public class MaxStoneR extends AiBase {

	public MaxStoneR(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord,Pos clickPos) {
		Pos retPos = null;

		ArrayList<Point> handList = new ArrayList<Point>();

		// おけるところすべてを取得してループをまわす
		ArrayList<Pos> canSetList = bord.getCanSetList(getMyColor());
		for (Pos pos : canSetList) {
			Bord vrBord = bord.clone();

			// 一度石を置く
			vrBord.DoSet(pos, getMyColor(), true);

			// 相手が次に一番いいところに打つ
			Pos workPos = bord.SearchMaxPos(Stone.reverseStone(getMyColor()));
			// 相手がパスか判断
			if (workPos != null) {
				vrBord.DoSet(workPos, Stone.reverseStone(getMyColor()), true);
			}

			// 相手の最善手のあと、自分も一番いいところにおいてみる
			workPos = bord.SearchMaxPos(getMyColor());
			if (workPos != null) {
				// 自分がパスか判断
				vrBord.DoSet(workPos, getMyColor(), true);
			}

			// 自分と相手の石の差を取得する
			int getCnt = vrBord.GetCount(getMyColor()) - vrBord.GetCount(Stone.reverseStone(getMyColor()));

			handList.add(new Point(getCnt, new Pos(pos.getX(), pos.getY())));

		}

		//結果が一番いいところを探す
		int maxCnt = Integer.MIN_VALUE;
		for (Point valuePos : handList) {
			if (maxCnt < valuePos.getValue()) {
				maxCnt = valuePos.getValue();
				retPos = valuePos.getPos();
			}
		}

		return retPos;
	}


	@Override
	public String toString() {

		return "MaxStoneR";
	}
}
