
package team3200.year2014;

/**
 * This class stores past values in doubles.
 * @author Raptacon
 */
public class PastValueStorage {
    private final double[] data; // More recent data is closer to slot 0
    private int timesDataStored; // Only increases to data.length
    
    /**
     * Creates a new storage.
     * @param length How long the storage should be
     */
    public PastValueStorage(int length) {
        data = new double[length];
        timesDataStored = 0;
    }
    
    /**
     * Store a new data point.
     * @param value The double to store
     */
    public void putDouble(double value) {
        System.arraycopy(data, 0, data, 1, data.length - 1); // Move all data down one slot
        data[0] = value;
        
        if (timesDataStored < data.length)
            timesDataStored++;
    }
    
    /**
     * Gets the most recent data.
     * @return The most recent value
     */
    public double getRecentDouble() {
        return data[0];
    }
    
    /**
     * Calculates the average value of the data stored.
     * @return The average value
     */
    public double getAverageValue() {
        double result = 0;
        if (timesDataStored > 0) { // If 0, nothing is stored...
            for (int i = 0; i < timesDataStored; i++) // Get all the data...
                result += data[i];
            result /= timesDataStored;
        }
        return result;
    }
    
    /**
     * Resets the storage.
     */
    public void clear() {
        double[] empty = new double[data.length];
        
        System.arraycopy(empty, 0, data, 0, data.length); // Empty out the data
        timesDataStored = 0;
    }
    
    /**
     * Prints all of the data that is stored.
     */
    public void printAllData() {
        System.out.println("");
        for (int i = 0; i < timesDataStored; i++)
            System.out.print(data[i] + " ");
        System.out.print("\n");
    }
}
