package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common;
import common.Common.Stone;
import common.Pos;


/*
 *ゲーム序盤～中盤は出来るだけ1つだけ石を取るようにする。
 *終盤はガシガシ石を取る。
 *
 * */

public class Ikeda extends AiBase {

	//コンストラクタ
	public Ikeda(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord, Pos clickPos) {

		//石を置く座標
		Pos setPos = null;

		//盤上の全ての石の数を取得
		int allStones = bord.GetCount(Stone.WHITE) + bord.GetCount(Stone.BLACK);

		//盤上の石の数が42以下だったら（ゲーム序盤～中盤だったら）
		if(allStones < 42){
			//石を置けるところリストを取得する
			ArrayList<Pos> cansetList = bord.getCanSetList(getMyColor());

			//石を一つだけ取れる座標リスト
			ArrayList<Pos> getminimumStoneList = new ArrayList<Pos>();

			//置けるところリストのうち、取れる石の数が1の座標をリストを作成
			for(Pos pos:cansetList){
				if(bord.DoSet(pos, getMyColor(), false)==1){
					getminimumStoneList.add(pos);
				}
			}
			//取れる石の数が1の座標がなければ、最少の取得数の座標を取る
			if(getminimumStoneList.size() == 0){
				setPos = SearchMinPos(getMyColor(),bord);
			}else{
					setPos = getminimumStoneList.get(0);
			}
		}else{
			//ゲーム終盤
			setPos = bord.SearchMaxPos(getMyColor());
		}
		//座標を返す
		return setPos;
		}
	/**
	 * 石が一番少なく取れる場所を探す
	 * ※ 同点の場所が複数あると、最初の値を優先します
	 * @param color 置く石の色
	 * @return 一番取れない座標
	 */
	private Pos SearchMinPos( Stone color,Bord bord) {
		Pos retPos = null;
		Pos workPos = new Pos();

		int minCnt = 18;
		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);
				int getCnt = bord.DoSet(workPos, color, false);
				if(getCnt != 0){
					if (getCnt <minCnt) {
						minCnt = getCnt;
						retPos = new Pos(x, y);
					}
				}
			}
		}
		return retPos;
	}
}
