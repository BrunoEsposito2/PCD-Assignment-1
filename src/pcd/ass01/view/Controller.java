package pcd.ass01.view;

public class Controller implements ActionListener {
	
	private Flag stopFlag;
	
	public Controller() {
		this.stopFlag = new Flag();
	}

	@Override
	public void started() {
		this.stopFlag.reset();
	}

	@Override
	public void stopped() {
		this.stopFlag.set();
	}
}
