/**
 * User.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    User(String email, String password) {
        this.email = email;
        this.password = password;
        this.firstName = "firstName unknown";
        this.lastName = "lastName unknown";
    }

    User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean passwordMatch(String anotherPassword) {
    	//haven't used this yet, would be nice but not necess
        return password.equals(anotherPassword);
    }

    @Override public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User user = (User) o;
            if (this.email.equals(user.email) && this.password.equals(user.password)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        String result = firstName + "\n" + lastName + "\n" + email + "\n"
                + password + "\n";
        return result;
    }

    public int hashCode() {
        int key = 0;
        String temp = email + password;
        for (int i = 0; i < temp.length(); i++) {
            key += (int) temp.charAt(i);
        }
        return key;
    }
}