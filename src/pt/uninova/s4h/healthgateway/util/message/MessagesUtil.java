package pt.uninova.s4h.healthgateway.util.message;

/**
 * Interface to hold useful information.
 */
public interface MessagesUtil {

    public enum EventMessageType {
        // Measurement events
        FORCE,
        ANGLE,
        HEADL,
        HEADR,
        HANDSL,
        HANDSR,
        BACK,
        LEGSL,
        LEGSR,
        FOOTL,
        FOOTR,
        CHECK_PORT_REQUEST,
        CHECK_PORT_RESPONSE,
        COMM_PORTS_REQUEST,
        COMM_PORTS_RESPONSE,
        CONNECTION_REQUEST,
        CONNECTION_RESPONSE,
        // b-health -> GUI events
        AUTO_CONNECTION_RESPONSE,
        COMMUNICATION_ERROR,
        FLEXION_ANGLE,
        FLEXION_ANGLE_CONF,
        EXTENSION_ANGLE,
        EXTENSION_ANGLE_CONF,
        ZERO_ANGLE,
        ZERO_ANGLE_CONF,
        ZERO_FORCE_CONF,
        REFERENCE_FORCE_CONF,
        PRESSURE,
        PRESSURE_CONF,
        START,
        START_CONF,
        STOP,
        STOP_CONF,
        BUTTON,
        // GUI -> b-health events
        AUTO_CONNECTION_REQUEST,
        DISCONNECT,
        EXERCISE,
        // GUI events
        START_TRAINING,
        STOP_TRAINING,
        CITIZEN_ID,
        TRAINING_ID,
        TRAINING_NUMBER,
        TRAINING_SESSION,
        TRAINING_TIMES,
        TRAINING_WEIGHT,
        TRAINING_SCORE,
        FINISH_TRAINING,
        UPLOAD_TRAINING,
        TRAINING_API_ERROR,
        TRAINING_TOKEN_ERROR,
        TRAINING_500_ERROR,
        USER_INFORMATION_REQUEST,
        USER_INFORMATION_RESPONSE,
        USER_ERROR,
        //KBZ -> GUI events
        KBZ_MESSAGE,
        KBZ_IMAGE;
    }
}
