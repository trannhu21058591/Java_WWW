package iuh.fit.se.bai_1;

public class Qualification {
    private String examination;   // Class X, Class XII, etc.
    private String board;         // Tên hội đồng/Trường
    private double percentage;    // Tỉ lệ phần trăm (tới 2 chữ số thập phân)
    private int yearOfPassing;    // Năm tốt nghiệp

    public Qualification() {
    }

    public Qualification(String examination, String board, double percentage, int yearOfPassing) {
        this.examination = examination;
        this.board = board;
        this.percentage = percentage;
        this.yearOfPassing = yearOfPassing;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(int yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }
}
