package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name = "2026 Auto")
public class autonomous extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    BNO055IMU imu;
    Orientation angles;

    private double sensitivity(double A) {
        return (Math.log(A*A*10+1)/11.3022167793+Math.abs(A)/90)*Math.signum(A)/3  /  2;
    }

    @Override
    public void runOpMode() {

        DcMotor flMotor = hardwareMap.get(DcMotor.class, "flMotor");
        DcMotor frMotor = hardwareMap.get(DcMotor.class, "frMotor");
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        DcMotor blMotor = hardwareMap.get(DcMotor.class, "blMotor");
        DcMotor brMotor = hardwareMap.get(DcMotor.class, "brMotor");
        blMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        brMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 10);

        double flPower;
        double frPower;
        double blPower;
        double brPower;
        double twist;

        waitForStart();

        runtime.reset();

        while (opModeIsActive()) {
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            twist = -sensitivity(angles.firstAngle);
            if (runtime.seconds() < 1.25) {
                flPower = -0.4+twist;
                frPower = -0.4-twist;
                blPower = 0.4+twist;
                brPower = 0.4-twist;
            }
            else if (runtime.seconds() < 3.5 && runtime.seconds() > 2.25) {
                flPower = 0.4+twist;
                frPower = 0.40 -twist;
                blPower = -0.40+twist;
                brPower = -0.4-twist;
            }
            else {
                 flPower=0;
                 frPower=0;
                 blPower=0;
                 brPower=0;
            }
            flMotor.setPower(flPower);
            frMotor.setPower(frPower);
            blMotor.setPower(blPower);
            brMotor.setPower(brPower);
        }

    }

}
