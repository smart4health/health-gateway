package pt.uninova.s4h.hub.medx.physiotherapy;

import com.fazecast.jSerialComm.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.hub.medx.mqtt.MedxMqttClient;

/**
 * Class to communicate with arduino through serial communication.
 *
 * @author Fábio Januário
 * @email faj@uninova.pt
 * @version 12 November 2019 - v1.0.
 */

public class SerialComm {

    //SerialPi object
    private FXMLDocumentController physio = null;

    //serial communications
    private SerialPort arduino = null;

    //reading buffer    
    private ByteBuffer bufferin = ByteBuffer.allocate(2000);

    //begin message byte
    private final byte BEGIN = (byte) 254;
    //Control byte
    private final byte SEND_MSG = (byte) 10;
    private final byte RECEIVE_MSG = (byte) 20; 
    //Flag Message byte
    private final byte A_FLEXION = (byte) 11;
    private final byte A_FLEXION_CONF = (byte) 12;
    private final byte A_EXTENSION = (byte) 21;
    private final byte A_EXTENSION_CONF = (byte) 22;
    private final byte A_INT = (byte) 23;
    private final byte A_INT_CONF = (byte) 24;
    private final byte FZERO = (byte) 31;
    private final byte FZERO_CONF = (byte) 32;
    private final byte FREF = (byte) 41;
    private final byte FREF_CONF = (byte) 42;
    private final byte PRESSURE = (byte) 43;
    private final byte PRESSURE_CONF = (byte) 44;
    private final byte START = (byte) 51;
    private final byte START_CONF = (byte) 52;
    private final byte STOP = (byte) 61;
    private final byte STOP_CONF = (byte) 62;
    private final byte VALUES = (byte) 71;
    private final byte BUTTON = (byte) 81;
    private final byte IDENT = (byte) 99;
    
    boolean exercise = false;
    
    private FileWriter file = null;
    private MedxMqttClient medxMqttClient = null;
    
    private static final ch.qos.logback.classic.Logger SERIAL_COMM_LOGGER = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SerialComm(FXMLDocumentController physio, MedxMqttClient medxMqttClient) {
        this.physio = physio;
        this.medxMqttClient = medxMqttClient;
    }

    public boolean connect(String comPort) {
        arduino = SerialPort.getCommPort(comPort);
        arduino.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        if (arduino.openPort()) {
            bufferin.order(ByteOrder.LITTLE_ENDIAN);
            arduino.addDataListener(ReceiveMSGArduino);
            return true;                
        } else {
            return false;
        }
    }
    
