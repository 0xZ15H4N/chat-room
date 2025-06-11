public class Color {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    public static void it(String str, String code) {
        String color = switch (code.toUpperCase()) {
            case "RED" -> RED;
            case "GREEN" -> GREEN;
            case "YELLOW" -> YELLOW;
            case "BLUE" -> BLUE;
            case "PURPLE" -> PURPLE;
            case "CYAN" -> CYAN;
            default -> RESET;
        };
        System.out.print(color + str + RESET);
    }

    public static void prompt(String symbol) {
        System.out.print(CYAN + symbol + RESET + " ");
    }

    public static void err(Exception e) {
        System.out.println(RED + "[ERROR] " + e + RESET);
    }
}
