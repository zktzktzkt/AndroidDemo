package demo.zkttestdemo.retrofit;

/**
 * 友好的错误提示异常类
 * <p>
 * Created by zkt on 2018-2-3.
 */

public class ResultException extends RuntimeException {
    // 默认是 -10000
    private String errCode = "-10000";

    /**
     * @param errCode 用于程序判断
     * @param message  友好的提示
     */
    public ResultException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
    }
}
