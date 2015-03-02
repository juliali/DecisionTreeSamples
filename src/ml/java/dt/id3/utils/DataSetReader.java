package ml.java.dt.id3.utils;

import ml.java.dt.id3.data.TrainingDataSet;

/**
 * Created by jull on 2015/1/28.
 */
public class DataSetReader {

    public static TrainingDataSet readTrainingSetData(String filePath) {
        String content = FileIO.readTextFile(filePath);
        String[] tmps = content.split("\n");
        int trainingsetSize = tmps.length - 1;

        TrainingDataSet ds = new TrainingDataSet(trainingsetSize);

        for (int i = 0; i < trainingsetSize + 1; i++) {
            String line = tmps[i];
            String[] strs = line.split(",");
            String label = strs[0];

            int featureNumber = strs.length - 1;

            String[] features = new String[featureNumber];
            for (int j = 0; j < featureNumber; j++) {
                features[j] = strs[j + 1];//Convertor.convertStringToInt(strs[j + 1]);
            }

            if (i == 0) {
                ds.setFeatureNameArray(features);
            } else {
                ds.setData(i -1, label, features);
            }
        }
        return ds;
    }
}
