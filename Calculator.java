import java.util.InputMismatchException;

public class Calculator {
    public static int translateSymbol(String e){
        return switch (e) {
            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> throw new InputMismatchException();
        };
    }
    public static int translateNum(int x){
        return switch (x) {
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            case 8 -> 0;
            default -> throw new InputMismatchException();
        };
    }
    public static String translateIntoSym(int a){
        return switch (a){
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> throw new InputMismatchException();
        };
    }
    public static int translateIntoNum(int a){
        return switch (a) {
            case 7 -> 1;
            case 6 -> 2;
            case 5 -> 3;
            case 4 -> 4;
            case 3 -> 5;
            case 2 -> 6;
            case 1 -> 7;
            case 0 -> 8;
            default -> throw new InputMismatchException();
        };
    }
}
