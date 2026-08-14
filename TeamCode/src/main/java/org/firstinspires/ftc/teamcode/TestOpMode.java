package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="test")
public class TestOpMode extends OpMode {

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
    }

    @Override
    public void loop() {
        double flPower;
        double frPower;
        double blPower;
        double brPower;

        double yMovement = gamepad1.right_stick_y;
        double xMovement = gamepad1.right_stick_x;
        double twist = gamepad1.left_stick_x/10;

        flPower = yMovement-xMovement+twist;
        frPower = yMovement+xMovement-twist;
        blPower = -yMovement-xMovement-twist;
        brPower = -yMovement+xMovement+twist;

        flMotor.setPower(flPower*flWeight);
        frMotor.setPower(frPower*frWeight);
        blMotor.setPower(blPower*blWeight);
        brMotor.setPower(brPower*brWeight);

        if (gamepad1.dpad_left) {
            linkServo.setPosition(0.60);
        } if (gamepad1.dpad_right) {
            linkServo.setPosition(0.37);
        } if (gamepad1.right_bumper) {
            linkServo.setPosition(linkServo.getPosition()>0.48?0.44:0.48);
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

    }

    @Override
    public void stop() {
        telemetry.addData("Weights:","FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",flWeight,frWeight,blWeight,brWeight);
    }
}
