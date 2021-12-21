/**
 * Order.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

import java.util.Comparator;
import java.util.Calendar;
import java.text.DecimalFormat;

public class Order {
    private Customer customer;
    private int shippingSpeed;
    private String currentDate;
    private List<VideoGame> orderContents;
    private boolean shippingStatus;
    private long priority;
    private double totalOrderPrice;

    public Order(Customer customer, List<VideoGame> orderContents,
                 int shippingSpeed, boolean shippingStatus) {
        Calendar rightNow = Calendar.getInstance();
        this.currentDate = "" + (rightNow.get(Calendar.MONTH)+1) + "/"
                + rightNow.get(Calendar.DATE) + "/"
                + rightNow.get(Calendar.YEAR);
        this.customer = customer;
        this.orderContents = orderContents;
        this.shippingSpeed = shippingSpeed;
        this.priority = (System.currentTimeMillis() + (shippingSpeed * 86400000));
        this.shippingStatus = shippingStatus;
        this.totalOrderPrice = calculateOrderPrice(orderContents, shippingSpeed);
    }

    public Order(Customer customer, String currentDate, List<VideoGame> orderContents,
                 int shippingSpeed, boolean shippingStatus, long priority) {
        this.currentDate = currentDate;
        this.customer = customer;
        this.orderContents = orderContents;
        this.shippingSpeed = shippingSpeed;
        this.priority = (System.currentTimeMillis() - (shippingSpeed * 86400000));
        this.shippingStatus = shippingStatus;
        this.priority = priority;
        this.totalOrderPrice = calculateOrderPrice(orderContents, shippingSpeed);
    }

    public double calculateOrderPrice(List<VideoGame> orderContents, int shippingSpeed) {
        double orderPrice = 0;
        orderContents.placeIterator();
        for (int i = 0; i < orderContents.getLength(); i++) {
            orderPrice += orderContents.getIterator().getPrice();
        }
        switch (shippingSpeed) {
            case 1:
                orderPrice += 14.95;
                break;
            case 2:
                orderPrice += 7.95;
                break;
            case 5:
                if (orderPrice >= 35) {
                } else {
                    orderPrice += 4.95;
                }
                break;
        }
        orderPrice *= 1.0725;
        return orderPrice;
    }

    public void displayPriceCalculation(List<VideoGame> orderContents, int shippingSpeed) {
        DecimalFormat dc = new DecimalFormat("$###,###,##0.00");
        double orderPrice = 0;
        double shippingPrice = 0;
        double stateTax;
        orderContents.placeIterator();
        for (int i = 0; i < orderContents.getLength(); i++) {
            orderPrice += orderContents.getIterator().getPrice();
            orderContents.advanceIterator();
        }
        System.out.println("\tSubtotal: " + dc.format(orderPrice));
		switch (shippingSpeed) {
		case 1:
			shippingPrice = 14.95;
			orderPrice += shippingPrice;
			break;
		case 2:
			shippingPrice = 7.95;
			orderPrice += shippingPrice;
			break;
		case 5:
			if (orderPrice >= 35) {
			} else {
				shippingPrice = 4.95;
				orderPrice += shippingPrice;
			}
			break;
		}
        System.out.println("+  Shipping Cost: " + dc.format(shippingPrice));
        stateTax = orderPrice * .0725;
        orderPrice += stateTax;
        System.out.println("+   CA Sales Tax: " + dc.format(stateTax));
		System.out.println("---------------------------------------------"
						 + "---------------------------------------------");
        System.out.println("\t   Total: " + dc.format(orderPrice) + "\n");
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int month, int date, int year) {
        this.currentDate = "" + month + "/" + date + "/" + year;
    }

    public List<VideoGame> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(List<VideoGame> orderContents) {
        this.orderContents = orderContents;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public int getShippingSpeed() {
        return shippingSpeed;
    }

    public void setShippingSpeed(int shippingSpeed) {
        this.shippingSpeed = shippingSpeed;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(boolean shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    @Override
    public String toString() { // return string in the format that we are writing out to the file
        String temp = "";
        temp += shippingSpeed + "\n";
        temp += currentDate + "\n";
        temp += priority + "\n";
        temp += orderContents.getLength() + "\n"; // Number of video games in the order
        orderContents.placeIterator();
        for (int i = 0; i < orderContents.getLength(); i++) {
            temp += orderContents.getIterator().getTitle() + "\n";
            orderContents.advanceIterator();
        }
        return temp;
    }

}

//Should also contain a Comparator class with a compare method to determine priority
class OrderComparator implements Comparator<Order> {
    public int compare(Order order1, Order order2) {
        return - Long.compare(order1.getPriority(), order2.getPriority());
    }
}