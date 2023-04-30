package business;

import model.*;
import repo.AbstractRepo;
import repo.AudioBookRepo;
import repo.LoanRecordRepo;
import repo.NormalBookRepo;
import view.UiElement;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
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

        GetAllBooks();
    }

    public void Start() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        if(user == null){
            LogInMenu(scanner);
        }

    }

    public void LogInMenu(Scanner scanner) throws SQLException {

        UiElement.formatHeader("Log In");

        String username = "";

        while(username.equals("")){
            System.out.print("Username: ");
            username = scanner.nextLine();
        }

        user = username;

        MainMenu(scanner);
    }

    public void PrintMenu(){
        UiElement.formatHeader("Main Menu");
        UiElement.formatMenuItem("See all books", 1);
        UiElement.formatMenuItem("See all Audio books", 2);
        UiElement.formatMenuItem("See all Normal books", 3);
        UiElement.formatMenuItem("Loan book", 4);
        UiElement.formatMenuItem("Return book", 5);
        UiElement.formatMenuItem("Log out", 6);
    }

    public void MainMenu(Scanner scanner) throws SQLException {

        String choice = "";

        while(!choice.equals("7")){
            PrintMenu();

            System.out.print("Choice: ");
            choice = scanner.nextLine();

            switch (choice){
                case "1":
                    PrintAllBooks();
                    break;
                case "2":
                    PrintAllAudioBooks();
                    break;
                case "3":
                    PrintAllNormalBooks();
                    break;
                case "4":
                    LoanBookMenu(scanner);
                    break;
                case "5":
                    ReturnBookMenu(scanner);
                    break;
                case "6":
                    PrintLoanRecords();
                    break;
                case "7":
                    System.out.printf("Logging out %s%n", user);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }


        }
    }

    public void GetAllBooks(){
        var audioBooks = audioBookRepo.RetrieveAll();
        var normalBooks = normalBookRepo.RetrieveAll();

        books.addAll(audioBooks);
        books.addAll(normalBooks);
    }

    public void PrintAllBooks(){
        UiElement.formatHeader("All Books");

        for (AbstractBook book : books) {
            System.out.println(book);
        }
    }

    public void PrintAllAudioBooks(){
        UiElement.formatHeader("All Audio Books");

        for (AbstractBook book : books) {
            if(book instanceof AudioBook){
                System.out.println(book);
            }
        }
    }

    public void PrintAllNormalBooks(){
        UiElement.formatHeader("All Normal Books");

        for (AbstractBook book : books) {
            if((book instanceof NormalBook)){
                System.out.println(book);
            }
        }
    }

    public void LoanBookMenu(Scanner scanner){
        UiElement.formatHeader("Loan Book");

        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine());

        System.out.print("Loan Type: \r\n");
        System.out.println("1. Normal");
        System.out.println("2. Audio");

        int userInput = 0;

        while(userInput == 0){
            System.out.print("Choice: ");
            userInput = Integer.parseInt(scanner.nextLine());

            switch (userInput){
                case 1:
                    LoanBook(bookId, LoanType.NORMAL);
                    break;
                case 2:
                    LoanBook(bookId, LoanType.AUDIO);
                    break;
                default:
                    System.out.println("Invalid choice");
                    userInput = 0;
                    break;
            }
        }
    }

    public void LoanBook(int bookId, LoanType loanType){

        //filter books by loanType. If LoanType is Normal, filter out AudioBooks and vice versa
        var book = books.stream().filter(
                b -> b instanceof NormalBook && loanType == LoanType.NORMAL
                        || b instanceof AudioBook && loanType == LoanType.AUDIO)
                .filter(b -> b.getId() == bookId).findFirst().orElse(null);



        if(book == null){
            System.out.println("Book not found");
            return;
        }

        if(book instanceof NormalBook && ((NormalBook) book).getNumberOfHardCopies() <= 0){
            System.out.println("Book is already loaned");
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

    public void ReturnBookMenu(Scanner scanner) throws SQLException {
        UiElement.formatHeader("Return Book");

        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine());

        System.out.print("Loan Type: ");
        System.out.println("1. Normal");
        System.out.println("2. Audio");

        int userInput = 0;

        while(userInput == 0){
            System.out.print("Choice: ");
            userInput = Integer.parseInt(scanner.nextLine());

            switch (userInput){
                case 1:
                    ReturnBook(bookId, LoanType.NORMAL);
                    break;
                case 2:
                    ReturnBook(bookId, LoanType.AUDIO);
                    break;
                default:
                    System.out.println("Invalid choice");
                    userInput = 0;
                    break;
            }
        }
    }

    public void ReturnBook(int bookId, LoanType loanType) throws SQLException {
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

    public void PrintLoanRecords() throws SQLException {
        UiElement.formatHeader("Loan Records");

        var loanRecords = loanRecordRepo.RetrieveAll();

        for (LoanRecord loanRecord : loanRecords) {
            System.out.println(loanRecord);
        }
    }
}
