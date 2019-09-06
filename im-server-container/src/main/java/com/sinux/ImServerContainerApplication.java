package com.sinux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 
* <p>Title: ImServerDeviceApplication</p>  
* <p>Description: 综合管理服务端容器服务启动类</p>  
* @author yexj  
* @date 2019年5月29日
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
public class ImServerContainerApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ImServerContainerApplication.class, args);
    }
}

