package sit.syone.itbkkapi.util;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Converter;
import jakarta.persistence.EnumType;

public enum Status {
    NO_STATUS("No Status"), TO_DO("To do"), DOING("Doing"), DONE("Done");

    private String humanReadableStats;

    private Status(String humanReadableStats){
        this.humanReadableStats = humanReadableStats;
    }

    public String getHumanReadableStats(){
        return humanReadableStats;
    }

    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.humanReadableStats.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
