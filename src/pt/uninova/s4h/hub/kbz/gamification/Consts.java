package pt.uninova.s4h.hub.kbz.gamification;

public final class Consts {
	public  static final int startDegree = 63;
	public  static final int endDegree = 0;
	public  static final int sessionMaxRepetitions = 3;
	public  static final int movTime = 4000;
	public  static final int restTime = 2000;
	public  static final int sessionFullTimer = sessionMaxRepetitions * (movTime*2 +restTime);
	public  static final int repetitionFullTimer = movTime*2 +restTime;
	public  static final long refreshRate = 100;
	public  static final double margin = 5.0; // margin to accept
	public  static final int repSamples = (int) ((movTime + restTime + movTime) / refreshRate);
	public  static final int halfRepSamples = (int) ((movTime + movTime) / refreshRate);
	public  static final String FRONT_MOV = "front";
	public  static final String BACK_MOV = "back";
	public  static final String HOLD_MOV = "hold";
	public  static final String EARLY = "early";
	public  static final String LATE = "late";
	public static final String OK = "OK";
	public  static final String NOT_OK = "NOT_OK";
	public  static final String FINALIMAGE = "FINISH";

}
