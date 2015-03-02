package ml.java.dt.id3.utils;

import java.io.*;

/**
 * Created by jull on 2015/1/28.
 */
public class FileIO {

    public static String readTextFile(String fileName) {

        String returnValue = "";
        FileReader file = null;

        try {
            file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                returnValue += line + "\n";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            returnValue = null;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    returnValue = null;
                }
            }
        }
        return returnValue;
    }

    public static void writeTextFile(String fileName, String s) {
        FileWriter output = null;
        BufferedWriter writer = null;
        try {
            output = new FileWriter(fileName);
            writer = new BufferedWriter(output);
            writer.write(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
