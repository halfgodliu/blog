package com.lyjava.service.impl;

import com.google.gson.Gson;
import com.lyjava.domain.ResponseResult;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import com.lyjava.service.UploadService;
import com.lyjava.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;


@Service
@Data
@ConfigurationProperties(prefix = "oss")//方便下面的三个属性从properties.yml文件中获取值
public class OssUploadService implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * 上传图片的方法
     * @param img 图片
     * @return
     */
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断，必须传入.jpg/.jpeg/.png后缀的图片
        if (!originalFilename.endsWith(".jpg")&&!originalFilename.endsWith(".jpeg")&&!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.IMG_TYPE_ERROR);
        }

        //判断通过，上传到OSS
        //先生成一个新的文件名
        String filePath = PathUtils.generateFilePath(originalFilename);

        //将图片文件和新的文件名传入自定义的方法，去进行OSS上传和返回图片地址
        String url = uploadOss(img, filePath);
        if (!StringUtils.hasText(url)){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return ResponseResult.okResult(url);
    }

    public String uploadOss(MultipartFile img, String filePath){
        //指定区域，如华东，华南，autoRegion()：自动选择最近的区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //指定存储的文件名，默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = img.getInputStream();

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
                //图片地址
                String url = "http://qiniu.quancifang.work/"+key;
                return url;
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
        return null;
    }
}
