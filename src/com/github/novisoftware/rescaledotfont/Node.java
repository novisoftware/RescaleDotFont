package com.github.novisoftware.rescaledotfont;

import java.util.ArrayList;

public class Node {
	double x;
	double y;
	ArrayList<Node> linkedNode;

	public Node(double x, double y) {
		this.x = x;
		this.y = y;
		this.linkedNode = new ArrayList<Node>();
	}
}
