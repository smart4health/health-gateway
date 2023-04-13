package pt.uninova.s4h.healthgateway.util.listeners;

import pt.uninova.s4h.healthgateway.ittm.manager.IttmTrainingManager;
import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.senml.MedxEvent;
import pt.uninova.s4h.healthgateway.util.message.HgHubMessage;

/**
 * Listener to Health Gateway to Hub Messages.
 */
public class HgHubMessageListener implements EventListener<HgHubMessage> {

    private final HgMqttClient hgMqttClient;
    private final IttmTrainingManager ittmTrainingManager;

    public HgHubMessageListener(HgMqttClient hgMqttClient, IttmTrainingManager ittmTrainingManager) {
        this.hgMqttClient = hgMqttClient;
        this.ittmTrainingManager = ittmTrainingManager;
    }

    @Override
    public void onEvent(HgHubMessage e) {
        if (ittmTrainingManager == null){
            return;
        }
        switch (e.getMessageType()) {
            case CITIZEN_ID:
                ittmTrainingManager.newCitizenId(e.getStringValue());
                return;
            case FLEXION_ANGLE:
                ittmTrainingManager.newFlexionAngle(e.getValue());
                break;
            case EXTENSION_ANGLE:
                ittmTrainingManager.newExtensionAngle(e.getValue());
                break;                
            case TRAINING_ID:
                ittmTrainingManager.newTrainingId(e.getStringValue());
                return;
            case TRAINING_NUMBER:
                ittmTrainingManager.newTrainingNumber(e.getValue());
                return;                
            case TRAINING_SESSION:
                ittmTrainingManager.newTrainingSession(e.getValue());
                return;                
            case UPLOAD_TRAINING:
                ittmTrainingManager.newUploadTraining();
                return;
            case START_TRAINING:
                ittmTrainingManager.newStartTrainig();
                break;
            case STOP_TRAINING:
                ittmTrainingManager.newStopTraining();
                break;
            case TRAINING_WEIGHT:
                ittmTrainingManager.newTrainingWeight(e.getValue());
                return;                
        }
        if (hgMqttClient == null) {
            return;
        }
        MedxEvent medxEvent = new MedxEvent(e.getMessageType().name());
        if (e.getValue() != null) {
            medxEvent.setValue(e.getValue());
        }
        if (e.getStringValue() != null) {
            medxEvent.setStringValue(e.getStringValue());
        }
        MedxEvent medxEvents[] = new MedxEvent[1];
        medxEvents[0] = medxEvent;
        hgMqttClient.publishMedxEvents(medxEvents);
    }
}
