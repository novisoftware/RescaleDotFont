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
