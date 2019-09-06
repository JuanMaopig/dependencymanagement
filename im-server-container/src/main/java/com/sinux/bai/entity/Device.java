package com.sinux.bai.entity;

import java.io.Serializable;
import java.net.DatagramSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinux.base.support.common.constants.Constants;
import com.sinux.base.support.common.util.HeartUtil;
import com.sinux.bai.entity.Device;
import com.sinux.bai.entity.device.BaseDevice;
import com.sinux.bai.entity.device.EntityInterface;
import com.sinux.bai.entity.device.interfaces.UdpInterface;
import com.sinux.bai.utils.ConvertDeviceUtil;

/**
 * 设备表，保存了设备的基本信息
 * @author Sopp
 * @since 2019-07-10
 */
public class Device extends BaseDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    /**日志*/
    private Logger log = LoggerFactory.getLogger(Device.class);
    /**
     * 主键
     */
    private Integer id;
    /**
     * 设备经度
     */
    private Double lng;
    /**
     * 设备纬度
     */
    private Double lat;

    /**
     * 初始化设备对象，增加超时判断
     * 
     * @time 2019年8月2日-上午11:49:12
     * @todo TODO
     * @author Tangjc
     */
    public Device(){
    	
    }

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Override
	public int initialize() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int powerOff() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int powerOn() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 重置倒数
	 * @Title: resetSecond
	 * @Description: 重置倒数
	 * @author Tangjc
	 * @return void
	 */
	@Override
	public void resetSecond(){
		this.setTimeoutSecond(this.getTimeout());
	}
	
	
	@Override
	public void restartListener(DatagramSocket socket) {
		EntityInterface eif = this.getEntityInterface();
		if(eif instanceof UdpInterface) {
			UdpInterface uif = (UdpInterface)eif;
			uif.setDatagramSocket(socket);
			//开启接口线程状态为true
			uif.setThreadStatus(true);
			//开启接收线程监听
			uif.startReceiveDataThread();
			//开启超时监听线程
			this.startTimeOutListern();
		}
	}

	/**
	 * 
	 * <p>Title: startTimeOutListern</p>  
	 * <p>Description: 启动设备超时监听线程</p>  
	 * @author yexj  
	 * @date 2019年8月16日
	 */
	public void startTimeOutListern() {
		Sets.tpe.execute(() -> {
			while(this.getEntityInterface().getThreadStatus()){
				try {
					//设备倒计时超时判断
					Thread.sleep(1000);
					int second = this.getTimeoutSecond().decrementAndGet();
					if(second==0){
						log.info("设备ID为"+this.getDeviceId()+"的设备超时。");
						// 修改状态
						this.setDeviceStatus(Constants.DEVICE_OFLINE_STATUS);
						// 移除设备监听线程，并关闭socket连接
						this.getEntityInterface().shutDownThreadAndSocket();
						// 发送变化数据到消息队列
						Sets.heartBox.getMsgQueueMap().get(Sets.heartBox.getpIp()).put(HeartUtil.genJson("childDevice", Constants.SYN_MSG_TYPE_UPDATE, ConvertDeviceUtil.cvDeviceToJsonObject(Sets.getDm().getDeviceById(this.getDeviceId())), this.getDeviceId()));
					}
				} catch (InterruptedException e) {
					log.error("设备超时处理异常！",e);
				}
			}
		});
	}
}

