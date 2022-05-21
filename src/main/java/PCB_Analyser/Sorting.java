package PCB_Analyser;

public class Sorting {

    // QUICK SORT //
    /**
     * Utility method to swap two elements in the same array.
     * @param array the number array.
     * @param low the low number.
     * @param pivot the pivot point.
     */
    static void swap(int[] array, int low, int pivot){
        int temp = array[low];
        array[low] = array[pivot];
        array[pivot] = temp;
    }

    /**
     * Method used to partition an array of numbers with the intention of sorting from highest to lowest.
     * @param array The number array.
     * @param low The starting index.
     * @param high The ending index.
     * @return The partitioned number array.
     */
    static int partitionHigh(int[] array, int low, int high){
        int p = low, j;
        for (j=low+1; j <= high; j++)
            if (array[j] > array[low])
                swap(array, ++p, j);
        swap(array, low, p);
        return p;
    }

    /**
     * Quick Sort method for sorting from highest to lowest.
     * @param array the regular array of numbers to be sorted.
     * @param low the starting index.
     * @param high the ending index.
     *
     */
    public static int quicksortHigh(int[] array, int low, int high){
        if (low < high) {
            int p = partitionHigh(array, low, high);
            quicksortHigh(array, low, p-1);
            quicksortHigh(array, p+1, high);
        }
        return array[0];
    }
}
