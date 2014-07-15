package ui;

import ui.parts.OceroFrame;

/**
 * Windowアプリ向けUI
 * エントリポイント
 */
public class OceroWindow {
	/**
	 * デフォルトバージョンID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * エントリポイント
	 */
	public static void main(String[] args){
		//フレームを定義して表示する
		OceroFrame flame = new OceroFrame();
		flame.setVisible(true);
	}


}
