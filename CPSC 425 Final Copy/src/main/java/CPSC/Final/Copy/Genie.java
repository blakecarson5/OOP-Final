package CPSC.Final.Copy;

public class Genie implements Observer {
	private Person myPerson;
	private int wishesLeft;
	
	public Genie(Person myPerson) {
		if(myPerson == null)
			throw new IllegalArgumentException("master cannot be null");
		
		this.myPerson = myPerson;
		this.wishesLeft = 3;
		
		this.myPerson.addObserver(this);
	}
	
	public Person getMaster() {
		return myPerson;
	}
	
	public int getWishesLeft() {
		return wishesLeft;
	}
	
	@Override
	public void update(Observable observer) {
		if(observer instanceof Person) {
			Person p = (Person) observer;
			
			if(!(p.equals(this.getMaster())))
				throw new IllegalArgumentException("wish not granted [not my master]");
			
			if(!(p.getWish().startsWith("I wish")))
				throw new IllegalArgumentException("wish not granted [must start with 'I wish']");
			
			wishesLeft--;
			
			if(wishesLeft == 0) {
				p.removeObserver(this);
				myPerson = null;
			}
		}
		
		else
			throw new IllegalArgumentException("wish not granted [not a person]");
	}
}
