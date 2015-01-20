package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common.Stone;
import common.Pos;

public class MH_Spart extends AiBase {
	/** スパートをかけるタイミング */
	private static int SPART_TIMING = 11;
	/** スパート時の合格閾値 */
	private static int SPART_THRESHOLD = 33;

	/** 優先度高の座標リスト */
	private ArrayList<Pos> HighWeightList;
	/** 優先度低の座標リスト */
	private ArrayList<Pos> LowWeightList;

	public MH_Spart(Stone MyColor) {
		super(MyColor);

		HighWeightList = new ArrayList<Pos>();
		HighWeightList.add(new Pos(0, 0));
		HighWeightList.add(new Pos(0, 7));
		HighWeightList.add(new Pos(7, 0));
		HighWeightList.add(new Pos(7, 7));

		LowWeightList = new ArrayList<Pos>();
		LowWeightList.add(new Pos(0, 1));
		LowWeightList.add(new Pos(1, 0));
		LowWeightList.add(new Pos(1, 1));

		LowWeightList.add(new Pos(6, 0));
		LowWeightList.add(new Pos(6, 1));
		LowWeightList.add(new Pos(7, 1));

		LowWeightList.add(new Pos(0, 6));
		LowWeightList.add(new Pos(1, 6));
		LowWeightList.add(new Pos(1, 7));

		LowWeightList.add(new Pos(6, 6));
		LowWeightList.add(new Pos(6, 7));
		LowWeightList.add(new Pos(7, 6));

	}

	@Override
	public Pos WhereSet(Bord bord, Pos clickPos) {

		Pos retPos = null;
		Bord vrBord = bord.clone();
		long start = System.currentTimeMillis();
		if (vrBord.GetCount(Stone.NONE) >= SPART_TIMING) {

			System.out.println("Normal");
			retPos = normalSearch(vrBord);
		} else {
			System.out.println("Spart");
			retPos = spartSearch(vrBord);
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		return retPos;
	}

	/**
	 * 通常時の探索
	 *
	 * 自分が置いた上で、
	 * 次に相手の置ける場所が少ない箇所を探します
	 *
	 * @param bord 盤情報
	 * @return 設置座標
	 */
	private Pos normalSearch(Bord bord) {
		Pos retPos = null;

		// 四隅だけは優先して置くように
		for (Pos pos : HighWeightList) {
			if (bord.CanSet(pos, getMyColor())) {
				return pos;
			}
		}

		Stone enStone = Stone.reverseStone(getMyColor());
		ArrayList<Pos> canList = bord.getCanSetList(getMyColor());
		int enCanPosCntMin = Integer.MAX_VALUE;
		for (Pos pos : canList) {

			if (LowWeightList.contains(pos)) {
				continue;
			}

			bord.DoSet(pos, getMyColor(), true);

			ArrayList<Pos> enCanList = bord.getCanSetList(enStone);
			if (enCanPosCntMin > enCanList.size()) {
				enCanPosCntMin = enCanList.size();
				retPos = pos;
			}
		}

		if (retPos == null) {
			// 優先度低いところへ
			for (Pos pos : LowWeightList) {
				if (bord.CanSet(pos, getMyColor())) {
					return pos;
				}
			}
		}

		return retPos;
	}

	/***
	 * スパート検索
	 * 現在の盤面から最後のいってまで読みきり、
	 * 自分が勝つ可能性の一番高い箇所を探します
	 *
	 * @param bord 盤情報
	 * @return 設置座標
	 */
	private Pos spartSearch(Bord bord) {
		Pos retPos = null;

		int winRouteCnt = Integer.MIN_VALUE;
		ArrayList<Pos> canList = bord.getCanSetList(getMyColor());
		for (Pos pos : canList) {
			int value = getValueByStatus(bord, pos, getMyColor());

			if (winRouteCnt < value) {
				winRouteCnt = value;
				retPos = pos;
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
	 * @return その座標に置いたときの評価得点
	 */
	public int getValueByStatus(Bord bord, Pos setPos, Stone color) {
		int retValue = 0;

		// 仮想ボードを作成する
		Bord vrBord = bord.clone();
		vrBord.DoSet(setPos, color, true);
		if (vrBord.GetCount(Stone.NONE) == 0) {

			// 自分と相手の数から状態を取得する
			int myHand = vrBord.GetCount(getMyColor());
			if (myHand >= SPART_THRESHOLD) {
				return 1;
			} else {
				return 0;
			}
		} else {
			// 色を逆転して、次に相手が置ける位置を選択する
			ArrayList<Pos> nextHandList = vrBord.getCanSetList(Stone.reverseStone(color));
			for (Pos pos : nextHandList) {
				retValue += getValueByStatus(vrBord, pos, Stone.reverseStone(color));
			}
		}
		return retValue;
	}

}