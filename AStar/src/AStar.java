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

		System.out.println("Loading...");
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
		System.out.println("Done!");
	}
	
	private String[] parseLine(String line) {
		String splitter = "([a-zA-Z]+(-?| ?)[a-zA-z]+)|(-?(\\d+.\\d*))";
		Matcher matcher = Pattern.compile(splitter).matcher(line);
		String[] infoArr = new String[3];
		
		for(int i = 0; i < 3; i++) {
			matcher.find();
			infoArr[i] = matcher.group();
		}
		return infoArr;
	}
	
	@Override
	public void setVerbose(int level) {
		verboseLevel = level;
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
