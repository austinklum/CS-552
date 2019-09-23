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
		Comparator<City> cityComp = new Comparator<City>() {
		    public int compare(City city1, City city2) {
		    	return (int) (city1.pastCost - city2.pastCost);
		    }
		};
		frontier = new PriorityQueue<City>(cityComp);
		parseFile(fileName);
	}
	
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
			String regexCities = "([a-zA-z(-)]+((-?| ?)[a-zA-z(-)])+)|\\s+(-?(\\d*.\\d*))\\s+(-?(\\d*.\\d*))";
			line = scan.nextLine();
			
			if (line.contains("#")) { // Ignore lines with #
				continue;
			}
			
			String[] infoArr = parseLine(line);
			
			if(!line.contains(",")) { // Add city
				graph.addCity(infoArr[0], Double.valueOf(infoArr[1]), Double.valueOf(infoArr[2]));
			} else { // Add edge
				graph.addEdge(infoArr[0], infoArr[1], Double.valueOf(infoArr[2]));
			}
		}
		printMsg("Done!", 1);
	}
	
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
	
	private void printMsg(String msg, int level) {
		if(verboseLevel >= level) {
			System.out.println(msg);
		}
	}

	@Override
	public FoundPath getPathInfo(String startCity, String endCity) {
		PathFound myPath = new PathFound();
		City currentCity = graph.cities.get(startCity);
		City goalCity = graph.cities.get(endCity);
		frontier.add(currentCity);
		myPath.totalNodes++;
		
		// while not at end city
		printMsg("Searching for path between " + startCity + " and " + endCity, 1);
		while(!currentCity.name.equals(endCity)) {
			printMsg("\nSearching...", 1);
		// Expand top priority node
			currentCity = frontier.poll();
			StringBuilder pCit = new StringBuilder(" => ");
			City tempCity = currentCity;
			while(tempCity.pastCity != null) {
				pCit.insert(0, " => " + tempCity.pastCity.name); 
				tempCity = tempCity.pastCity;
			}
			printMsg("Expanded " + pCit + currentCity,2);
		// Add stats to FoundPath
			myPath.openNodes++;
			myPath.totalCost += currentCity.pastCost;
		// calculate possible edges 
			for(Edge edge : graph.edges.get(currentCity.name)) {
				double gn = currentCity.pastCost + edge.distanceApart;
				double hn = getHeuristic(edge.cityEnd,goalCity);
				double cost = getHeuristic(edge.cityEnd,goalCity) 
						 	+ currentCity.pastCost 
						 	+ edge.distanceApart;
				printMsg(edge.cityEnd.name + "  :: f(n) = " + cost + " = (" + edge.distanceApart + " + " + currentCity.pastCost + ") + " + hn, 2);
				City copy = edge.cityEnd.copy();
				copy.pastCost = gn;
				copy.pastCity = edge.cityStart;
				frontier.add(copy);	
				myPath.totalNodes++;
			}
		}
		// Final Path
		LinkedList<String> path = new LinkedList<>();
		while(currentCity.pastCity != null) {
			path.add(currentCity.name);
			currentCity = currentCity.pastCity;
		}
		path.add(startCity);
		myPath.path = path;
		
		printMsg("Search Complete!",1);
		return myPath;
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
		return (List<String>) graph.cities.keySet();
	}
	
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


	// convert degrees to radians
	private double deg2rad(double deg) {
		return deg * Math.PI/180; // radians = degrees * pi/180
	}
		
	class Graph {
		HashMap<String,City> cities;
		HashMap<String,LinkedList<Edge>> edges;
		//HashMap<String,Double> bestCost;
		
		Graph(){
			cities = new HashMap<>();
			edges = new HashMap<>();
		//	bestCost = new HashMap<>();
		}
		
		private void addCity(String cityName, double lat, double lon) {
			cities.put(cityName, new City(cityName,lat,lon));
			edges.put(cityName, new LinkedList<>());
			//bestCost.put(cityName,Double.MAX_VALUE);
		}
		
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
		public double pastCost;
		public City pastCity;
		
		City(String name, double lat, double lon){
			pastCost = 0;
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
		
		City(String name, double lat, double lon, double pastCost){
			this.pastCost = pastCost;
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
		
		private City copy() {
			return new City(name,lat,lon,pastCost);
		}
		
		@Override
		public String toString() {
			return name + " :: " + pastCost;
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


