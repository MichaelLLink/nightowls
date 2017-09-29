import rxtxrobot.*;

public class SenseDistance {

    public static RXTXRobot robot;

    public static void main(String[] args) {

            //set up robot
        robot = new ArduinoUno();
        robot.setPort("COM3"); //unsure about port
        robot.connect();

            //take distance
        robot.refreshDigitalPins();
        int distance = robot.getPing(7); //remember to check pin

            //read out distance
        System.out.println("Distance: " + distance + "cm");

            //close robot
        robot.close();

    }
}
