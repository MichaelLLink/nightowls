import rxtxrobot.*;

public class Move
{

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        robot = new ArduinoUno();
        robot.setPort(""); //make sure to update port
        robot.connect();

            //motors 1 and 2 are already set up so we don't need to do that

        //distance in inches
        int distance = 3;

            //set ticks (11 being our ticks-per-inches ratio)
        int ticks = 11 * distance;

            //move 3 feet
        robot.runEncodedMotor(RXTXRobot.MOTOR1, 500, ticks, RXTXRobot.MOTOR2, 500, ticks);

            //close robot
        robot.close();


    }

}
