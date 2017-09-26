include rxtxrobot.*;

public class Move
{

    public static int main()
    {
            //set up arduino
        RXTXRobot robot = new ArduinoUno();
        robot.setPort(/dev/tty.usbmodem411); //mac port???
        robot.connect();

            //motors 1 and 2 are already set up so we don't need to do that

        //distance in inches
        int distance = 3;

            //set ticks (11 being our ticks-per-inches ratio)
        int ticks = 11 * distance;

            //move 3 feet
        runEncodedMotor(robot.MOTOR1, 500, ticks, robot.MOTOR2, 500, ticks);

            //close robot
        robot.close();


    }

}
