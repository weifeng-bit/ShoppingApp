/**
 * Customer.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

import java.text.DecimalFormat;

public class Customer extends User {
	private String address, city, state, username;
	private int zip;
	private List<Order> unshippedOrders, shippedOrders;

	DecimalFormat df = new DecimalFormat("$###,##0.00");

	public Customer(String email, String password) {
		super(email, password);
		this.address = "address unknown";
		this.city = "city unknown";
		this.state = "state unknown";
		this.zip = 00000;
	}
	
	public Customer(String firstName, String lastName, String email)  {
		super(firstName, lastName, email);
		this.address = "address unknown";
		this.city = "city unknown";
		this.state = "state unknown";
		this.zip = 00000;
	}
	
	public Customer(String username, String firstName, String lastName, String email, String password, 
			String address, String city, String state, int zip) {
		//call this one when the customers doesn't have any existing orders
		super(firstName, lastName, email, password);
		this.username = username;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.unshippedOrders = new List<>();
		this.shippedOrders = new List<>();
	}
	
	public Customer(String firstName, String lastName, String email, 
			String address, String city, String state, int zip) { //Guest Constructor
		//good constructor should assign to every var in calss
		super(firstName, lastName, email, "NA");
		this.username = "NA";
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.unshippedOrders = new List<>();  
		this.shippedOrders = new List<>();
	}
	
	public Customer(String username, String firstName, String lastName, String email, String password, 
			String address, String city, String state, int zip,
			List<Order> unshippedOrders, List<Order> shippedOrders) {
		super(firstName, lastName, email, password);
		this.username = username;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.unshippedOrders = unshippedOrders; 
		this.shippedOrders = shippedOrders;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}
	
	public void placeUnshippedOrder(Order order) {
        this.unshippedOrders.addLast(order);
    }

    public void placeShippedOrder(Order order) {
        this.shippedOrders.addLast(order);
    }

	public void removeUnshippedOrder(Order order) {
		unshippedOrders.placeIterator();
		while (!unshippedOrders.offEnd()) {
			if (unshippedOrders.getIterator().equals(order)) {
				unshippedOrders.removeIterator();
				return;
			}
			unshippedOrders.advanceIterator();
		}
	}

	public void removeShippedOrder(Order order) {
		shippedOrders.placeIterator();
		while (!shippedOrders.offEnd()) {
			if (shippedOrders.getIterator().equals(order)) {
				shippedOrders.removeIterator();
				return;
			}
			shippedOrders.advanceIterator();
		}
	}
	
	public void viewUnshippedOrders() {
		String divider = "-----------------------------------------------"
						+ "-------------------------------------------";
		String t2 = "\t\t", t3 = "\t\t\t", t4 = "\t\t\t\t";
		if (unshippedOrders.isEmpty()) {
			System.out.println("You don't have any unshipped orders!");
		} else {
			unshippedOrders.placeIterator();
			for (int i = 0; i < unshippedOrders.getLength(); i++) {
				List<VideoGame> vgList = unshippedOrders.getIterator()
						.getOrderContents();
				vgList.placeIterator();
				System.out.println(t4 + "[Order #" + (i + 1) + "]\n"
						+ "      " + t2 + "  Price" + t4 + "Title\n" + divider);
				for (int j = 0; j < vgList.getLength(); j++) {
					VideoGame currVG = vgList.getIterator();
					System.out.println(t2 + "  "
							+ df.format(currVG.getPrice()) + "\t" + currVG.getTitle() 
							+ " (" + currVG.getPlatform() + ")");
					vgList.advanceIterator();
				}
				
				int tempShippingSpeed = unshippedOrders.getIterator().getShippingSpeed();
				System.out.println(divider);
				unshippedOrders.getIterator().displayPriceCalculation(vgList, tempShippingSpeed);
				unshippedOrders.advanceIterator();
			}
		}
	}

	public void viewShippedOrders() {
		String divider = "-----------------------------------------------"
				+ "-------------------------------------------";
		String t2 = "\t\t", t3 = "\t\t\t", t4 = "\t\t\t\t";
		if (shippedOrders.isEmpty()) {
			System.out.println("You don't have any shipped orders!");
		} else {
			shippedOrders.placeIterator();
			for (int i = 0; i < shippedOrders.getLength(); i++) {
				List<VideoGame> vgList = shippedOrders.getIterator()
						.getOrderContents();
				vgList.placeIterator();
				System.out.println(t4 + "[Order #" + (i + 1) + "]\n"
						+ "      " + t2 + "  Price" + t4 + "Title\n" + divider);
				for (int j = 0; j < vgList.getLength(); j++) {
					VideoGame currVG = vgList.getIterator();
					System.out.println(t2 + "  "
							+ df.format(currVG.getPrice()) + "\t" + currVG.getTitle() 
							+ " (" + currVG.getPlatform() + ")");
					vgList.advanceIterator();
				}
				int tempShippingSpeed = shippedOrders.getIterator().getShippingSpeed();
				System.out.println(divider);
				shippedOrders.getIterator().displayPriceCalculation(vgList, tempShippingSpeed);
				shippedOrders.advanceIterator();

			}
		}
	}
	
	@Override public String toString() {
		String result = username + "\n"
				+ super.toString()
				+ address + "\n"
				+ city + "\n"
    		    + state + "\n"
    		    + zip + "\n\n"
				+ unshippedOrders.getLength() + "\n"
    		    + unshippedOrders
				+ "\n"
				+ shippedOrders.getLength() + "\n"
    		    + shippedOrders
				+ "\n";
		return result;
	}
	
	public boolean equals(Customer cust, String s) throws NullPointerException{
        if(cust == null) {
        	throw new NullPointerException("Customer.equals(): customer is null cannot equal");
        }else if(cust == this) {
            return true;
        }else {
        	String name = this.getFirstName() + this.getLastName();
        	if (name.equals(cust.getFirstName() + cust.getLastName())) {
        		return true;
        	} else {
        		return false;
        	}
        }
	}

	public void displayCustomer() {
		String divider = "---------------------------------------"
						+ "-----------------------------";
		String t2 = "\t\t";
		System.out.println(divider + "\n" + t2 + "      Customer's Contact Info\n" + divider);
		System.out.println(t2 + "Email: " + getEmail());
		System.out.println(t2 + "Username: " + getUsername());
		System.out.println(t2 + "Address: " + getAddress() + " " + getCity() + ", " + getState());
	}
}
