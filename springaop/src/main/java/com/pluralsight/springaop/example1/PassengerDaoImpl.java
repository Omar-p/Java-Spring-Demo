package com.pluralsight.springaop.example1;

import java.util.HashMap;
import java.util.Map;

public class PassengerDaoImpl implements PassengerDao {
	
	private static Map<Integer, Passenger> passengersMap = new HashMap<>();

	public Passenger getPassenger(int id) {
		return passengersMap.put(id, new Passenger(id));
	}

}
