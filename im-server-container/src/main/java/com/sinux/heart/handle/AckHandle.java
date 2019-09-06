package com.sinux.heart.handle;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.heart.HeartBox;
import com.sinux.heart.constants.Constants;
import com.sinux.heart.handle.base.BaseHandle;
/**
 * 回执同步处理类
 * @ClassName AckHandle
 * @Description: 回执同步处理类
 * @date 2019年6月10日 下午5:23:55
 */
public class AckHandle implements BaseHandle{
	@Override
	public void deal(String data,HeartBox heartBox) {
		JSONObject json = JSON.parseObject(data);
		// 存储设备数据 key->ip:port value->status 1:在线 0:不在线*/
		//heartBox.getTmpDeviceMap().put(json.getString(Constants.HeartMessage.RECEIVE_IP).concat(Constants.HeartMessage.IP_PORT_SPLIT).concat(json.getString(Constants.HeartMessage.RECEIVE_PORT)), com.sinux.base.support.common.constants.Constants.DEVICE_ONLINE_STATUS);
		// 获取完整性校验字段
		String sign = json.getString(Constants.HeartMessage.SIGN);
		String removeSign = json.getString(Constants.HeartMessage.REMOVE_SIGN);
		String verifySign = DigestUtils.md5Hex(json.getString(Constants.HeartMessage.MSG)==null?"":json.getString(Constants.HeartMessage.MSG));
		if(StringUtils.equals(sign, verifySign)){//数据完整
			// 从缓存删除ack数据
			if(heartBox.getAckMap().containsKey(removeSign)){
				heartBox.getAckMap().remove(removeSign);
			}
			// 判断是否需要同步数据
			String msg = json.getString(Constants.HeartMessage.MSG);
			if(StringUtils.isNotEmpty(msg)){//存在同步数据 进行数据同步
				try {
					heartBox.getReceiveQueue().put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
