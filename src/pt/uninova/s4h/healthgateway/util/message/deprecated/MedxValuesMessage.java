package pt.uninova.s4h.healthgateway.util.message.deprecated;

/**
 * Class to hold MedX values message.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class MedxValuesMessage {

    private float timestamp;
    private float force;
    private float angle;
    private short headl;
    private short headr;
    private short handsl;
    private short handsr;
    private short back;
    private float legsl;
    private float legsr;
    private float footl;
    private float footr;

    public MedxValuesMessage(float timestamp, float force, float angle, short headl, short headr, short handsl, short handsr, short back, float legsl, float legsr, float footl, float footr) {
        this.timestamp = timestamp;
        this.force = force;
        this.angle = angle;
        this.headl = headl;
        this.headr = headr;
        this.handsl = handsl;
        this.handsr = handsr;
        this.back = back;
        this.legsl = legsl;
        this.legsr = legsr;
        this.footl = footl;
        this.footr = footr;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public short getHeadL() {
        return headl;
    }

    public short getHeadR() {
        return headr;
    }    
    
    public void setHeadL(short head) {
        this.headl = head;
    }

    public void setHeadR(short head) {
        this.headr = head;
    }    
    
    public short getHandsL() {
        return handsl;
    }

    public short getHandsR() {
        return handsr;
    }    
    
    public void setHandsL(short hands) {
        this.handsl = hands;
    }

    public void setHandsR(short hands) {
        this.handsr = hands;
    }    
    
    public short getBack() {
        return back;
    }

    public void setBack(short back) {
        this.back = back;
    }

    public float getLegsL() {
        return legsl;
    }
    
    public float getLegsR() {
        return legsr;
    }    

    public void setLegsL(float legs) {
        this.legsl = legs;
    }
    
    public void setLegsR(float legs) {
        this.legsr = legs;
    }    

    public float getFootL() {
        return footl;
    }

    public float getFootR() {
        return footr;
    }    
    
    public void setFootL(float foot) {
        this.footl = foot;
    }
    
    public void setFootR(float foot) {
        this.footr = foot;
    } 
}