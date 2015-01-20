package game.ai.impl;

/**
 * インポート情報
 */
import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common.Stone;
import common.Pos;

public class SN_Ai extends AiBase {
	/**
	 * コンストラクタ
	 * @param MyColor
	 */
	public SN_Ai(Stone MyColor) {
		super(MyColor);

	}

	/* 定数 */
	public enum TypeEnum {
	    MAX(1),
	    MIN(2);

	    private final int type;

	    private TypeEnum(final int type) {
	        this.type = type;
	    }

	    public int getType() {
	        return type;
	    }

	}

	@Override
	public Pos WhereSet(Bord bord, Pos pos) {
		Pos rPos = null;

		/* 四隅の設置場所 */
		Pos TopRight = new Pos(0,0);
		Pos TopLeft = new Pos(0,7);
		Pos ButtomRight = new Pos(7,0);
		Pos ButtomLeft = new Pos(7,7);

		/* 全設置可能リストを取得 */
		ArrayList<Pos> lists = bord.getCanSetList(getMyColor());
		/* 四隅設置可能リスト */
		ArrayList<Pos> corners = new ArrayList<Pos>();
		/* 設置可能リスト */
		ArrayList<Pos> setLists = new ArrayList<Pos>();

		if (lists.size() > 0) {
			for (int i = 0, n = lists.size(); i < n; i++) {
				if (
						(lists.get(i) == TopRight) ||
						(lists.get(i) == TopLeft) ||
						(lists.get(i) == ButtomRight) ||
						(lists.get(i) == ButtomLeft)
					)
				{
					/* 四隅が設置可能の場合、四隅設置可能リストへ保存 */
					corners.add(lists.get(i));

				}

				else {
					/* 設置可能リストへ保存 */
					setLists.add(lists.get(i));

				}

			}
			/* 四隅設置可能リストにレコードがあれば設置場所を返す */
			if (corners.size() > 0) {
				rPos = SearchPos(bord, corners, TypeEnum.MAX.getType());

			} else if (setLists.size() > 0) {
				/* 辺設置可能リスト */
				ArrayList<Pos> sideLists = new ArrayList<Pos>();
				for (int i = 0, n = setLists.size(); i < n; i++) {
					if (
							(setLists.get(i).getX() == 0) ||
							(setLists.get(i).getY() == 0)
						)
					{
						sideLists.add(setLists.get(i));

					}

				}
				/* 辺設置可能リストにレコードがあれば設置場所を返す */
				if (sideLists.size() > 0) {
					/* 辺が設置可能な場合、石が一番多く取れる座標を返す */
					rPos = SearchPos(bord, sideLists, TypeEnum.MAX.getType());
				} else if ( bord.GetCount(Stone.NONE) >= 35) {
					/* 辺が設置不可能な場合、石が一番少なく取れる座標を返す */
					rPos = SearchPos(bord, setLists, TypeEnum.MIN.getType());
				} else {
					/* 辺が設置不可能で終盤の場合、石が一番多く取れる座標を返す */
					rPos = SearchPos(bord, setLists, TypeEnum.MAX.getType());
				}

			}

		}
		return rPos;
	}

	/**
	 * リストからコマを一番多く取れる、もしくは一番少なく取れる座標を返す
	 * @param lists
	 * @param type
	 * @return
	 */
	private Pos SearchPos(Bord bord, ArrayList<Pos> lists, int type) {
		Pos rPos = null;
		Bord myBord = bord.clone();

		/* コマを一番多く取れる座標を取得 */
		if (type == TypeEnum.MAX.getType()) {
			int max = 0;
			Pos maxPos = null;
			for (int i = 0, n = lists.size(); i < n; i++) {
				if ( max <= myBord.DoSet(lists.get(i), getMyColor(), false)) {
					max = myBord.DoSet(lists.get(i), getMyColor(), false);
					maxPos = lists.get(i);
				}

			}
			rPos = maxPos;
		}
		/* コマを一番少なく取れる座標を取得 */
		else if (type == TypeEnum.MIN.getType()) {
			int min = 99;
			Pos minPos = null;
			for (int i = 0, n = lists.size(); i < n; i++) {
				if ( min >= myBord.DoSet(lists.get(i), getMyColor(), false)) {
					min = myBord.DoSet(lists.get(i), getMyColor(), false);
					minPos = lists.get(i);
				}

			}
			rPos = minPos;
		}
		return rPos;
	}

}

