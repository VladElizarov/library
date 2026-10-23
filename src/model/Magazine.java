package model;

public class Magazine extends LibraryItem {
    private static final int LOAN_DAYS = 7;
    private final int issueNum;

    public Magazine(String name, int issueNum) {
        if (issueNum <= 0) {
            throw new IllegalArgumentException("Номер выпуска должен быть больше 0, получено: " + issueNum);
        }
        super(name);
        this.issueNum = issueNum;

    }

    public Magazine(int id, String name, int issueNum) {
        if (issueNum <= 0) {
            throw new IllegalArgumentException("Номер выпуска должен быть больше 0, получено: " + issueNum);
        }
        super(id, name);
        this.issueNum = issueNum;

    }

    @Override
    public String convertToCSV() {
        return "Magazine;" + super.convertToCSV() + ";" + issueNum;
    }

    @Override
    public int getLoanDays() {
        return LOAN_DAYS;
    }

    @Override
    public String getTypeName() {
        return "Журнал";
    }

    @Override
    public String toString() {
        return super.toString() + " - журнал, номер выпуска: " + issueNum;
    }
}
