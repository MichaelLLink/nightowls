import rxtxrobot.*;
import java.util.Scanner;

public class ChangeAngle
{

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        int angle = 0;
        angle = input.nextInt();

            //set up robot
        robot = new ArduinoUno();
        robot.setPort("COM3"); //unsure about port
        robot.connect();

            //set up servo motors
        robot.attachServo(RXTXRobot.SERVO1, 9);

            //move servo
        robot.moveServo(RXTXRobot.SERVO1, angle);
        //robot.moveServo(RXTXRobot.SERVO1, 90);

            //reset servo
        robot.moveServo(RXTXRobot.SERVO1, 0);

            //close robot
        robot.close();


    }
}
