import java.util.ArrayList;

/**
 * Employee.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */
public class Employee extends User {
		
	public Employee(String email, String password) {
		super(email, password);
	}
	
	public Employee(String firstName, String lastName, 
			String email, String password) {
		super(firstName, lastName, email, password);
	}

	public static Customer searchCustomer(String firstName,
										  String lastName, HashTable<Customer> customersByName) {
		String fullNameKey = firstName + lastName;
		int hash = customersByName.getHash(fullNameKey);
		List<Customer> customersList = customersByName.getBucket(hash);
		customersList.placeIterator();
		for (int i = 0; i < customersList.getLength(); i++) {
			if (customersList.getIterator().equals(new Customer(firstName, lastName, "NA"), "")) {
				return customersList.getIterator();
			} else {
				customersList.advanceIterator();
			}
		}
		return null;
	}
	
	public static void addProduct(BST<VideoGame> byTitle, BST<VideoGame> byDate, VideoGame videogame,
			TitleComparator tc, DateComparator dc) {
		byTitle.insert(videogame, tc);
		byDate.insert(videogame, dc);
	}
	
	public static void removeProduct(BST<VideoGame> byTitle, BST<VideoGame> byDate, VideoGame videogame,
			TitleComparator tc, DateComparator dc) {
		byTitle.remove(videogame, tc);
		byDate.remove(videogame, dc);
	}
	
	public static void updateAvailability(BST<VideoGame> byTitle, BST<VideoGame> byDate, VideoGame videogame,
			TitleComparator tc, DateComparator dc) {
		byTitle.search(videogame, tc).setAvailability(false);
		byDate.search(videogame, dc).setAvailability(false);
	}

	@Override public String toString() {
		return super.toString();
	}

	public static void viewCustomers(HashTable<Customer> customerHashTable) {
		System.out.println();
		ArrayList<Customer> custAL = customerHashTable.hashToAl();
		for (int i = 0; i < custAL.size(); i++) {
			custAL.get(i).displayCustomer();
			System.out.println();
		}
	}
}
