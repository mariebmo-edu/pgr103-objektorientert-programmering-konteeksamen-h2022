package model;

import java.sql.Date;
import java.text.DateFormat;

public class LoanRecord {

    int id;
    String userName;
    int bookId;
    LoanType loanType;

    public LoanRecord(int bookId, String userName, LoanType loanType) {
        this.userName = userName;
        this.bookId = bookId;
        this.loanType = loanType;
    }

    public LoanRecord(int id, String userName, int bookId, LoanType loanType) {
        this.id = id;
        this.userName = userName;
        this.bookId = bookId;
        this.loanType = loanType;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return "LoanRecord{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", bookId=" + bookId +
                ", loanType=" + loanType +
                '}';
    }
}

