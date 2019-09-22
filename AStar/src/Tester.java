public class Tester {

	public static void main(String[] args) {
		AStar astr = new AStar("cities02.txt");
		astr.verboseLevel = 2;
		FoundPath fp = astr.getPathInfo("La Crosse","Minneapolis");
		printPath(fp);
		
	}
	
	private static void printPath(FoundPath fp) {
		for(String city : fp.getPath()) {
			System.out.println(" => " + city);
		}
	}
}
