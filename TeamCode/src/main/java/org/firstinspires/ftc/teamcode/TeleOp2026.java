package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

@TeleOp(name="test")
public class TeleOp2026 extends OpMode {

    private DcMotor flMotor = null;
    private DcMotor frMotor = null;
    private DcMotor blMotor = null;
    private DcMotor brMotor = null;
    private double flWeight = 1;
    private Servo linkServo = null;
    private double frWeight = 1;
    private double blWeight = 1;
    private double brWeight = 1;
    private boolean change = true;
    int targetAngle = 0;
    private boolean turned = true;
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    private double sensitivity(double A) {
        return (Math.log(A*A*10+1)/11.3022167793+Math.abs(A)/90)*Math.signum(A)/3  /  2;
    }

    @Override
    public void init() {
        flMotor = hardwareMap.get(DcMotor.class, "flMotor");
        frMotor = hardwareMap.get(DcMotor.class, "frMotor");
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        blMotor = hardwareMap.get (DcMotor.class, "blMotor");
        brMotor = hardwareMap.get(DcMotor.class, "brMotor");
        blMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        brMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        linkServo = hardwareMap.get(Servo.class, "Linkage");
        linkServo.setDirection(Servo.Direction.FORWARD);
        linkServo.scaleRange(0.5,0.8);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        composeTelemetry();

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 10);
    }

    @Override
    public void loop() {
        double flPower;
        double frPower;
        double blPower;
        double brPower;

        double yMovement = gamepad1.right_stick_y;
        double xMovement = gamepad1.right_stick_x;
        yMovement = xMovement>0.4?0:yMovement;
        xMovement = yMovement>0.4?0:xMovement;
        double twist = (gamepad1.left_stick_x)/10-sensitivity(angles.firstAngle- targetAngle);

        flPower = yMovement-xMovement+twist;
        frPower = yMovement+xMovement-twist;
        blPower = -yMovement-xMovement-twist;
        brPower = -yMovement+xMovement+twist;

        flMotor.setPower(flPower*flWeight*0.52);
        frMotor.setPower(frPower*frWeight*0.52);
        blMotor.setPower(blPower*blWeight*0.52);
        brMotor.setPower(brPower*brWeight*0.52);

        if ((gamepad1.right_trigger>0.6 && targetAngle<76) && turned) {
            targetAngle+=15;
            turned = false;
        }
        if ((gamepad1.left_trigger>0.6 && targetAngle>-76) && turned) {
            targetAngle-=15;
            //noinspection UnusedAssignment
            turned = false;
        }
        turned = (gamepad1.left_trigger<0.2 && gamepad1.right_trigger<0.2);

        if (gamepad1.dpad_up) {
            linkServo.setPosition(0.63);
        } if (gamepad1.dpad_down) {
            linkServo.setPosition(0.38);
        } if (gamepad1.right_bumper) {
            linkServo.setPosition(0.47);
        }

        if (change) {
            flWeight += ((gamepad1.dpad_up ? 0.01 : 0) - (gamepad1.dpad_down ? 0.01 : 0)) * (gamepad1.x ? 1 : 0);
            frWeight += ((gamepad1.dpad_up ? 0.01 : 0) - (gamepad1.dpad_down ? 0.01 : 0)) * (gamepad1.y ? 1 : 0);
            blWeight += ((gamepad1.dpad_up ? 0.01 : 0) - (gamepad1.dpad_down ? 0.01 : 0)) * (gamepad1.a ? 1 : 0);
            brWeight += ((gamepad1.dpad_up ? 0.01 : 0) - (gamepad1.dpad_down ? 0.01 : 0)) * (gamepad1.b ? 1 : 0);
            change = false;
        }
        if (!gamepad1.dpad_up & !gamepad1.dpad_down) {
            change = true;
        }

        telemetry.addData("Position: ", linkServo.getPosition());

        telemetry.addData("Weights:","FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",flWeight,frWeight,blWeight,brWeight);

        telemetry.update();
    }

    @Override
    public void stop() {
        telemetry.addData("Weights:","FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",flWeight,frWeight,blWeight,brWeight);
    }

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(() -> {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        });

        telemetry.addLine()
                .addData("status", ()->imu.getSystemStatus().toShortString())
                .addData("calibrated?", ()->imu.getCalibrationStatus().toString());

        telemetry.addLine()
                .addData("heading", ()->formatAngle(angles.angleUnit, angles.firstAngle))
                .addData("roll", ()->formatAngle(angles.angleUnit, angles.secondAngle))
                .addData("pitch", ()->formatAngle(angles.angleUnit, angles.thirdAngle));

        telemetry.addLine()
                .addData("gravity", ()->gravity.toString())
                .addData("mag", ()->String.format(Locale.getDefault(), "%.3f",
                        Math.sqrt(gravity.xAccel*gravity.xAccel
                                + gravity.yAccel*gravity.yAccel
                                + gravity.zAccel*gravity.zAccel)));
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
