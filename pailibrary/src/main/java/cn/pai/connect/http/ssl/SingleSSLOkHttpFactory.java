package cn.pai.connect.http.ssl;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.pai.connect.http.OkHttpFactory;
import cn.pai.connect.http.cookie.CookieJarImpl;
import cn.pai.connect.http.cookie.CookieMemory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author pany
 * @description SSL单向认证OkHttpClient创建
 * @date 2017年6月9日下午2:50:26
 */
public abstract class SingleSSLOkHttpFactory implements OkHttpFactory {

    private Context context;

    public SingleSSLOkHttpFactory(Context context) {
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
                .sslSocketFactory(SSLSocketHelper.getSingleSSLFactory(getCertificateStream(context)))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        //这里必须验证主机名是否与服务器的身份验证方案匹配，否则会报异常
                        return true;
                    }
                })
                .cookieJar(new CookieJarImpl(new CookieMemory())).build();
        return clientBuilder.build();
    }

    protected abstract InputStream[] getCertificateStream(Context context);
}
