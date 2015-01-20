package game.ai.impl;



import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common;
import common.Common.Stone;
import common.Pos;




public class HiranoTest  extends AiBase {
	private static int OceroBordColumnNum = Common.Y_MAX_LEN - 1;
/*
	private class PosList extends ArrayList<Pos>{
	}
*/
	private ArrayList<ArrayList<Pos>> PriorityLevel;



	public HiranoTest(Stone MyColor) {
			super(MyColor);
			InitilizeList();
	}

	private void InitilizeList(){
		PriorityLevel = new  ArrayList<ArrayList<Pos>> ();

		/*四隅をリストに追加*/
		ArrayList<Pos> NewPosList = new ArrayList<Pos>() ;
		NewPosList.add(new Pos(0, 0));
		NewPosList.add(new Pos(0, OceroBordColumnNum));
		NewPosList.add(new Pos(OceroBordColumnNum,0));
		NewPosList.add(new Pos(OceroBordColumnNum,OceroBordColumnNum));
		PriorityLevel.add(NewPosList);

		/* 最終ラインの四隅＋以外を追加 */
		ArrayList<Pos> NewPosList2 = new ArrayList<Pos>() ;
		for (int i = 2; i < OceroBordColumnNum - 1 ; i++){
			NewPosList2.add(new Pos(0, i));
			NewPosList2.add(new Pos(OceroBordColumnNum, i));
			NewPosList2.add(new Pos(i, 0));
			NewPosList2.add(new Pos(i, OceroBordColumnNum));
		}
		PriorityLevel.add(NewPosList2);

		/* 以下、奇数ラインをリストに追加 */
		if ( 2 < OceroBordColumnNum){
			ListAdd(1,OceroBordColumnNum-1,false);
		}
	}

	private void ListAdd(int StartNum ,int EndNum ,boolean Flg){
			if (StartNum + 2 >= EndNum){
					return;
			}

			if (Flg == true){
				/*ラインをリストに追加*/
				ArrayList<Pos> NewPosList = new ArrayList<Pos>() ;
				NewPosList.add(new Pos(StartNum, StartNum));
				NewPosList.add(new Pos(StartNum, EndNum));
				NewPosList.add(new Pos(EndNum,StartNum));
				NewPosList.add(new Pos(EndNum,EndNum));
				for (int i = StartNum+1; i < EndNum; i++){
					NewPosList.add(new Pos(StartNum, i));
					NewPosList.add(new Pos(EndNum, i));
					NewPosList.add(new Pos(i, StartNum));
					NewPosList.add(new Pos(i, EndNum));
				}
				PriorityLevel.add(NewPosList);
			}
			StartNum += 1;
			EndNum -= 1 ;
			ListAdd(StartNum ,EndNum, ! Flg);
	}


	@Override
	public Pos WhereSet(Bord bord,Pos clickPos) {
		Pos retPos = null;
		ArrayList<Pos> myHands = bord.getCanSetList(getMyColor());

		if (myHands.size() > 0) {
			for (ArrayList<Pos> PriorityList : PriorityLevel){
				for (Pos HandsPos : myHands){
					if (PriorityList.contains(HandsPos)==true){
						/* 置いた結果、相手が四隅に置けるかチェック */
						if(doSet(bord,HandsPos,getMyColor())==true){
							return HandsPos;
						}
					}

				}
			}
			/* どこもヒットしなかったら。とりあえず最大数とれるところにおいてみよう・・・ */
			retPos = bord.SearchMaxPos(getMyColor());
		}

		return retPos;
	}

	/*
	 * 指定Posに置くべきか判別・・・とりあえず相手に四隅置かれないようにしてみよう
	 * */
	public boolean doSet(Bord bord, Pos setPos, Stone color) {
		// 仮想ボードを作成する
		Bord vrBord = bord.clone();

		// 仮想ボードに置いてみる
		vrBord.DoSet(setPos, color, true);

		// 相手の手で四隅に置けるかチェック
		ArrayList<Pos> enHands = vrBord.getCanSetList(Stone.reverseStone(color));

		// NULLをセットして少しでも軽く
		vrBord = null;

		for (Pos pos : enHands){
			if ( PriorityLevel.get(0).contains(pos)==true){
				return false;
			}
		}

		return true;
	}


}
