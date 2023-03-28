package com.lyjava;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@SpringBootTest
public class OSSTest {
    /**
     * 从application.yml文件中获取数据
     */
    @Value("${oss.accessKey}")
    private String accessKey;
    @Value("${oss.secretKey}")
    private String secretKey;
    @Value("${oss.bucket}")
    private String bucket;

    @Test
    public void test(){
        //指定区域，如华东，华南，autoRegion()：自动选择最近的区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //前面已经定义好了，这里不用定义了
        /*String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucket = "your bucket name";*/

        //指定存储的文件名，默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "2023/cifang1.jpg";
        try {
            //不用这种方法
            /*byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);*/
            //用FileInputStream的上传 D:\document\图片\Saved Pictures\泉此方\cifang1.jpg
            InputStream inputStream =
                    new FileInputStream("D:\\document\\图片\\Saved Pictures\\泉此方\\cifang1.jpg");

            //创建凭证
            Auth auth = Auth.create(accessKey, secretKey);
            //上传凭证的方法
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
