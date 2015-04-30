package Update;

import main.Main;
import Geometry.ColoredPolygon;
import Other.OtherFunctions;
import Thread.Task;

public class UpdateTask extends Task {

	int orderNum;
	double percent;
	boolean doneUpating;

	@Override
	public void runTask() { // The default task object
		if (!Main.paused && readyToUpdate()) {
			try {
				Main.display.repaint();
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.current = OtherFunctions.sortList(Main.current);
			Main.toDraw = new ColoredPolygon[Main.current.size()];
			for (int i = 0; i < Main.rasterizers.length; i++) {
				Main.rasterizers[i].isDone = false;
			}
		}
	}

	public boolean readyToUpdate() {
		for (int i = 0; i < Main.rasterizers.length; i++) {
			if (Main.rasterizers[i].isDone == false) {
				return false;
			}
		}
		return true;
	}

	// Most methods will be overriden
	@Override
	public Boolean returnRunnable() {
		return true;
	}

	@Override
	public int getWait() {
		return 1;
	}

	@Override
	public int[] getData() {
		return new int[] { 0 };
	}

	@Override
	public int getCPULoad() {
		return 2; // 0 is no load, 3 is maximum load
	}
}
