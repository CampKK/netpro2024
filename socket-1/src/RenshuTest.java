import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RenshuTest {
    Renshu renshu = new Renshu();

    @Test
    void testDoubleValue() {
        assertEquals(4, renshu.doubleValue(2));
        assertEquals(0, renshu.doubleValue(0));
        assertEquals(-6, renshu.doubleValue(-3));
    }

    @Test
    void testSumUpToN() {
        assertEquals(55, renshu.sumUpToN(10));
        assertEquals(0, renshu.sumUpToN(0));
        assertEquals(5050, renshu.sumUpToN(100));
    }

    @Test
    void testSumFromPtoQ() {
        assertEquals(5050, renshu.sumFromPtoQ(1, 100));
        assertEquals(6, renshu.sumFromPtoQ(1, 3));
        assertEquals(-1, renshu.sumFromPtoQ(5, 3)); // assuming return -1 when p > q
    }

    @Test
    void testSumFromArrayIndex() {
        int[] a = { 1, 2, 3, 4, 5 };
        assertEquals(12, renshu.sumFromArrayIndex(a, 2));
        assertEquals(15, renshu.sumFromArrayIndex(a, 0));
        assertEquals(-1, renshu.sumFromArrayIndex(a, 5)); // assuming -1 for invalid index
    }

    @Test
    void testSelectMaxValue() {
        int[] a = { 1, 3, 5, 7, 9 };
        assertEquals(9, renshu.selectMaxValue(a));
        int[] b = { -1, -3, -5 };
        assertEquals(-1, renshu.selectMaxValue(b));
    }

    @Test
    void testSelectMinValue() {
        int[] a = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        assertEquals(1, renshu.selectMinValue(a));
        int[] b = { 5, -1, -5, 3 };
        assertEquals(-5, renshu.selectMinValue(b));
    }

    @Test
    void testSelectMaxIndex() {
        int[] a = { 10, 9, 8, 4, 15, 0, -3, 18, 9, 7 };
        assertEquals(7, renshu.selectMaxIndex(a));
    }

    @Test
    void testSelectMinIndex() {
        int[] a = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        assertEquals(9, renshu.selectMinIndex(a));
        int[] b = { 5, -1, -5, 3, -5 };
        assertEquals(2, renshu.selectMinIndex(b)); // Assuming it returns the first occurrence of the minimum value
    }

    @Test
    void testSwapArrayElements() {
        int[] p = { 1, 2, 3, 4, 5 };
        int[] expected = { 5, 2, 3, 4, 1 };
        renshu.swapArrayElements(p, 0, 4);
        assertArrayEquals(expected, p);
    }

    @Test
    void testSwapTwoArrays() {
        int[] a = { 1, 2, 3 };
        int[] b = { 4, 5, 6 };
        assertTrue(renshu.swapTwoArrays(a, b));
        assertArrayEquals(new int[] { 4, 5, 6 }, a);
        assertArrayEquals(new int[] { 1, 2, 3 }, b);
        int[] c = { 1, 2 };
        assertFalse(renshu.swapTwoArrays(a, c)); // testing with different lengths
    }

    @Test
    void bubbleSort() {
    int[] a = new int[]{5, 2, 3, 5, 4};
    renshu.bubble_sort(a);
    assertArrayEquals(new int[]{2, 3, 4, 5, 5}, a);
}
@Test
    void quickSort() {
        int[] a = new int[]{5, 2, 3, 5, 4};
        Renshu.quickSort(a);
        assertArrayEquals(new int[]{2, 3, 4, 5, 5}, a);
    }

    @Test
    void quickSortShortTime() {
        int[] a = new int[1000000];
        Random r = new Random();
        for (int i = 0; i < 1000000; i++) {
            a[i] = r.nextInt(1000000);
        }
        Assertions.assertTimeout(Duration.ofSeconds(1), () -> { // NOTE: 1秒以内に実行できる
            Renshu.quickSort(a);
            assertTrue(isSorted(a)); // NOTE: ソートされている
        });
    }

    boolean isSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) return false;
        }
        return true;
    }
}
