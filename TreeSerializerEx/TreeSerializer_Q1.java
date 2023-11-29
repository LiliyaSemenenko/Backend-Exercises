public class TreeSerializer_Q1 implements TreeSerializer{

    public String serialize(Node root){

        StringBuilder resultString = new StringBuilder();

        // Define a local inner class for recursive traversal
        class Traverse {

            // Constructor for Traverse class
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
                // If the current node has a right child, recursively traverse the left subtree
                if (currentNode.right != null) {
                    new Traverse(currentNode.right);
                }
            }
        }
        new Traverse(root);

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

        // Create a Tree
        Node root = new Node();
        root.num = 1;

        root.left = new Node();
        root.left.num = 2;
        root.right = new Node();
        root.right.num = 1;

        // left from 1: no child
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

        // no child from 7
        root.left.left.right = null;

        root.left.left.left = new Node();
        root.left.left.left.num = 4;
        // no children
        root.left.left.left.left = null;
        root.left.left.left.right = null;

        // right from 2
        root.left.right = new Node();
        root.left.right.num = 5;
        // no children
        root.left.right.left = null;
        root.left.right.right = null;

        TreeSerializer_Q1 test = new TreeSerializer_Q1();
        String serializeToString = test.serialize(root);

        Node deserializeToNode = test.deserialize(serializeToString);

        String serializeBackToString = test.serialize(deserializeToNode);
        System.out.println(serializeBackToString);

        assert deserializeToNode.equals(root);
    }
}