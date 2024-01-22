import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


enum Gender {
    MALE, FEMALE, OTHER
}


class User {
    private String userId;
    private String username;
    private String password;
    private Gender gender;

    public User(String userId, String username, String password, Gender gender) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User: " + username;
    }
}

