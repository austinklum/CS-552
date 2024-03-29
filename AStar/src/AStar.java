import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AStar extends Pathfinder{

	int verboseLevel;
	PriorityQueue<City> frontier;
	Graph graph;
	FoundPath foundPath;
	
	AStar(String fileName) {
		verboseLevel = 0;
		graph = new Graph();
		Comparator<City> cityComp = new Comparator<City>() { // Create compartator object for frontier
		    public int compare(City city1, City city2) {
		    	return (int) (city1.cost - city2.cost);
		    }
		};
		frontier = new PriorityQueue<City>(cityComp);
		parseFile(fileName);
	}
	
	/***
	 * Parses through the file and creates node set and edge set
	 * @param fileName Where we will scan and take information from
	 */
	private void parseFile(String fileName) {

		String line = "";
		Scanner scan = null;
		
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		printMsg("Loading...", 1);
		while(scan.hasNext()) {
			line = scan.nextLine();
			
			if (line.contains("#")) { // Ignore lines with #
				continue;
			}
			
			// Parse Line into infoArr
			String[] infoArr = parseLine(line);
			
			if(!line.contains(",")) { // Add city
				graph.addCity(infoArr[0], Double.valueOf(infoArr[1]), Double.valueOf(infoArr[2]));
			} else { // Add edge
				graph.addEdge(infoArr[0], infoArr[1], Double.valueOf(infoArr[2]));
			}
		}
		printMsg("Done!", 1);
	}
	
	/***
	 * Parse the line into an info arr of size 3
	 * @param line Line that we are parsing
	 * @return info arr of size 3
	 */
	private String[] parseLine(String line) {
		String splitter = "([a-zA-z(-)'-']+((-?| ?)[a-zA-z(-)'-'])+)|(-?(\\d+.\\d*))";
		Matcher matcher = Pattern.compile(splitter).matcher(line);
		String[] infoArr = new String[3];
		
		printMsg("Parsing Line: " + line, 2);
		for(int i = 0; i < 3; i++) {
			matcher.find();
			infoArr[i] = matcher.group();
			printMsg(matcher.group(), 2);
		}
		return infoArr;
	}
	
	@Override
	public void setVerbose(int level) {
		verboseLevel = level;
	}
	
	/***
	 * Method to print a message depending on the verbosity level
	 * @param msg String message that will be printed out
	 * @param level Level of verbosity this should show on.
	 */
	private void printMsg(String msg, int level) {
		if(verboseLevel >= level) {
			System.out.println(msg);
		}
	}

	@Override
	public FoundPath getPathInfo(String startCity, String endCity) {
		// Init path and starting city
		PathFound myPath = new PathFound();
		City currentCity = graph.cities.get(startCity);
		City goalCity = graph.cities.get(endCity);
		frontier.add(currentCity);
		myPath.totalNodes++;
		
		printMsg("Searching for path between " + startCity + " and " + endCity, 1);
		
		// while not at end city
		while(!currentCity.name.equals(endCity)) {
			printMsg("\nSearching...", 1);
		// Expand top priority node
			currentCity = frontier.poll();
			getPathHistory(currentCity);
		// Add stats to FoundPath
			myPath.openNodes++;
		// calculate possible edges 
			for(Edge edge : graph.edges.get(currentCity.name)) {
				addNeighbors(myPath, currentCity, goalCity, edge);
			}
		}
		// Final Path
		getFinalPathSoln(startCity, myPath, currentCity);
		
		printMsg("Search Complete!",1);
		return myPath;
	}

	/***
	 * Creates and sets the LinkedList<String> of the path found
	 * @param startCity Key of starting city
	 * @param myPath FoundPath information
	 * @param currentCity Goal state node with history link back
	 */
	private void getFinalPathSoln(String startCity, PathFound myPath, City currentCity) {
		LinkedList<String> path = new LinkedList<>();
		myPath.totalCost = (int) currentCity.actualCost; 
		
		// Add city names in reserve order to path
		while(currentCity != null) {
			path.addFirst(currentCity.name);
			currentCity = currentCity.pastCity;
		}
		myPath.path = path;
	}

	/**
	 * Adds the neighbors and stats from the neighborhood of the current node
	 * @param myPath
	 * @param currentCity
	 * @param goalCity
	 * @param edge
	 */
	private void addNeighbors(PathFound myPath, City currentCity, City goalCity, Edge edge) {
		// Calculate cost from g(n) and h(n)
		double gn = currentCity.actualCost + edge.distanceApart;
		double hn = getHeuristic(edge.cityEnd,goalCity);
		double cost = gn + hn;
		
		printMsg(edge.cityEnd.name + "  :: f(n) = " + cost + " = (" + edge.distanceApart + " + " + currentCity.actualCost + ") + " + hn, 2);
		
		// Create a frontier node
		City copy = edge.cityEnd.copy();
		copy.cost = cost;
		copy.actualCost = gn;
		copy.pastCity = currentCity;
		
		// Add to the frontier if we can find a better costing path
		if(graph.bestCost.get(copy.name) > copy.actualCost) {
			graph.bestCost.put(copy.name, copy.actualCost);
			frontier.add(copy);	
			myPath.totalNodes++;
		}
	}

	/***
	 * Prints out a list of prev visited cities
	 * @param currentCity Where we are now in the search
	 */
	private void getPathHistory(City currentCity) {
		StringBuilder pCit = new StringBuilder(" => ");
		City tempCity = currentCity;
		while(tempCity.pastCity != null) {
			pCit.insert(0, " => " + tempCity.pastCity.name); 
			tempCity = tempCity.pastCity;
		}
		printMsg("Expanded " + pCit + currentCity,2);
	}

	@Override
	public Optional<Double> getDirectDistance(String startCity, String endCity) {
		for(Edge edge : graph.edges.get(startCity)) {
			if(edge.cityEnd.name.equals(endCity)) {
				return Optional.of(edge.distanceApart);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<String> getCities() {
		List<String> citites = new LinkedList<>();
		for(String city : graph.cities.keySet()) {
			citites.add(city);
		}
		return citites;
	}
	
	/***
	 * Get the heuristic value based off haversine function
	 * @param cityStart Starting City
	 * @param cityEnd Ending City
	 * @return heuristic value to travel from starting city to ending city.
	 */
	private double getHeuristic(City cityStart, City cityEnd) {
		
	double Rm = 3961; // mean radius of the earth (miles) at 39 degrees from the equator
	
	// convert coordinates to radians
	double lat1 = deg2rad(cityStart.lat);
	double lon1 = deg2rad(cityStart.lon);
	double lat2 = deg2rad(cityEnd.lat);
	double lon2 = deg2rad(cityEnd.lon);
	
	double dlat = lat2 - lat1;
	double dlon = lon2 - lon1;
	
	double a  = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
	double c  = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); // great circle distance in radians
	double dm = c * Rm; // great circle distance in miles
	
	return dm;
}


	/***
	 * Converts Degrees to Radians
	 * @param deg Degrees
	 * @return Radian value
	 */
	private double deg2rad(double deg) {
		return deg * Math.PI/180; // radians = degrees * pi/180
	}
	
	class Graph {
		HashMap<String,City> cities;
		HashMap<String,LinkedList<Edge>> edges;
		HashMap<String,Double> bestCost;

		
		Graph(){
			cities = new HashMap<>();
			edges = new HashMap<>();
			bestCost = new HashMap<>();
		}
		/***
		 * Adds a city to our hashmap and initializes other hash maps
		 * @param cityName Key for Hash
		 * @param lat Lattitude of City
		 * @param lon Longitude of City
		 */
		private void addCity(String cityName, double lat, double lon) {
			cities.put(cityName, new City(cityName,lat,lon));
			edges.put(cityName, new LinkedList<>());
			bestCost.put(cityName,Double.MAX_VALUE);
		}
		
		/***
		 * Adds an edge to our edge set
		 * @param startCity Key for starting city
		 * @param endCity Key for ending city
		 * @param distanceApart How far away the two cities are (in miles)
		 */
		private void addEdge(String startCity, String endCity, double distanceApart) {
			City start = cities.get(startCity);
			City end = cities.get(endCity);
			edges.get(startCity).add(new Edge(start,end,distanceApart));
			edges.get(endCity).add(new Edge(end,start,distanceApart));
		}
	}
	
	class City {
		public String name;
		public double lat;
		public double lon;
		public double cost;
		public double actualCost;
		public City pastCity;
		
		City(String name, double lat, double lon){
			cost = 0;
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
		
		City(String name, double lat, double lon, double cost, double actualCost){
			this.cost = cost;
			this.actualCost = actualCost;
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
		/***
		 * Creates a copy of the city to put in our frontier
		 * @return Copy of city node
		 */
		private City copy() {
			return new City(name,lat,lon,cost,actualCost);
		}
		
		@Override
		public String toString() {
			return name + " :: " + cost;
		}
	}
	
	class Edge {
		City cityStart;
		City cityEnd;
		double distanceApart;
		
		Edge(City cityStart,City cityEnd,double distanceApart) {
			this.cityStart = cityStart;
			this.cityEnd = cityEnd;
			this.distanceApart = distanceApart;
		}
		
		@Override
		public String toString() {
			return cityStart.name + " => " + cityEnd.name + " :: " + distanceApart;
		}
		
	}
	
	class PathFound implements FoundPath {
		int totalNodes;
		int totalCost;
		int openNodes;
		List<String> path;
		
		PathFound() {
			totalNodes = 0;
			totalCost = 0;
			openNodes = 0;
		}
		
		@Override
		public List<String> getPath() {
			return path;
		}

		@Override
		public Optional<Integer> getTotalCost() {
			return Optional.of(totalCost);
		}

		@Override
		public int totalNodes() {
			return totalNodes;
		}

		@Override
		public int openNodes() {
			return openNodes;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(String city : path) {
				sb.append(" => ");
				sb.append(city);
				sb.append("\n");
			}
			return sb.toString();
		}
	}
}


