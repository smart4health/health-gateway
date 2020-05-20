package pt.uninova.s4h.hub.kbz.gamification;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
//import org.json.JSONObject;
import pt.uninova.s4h.hub.kbz.mqtt.KbzMqttClient;

public class Ghost {

    private static double lastValue = 0.0;
    private static String lastMovement = "";
    private static int startAngle;
    private static int endAngle;
    private static List<String> sessionCmp = new ArrayList<>();     // List of repetition comparison strings
    private static List<Double> precisionScore;                     // List of repetition percentages
    private static double repetitionPrecisionScore;                 // Current repetition score
    private static double repetitionMaxScore;                       // repetition Max score
    private static double repetitionNumberScore;                    // Number of repetitions score
    private static double totalScore;                               // Total score
    private static double repetitionNumber;                         // Repetition number
    private static int halfrepSamples;
    private static double halfRepetitionScore;
    private static boolean halfRepetitionFlag = false;
    private static float machineValue;
    private static int repetitionTime; // repetition time
    private static int sessionTime; // repetition time
    private static int iterationNumber;
    private static String halfRepetitionMessage = "";
    private double finalPrecisionScore;
    private boolean flagStartTrain;
    private int backMovementTime;
    private int frontMovementTime;
    private int holdTime;
    private int fullRepetitionTime;
    private boolean cancelSession;
    private static String patientPosition;
    private final KbzMqttClient kbzMqttClient;

    private final boolean testVersion = false;                       //TODO Change when release

    private static final ch.qos.logback.classic.Logger KBZ_MQTT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Ghost(KbzMqttClient kbzMqttClient) {
        this.kbzMqttClient = kbzMqttClient;
    }

    public void initializeTraining(int sAngle, int eAngle, int bTime, int fTime, int hTime) {
        KBZ_MQTT_LOGGER.debug("INITIALIZE______________________________" + sAngle + "___" + eAngle + "___" + bTime + "___" + fTime + "___" + hTime + "___");

        cancelSession = false;
        flagStartTrain = true;

        if (testVersion) { //TODO manter esta proteccao altera la no angle CLIENTE
            startAngle = Consts.startDegree;
            endAngle = Consts.endDegree;
            backMovementTime = Consts.movTime;
            frontMovementTime = Consts.movTime;
            holdTime = Consts.restTime;
        } else {
            startAngle = sAngle;
            endAngle = eAngle;
            backMovementTime = bTime * 1000;
            frontMovementTime = fTime * 1000;
            holdTime = hTime * 1000;
        }
        fullRepetitionTime = backMovementTime + frontMovementTime + holdTime;
        repetitionTime = 0;
        sessionTime = 0;
        iterationNumber = 0;
        repetitionMaxScore = 100;                                                                                       //(int) ((fullRepetitionTime) / Consts.refreshRate);
        repetitionNumberScore = 0;
        totalScore = 0;
        repetitionPrecisionScore = repetitionMaxScore;
        precisionScore = new ArrayList<>();
        finalPrecisionScore = 0;
        halfrepSamples = (int) ((frontMovementTime + backMovementTime) / Consts.refreshRate);
        halfRepetitionScore = halfrepSamples;
        patientPosition = "";
    }

    public void stopTraining() {
        cancelSession = true;
    }

    public void gamificationOrchestrator(float medxValue) {

        KBZ_MQTT_LOGGER.debug("ORCHESTRATOR______________________________" + sessionTime);
        KBZ_MQTT_LOGGER.debug("INFO______________________________" + "StartAngle:" + startAngle + " ::: EndAngle: " + endAngle + " ::: Time: " + sessionTime);
        if (flagStartTrain) {
            ghostStart();
            flagStartTrain = false;
        }
        machineValue = medxValue;
    }

    private String makeFinalMessage() {

        StringBuilder sb = new StringBuilder();
        sb.append(lastValue).append(";");
        sb.append(sessionTime).append(";");
        sb.append(fullRepetitionTime - repetitionTime).append(";");
        sb.append(iterationNumber + 1).append(";");
        sb.append(patientPosition).append(";");

        return sb.toString();
    }

