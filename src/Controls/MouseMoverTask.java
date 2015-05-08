package Controls;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import main.Main;
import Other.OtherFunctions;
import Thread.Task;

public class MouseMoverTask extends Task {

	int currentX;
	int currentY;
	Robot mouseMover;
	boolean toMoveOrCalculate = false; // True is move, false is calculate

	public MouseMoverTask() {
		try {
			mouseMover = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void runTask() { // The default task object
		if (!Main.paused) {
			if (toMoveOrCalculate) {
				mouseMover.mouseMove((int)(Main.screenWidth / 2),
						(int)(Main.screenHeight / 2));
				Main.display.setBounds((Main.screenWidth - Main.display.getWidth()) / 2, (Main.screenHeight - Main.display.getHeight()) / 2, Main.display.getWidth(), Main.display.getHeight());
				toMoveOrCalculate = false;
			} else {
				Point point = MouseInfo.getPointerInfo().getLocation();
				currentX = (int) point.getX();
				currentY = (int) point.getY();
				double xAngle = Main.xAngle + (Main.FOV / Main.screenWidth / 2)
						* (currentX - Main.screenWidth / 2);
				double yAngle = Main.yAngle + (Main.FOV / Main.screenHeight / 2)
						* (currentY - Main.screenHeight / 2);
				Main.xAngle = xAngle;
				Main.yAngle = yAngle;
				toMoveOrCalculate = true;
			}
		}
		if(!Main.display.isActive() && !Main.paused) {
			Main.paused = true;
			Main.display.display.background = new Color(255, 0, 0);
			OtherFunctions.hideCursor(false);
			Main.keyControls.blackOrRed = false;
			Main.display.repaint();
		}
	}

	// Most methods will be overriden
	@Override
	public Boolean returnRunnable() {
		return true;
	}

	@Override
	public int getWait() {
		return 7;
	}

	@Override
	public int[] getData() {
		return new int[] { 0 };
	}

	@Override
	public int getCPULoad() {
		return 0; // 0 is no load, 3 is maximum load
	}
}
