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

	public static void main(String[] args){

		OceroFrame flame = new OceroFrame();
		flame.setVisible(true);

	}


}
