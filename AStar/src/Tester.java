
public class Tester {

	public static void main(String[] args) {
		AStar astr = new AStar("text.txt");
		astr.verboseLevel = 1;
		System.out.println(astr.getPathInfo("La Crosse","Minneapolis"));
	}
}
