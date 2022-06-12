package io.vocabulary.utils;

import io.vocabulary.model.Message;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
@Slf4j
public class CustomMultitasking extends Task<Void> {


    private static String savePath = null;
    private static String filePath = null;
    private static String realPath = null;

    private File file;
    private Consumer<String> consumer;

    public CustomMultitasking(String filePath,String savePath,String realPath) {
        this.savePath = savePath;
        this.filePath = filePath;
        this.realPath = realPath;
    }

    public CustomMultitasking( Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected Void call() throws Exception {

        long startTime = System.currentTimeMillis();
        // 声明文件对象
        File saverPath = new File(savePath);
        // 判断文件是否存在
        if (!saverPath.exists()) {
            // 文件不存在就创建一个一级目录【远程请求下载】
            saverPath.mkdirs();
        }
        // 根据/切割接受到的请求网络URL
        String[] urlName = filePath.split("/");
        // 获取到切割的字符串数组长度-1
        int len = urlName.length - 1;
        // 获取到请求下载文件的名称
        String uname = urlName[len];
        log.info("文件名字{}",uname);


        try {
            // 创建保存文件对象
            //创建新文件
            file = new File(saverPath + File.separator + uname);
            if (file != null && !file.exists()) {
                file.createNewFile();
            }

            // 通过高效字节输出流输出创建的文件对象
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            // 创建URL对象[请求路径]
            URL url = new URL(filePath);
            // 返回一个URLConnection实例，表示与URL引用的远程对象的URL
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            // 设置的值 doInput领域本 URLConnection指定值。
            uc.setDoInput(true);
            // 打开与此URL引用的资源的通信链接，如果此类连接尚未建立。
            uc.connect();
            // 获取服务端的字节输入流
            InputStream inputStream = uc.getInputStream();
            log.info("file size is：{}" , uc.getContentLength());

            // 声明字节数组存放读取的文件
            byte[] b = new byte[1024 * 1024];
            int byteRead = 0;
            // 循环读取
            while ((byteRead = inputStream.read(b)) != -1) {
                bufferedOutputStream.write(b, 0, byteRead);
            }
            // 关闭流和刷新流
            inputStream.close();
            bufferedOutputStream.close();
            long endTime = System.currentTimeMillis();
            log.info("下载耗时：{}" , (endTime -  startTime) / 1000 * 1.0 + "s");
            log.info("文件下载成功！");

            // ---------- 解压文件 ----------
            log.info("解压文件中...");

            // 创建zip文件对象
            String fileCode = (String) System.getProperties().get("file.encoding");
            String sysCode = (String)System.getProperties().get("sun.jnu.encoding");

            log.info(fileCode);
            log.info(sysCode);
            ZipFile zipFile = new ZipFile(savePath+File.separator+uname);
            zipFile.setFileNameCharset(sysCode);
            // 解压全部zip文件
            zipFile.extractAll(savePath);

            File fileZip = new File(savePath + File.separator + uname);
            fileZip.delete();

            Message message = new Message();
            message.setFlag(false);
            EventBusUtil.getEventBus().post(message);
        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
            saverPath.delete();
        }

        return null;
    }
}