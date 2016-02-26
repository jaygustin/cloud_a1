package cloud_a1;

public class BinaryTree {

	Node root;

	public void addRootNode(int key, String name) {
		root = new Node(key, name);
	}

	public void addYesChild(int parentKey, int key, String name) {
		Node parent = findNode(parentKey);
		parent.yesChild = new Node(key, name);
	}

	public void addNoChild(int parentKey, int key, String name) {
		Node parent = findNode(parentKey);
		parent.noChild = new Node(key, name);
	}

	public Node getNextNode(int key, boolean yes) {
		Node focusNode = findNode(key);
		return yes ? focusNode.yesChild : focusNode.noChild;
	}

	public Node findNode(int key) {
		// Start at root
		Node focusNode = root;

		while (focusNode.id != key) {
			if (key < focusNode.id) {
				focusNode = focusNode.yesChild;
			} else {
				focusNode = focusNode.noChild;
			}
			// The node wasn't found
			if (focusNode == null)
				return null;
		}
		return focusNode;
	}
}

class Node {
	int id;
	String question;

	Node yesChild;
	Node noChild;

	Node(int id, String question) {
		this.id = id;
		this.question = question;
	}

	public String toString() {
		return id + ":" + question;
	}
}
