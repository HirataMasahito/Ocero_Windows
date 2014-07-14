package common;

import java.util.ArrayList;

/**
 * 座標管理用クラス
 *
 */
public class Pos {
	/** X座標 */
	private int x;
	/** Y座標 */
	private int y;

	/**
	 * 引数なしコンストラクタ
	 */
	public Pos() {
		x = 0;
		y = 0;
	}

	/**
	 * 引数憑きコンストラクタ
	 *
	 * @param x
	 *            x座標
	 * @param y
	 *            y座標
	 */
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            セットする x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            セットする y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 移動可能か判断
	 *
	 * @param toDirection
	 *            進行方向
	 * @return 移動可能ならTrue
	 */
	public boolean canMove(Pos toDirection) {
		boolean retFlg = true;
		int moveX = this.getX() + toDirection.getX();
		int moveY = this.getY() + toDirection.getY();
		if (moveX < Common.X_MIN_LEN || moveX >= Common.X_MAX_LEN || moveY < Common.Y_MIN_LEN || moveY >= Common.Y_MAX_LEN) {
			retFlg = false;
		}
		return retFlg;
	}

	/**
	 * 指定した方向に移動する
	 *
	 * @param toDirection
	 *            進行方向
	 */
	public void doMove(Pos toDirection) {
		setX(getX() + toDirection.getX());
		setY(getY() + toDirection.getY());
	}

	/**
	 * 進行方向となる8方向の移動増す数をリストで取得
	 *
	 * @return 8方向の移動距離の入ったArrayList
	 */
	public static ArrayList<Pos> getToAllDirection() {
		ArrayList<Pos> retList = new ArrayList<Pos>();

		retList.add(new Pos(-1, -1));
		retList.add(new Pos(0, -1));
		retList.add(new Pos(1, -1));
		retList.add(new Pos(-1, 0));
		retList.add(new Pos(1, 0));
		retList.add(new Pos(-1, 1));
		retList.add(new Pos(0, 1));
		retList.add(new Pos(1, 1));

		return retList;
	}

	@Override
	public boolean equals(Object obj) {
		Pos target = (Pos) obj;
		if (this.getX() == target.getX() && this.getY() == target.getY()) {
			return true;
		} else {
			return false;
		}
	}
}
