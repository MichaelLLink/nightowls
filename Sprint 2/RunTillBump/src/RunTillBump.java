import rxtxrobot.*;

public class RunTillBump {

    public static int main()
    {
        boolean bumpTriggered = false;


        //set up arduino
        RXTXRobot robot = new ArduinoUno();
        robot.setPort(/dev/tty.usbmodem411); //mac port???
        robot.connect();

        while (!bumpTriggered)
        {
            //check bump sensor, which would be in a DIGITAL port
            robot.refreshDigitalPins();
            int reading = robot.getDigitalPin(11).getValue(); //IDK what value signifies not pushed

            //add if statement to set bumpTriggered based on reading

            if(!bumpTriggered)
            {
                //run motor
                //runEncodedMotor(robot.MOTOR1, 500, 0, robot.MOTOR2, 500, 0);  //if you can't call an encoded motor directly, we gotta figure out how to use this
                runMotor(robot.MOTOR1, 500, 0, robot.MOTOR2, 500, 0);
            }

        }
        //close robot
        robot.close();


    }

}
