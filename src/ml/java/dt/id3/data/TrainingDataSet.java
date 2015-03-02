package ml.java.dt.id3.data;

/**
 * Created by jull on 2015/1/28.
 */
public class TrainingDataSet {

    private String[][] featureMatrix = null;
    private String[] labelArray = null;
    private String[] featureNameArray = null;
    private int rowNumber = -1;
    private int featureNumber = -1;

    public TrainingDataSet(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setData(int trainingsetIndex, String label, String[] features) {
        if (this.featureNumber == -1) {
            this.featureNumber = features.length;
        }

        if (this.featureMatrix == null) {
            this.featureMatrix = new String[rowNumber][featureNumber];
        }

        if (this.featureNameArray == null) {
            this.featureNameArray = new String[featureNumber];
        }

        if (this.labelArray == null) {
            this.labelArray = new String[rowNumber];
        }

        this.labelArray[trainingsetIndex] = label;
        this.featureMatrix[trainingsetIndex] = features;
    }

    public String[][] getFeatureMatrix() {
        return featureMatrix;
    }

    public String[] getFeatureValuesForARow(int rowIndex) {
        return featureMatrix[rowIndex];
    }

    public String[] getLabelArray() {
        return labelArray;
    }

    public String getLabel(int rowIndex) {
        return this.labelArray[rowIndex];
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getFeatureNumber() {
        return featureNumber;
    }

    public String[] getFeatureArray(int featureIndex) {
        String[] featureArray = new String[rowNumber];
        for (int i = 0; i < rowNumber; i ++) {
            featureArray[i] = featureMatrix[i][featureIndex];
        }
        return featureArray;
    }

    public String[] getFeatureNameArray() {
        return featureNameArray;
    }

    public String getFeatureName(int index) {
        return featureNameArray[index];
    }

    public void setFeatureNameArray(String[] featureNameArray) {
        this.featureNameArray = featureNameArray;
    }
}
