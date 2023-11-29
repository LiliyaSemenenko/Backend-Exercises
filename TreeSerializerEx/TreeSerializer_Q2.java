import java.util.HashSet;
import java.util.Set;

public class TreeSerializer_Q2 implements TreeSerializer{

    public String serialize(Node root){

        StringBuilder resultString = new StringBuilder();
        Set<Node> seenNodes = new HashSet<>();

        class Traverse {

            // Constructor for Traverse class
            Traverse(Node currentNode) {

                if (seenNodes.contains(currentNode)) {
                    throw new RuntimeException("This is a cyclic tree.");
                }
                seenNodes.add(currentNode);

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
                // If the current node has a right child, recursively traverse the left subtree
                if (currentNode.right != null) {
                    new Traverse(currentNode.right);
                }
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
        // no kid
        root.left.right.left = null;
        // cyclic part
        // root.left.right.right = root.right;
        root.left.right.right =  null;

        TreeSerializer_Q2 test = new TreeSerializer_Q2();
        String serializeToString = test.serialize(root);
        System.out.println(serializeToString);

        Node deserializeToNode = test.deserialize(serializeToString);

        String serializeBackToString = test.serialize(deserializeToNode);
        System.out.println(serializeBackToString);

        assert deserializeToNode.equals(root);

    }
}