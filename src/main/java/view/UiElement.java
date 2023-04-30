package view;

import model.AbstractBook;
import model.AudioBook;
import model.LoanRecord;
import model.NormalBook;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class UiElement {

    public static char columnSeparator = '|';
    public static char decoratorChar = '-';
    public static String decorator = repeatChar(decoratorChar, 150);

    public static void formatHeader(String title) {

        System.out.println(decorator);
        System.out.println(centerTitle(title, decorator));
        System.out.println(decorator);
    }

    public static void formatMenuItem(String menuItem, char selectionChar) {
        System.out.println(selectionChar + ". " + menuItem);
    }

    private static String centerTitle(String title, String decorator) {
        int numOfSpaces = (decorator.length() / 2) - (title.length() / 2);

        return " ".repeat(Math.max(0, numOfSpaces)) + title;
    }

    public static void printBookTable(ArrayList<AbstractBook> books){

        List<AudioBook> audioBooks = books.stream().filter(book -> book instanceof AudioBook).map(book -> (AudioBook) book).toList();
        List<NormalBook> normalBooks = books.stream().filter(book -> book instanceof NormalBook).map(book -> (NormalBook) book).toList();

        printAudioBookTable(audioBooks);
        System.out.println(decorator);
        printNormalBookTable(normalBooks);
    }

    public static void printAudioBookTable(List<AudioBook> books){
        // ID, type, title, author, free trial period
        // use seperator char between each column
        Formatter audioBookFormatter = new Formatter();
        audioBookFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s", "ID", columnSeparator, "TYPE", columnSeparator, "TITLE", columnSeparator, "AUTHOR",columnSeparator, "FREE TRIAL PERIOD");

        audioBookFormatter.format("\n");
        for(int i = 0; i < decorator.length(); i++){
            audioBookFormatter.format("%c", decoratorChar);
        }

        for(AudioBook book : books){
            audioBookFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s", book.getId(), columnSeparator, "Audio", columnSeparator, book.getTitle(), columnSeparator, book.getAuthor(), columnSeparator, book.getFreeTrialPeriod() + " days");
        }

        System.out.println(audioBookFormatter);
        System.out.println("\n");
    }

    public static void printNormalBookTable(List<NormalBook> books){
        // ID, type, title, author, number of hard copies, loan period
        Formatter normalBookFormatter = new Formatter();
        normalBookFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s %-1c %-20s", "ID", columnSeparator, "TYPE", columnSeparator, "TITLE", columnSeparator, "AUTHOR",columnSeparator, "AVAILABLE COPIES", columnSeparator, "LOAN PERIOD");

        // add decorator below header
        normalBookFormatter.format("\n");
        for(int i = 0; i < decorator.length(); i++){
            normalBookFormatter.format("%c", decoratorChar);
        }

        for(NormalBook book : books){
            normalBookFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s %-1c %-20s", book.getId(), columnSeparator, "Normal", columnSeparator, book.getTitle(), columnSeparator, book.getAuthor(), columnSeparator, book.getNumberOfHardCopies(), columnSeparator, book.getLoanPeriod() + " days");
        }

        System.out.println(normalBookFormatter);
        System.out.println("\n");
    }

    public static void printLoanRecordTable(List<LoanRecord> loanRecords){
        //ID, book id, username, loan type
        Formatter loanRecordFormatter = new Formatter();
        loanRecordFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s", "ID", columnSeparator, "BOOK ID", columnSeparator, "USERNAME", columnSeparator, "LOAN TYPE");

        loanRecordFormatter.format("\n");
        for(int i = 0; i < decorator.length(); i++){
            loanRecordFormatter.format("%c", decoratorChar);
        }

        for(LoanRecord loanRecord : loanRecords){
            loanRecordFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s", loanRecord.getId(), columnSeparator, loanRecord.getBookId(), columnSeparator, loanRecord.getUserName(), columnSeparator, loanRecord.getLoanType());
        }
        System.out.println(loanRecordFormatter);
        System.out.println("\n");
    }

    public static String repeatChar(char c, int count) {
        return String.valueOf(c).repeat(Math.max(0, count));
    }
}
