package org.example.Barnes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BarnesAndNobleTest {

    private TestBookDatabase bookDatabase;
    private TestBuyBookProcess buyBookProcess;
    private BarnesAndNoble barnesAndNoble;

    @BeforeEach
    public void setUp() {
        bookDatabase = new TestBookDatabase();
        buyBookProcess = new TestBuyBookProcess();
        barnesAndNoble = new BarnesAndNoble(bookDatabase, buyBookProcess);
    }

    // SPECIFICATION-BASED TESTS

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithValidOrder_ReturnsCorrectTotal() {
        // Arrange
        Book book1 = new Book("ISBN1", 20, 10);
        Book book2 = new Book("ISBN2", 15, 5);
        bookDatabase.addBook("ISBN1", book1);
        bookDatabase.addBook("ISBN2", book2);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN1", 2); // 2 * $20 = $40
        order.put("ISBN2", 3); // 3 * $15 = $45
        // Total = $85

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertNotNull(result);
        assertEquals(85, result.getTotalPrice());
    }

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithNullOrder_ReturnsNull() {
        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithEmptyOrder_ReturnsZeroTotal() {
        // Arrange
        Map<String, Integer> emptyOrder = new HashMap<>();

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(emptyOrder);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalPrice());
    }

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithInsufficientStock_MarksUnavailable() {
        // Arrange
        Book book = new Book("ISBN-LOW", 30, 2);
        bookDatabase.addBook("ISBN-LOW", book);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-LOW", 5); // Request more than available

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUnavailable().size());
        assertTrue(result.getUnavailable().containsKey(book));
    }

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithMultipleBooks_ProcessesAllCorrectly() {
        // Arrange
        Book book1 = new Book("ISBN-A", 10, 20);
        Book book2 = new Book("ISBN-B", 25, 15);
        Book book3 = new Book("ISBN-C", 12, 10);
        bookDatabase.addBook("ISBN-A", book1);
        bookDatabase.addBook("ISBN-B", book2);
        bookDatabase.addBook("ISBN-C", book3);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-A", 5);  // 5 * $10 = $50
        order.put("ISBN-B", 3);  // 3 * $25 = $75
        order.put("ISBN-C", 4);  // 4 * $12 = $48
        // Total = $173

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertEquals(173, result.getTotalPrice());
    }

    @Test
    @DisplayName("specification-based")
    public void testGetPriceForCart_WithExactStockAmount_DepletesInventory() {
        // Arrange
        Book book = new Book("ISBN-EXACT", 20, 5);
        bookDatabase.addBook("ISBN-EXACT", book);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-EXACT", 5);

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getTotalPrice());
        assertEquals(0, book.getQuantity());
    }

    // STRUCTURAL-BASED TESTS

    @Test
    @DisplayName("structural-based")
    public void testGetPriceForCart_BranchCoverage_NullOrderCheck() {
        // Tests the if(order==null) branch
        PurchaseSummary result = barnesAndNoble.getPriceForCart(null);
        assertNull(result);
    }

    @Test
    @DisplayName("structural-based")
    public void testRetrieveBook_BranchCoverage_InsufficientQuantity() {
        // Tests the if (book.getQuantity() < quantity) branch
        Book book = new Book("ISBN-BRANCH", 15, 3);
        bookDatabase.addBook("ISBN-BRANCH", book);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-BRANCH", 5); // Request 5, only 3 available

        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Even with insufficient stock, it buys what's available...so all 3
        assertEquals(0, book.getQuantity()); // All 3 books purchased
        // And marks the shortage in unavailable
        assertTrue(result.getUnavailable().containsKey(book));
        // Price should be for 3 books only
        assertEquals(45, result.getTotalPrice()); // 3 * 15 = 45
    }

    @Test
    @DisplayName("structural-based")
    public void testRetrieveBook_BranchCoverage_SufficientQuantity() {
        // Tests the else branch (sufficient quantity)
        Book book = new Book("ISBN-SUFFICIENT", 20, 10);
        bookDatabase.addBook("ISBN-SUFFICIENT", book);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-SUFFICIENT", 5);

        barnesAndNoble.getPriceForCart(order);

        assertEquals(5, book.getQuantity());
    }

    @Test
    @DisplayName("structural-based")
    public void testGetPriceForCart_LoopCoverage_MultipleIterations() {
        // Tests the for loop with multiple iterations
        Book book1 = new Book("LOOP1", 5, 10);
        Book book2 = new Book("LOOP2", 10, 10);
        Book book3 = new Book("LOOP3", 15, 10);
        Book book4 = new Book("LOOP4", 20, 10);

        bookDatabase.addBook("LOOP1", book1);
        bookDatabase.addBook("LOOP2", book2);
        bookDatabase.addBook("LOOP3", book3);
        bookDatabase.addBook("LOOP4", book4);

        Map<String, Integer> order = new HashMap<>();
        order.put("LOOP1", 1);
        order.put("LOOP2", 2);
        order.put("LOOP3", 3);
        order.put("LOOP4", 1);

        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        assertEquals(90, result.getTotalPrice());
    }

    @Test
    @DisplayName("structural-based")
    public void testGetPriceForCart_PathCoverage_EmptyLoop() {
        // Tests the path where loop doesn't execute
        Map<String, Integer> emptyOrder = new HashMap<>();

        PurchaseSummary result = barnesAndNoble.getPriceForCart(emptyOrder);

        assertNotNull(result);
        assertEquals(0, result.getTotalPrice());
        assertTrue(result.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    public void testGetPriceForCart_PathCoverage_BuyBookCalled() {
        // Ensures process.buyBook() path is executed
        Book book = new Book("PATH-TEST", 25, 8);
        bookDatabase.addBook("PATH-TEST", book);

        Map<String, Integer> order = new HashMap<>();
        order.put("PATH-TEST", 3);

        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        assertEquals(5, book.getQuantity());
        assertEquals(75, result.getTotalPrice());
    }

    // HELPER TEST CLASSES

    private class TestBookDatabase implements BookDatabase {
        private Map<String, Book> books = new HashMap<>();

        @Override
        public Book findByISBN(String ISBN) {
            return books.get(ISBN);
        }

        public void addBook(String isbn, Book book) {
            books.put(isbn, book);
        }
    }

    private class TestBuyBookProcess implements BuyBookProcess {
        @Override
        public void buyBook(Book book, int amount) {
            try {
                // Use reflection to access the private amount field
                java.lang.reflect.Field amountField = Book.class.getDeclaredField("amount");
                amountField.setAccessible(true);
                int currentAmount = (int) amountField.get(book);
                amountField.set(book, currentAmount - amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}