    public void ghostStart() {
        int restTime = holdTime;                                                                              //Consts.restTime;
        int refreshrate = (int) Consts.refreshRate;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                double percentage = 0.0;
                String s = "";

                // back movement
                if (repetitionTime >= 0 && repetitionTime < backMovementTime) {
                    percentage = (repetitionTime * 100.0) / backMovementTime;
                    s = Consts.BACK_MOV;
                } // hold
                else if (repetitionTime >= backMovementTime && repetitionTime < backMovementTime + restTime) {
                    percentage = (backMovementTime * 100.0) / backMovementTime;
                    s = Consts.HOLD_MOV;

                    if (halfRepetitionFlag) {
                        double halfRepetitionPercentage = Math.round(((halfRepetitionScore * 100.0) / halfrepSamples) * 100.0) / 100.0;
                        halfRepetitionString(halfRepetitionPercentage);
                        halfRepetitionFlag = false;
                        halfRepetitionScore = halfrepSamples;
                    }
                } // front movement
                else if (repetitionTime >= backMovementTime + restTime && repetitionTime < backMovementTime + restTime + frontMovementTime) {
                    percentage = (((fullRepetitionTime - refreshrate) - repetitionTime) * 100.0) / frontMovementTime;
                    s = Consts.FRONT_MOV;
                    halfRepetitionFlag = true;
                }

                lastValue = Math.round(percentageTwoNumbers(percentage, endAngle, startAngle) * 10.0) / 10.0;
                KBZ_MQTT_LOGGER.debug("LastValue_Ghost------> " + lastValue);

                lastMovement = s;
                KBZ_MQTT_LOGGER.debug("LastMovement_Direction------> " + lastMovement);
                //Random r = new Random();
                //int rInt = r.ints(-10, 10).findFirst().getAsInt();

                // new iteration
                if (repetitionTime >= fullRepetitionTime) {
                    repetitionTime = 0;
                    iterationNumber++;
                    setSessionScore();
                }

                compare(machineValue);

                repetitionTime += Consts.refreshRate;
                sessionTime += Consts.refreshRate;

                finalPrecisionScore = Math.round(precisionScore.stream().mapToDouble(val -> val).average().orElse(0.0) * 100.0) / 100.0;
                calculateScore();
                KBZ_MQTT_LOGGER.debug("sessionScore------> " + precisionScore);
                KBZ_MQTT_LOGGER.debug("Score------> " + finalPrecisionScore);

                if (cancelSession) {                                                                                                //if (iterationNumber == Consts.sessionRepetitions) {
                    t.cancel();
                    KBZ_MQTT_LOGGER.debug("end. Session Average: " + finalPrecisionScore + "%");
                    String finalMessage = "#Final Score: " + totalScore + " #Precision Score: " + finalPrecisionScore + " #Total of Repetitions: " + repetitionNumber;
                    KBZ_MQTT_LOGGER.debug("Message Sent to Broker when STOPPED_______" + finalMessage);
                    KBZ_MQTT_LOGGER.debug("Image Sent to Broker when STOPPED_______" + Consts.FINALIMAGE);
                    kbzMqttClient.publishKbzImageAndMessage(Consts.FINALIMAGE, finalMessage);
                } else {
                    String fullMessage = makeFinalMessage();
                    KBZ_MQTT_LOGGER.debug("Message Sent to Broker_______" + fullMessage);
                    KBZ_MQTT_LOGGER.debug("Image Sent to Broker_______" + lastMovement);
                    kbzMqttClient.publishKbzImageAndMessage(lastMovement, fullMessage);
                }
            }
        }, 0, Consts.refreshRate);
    }

    private static void setSessionScore() {
        //add repetition score to list and reset
        double repetitionPercentage = Math.round(((repetitionPrecisionScore * 100.0) / repetitionMaxScore) * 100.0) / 100.0;
        precisionScore.add(repetitionPercentage);
        repetitionPrecisionScore = repetitionMaxScore;                                                               //Consts.repSamples;
        repetitionNumberScore = iterationNumber;
    }

