package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import common.Common.Stone;
import common.Point;
import common.Pos;

/**
 * 自分の状態を元に、再帰処理で次の位置を決める
 *
 */
public class SteadyR extends AiBase {
	/** 優先度高の座標リスト */
	private ArrayList<Pos> HighWeightList;

	/** 自分の状態 超優勢 */
	private static int MY_STATUS_VERY_GOOD = 4;
	/** 自分の状態 優勢 */
	private static int MY_STATUS_GOOD = 2;
	/** 自分の状態 対等 */
	private static int MY_STATUS_EQUAL = 0;
	/** 自分の状態 劣勢 */
	private static int My_STATUS_BAD = -3;
	/** 再帰処理の最大回数*/
	private static int REFLEX_CNT = 4;


	public SteadyR(Stone MyColor) {
		super(MyColor);

		HighWeightList = new ArrayList<Pos>();
		HighWeightList.add(new Pos(0, 0));
		HighWeightList.add(new Pos(0, 7));
		HighWeightList.add(new Pos(7, 0));
		HighWeightList.add(new Pos(7, 7));
	}

	@Override
	public Pos WhereSet(Bord bord,Pos clickPos) {
		Pos retPos = null;

		ArrayList<Pos> myHands = bord.getCanSetList(getMyColor());
		if (myHands.size() > 0) {

			// 四隅だけは優先して置くように
			if (HighWeightList.containsAll(myHands)) {
				for (Pos pos : HighWeightList) {
					if (bord.CanSet(pos, getMyColor())) {
						retPos = pos;
						break;
					}
				}
			} else {
				// 四隅に置けないなら、堅実にして置くように
				ArrayList<Point> valueList = new ArrayList<Point>();
				for (Pos pos : myHands) {
					int value = getValueByStatus(bord, pos, getMyColor(), REFLEX_CNT);
					valueList.add(new Point(value, pos));
					//System.out.println(pos.getX() + ":" + pos.getY() + "=" + value);
				}

				// リストを降順でソートする
				Collections.sort(valueList, new PointComparator());
				// ソート順の最高の値を取得
				retPos = valueList.get(0).getPos();

			}

		}

		return retPos;
	}

	/**
	 * 再帰処理で戦況を元に得点を取得する
	 *
	 * @param bord 盤情報
	 * @param setPos 設置座標
	 * @param color 設置する石
	 * @param cnt ループカウンタ
	 * @return その座標に置いたときの評価得点
	 */
	public int getValueByStatus(Bord bord, Pos setPos, Stone color, int cnt) {
		int retValue = 0;

		// 仮想ボードを作成する
		Bord vrBord = bord.clone();
		vrBord.DoSet(setPos, color, true);
		if (cnt < 0) {
			// 自分と相手の数から状態を取得する
			int myHand = vrBord.GetCount(getMyColor());
			int enHand = vrBord.GetCount(Stone.reverseStone(getMyColor()));

			int nawStatus = myHand - enHand;
			// NULLをセットして少しでも軽く
			vrBord = null;
			if (nawStatus > 0) {
				if (myHand > enHand * 1.5) {
					return MY_STATUS_VERY_GOOD;
				} else {
					return MY_STATUS_GOOD;
				}
			} else if (nawStatus < 0) {
				return My_STATUS_BAD;
			} else {
				return MY_STATUS_EQUAL;
			}

		} else {
			// 色を逆転して、次に相手が置ける位置を選択する
			ArrayList<Pos> nextHandList = vrBord.getCanSetList(Stone.reverseStone(color));
			for (Pos pos : nextHandList) {
				int nextCnt = cnt - 1;
				retValue += getValueByStatus(vrBord, pos, Stone.reverseStone(color), nextCnt);
			}

		}

		return retValue;
	}


	class PointComparator implements Comparator<Point> {

		// 比較メソッド（データクラスを比較して-1, 0, 1を返すように記述する）
		public int compare(Point a, Point b) {
			int no1 = a.getValue();
			int no2 = b.getValue();

			// 降順でソートされる
			if (no1 > no2) {
				return -1;

			} else if (no1 == no2) {
				return 0;

			} else {
				return 1;

			}
		}

	}

	@Override
	public String toString() {
		return "SteadyR(遅いので注意)";
	}
}
