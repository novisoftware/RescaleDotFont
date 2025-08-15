package com.github.novisoftware.rescaledotfont;

public class BitmapArrayToNodesConverter {
	static NodeHolder convert(boolean[][] bitmap) {
		NodeHolder holder = new NodeHolder();
		Node[][] work = new Node[bitmap.length][bitmap[0].length];

		for (int y = 0 ; y < bitmap.length ; y++) {
			for (int x = 0 ; x < bitmap[y].length ; x++) {
				if (bitmap[y][x]) {
					Node n = new Node(x, y);
					work[y][x] = n;
					holder.add(n);
				}
			}
		}

		for (int y = 1 ; y < bitmap.length - 1 ; y++) {
			for (int x = 1 ; x < bitmap[y].length - 1 ; x++) {
				if (work[y][x] == null) {
					continue;
				}
				Node n = work[y][x];
				for (int yy = -1 ; yy <=1; yy++) {
					for (int xx = -1; xx <=1 ; xx++) {
						if (yy == 0 && xx == 0) {
							continue;
						}
						Node n2 = work[y+yy][x+xx];
						if (n2 == null) {
							continue;
						}

						n.linkedNode.add(n2);
					}
				}

			}
		}

		return holder;
	}

	static NodeHolder convertNegative(boolean[][] bitmap) {
		NodeHolder holder = new NodeHolder();
		Node[][] work = new Node[bitmap.length][bitmap[0].length];

		for (int y = 1 ; y < bitmap.length - 1 ; y++) {
			for (int x = 1 ; x < bitmap[y].length -1 ; x++) {
				if (!bitmap[y][x]) {
					if ((bitmap[y-1][x] && bitmap[y+1][x])
							|| (bitmap[y][x-1] && bitmap[y][x+1])) {
						Node n = new Node(x, y);
						work[y][x] = n;
						holder.add(n);
					}
				}
			}
		}

		return holder;
	}
}
