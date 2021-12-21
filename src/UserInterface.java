/**
 * UserInterface.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	private static final String vgFile = ("products.txt"),
			custFile = ("customers.txt"), empFile = ("employees.txt");
	public static String fName, lName, email, addr, city, state, pw, title,
						 username, fullNameKey, emailPWKey;
	public static int zip;
	public static Customer currentC = null;
	public static Employee currentEmp = null;
	public static final TitleComparator tc = new TitleComparator();
	public static final DateComparator dc = new DateComparator();
	public static final OrderComparator oc = new OrderComparator();
	private static Scanner input;
	
	public static void main(String[] args) {
		final int custSize = 5, empSize = 3;
		int numChoice;
		String userType;
		HashTable<Customer> custHT = new HashTable<>(custSize * 2);
		HashTable<Customer> custByName = new HashTable<>(custSize * 2);
		HashTable<Employee> empHT = new HashTable<>(empSize * 2);
		BST<VideoGame> vgByTitle = new BST<>();
		BST<VideoGame> vgByDate = new BST<>();
		ArrayList<Order> tempOrderAl = new ArrayList<>();
		tempOrderAl.add(null);
		Heap<Order> priorityQueue = new Heap<>(tempOrderAl, oc);

		try {
			fileToVG(vgByTitle, vgByDate);
			fileToCustandPQ(custHT, custByName, vgByTitle, priorityQueue);
			fileToEmp(empHT);
		} catch (FileNotFoundException e) {
			System.out.println("File(s) not found, please make sure it is in the project"
					+ "folder and rereun the program.");
		}
		input = new Scanner(System.in);
		System.out.println("Welcome to Triforce Games! \n");
		System.out.println("Please note that we don't offer refunds after you place your orders!\n");
		System.out.println("[Please select your user type]\n"
				+ "1. Customer\n"
				+ "2. Employee");
		System.out.print("\nPlease enter 1 or 2: ");
		userType = input.nextLine();
		do {
			try {
				numChoice = Integer.parseInt(userType);
				if (numChoice == 1 || numChoice == 2) {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
			System.out.print("Please enter 1 or 2: ");
			userType = input.nextLine();
		} while (true);
		
		if (numChoice == 1) {
			custInterface(custHT, custByName, vgByTitle, vgByDate, priorityQueue);
		} else {
			empInterface(vgByTitle, vgByDate, custHT, custByName, empHT, priorityQueue);
		}
		try {
			customerToFile(custHT);
			setVgFile(vgByTitle);
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public static void custAccSetup(HashTable<Customer> custHT,
			HashTable<Customer> custByName) {
		int numChoice;
		String ans;
		String createAcc = "\nLet's create an account for you!\n";
		String enterUsername = "Enter your username: ";
		String createPW = "Create a password: ";
		boolean restart = true;
		while (restart) {
			System.out.println("\nWelcome to our store, please login here!");
			System.out.println("\n[Choose your customer type]\n"
					+ "1. Guest\n"
					+ "2. New Customer\n"
					+ "3. Existing Customer");
			System.out.print("\nPlease enter 1, 2, or 3: ");
			ans = input.nextLine();
			do {
				try {
					numChoice = Integer.parseInt(ans);
					if (numChoice >= 1 && numChoice <= 3) {
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
				}
				System.out.print("Please enter 1, 2, or 3: ");
				ans = input.nextLine();
			} while (true);
			if (numChoice == 1) {
				System.out.println("\nPlease start by filling out "
						+ "your shipping info!");
				createAccount();
				emailPWKey = email + pw;
				fullNameKey = fName + lName;
				currentC = new Customer(fName, lName, email, addr,
						city, state, zip);
				custHT.insert(currentC, emailPWKey);
				custByName.insert(currentC, emailPWKey);
				System.out.println("\nThank you for filling out your shipping info, "
								+ fName + " " + lName + "!");
				restart = false;
			} else if (numChoice == 2) {
				createAccount();
				System.out.println(createAcc);
				System.out.print(enterUsername);
				username = input.nextLine();
				System.out.print(createPW);
				pw = input.nextLine();
				emailPWKey = email + pw;
				fullNameKey = fName + lName;
				currentC = new Customer(username, fName, lName, email, pw, addr,
						city, state, zip);
				custHT.insert(currentC, emailPWKey);
				custByName.insert(currentC, emailPWKey);
				System.out.println("\nYou have successfully created an account, "
						+ fName + " " + lName + "!\n");
				restart = false;
			} else if (numChoice == 3){
				System.out.print("\nEnter your email address: ");
				email = input.nextLine();
				System.out.print("Enter your password: ");
				pw = input.nextLine();
				emailPWKey = email + pw;
				fullNameKey = fName + lName;
				Customer tempC = new Customer(email, pw);
				boolean signinStatus = custHT.contains(tempC, emailPWKey);
				if (!(signinStatus)) {
					System.out.println("\nIt appears we don't have "
							+ "your account on file...\n");
					System.out.print("Would you like to try again? (y/n): ");
					ans = input.nextLine();
					while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
						System.out.print("Please enter \"y\" or \"n\": ");
						ans = input.nextLine();
					}
					if (ans.equalsIgnoreCase("y")) {
						restart = true;
					} else {
						System.out.println(createAcc);
						System.out.print(enterUsername);
						username = input.nextLine();
						System.out.print(createPW);
						pw = input.nextLine();
						createAccount();
						currentC = new Customer(username, fName, lName, email, pw, addr,
								city, state, zip);
						custHT.insert(currentC, emailPWKey);
						custByName.insert(currentC, fullNameKey);
						System.out.println("\nYou have successfully created an account, "
								+ fName + " " + lName + "!\n");
						restart = false;
					}
				} else {
					currentC = custHT.get(tempC, emailPWKey);
					System.out.println("\nWelcome back, " + currentC.getFirstName() + " "
							+ currentC.getLastName() + "!\n");
					restart = false;
				}
			}
		}
	}

	public static void createAccount() {
		System.out.print("\nEnter your first name: ");
		fName = input.nextLine();
		System.out.print("Enter your last name: ");
		lName = input.nextLine();
		System.out.print("Enter your email: ");
		email = input.nextLine();
		System.out.print("Enter your address: ");
		addr = input.nextLine();
		System.out.print("Enter your city: ");
		city = input.nextLine();
		System.out.print("Enter your state: ");
		state = input.nextLine();
		System.out.print("Enter your zipcode: ");
		zip = input.nextInt();
		input.nextLine(); // clear buffer
	}

	public static void custInterface(HashTable<Customer> custHT,
									 HashTable<Customer> custByName, BST<VideoGame> vgByTitle,
									 BST<VideoGame> vgByDate, Heap<Order> priorityQueue) {
		String choice = "", ans;
		custAccSetup(custHT, custByName);
		while (!choice.equalsIgnoreCase("X")) {
			if (currentC != null) {
				displayCustMenu();
			}
			System.out.print("Enter your choice: ");
			choice = input.nextLine();
			
			switch (choice.toUpperCase()) {
				case "1":
					placeOrder(vgByTitle, priorityQueue);
					break;
				case "2":
					listVG(vgByTitle, vgByDate);
					break;
				case "3":
					searchVG(vgByTitle, vgByDate);
					break;
				case "4":
					viewOrders();
					break;
				case "5":
					System.out.println("\nWould you like to sign out?\n");
					System.out.print("Enter (y/n): ");
					ans = input.nextLine();
					while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
						System.out.print("Please enter \"y\" or \"n\": ");
						ans = input.nextLine();
					}
					if (ans.equalsIgnoreCase("y")) {
						try {
							customerToFile(custHT);
							setVgFile(vgByTitle);
						} catch (IOException e) {
							e.getMessage();
						}
						custAccSetup(custHT, custByName);
					}
					break;
				case "X":
					System.out.println("\nGoodbye! Thank you for being a valued customer!"
							+ "\nWe hope to see you again!");
					break;
				default:
					System.out.println("\nInvalid menu option."
							+ " Please enter 1-5 or X to exit.");
					break;
			}
		}
	}

	public static void empLogin(HashTable<Employee> empHT) {
		System.out.println("\n[Employee Login Menu]");
		System.out.println("\nWelcome back! Please login here:");
		System.out.println("--------------------------------\n");
		System.out.print("Enter your email address: ");
		email = input.nextLine();
		System.out.print("Enter your password: ");
		pw = input.nextLine();
		currentEmp = new Employee(email, pw);
		while (!(empHT.contains(currentEmp, emailPWKey))) {
			System.out.println("\nPlease make sure you entered your correct"
					+ " case sensitive email and password!");
			System.out.print("\nEnter your email address: ");
			email = input.nextLine();
			System.out.print("\nEnter your password: ");
			pw = input.nextLine();
			currentEmp = new Employee(email, pw);
		}
		currentEmp = empHT.get(currentEmp, emailPWKey);
		System.out.println("\nWelcome back, " + currentEmp.getFirstName()
				+ " " + currentEmp.getLastName() + "!\n");
	}

	public static void empInterface(BST<VideoGame> vgByTitle,
									BST<VideoGame> vgByDate, HashTable<Customer> custHT,
									HashTable<Customer> custByName, HashTable<Employee> empHT,
									Heap<Order> priorityQueue) {
		String choice = "", ans;
		empLogin(empHT);
		while (!choice.equalsIgnoreCase("X")) {
			displayEmpMenu();
			System.out.print("Enter your choice: ");
			choice = input.nextLine();
			switch (choice.toUpperCase()) {
				case "1":
					viewPriorityQueue(priorityQueue);
					break;
				case "2":
					Employee.viewCustomers(custHT);
					break;
				case "3":
					searchingCust(custByName);
					break;
				case "4":
					shipOrder(priorityQueue);
					break;
				case "5":
					listVG(vgByTitle, vgByDate);
					break;
				case "6":
					addVG(vgByTitle, vgByDate);
					break;
				case "7":
					removeVG(vgByTitle, vgByDate);
					break;
				case "8":
					System.out.println("\nWould you like to sign out?\n");
					System.out.print("Enter (y/n): ");
					ans = input.nextLine();
					while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
						System.out.print("Please enter \"y\" or \"n\": ");
						ans = input.nextLine();
					}
					if (ans.equalsIgnoreCase("y")) {
						try {
							customerToFile(custHT);
							setVgFile(vgByTitle);
						} catch (IOException e) {
							e.getMessage();
						}
						empLogin(empHT);
					}
					break;
				case "X":
					System.out.println("\nGoodbye! Thank you for your hard work, "
									+ currentEmp.getFirstName() + " "+ currentEmp.getLastName() + "!");
					break;
				default:
					System.out.println("\nInvalid menu option."
							+ " Please enter 1-8 or X to exit.");
					break;
			}
		}
	}

	public static void searchingCust(HashTable<Customer> custByName) {
		System.out.print("\nPlease type in the first name of the customer "
						+ "you are searching for: ");
		fName = input.nextLine();
		System.out.print("\nPlease type in the last name of the customer "
						+ "you are searching for: ");
		lName = input.nextLine();
		Customer cust = Employee.searchCustomer(fName, lName, custByName);
		if (cust == null) {
			System.out.println("Customer doesn't exist!");
		} else {
			System.out.println("Customer has been found:\n");
			cust.displayCustomer();
		}
	}
	
	public static void placeOrder(BST<VideoGame> vgByTitle, Heap<Order> priorityQueue) {
		Order placeOrder;
		String userInput, ans;
		int numChoice;
		String divider = "-------------------------------------------"
					+ "-----------------------------------------------";
		String t4 = "\t\t\t\t";
		List<VideoGame> unshippedVG = new List<>();
		System.out.println();
		vgByTitle.inOrderPrint();
		System.out.print("Enter the number of games you would like to purchase: ");
		
		userInput = input.nextLine();
		do {
			try {
				numChoice = Integer.parseInt(userInput);
				if (numChoice >= 1) {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.print("Please enter a number greater than 0: ");
				userInput = input.nextLine();
			}
			
								
		} while (true);

		for (int i = 0; i < numChoice; i++) {
			if (numChoice == 1) {
				System.out.print("\nEnter the case sensitive title of the video game you'd like to purchase: ");
			} else {
			System.out.print("\nEnter the case sensitive title of the video game purchase #" + (i + 1) + ": ");
			}
			userInput = input.nextLine();
			VideoGame tempVG = new VideoGame(userInput);
			tempVG = vgByTitle.search(tempVG, tc);
			while (tempVG == null) {
				System.out.print("Please confirm correct spelling. Enter case sensitive title: ");
				userInput = input.nextLine();
				tempVG = new VideoGame(userInput);
				tempVG = vgByTitle.search(tempVG, tc);
			}
			if (tempVG.getInStock() == false) {
				System.out.println("\nSorry, " + tempVG.getTitle() + " is currently out of stock.");
			} else {
				unshippedVG.addLast(tempVG);
			}
		}
		if (!unshippedVG.isEmpty()) {
			System.out.println("\nWhich shipping option would you like to choose?\n");
			System.out.println("1. Standard Shipping (5 Days): $4.95 *Free for orders over $35!*"
					+ "\n2. Rush Shipping (2 Days): $7.95" + "\n3. Overnight Shipping (1 Day): $14.95\n");
			System.out.print("Please choose your desired shipping speed: ");
			userInput = input.nextLine();
			do {

				try {
					numChoice = Integer.parseInt(userInput);

					if (numChoice >= 1 && numChoice <= 3) {
						break;
					}

				} catch (NumberFormatException e) {
				}

				System.out.print("Input must be a number between 1 and 3: ");
				userInput = input.nextLine();
			} while (true);

			switch (numChoice) {
			case 1:
				numChoice = 5;
				break;
			case 2:
				numChoice = 2;
				break;
			case 3:
				numChoice = 1;
				break;
	
			}
		System.out.println("\nThank you, your order is being processed!\n");
		placeOrder = new Order(currentC, unshippedVG, numChoice, false);
		System.out.println(t4 + "[Order Info]\n"
				+ "\t\t  Price" + t4 + "Title\n" + divider);
		unshippedVG.placeIterator();
		for (int i = 0; i < unshippedVG.getLength(); i++) {
			unshippedVG.getIterator().printContentNoDash();
			unshippedVG.advanceIterator();
		}
		System.out.println(divider);
		placeOrder.displayPriceCalculation(unshippedVG, numChoice);
		priorityQueue.insert(placeOrder);
		currentC.placeUnshippedOrder(placeOrder);
		} else {
			System.out.println("\nOrder empty, returning to main menu.");
		}
	}

	public static void viewOrders() {
		String ans = "";
		System.out.println("\n[Viewing Order(s) Submenu]");
		System.out.println("\nWhich would you like to view?\n\n"
				+ "U: My Unshipped Orders\n"
				+ "S: My Shipped Orders\n");
		System.out.print("Enter your choice: ");
		ans = input.nextLine();
		while (!ans.equalsIgnoreCase("u") && !ans.equalsIgnoreCase("s")) {
			System.out.print("Please enter \"u\" or \"s\": ");
			ans = input.nextLine();
		}
		if (ans.equalsIgnoreCase("U")) {
			if (!currentC.getUsername().equals("NA")) {
				System.out.println("\n\t\t\t[" + currentC.getUsername()
						+ "'s Unshipped Orders]\n");
				currentC.viewUnshippedOrders();
			} else {
				System.out.println("\n\t\t\t[" + currentC.getFirstName() + " "
						+ currentC.getLastName() + "'s Unshipped Orders]\n");
				currentC.viewUnshippedOrders();
			}
			
		} else if (ans.equalsIgnoreCase("S")) { // for typos
			if (!currentC.getUsername().equals("NA")) {
				System.out.println("\n\t\t\t[" + currentC.getUsername()
						+ "'s Shipped Orders]\n");
				currentC.viewShippedOrders();
			} else {
				System.out.println("\n\t\t\t[" + currentC.getFirstName() + " "
						+ currentC.getLastName() + "'s Shipped Orders]\n");
				currentC.viewShippedOrders();
			}
		}
	}

	public static void shipOrder(Heap<Order> priorityQueue) {
		String divider = "--------------------------------------------------"
						+ "----------------------------------------";
		String t3 = "\t\t\t", t4 ="\t\t\t\t";
		Customer tempC;
		if (priorityQueue.getHeapSize() == 0) {
			System.out.println("\nThere aren't any orders to ship!");
		} else {
			System.out.println("\nShipping the next order with the highest priority!\n");
			Order tempOrder = priorityQueue.getMax();
			//TODO: check pq's size to see if there are orders to ship
			priorityQueue.remove(1);
			List<VideoGame> tempOrderVG = tempOrder.getOrderContents();
			tempC = tempOrder.getCustomer();
			System.out.print(divider + "\n" + t3 + tempC.getFirstName() + " "
					+ tempC.getLastName() + "'s Unshipped Order\n" + divider
					+ "\n" + t4 + "Date ordered: " + tempOrder.getCurrentDate()
					+ "\n\n" + t3 + "Shipping type: ");
			if (tempOrder.getShippingSpeed() == 1) {
				System.out.print("[Overnight Shipping]\n\n");
			} else if (tempOrder.getShippingSpeed() == 2) {
				System.out.print("[Rush Shipping]\n\n");
			} else {
				System.out.print("[Standard Shipping]\n\n");
			}
			tempOrderVG.placeIterator();
			while (!tempOrderVG.offEnd()) {
				tempOrderVG.getIterator().printContent();
				tempOrderVG.advanceIterator();
			}
			int tempShippingSpeed = tempOrder.getShippingSpeed();
			System.out.println(divider);
			tempOrder.displayPriceCalculation(tempOrderVG, tempShippingSpeed);
			currentC = tempOrder.getCustomer();
			currentC.displayCustomer();
			currentC.removeUnshippedOrder(tempOrder);
			currentC.placeShippedOrder(tempOrder);
			System.out.println("\nThe order has been shipped!");
			}
	}

	public static void searchVG(BST<VideoGame> vgByTitle, BST<VideoGame> vgByDate) {
		VideoGame searchVG;
		String choice = "0";
		String dateStr;
		int date;
		while(!choice.equals("1") && !choice.equals("2")) {
			System.out.println("\nWhat would you like to search by?\n" +
					"1. Title\n" +
					"2. Release Date\n");
			System.out.print("Enter your choice: ");
			choice = input.nextLine();
			while(!choice.equals("1") && !choice.equals("2")) {
				System.out.print("Please enter \"1\" or \"2\": ");
				choice = input.nextLine();
			}
		}
		System.out.println("\nWhich video game would you like to search for?");
		if (choice.equals("1")) {
			System.out.print("\nEnter the title: ");
			title = input.nextLine();
			searchVG = new VideoGame(title);
			searchVG = vgByTitle.search(searchVG, tc);
			if (searchVG != null) {
				System.out.println("\nWe were able to find this video game: \n\n"
						+ searchVG);
			} else {
				System.out.println("Sorry, we don't have " + title + " in our catalog yet!");
			}
		} else {
			System.out.print("\nEnter the release date (YYYYMMDD format, without slashes): ");
			dateStr = input.nextLine();
			date = Integer.parseInt(dateStr);
			searchVG = new VideoGame(date);
			searchVG = vgByDate.search(searchVG, dc);
			if (searchVG != null) {
				System.out.println("\nWe were able to find these video game(s) with that release date: \n\n"
						+ searchVG);
			} else {
				System.out.println("Sorry, we don't have any video games with this release date in our catalog!");
			}
		}
	}

	public static void listVG(BST<VideoGame> vgByTitle,
							  BST<VideoGame> vgByDate) {
		String choice = "";
		System.out.println("\nHow would you like to sort the"
				+ " available video games?\n"
				+ "1. By Title\n"
				+ "2. By Release Date");
		System.out.print("\nEnter your choice: ");
		choice = input.nextLine();
		System.out.println();
		while (!(choice.equals("1") || choice.equals("2"))) {
			// TODO: Is this fixed now?
			System.out.println("Please enter \"1\" or \"2\": ");
			choice = input.nextLine();
		}
		if (choice.equals("1")) {
			vgByTitle.inOrderPrint();
		} else if (choice.equals("2")) {
			vgByDate.inOrderPrint();
		} else {
			System.out.println("Invalid Input, Please enter only 1 or 2!");
		}
	}


	public static void addVG(BST<VideoGame> vgByTitle,
							 BST<VideoGame> vgByDate) {
		String dev, genre, ESRB, pform;
		double price;
		int rDate, mcScore;
		boolean avail;

		System.out.print("\nPlease enter the title of the video game: ");
		title = input.nextLine();
		VideoGame tempVG = new VideoGame(title);
		tempVG = vgByTitle.search(tempVG, tc);
		if (tempVG == null) {
			System.out.print("Please enter the developer of " + title + ": ");
			dev = input.nextLine();
			System.out.print("Please enter the release date (YYYYMMDD): ");
			rDate = input.nextInt();
			System.out.print("Please enter the price: $");
			price = input.nextDouble();
			input.nextLine();
			System.out.print("Please enter the genre: ");
			genre = input.nextLine();
			System.out.print("Please enter the ESRB (Entertainment Software Rating Board) Rating: ");
			ESRB = input.nextLine();
			System.out.print("Please enter the Metacritic Score: ");
			mcScore = input.nextInt();
			input.nextLine();
			System.out.print("Please enter the platform: ");
			pform = input.nextLine();
			System.out.print("Please enter whether or not title is in stock(\"true\" or \"false\"): ");
			avail = input.nextBoolean();
			input.nextLine();
			VideoGame newVG = new VideoGame(title, dev, rDate, price, genre, ESRB,
					mcScore, pform, avail);
			Employee.addProduct(vgByTitle, vgByDate, newVG, tc, dc);
		} else {
			System.out.println(
					"\nThis video game already exists in our system!");
		}
	}

	public static void removeVG(BST<VideoGame> vgByTitle,
								BST<VideoGame> vgByDate) {
		System.out.print("\nPlease type in the title of the Video Game you want to remove: ");
		title = input.nextLine();
		VideoGame vg = vgByTitle.search(new VideoGame(title), tc);
		if (vg != null) {
			Employee.updateAvailability(vgByTitle, vgByDate, vg, tc, dc);
			System.out.println(title + " has been successfully tagged as out of stock!");
		} else {
			System.out.println("Cannot find " + title + "in our product catalog, please try again!");
		}
	}

	public static void displayCustMenu() {
		System.out.println("\n[Customer Main Menu]\n\n"
				+ "Please select from the following options:\n\n"
				+ "1. Place Order\n"
				+ "2. List Video Games\n"
				+ "3. Search for Video Game\n"
				+ "4. View Unshipped and Shipped Orders");
		if (currentC.getUsername().equalsIgnoreCase("NA")) {
			System.out.println("5. Sign out as a Guest\n"
								+ "X. Exit\n");
		} else {
			System.out.println("5. Sign out of your account\n"
								+ "X. Exit\n");
		}
	}

	public static void displayEmpMenu() {
		System.out.println("\n[Employee Main Menu]\n\n"
				+ "Please select from the following options:\n\n"
				+ "1. View Orders by Priority\n"
				+ "2. Display Customer Info\n"
				+ "3. Search for Customer\n"
				+ "4. Ship Orders\n"
				+ "5. List Video Games\n"
				+ "6. Add New Product\n"
				+ "7. Remove a Product\n"
				+ "8. Sign Out of Your Account\n"
				+ "X. Exit\n");
	}

	public static void fileToCustandPQ(HashTable<Customer> custHT,
			HashTable<Customer> custByName, BST<VideoGame> vgByTitle,
			Heap<Order> priorityQueue) throws FileNotFoundException {
		String address;
		long priority;
		int numGames, uNumOrders, sNumOrders = 0, uShipSpeed = 0, sShipSpeed = 0;
		String orderDate = ""; // for orders
		File file = new File(custFile);
		input = new Scanner(file);
		while (input.hasNextLine()) {
			username = input.nextLine();
			fName = input.nextLine();
			lName = input.nextLine();
			email = input.nextLine();
			pw = input.nextLine();
			address = input.nextLine();
			city = input.nextLine();
			state = input.nextLine();
			zip = input.nextInt();
			if (input.hasNextLine()) {
				input.nextLine();
			}
			Customer newC = new Customer(username, fName, lName, email, pw,
					address, city, state, zip);
			uNumOrders = input.nextInt();
			for (int i = 0; i < uNumOrders; i++) {
				List<VideoGame> unshippedVG = new List<>();
				uShipSpeed = input.nextInt();
				input.nextLine();
				orderDate = input.nextLine();
				priority = input.nextLong();
				numGames = input.nextInt();
				input.nextLine();
				for (int j = 0; j < numGames; j++) {
					title = input.nextLine();
					VideoGame tempVG = new VideoGame(title);
					tempVG = vgByTitle.search(tempVG, tc);
					unshippedVG.addLast(tempVG);
				}
				Order unShippedOrder = new Order(newC, orderDate, unshippedVG,
						uShipSpeed, false, priority);
				newC.placeUnshippedOrder(unShippedOrder);
				priorityQueue.insert(unShippedOrder);
			}
			sNumOrders = input.nextInt();
			if (input.hasNextLine()) {
				input.nextLine();
			}
			for (int i = 0; i < sNumOrders; i++) {
				List<VideoGame> shippedVG = new List<>();
				sShipSpeed = input.nextInt();
				input.nextLine();
				orderDate = input.nextLine();
				priority = input.nextLong();
				numGames = input.nextInt();
				input.nextLine();
				for (int j = 0; j < numGames; j++) {
					title = input.nextLine();
					VideoGame tempVG = new VideoGame(title);
					tempVG = vgByTitle.search(tempVG, tc);
					shippedVG.addLast(tempVG);
				}
				Order shippedOrder = new Order(newC, orderDate, shippedVG,
						sShipSpeed, true, priority);
				newC.placeShippedOrder(shippedOrder);
			}
			if (input.hasNextLine()) {
				input.nextLine();
			}
			emailPWKey = email + pw;
			fullNameKey = fName + lName;
			custHT.insert(newC, emailPWKey);
			custByName.insert(newC, fullNameKey);
		}
		input.close();
	}

	public static void fileToEmp(HashTable<Employee> empHT)
			throws FileNotFoundException {
		File file = new File(empFile);
		input = new Scanner(file);
		while (input.hasNextLine()) {
			fName = input.nextLine();
			lName = input.nextLine();
			email = input.nextLine();
			pw = input.nextLine();
			if (input.hasNextLine()) {
				input.nextLine();
			}
			Employee newE = new Employee(fName, lName, email, pw);
			empHT.insert(newE, emailPWKey);
		}
		input.close();
	}

	public static void fileToVG(BST<VideoGame> vgByTitle,
								BST<VideoGame> vgByDate)
			throws FileNotFoundException {
		String dev, genre, ESRB, pform;
		double price;
		int rDate, mcScore;
		boolean avail;
		File file = new File(vgFile);
		input = new Scanner(file);
		while (input.hasNextLine()) {
			title = input.nextLine();
			dev = input.nextLine();
			rDate = input.nextInt();
			price = input.nextDouble();
			input.nextLine(); // clear buffer
			genre = input.nextLine();
			ESRB = input.nextLine();
			mcScore = input.nextInt();
			input.nextLine();
			pform = input.nextLine();
			avail = input.nextBoolean();
			input.nextLine();
			if (input.hasNextLine()) {
				input.nextLine();
			}
			VideoGame newVG = new VideoGame(title, dev, rDate, price, genre,
					ESRB, mcScore, pform, avail);
			vgByTitle.insert(newVG, tc);
			vgByDate.insert(newVG, dc);
		}
		input.close();
	}

	public static void viewPriorityQueue(Heap<Order> priorityQueue) {
		ArrayList<Order> tempOrder = priorityQueue.sort();
		Customer tempC;
		int orderPriorityNum = 1;
		String divider = "---------------------------------------------";
		System.out.println("\nHere are the customers' unshipped orders "
				+ "(sorted in terms of priority for being shipped): \n");
		for (int i = tempOrder.size() - 1; i > 0; i--) {
			tempC = tempOrder.get(i).getCustomer();
			if (orderPriorityNum > 1) {
				System.out.println();
			}
			System.out.println(divider + "\n[#" + orderPriorityNum + "] "
					+ tempC.getFirstName() + " " + tempC.getLastName()
					+ "'s Unshipped Order\n" + divider + "\n\t   Order Date: "
					+ tempOrder.get(i).getCurrentDate() + "\n");
			System.out.print("   Shipping type: ");
			if (tempOrder.get(i).getShippingSpeed() == 1) {
				System.out.print("[Overnight Shipping]\n\n");
			} else if (tempOrder.get(i).getShippingSpeed() == 2) {
				System.out.print("[Rush Shipping]\n\n");
			} else {
				System.out.print("[Standard Shipping]\n\n");
			}
			tempOrder.get(i).getShippingSpeed();
			List<VideoGame> indivOrderInfo = tempOrder.get(i)
					.getOrderContents();
			indivOrderInfo.placeIterator();
			for (int j = 0; j < indivOrderInfo.getLength(); j++) {
				VideoGame currVG = indivOrderInfo.getIterator();
				indivOrderInfo.advanceIterator();
				System.out.println("   " + currVG.getTitle());
			}
			orderPriorityNum++;
			if (i == 1) {
				System.out.println("\n" + divider + "------------\n");
			}
		}
	}

	public static void customerToFile(HashTable<Customer> custHT) throws IOException {
		FileWriter myWriter = new FileWriter(custFile);
		myWriter.write(custHT.toString());
		myWriter.close();
	}

	public static void setVgFile(BST<VideoGame> vgByTitle) throws IOException {
		ArrayList<VideoGame> tempAl = vgByTitle.inOrderToAL();
		String fileOutput = "";
		for (int i = 0; i < tempAl.size(); i++) {
			fileOutput += tempAl.get(i).toText();
			fileOutput += "\n";
		}
		FileWriter vgWriter = new FileWriter(vgFile);
		vgWriter.write(fileOutput);
		vgWriter.close();
	}
}
