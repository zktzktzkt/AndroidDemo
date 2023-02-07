package demo.zkttestdemo.effect.coordinator.jdBehavior;

/**
 * MathUtils
 *
 * @author duanwenqiang1
 * @date 2018/6/5
 */
public class MathUtils {

    /**
     *
     * @param amount
     * @param low
     * @param high
     * @return
     */
    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     *
     * @param amount
     * @param low
     * @param high
     * @return
     */
    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     * This method takes a numerical value and ensures it fits in a given numerical range. If the
     * number is smaller than the minimum required by the range, then the minimum of the range will
     * be returned. If the number is higher than the maximum allowed by the range then the maximum
     * of the range will be returned.
     *
     * @param value the value to be clamped.
     * @param min minimum resulting value.
     * @param max maximum resulting value.
     *
     * @return the clamped value.
     */
    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

}
