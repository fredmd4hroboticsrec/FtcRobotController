package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="test")
public class TestOpMode extends OpMode {

    private DcMotor flMotor = null;
    private DcMotor frMotor = null;

    @Override
    public void init() {
        flMotor = hardwareMap.get(DcMotor.class, "flMotor");
        frMotor = hardwareMap.get(DcMotor.class, "frMotor");
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {
        double flPower;
        double frPower;

        double yMovement = gamepad1.right_stick_y;
        double xMovement = gamepad1.right_stick_x;

        flPower = yMovement+xMovement;
        frPower = yMovement-xMovement;

        flMotor.setPower(flPower);
        frMotor.setPower(frPower);
    }
}
