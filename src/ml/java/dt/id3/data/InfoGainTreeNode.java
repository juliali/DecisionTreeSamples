package ml.java.dt.id3.data;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by jull on 2015/1/28.
 */
public class InfoGainTreeNode {

    private String rootFeatureName = null;

    private Map<String, InfoGainTreeNode> children = null;

    public InfoGainTreeNode(String featureName) {
        this.rootFeatureName = featureName;
    }

    public String getRootFeatureName() {
        return this.rootFeatureName;
    }

    public InfoGainTreeNode getChild(String featureValue) {
        if (children == null) {
            return null;
        }

        return this.children.get(featureValue);
    }

    public void setChild(String condition, InfoGainTreeNode child) {
        if (this.children == null) {
            this.children = new HashMap<String, InfoGainTreeNode>();
        }
        this.children.put(condition, child);
    }

    public void printNodeFeatureName() {
        System.out.println("Node Feature Name: " + rootFeatureName);
    }

    public String printTree(int rootNodeLevel) {
        String prefix = "";
        for (int i = 0; i < rootNodeLevel; i ++) {
            prefix += "    ";
        }

        String printOutStr = prefix + "[level " + rootNodeLevel + "] Feature: " + rootFeatureName + "\n";

        Set<String> keySet = children.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String condition = iter.next();
            printOutStr += prefix + "[level " + rootNodeLevel + " -> " + (rootNodeLevel + 1) +"] condition: " + condition + "\n";
            InfoGainTreeNode node = children.get(condition);
            try {
                printOutStr += prefix + node.printTree(rootNodeLevel + 1) + "\n";
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        return printOutStr;
    }
}
