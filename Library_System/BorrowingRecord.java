import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BorrowingRecord {
    private String isbn;
    private String bookTitle;
    private String studentId;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private String status;

    public BorrowingRecord(String isbn, String bookTitle, String studentId) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.studentId = studentId;
        this.borrowDate = LocalDateTime.now();
        this.status = "Borrowed";
    }

    public void returnBook() {
        this.returnDate = LocalDateTime.now();
        this.status = "Returned";
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getBookTitle() { return bookTitle; }
    public String getStudentId() { return studentId; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s",
            isbn,
            bookTitle,
            studentId,
            formatDateTime(borrowDate),
            formatDateTime(returnDate),
            status
        );
    }
} 