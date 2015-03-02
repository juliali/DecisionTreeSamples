package ml.java.dt.id3.main;

import ml.java.dt.id3.data.DirectClassifiedNode;
import ml.java.dt.id3.data.InfoGainTreeNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by jull on 2015/2/5.
 */
public class ID3Predicter {

    public ID3Predicter() {

    }

    public String predict(InfoGainTreeNode dTree, Map<String, String> featureValueMap) {
        InfoGainTreeNode node = dTree;
        String dividedFeatureName = dTree.getRootFeatureName();

        while(node != null && !(node instanceof DirectClassifiedNode)) {
            String featureValue = featureValueMap.get(dividedFeatureName);
            InfoGainTreeNode child = node.getChild(featureValue);

            if (child != null) {
                node = child;
            }
        }

        if (node == null) {
            System.err.println("Error Cannot get the result!");
            return null;
        } else {
            String resultLabel = ((DirectClassifiedNode) node).getLabel();
            System.out.println("The prediction results is: " + resultLabel);
            return resultLabel;
        }
    }

    public static void main(String[] args) {

    }
}
