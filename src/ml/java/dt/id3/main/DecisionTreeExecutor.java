package ml.java.dt.id3.main;

import ml.java.dt.id3.data.InfoGainTreeNode;
import ml.java.dt.id3.data.TrainingDataSet;
import ml.java.dt.id3.utils.DataSetReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jull on 2015/2/28.
 */
public class DecisionTreeExecutor {

    private InfoGainTreeNode dcTree = null;
    private String[] featureNames = null;

    public DecisionTreeExecutor(String filePath) {
        TrainingDataSet tds = DataSetReader.readTrainingSetData(filePath);

        ID3Trainer id3Trainer = new ID3Trainer();
        this.dcTree = id3Trainer.induceID3Tree(tds);
        this.featureNames = tds.getFeatureNameArray();
    }

    public void predictOne(String[] featuresForOneRecord) {
        if (featuresForOneRecord == null || featuresForOneRecord.length == 0) {
            return;
        }

        if (featuresForOneRecord.length != this.featureNames.length) {
            System.out.println("Wrong features.");
            return;
        }

        Map<String, String> featureValueMap = new HashMap<String, String>();
        for (int i = 0; i <featuresForOneRecord.length; i ++) {
            featureValueMap.put(featureNames[i], featuresForOneRecord[i]);
        }

        ID3Predicter id3Predicter = new ID3Predicter();
        String label = id3Predicter.predict(dcTree, featureValueMap);

    }

    public static void main(String[] args) {
        String filePath = "F:\\Projects\\Java\\mlsamples\\src\\resources\\girlcat2.csv";
        String helloKittyFeatureValueStr = "Yes,Yes,Yes,Yes,Yes,Yes,Yes";

        String[] helloKittyFeatureValues = helloKittyFeatureValueStr.split(",");

        DecisionTreeExecutor decisionTreeExecutor = new DecisionTreeExecutor(filePath);
        decisionTreeExecutor.predictOne(helloKittyFeatureValues);
    }
}
