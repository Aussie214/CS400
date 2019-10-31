/**
 * Filename:   AVLTree.java
 * Credits:    N/A
 * Project:    p2
 * Authors:    Debra Deppeler, Austin Wilson
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    002
 * 
 * Due Date:   Before 10pm on September 24, 2018
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       leftRotate and rightRotate are not implemented
 */

import java.lang.IllegalArgumentException;

/** This class implements the AVLTreeADT<K> generic interface to produce
 * a working AVLTree for any given comparable object.
 * @param <K> Generic Object that can be used within the AVLTree
 */
public class AVLTree<K extends Comparable<K>> implements AVLTreeADT<K> {

	/** This class creates a new BSTNode of type K and provides getters
	 * and setters for manipulating and fetching data from the nodes.
	 * Represents a tree node.
	 * @param <K> Generic type of the AVLTree
	 */
	class BSTNode<K> {
		/* fields */
		private K key;	//Data of generic type K to be put in node
		private int height;	//Height of given node's children 
		private BSTNode<K> left, right;	//Pointer variables to left and right children
		
		/**
		 * Constructor for a BST node.
		 * @param key
		 */
		BSTNode(K key) {
			this.key = key;
			this.height = 1;
			this.left = null;
			this.right = null;
		}

		/* accessors */
		public K getKey() {
		    return this.key;
		} 
		public int getHeight() {
			return this.height;
		}
		public BSTNode<K> getLeft() {
			return this.left;
		}
		public BSTNode<K> getRight() {
			return this.right;
		}

		/* mutators */
		public void setKey(K key) {
			this.key = key;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		public void setLeft(BSTNode<K> left) {
			this.left = left;
		}
		public void setRight(BSTNode<K> right) {
			this.right = right;
		}

	}

	private BSTNode<K> root = null;	

