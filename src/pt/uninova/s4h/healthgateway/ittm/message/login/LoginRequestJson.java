package pt.uninova.s4h.healthgateway.ittm.message.login;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Login Json file to be sent to the ITTM servers.
 */
public class LoginRequestJson {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
