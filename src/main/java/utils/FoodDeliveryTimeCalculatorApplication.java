package utils;

import calculator.Calculator;
import exceptions.UnrecoverableException;
import modules.MainModule;
import com.google.inject.Injector;

import static com.google.inject.Guice.createInjector;

public class FoodDeliveryTimeCalculatorApplication {
    public static void execute() {

        String inputFilePath = "Request.json";

        Injector injector = createInjector(
                new MainModule()
        );

        Calculator calculator = injector.getInstance(Calculator.class);

        try {
            calculator.calculateDeliveryTime(inputFilePath);
        } catch (UnrecoverableException ure) {
            System.out.println("Exception Occurred: " + ure.getMessage());
        } finally {
            System.out.println("Calculation finished");
        }
    }
}
