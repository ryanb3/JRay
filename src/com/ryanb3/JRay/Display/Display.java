package com.ryanb3.JRay.Display;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.ryanb3.JRay.Controls.MainKeyControls;
import com.ryanb3.JRay.Controls.MouseMoverTask;
import com.ryanb3.JRay.Other.ScreenPointFinder;
import com.ryanb3.JRay.Scene.Scene;
import com.ryanb3.JRay.Tests.Test;
import com.ryanb3.JRay.Update.RasterizeTask;
import com.ryanb3.JRay.Update.UpdateTask;
import com.ryanb3.TaskManager.TaskManager;

public class Display extends JFrame { // Just a holder for a JPanel

	public DisplayPanel display;
	public RasterizeTask[] rasterizers; // The tasks which do the rasterization
	public UpdateTask update; // The class that orders how stuff should be drawn and what should be drawn
	public MainKeyControls keyControls; // The class that control the camera movement
	public boolean paused = false; // Whether the engine is paused or not
	public ScreenPointFinder find; // The algorithm that changes angle to pixel
	MouseMoverTask mover; // The task which controls mouse and window centering
							// Also translates movement to angle movement
	public Scene currentScene;
	public double FOV = 1.13446401379; // A nice number which looks good
	public TaskManager myManage; //The thing that allocates "Tasks" to different CPU Cores
	public int screenOffset = 0;
	public int displayWait = 1; //1000 / FPSLimit - 1 = displayWait

	public Display(TaskManager manage) { // Inits the display panel
		myManage = manage;
		display = new DisplayPanel();
		display.setBounds(0, 0, Test.screenWidth, Test.screenHeight);
		add(display);
		initPointFinder();
		initUserControls();
		startEngine();
	}

	public void addScene(Scene toLoad) {
		currentScene = toLoad;
	}

	public void updateKeyControls() {
		this.addKeyListener(keyControls);
	}
	
	public void hideCursor(boolean hideOrShow) { // True is hide, false
		// is show
		if (hideOrShow) { // Hides the cursor
			BufferedImage cursorImg = new BufferedImage(16, 16,// Makes it a blank cursor
					BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
			super.getContentPane().setCursor(blankCursor);
		} else {
			super.getContentPane().setCursor(Cursor.getDefaultCursor()); // Shows the cursor, makes it the default
		}
	}

	public void initPointFinder() { // Makes some more stuff not null
		find = new ScreenPointFinder();
	}

	public void startEngine() { // Starts the rasterizers and updater
		update = new UpdateTask();
		//double cores = Runtime.getRuntime().availableProcessors();
		double cores = 1;
		rasterizers = new RasterizeTask[(int) cores];
		myManage.addTask(update);
		for (int i = 0; i < cores; i++) {
			rasterizers[i] = new RasterizeTask(1 / cores, i);
			myManage.addTask(rasterizers[i]);
		}
	}

	public void initUserControls() { // Starts the user controls
		keyControls = new MainKeyControls();
		updateKeyControls();
		mover = new MouseMoverTask();
		myManage.addTask(mover);
	}

	public void setScene(Scene toSet) {
		currentScene = toSet;
	}
	
	public void removeScene() {
		currentScene = null;
	}
	
	public double findLowestAngle(double angle) { //Finds the smallest angle in the domain of -FOV - 2PI + FOV
		if(angle < -FOV) {
			angle += Math.PI * 2;
			return findLowestAngle(angle);
		} else if(angle > Math.PI * 2 - FOV) {
			angle -= Math.PI * 2;
			return findLowestAngle(angle);
		} else {
			return angle;
		}
	}
}