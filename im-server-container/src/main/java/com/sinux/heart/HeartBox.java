package com.sinux.heart;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import com.sinux.base.support.common.util.ResourceUtil;
import com.sinux.heart.constants.Constants;
/**
 * 存储心跳信息
 * @ClassName Box
 * @Description: 存储心跳信息
 * @author Tangjc
 * @date 2019年6月10日 上午10:38:23
 */
public class HeartBox {
	/** 本机ip */
	private String ip;
	/** 本机端口 */
	private int port;
	/** 父级ip */
	private String pIp;
	/** 父级端口 */
	private int pPort;
	/** 上报超时时间 */
	private long timeout = 5000;
	/** 在线设备状态临时表 */
	private List<Map<String,String>> devices;
	/** socket对象 */
	private DatagramSocket socket;
	/** 额外消息队列集 */
	private Map<String,ArrayBlockingQueue<String>> msgQueueMap = new HashMap<String,ArrayBlockingQueue<String>>();
	/** 收集心跳传过来的额外数据集合 */
	private ArrayBlockingQueue<String> receiveQueue = new ArrayBlockingQueue<String>(1000, true);
	/** 消息ack */
	private Map<String,String> ackMap = new HashMap<String,String>();
	/** 在线客户端设备列表 key->ip:port value->status 1:在线 0:不在线*/
	//private Map<String,Integer> deivceMap = new HashMap<String,Integer>();
	/** 临时在线设备状态 */
	//private Map<String,Integer> tmpDeviceMap = new ConcurrentHashMap<String,Integer>();
	/** 心跳配置 */
	public static Map<String,String> heartMap;
	static{
		Properties pro;
		try {
			pro = ResourceUtil.getProperties(Constants.FilePath.HEART_PROPERTIES);
			heartMap = ResourceUtil.propertiesToMap(pro);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getpIp() {
		return pIp;
	}
	public void setpIp(String pIp) {
		this.pIp = pIp;
	}
	public int getpPort() {
		return pPort;
	}
	public void setpPort(int pPort) {
		this.pPort = pPort;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public List<Map<String, String>> getDevices() {
		return devices;
	}
	public void setDevices(List<Map<String, String>> devices) {
		this.devices = devices;
	}
	public Map<String, ArrayBlockingQueue<String>> getMsgQueueMap() {
		return msgQueueMap;
	}
	public void setMsgQueueMap(Map<String, ArrayBlockingQueue<String>> msgQueueMap) {
		this.msgQueueMap = msgQueueMap;
	}
	public Map<String, String> getAckMap() {
		return ackMap;
	}
	public void setAckMap(Map<String, String> ackMap) {
		this.ackMap = ackMap;
	}
	public DatagramSocket getSocket() {
		return socket;
	}
	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}
	public ArrayBlockingQueue<String> getReceiveQueue() {
		return receiveQueue;
	}
	public void setReceiveQueue(ArrayBlockingQueue<String> receiveQueue) {
		this.receiveQueue = receiveQueue;
	}
//	public Map<String, Integer> getDeivceMap() {
//		return deivceMap;
//	}
//	public void setDeivceMap(Map<String, Integer> deivceMap) {
//		this.deivceMap = deivceMap;
//	}
//	public Map<String, Integer> getTmpDeviceMap() {
//		return tmpDeviceMap;
//	}
//	public void setTmpDeviceMap(Map<String, Integer> tmpDeviceMap) {
//		this.tmpDeviceMap = tmpDeviceMap;
//	}
	public static Map<String, String> getHeartMap() {
		return heartMap;
	}
	public static void setHeartMap(Map<String, String> heartMap) {
		HeartBox.heartMap = heartMap;
	}
}
