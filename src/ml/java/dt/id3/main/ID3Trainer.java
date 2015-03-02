package ml.java.dt.id3.main;

import ml.java.dt.id3.data.DirectClassifiedNode;
import ml.java.dt.id3.data.InfoGainTreeNode;
import ml.java.dt.id3.data.TrainingDataSet;
import ml.java.dt.id3.utils.DataSetReader;

import java.util.*;

/**
 * Created by jull on 2015/1/28.
 */
public class ID3Trainer {

    private InfoGainTreeNode rootNode = null;
    private Map<String, Double> labelProbabilityMap = null;

    public ID3Trainer() {
    }

    private boolean isLabelIdentical(String[] labelArray) {
        String label1 = labelArray[0];

        for (int i = 1; i < labelArray.length; i++) {
            if (!label1.equals(labelArray[i])) {
                return false;
            }
        }
        return true;
    }

    private Map<String, Integer> getCountMap(String[] array) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();

        for (int i = 0; i < array.length; i++) {
            String label = array[i];
            if (label == null) {
                System.out.println("index: " + i);
            }
            Integer count = countMap.get(label);
            if (count == null) {
                countMap.put(label, 1);
            } else {
                countMap.put(label, count + 1);
            }
        }
        return countMap;
    }

    private Map<String, Double> getProbabilityMap(String[] array) {
        Map<String, Integer> countMap = getCountMap(array);
        Set<String> keySet = countMap.keySet();
        Iterator<String> iter = keySet.iterator();
        int totalCount = array.length;

        Map<String, Double> probabilityMap = new HashMap<String, Double>();
        while (iter.hasNext()) {
            String label = iter.next();
            int count = countMap.get(label);
            Double probability = (double) count / (double) totalCount;
            probabilityMap.put(label, probability);
        }

        return probabilityMap;
    }

    private double countInfo(Map<String, Double> probabilityMap) {
        double result = 0.0;
        Set<String> keySet = probabilityMap.keySet();
        Iterator<String> iter = keySet.iterator();

        while (iter.hasNext()) {
            String label = iter.next();
            Double probability = probabilityMap.get(label);

            result += (0.0 - probability) * Math.log(probability);
        }

        return result;
    }

    private double countInfo(String[] array) {
        Map<String, Double> probabilityMap = getProbabilityMap(array);
        double result = countInfo(probabilityMap);
        return result;
    }


    private double countFeatureInfo(String[] featureArray, String[] labelArray) {
        Map<String, Double> featureValueEnumProbabilityMap = getProbabilityMap(featureArray);

        Set<String> keySet = featureValueEnumProbabilityMap.keySet();
        Iterator<String> iter = keySet.iterator();

        double featureInfo = 0.0;
        while (iter.hasNext()) {
            String featureValue = iter.next();
            double probabilityForFeature = featureValueEnumProbabilityMap.get(featureValue);

            List<String> labelForFeatureValue = new ArrayList<String>();
            for (int i = 0; i < labelArray.length; i++) {
                if (featureArray[i].equals(featureValue)) {
                    labelForFeatureValue.add(labelArray[i]);
                }
            }

            String[] labelArrayForFeatureValue = new String[labelForFeatureValue.size()];
            labelArrayForFeatureValue = labelForFeatureValue.toArray(labelArrayForFeatureValue);
            double infoForFeature = countInfo(labelArrayForFeatureValue);

            featureInfo += infoForFeature * probabilityForFeature;
        }

        return featureInfo;
    }

    private int getMaxIndex(double[] array) {
        int maxIndex = 0;

        for (int i = 1; i < array.length; i++) {
            if (array[i] >= array[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    // ---------------------------------------------------------------------------------------------
    private static String getDefaultLabel(TrainingDataSet tds) {
        Map<String, Integer> labelCountMap = new HashMap<String, Integer>();
        String[] labelArray = tds.getLabelArray();
        for (int i = 0; i < labelArray.length; i ++) {
            String label = labelArray[i];
            Integer num = labelCountMap.get(label);
            if (num == null) {
                labelCountMap.put(label, 1);
            } else {
                labelCountMap.put(label, num + 1);
            }
        }
        String defaultLabel = labelArray[0];
        int maxCount = 0;
        Set<String> keySet = labelCountMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()) {
            String label = iter.next();
            int count = labelCountMap.get(label);
            if (count > maxCount) {
                maxCount = count;
                defaultLabel = label;
            }
        }

        return defaultLabel;
    }

    private double countInfoD(TrainingDataSet tds) {
        String[] labelArray = tds.getLabelArray();
        double result = countInfo(labelArray);
        return result;
    }

    private double[] countInfoDForFeatures(TrainingDataSet tds) {
        int featureNumber = tds.getFeatureNumber();
        double[] results = new double[featureNumber];

        String[] labelArray = tds.getLabelArray();

        for (int i = 0; i < featureNumber; i++) {
            String[] featureArray = tds.getFeatureArray(i);
            results[i] = countFeatureInfo(featureArray, labelArray);
        }

        return results;
    }

    private int getTopGainIndex(double infoD, double[] infoDForFeatures) {
        double[] gainForFeatures = new double[infoDForFeatures.length];
        for (int i = 0; i < infoDForFeatures.length; i++) {
            gainForFeatures[i] = infoD - infoDForFeatures[i];
        }

        return getMaxIndex(gainForFeatures);
    }

    private int getDividePointFeatureIndex(TrainingDataSet tds) {
        double infoD = countInfoD(tds);
        double[] infoDForFeatures = countInfoDForFeatures(tds);

        int dividePointFeatureIndex = getTopGainIndex(infoD, infoDForFeatures);
        return dividePointFeatureIndex;
    }

    private String[] getNewArrayWithoutIndexedOne(String[] origArray, int indexToRemove) {
        String[] newArray = new String[origArray.length - 1];
        int n = 0;
        for (int i = 0; i < origArray.length; i++) {
            if (i != indexToRemove) {
                newArray[n++] = origArray[i];
            }
        }
        return newArray;
    }

    private TrainingDataSet getRenewTrainingSubDataSet(TrainingDataSet origTds, String[] currentFeatures) {
        Set<String> cfSet = new HashSet<String>();
        for (int i = 0; i < currentFeatures.length; i ++) {
            cfSet.add(currentFeatures[i]);
        }

        List<Integer> featureColumnIndexes = new ArrayList<Integer>();
        String[] origFeatures = origTds.getFeatureNameArray();

        for (int i = 0; i < origFeatures.length; i ++ ) {
            if (cfSet.contains(origFeatures[i])) {
                featureColumnIndexes.add(i);
            }
        }

        int newFeatureNumber = featureColumnIndexes.size();
        int rowNumber = origTds.getRowNumber();
        TrainingDataSet newTds = new TrainingDataSet(rowNumber);
        newTds.setFeatureNameArray(currentFeatures);

        for (int i = 0; i < rowNumber; i ++) {
            String[] origFeatureValues = origTds.getFeatureValuesForARow(i);
            String[] newFeatureValues = new String[newFeatureNumber];
            int n = 0;
            for (int columnNum : featureColumnIndexes) {
                newFeatureValues[n ++] = origFeatureValues[columnNum];
            }
            newTds.setData(i, origTds.getLabel(i), newFeatureValues);
        }

        return newTds;
    }

    private TrainingDataSet getSubTrainingDataSet(TrainingDataSet tds, int dividedIndex, String condition) {
        List<Integer> rowIndexList = new ArrayList<Integer>();

        String[] featureArray = tds.getFeatureArray(dividedIndex);
        for (int i = 0; i < featureArray.length; i++) {
            if (featureArray[i].equals(condition)) {
                rowIndexList.add(i);
            }
        }

        String[] featureNameArray = tds.getFeatureNameArray();
        String[] newFeatureNames = getNewArrayWithoutIndexedOne(featureNameArray, dividedIndex);

        if (newFeatureNames.length > 0) {
            int rowNumber = rowIndexList.size();
            int columnNumber = tds.getFeatureNumber() - 1;

            TrainingDataSet newTds = new TrainingDataSet(rowNumber);
            newTds.setFeatureNameArray(newFeatureNames);

            int rowNum = 0;
            for (int rowIndex : rowIndexList) {
                String[] featuresForTheRow = tds.getFeatureValuesForARow(rowIndex);
                String label = tds.getLabel(rowIndex);
                String[] newFeatures = getNewArrayWithoutIndexedOne(featuresForTheRow, dividedIndex);

                newTds.setData(rowNum++, label, newFeatures);
            }

            return newTds;
        } else {
            return null;
        }
    }

    private InfoGainTreeNode createTreeMethod(TrainingDataSet tds, String defaultLabel, int conditionNumber, TrainingDataSet origTds) {
        if (tds == null) {
            return null;
        }

        if (labelProbabilityMap == null) {
            labelProbabilityMap = this.getProbabilityMap(tds.getLabelArray());
        }

        String[] labelArray = tds.getLabelArray();
        if (isLabelIdentical(labelArray)) {
            return new DirectClassifiedNode(labelArray[0]);
        }

        if (tds.getFeatureNumber() == 1) {
            InfoGainTreeNode lastFeatureNode = new InfoGainTreeNode(tds.getFeatureName(0));
            TrainingDataSet newSet = getRenewTrainingSubDataSet(origTds, tds.getFeatureNameArray());
            String[] featureValues = newSet.getFeatureArray(0);
            String[] labels = newSet.getLabelArray();
            int rn = newSet.getRowNumber();

            Map<String, Map<String, Integer>> lastFeatureDecideMap = new HashMap<String, Map<String, Integer>>();

            for (int i = 0; i < rn; i ++) {
                String cFeature = featureValues[i];
                String cLabel = labels[i];
                int count = 0;
                Map<String, Integer> cmap = lastFeatureDecideMap.get(cFeature);
                if (cmap == null) {
                    cmap = new HashMap<String, Integer>();
                }
                if (cmap.get(cLabel) != null) {
                    count = cmap.get(cLabel) + 1;
                } else {
                    count = 1;
                }

                cmap.put(cLabel, count);
                lastFeatureDecideMap.put(cFeature, cmap);
            }

            Set<String> keySet = lastFeatureDecideMap.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String condition = iter.next();
                Map<String, Integer> cmap = lastFeatureDecideMap.get(condition);
                Set<String> ks = cmap.keySet();
                Iterator<String> iterator = ks.iterator();
                String maxLabel = null;
                int maxCount = 0;
                while (iterator.hasNext()) {
                    String label = iterator.next();
                    int count = cmap.get(label);
                    if (maxCount < count) {
                        maxCount = count;
                        maxLabel = label;
                    }
                }

                DirectClassifiedNode dNode = new DirectClassifiedNode(maxLabel);
                lastFeatureNode.setChild(condition, dNode);
            }
            return lastFeatureNode;
        }

        int dividFeatureIndex = getDividePointFeatureIndex(tds);
        String dividFeatureName = tds.getFeatureName(dividFeatureIndex);
        InfoGainTreeNode node = new InfoGainTreeNode(dividFeatureName);

        String[] divideFeatureArray = tds.getFeatureArray(dividFeatureIndex);
        Map<String, Integer> divideFeatureValueMap = getCountMap(divideFeatureArray);
        if (divideFeatureValueMap.size() == conditionNumber) {
            Set<String> keySet = divideFeatureValueMap.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String condition = iter.next();

                TrainingDataSet subSet = getSubTrainingDataSet(tds, dividFeatureIndex, condition);

                InfoGainTreeNode subNode = createTreeMethod(subSet, defaultLabel, conditionNumber, origTds);
                if (subSet == null) {
                    subNode = new DirectClassifiedNode(defaultLabel + " (default)");
                }
                node.setChild(condition, subNode);
            }
        } else {
            String[] currentFeatures = tds.getFeatureNameArray();
            TrainingDataSet newSet = getRenewTrainingSubDataSet(origTds, currentFeatures);
            node = createTreeMethod(newSet, defaultLabel, conditionNumber, origTds);
        }
        return node;
    }

    public InfoGainTreeNode induceID3Tree(TrainingDataSet tds) {
        ID3Trainer id3Trainer = new ID3Trainer();
        String defaultLabel = ID3Trainer.getDefaultLabel(tds);
        InfoGainTreeNode dcTree = id3Trainer.createTreeMethod(tds, defaultLabel, 2, tds);

        System.out.println("\n\n ===================================");
        String printStr = dcTree.printTree(0);
        System.out.println(printStr);

        return dcTree;
    }

    public static void main(String[] args) {
        String filePath = "F:\\Projects\\Java\\mlsamples\\src\\resources\\girlcat2.csv";
        TrainingDataSet tds = DataSetReader.readTrainingSetData(filePath);

        ID3Trainer id3Trainer = new ID3Trainer();
        id3Trainer.induceID3Tree(tds);
    }
}
