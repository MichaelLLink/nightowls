import rxtxrobot.*;

public class TakeTemp
{

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //unsure about port
        robot.connect();

        /*
        double thermistorReading = getThermistorReading();

        System.out.println("The probe read the value: " + thermistorReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));


        double temp = 0;
        double intercept = 687.9718646;
        double slope = -6.58594486;

        temp = (thermistorReading - intercept)/slope;

        System.out.println("The temperature is: " + temp + " celsius");
        */

        double anemometerReading = getAnemometerReading();
        double thermistorReading = getThermistorReading();

        double unshieldedTemp = 0;
        double shieldedTemp = 0;

        unshieldedTemp = (thermistorReading - exposedIntercept)/exposedSlope;
        shieldedTemp = (anemometerReading - shieldedIntercept)/shieldedSlope;

        //not entirely sure what we need here
        System.out.println("The shielded thermistor read the value: " + anemometerReading);
        System.out.println("In volts: " + (anemometerReading * (5.0/1023.0)));

        System.out.println("The exposed read the value: " + thermistorReading);
        System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        System.out.println("The difference is: " + (anemometerReading - thermistorReading));
        System.out.println("In temp: " + (shieldedTemp - unshieldedTemp));


        robot.close();
    }

    public static int getThermistorReading()
    {
        int sum = 0;
        int readingCount = 10;

        for(int i = 0; i < readingCount; i++)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(0).getValue();
            sum += reading;
        }

        return sum / readingCount;
    }

    public static int getAnemometerReading()
    {
        int sum = 0;
        int readingCount = 10;

        for(int i = 0; i < readingCount; i++)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(windPin).getValue();
            sum += reading;
        }

        return sum / readingCount;
    }

}
