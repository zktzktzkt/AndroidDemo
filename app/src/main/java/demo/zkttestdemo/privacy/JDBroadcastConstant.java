package demo.zkttestdemo.privacy;

/**
 * 系统底层发出的各种广播，统一加入到这里
 * @author liuweihuan
 *
 */
public interface JDBroadcastConstant {
	/**
	 * 同意了隐私协议
	 */
	String ACTION_PRIVACY_AGREE = "com.jingdong.action.privacy.agree";
	/**
	 * 没同意隐私协议
	 */
	String ACTION_PRIVACY_NOT_AGREE = "com.jingdong.action.privacy.not.agree";
}