	/**Checks to see if the AVLTree is empty, or contains elements
	 * @return true if the tree contains no elements
	 */
	@Override
	public boolean isEmpty() {
		if (root.equals(null)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**Inserts a new node into the tree based on a given generic value K
	 * @param key Value to be inserted
	 * @throws DuplicateKeyException
	 * @throws IllegalArgumentException 
	 */
	@Override
	public void insert(K key) throws DuplicateKeyException, IllegalArgumentException {
		if (key.equals(null)) {
			throw new IllegalArgumentException();
		}
		BSTNode<K> node = new BSTNode<K>(key);
		if (isEmpty()) {
			this.root = node;
			return;
		}
		insert(node, root);
	}

	/*Private helper method to traverse the tree and find the correct 
	 *place to insert a new node.
	 * @param node The new node to be inserted into the tree
	 * @param root The root node to check values against to traverse 
	 * correctly
	 * @thorws IllegalArgumentException
	 * @throws DuplicateKeyException
	 */
	private void insert(BSTNode<K> node, BSTNode<K> root) throws DuplicateKeyException, IllegalArgumentException {
		//Base case for recursion
		if ((node.getKey()).compareTo(root.getKey()) == 0) {
			throw new DuplicateKeyException();
		}
		//Recursion to go left in the tree
		if (node.getKey().compareTo(root.getKey()) < 0) {
			if (root.getLeft() != null) {
				insert(node, root.getLeft());
			}
			else {
				root.setLeft(node);
			}
		}
		//Recursion to go right in the tree
		if (node.getKey().compareTo(root.getKey()) > 0) {
			if (root.getRight() != null) {
				insert(node, root.getRight());
			}
			else {
				root.setRight(node);
			}
		}
		//Update height
		node.setHeight(setUpdatedHeight(node) + 1);
		int balance = getBF(node);
		//Left rotate
		if (balance > 1 && root.getKey().compareTo(node.getRight().getKey()) < 0) {
			rightRotate(node);
		}
		//Right rotate
		if (balance < -1 && root.getKey().compareTo(node.getLeft().getKey()) > 0) {
			leftRotate(node);
		}
		//Left right rotate
		if (balance > 1 && root.getKey().compareTo(node.getLeft().getKey()) > 0) {
			node.setLeft(leftRotate(node.getLeft()));
			rightRotate(node);
		}
		//Right left rotate
		if (balance < -1 && root.getKey().compareTo(node.getRight().getKey()) < 0) {	
			node.setRight(rightRotate(node.getRight()));
			leftRotate(node);
		}
	}		

	/**Search through the tree and find the correct node based on
	 * the given generic value of K, and either delete that node,
	 * or show that node with key K does not exist in the tree.
	 * @param key Generic value K to be found and deleted from the tree
	 * @throws IllegalArgumentException
	 */
	@Override
	public void delete(K key) throws IllegalArgumentException {
		if (key.equals(null)) {
			throw new IllegalArgumentException();
		}
                this.root = delete(this.root, key);
	}
	private BSTNode<K> delete(BSTNode<K> root, K key) {
		//less than root, go left
		if (key.compareTo(root.getKey()) < 0) {
			root.setLeft(delete(root.getLeft(), key));
		}
		//greater than root, go right
		else if (key.compareTo(root.getKey()) > 0) {
			root.setRight(delete(root.getRight(), key));
		}
		//same, then this is node to be deleted
		else {
			//One child or no children
			if (root.getLeft() == null || root.getRight() == null){
				BSTNode<K> node;
				if (root.getLeft() == null) {
					node = root.getRight();
				}
				else {
					node = root.getLeft();
				}
				//No children
				if (node == null) {
					node = root;
					root = null;
				}
				//One child
				else {
					root = node;
				}
			}
			else {
				//Swapping nodes and re-arranging pointers
				BSTNode<K> node = inOrderSucc(root.getRight());
				root.setKey(node.getKey());
				root.setRight(delete(root.getRight(), node.getKey()));
			}
		}
		//Restructuring tree below
		//Update height
		root.setHeight(setUpdatedHeight(root) + 1);
		//Checking if tree needs re-balancing
		int balanceFactor = getBF(root);
		//Right rotate
		if (balanceFactor < -1 && getBF(root.getRight()) <= 0) {
			leftRotate(root);
		}
		//Left rotate
		if (balanceFactor > 1 && getBF(root.getLeft()) >= 0) {
			rightRotate(root);
		}
		//Right left rotate
		if (balanceFactor < -1 && getBF(root.getRight()) > 0) {
			root.setRight(rightRotate(root.getRight()));
			leftRotate(root);
		}
		//Left right rotate
		if (balanceFactor > 1 && getBF(root.getLeft()) >= 0) {
			root.setLeft(leftRotate(root.getLeft()));
			rightRotate(root);
		}
		return root;	
	}

	/**Balancing method to restructure tree
	 * @return root node for that subtree
	 **/
	private BSTNode<K> leftRotate(BSTNode<K> root) {
		return root;
	}
	
	/**Balancing method to restructure tree
	 * @return root node for that subtree
	 **/
	private BSTNode<K> rightRotate(BSTNode<K> root){
		return root;
	}	
	/**Helper method to calculate height of a given node during 
	 * the return callback of stack recursion
	 * @param root given node to calculate height for
	 * @return int value for height
	 **/
	private int setUpdatedHeight(BSTNode<K> root) {
		int tempR = 0;
		int tempL = 0;
		if (root.getLeft() != null) {
			tempL = root.getLeft().getHeight();
		}
		if (root.getRight() != null) {
			tempR = root.getRight().getHeight();
		}
		return ((tempL > tempR) ? tempR : tempL);
	}

	 /**Given a node, this mathod calculates the balancing
	 * factor for the node. 
	 * @param root Given node to calculate BF for
	 * @return int representing BF
	 **/
	private int getBF(BSTNode<K> root) {
                int tempL = 0;
		int tempR = 0;
		if (root.getLeft() != null) {
			tempL = root.getLeft().getHeight();
		}
		if (root.getRight() != null) {
			tempR = root.getRight().getHeight();
		}
		return (tempL - tempR);
	}


	/**Method to get the in order successor for restructuring the tree
	 * @param root The right child of the node to calculate IOS from
	 * @return the IOS node
	 **/
	private BSTNode<K> inOrderSucc(BSTNode<K> root) {
		while (root.getLeft() != null) {
			root.setLeft(root);
		}
		return root;
	}

	/**Searches through the AVLTree to find a node given a key value.
	 * @params key Generic value to be located in the tree
	 * @return true if the node is found in the tree, false otherwise
	 * @throws IllegalArgumentException
	 */
	@Override
	public boolean search(K key) throws IllegalArgumentException {
		//Throws exception if key is null
		if (key.equals(null)) {
			throw new IllegalArgumentException();
		}
		//Checks if there are no nodes in the tree, returns false
		if (isEmpty()) {
			return false;
		}
		//Calls recursive helper method
		else {
			BSTNode<K> temp = search(key, root);
			if (temp == null) {
				return false;
			}
			else if (temp.getKey().equals(key)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/*Recursive helper method to traverse the tree and search for a node based on a given key value.
	 *@param key Generic value K to be searched for in the tree
	 *@param root BSTNode of type K to check key against to traverse tree
	 *@return a BSTNode<K> or null if no node is found
	 */
	private BSTNode<K> search(K key, BSTNode<K> root) {
	        //Base case for when key matches root node
		if (key.compareTo(root.getKey()) == 0) {
			return root;
		}
		//Recursive case for traversing left in the tree
		else if (key.compareTo(root.getKey()) < 0) {
			if (root.getLeft() != null) {
				return search(key, root.getLeft());
			}
		}
		//Recursive case for traversing right in the tree
		else {
			if (root.getRight() != null) {
				return search(key, root.getRight());
			}
		}
		//If key is not found, return null
		return null;
	}

	/**Travels tree with an in-order traversal and concatinates all the
	 * data into one string to be returned and printed.
	 * @return A string literal containing all of the data in the tree
	 * seperated by a space
	 */
	@Override
	public String print() {
		String toRet = "";
		if (root.equals(null)) {
			return toRet;
		}
		else {
			return print(root, toRet);
		}
	}
	

	/**Priavte helper method to recursively travel the tree in-order
	 * @param root Given root to be used in recursive method
	 * @param toRet String variable to store all of the data in 
	 * @return String containing data of the tree
	 **/
	private String print(BSTNode<K> root, String toRet) {
		//Go left until you can't anymore
		if (root.getLeft() != null) {
			print(root.getLeft(), toRet);
		}
		//If root is not null, add data from root to toRet
		if (root != null) {
			toRet += root.getKey().toString() + " ";
		}
		//Go right until you can't anymore
		if (root.getRight() != null) {
			print(root.getRight(), toRet);
		}
		return toRet;
	}

	/**Check to ensure tree maintains the height balancing properties of an AVL
	 * tree
	 * @return true if the height properties of the tree is correct
	 */
	@Override
	public boolean checkForBalancedTree() {
		return checkForBalancedTree(root);
	}
	private boolean checkForBalancedTree(BSTNode<K> root) {
		if (root.getLeft() != null) {
			checkForBalancedTree(root.getLeft());
		}
		if (getBF(root) < -1 || getBF(root) > 1) {
			return false;
		}
		if (root.getRight() != null) {
			checkForBalancedTree(root.getRight());
		}
		return true;
	}

	/**Checks tree to see if it maintains the properties of a BST
	 * @return true if it does, false otherwise
	 */
	@Override
	public boolean checkForBinarySearchTree() {
		return checkBST(root);
	}
	
	/**Private helper method to recursively check for BST properties
	 * @param root Root node to be used recursively in checking of BST properties
	 * @return true if BST properties hold
	 **/
	private boolean checkBST(BSTNode<K> root) {
		//checking for an empty tree, if its empty return true
		if (root == null) {
			return true;
		}
		//Base Case
		if (root.getLeft() == null && root.getRight() == null) {
			return true;
		}
		//If root is less than left child, return false
		if (root.getLeft() != null) {
			//If it is out of order, return false, otherwise recurse
			if (root.getKey().compareTo(root.getLeft().getKey()) < 0) {
				return false;
			}
			else {
				return checkBST(root.getLeft());
			}
		}	
		//If root is greater than right child, return false
		if (root.getRight() != null) {
			//If it is out of order, return false, otherwise recurse
			if (root.getKey().compareTo(root.getRight().getKey()) > 0) {
				return false;
			}	
			else {
				return checkBST(root.getRight());
			}
		}
		return false;
	}

}
