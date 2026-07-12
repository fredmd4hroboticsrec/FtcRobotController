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
    private double flWeight = 1;
    private double frWeight = 1;
    private double blWeight = 1;
    private double brWeight = 1;
    private boolean change = true;
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
    }

    @Override
    public void loop() {
        double flPower;
        double frPower;
        double blPower;
        double brPower;

        double yMovement = gamepad1.right_stick_y;
        double xMovement = gamepad1.right_stick_x;

        flPower = yMovement-xMovement;
        frPower = yMovement+xMovement;
        blPower = -yMovement-xMovement;
        brPower = -yMovement+xMovement;

        flMotor.setPower(flPower*flWeight);
        frMotor.setPower(frPower*frWeight);
        blMotor.setPower(blPower*blWeight);
        brMotor.setPower(brPower*brWeight);
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
        telemetry.addData("Weights:","FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",flWeight,frWeight,blWeight,brWeight);

    }

    @Override
    public void stop() {
        telemetry.addData("Weights:","FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",flWeight,frWeight,blWeight,brWeight);
    }
}
