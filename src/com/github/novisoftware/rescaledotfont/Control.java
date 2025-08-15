package com.github.novisoftware.rescaledotfont;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * JLabelの見た目を変更したもの。
 */
class JLabel2 extends JLabel {
	public JLabel2(String text) {
		super(text);
		this.setFont(GuiPreference.LABEL_FONT);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g2);
	}
}

class JSlider2 extends JSlider {
	public JSlider2(int a, int b) {
		super(a, b);
		this.setPreferredSize(new Dimension(400, 24));
	}
}


class JTextField2 extends JTextField {
	public JTextField2(String s) {
		super(s);

		this.setPreferredSize(new Dimension(400,24));
		this.setFont(GuiPreference.INPUT_FONT);
	}
}

class JButton2 extends JButton {
	public JButton2(String s) {
		super(s);
		this.setFont(GuiPreference.INPUT_FONT);
	}
}


public class Control extends JFrame {
	int N_WIDTH = 400;
	int R_WIDTH = 495;

	public static void setLookAndFeel(JFrame frame) {
		String tryLaf[] = {
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
				// "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
		};

		for (String lookAndFeel: tryLaf) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
				SwingUtilities.updateComponentTreeUI(frame);
				break;
			} catch(Exception e){
				// 処理不要
			}
		}
	}

	protected void addHorizontalRule(Container c, int width, int height) {
		Box hr = Box.createHorizontalBox();
		hr.setPreferredSize(new Dimension(width, height));
		c.add(hr);
	}

	protected void addRule(Container c) {
		addHorizontalRule(c, R_WIDTH, 2);
	}

	static final int SLIDER_SCALE = 100;

	final Viewer viewer;

	public Control(Viewer viewer) {
		this.viewer = viewer;
		this.setTitle("テキストをビットマップにします");

		Control.setLookAndFeel(this);
		JPanel panel = new JPanel();
		// panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		// panel.setLayout(new FlowLayout());
		this.add(panel);

		ArrayList<JSlider> sliders = new ArrayList<JSlider>();

		addRule(panel);

		panel.add(new JLabel2("テキスト"));
		addRule(panel);
		final JTextField tf1 = new JTextField2("");
		panel.add(tf1);

		addRule(panel);
		panel.add(new JLabel2("ドット位置スムージング (弱←→強)"));
		addRule(panel);
		final JSlider slider1 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider1);
		sliders.add(slider1);

		addRule(panel);
		panel.add(new JLabel2("ランダム添加 (弱←→強)"));
		addRule(panel);
		final JSlider slider2 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider2);
		sliders.add(slider2);
		// ランダム添加は、初期値0
		slider2.setValue(0);

		addRule(panel);
		panel.add(new JLabel2("ランダムのシード"));
		addRule(panel);
		final JSlider slider3 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider3);
		sliders.add(slider3);


		addRule(panel);
		panel.add(new JLabel2("（レンダリング）カーネルの大きさ"));
		addRule(panel);
		final JSlider slider4 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider4);
		sliders.add(slider4);


		addRule(panel);
		panel.add(new JLabel2("（レンダリング）閾値"));
		addRule(panel);
		final JSlider slider5 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider5);
		sliders.add(slider5);


		addRule(panel);
		panel.add(new JLabel2("文字の間隔"));
		addRule(panel);
		final JSlider slider6 = new JSlider2(0, SLIDER_SCALE);
		panel.add(slider6);
		sliders.add(slider6);


		addRule(panel);
		panel.add(new JLabel2("横波歪み（x方向）"));
		addRule(panel);
		panel.add(new JLabel2("周期"));
		final JSlider slider7a = new JSlider2(0, SLIDER_SCALE);
		slider7a.setValue(0);
		panel.add(slider7a);
		sliders.add(slider7a);

		addRule(panel);
		panel.add(new JLabel2("位相"));
		final JSlider slider7b = new JSlider2(0, SLIDER_SCALE);
		slider7b.setValue(0);
		panel.add(slider7b);
		sliders.add(slider7b);

		addRule(panel);
		panel.add(new JLabel2("強さ"));
		final JSlider slider7c = new JSlider2(0, SLIDER_SCALE);
		slider7c.setValue(0);
		panel.add(slider7c);
		sliders.add(slider7c);


		addRule(panel);
		panel.add(new JLabel2("横波歪み（y方向）"));
		addRule(panel);
		panel.add(new JLabel2("周期"));
		final JSlider slider8a = new JSlider2(0, SLIDER_SCALE);
		slider8a.setValue(0);
		panel.add(slider8a);
		sliders.add(slider8a);

		addRule(panel);
		panel.add(new JLabel2("位相"));
		final JSlider slider8b = new JSlider2(0, SLIDER_SCALE);
		slider8b.setValue(0);
		panel.add(slider8b);
		sliders.add(slider8b);

		addRule(panel);
		panel.add(new JLabel2("強さ"));
		final JSlider slider8c = new JSlider2(0, SLIDER_SCALE);
		slider8c.setValue(0);
		panel.add(slider8c);
		sliders.add(slider8c);


		addRule(panel);
		JButton button1 = new JButton2("生成");
		panel.add(button1);

		addRule(panel);
		JButton button2a = new JButton2("透過PNGで保存");
		panel.add(button2a);
		button2a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.outputImageFile(true);
			}
		});

		addRule(panel);
		JButton button2c = new JButton2("PNGで保存(非透過)");
		panel.add(button2c);
		button2c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.outputImageFile(false);
			}
		});

		// 変換処理
		final Runnable render =
		new Runnable() {
            @Override
            public void run() {
				double smoothRatio = slider1.getValue() *1.0 / SLIDER_SCALE;

				String text = tf1.getText();
				ArrayList<ArrayList<NodeHolder>> t = Control.conv(text);
				ArrayList<NodeHolder> nhList = t.get(0);
				ArrayList<NodeHolder> nhListNegative = t.get(1);

				// 座標スムージング
				for (NodeHolder nh : nhList) {
					nh.smoothing(smoothRatio);
				}

				// ランダム添加
				double size = 5;
				int randomSeed = 150 * slider3.getValue();
				double randomAdopt = slider2.getValue() *1.0 / SLIDER_SCALE;
				if (randomAdopt > 0.0) {
					Random random = new Random(randomSeed);

					for (NodeHolder nh : nhList) {
						for (Node n : nh.nodeList) {
							n.x += random.nextDouble() * size * randomAdopt;
							n.y += random.nextDouble() * size * randomAdopt;
						}
					}
				}

				double kernelSize = slider4.getValue() *1.0 / SLIDER_SCALE;
				double threshold = slider5.getValue() *1.0 / SLIDER_SCALE;

				// 文字の間隔
				double pitchOfChar = slider6.getValue() * 1.0 / SLIDER_SCALE;

				// 横波歪み（x方向）
				double xDistFreq = slider7a.getValue() * 1.0 / SLIDER_SCALE * 30;
				double xDistPhase = slider7b.getValue() * 1.0 / SLIDER_SCALE;
				double xDistAmp = slider7c.getValue() * 1.0 / SLIDER_SCALE * 3;

				// 横波歪み（y方向）
				double yDistFreq = slider8a.getValue() * 1.0 / SLIDER_SCALE * 8;
				double yDistPhase = slider8b.getValue() * 1.0 / SLIDER_SCALE;
				double yDistAmp = slider8c.getValue() * 1.0 / SLIDER_SCALE;


				viewer.setTitleStr(text);
				viewer.setNodeHolderList(nhList, nhListNegative, pitchOfChar, kernelSize, threshold,
						/* 横波歪み */
						xDistFreq ,
						xDistPhase ,
						xDistAmp ,
						yDistFreq ,
						yDistPhase ,
						yDistAmp
						);
			}
            };

            ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				render.run();
			}
		};

		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				render.run();
			}
		};

		button1.addActionListener(al);
		for (JSlider s : sliders) {
			s.addChangeListener(cl);
		}
	}

	static public ArrayList<ArrayList<NodeHolder>> conv(String str) {
		ArrayList<ArrayList<NodeHolder>> ret = new ArrayList<ArrayList<NodeHolder>>();
		ArrayList<NodeHolder> work = new ArrayList<NodeHolder>();
		ArrayList<NodeHolder> workNegative = new ArrayList<NodeHolder>();

		for (int i=0 ; i < str.length() ; i++) {
			String s1 = "" + str.charAt(i);
			boolean[][] bitmap = TextToBitmapArrayConverter.convert(s1);
			NodeHolder nh = BitmapArrayToNodesConverter.convert(bitmap);
			work.add(nh);
			NodeHolder nhNegative = BitmapArrayToNodesConverter.convertNegative(bitmap);
			workNegative.add(nhNegative);
		}

		ret.add(work);
		ret.add(workNegative);

		return ret;
	}


	static public void main(String[] arg) {
		Viewer viewer = new Viewer();
		viewer.setVisible(true);
		Control controller = new Control(viewer);
		controller.setSize(500, 1000);
		controller.setVisible(true);
	}
}
