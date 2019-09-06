package com.sinux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 
* <p>Title: ImServerAuthApplication</p>  
* <p>Description: 综合管理服务端授权服务启动类</p>  
* @author yexj  
* @date 2019年5月29日
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoConfiguration
public class ImServerAuthApplication 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ImServerAuthApplication.class, args);
    }
}
