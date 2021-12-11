package utils;


import exceptions.UnrecoverableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

    public static String readJsonString(String inputFilePath) throws UnrecoverableException {
        ClassLoader classLoader = FoodDeliveryTimeCalculatorApplication.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(inputFilePath);

        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new UnrecoverableException("Exception occurred while reading Json Input file: " + e.getMessage());
        }

        return resultStringBuilder.toString();
    }
}
