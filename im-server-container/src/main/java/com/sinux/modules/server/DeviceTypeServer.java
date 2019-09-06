package com.sinux.modules.server;





import com.sinux.base.support.common.entity.Query;
import com.sinux.base.support.common.entity.R;




/**
 * 
* <p>Title: DeviceTypeServer</p>  
* <p>Description: 设备类型字典服务接口</p>  
* @author yexj  
* @date 2019年6月17日
 */
public interface DeviceTypeServer {
	
	/**
	 * 
	 * <p>Title: getAllList</p>  
	 * <p>Description: 获取所有设备类型字典数据</p>  
	 * @author yexj  
	 * @date 2019年6月17日  
	 * @return 返回查询数据，code=200查询成功，code=500查询失败
	 */
	public R getAllList();
	
	public R getList(Query query);
	

	
	
	
}
