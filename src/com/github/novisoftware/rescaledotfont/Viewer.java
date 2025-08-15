package com.github.novisoftware.rescaledotfont;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Viewer extends JFrame {
	static int W = 3000, H = 500;
	static double scale = 20;
	static double r = 0.3;
	JPanel panel;
	ArrayList<NodeHolder> nhList;
	ArrayList<NodeHolder> nhListNegative;
	String titleStr = "";

	public Viewer() {
		JPanel panel = new JPanel();
		this.add(panel);
		this.setSize(W, H);
		this.setTitleStr("");

		bimg0 = new BufferedImage(W, H, BufferedImage.TYPE_4BYTE_ABGR);
		bimg1 = new BufferedImage(W, H, BufferedImage.TYPE_4BYTE_ABGR);
		java.awt.Graphics g0 = bimg0.getGraphics();
		Graphics2D g = (Graphics2D) g0;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, W, H);
		g.dispose();
		java.awt.Graphics g1 = bimg0.getGraphics();
		Graphics2D g1b = (Graphics2D) g1;
		g1b.setColor(Color.WHITE);
		g1b.fillRect(0, 0, W, H);
		g1b.dispose();

	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
		this.setTitle("プレビュー  - " + titleStr);
	}

	// 2乗する
	private static final double sq(double d) {
		return d*d;
	}

	// 偶数
	BufferedImage bimg0;
	// 奇数
	BufferedImage bimg1;

	// Xの開始位置
	HashMap<NodeHolder,Double> nhToXbase = new HashMap<NodeHolder,Double>();

	// 描画の右端、または、固定値。
	// （画像ファイルを出力する際に使用する）
	int xMax = 100;


	void render(BufferedImage img, ArrayList<NodeHolder> nhListWork,
			ArrayList<NodeHolder> nhListNegative,
			double kernelSize, double threshold) {
		java.awt.Graphics g0 = img.getGraphics();
		Graphics2D g = (Graphics2D) g0;


		int h = img.getHeight();
		int w = img.getWidth();

		double[][] work2 = new double[h][w];


		// ネガティブ濃度マップを作る
		double Rneg = 100;
		double R2neg = sq(Rneg);
		double C2neg = sq(30);

		for (NodeHolder nh : nhListNegative) {
			double xBase = this.nhToXbase.get(nh);
			for (Node n : nh.nodeList) {
				double nx0 = n.x * scale + xBase;
				double ny0 = n.y * scale;

				double xDist = this.xDist(nx0);
				double yDist = this.yDist(n.y);

				double nx = nx0 + yDist * scale;
				double ny = ny0 + xDist * scale;

				int x0 = (int)Math.floor( nx - Rneg);
				int x1 = (int)Math.ceil( nx + Rneg);
				int y0 = (int)Math.floor( ny - Rneg);
				int y1 = (int)Math.ceil( ny + Rneg);

				if (x0 < 0) {
					x0 = 0;
				}
				if (y0 < 0) {
					y0 = 0;
				}
				if (x1 > w-1) {
					x1 = w-1;
				}
				if (y1 > h-1) {
					y1 = h-1;
				}

				for (int y = y0 ; y <= y1 ; y++) {
					for (int x = x0 ; x <= x1 ; x++) {
						double r2 = sq(x - nx) + sq(y - ny);
						if (r2 > R2neg) {
							continue;
						}

						work2[y][x] += 1 * Math.exp(-r2/C2neg);
						// 全然ダメ
						// ↓
						// work[y][x] *= 1 / (1 + 100 * Math.exp(-r2/C2));
					}
				}
			}
		}

		// 濃度マップを作る

		// double th = 0.2;
		double th = 0.3 * (1.1 - threshold);

		double R = 150;
		double R2 = sq(R);
		double C2 = sq(30*kernelSize);


		double[][] work = new double[h][w];

		for (NodeHolder nh : nhListWork) {
			double xBase = this.nhToXbase.get(nh);
			for (Node n : nh.nodeList) {
				double nx0 = n.x * scale + xBase;
				double ny0 = n.y * scale;

				double xDist = this.xDist(nx0);
				double yDist = this.yDist(n.y);

				double nx = nx0 + yDist * scale;
				double ny = ny0 + xDist * scale;


				int x0 = (int)Math.floor( nx - R);
				int x1 = (int)Math.ceil( nx + R);
				int y0 = (int)Math.floor( ny - R);
				int y1 = (int)Math.ceil( ny + R);

				if (x0 < 0) {
					x0 = 0;
				}
				if (y0 < 0) {
					y0 = 0;
				}
				if (x1 > w-1) {
					x1 = w-1;
				}
				if (y1 > h-1) {
					y1 = h-1;
				}

				for (int y = y0 ; y <= y1 ; y++) {
					for (int x = x0 ; x <= x1 ; x++) {
						double R2tmp = R2 * (1 / (1 + work2[y][x]));
						double C2tmp = C2 * (1 / (1 + work2[y][x]));

						double r2 = sq(x - nx) + sq(y - ny);
						if (r2 > R2tmp) {
						// if (r2 > R2) {
							continue;
						}

						// work[y][x] += Math.exp(-r2/C2);
						work[y][x] += Math.exp(-r2/C2tmp);

					}
				}
			}
		}


		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.setColor(Color.BLACK);
		g.dispose();

		for (int y = 0 ; y < h ; y++ ) {
			for (int x = 0; x < w ; x++) {

				if (work[y][x] > th) {

				// if (work[y][x] * (1 / (1 + work2[y][x])) > th) {


				// if (work[y][x] > th && work2[y][x] < th) {
					img.setRGB(x, y, 0xFF000000);
					// レンダリングが存在する右端を記録する。
					if (xMax < x) {
						xMax = x;
					}
				} else {
					img.setRGB(x, y, 0x00FFFFFF);
				}
			}
		}
	}

	@Override
	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, W, H);

		g.drawImage(bimg0, 0, 0, null);
		g.drawImage(bimg1, 0, 0, null);

		if (nhList == null) {
			return;
		}

		// g.setColor(Color.LIGHT_GRAY);
		g.setColor(Color.LIGHT_GRAY);

		// double xBase = 50;
		// double xBaseWork = 0;

		for (NodeHolder nh : nhList) {
			double xBase = this.nhToXbase.get(nh);
			for (Node n : nh.nodeList) {
				double x = n.x * scale + xBase;
				double y = n.y * scale;
				g.fillOval((int) Math.round(x - scale * r * 0.5),
						(int) Math.round(y - scale * r * 0.5),
						(int) Math.round(scale * r),
						(int) Math.round(scale * r));

				// if (x > xBaseWork) {
				// 	xBaseWork = x;
				// }

				for (Node nn : n.linkedNode) {
					double xx = nn.x * scale + xBase;
					double yy = nn.y * scale;

					g.drawLine((int) Math.round(x),
							(int) Math.round(y),
							(int) Math.round(xx),
							(int) Math.round(yy));
				}
			}

			// xBase = xBaseWork;
		}

		g.setColor(Color.BLUE);
		for (NodeHolder nh : nhListNegative) {
			double xBase = this.nhToXbase.get(nh);
			for (Node n : nh.nodeList) {
				double x = n.x * scale + xBase;
				double y = n.y * scale;
				g.fillOval((int) Math.round(x - scale * r * 0.5),
						(int) Math.round(y - scale * r * 0.5),
						(int) Math.round(scale * r),
						(int) Math.round(scale * r));
			}

			// xBase = xBaseWork;
		}
	}

	public String makeFilename(boolean isTransparent) {
		String safeText = this.titleStr.replaceAll("[\\\\/:*?\"<>| ]+", "_");

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format =
				DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String strNow = format.format(now);
		String f = String.format("%s_%s%s.png", safeText, strNow, isTransparent ? "【透過】" : "");

		return f;
	}

	public void outputImageFile(boolean isTransparent) {
		int rightMargin = 50;
		int imageWidth = xMax + rightMargin;

		int imageType = isTransparent ?  BufferedImage.TYPE_INT_ARGB :  BufferedImage.TYPE_INT_RGB;

		BufferedImage bimg = new BufferedImage(imageWidth, H, imageType);
		for (int y = 0; y < H ; y++) {
			for (int x = 0 ; x < imageWidth ; x++) {
				bimg.setRGB(x, y, 0x00FFFFFF);
			}
		}
		java.awt.Graphics g0 = bimg.getGraphics();
		Graphics2D g = (Graphics2D) g0;
		g.drawImage(bimg0, 0, 0, null);
		g.drawImage(bimg1, 0, 0, null);
		g.dispose();

		String filename = makeFilename(isTransparent);
		try {
			ImageIO.write(bimg, "png", new File(filename));
		} catch (IOException e) {
			System.err.println("ファイル出力に失敗しました: " + e.toString());
		}
		System.out.println("ファイルを出力しました: " + filename);
	}

	// 横波歪み（x方向）
	double xDistFreq ;
	double xDistPhase ;
	double xDistAmp ;

	// 横波歪み（y方向）
	double yDistFreq ;
	double yDistPhase ;
	double yDistAmp ;

	static final double LEFT_MARGIN = 50;

	// 暫定の右端位置
	double xMaxPre = 100;
	double yMinPre = 0;
	double yMaxPre = 0;

	// 返り値はyへの加算量
	public final double xDist(double x0) {
		if (LEFT_MARGIN == xMaxPre) {
			return 0;
		}
		double p0 = (x0 - LEFT_MARGIN) / (LEFT_MARGIN - xMaxPre) * this.xDistFreq  * 2 * Math.PI;
		double p1 = p0 + this.xDistPhase * 2 * Math.PI;

		return Math.sin(p1) * this.xDistAmp;
	}

	// 返り値はxへの加算量
	public final double yDist(double y0) {
		if (yMinPre == yMaxPre) {
			return 0;
		}

		double p0 = (y0 - this.yMinPre) / (this.yMaxPre - this.yMinPre) * this.yDistFreq * 2 * Math.PI;
		double p1 = p0 + this.yDistPhase * 2 * Math.PI;

		System.out.println("p " + (Math.sin(p1) * this.yDistAmp));

		return Math.sin(p1) * this.yDistAmp;
	}


	public void setNodeHolderList(ArrayList<NodeHolder> nhList,
			ArrayList<NodeHolder> nhListNegative,
			double pitchOfChar, double kernelSize, double threshold,
			double xDistFreq ,
			double xDistPhase ,
			double xDistAmp ,
			double yDistFreq ,
			double yDistPhase ,
			double yDistAmp
			) {
		this.xDistFreq =    xDistFreq ;
		this.xDistPhase =   xDistPhase ;
		this.xDistAmp =     xDistAmp ;

		this.yDistFreq =    yDistFreq ;
		this.yDistPhase =   yDistPhase ;
		this.yDistAmp =     yDistAmp ;

		double xBase = LEFT_MARGIN;
		for (int i = 0 ; i < nhList.size(); i++) {
			NodeHolder nh = nhList.get(i);
			NodeHolder nhNeg = nhListNegative.get(i);
			this.nhToXbase.put(nh, xBase);
			this.nhToXbase.put(nhNeg, xBase);

			double xBaseWork = xBase;
			for (Node n : nh.nodeList) {
				double x = n.x * scale + xBase;
				if (x > xBaseWork) {
					xBaseWork = x;
				}
			}
			xBase = xBaseWork;
			this.xMaxPre = xBase;
			xBase += 100* pitchOfChar;
		}

		// yの最大値、最小値
		boolean isFirst = true;
		for (int i = 0 ; i < nhList.size(); i++) {
			NodeHolder nh = nhList.get(i);
			for (Node n : nh.nodeList) {
				if (isFirst) {
					isFirst = false;

					yMinPre = n.y;
					yMaxPre = n.y;
				}

				if (n.y < yMinPre) {
					yMinPre = n.y;
				}
				if (n.y > yMaxPre) {
					yMaxPre = n.y;
				}
			}
		}

		ArrayList<NodeHolder> evenList = new ArrayList<NodeHolder>();
		ArrayList<NodeHolder> oddList = new ArrayList<NodeHolder>();
		ArrayList<NodeHolder> evenListNegative = new ArrayList<NodeHolder>();
		ArrayList<NodeHolder> oddListNegative = new ArrayList<NodeHolder>();

		for (int i = 0 ; i < nhList.size(); i++) {
			NodeHolder nh = nhList.get(i);
			NodeHolder nhNeg = nhListNegative.get(i);
			if ((i&1)==0) {
				evenList.add(nh);
				evenListNegative.add(nhNeg);
			}
			else {
				oddList.add(nh);
				oddListNegative.add(nhNeg);
			}
		}

		this.xMax = 100;
		this.render(bimg0, evenList, evenListNegative, kernelSize, threshold);
		this.render(bimg1, oddList, oddListNegative, kernelSize, threshold);

		this.nhList = nhList;
		this.nhListNegative = nhListNegative;
		this.repaint();
	}
}
