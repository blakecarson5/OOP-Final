package CPSC.Final.Copy;

import java.util.ArrayList;
import java.util.List;

public class Person implements Observable {
	private List<Observer> observers = new ArrayList<>(); 
	private String lastWish;
	
	public Person() {
		lastWish = "";
	}
	
	public void makeWish(String wish) {
		if(wish == null || wish.isEmpty())
			throw new IllegalArgumentException("wishes cannot be null or empty");
		
		lastWish = wish;
		
		for(int i = 0; i < observers.size(); i++)
			observers.get(i).update(this);
		
		
	}
	
	public String getWish() {
		return lastWish;
	}

	@Override
	public boolean addObserver(Observer observer) {
		if(observers.contains(observer))
			return false;
		return observers.add(observer);
	}

	@Override
	public boolean removeObserver(Observer observer) {
		return observers.remove(observer);
	}

	@Override
	public boolean hasObservers() {
		return observers.size() > 0;
	}

}
