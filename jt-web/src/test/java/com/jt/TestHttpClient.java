package com.jt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	@Autowired
	private HttpClientService client;
	/**
	 * 1.实例化工具API对象
	 * 2.确定请求URL地址
	 * 3 第一请求方式 get、post
	 * 4 发起http请求，并且获取响应数据
	 * 5 判断状态码status是否为200
	 * 6 获取服务器返回值信息
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com";
		client = new HttpClientService();
		String data = client.doGet(url);
		System.out.println(data);
//		CloseableHttpClient client = HttpClients.createDefault();
//		HttpGet get = new HttpGet(url);
//		CloseableHttpResponse response = client.execute(get);
//		if (response.getStatusLine().getStatusCode() == 200) {
//			String data = EntityUtils.toString(response.getEntity(),"utf-8");
//			System.out.println(data);
//		}
	}
}