//    private JSONObject calculateScore() {
//        double auxScore = 0;
//        resultsJSON.put("precision", finalPrecisionScore);
//        resultsJSON.put("repetitions", Math.round((repetitionNumberScore) * 10.0) / 10.0);
//        auxScore = repetitionNumberScore * 100 / Consts.sessionMaxRepetitions;
//        if (auxScore > 100) {
//            auxScore = 100;
//        }
//        totalScore = Math.round(((finalPrecisionScore + auxScore) / 2) * 10.0) / 10.0;
//        resultsJSON.put("total", totalScore);
//        return resultsJSON;
//    }
    private void calculateScore() {
        repetitionNumber = Math.round((repetitionNumberScore) * 10.0) / 10.0;
        double auxScore = repetitionNumberScore * 100 / Consts.sessionMaxRepetitions;
        if (auxScore > 100) {
            auxScore = 100;
        }
        totalScore = Math.round(((finalPrecisionScore + auxScore) / 2) * 10.0) / 10.0;
    }

    /**
     * Returns the number that corresponds to the given percentage between two
     * given numbers
     *
     * @param percentage percentage
     * @param high highest value (100%)
     * @param low lowest value (0%)
     * @return number correspondent to the percentage given
     */
    private static double percentageTwoNumbers(double percentage, double high, double low) {
        return ((high - low) / 100) * percentage + low;
    }

    private static void compare(double machine) {
        double ghost = lastValue;
        String mov = lastMovement;
        String out = "";
        int cmp = 0;

        double fullPercent = startAngle - endAngle;                                                                     //Consts.startDegree - Consts.endDegree;
        double diff = Math.abs(machine - ghost);
        double percentage = Math.round(diff * 100.0 / fullPercent) / 100.0;

        switch (mov) {
            case Consts.BACK_MOV:
//                if (Math.abs(machine - ghost) <= Consts.margin) { // if inside margin, treat as equal
//                    cmp = 0;
//                } else {
                cmp = Double.compare(machine, ghost);
//                }
                break;
            case Consts.FRONT_MOV:
//                if (Math.abs(machine - ghost) <= Consts.margin) { // if inside margin, treat as equal
//                    cmp = 0;
//                } else {
                cmp = Double.compare(ghost, machine);
//                }
                break;
            case Consts.HOLD_MOV:
                cmp = Double.compare(ghost, machine);
                if (cmp == 0) {
                    out = Consts.OK;
                } else {
                    out = Consts.NOT_OK;
                    repetitionPrecisionScore -= percentage; // reduce repetition score
                }
                break;
            default:
                break;
        }
        if (mov.equals(Consts.BACK_MOV) || mov.equals(Consts.FRONT_MOV)) {
            if (cmp > 0) {
                out = Consts.LATE;
                repetitionPrecisionScore -= percentage; // reduce repetition score
                if (halfRepetitionFlag) {
                    halfRepetitionScore -= percentage;
                }
            } else if (cmp < 0) {
                out = Consts.EARLY;
                repetitionPrecisionScore -= percentage; // reduce repetition score
                if (halfRepetitionFlag) {
                    halfRepetitionScore -= percentage;
                }
            } else {
                out = Consts.OK;
            }
        }

        repetitionPrecisionScore = Math.round(repetitionPrecisionScore * 100.0) / 100.0;
        halfRepetitionScore = Math.round(halfRepetitionScore * 100.0) / 100.0;

        //KBZ_MQTT_LOGGER.debug(lastMovement + " - m: " + machine + "; g: " + ghost + " - " + out + "; penalty=" + percentage + "; total=" + repetitionScore);
        KBZ_MQTT_LOGGER.debug("GhostRunning------> " + lastMovement + " - machine: " + machine + "; ghost: " + ghost + " - " + out + "; penalty=" + percentage + "; total=" + repetitionPrecisionScore);

        patientPosition = out;
        sessionCmp.add(out); // add the comparison of repetition to the array
    }

    private static String halfRepetitionString(double halfRepetitionPercentage) {

        if (halfRepetitionPercentage >= 90) {                     //Review the nomenclature
            halfRepetitionMessage = "Excellent";
        } else if (halfRepetitionPercentage >= 80) {
            halfRepetitionMessage = "Great";
        } else if (halfRepetitionPercentage >= 70) {
            halfRepetitionMessage = "good";
        } else if (halfRepetitionPercentage >= 60) {
            halfRepetitionMessage = "medium";
        } else if (halfRepetitionPercentage >= 50) {
            halfRepetitionMessage = "average";
        } else if (halfRepetitionPercentage >= 40) {
            halfRepetitionMessage = "bellow average";
        } else if (halfRepetitionPercentage < 40) {
            halfRepetitionMessage = "not so great";
        }

        if (halfRepetitionPercentage < 80) {
            // sublist of last half repetition
            List<String> halfSessionCmp = sessionCmp.subList(sessionCmp.size() - halfrepSamples, sessionCmp.size());
//        halfSessionCmp.forEach(System.out::println);
            // one-liner to get most frequent word of a list
            String mostly = halfSessionCmp.stream()
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting())) // hashmap of word and count
                    .entrySet()
                    .stream()
                    .max(Comparator.comparing(Map.Entry::getValue)) // get most frequent value
                    .get() // get hashMapNode
                    .getKey(); // get key

            halfRepetitionMessage = "Your performance was " + halfRepetitionMessage + " and you were mostly \n" + mostly;
        }

        return halfRepetitionMessage;
    }
}
