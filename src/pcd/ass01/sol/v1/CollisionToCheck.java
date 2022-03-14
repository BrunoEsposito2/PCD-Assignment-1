package pcd.ass01.sol.v1;

public class CollisionToCheck {

	private int first, second;
	
	public CollisionToCheck(int i, int j) {
		this.first = i;
		this.second = j;
	}
	
	public int getFirst() {
		return first < second ? first : second;
	}
	
	public int getSecond() {
		return first < second ? second : first;
	}
}
