import java.util.*;

public class CloudTravel {

	public static void main(String[] args) {
		
		// total no. of values
		int airportMin = 1;
		int airportMax = 20;
		
		
		// latitude values limit
		int latitudeMin = -89;
		int latitudeMax = 89;
		
		// longitude values limit
		int longitudeMin = -179;
		int longitudeMax = 179;
		
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter No. of Airports = ");
		int airports = sc.nextInt();
				
		if(airports>=airportMin&&airports<=airportMax) {
			
			int[] latArray = new int[airports];
			int[] lngArray = new int[airports];
			String canTravel[] = new String[airports];
			
			boolean valueLimitFlag = true;
			System.out.println("\nEnter Latitudes:");
			for(int i=0;i<airports;i++) {
				int inputLat = sc.nextInt();
				if( inputLat<latitudeMin || inputLat>latitudeMax ) {
					valueLimitFlag = false;
					break;
				}
				latArray[i] = inputLat;
			}
			
			// If Latitude values are defined in the range
			if(valueLimitFlag) {
				
				System.out.println("\nEnter Longitudes:");
				for(int i=0;i<airports;i++) {
					int inputLng = sc.nextInt();
					if( inputLng<longitudeMin || inputLng>longitudeMax ) {
						valueLimitFlag = false;
						break;
					}
					lngArray[i] = inputLng;
				}
				
				
				// If Longitude values are defined in the range
				if(valueLimitFlag) {
					
					System.out.println("\nEnter canTravel:");
					canTravel[0] = sc.nextLine();
					for(int j=0;j<airports;j++) {
						canTravel[j] = sc.nextLine();
					}
					
					System.out.print("\nEnter Source Airport: ");
					int source = sc.nextInt();
					
					if(source<0 || source>airports-1) {
						valueLimitFlag = false;
					}
					
					// If source airport index value is defined in the range
					if(valueLimitFlag) {
						
						System.out.print("\nEnter Destination Airport: ");
						int destination = sc.nextInt();
						
						sc.close();
						
						if(destination<0 || destination>airports-1) {
							valueLimitFlag = false;
						}
												
						// If destination airport index value is defined in the range
						if(valueLimitFlag) {
							Double travelledDistance = (source==destination)?0.0:shortestCourierTrip(airports, latArray, lngArray, canTravel, source, destination);
							System.out.println("\n\nFinal Distance from "+source+" to "+destination+": "+((travelledDistance==Double.MAX_VALUE || travelledDistance==-1.0)?-1:travelledDistance));
						}
						else {
							System.out.println("Destination Airport Index must be between 0 and "+(airports-1)+", inclusive.");
						}
						
					}
					else {
						System.out.println("Source Airport Index must be between 0 and "+(airports-1)+", inclusive.");
					}
					
				}
				else {
					System.out.println("Longitude must be between -179 and 179, inclusive.");
				}
				
			}
			else {
				System.out.println("Latitude must be between -89 and 89, inclusive.");
			}
			
		}
		else {
			System.out.println("Number of airports must be between 1 and 20 elements, inclusive.");
		}
		
	}

	
	// Find shortest courier trip
	private static Double shortestCourierTrip(int airports, int[] latArray, int[] lngArray, String[] canTravel, int source, int destination) {
		Double totalDistance = 0.0;
		
		// Make a list of distances from source airport to destination airport
		ArrayList<Integer> sources = new ArrayList<>();
		ArrayList<Integer> destinations = new ArrayList<>();
		ArrayList<Double> distances = new ArrayList<>();
			
		System.out.println("\n\nDistances among the airports");
		for(int i=0;i<canTravel.length;i++) {
			String[] travelArray = canTravel[i].split("\\s");
						
			for(int j=0;j<travelArray.length;j++) {
				int tempDestinationIndex = Integer.parseInt(travelArray[j]);
				Double currentDistance = calculateDistance(latArray[i], lngArray[i], latArray[tempDestinationIndex], lngArray[tempDestinationIndex]);
				sources.add(i);
				destinations.add(tempDestinationIndex);
				distances.add(currentDistance);
							
				System.out.println(i+", "+tempDestinationIndex+": "+currentDistance);
			}
		}
					
		totalDistance = getShortestDistance(airports, sources, destinations, distances, source, destination);
		
		return totalDistance;
	}
	
	
	
	// Technique from the Dijkstra Algorithm
	// Find the shortest distance from source to destination
	private static Double getShortestDistance(int airports, ArrayList<Integer> sources, ArrayList<Integer> destinations, ArrayList<Double> distances, int source, int destination) {
		Double doubleMax = Double.MAX_VALUE; // Define the max value for the distnaces
		
		// Create queue to maintain the distances
		ArrayList<Integer> queue = new ArrayList<>();
		double[] distanceArray = new double[sources.size()];
		
		// Create Points to calculate the final distances from the source
		HashMap<Integer,Double> finalArray = new HashMap<>();
		
		queue.add(0);
		finalArray.put(0,0.0);
		
		
		// Adding the infinite distance by "double" max value except for '0th' index 
		distanceArray[0] = 0.0;
		for(int i=1;i<airports;i++){
			distanceArray[i] = doubleMax;
		}
		
		
		boolean condition=true;
		int airportIndex = 0;
		while(condition) {
			
			// Put the counted distances into the distanceArray
			for(int i=0;i<sources.size();i++) {
				if(sources.get(i)==airportIndex) {
					if(queue.indexOf(destinations.get(i))<0 && (distances.get(i)+distanceArray[airportIndex])<distanceArray[destinations.get(i)]){
						distanceArray[destinations.get(i)] = distances.get(i)+distanceArray[airportIndex];
					}
				}
			}
			
			// Find index with Minimum Distance from the distanceArray
			double findMin = doubleMax;
			for(int i=0;i<distanceArray.length;i++) {
				if(queue.indexOf(i)<0 && distanceArray[i]<=findMin){
					findMin = distanceArray[i];
					airportIndex = i;
				}
			}
			
			// Add to the Queue and original Array
			queue.add(airportIndex);
			finalArray.put(airportIndex,findMin);
			
			if(queue.size()==distanceArray.length) {
				condition=false;
			}
			
		}
		
		return finalArray.get(destination);
	}

	
	// Method for calculating the Distance between two Airports
	private static Double calculateDistance(int lat1, int lng1, int lat2, int lng2) {
		Double distance=0.0;
		distance = 4000 * Math.acos( ( Math.sin(getRadian(lat1)) * Math.sin(getRadian(lat2)) ) + ( Math.cos(getRadian(lat1)) * Math.cos(getRadian(lat2)) * Math.cos( getRadian(lng1 - lng2) ) ) );
		return distance;
	}
	
	// Convert Degree to Radian
	private static Double getRadian(int num) {
		return Math.toRadians(num);
	}

}
