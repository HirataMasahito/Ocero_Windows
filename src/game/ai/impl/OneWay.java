package game.ai.impl;

import common.Common.Stone;
import common.Common;
import common.Pos;

import game.ai.AiBase;
import game.othello.Bord;

/**
 * AIサンプル
 * 確か左上から右下に掛けての線形探索
 *
 */
public class OneWay extends AiBase {

	public OneWay(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord,Pos clickPos) {
		Pos retPos = new Pos();
		//Y軸のループ 上から下へ
		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			retPos.setY(y);
			// X軸のループ 左から右へ
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				retPos.setX(x);
				//置けるか判断して、置けるならその座標を返す。
				if (bord.CanSet(retPos, getMyColor())) {
					return retPos;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "OneWay";
	}
}
