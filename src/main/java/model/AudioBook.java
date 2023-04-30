package model;

public class AudioBook extends AbstractBook {

    int freeTrialPeriod;

    public AudioBook(int id, String title, String author, int freeTrialPeriod) {
        super(id, title, author);
        this.freeTrialPeriod = freeTrialPeriod;
    }

    public int getFreeTrialPeriod() {
        return freeTrialPeriod;
    }

    public void setFreeTrialPeriod(int freeTrialPeriod) {
        this.freeTrialPeriod = freeTrialPeriod;
    }

    @Override
    public String toString() {
        return "(Audio Book) " + id + " - " + title + " by " + author + " (Free trial period: " + freeTrialPeriod + " days)";
    }
}
