package view;

public class UiElement {

    public static String decorator = "---------------------------------------------";

    public static void formatHeader(String title) {

        System.out.println(decorator);
        System.out.println(centerTitle(title, decorator));
        System.out.println(decorator);
    }

    public static void formatMenuItem(String menuItem, int num) {
        System.out.println(num + ". " + menuItem);
    }

    private static String centerTitle(String title, String decorator) {
        int numOfSpaces = (decorator.length() / 2) - (title.length() / 2);

        return " ".repeat(Math.max(0, numOfSpaces)) + title;
    }

}
