package business;

import model.*;
import repo.AudioBookRepo;
import repo.LoanRecordRepo;
import repo.NormalBookRepo;
import view.UiElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {

    String user;
    AudioBookRepo audioBookRepo;
    NormalBookRepo normalBookRepo;
    LoanRecordRepo loanRecordRepo;

    ArrayList<AbstractBook> books = new ArrayList<>();

    public Library() throws IOException {
        audioBookRepo = new AudioBookRepo();
        normalBookRepo = new NormalBookRepo();
        loanRecordRepo = new LoanRecordRepo();

        getAllBooks();
    }

    public void start() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        if(user == null){
            logInMenu(scanner);
        }

    }

    public void logInMenu(Scanner scanner) throws SQLException {

        UiElement.formatHeader("Log In");

        String username = "";

        while(username.equals("")){
            System.out.print("Username: ");
            username = scanner.nextLine();
        }

        user = username;

        mainMenu(scanner);
    }

    public void printMenu(){

        UiElement.formatHeader("Main Menu");

        UiElement.formatMenuItem("Print all books", '1');
        UiElement.formatMenuItem("Print all audio books", '2');
        UiElement.formatMenuItem("Print all normal books", '3');
        UiElement.formatMenuItem("Loan book", '4');
        UiElement.formatMenuItem("Return book", '5');
        UiElement.formatMenuItem("Print all loan records", '6');
        UiElement.formatMenuItem("Print all loan records for user", '7');
        UiElement.formatMenuItem("Log out", 'Q');
    }

    public void mainMenu(Scanner scanner) throws SQLException {

        String choice = "";

        while(!choice.equalsIgnoreCase("Q")){
            printMenu();

            System.out.print("Choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> printAllBooks();
                case "2" -> printAllAudioBooks();
                case "3" -> printAllNormalBooks();
                case "4" -> loanBookMenu(scanner);
                case "5" -> returnBookMenu(scanner);
                case "6" -> printLoanRecords();
                case "7" -> printUserLoanRecords();
                case "Q" -> System.out.printf("Logging out %s%n", user);
                default -> System.out.println("Invalid choice");
            }


        }
    }

    public void getAllBooks(){
        var audioBooks = audioBookRepo.RetrieveAll();
        var normalBooks = normalBookRepo.RetrieveAll();

        books.addAll(audioBooks);
        books.addAll(normalBooks);
    }

    public void printAllBooks(){
        UiElement.formatHeader("All Books");

        for (AbstractBook book : books) {
            System.out.println(book);
        }
    }

    public void printAllAudioBooks(){
        UiElement.formatHeader("All Audio Books");

        for (AbstractBook book : books) {
            if(book instanceof AudioBook){
                System.out.println(book);
            }
        }
    }

    public void printAllNormalBooks(){
        UiElement.formatHeader("All Normal Books");

        for (AbstractBook book : books) {
            if((book instanceof NormalBook)){
                System.out.println(book);
            }
        }
    }

    public void loanBookMenu(Scanner scanner) throws SQLException {
        UiElement.formatHeader("Loan Book");

        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine());

        System.out.print("Return Type: \r\n");
        System.out.println("1. Normal");
        System.out.println("2. Audio");
        System.out.println("Q. Quit");

        String userInput = "";

        while(!userInput.equalsIgnoreCase("Q")){
            System.out.print("Choice: ");
            userInput = scanner.nextLine();

            switch (userInput) {
                case "1" -> loanBook(bookId, LoanType.NORMAL);
                case "2" -> loanBook(bookId, LoanType.AUDIO);
                case "Q" -> {
                    System.out.println("Returning to main menu");
                    mainMenu(scanner);
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }

    public void loanBook(int bookId, LoanType loanType){

        //filter books by loanType. If LoanType is Normal, filter out AudioBooks and vice versa
        var book = books.stream().filter(
                b -> b instanceof NormalBook && loanType == LoanType.NORMAL
                        || b instanceof AudioBook && loanType == LoanType.AUDIO)
                .filter(b -> b.getId() == bookId).findFirst().orElse(null);



        if(book == null){
            System.out.println("Book not found");
            return;
        }

        if(book instanceof NormalBook && !((NormalBook) book).isAvailable()){
            System.out.println("There are no more hard copies of this book available");
            return;
        }

        var loanRecord = new LoanRecord(book.getId(),  user, loanType);

        loanRecordRepo.Insert(loanRecord);

        if(book instanceof NormalBook){
            ((NormalBook) book).setNumberOfHardCopies(((NormalBook) book).getNumberOfHardCopies() - 1);
            normalBookRepo.Update((NormalBook) book);
        }

        System.out.printf("Book %s loaned to %s%n", book.getTitle(), user);
    }

    public void returnBookMenu(Scanner scanner) throws SQLException {
        UiElement.formatHeader("Return Book");

        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine());

        System.out.print("Loan Type: \r\n");
        System.out.println("1. Normal");
        System.out.println("2. Audio");
        System.out.println("Q. Quit");

        String userInput = "";

        while(!userInput.equalsIgnoreCase("Q")){
            System.out.print("Choice: ");
            userInput = scanner.nextLine();

            switch (userInput) {
                case "1" -> returnBook(bookId, LoanType.NORMAL);
                case "2" -> returnBook(bookId, LoanType.AUDIO);
                case "Q" -> {
                    System.out.println("Returning to main menu");
                    mainMenu(scanner);
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }

    public void returnBook(int bookId, LoanType loanType) throws SQLException {
        LoanRecord loanRecord = loanRecordRepo.getLoanRecordByBookIdAndType(bookId, loanType);

        if(loanRecord == null){
            System.out.println("Book not found");
            return;
        }

        loanRecordRepo.DeleteById(loanRecord.getId());

        var book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);

        if(book instanceof NormalBook){
            ((NormalBook) book).setNumberOfHardCopies(((NormalBook) book).getNumberOfHardCopies() + 1);
            normalBookRepo.Update((NormalBook) book);
        }

        assert book != null;
        System.out.printf("Book %s returned by %s%n", book.getTitle(), user);
    }

    public void printLoanRecords() throws SQLException {
        UiElement.formatHeader("Loan Records");

        var loanRecords = loanRecordRepo.RetrieveAll();

        for (LoanRecord loanRecord : loanRecords) {
            System.out.println(loanRecord);
        }
    }

    public void printUserLoanRecords() throws SQLException {
        UiElement.formatHeader("Loan Records");

        var loanRecords = loanRecordRepo.RetrieveAll();

        for (LoanRecord loanRecord : loanRecords) {
            if (loanRecord.getUserName().equals(user)) {
                System.out.println(loanRecord);
            }
        }
    }
}
