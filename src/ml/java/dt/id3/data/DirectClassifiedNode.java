package ml.java.dt.id3.data;

/**
 * Created by jull on 2015/1/28.
 */
public class DirectClassifiedNode extends InfoGainTreeNode {

    private String label = null;

    public DirectClassifiedNode(String label) {
        super(null);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void printNodeFeatureName() {
        System.out.println("Node Label Name: " + label);
    }

    public String printTree(int rootNodeLevel) {
        String prefix = "";
        for (int i = 0; i < rootNodeLevel; i++) {
            prefix += "    ";
        }

        String printOutStr = prefix + "[level " + rootNodeLevel + "] Label: " + label + "\n";
        return printOutStr;
    }
}
