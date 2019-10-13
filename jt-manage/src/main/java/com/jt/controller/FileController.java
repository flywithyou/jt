package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVo;

@RestController
public class FileController {
	@Autowired
	private FileService fileService;
	/**
	 * 文件上传成功后，返回json数据
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws  IOException {
		//1.获取图片名称
		String fileName = fileImage.getOriginalFilename();
		//2.创建文件对象，指定上传目录
		File dir = new File("E:/eclipse-workspace/LYJ/jt-images");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = "E:/eclipse-workspace/LYJ/jt-images/"+fileName;
		File file = new File(path);
		fileImage.transferTo(file);
		return "文件上传成功！";
	}
	
	@RequestMapping("/pic/upload")
	public ImageVo fileUpload(MultipartFile uploadFile) {
		return fileService.upload(uploadFile);
	}
}
