import rxtxrobot.*;

public class TakeTemp
{

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        robot = new ArduinoUno();
        robot.setPort("COM3"); //unsure about port
        robot.connect();

        int thermistorReading = getThermistorReading();

        System.out.println("The probe read the value: " + thermistorReading);
        System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        /*
        int tempK = 0;
        int tempC = 0;
        int interceptK =
        int slopeK =
        int interceptC =
        int slopeC =

        tempK = (thermistorReading - interceptK)/slopeK;
        tempC = (thermistorReading - interceptC)/slopeC;

        System.out.println("The temperature in CELSIUS is: " + tempC);
        System.out.println("The temperature in KELVIN is: " + tempK);

        */

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
