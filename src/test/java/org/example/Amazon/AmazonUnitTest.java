package org.example.Amazon;

import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AmazonUnitTest {

    @Test
    @DisplayName("specification-based")
    void testCalculate_UsesPriceRulesCorrectly() {
        // Arrange
        ShoppingCart mockCart = mock(ShoppingCart.class);
        PriceRule mockRule = mock(PriceRule.class);
        Item item = new Item(null, "Book", 2, 10.0);
        when(mockCart.getItems()).thenReturn(List.of(item));
        when(mockRule.priceToAggregate(any())).thenReturn(20.0);

        Amazon amazon = new Amazon(mockCart, List.of(mockRule));

        // Act
        double result = amazon.calculate();

        // Assert
        assertEquals(20.0, result);
        verify(mockRule, times(1)).priceToAggregate(any());
    }

    @Test
    @DisplayName("structural-based")
    void testAddToCart_DelegatesToCart() {
        ShoppingCart mockCart = mock(ShoppingCart.class);
        PriceRule mockRule = mock(PriceRule.class);
        Item newItem = new Item(null, "Mouse", 1, 25.0);
        Amazon amazon = new Amazon(mockCart, List.of(mockRule));

        amazon.addToCart(newItem);

        verify(mockCart, times(1)).add(newItem);
    }
}