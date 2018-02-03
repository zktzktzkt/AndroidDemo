package demo.zkttestdemo.retrofit.bean;

/**
 * Created by zkt on 2018-2-3.
 */

public class BaseBean<T> {

    T results;
    private boolean error;
    private String errCode = "-10003";
    private String errMsg = "服务器统一错误";

    public T getResults() {
        return results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
