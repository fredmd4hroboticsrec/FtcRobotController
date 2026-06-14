package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="test")
public class TestOpMode extends OpMode {

    private DcMotor flMotor = null;
    private DcMotor frMotor = null;
    private DcMotor blMotor = null;
    private DcMotor brMotor = null;

    @Override
    public void init() {
        flMotor = hardwareMap.get(DcMotor.class, "flMotor");
        frMotor = hardwareMap.get(DcMotor.class, "frMotor");
        flMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frMotor.setDirection(DcMotorSimple.Direction.REVERSE );
        blMotor = hardwareMap.get (DcMotor.class, "blMotor");
        brMotor = hardwareMap.get(DcMotor.class, "brMotor");
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {
        double flPower;
        double frPower;
        double blPower;
        double brPower;

        double yMovement = gamepad1.right_stick_y;
        double xMovement = gamepad1.right_stick_x;

        flPower = yMovement+xMovement;
        frPower = yMovement-xMovement;
        blPower = yMovement+xMovement;
        brPower = yMovement-xMovement;

        flMotor.setPower(flPower);
        frMotor.setPower(frPower);
        blMotor.setPower(blPower);
        brMotor.setPower(brPower);






    }
}
