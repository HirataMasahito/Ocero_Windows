package ui.parts;

import game.ai.AiBase;
import game.controller.GameController;
import game.othello.Bord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import common.Common;
import common.Pos;

/**
 * 画面描画、および、イベント管理クラス
 * 画面の描画、ゲームの進行を管理します。
 */
public class OceroPanel extends JPanel implements MouseListener, ActionListener, Observer {

	/**
	 * バージョンID
	 */
	private static final long serialVersionUID = 1L;

	GameController controller;
	Timer timer;

	/**
	 * コンストラクタ ゲームコントローラの初期化を行う
	 */
	public OceroPanel() {
		setPreferredSize(new Dimension(Common.BORD_WIDTH, Common.BORD_HEIGHT));
		addMouseListener(this);

		timer = new Timer(100, this);
		timer.start();
	}

	/**
	 * オセロのリセット処理
	 * メニューから呼ばれる
	 * @param player1 黒のAI
	 * @param player2 白のAI
	 */
	public void Reset(AiBase player1, AiBase player2) {
		controller = new GameController(player1, player2);
		controller.addObserver(this);
		controller.GameStart();
		timer.start();
	}

	/**
	 * 描画処理
	 */
	public void paintComponent(Graphics g) {

		// 背景
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Common.BORD_WIDTH, Common.BORD_HEIGHT);

		// 線
		g.setColor(Color.BLACK);
		for (int i = 0; i < 8; i++) {
			g.drawLine(0, i * Common.CELL_SIZE, Common.BORD_WIDTH, i * Common.CELL_SIZE);
			g.drawLine(i * Common.CELL_SIZE, 0, i * Common.CELL_SIZE, Common.BORD_HEIGHT);
		}
		g.setColor(Color.DARK_GRAY);
		// g.drawRect(SIZE*2, SIZE*2, SIZE*4, SIZE*4);

		Bord playBord = controller.getBord();

		Pos workPos = new Pos();
		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);
				switch (playBord.getColor(workPos)) {
				case NONE:
					break;
				case BLACK:
					g.setColor(Color.BLACK);
					g.fillOval(x * Common.CELL_SIZE, y * Common.CELL_SIZE, Common.CELL_SIZE, Common.CELL_SIZE);
					break;
				case WHITE:
					g.setColor(Color.WHITE);
					g.fillOval(x * Common.CELL_SIZE, y * Common.CELL_SIZE, Common.CELL_SIZE, Common.CELL_SIZE);
					break;
				}
			}
		}

	}

	/**
	 * 画面の更新
	 */
	public void update(Observable o, Object arg) {
		repaint();
		if (controller.isGameSet()) {
			timer.stop();
			JOptionPane.showMessageDialog(this, controller.getResultMessage(), "Finish",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * マウスのクリックイベント
	 */
	public void mousePressed(MouseEvent e) {

		int x = e.getX() / Common.CELL_SIZE;
		int y = e.getY() / Common.CELL_SIZE;

		System.out.println("(x,y)=(" + x + "," + y + ")");

		// 入力範囲を超えていないか判断
		if (x >= Common.X_MAX_LEN || y < Common.X_MIN_LEN) {
			return;
		}
		// クリックした場所に置けるか判断
		Pos clickPos = new Pos(x, y);
		if (controller.getBord().CanSet(clickPos, controller.getNowColor())) {
			controller.NextTurn(clickPos);
		}
	}


	/**
	 * タイマー処理
	 */
	public void actionPerformed(ActionEvent e) {
		// 次が機械なら自動で打つ
		if (!controller.isNowHuman()) {
			controller.NextTurn(null);
		}
	}

	///以下、未使用メソッド
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
