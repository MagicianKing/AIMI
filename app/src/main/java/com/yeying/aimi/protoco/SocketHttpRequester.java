package com.yeying.aimi.protoco;


import android.util.Log;

import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.utils.Tools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import Decoder.BASE64Decoder;

/**
 * 多文件上传
 */
public class SocketHttpRequester {

    // 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
    public static SocketBean post(String actionUrl, Map<String, String> params, FormFile[] files, int type) throws IOException {
        if (type == 0) {
            //普通上传
            actionUrl = Tools.upload_web;
        }


        SocketBean bean = new SocketBean();
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setDoInput(true);// 允许输入  
        conn.setDoOutput(true);// 允许输出  
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式  
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数  
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据  
        for (FormFile uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if (uploadFile.getInStream() != null) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            } else {
                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        // 请求结束标志  
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码  
        boolean success = conn.getResponseCode() == 200;
        InputStream in = conn.getInputStream();
//        InputStreamReader isReader = new InputStreamReader(in);  
//        BufferedReader bufReader = new BufferedReader(isReader);  
//        String line = null;  
//        String data = "getResult=";  
//        while ((line = bufReader.readLine()) != null)  
//            data += line;  

        List<Byte> list = new ArrayList();

        int i = 0;
        while ((i = in.read()) != -1) {

            list.add((byte) i);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(list.size());

        for (int a = 0; a < list.size(); a++) {

            byteBuffer.put(list.get(a));


        }


        byte[] bb = byteBuffer.array();

        String str = new String(bb, "utf-8");


        Result result = new Result().fromContent(str, "text/html");
        int index = result.content.indexOf("&");
        String content = result.content.substring(index + 1);

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bresult = decoder.decodeBuffer(content);
        String password = "hexinjingu_owl01";

        try {
            byte[] decryResult = decrypt(bresult, password);
            content = new String(decryResult);
        } catch (Exception e1) {
            Log.i("", "des解密失败");
            e1.printStackTrace();
        }

        outStream.close();
        in.close();
        conn.disconnect();
        bean.content = content;
        bean.isSuccess = true;
        return bean;
    }

    /**
     * 单文件上传
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static SocketBean post(String path, Map<String, String> params, FormFile file, int type) throws Exception {
        return post(path, params, new FormFile[]{file}, type);
    }

    public static SocketBean post(String path, Map<String, String> params, FormFile file) throws Exception {
        return post(path, params, new FormFile[]{file}, 0);
    }

    public static SocketBean post(String path, Map<String, String> params, FormFile[] files) throws Exception {
        return post(path, params, files, 0);
    }

    public static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
}
