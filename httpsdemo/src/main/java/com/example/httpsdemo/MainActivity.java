package com.example.httpsdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                getHttp();
                getHttpsClient();
            }
        };
        new Thread(runnable).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tvContent.setText(String.valueOf(msg.obj));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getHttp() {
        String https = "http://testapi.kakasure.com/index/recommend.json";
        HttpURLConnection conn = null;
        InputStreamReader isr = null;
        try {
            //构建图片的url地址
            URL url = new URL(https);
            //开启连接
            conn = (HttpURLConnection) url.openConnection();
            //设置超时的时间，5000毫秒即5秒
            conn.setConnectTimeout(3000);
            //设置获取图片的方式为GET
            conn.setRequestMethod("GET");
            //响应码为200，则访问成功
            if (conn.getResponseCode() == 200) {
                isr = new InputStreamReader(conn.getInputStream()); //获取输入流，此时才真正建立链接
                BufferedReader br = new BufferedReader(isr);
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // 读取结果，发送到主线程
                handler.obtainMessage(0, sb.toString()).sendToTarget();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void getHttps() {
//        String https = "https://testapi.kakasure.com" + "/index/recommend.json";
        String https = "https://testapi.kakasure.com" + "/index/recommend.json";
        HttpsURLConnection conn = null;
        HttpURLConnection urlConnection = null;
        InputStreamReader isr = null;
        try {

            // 打开某个地址的连接
            urlConnection = (HttpURLConnection) new URL(https).openConnection();
            urlConnection.setRequestMethod("GET");
            // 设置SSLSocketFoactory，这里有两种：1.需要安全证书 2.不需要安全证书
            if (urlConnection instanceof HttpsURLConnection) { // 是Https请求
                conn = (HttpsURLConnection) urlConnection;

                // 负责证书管理和信任管理器的，我们说Https可以有证书也可以没有证书
                SSLContext sslContext = getSSLContext("kakasurecom.cer");
                if (sslContext != null) {
                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    conn.setSSLSocketFactory(sslSocketFactory);
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
                    HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
                }
            }

            urlConnection.setConnectTimeout(8 * 1000);
            urlConnection.setReadTimeout(8 * 1000);

            if (urlConnection.getResponseCode() == 200) {
                isr = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // 读取结果，发送到主线程
                handler.obtainMessage(0, sb.toString()).sendToTarget();
            }

        } catch (MalformedURLException e) {
            // URL
            Log.e(this.getClass().getName(), e.getMessage());
        } catch (IOException e) {
            // URL
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // isr.close();
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void getHttpsClient() {
        String https = "https://testapi.kakasure.com" + "/index/recommend.json";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //注册密匙库
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(getKeyStore("kakasurecom.cer"));
            //不校验域名
            socketFactory.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", socketFactory, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);
            HttpGet httpget = new HttpGet(https);
            ResponseHandler responseHandler = new BasicResponseHandler();
            String responseBody = (String) httpclient.execute(httpget, responseHandler);
            // 读取结果，发送到主线程
            handler.obtainMessage(0, responseBody).sendToTarget();
            System.out.println(responseBody);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }  catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    private class MyHostnameVerifier implements HostnameVerifier {

        /**
         * 校验服务器证书的域名是否相符
         *
         * @param s
         * @param sslSession
         * @return
         */
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            boolean result = hv.verify("*.testapi.kakasure.com", sslSession);
            return result;
        }
    }

    public SSLContext getSSLContext(String certName) {
        // 生成SSLContext对象
        SSLContext sslContext = null;
        InputStream inStream = null;
        try {
            sslContext = SSLContext.getInstance("TLS");

            // 从assets中加载证书
            inStream = getApplicationContext().getAssets().open(certName);

            // 证书工厂 -- 读取证书
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            final Certificate cer = cerFactory.generateCertificate(inStream); // 是APP中预埋的服务器端公钥证书

            // 密钥库
            KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType()); // "PKCS12"
            kStore.load(null, null);
            kStore.setCertificateEntry("trust", cer);

            // 密钥管理器
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(kStore, null); // 加载密钥库到管理器

            // 信任管理器
            TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tFactory.init(kStore); // 加载密钥库到信任管理器

            // 初始化
//            sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());

            TrustManager[] trustAllCerts = {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        /**
                         * 对服务器证书域名进行强校验
                         * @param x509Certificates
                         * @param s
                         * @throws CertificateException
                         */
                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                            if (x509Certificates == null) {
                                throw new IllegalArgumentException("check Server x509Certificates is null");
                            }

                            if (x509Certificates.length < 0) {
                                throw new IllegalArgumentException("check Server x509Certificates is empty");
                            }

                            for (X509Certificate cert : x509Certificates) {
                                cert.checkValidity();
                                // 检查服务器端证书签名是否有问题
                                try {
                                    // 和APP预埋的证书做对比
                                    cert.verify(cer.getPublicKey());
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchProviderException e) {
                                    e.printStackTrace();
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            sslContext.init(keyFactory.getKeyManagers(), trustAllCerts, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            // SSLContext.getInstance
            Log.e(this.getClass().getName(), e.getMessage());
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sslContext;
        }
    }

    public KeyStore getKeyStore(String certName) {
        // 生成SSLContext对象
        SSLContext sslContext = null;
        InputStream inStream = null;
        KeyStore kStore = null;
        try {
            sslContext = SSLContext.getInstance("TLS");

            // 从assets中加载证书
            inStream = getApplicationContext().getAssets().open(certName);

            // 证书工厂 -- 读取证书
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            final Certificate cer = cerFactory.generateCertificate(inStream); // 是APP中预埋的服务器端公钥证书

            // 密钥库
            kStore = KeyStore.getInstance(KeyStore.getDefaultType()); // "PKCS12"
            kStore.load(null, null);
            kStore.setCertificateEntry("trust", cer);

            // 密钥管理器
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(kStore, null); // 加载密钥库到管理器

            // 信任管理器
            TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tFactory.init(kStore); // 加载密钥库到信任管理器

            // 初始化
//            sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());

            TrustManager[] trustAllCerts = {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        /**
                         * 对服务器证书域名进行强校验
                         * @param x509Certificates
                         * @param s
                         * @throws CertificateException
                         */
                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                            if (x509Certificates == null) {
                                throw new IllegalArgumentException("check Server x509Certificates is null");
                            }

                            if (x509Certificates.length < 0) {
                                throw new IllegalArgumentException("check Server x509Certificates is empty");
                            }

                            for (X509Certificate cert : x509Certificates) {
                                cert.checkValidity();
                                // 检查服务器端证书签名是否有问题
                                try {
                                    // 和APP预埋的证书做对比
                                    cert.verify(cer.getPublicKey());
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchProviderException e) {
                                    e.printStackTrace();
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            sslContext.init(keyFactory.getKeyManagers(), trustAllCerts, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            // SSLContext.getInstance
            Log.e(this.getClass().getName(), e.getMessage());
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return kStore;
        }
    }
}
