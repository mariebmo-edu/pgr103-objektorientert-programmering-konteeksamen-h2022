package model;

public class NormalBook extends AbstractBook {
    int numberOfHardCopies;
    int loanPeriod;

    public NormalBook(int id, String title, String author, int numberOfHardCopies, int loanPeriod) {
        super(id, title, author);
        this.numberOfHardCopies = numberOfHardCopies;
        this.loanPeriod = loanPeriod;
    }

    public int getNumberOfHardCopies() {
        return numberOfHardCopies;
    }

    public void setNumberOfHardCopies(int numberOfHardCopies) {
        this.numberOfHardCopies = numberOfHardCopies;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public boolean isAvailable() {
        return numberOfHardCopies > 0;
    }

    @Override
    public String toString() {
        return "NormalBook{" +
                "numberOfHardCopies=" + numberOfHardCopies +
                ", loanPeriod=" + loanPeriod +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
