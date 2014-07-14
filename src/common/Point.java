package common;

/**
 * 座標と、値を保持するためのクラス。
 * 用途は色々
 */
public class Point {
	/** 値 */
	int value;
	/** 座標 */
	Pos pos;

	/**
	 * コンストラクタ
	 */
	public Point() {
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param pos 座標
	 */
	public Point(int value, Pos pos) {
		this.value = value;
		this.pos = pos;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value){
		this.value = value;
	}

	public Pos getPos() {
		return pos;
	}
	public void setPos(Pos pos){
		this.pos = pos;
	}
}
