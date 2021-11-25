package pt.uninova.s4h.healthgateway.ittm.message.file;

//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingRequestJson;


/**
 * Auxiliary class to save the trainings and force tests executed in the
 * machine.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 21 November 2019 - First version.
 */
public class SaveMedxEvents {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    
    public static void saveForceTest(String directory, ForceTestRequestJson forceTestRequestJson) throws SaveMedxEventsException {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(forceTestRequestJson.getTestTime()), ZonedDateTime.now().getZone());
        String fullpath = directory + DATE_TIME_FORMATTER.format(date) + "_" + forceTestRequestJson.getCitizenId() + "_" + forceTestRequestJson.getForceTestId() + ".txt";
        writeToFile(fullpath, new ForceTestGsonUtil().toRequestJson(forceTestRequestJson));
    }

    public static void saveTrainingData(String directory, TrainingRequestJson trainingRequestJson) throws SaveMedxEventsException {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.now(), ZonedDateTime.now().getZone());
        //LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(trainingRequestJson.getStopTime()), ZonedDateTime.now().getZone());
        String fullpath = directory + DATE_TIME_FORMATTER.format(date) + "_" + trainingRequestJson.getCitizenId() + "_" + trainingRequestJson.getTrainingId() + ".txt";
        writeToFile(fullpath, new TrainingGsonUtil().toRequestJson(trainingRequestJson));
    }

    private static void writeToFile(String fullPath, String content) throws SaveMedxEventsException {
        try {
            FileWriter file = new FileWriter(fullPath);
            file.append(content);
            file.close();
        } catch (IOException ex) {
            throw new SaveMedxEventsException(ex.getMessage());
        }
    }
}
