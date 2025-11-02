package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

  public void runOpMode() {
    DcMotor motor = hardwareMap.dcMotor.get("49nck237923a")  
    int vel = getSpeed(motor);
    boolean slvel = velocity(vel);
    if (slvel == true) {
      int set = new.bundle(0.2, 0.2);
    } 

    }
public boolean velocity(int s){
  if (s > 0.8) {
    return true;
  }

public void bundle(int u, int uu) {
  DcMotor motor = hardewareMap.dcMotor.get("49nck237923a"); 
  double vel = getSpeed(motorvelfl);
  vel /= u;
  motor.setPower(uu);

  
}
/*







*/


