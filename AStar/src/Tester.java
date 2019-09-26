public class Tester {

	public static void main(String[] args) {
		AStar astr = new AStar("cities02.txt");
		FoundPath fp = astr.getPathInfo("Boston","San Jose");
		printPath(fp);
		System.out.println(astr.getCities());
		
	}
	
	private static void printPath(FoundPath fp) {
		for(String city : fp.getPath()) {
			System.out.println(" => " + city);
		}
		System.out.println(fp.getTotalCost());
	}
}
