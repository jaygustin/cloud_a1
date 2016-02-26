package cloud_a1;

/**
 * Code modified from http://www.newthinktank.com/2013/03/binary-tree-in-java/
 * 
 * @author Derek Banas
 * @author Janice Agustin
 *
 */
public class BinaryTree {

	Node root;

	public void addNode(int key, String name) {

		Node newNode = new Node(key, name);

		// If there is no root this becomes root
		if (root == null) {
			root = newNode;
		} else {
			Node focusNode = root;
			Node parent;

			while (true) {
				parent = focusNode;

				// Check if the new node should go be a yes
				if (key < focusNode.key) {
					// Switch focus to the yes child
					focusNode = focusNode.yesChild;

					// If the yes child has no children
					if (focusNode == null) {
						// then place the new node on the left of it
						parent.yesChild = newNode;
						return; // All Done
					}
				} else { // node should be no
					focusNode = focusNode.noChild;
					if (focusNode == null) {
						parent.noChild = newNode;
						return;
					}
				}
			}
		}
	}

	public Node getNextNode(int key, boolean yes) {
		Node focusNode = findNode(key);
		return yes ? focusNode.yesChild : focusNode.noChild;
	}
	
	public Node getNextNode(Node focusNode, boolean yes) {
		return yes ? focusNode.yesChild : focusNode.noChild;
	}

	// All nodes are visited in ascending order
	// Recursion is used to go to one node and
	// then go to its child nodes and so forth

	public void inOrderTraverseTree(Node focusNode) {
		if (focusNode != null) {

			// Traverse the left node
			inOrderTraverseTree(focusNode.yesChild);

			// Visit the currently focused on node
			System.out.println(focusNode);

			// Traverse the right node
			inOrderTraverseTree(focusNode.noChild);
		}
	}

	public void preorderTraverseTree(Node focusNode) {
		if (focusNode != null) {

			System.out.println(focusNode);

			preorderTraverseTree(focusNode.yesChild);
			preorderTraverseTree(focusNode.noChild);
		}
	}

	public void postOrderTraverseTree(Node focusNode) {
		if (focusNode != null) {
			postOrderTraverseTree(focusNode.yesChild);
			postOrderTraverseTree(focusNode.noChild);

			System.out.println(focusNode);
		}
	}

	public Node findNode(int key) {
		// Start at the top of the tree
		Node focusNode = root;

		// While we haven't found the Node
		// keep looking

		while (focusNode.key != key) {

			// If we should search to the left
			if (key < focusNode.key) {
				// Shift the focus Node to the left child
				focusNode = focusNode.yesChild;
			} else {
				// Shift the focus Node to the right child
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

	int key;
	String name;

	Node yesChild;
	Node noChild;

	Node(int key, String name) {

		this.key = key;
		this.name = name;

	}

	public String toString() {

		return name + " has the key " + key;
	}

}
