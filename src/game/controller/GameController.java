package game.controller;

import game.ai.AiBase;
import game.ai.impl.Human;
import game.othello.Bord;

import java.util.ArrayList;
import java.util.Observable;

import common.Common.Stone;
import common.Pos;

/**
 * ゲーム進行を管理する
 * ※環境に応じて、コントローラを改造する必要がある。
 * これはCUI版
 *
 */
public class GameController extends Observable {

	/** プレイヤーリスト */
	private ArrayList<AiBase> PlayerList;
	/**  */
	private Bord bord = new Bord();
	/***/
	private boolean passFlg = false;
	/***/
	private boolean gamesetFlg = false;
	/***/
	private int gameCnt = 0;

	/**
	 * コンストラクタ
	 */
	public GameController() {
		PlayerList = new ArrayList<AiBase>();
	}

	/**
	 * プレイヤー初期化コンストラクタ
	 *
	 * @param Player1
	 *            プレイヤー１のAIクラス
	 * @param Player2
	 *            プレイヤー２のAIクラス
	 */
	public GameController(AiBase Player1, AiBase Player2) {
		PlayerList = new ArrayList<AiBase>();
		PlayerList.add(Player1);
		PlayerList.add(Player2);
	}

	/**
	 * ゲームの開始
	 */
	public void GameStart() {

		bord.BordInit();
		setChanged();
		notifyObservers();
	}

	/**
	 * 現在の手版が、人か、AIか判断します。
	 *
	 * @return
	 */
	public boolean isNowHuman() {
		AiBase turnPlayer = PlayerList.get(gameCnt % 2);
		return (turnPlayer instanceof Human);
	}

	public Stone getNowColor() {
		AiBase turnPlayer = PlayerList.get(gameCnt % 2);
		return turnPlayer.getMyColor();
	}

	public boolean isGameSet() {
		return gamesetFlg;
	}

	public void NextTurn(Pos clickPos) {
		// 現在の手版を取得
		AiBase turnPlayer = PlayerList.get(gameCnt % 2);
		System.out.println(turnPlayer.getMyColor().getName() + "の番");

		// プレイヤーから石を置く位置を取得する。 AIが盤をいじらないようクローンを渡す
		Pos setHand = new Pos();
		setHand = turnPlayer.WhereSet(bord.clone(), clickPos);

		// 設置場所がNULLの場合パスとする
		if (setHand == null) {
			System.out.println(turnPlayer.getMyColor().getName() + "はパスします");

			// 両プレイヤーがパスをした場合、ゲーム終了とする
			if (passFlg) {
				System.out.println("どちらもパスしたため、ゲームを終了します。");
				gamesetFlg = true;
			}

			passFlg = true;
		} else {
			// 石を設置する
			bord.DoSet(setHand, turnPlayer.getMyColor(), true);
			passFlg = false;
		}
		gameCnt++;

		// ゲームの終了判定
		if (bord.GetCount(Stone.NONE) == 0) {
			gamesetFlg = true;
		}

		setChanged();
		notifyObservers();
		// 版情報を描画
		System.out.println(bord.toString());
	}

	/**
	 *
	 * @return
	 */
	public Bord getBord() {
		return bord.clone();
	}

	/**
	 * 試合の結果メッセージを返す
	 *
	 */
	public String getResultMessage() {
		StringBuffer sb = new StringBuffer();
		int player1Cnt = bord.GetCount(PlayerList.get(0).getMyColor());
		int player2Cnt = bord.GetCount(PlayerList.get(1).getMyColor());

		sb.append(player1Cnt);
		sb.append(":");
		sb.append(player2Cnt);
		sb.append("\r\n");
		if (player1Cnt > player2Cnt) {
			sb.append(PlayerList.get(0).getMyColor().getName() + "の勝利");
		} else if (player1Cnt == player2Cnt) {
			sb.append("引き分け");
		} else {
			sb.append(PlayerList.get(1).getMyColor().getName() + "の勝利");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

}
