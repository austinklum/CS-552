import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class AStar extends Pathfinder{

	int verboseLevel;
	PriorityQueue<Double> frontier;
	Graph graph;

	
	AStar(String fileName) {
		verboseLevel = 0;
		frontier = new PriorityQueue<>();
		graph = new Graph();
		parseFile(fileName);
	}
	
	private void parseFile(String fileName) {
		
		//While adding cities
		
		//split up line
		String cityName = "";
		double lat = 0, lon = 0;
		//Add city
		graph.addCity(cityName,lat,lon);
		
		// While adding edges
		
		//split up line
		String startCity = "", endCity = "";
		double distance = 0;
		
		//Add Edge
		graph.addEdge(startCity,endCity,distance);
	}
	
	
	
	@Override
	public void setVerbose(int level) {
		verboseLevel = level;
	}

	@Override
	public FoundPath getPathInfo(String startCity, String endCity) {
		// while not at end city
		
		// Expand top priority node
		
		// Add stats to FoundPath
				
		// calculate possible edges 
		
		// add to frontier
		return null;
	}

	@Override
	public Optional<Double> getDirectDistance(String startCity, String endCity) {
		double dist = Double.MAX_VALUE;
		
		//Search for shortest direct edge between two cities
		for(Edge edge : graph.cities.get(startCity).edges) {
			if(edge.cityEnd.name.equals(endCity) && edge.distanceApart < dist) {
				dist = edge.distanceApart;
			}
		}
		
		if(dist != Double.MAX_VALUE) {
			return Optional.of(dist);
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
