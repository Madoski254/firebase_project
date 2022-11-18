package co.ke.adse2105.firebaseapp2.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class AppUser implements Serializable
{
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date joinDate;

    public AppUser(String firstname, String lastname, String email, String password ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.joinDate = Calendar.getInstance().getTime();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
