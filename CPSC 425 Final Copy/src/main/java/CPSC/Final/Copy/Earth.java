package CPSC.Final.Copy;

public class Earth {
	private static Earth unique;
	private int trees;
	
	static {
		unique = new Earth();
	}
	
	private Earth() {
		trees = 0;
	}
	
	public static Earth getInstance() {
		return unique;
	}
	
	public int plantTree() {
		trees++;
		return trees;
	}
	
	@Override
	public String toString() {
		return "Planet Earth [trees=" + trees + "]";
	}
}
