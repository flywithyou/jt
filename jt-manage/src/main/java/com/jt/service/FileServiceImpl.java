package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVo;
@Service
@PropertySource("classpath:/properties/image.properties")	//加载pro配置文件
public class FileServiceImpl implements FileService {
	//存储文件的根路径
	@Value("${image.localDirPath}")
	private String localDirPath ;	//= "E:/eclipse-workspace/LYJ/jt-images/";
	//定义虚拟路径地址
	@Value("${image.urlDirPath}")
	private String urlDirPath;		// = "http://image.jt.com/";
	/**
	 *	 实现思路：
	 * 	1.校验图片类型 JPG/jpeg/png/jif
	 * 	2.校验是否为恶意程序。
	 * 	3 分文件存储
	 * 	4 防止文件重名 自定义文件名称 UUID。类型
	 */
	@Override
	public ImageVo upload(MultipartFile uploadFile) {
		//1.获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//2 校验 正则表达式
		if (!fileName.matches("^.+\\.(jpg|png|gif|jpeg)$")) {
			return ImageVo.fail();
		}
		System.out.println("测试成功");
		//3.校验恶意程序 图片：高度、宽度
		try {
			BufferedImage bImage = ImageIO.read(uploadFile.getInputStream());
			int width = bImage.getWidth();
			int height = bImage.getHeight();
			if ( 0 == width || 0== height) {
				return ImageVo.fail();
			}
			System.out.println("是图片");
			//4 分文件存储
			String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			String dirFilePath = localDirPath+dateDir;
			File dirFile = new File(dirFilePath);
			if (!dirFile.exists()) {
				//如果目录不存在，则需要创建
				dirFile.mkdirs();
			}
			System.out.println(dirFilePath);
			//5 动态生成文件名称UUID+文件后缀
			String uuid = UUID.randomUUID().toString();
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realFileName = uuid+fileType;
			String realFilePath = dirFilePath+realFileName;
			System.out.println(realFilePath);
			uploadFile.transferTo(new File(realFilePath));
			//6 数据回显
			String url = urlDirPath+dateDir+realFileName;
			ImageVo imageVo = new ImageVo(0, url, width, height);
			return imageVo;
		} catch (IOException e) {
			e.printStackTrace();
			return ImageVo.fail();
		}
	}

}
