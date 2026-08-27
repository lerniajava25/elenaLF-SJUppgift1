package elprices.domain;

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


}
