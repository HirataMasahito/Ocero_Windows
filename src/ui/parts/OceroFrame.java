package ui.parts;

import game.ai.AiBase;
import game.ai.impl.Human;
import game.ai.impl.MaxStone;
import game.ai.impl.MaxStoneR;
import game.ai.impl.OneWay;
import game.ai.impl.SteadyR;

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
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Othello";
	private static final String GAME_MENU = "GAME";
	private static final String GAME_MENU_RESET = "RESET";
	private static final String GAME_MENU_EXIT = "EXIT";
	private static final String SETTING_MENU = "SETTING";
	private static final String SETTING_MENU_BLACK = "BLACK";
	private static final String SETTING_MENU_WHITE = "WHITE";

	private OceroPanel panel;

	JMenu aiListBlackMenu = new JMenu(SETTING_MENU_BLACK);
	JMenu aiListWhiteMenu = new JMenu(SETTING_MENU_WHITE);

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
	 *
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

		ButtonGroup group = new ButtonGroup();
		group.add(menuitem1);
		group.add(menuitem2);
		group.add(menuitem3);
		group.add(menuitem4);
		group.add(menuitem5);

		menuitem1.setSelected(true);

		parentMenu.add(menuitem1);
		parentMenu.add(menuitem2);
		parentMenu.add(menuitem3);
		parentMenu.add(menuitem4);
		parentMenu.add(menuitem5);

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

	private void gameReset() {
		AiBase player1 = getSelectedAi(aiListBlackMenu,Stone.BLACK);
		AiBase player2 = getSelectedAi(aiListWhiteMenu,Stone.WHITE);
		panel.Reset(player1,player2);
	}

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
		}

		return retAi;

	}

}