    public boolean autoConnect(String comPort) {
        arduino = SerialPort.getCommPort(comPort);
        arduino.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        if (arduino.openPort()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                SERIAL_COMM_LOGGER.warn("ERROR SLEEP SERIAL PORT.");
                arduino.closePort();
                return false;
            }
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(4);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 2);
                bufferout.put(SEND_MSG);
                bufferout.put(IDENT);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send identification Arduino.");
            } else {
                SERIAL_COMM_LOGGER.error("Error send identification Arduino. No connection.");
                return false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                SERIAL_COMM_LOGGER.warn("ERROR SLEEP SERIAL PORT.");
                arduino.closePort();
                return false;
            }
            while (arduino.bytesAvailable() > 0){
                byte[] newData = new byte[arduino.bytesAvailable()];
                int numRead = arduino.readBytes(newData, newData.length);
                bufferin.put(newData);
            }
            bufferin.flip();
            String response = new String(bufferin.array());
            if (response.contains("#S4Hi")){
                bufferin.clear();
                bufferin.order(ByteOrder.LITTLE_ENDIAN);
                arduino.addDataListener(ReceiveMSGArduino);
                return true;                
            } else {
                bufferin.clear();
                arduino.closePort();
                return false;
            }
        } else {
            SERIAL_COMM_LOGGER.warn("ERROR OPEN SERIAL PORT.");
            return false;            
        }
    }   
    
    public void SendAutoConnect(String comPort){
        if (comPort != null){
            if (autoConnect(comPort)){
                physio.receiveAutoConnect(true, comPort);
                return;
            }   
        }
        SerialPort[] portList = SerialPort.getCommPorts();
        for (SerialPort portList1 : portList){
            if (autoConnect(portList1.getSystemPortName())){
                physio.receiveAutoConnect(true, portList1.getSystemPortName());
                return; 
            }
        }
        physio.receiveAutoConnect(false,null);
    }
    
    public void disconnect(){
        if (arduino != null) {
            sendStop();
            arduino.removeDataListener();
            if (arduino.isOpen()) {
                arduino.closePort();
            }
            arduino = null;
        }
        if (file != null) {
            try {
                file.close();
                file = null;
            } catch (IOException ex) {
                SERIAL_COMM_LOGGER.error("Error Close File.");
            }        
        }
    }

    public void setExercise(boolean aux){
        this.exercise = aux;
    }
    
    public void sendFlexionAngle(short flexionAngle) {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(6);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 4);
                bufferout.put(SEND_MSG);
                bufferout.put(A_FLEXION);
                bufferout.putShort(flexionAngle);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send Amax: " + flexionAngle);
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Amax. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Amax. No connection.");
        }
    }
    
    public void sendExtensionAngle(short extensionAngle) {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(6);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 4);
                bufferout.put(SEND_MSG);
                bufferout.put(A_EXTENSION);
                bufferout.putShort(extensionAngle);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send Extension Angle: " + extensionAngle);
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Extension Angle. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Extension Angle. No connection.");
        }
    }   
    
    public void sendAint(short intAngle) {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(6);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 4);
                bufferout.put(SEND_MSG);
                bufferout.put(A_INT);
                bufferout.putShort(intAngle);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send Aint: " + intAngle);
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Aint. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Aint. No connection.");
        }
    }    

    public void sendPressure() {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(4);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 2);
                bufferout.put(SEND_MSG);
                bufferout.put(PRESSURE);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send Pressure.");
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Pressure. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Pressure. No connection.");
        }
    }    
    
    public void sendStart(short sampletime) {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(6);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 4);
                bufferout.put(SEND_MSG);
                bufferout.put(START);
                bufferout.putShort(sampletime);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send Start: " + sampletime);
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Start. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Start. No connection.");
        }
    }

    public void sendStop() {
        if (arduino != null) {
            if (arduino.isOpen()) {
                ByteBuffer bufferout = ByteBuffer.allocate(4);
                bufferout = bufferout.order(ByteOrder.LITTLE_ENDIAN);
                bufferout.put(BEGIN);
                bufferout.put((byte) 2);
                bufferout.put(SEND_MSG);
                bufferout.put(STOP);
                bufferout.flip();
                arduino.writeBytes(bufferout.array(),bufferout.limit());
                SERIAL_COMM_LOGGER.debug("SerialComm Send STOP");
            } else {
                physio.communicationError();
                SERIAL_COMM_LOGGER.error("Error send Stop. No connection.");
            }
        } else {
            physio.communicationError();
            SERIAL_COMM_LOGGER.error("Error send Stop. No connection.");
        }
    }
    
    public boolean checkPort(){
        if (arduino != null) {
            return arduino.isOpen();
        } else {
            return false;
        }
    } 

    private final SerialPortDataListener ReceiveMSGArduino = new SerialPortDataListener() {
        @Override
        public int getListeningEvents() {return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                return;
            if (arduino.bytesAvailable() > 0) {
                byte[] newData = new byte[arduino.bytesAvailable()];
                int numRead = arduino.readBytes(newData, newData.length);
                bufferin.put(newData);
                bufferin.flip();
                while (bufferin.remaining() > 2) {
                    byte input = bufferin.get(bufferin.position());
                    if (input == BEGIN) {
                        input = bufferin.get(bufferin.position() + 1);
                        int numberBytes = Byte.toUnsignedInt(input);
                        if (numberBytes <= (bufferin.remaining() - 2)) {
                            input = bufferin.get(bufferin.position() + 2);
                            if (input == RECEIVE_MSG) {
                                input = bufferin.get(bufferin.position() + 3);
                                if (input == A_FLEXION_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm A_FLEXION_CONF");
                                    physio.receiveFlexionAngleConf();
                                } else if (input == A_EXTENSION_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm A_EXTENSION_CONF");
                                    physio.receiveExtensionAngleConf();
                                } else if (input == A_INT_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm A_INT_CONF");
                                    physio.receiveIntAngleConf();                                    
                                } else if (input == FZERO_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm Fzero_CONF");
                                    //physio.receiveFzeroConf();
                                } else if (input == FREF_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm Fref_CONF");
                                    //physio.receiveFrefConf();
                                } else if (input == PRESSURE_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm Pressure_CONF");
                                    physio.receivePressureConf();                                    
                                } else if (input == START_CONF) {
                                    bufferin.position(bufferin.position() + 4);
                                    SERIAL_COMM_LOGGER.debug("SerialComm Start_CONF");                              
//                                    try {
//                                        LocalDateTime datetime = LocalDateTime.now();
//                                        String fileString = String.valueOf(datetime.getYear()) + "-" + String.valueOf(datetime.getMonth())+ "-" + String.valueOf(datetime.getDayOfMonth()) + "_" + String.valueOf(datetime.getHour()) + "-" + String.valueOf(datetime.getMinute()) + "-" + String.valueOf(datetime.getSecond()) + ".csv";
//                                        file = new FileWriter(fileString);
//                                        file.append("Time Stamp;Angle;Force;Head;Hands;Back;Legs;Foot\n");
                                        physio.receiveStartConf();
//                                    } catch (IOException ex) {
//                                        SERIAL_COMM_LOGGER.error("Error Open File. " + ex);
//                                    }                                                                       
                                } else if (input == STOP_CONF) {
                                    bufferin.position(bufferin.position() + 4);                                                                                                         
//                                    if (file != null){
//                                        try {
//                                            file.close();
//                                            file = null;
//                                        } catch (IOException ex) {
//                                            SERIAL_COMM_LOGGER.error("Error Close File.");
//                                        }
//                                    }
                                    SERIAL_COMM_LOGGER.debug("SerialComm Stop_CONF");
                                    physio.receiveStopConf();                                        
                                } else if (input == VALUES) {
                                    bufferin.position(bufferin.position() + 4);
                                    float timestamp = bufferin.getFloat();
                                    float force = bufferin.getFloat();
                                    float angle = bufferin.getFloat();
                                    short head = bufferin.getShort();
                                    short hands = bufferin.getShort();
                                    short back = bufferin.getShort();
                                    short legs = bufferin.getShort();
                                    short foot = bufferin.getShort();                                       
                                    SERIAL_COMM_LOGGER.debug("SerialComm time:" + timestamp + " force:" + force + " angle:" + angle + " head:" + head + " hands:" + hands + " back:" + back + " legs:" + legs + " foot:" + foot);               
                                    if ((medxMqttClient != null) && (exercise)) {
                                        medxMqttClient.publishAngleMeasurement(angle);
                                    }
//                                    if (file != null){
//                                        try {
//                                            file.append(LocalDateTime.now().toString()+";"+angle+";"+force+";"+head+";"+hands+";"+back+";"+legs+";"+foot+"\n");
//                                        } catch (IOException ex) {
//                                            SERIAL_COMM_LOGGER.error("Error write file.");
//                                        }
//                                    }
                                    physio.receiveValues(timestamp, force, angle, head, hands, back, legs, foot);                                        
                                } else if (input == BUTTON) {
                                    bufferin.position(bufferin.position() + 4);                                                                                                         
                                    SERIAL_COMM_LOGGER.debug("SerialComm Button");
                                    physio.receiveButton();                                        
                                } else {
                                    bufferin.position(bufferin.position() + 1);
                                    SERIAL_COMM_LOGGER.debug("SerialComm MSG error 1." + input);
                                }
                            } else {
                                bufferin.position(bufferin.position() + 1);
                                SERIAL_COMM_LOGGER.debug("SerialComm MSG error 2.");
                            }
                        } else if (numberBytes > 24) {
                            bufferin.position(bufferin.position() + 1);
                            SERIAL_COMM_LOGGER.debug("SerialComm NB error.");
                        } else {
                            SERIAL_COMM_LOGGER.debug("SerialComm without bytes");
                            break;
                        }
                    } else {
                        input = bufferin.get();
                        SERIAL_COMM_LOGGER.debug("SerialComm not BEGIN: " + input);
                    }
                }
                bufferin.compact();
            }
        }
    };
}