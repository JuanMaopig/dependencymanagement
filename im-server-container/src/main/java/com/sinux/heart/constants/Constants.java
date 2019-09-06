package com.sinux.heart.constants;

public class Constants {
	public interface FilePath{
		public static String HEART_PROPERTIES = FilePath.class.getResource("/heart.properties").getPath();
	}
	/**
	 * 心跳内容key
	 * @ClassName HeartMessage
	 * @Description: 心跳内容key
	 * @date 2019年6月16日 下午4:58:49
	 */
	public interface HeartMessage{
		/** 额外数据，同步数据 */
		public static String MSG = "msg";
		/** 签名 */
		public static String SIGN = "sign";
		/** 消息类型 1接收 2接收后回执 */
		public static String MSGTYPE = "msgType";
		/** 发送方ip */
		public static String RECEIVE_IP = "ip";
		/** 发送方端口 */
		public static String RECEIVE_PORT = "port";
		/** 编码 */
		public static String ENCODING_UTF_8 = "utf-8";
		/** 设备集合key分隔符 */
		public static String IP_PORT_SPLIT = ":";
		/** 需要删除的sign */
		public static String REMOVE_SIGN = "removeSign";
	}
}
