package game.ai;


import game.othello.Bord;

import common.Common.Stone;
import common.Pos;

/**
 * AIの共通スーパークラス
 * @author 本体
 *
 */
public abstract class AiBase {

	/** Aiの使用色 */
	private Stone MyColor;

	/**
	 * 引数憑きコンストラクタ
	 * @param MyColor AIの仕用色
	 */
	public AiBase(Stone MyColor){
		this.MyColor = MyColor;
	}

	/**
	 * 色の取得
	 * @return 使用色
	 */
	public Stone getMyColor(){
		return MyColor;
	}

	/**
	 * どこにおくかを取得します。
	 * @param bord 盤情報
	 * @param クリックした座標(HUMANクラス以外では使わない)
	 * @return 設置場所
	 */
	abstract public Pos WhereSet(Bord bord,Pos clickPos);

}
