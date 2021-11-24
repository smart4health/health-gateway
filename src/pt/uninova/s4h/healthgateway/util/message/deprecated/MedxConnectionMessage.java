package pt.uninova.s4h.healthgateway.util.message.deprecated;

/**
 * Class to hold MedX connection message.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class MedxConnectionMessage {

    private boolean connection;
    private String commPort;
    
    public MedxConnectionMessage(boolean connection, String commPort) {
        this.connection = connection;
        this.commPort = commPort;
    }

    public boolean getConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public String getCommPort() {
        return commPort;
    }

    public void setPort(String commPort) {
        this.commPort = commPort;
    }
}
