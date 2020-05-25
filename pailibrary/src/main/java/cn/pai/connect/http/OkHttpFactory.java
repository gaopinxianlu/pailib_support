package cn.pai.connect.http;

import okhttp3.OkHttpClient;

/**
 * @author pany
 * @description Okhttp创建接口
 * @date 2017年6月9日下午2:50:26
 */
public interface OkHttpFactory {

	int DEFAULT_TIMEOUT = 15000; // 默认的超时时间

	OkHttpClient create();
}
