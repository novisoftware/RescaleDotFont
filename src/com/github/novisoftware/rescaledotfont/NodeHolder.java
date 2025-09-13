package com.github.novisoftware.rescaledotfont;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeHolder {
	ArrayList<Node> nodeList;

	public NodeHolder() {
		this.nodeList = new ArrayList<Node>();
	}

	void add(Node node) {
		this.nodeList.add(node);
	}

	static public final double mod360(double a) {
		if (a < 0) {
			a +=360;
		}
		if (a > 360) {
			a -= 360;
		}

		return a;
	}

	static public boolean nearEq(double a, double b) {
		double w = Math.abs(mod360(a - b));

		return w < 5 || w > 355;
	}

	// 余分な枝を取る
	public void cut() {
		HashMap<Node, Node> workMap = new HashMap<Node, Node>();

		for (Node n : nodeList) {
			int nNode = n.linkedNode.size();

			if (nNode >= 3) {
				Node[] nn = new Node[nNode];
				double ax = 0;
				double ay = 0;

				// 角度を求める（ここから）
				double[] th = new double[nNode];
				for (int i = 0 ; i < nNode ; i++) {
					nn[i] = n.linkedNode.get(i);
					th[i] = mod360(Math.atan2(nn[i].x - n.x, nn[i].y - n.y) * (180.0 / Math.PI));

					ax += nn[i].x - n.x;
					ay += nn[i].y - n.y;
				}
				// 角度を求める（ここまで）

				// 十字の交差は、スルーする。
				if (ax == 0 && ay == 0) {
					continue;
				}
				/*
				if (nearEq(mod360(mod360(mod360(th[0]+th[1])+th[2])+th[3]), 0)) {
					// 十字の交差
					continue;
				}
				*/

				// 「Ψ」の形の交差
				boolean isFound = false;
				for (int i = 0 ; i < nNode ; i++) {
					for (int j = 0 ; j < i ; j++) {
						if (nearEq(th[i], th[j] + 180)) {
							n.linkedNode.clear();
							n.linkedNode.add(nn[i]);
							n.linkedNode.add(nn[j]);
							isFound = true;
						}
					}
					if (isFound) {
						break;
					}
				}
			}
		}

		for (Node n : nodeList) {
			Node newPos = workMap.get(n);
			if (newPos != null) {
				n.x = newPos.x;
				n.y = newPos.y;
			}
		}
	}

	// 位置をスムージングする
	// ratio が スムージングの強さ
	public void smoothing(double ratio) {
		if (ratio == 0.0) {
			return;
		}

		// double ratio = 0.5;
		double rc = 1 - ratio;

		HashMap<Node, Node> workMap = new HashMap<Node, Node>();

		for (Node n : nodeList) {
			if (n.linkedNode.size() == 2) {
				Node nn0 = n.linkedNode.get(0);
				Node nn1 = n.linkedNode.get(1);

				// 角度を求める（ここから）
				double th0 = Math.atan2(nn0.x - n.x, nn0.y - n.y) * (180.0 / Math.PI);
				double th1 = Math.atan2(nn1.x - n.x, nn1.y - n.y) * (180.0 / Math.PI);

				double thDiff = th1 - th0;
				if (thDiff < 0) {
					thDiff *= -1;
				}
				if (thDiff > 180) {
					thDiff = 360 - thDiff;
				}
				// 角度を求める（ここまで）

				// 鈍角の場合
				if (thDiff > 91 ) {
					double x2 = ratio * (nn0.x + nn1.x)/2 + rc * n.x;
					double y2 = ratio * (nn0.y + nn1.y)/2 + rc * n.y;

					workMap.put(n, new Node(x2, y2));
				}
			}
		}

		for (Node n : nodeList) {
			Node newPos = workMap.get(n);
			if (newPos != null) {
				n.x = newPos.x;
				n.y = newPos.y;
			}
		}
	}

}
