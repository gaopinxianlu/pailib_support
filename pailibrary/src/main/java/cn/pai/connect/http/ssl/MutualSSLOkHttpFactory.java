package cn.pai.connect.http.ssl;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.pai.connect.http.OkHttpFactory;
import cn.pai.connect.http.cookie.CookieJarImpl;
import cn.pai.connect.http.cookie.CookieMemory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author pany
 * @description SSL双向认证OkHttpClient创建
 * @date 2017年6月9日下午2:50:26
 */
public class MutualSSLOkHttpFactory implements OkHttpFactory {

    private Context context;

    public MutualSSLOkHttpFactory(Context context) {
        this.context = context;
    }

    @Override
    public OkHttpClient create() {
        final File cacheFile = new File(context.getExternalCacheDir(),
                "httpCache");
        // 创建缓存对象
        final Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);// 缓存大小为10M
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream priKey = assetManager.open("certificate/ca-cert.pem");
            InputStream pubKey = assetManager.open("certificate/ca-cert.pem");
            clientBuilder
                    .retryOnConnectionFailure(false)
                    // 失败不重发
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).cache(cache)// 缓存
                    .sslSocketFactory(SSLSocketHelper.getMutualSSLFactory(priKey, "", pubKey, ""))
                    .cookieJar(new CookieJarImpl(new CookieMemory())).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientBuilder.build();
    }
}
