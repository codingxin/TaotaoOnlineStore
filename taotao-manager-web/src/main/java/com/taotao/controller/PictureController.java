package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.taotao.utils.FastDFSClient;

@Controller  
public class PictureController {  
	
	//BUG:火狐浏览器不支持map类型,解决：https://blog.csdn.net/u012453843/article/details/70182519

   //得到resource.propeties里面的服务器图片地址
    @Value("${IMAGE_SERVER_URL}")  
    private String IMAGE_SERVER_URL;  
/*@Value("${IMAGE_SERVER_URL}")是为了注入我们在配置文件resource.properties中配置的图片
 * 访问前缀
 * @RequestMapping("/pic/upload")指定上传文件请求的url， 与下图指定url一样，
 *public Map uploadFile(MultipartFile uploadFile) 
 *参数"uploadFile"与下图的上传文件的方法参数名称是要求一样的。*/
    @RequestMapping("/pic/upload")  
    @ResponseBody  
    public String uploadFile(MultipartFile uploadFile) {  
        Map result  = new HashMap<>();  
        try {  
        
            //1.接收上传的文件  
            //2.获取扩展名  
            String orignalName = uploadFile.getOriginalFilename();  
            String extName = orignalName.substring(orignalName.lastIndexOf(".")+1);  
            //3.上传到图片服务器  
            FastDFSClient fastDFSClient = new FastDFSClient("F:\\TaotaoOnlineStore\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");  
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);  
            System.out.println(url);
            url = IMAGE_SERVER_URL + url;  
            result.put("error", 0);  
            result.put("url", url);  
            //return result;  
            return JSON.toJSONString(result);
        }  catch (Exception e) {  
            e.printStackTrace();  
            result.put("error", 0);  
            result.put("message", "上传图片失败！");  
            //return result;  
            return JSON.toJSONString(result);
        }  
    }  
}