/**
 * VideoGame.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

import java.util.Comparator;
import java.text.DecimalFormat;

public class VideoGame {
    private String title, developer, genre, esrb, platform;
    private double price;
    private int releaseDate, metaCriticScore;
    private boolean inStock;

    //constructors
	public VideoGame(String title, String developer, int releaseDate,
			double price, String genre, String esrb, int metaCriticScore,
			String platform, boolean inStock) {
		this.title = title;
		this.developer = developer;
		this.releaseDate = releaseDate;
		this.price = price;
		this.genre = genre;
		this.esrb = esrb;
		this.metaCriticScore = metaCriticScore;
		this.platform = platform;
		this.inStock = inStock;
	}

    public VideoGame(String title) {
        this.title = title;
        developer = "no developer";
        releaseDate = 00000000;
        price = 0;
        genre = "no genre";
        esrb = "no ESRB";
        metaCriticScore = 0;
        platform = "no platform";
        inStock = true;
    }

    public VideoGame(int releaseDate) {
        title = "no title";
        developer = "no developer";
        this.releaseDate = releaseDate;
        price = 0;
        genre = "no genre";
        esrb = "no ESRB";
        metaCriticScore = 0;
        platform = "no platform";
        inStock = true;
    }

    //accessors
    public String getTitle() {
        return title;
    }

    public String getDeveloper() {
        return developer;
    }

    public int getDate() {
        return releaseDate;
    }

    public double getPrice() {
        return price;
    }

    public String getGenre() {
        return genre;
    }

    public String getESRB() {
        return esrb;
    }

    public double getScore() {
        return metaCriticScore;
    }

    public String getPlatform() {
        return platform;
    }
    
    public boolean getInStock() {
    	return inStock;
    }

    //mutators
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setESRB(String esrb) {
        this.esrb = esrb;
    }

    public void setScore(int metaCriticScore) {
        this.metaCriticScore = metaCriticScore;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public void setAvailability(boolean inStock) {
    	this.inStock = inStock;
    }

    //additional operations

    public void printContent() {
    	DecimalFormat df = new DecimalFormat("$####0.00");
		System.out.println("\t\t  "+ df.format(price) + " - " + title + " (" + platform + ")");
    }
    
    public void printContentNoDash() {
    	DecimalFormat df = new DecimalFormat("$####0.00");
		System.out.println("\t\t  "+ df.format(price) + "  " + title + " (" + platform + ")");
    }
    
    @Override public String toString() {
    	DecimalFormat df = new DecimalFormat("##0.00");
        String dateStr = "" + releaseDate;
        String S = "Title: " + title + "\n"
                + "Developer: " + developer + "\n"
				+ "Release Date: " + dateStr.substring(4, 6) + "/"
				+ dateStr.substring(6) + "/" + dateStr.substring(0, 4) + "\n"
                + "Price: $" + df.format(price) + "\n"
                + "Genre: " + genre + "\n"
                + "ESRB Rating: " + esrb + "\n"
                + "Metacritic Score: " + metaCriticScore + "\n"
                + "Platform: " + platform + "\n"
                + "In Stock: " + inStock + "\n";
        return S;
    }

    public String toText() {
        String S = title + "\n"
                + developer + "\n"
                + releaseDate + "\n"
                + price + "\n"
                + genre + "\n"
                + esrb + "\n"
                + metaCriticScore + "\n"
                + platform + "\n"
        		+ inStock + "\n";
        return S;
    }
    
    //compares this videoGame to another object for equality, compares titles
    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof VideoGame)) {
            return false;
        } else {
            VideoGame otherGame = (VideoGame) o;
            return otherGame.getTitle().equals(title);
        }
    }

    //adds primary and secondary keys, title and developer, and converts to unicode
    @Override public int hashCode() {
        String key = title + releaseDate;
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += (int) key.charAt(i);
        }
        return sum;
    }
}

//Comparators, compare by
class TitleComparator implements Comparator<VideoGame> {
	public int compare(VideoGame v1, VideoGame v2) {
		return v1.getTitle().compareTo(v2.getTitle());
	}
}

class DateComparator implements Comparator<VideoGame> {
    public int compare(VideoGame v1, VideoGame v2) {
        return v1.getDate() - v2.getDate();
    }
}

