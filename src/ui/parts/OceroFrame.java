package ui.parts;

import game.ai.AiBase;
import game.ai.impl.HiranoTest;
import game.ai.impl.Human;
import game.ai.impl.Ikeda;
import game.ai.impl.MH_Spart;
import game.ai.impl.MaxStone;
import game.ai.impl.MaxStoneR;
import game.ai.impl.NoSafety;
import game.ai.impl.OneWay;
import game.ai.impl.SN_Ai;
import game.ai.impl.SteadyR;
import game.ai.impl.Uchida1;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import common.Common.Stone;

/**
 * オセロウィンドウフレーム
 * 後メニュー管理
 */
public class OceroFrame extends JFrame implements ActionListener {

	/**
	 * デフォルトバージョン番号
	 */
	private static final long serialVersionUID = 1L;
	/** 画面タイトル */
	private static final String TITLE = "Othello";
	/** メニュータイトル GAME */
	private static final String GAME_MENU = "GAME";
	/** メニュータイトル リセット */
	private static final String GAME_MENU_RESET = "RESET";
	/** メニュータイトル 終了 */
	private static final String GAME_MENU_EXIT = "EXIT";
	/** メニュータイトル 設定 */
	private static final String SETTING_MENU = "SETTING";
	/** メニュータイトル 黒石AI */
	private static final String SETTING_MENU_BLACK = "BLACK";
	/** メニュータイトル 白いしAI */
	private static final String SETTING_MENU_WHITE = "WHITE";

	/** フレームに表示するパネルクラス */
	private OceroPanel panel;

	/** 黒AIメニュー */
	private JMenu aiListBlackMenu = new JMenu(SETTING_MENU_BLACK);
	/** 白AIメニュー */
	private JMenu aiListWhiteMenu = new JMenu(SETTING_MENU_WHITE);

	/**
	 * コンストラクタ
	 * タイトル、メニューを設定しゲームを初期化する
	 */
	public OceroFrame() {
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// メニューバーを生成
		JMenuBar menubar = new JMenuBar();
		menubar.add(createGameMenu());
		menubar.add(createSettingMenu());
		setJMenuBar(menubar);

		// パネルの生成
		panel = new OceroPanel();
		// 初めは人VS人で設定
		panel.Reset(new Human(Stone.BLACK), new Human(Stone.WHITE));
		getContentPane().add(panel);
		pack();
	}

	/**
	 * 【GAME】メニューの生成
	 *
	 * @return GAME メニュー
	 */
	private JMenu createGameMenu() {
		JMenu gameMenu = new JMenu(GAME_MENU);
		JMenuItem menuitem1 = new JMenuItem(GAME_MENU_RESET);
		JMenuItem menuitem2 = new JMenuItem(GAME_MENU_EXIT);

		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);

