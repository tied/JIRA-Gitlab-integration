package pl.hycom.jira.plugins.gitlab.integration.service.processors;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.hycom.jira.plugins.gitlab.integration.exceptions.ProcessException;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Karol Joachimiak on 07.06.2016.
 */
@Component
@Slf4j
public class IssueWorklogChangeProcessor implements ProcessorInterface {
    @Override
    public void execute(@NotNull Commit commitInfo) throws ProcessException {
        List<Time> timeFromMessage = getExtractedMsg(commitInfo);
    }

    public List<Time> getExtractedMsg(@NotNull Commit commitInfo) {
        List<Time> timesList = new ArrayList<>();
        String messageWithoutSpaces = commitInfo.getMessage().replaceAll("\\s+", "");
        Pattern timePattern = Pattern.compile("([0-9]+y)?([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s)?");
        Matcher matcher = timePattern.matcher(messageWithoutSpaces);
        String extractedMessage = "";
        if (matcher.matches()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                String onlyChars = group.replaceAll("\\d+","");
                group = matcher.group(i);
                String onlyDigits = group.replaceAll("(?i)[a-z]","");
                Time time = Time.getTime(onlyChars, Integer.parseInt(onlyDigits));
                timesList.add(time);
                extractedMessage+=onlyDigits+onlyChars;
            }
            log.info("Found time logged: " + extractedMessage);

        }
        return timesList;
    }

    public enum Time {
        YEAR("y"),
        WEEK("w"),
        DAY("d"),
        HOUR("h"),
        MINUTE("m"),
        SECOND("s");

        private String fieldName;
        private int fieldValue;

        public int getFieldValue(){
            return fieldValue;
        }
        public String getFieldName(){
            return fieldName;
        }

        private Time(String fieldName2) {
            fieldName = fieldName2;
        }

        public static Time getTime(String fieldName, int fieldValue) {
            Time time = SECOND;
            switch (fieldName) {
                case "y": {
                    time = YEAR;
                    time.fieldValue = fieldValue;
                    break;
                }
                case "w": {
                    time = WEEK;
                    time.fieldValue = fieldValue;
                    break;
                }
                case "d": {
                    time = DAY;
                    time.fieldValue = fieldValue;
                    break;
                }
                case "h": {
                    time = HOUR;
                    time.fieldValue = fieldValue;
                    break;
                }
                case "m": {
                    time = MINUTE;
                    time.fieldValue = fieldValue;
                    break;
                }
                case "s": {
                    time = SECOND;
                    time.fieldValue = fieldValue;
                    break;
                }
            }
            return time;
        }
    }
}
