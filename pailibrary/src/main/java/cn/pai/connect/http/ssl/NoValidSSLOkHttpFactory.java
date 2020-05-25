package cn.pai.connect.http.ssl;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.pai.connect.http.OkHttpFactory;
import cn.pai.connect.http.cookie.CookieJarImpl;
import cn.pai.connect.http.cookie.CookieMemory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * description：
 * author：pany
 * on 2019/4/26 23:25
 */
public class NoValidSSLOkHttpFactory implements OkHttpFactory {

    private Context context;

    public NoValidSSLOkHttpFactory(Context context) {
        this.context = context;
    }

    @Override
    public OkHttpClient create() {
        final File cacheFile = new File(context.getExternalCacheDir(),
                "httpCache");
        // 创建缓存对象
        final Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);// 缓存大小为10M
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder
                .retryOnConnectionFailure(false)
                // 失败不重发
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).cache(cache)// 缓存
                .sslSocketFactory(SSLSocketHelper.getNoValidSSLFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .cookieJar(new CookieJarImpl(new CookieMemory())).build();

        return clientBuilder.build();
    }
}
