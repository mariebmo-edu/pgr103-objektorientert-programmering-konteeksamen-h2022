package view;

import model.AbstractBook;
import model.AudioBook;
import model.LoanRecord;
import model.NormalBook;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class UiElement {

    public static String decorator = "---------------------------------------------------------------------------------------------------------------------------------------";
    public static char columnSeparator = '|';
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
        audioBookFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s", "ID", ' ', "TYPE", ' ', "TITLE", ' ', "AUTHOR",' ', "FREE TRIAL PERIOD");

        for(AudioBook book : books){
            audioBookFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s", book.getId(), columnSeparator, "Audio", columnSeparator, book.getTitle(), columnSeparator, book.getAuthor(), columnSeparator, book.getFreeTrialPeriod());
        }

        System.out.println(audioBookFormatter);
    }

    public static void printNormalBookTable(List<NormalBook> books){
        // ID, type, title, author, number of hard copies, loan period
        Formatter normalBookFormatter = new Formatter();
        normalBookFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s %-1c %-20s", "ID", ' ', "TYPE", ' ', "TITLE", ' ', "AUTHOR",' ', "AVAILABLE COPIES", ' ', "LOAN PERIOD");

        for(NormalBook book : books){
            normalBookFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s %-1c %-20s %-1c %-20s", book.getId(), columnSeparator, "Normal", columnSeparator, book.getTitle(), columnSeparator, book.getAuthor(), columnSeparator, book.getNumberOfHardCopies(), columnSeparator, book.getLoanPeriod());
        }

        System.out.println(normalBookFormatter);
    }

    public static void printLoanRecordTable(List<LoanRecord> loanRecords){
        //ID, book id, username, loan type
        Formatter loanRecordFormatter = new Formatter();
        loanRecordFormatter.format("%-5s %-1c %-10s %-1c %-50s %-1c %-20s", "ID", ' ', "BOOK ID", ' ', "USERNAME", ' ', "LOAN TYPE");
        for(LoanRecord loanRecord : loanRecords){
            loanRecordFormatter.format("\n%-5s %-1c %-10s %-1c %-50s %-1c %-20s", loanRecord.getId(), columnSeparator, loanRecord.getBookId(), columnSeparator, loanRecord.getUserName(), columnSeparator, loanRecord.getLoanType());
        }
        System.out.println(loanRecordFormatter);
    }
}
