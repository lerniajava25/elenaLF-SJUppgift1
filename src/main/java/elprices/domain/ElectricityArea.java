package elprices.domain;
import java.util.Optional;

public enum ElectricityArea {
    SE1("Luleå – Norra Sverige"),
    SE2("Sundsvall – Norra Mellansverige"),
    SE3("Stockholm – Södra Mellansverige"),
    SE4("Malmö – Södra Sverige");

    private final String description;

    ElectricityArea(String description) {
        this.description = description;
    }
    public String description(){
        return description;
    }

    // to parse an input from SE3/se3/3 or return an empty Optional for anything invalid, so it can reac without catching exceptions
    // I use Optional if this object may contain an HourlyPrice or nothing/empty so Idon't get nullpointer exception.
    //eg. "se3 " ->trim ->"se3" -> "SE3" toUppercase ->find enum -> return value from the above enum.
    public static Optional<ElectricityArea> parse(String input) {

        if (input == null) {
            return Optional.empty();
        }

        //clean the input
        var normalized = input.trim().toUpperCase();

        if (normalized.matches("[1-4]")) {
            normalized= "SE" + normalized;
        }
        for (var area:values()){
            if (area.name().equals(normalized)){
            return Optional.of(area);
            }
        }
        return Optional.empty();
    }

}
