public class TreeSerializer_Q1 implements TreeSerializer{

    public String serialize(Node root){

        // Create an ArrayList to store the node values in the traversal order
        StringBuilder resultString = new StringBuilder();  // Use StringBuilder for efficient string concatenation

        // Define a local inner class named Traverse for recursive traversal
        class Traverse {

            // Constructor for Traverse class taking a Node as an argument
            Traverse(Node currentNode) {

                resultString.append(currentNode.num);
                resultString.append(",");

                // If the current node has a left child, recursively traverse the left subtree
                if (currentNode.left != null) {
                    new Traverse(currentNode.left);
                }
                if (currentNode.left == null) {
                    resultString.append("#");
                    resultString.append(",");
                }
                if (currentNode.right == null) {
                    resultString.append("#");
                    resultString.append(",");
                }
                if (currentNode.right != null) {
                    new Traverse(currentNode.right);
                }
                // 1274###5##1#28##
            }
        }
        // Start the pre-order traversal by creating a new instance of Traverse with the root node
        new Traverse(root);

        // Return the results list containing the node values in the pre-order traversal order
        return resultString.toString();

    }

    static int counter;

    public Node deserialize(String str){

        if (str == null){
            return null;
        }
        String[] nodesArray = str.split(",");
        counter = 0;
        return aux(nodesArray);
    }

    public static Node aux(String[] nodesArray) {

        if (nodesArray[counter].equals("#")){
            return null;
        }
        Node root = new Node();
        root.num = Integer.parseInt(nodesArray[counter]);

        counter++;
        root.left = aux(nodesArray);

        counter++;
        root.right = aux(nodesArray);

        return root;
    }


    public static void main(String[] args) {

        // understand why works with searate files.
        Node root = new Node();
        root.num = 1;

        root.left = new Node();
        root.left.num = 2;
        root.right = new Node();
        root.right.num = 1;

        // left from 1: no kid
        root.right.left = null;

        // right from 1
        root.right.right = new Node();
        root.right.right.num = 28;
        // no kids from 28
        root.right.right.left = null;
        root.right.right.right = null;

        // left from 2
        root.left.left = new Node();
        root.left.left.num = 7;

        // no kid from 7
        root.left.left.right = null;

        root.left.left.left = new Node();
        root.left.left.left.num = 4;
        // no kids
        root.left.left.left.left = null;
        root.left.left.left.right = null;

        // right from 2
        root.left.right = new Node();
        root.left.right.num = 5;
        // no kids
        root.left.right.left = null;
        root.left.right.right = null;


        TreeSerializer_Q1 test = new TreeSerializer_Q1();
        String serializeToString = test.serialize(root);
        System.out.println(serializeToString);

        Node deserializeToNode = test.deserialize(serializeToString);
        System.out.println(deserializeToNode.num);
        System.out.println(deserializeToNode.left.num);
        System.out.println(deserializeToNode.right.num);
        System.out.println(deserializeToNode.right.right.num);


        String serializeBackToString = test.serialize(deserializeToNode);
        System.out.println(serializeBackToString);

        assert deserializeToNode.equals(root);

    }
}


















// import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.Queue;


// public class BinarySearchTree {

//     // The root node of the binary search tree
//     public Node root;

//     // The Node class represents a node in the binary search tree
//     public static class Node {
//         public int value;
//         public Node left;
//         public Node right;

//         // Constructor that takes an int value and sets the value of the node
//         private Node(int value) {
//             this.value = value;
//         }
//     }
//     // Get the root node of the binary search tree
//     public Node getRoot() {
//         return root;
//     }

//     public boolean insert(int value) {
//         Node newNode = new Node(value);
//         if (root == null) {
//             root = newNode;
//             return true;
//         }
//         Node temp = root;
//         while (true) {
//             if (newNode.value == temp.value) return false;
//             if (newNode.value < temp.value) {
//                 if (temp.left == null) {
//                     temp.left = newNode;
//                     return true;
//                 }
//                 temp = temp.left;
//             } else {
//                 if (temp.right == null) {
//                     temp.right = newNode;
//                     return true;
//                 }
//                 temp = temp.right;
//             }
//         }
//     }

//     public boolean contains(int value) {
//         if (root == null) return false;
//         Node temp = root;
//         while (temp != null) {
//             if (value < temp.value) {
//                 temp = temp.left;
//             } else if (value > temp.value) {
//                 temp = temp.right;
//             } else {
//                 return true;
//             }
//         }
//         return false;
//     }

    // public ArrayList<Integer> DFSPreOrder() {

    //     // Create an ArrayList to store the node values in the traversal order
    //     ArrayList<Integer> results = new ArrayList<>();

    //     // Define a local inner class named Traverse for recursive traversal
    //     class Traverse {
    //         // Constructor for Traverse class taking a Node as an argument
    //         Traverse(Node currentNode) {
    //             // Add the current node's value to the results list
    //             results.add(currentNode.value);
    //             // If the current node has a left child, recursively
    //             // traverse the left subtree
    //             if (currentNode.left != null) {
    //                 new Traverse(currentNode.left);
    //             }
    //             // If the current node has a right child, recursively
    //             // traverse the right subtree
    //             if (currentNode.right != null) {
    //                 new Traverse(currentNode.right);
    //             }
    //         }
    //     }

    //     // Start the pre-order traversal by creating a new
    //     // instance of Traverse with the root node
    //     new Traverse(root);

    //     // Return the results list containing the node values
    // // in the pre-order traversal order
    //     return results;
    // }


//     // public class Main {

//     public static void main(String[] args) {

//         BinarySearchTree myBST = new BinarySearchTree();

//         myBST.insert(47);
//         myBST.insert(21);
//         myBST.insert(76);
//         myBST.insert(18);
//         myBST.insert(27);
//         myBST.insert(52);
//         myBST.insert(82);

//         System.out.println("DFS PreOrder:");
//         System.out.println( myBST.DFSPreOrder() );

//         /*
//             EXPECTED OUTPUT:
//             ----------------
//             DFS PreOrder:
//             [47, 21, 18, 27, 76, 52, 82]

//         */

//     }

//     // }
// }