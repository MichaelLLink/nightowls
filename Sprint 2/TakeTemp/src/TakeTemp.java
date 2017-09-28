import rxtxrobot.*;

public class TakeTemp
{

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //unsure about port
        robot.connect();

        double thermistorReading = getThermistorReading();

        System.out.println("The probe read the value: " + thermistorReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));


        double temp = 0;
        double intercept = 687.9718646;
        double slope = -6.58594486;

        temp = (thermistorReading - intercept)/slope;

        System.out.println("The temperature is: " + temp + " celsius");



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

}
