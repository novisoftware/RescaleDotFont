package com.github.novisoftware.rescaledotfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;


// 文字列からビットマップでグリフを取得する
// （グリフの取得方法は、たんに drawString で描画する）
public class TextToBitmapArrayConverter {
	static final int W = 30, H = 30;

	static void paint(Graphics2D g, String str) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, TextToBitmapArrayConverter.W, TextToBitmapArrayConverter.H);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		try {
			String fontName = "ＭＳ ゴシック";
			// String fontName = "ＭＳ 明朝";
			Font f = new Font(fontName, Font.PLAIN, 16);
			g.setFont(f);
		} catch(Exception e) {
			e.printStackTrace();

			Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
			if (f != null) {
				g.setFont(f);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString("" + str, 1, 20);
	}

	static boolean[][] convert(String str) {
		BufferedImage bimg = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics g0 = bimg.getGraphics();
		Graphics2D g = (Graphics2D) g0;
		paint(g, str);
		g.dispose();


		boolean[][] bitmap = new boolean[bimg.getHeight()][bimg.getWidth()];

		boolean isStarted = false;
		int xPos = 1;
		for (int x = 0 ; x < bimg.getWidth() ; x++ ) {
			for (int y = 0 ; y < bimg.getHeight() ; y++ ) {
				int value = bimg.getRGB(x, y);
				boolean b = ((value & 0x255) > 128);

				// bitmap[y][x] = (value != 0);
				bitmap[y][xPos] = b;

				isStarted |= b;
			}
			if (isStarted) {
				xPos ++;
			}
		}


		return bitmap;
	}
}
