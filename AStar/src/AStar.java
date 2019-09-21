import java.io.File;
import java.io.FileNotFoundException;
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
	PriorityQueue<Double> frontier;
	Graph graph;
	FoundPath foundPath;
	
	AStar(String fileName) {
		verboseLevel = 0;
		frontier = new PriorityQueue<>();
		graph = new Graph();
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
			String regexCities = "([a-zA-Z]+(-?| ?)[a-zA-z]+)\\s+(-?(\\d*.\\d*))\\s+(-?(\\d*.\\d*))";
			line = scan.nextLine();
			
			if (line.contains("#")) { // Ignore lines with #
				continue;
			}
			
			String[] infoArr = parseLine(line);
			
			if(Pattern.matches(regexCities, line)) { // Add city
				graph.addCity(infoArr[0], Double.valueOf(infoArr[1]), Double.valueOf(infoArr[2]));
			} else { // Add edge
				graph.addEdge(infoArr[0], infoArr[1], Double.valueOf(infoArr[2]));
			}
		}
		printMsg("Done!", 1);
	}
	
	private String[] parseLine(String line) {
		String splitter = "([a-zA-Z]+(-?| ?)[a-zA-z]+)|(-?(\\d+.\\d*))";
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
		City currentCity = graph.cities.get(startCity);
		
		// while not at end city
		while(!currentCity.name.equals(endCity)) {
			
		// Expand top priority node
			
		// Add stats to FoundPath
				
		// calculate possible edges 
		
		// add to frontier
		}
		return null;
	}

	@Override
	public Optional<Double> getDirectDistance(String startCity, String endCity) {
		for(Edge edge : graph.cities.get(startCity).edges) {
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
	
	private double getHeuristic(String city1, String city2) {
		
	City cityStart = graph.cities.get(city1);
	City cityEnd = graph.cities.get(city2);
	
	double Rm = 3961; // mean radius of the earth (miles) at 39 degrees from the equator
	
	// convert coordinates to radians
	double lat1 = deg2rad(cityStart.lat);
	double lon1 = deg2rad(cityStart.lon);
	double lat2 = deg2rad(cityEnd.lat);
	double lon2 = deg2rad(cityEnd.lon);
	
	// find the differences between the coordinates
	double dlat = lat2 - lat1;
	double dlon = lon2 - lon1;
	
	// here's the heavy lifting
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
		
		Graph(){
			cities = new HashMap<>();
		}
		
		private void addCity(String cityName, double lat, double lon) {
			cities.put(cityName, new City(cityName,lat,lon));
		}
		
		private void addEdge(String startCity, String endCity, double distanceApart) {
			City start = cities.get(startCity);
			City end = cities.get(endCity);
			start.edges.add(new Edge(start,end,distanceApart));
			end.edges.add(new Edge(end,start,distanceApart));
		}
	}
	
	class City {
		public String name;
		public double lat;
		public double lon;
		LinkedList<Edge> edges;
		
		City(String name, double lat, double lon){
			this.name = name;
			this.lat = lat;
			this.lon = lon;
			edges = new LinkedList<>();
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
	}
	
}
