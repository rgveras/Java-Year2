import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// An AVL tree implementation
public class AVLTree<Key extends Comparable<Key>, Value> {
	public static boolean check = false;		// Check variable to identify if a key is in the tree

    private Node root;  // root of the AVL tree

    // A node of the AVL tree
    private class Node {
        private Key key;           // key of the node
        private Value value;       // value of the node
        private Node left, right;  // left and right subtrees of the node
        private int height;        // height of the node

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }
    

    // Returns the height of the node (or 0 if node is null)
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    
    // Returns the balance factor of the node
    private int balanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    
    // Rotates the node to the left
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return y;
    }

    
    // Rotates the node to the right
    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return y;
    }

    
    // Balances the subtree rooted at the given node
    private Node balance(Node node) {
        if (node == null) {
            return null;
        }
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        else if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }
        else {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
        return node;
    }

    
    // Returns the value associated with the given key
    public Value get(Key key) {
        Node node = get(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }
    
    
    // Returns the node associated with the given key
    private Node get(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        }
        else if (cmp > 0) {
            return get(node.right, key);
        }
        else {
            return node;
        }
    }

    
    // Inserts the key-value pair into the AVL tree
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    
    // Inserts the key-value pair into the subtree rooted at the given node
    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            return new Node(key,value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        node = balance(node);
        return node;
    }


    // Traverse the tree in-order, printing each nodes key and value
    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.key + " " + node.value);
            inOrderTraversal(node.right);
        }
    }

    
    // Call this method from main:
    public void inOrderTraversal() {
        inOrderTraversal(root);
    }
    
    
    // Return root of tree
    public Node getRoot() {
    	return root;
    }

    
    // Check if the word stored in parameter "key" is in the tree of variable "node", returning true if it is, false otherwise
    public boolean inOrder(String key, Node node) {

    	if(node != null) {
    		
    		// If the word has not been found, continue comparing variable "key" with elements from the tree
    		while(!check) {
    			String nodeKey = node.key.toString();
    			
    			if(nodeKey.toLowerCase().compareTo(key) == 0) {
    				check = true;								// If the word is in the tree, check is set to true and returned
    				return check;
    			}
    			
    			// Continue checking each node of the tree for the word stored in parameter "key"
    			else if (!check){

    				inOrder(key, node.left);
    				if(check) return check;		
    				inOrder(key, node.right);
    				if(check) return check;

    				return check=false;
    			}
    			break;
    		}
}
    	
    	return check;
    }
    

    // Create a list of node keys using a stack 
    private List<String> returnNodeList(Node root) {
    	List<String> nodelist = new ArrayList<String>();
    	Stack<Node> inorder = new Stack<Node>();
        
    	// While there are still elements, add them to the stack
    	while (root != null | inorder.empty() != true) {
    		while(root != null) {
    			inorder.push(root);
                root = root.left;
            }
            
            root = inorder.peek();
            inorder.pop();
            nodelist.add((String) root.key);	// Add stack elements' keys to the list
            root = root.right;
        }
        return nodelist;
    }

    
    // Call this method from main:
    public List<String> returnNodeList() {
    	return returnNodeList(root);
    }

    
    public static void main(String[] args) {
        AVLTree<Integer, String> tree = new AVLTree<>();

        tree.put(5, "apple");
        tree.put(2, "banana");
        tree.put(8, "orange");
        tree.put(1, "pear");
        tree.put(4, "grape");
        tree.put(7, "kiwi");
        tree.put(9, "pineapple");
        tree.put(3, "mango");
        tree.put(6, "peach");

        System.out.println("Inorder traversal:");
        tree.inOrderTraversal();

        System.out.println("Get value at key 4: " + tree.get(4));
    }
}
