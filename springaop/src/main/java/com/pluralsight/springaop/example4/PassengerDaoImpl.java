package com.pluralsight.springaop.example4;

import java.util.HashMap;
import java.util.Map;

public class PassengerDaoImpl implements PassengerDao {
	
	private static Map<Integer, Passenger> passengersMap = new HashMap<>();

	@Log	public Passenger getPassenger(int id) {
		if (null != passengersMap.get(id)) {
			return passengersMap.get(id);
		}
		
		Passenger passenger = new Passenger(id);
		passengersMap.put(id, passenger);
		return passenger;
	}

}
