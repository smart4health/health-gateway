package pt.uninova.s4h.healthgateway.util.message.deprecated;

/**
 * Class to hold HealthGateway connection message.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgConnectionMessage {

    private String commPort;
    
    public HgConnectionMessage(String commPort) {
        this.commPort = commPort;
    }

    public String getCommPort() {
        return commPort;
    }

    public void setCommPort(String commPort) {
        this.commPort = commPort;
    }
}