		gameMenu.add(menuitem1);
		gameMenu.add(menuitem2);
		return gameMenu;
	}

	/**
	 * 【SETTING】メニューの生成
	 *
	 * @return SETTING メニュー
	 */
	private JMenu createSettingMenu() {
		JMenu settingMenu = new JMenu(SETTING_MENU);

		aiListBlackMenu.addActionListener(this);
		aiListWhiteMenu.addActionListener(this);

		settingMenu.add(createAiMenuList(aiListBlackMenu));
		settingMenu.add(createAiMenuList(aiListWhiteMenu));

		return settingMenu;
	}

	/**
	 * 【SETTING】メニュー内のAI選択メニューの生成
	 * TODO 新しくAIを追加したら、ここに追加してください。
	 * @param parentMenu
	 *            親となるメニュー
	 * @return AIリストの追加されたメニュー
	 */
	private JMenuItem createAiMenuList(JMenu parentMenu) {
		JRadioButtonMenuItem menuitem1 = new JRadioButtonMenuItem("HUMAN");
		JRadioButtonMenuItem menuitem2 = new JRadioButtonMenuItem("MaxStone");
		JRadioButtonMenuItem menuitem3 = new JRadioButtonMenuItem("MaxStoneR");
		JRadioButtonMenuItem menuitem4 = new JRadioButtonMenuItem("OneWay");
		JRadioButtonMenuItem menuitem5 = new JRadioButtonMenuItem("SteadyR");
		JRadioButtonMenuItem menuitem6 = new JRadioButtonMenuItem("NoSafety");
		JRadioButtonMenuItem menuitem7 = new JRadioButtonMenuItem("MH_Spart");
		JRadioButtonMenuItem menuitem8 = new JRadioButtonMenuItem("Uchida1");
		JRadioButtonMenuItem menuitem9 = new JRadioButtonMenuItem("SN_Ai");
		JRadioButtonMenuItem menuitem10 = new JRadioButtonMenuItem("Ikeda");
		JRadioButtonMenuItem menuitem11 = new JRadioButtonMenuItem("HiranoTest");
		/* TODO 新たにAIを作成したら、このコードをコピーして追記してください*/
		//JRadioButtonMenuItem menuitemX = new JRadioButtonMenuItem("【メニューに表示するAI名】");

		ButtonGroup group = new ButtonGroup();
		group.add(menuitem1);
		group.add(menuitem2);
		group.add(menuitem3);
		group.add(menuitem4);
		group.add(menuitem5);
		group.add(menuitem6);
		group.add(menuitem7);
		group.add(menuitem8);
		group.add(menuitem9);
		group.add(menuitem10);
		group.add(menuitem11);

		/* TODO 新たにAIを作成したら、このコードをコピーして追記してください*/
		//group.add(menuitemS);


		menuitem1.setSelected(true);

		parentMenu.add(menuitem1);
		parentMenu.add(menuitem2);
		parentMenu.add(menuitem3);
		parentMenu.add(menuitem4);
		parentMenu.add(menuitem5);
		parentMenu.add(menuitem6);
		parentMenu.add(menuitem7);
		parentMenu.add(menuitem8);
		parentMenu.add(menuitem9);
		parentMenu.add(menuitem10);
		parentMenu.add(menuitem11);
		/* TODO 新たにAIを作成したら、このコードをコピーして追記してください*/
		//parentMenu.add(menuitemX);

		return parentMenu;

	}

	/**
	 * メニュークリックイベントリスナ
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(GAME_MENU_RESET)) {
			gameReset();
		} else if (e.getActionCommand().equals(GAME_MENU_EXIT)) {
			System.exit(0);// 終了
		}

	}

	/**
	 * ゲームのリセット
	 */
	private void gameReset() {
		//選択されているAIで、ゲームを初期化する
		AiBase player1 = getSelectedAi(aiListBlackMenu,Stone.BLACK);
		AiBase player2 = getSelectedAi(aiListWhiteMenu,Stone.WHITE);
		panel.Reset(player1,player2);
	}

	/**
	 * メニューで選択されているAIを取得する
	 *
	 * TODO 新しくAIを追加したら、ここに追加してください。
	 *
	 * @param menuList メニューリスト（黒メニュー、白メニュー）
	 * @param color 石の色
	 * @return 選択されているAI
	 */
	private AiBase getSelectedAi(JMenu menuList,Stone color) {
		AiBase retAi = null;
		JRadioButtonMenuItem selectedMenu = null;
		for (Component component : menuList.getPopupMenu().getComponents()) {
			if (component == null) {
				continue;
			}
			selectedMenu = (JRadioButtonMenuItem)component;
			if(selectedMenu.isSelected()){
				break;
			}
		}

		if(selectedMenu == null || selectedMenu.getText().equals("HUMAN")){
			retAi = new Human(color);
		}else if(selectedMenu.getText().equals("MaxStone")){
			retAi = new MaxStone(color);
		}else if(selectedMenu.getText().equals("MaxStoneR")){
			retAi = new MaxStoneR(color);
		}else if(selectedMenu.getText().equals("OneWay")){
			retAi = new OneWay(color);
		}else if(selectedMenu.getText().equals("SteadyR")){
			retAi = new SteadyR(color);
		}else if(selectedMenu.getText().equals("NoSafety")){
			retAi = new NoSafety(color);
		}else if(selectedMenu.getText().equals("MH_Spart")){
			retAi = new MH_Spart(color);
		}else if(selectedMenu.getText().equals("Uchida1")){
			retAi = new Uchida1(color);
		}else if(selectedMenu.getText().equals("SN_Ai")){
			retAi = new SN_Ai(color);
		}else if(selectedMenu.getText().equals("Ikeda")){
			retAi = new Ikeda(color);
		}else if(selectedMenu.getText().equals("HiranoTest")){
			retAi = new HiranoTest(color);
		}

		/* TODO 新たにAIを作成したら、このブロックをコピーして追記してください
		else if(selectedMenu.getText().equals("【メニューに追加したAI名】")){
			retAi = new 【作成したAI】(color);
		}
		 */

		return retAi;

	}

}
