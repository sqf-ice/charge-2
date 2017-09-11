package com.clouyun.charge.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.clouyun.boot.common.exception.BizException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 描述: UploadFile
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshui
 * 版本: 2.0
 * 创建日期: 2017年3月28日
 */
public class OSSUploadFileUtils {
	
	private static final String ENDPOINT = "http://oss-cn-shenzhen-internal.aliyuncs.com"; //内网地址
	//private static final String ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com"; //外网地址
	
	private static final String ACCESSKEYID = "LTAIxT31AmyQtF1Y";
	
	private static final String SECRETACCESSKEY = "IGsxubka6LwtSuxqX8XgWfkL6Zi6iA";
	
	public final static String BUCKET_NAME = "xqc";

    private static final long PART_SIZE = 5 * 1024 * 1024L; // 每个Part的大小，最小为5MB

    private static final int CONCURRENCIES = 3; // 上传Part的并发线程数。
	
	/**
	 * 文件上传
	 *
	 * @param file     文件
	 * @param fileName = key : images/123456.png 或 information/images/123456.png
	 * @return
	 * @throws Exception
	 */
	public static void toUploadFile(MultipartFile file, String fileName) throws BizException {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(file.getSize());
			// 可以在metadata中标记文件类型
			if (fileName.endsWith(".apk")) {
				objectMeta.setContentType("application/vnd.android.package-archive");//video/mpeg4
			} else if (fileName.endsWith(".jpg")) {
				objectMeta.setContentType("image/jpeg");
			} else if (fileName.endsWith(".bmp")) {
				objectMeta.setContentType("image/bmp");
			} else if (fileName.endsWith(".gif")) {
				objectMeta.setContentType("image/gif");
			} else if (fileName.endsWith(".png")) {
				objectMeta.setContentType("image/png");
			} else if (fileName.endsWith(".html")) {
				objectMeta.setContentType("text/html");
			} else if (fileName.endsWith(".doc")) {
				objectMeta.setContentType("application/msword");
			} else if (fileName.endsWith(".txt")) {
				objectMeta.setContentType("text/plain");
			} else if (fileName.endsWith(".pdf")) {
				objectMeta.setContentType("application/pdf");
			} else if (fileName.endsWith(".docx")) {
				objectMeta.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			} else {
				throw new BizException(1002000);
			}
			InputStream input = file.getInputStream();
			getInstance().putObject(BUCKET_NAME, fileName, input, objectMeta);
		} catch (Exception e) {
			throw new BizException(1002001);
		}
	}
	
	/**
	 * 文件上传
	 *
	 * @param file     文件
	 * @param fileName = key : images/123456.png 或 information/images/123456.png
	 * @return
	 * @throws Exception
	 */
	public static void toUploadPictureFile(MultipartFile file, String fileName) throws BizException {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(file.getSize());
			// 可以在metadata中标记文件类型
//			if (fileName.endsWith(".apk")) {
//				objectMeta.setContentType("application/vnd.android.package-archive");//video/mpeg4
//			} else 
			if (fileName.endsWith(".jpg")) {
				objectMeta.setContentType("image/jpeg");
			} else if (fileName.endsWith(".bmp")) {
				objectMeta.setContentType("image/bmp");
			} else if (fileName.endsWith(".gif")) {
				objectMeta.setContentType("image/gif");
			} else if (fileName.endsWith(".png")) {
				objectMeta.setContentType("image/png");
//			} else if (fileName.endsWith(".html")) {
//				objectMeta.setContentType("text/html");
//			} else if (fileName.endsWith(".doc")) {
//				objectMeta.setContentType("application/msword");
//			} else if (fileName.endsWith(".txt")) {
//				objectMeta.setContentType("text/plain");
//			} else if (fileName.endsWith(".pdf")) {
//				objectMeta.setContentType("application/pdf");
//			} else if (fileName.endsWith(".docx")) {
//				objectMeta.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			} else {
				throw new BizException(1002000);
			}
			InputStream input = file.getInputStream();
			getInstance().putObject(BUCKET_NAME, fileName, input, objectMeta);
		} catch (Exception e) {
			throw new BizException(1002001);
		}
	}
	
	/**
	 * 文件上传
	 *
	 * @param content  文件内容
	 * @param fileName = key : images/123456.png 或 information/images/123456.png
	 * @return
	 * @throws Exception
	 */
	public static void toUploadFile(String content, String fileName) throws BizException {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			//objectMeta.setContentLength(content.length());
			// 可以在metadata中标记文件类型
			if (fileName.endsWith(".apk")) {
				objectMeta.setContentType("application/vnd.android.package-archive");//video/mpeg4
			} else if (fileName.endsWith(".mp4")) {
				objectMeta.setContentType("video/mpeg4");
			} else if (fileName.endsWith(".mp3")) {
				objectMeta.setContentType("audio/mp3");
			} else if (fileName.endsWith(".jpg")) {
				objectMeta.setContentType("image/jpeg");
			} else if (fileName.endsWith(".png")) {
				objectMeta.setContentType("image/png");
			} else if (fileName.endsWith(".html")) {
				objectMeta.setContentType("text/html");
			}
			getInstance().putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(content.getBytes()), objectMeta);
		} catch (Exception e) {
			throw new BizException(1002001);
		}
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @param fileName 文件名称带路径
	 * @return
	 */
	public static String upload(MultipartFile file, String fileName) throws BizException {
		try {
			OSSClient client = getInstance();
			if (file.getSize() < PART_SIZE) {
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				String contentType = OSSUploadFileUtils.contentType(suffix);
				uploadFile(client, BUCKET_NAME, fileName, file, contentType);
			} else {
				uploadBigFile(client, BUCKET_NAME, fileName, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(1002001);
		} finally {
		}
		return fileName;
	}
    /**
     * 文件下载
     * @param key
     * @param directory
     */
	public static void download(String key, String directory) {
		download(key, directory, false);
	}
	/**
	 * 文件下载
	 * @param key
	 * @param directory
	 * @param del 是否删除
	 */
	public static void download(String key, String directory, boolean del) {
		OSSClient client = getInstance();
		client.getObject(new GetObjectRequest(BUCKET_NAME, key), new File(directory + "/" + key));
		if (del)
			client.deleteObject(BUCKET_NAME, key);
	}

	/**
	 * 删除对象
	 * @param key
	 */
	public static void delete(String key) {
		OSSClient client = getInstance();
		client.deleteObject(BUCKET_NAME, key);
	}


	// 上传文件
	public static void uploadFile(OSSClient client, String bucketName, String key, MultipartFile file, String contentType) throws OSSException, ClientException, IOException {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.getSize());
		// 可以在metadata中标记文件类型
		objectMeta.setContentType(contentType);//video/mpeg4
		InputStream input = file.getInputStream();
		client.putObject(bucketName, key, input, objectMeta);
	}
	
	public static String contentType(String fileNameExt) throws BizException {
		String contentType;
		switch (fileNameExt) {
			case "apk":
				contentType = "application/vnd.android.package-archive";
				break;
			case "jpg":
				contentType = "image/jpeg";
				break;
			case "bmp":
				contentType = "image/bmp";
				break;
			case "gif":
				contentType = "image/gif";
				break;
			case "png":
				contentType = "image/png";
				break;
			case "html":
				contentType = "text/html";
				break;
			case "doc":
				contentType = "application/msword";
				break;
			case "txt":
				contentType = "text/plain";
				break;
			case "pdf":
				contentType = "application/pdf";
				break;
			case "docx":
				contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
				break;
			default:
				throw new BizException(1002000);
		}
		return contentType;
	}
	
	// 通过Multipart的方式上传一个大文件
	// 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
	private static void uploadBigFile(OSSClient client, String bucketName, String key, MultipartFile uploadFile) throws OSSException, ClientException, InterruptedException {
		
		int partCount = calPartCount(uploadFile);
		if (partCount <= 1) {
			throw new IllegalArgumentException("要上传文件的大小必须大于一个Part的字节数：" + PART_SIZE);
		}
		
		String uploadId = initMultipartUpload(client, bucketName, key);
		
		ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
		
		List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());
		
		for (int i = 0; i < partCount; i++) {
			long start = PART_SIZE * i;
			long curPartSize = PART_SIZE < uploadFile.getSize() - start ? PART_SIZE : uploadFile.getSize() - start;
			pool.execute(new UploadPartThread(client, bucketName, key, uploadFile, uploadId, i + 1, PART_SIZE * i, curPartSize, eTags));
		}
		// 关闭线程池（线程池不马上关闭），执行以前提交的任务，但不接受新任务。
		pool.shutdown();
		// 如果关闭后所有任务都已完成，则返回 true
		while (!pool.isTerminated()) {
			pool.awaitTermination(5, TimeUnit.SECONDS);
		}
		
		if (eTags.size() != partCount) {
			throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
		}
		
		completeMultipartUpload(client, bucketName, key, uploadId, eTags);
	}
	
	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
	private static int calPartCount(MultipartFile f) {
		int partCount = (int) (f.getSize() / PART_SIZE);
		if (f.getSize() % PART_SIZE != 0) {
			partCount++;
		}
		return partCount;
	}
	
	// 完成一个multi-part请求。
	private static void completeMultipartUpload(OSSClient client, String bucketName, String key, String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
		//为part按partnumber排序
		// completeMultipartUpload方法会进行排序
		//Collections.sort(eTags, new Comparator<PartETag>() {
		//	public int compare(PartETag arg0, PartETag arg1) {
		//		PartETag part1 = arg0;
		//		PartETag part2 = arg1;
		//		return part1.getPartNumber() - part2.getPartNumber();
		//	}
		//});
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
		client.completeMultipartUpload(completeMultipartUploadRequest);
	}
	
	// 初始化一个Multi-part upload请求。
	private static String initMultipartUpload(OSSClient client, String bucketName, String key) throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}
	
	private static class UploadPartThread implements Runnable {
		
		private MultipartFile uploadFile;
		
		private String bucket;
		
		private String object;
		
		private long start;
		
		private long size;
		
		private List<PartETag> eTags;
		
		private int partId;
		
		private OSSClient client;
		
		private String uploadId;
		
		UploadPartThread(OSSClient client, String bucket, String object, MultipartFile uploadFile, String uploadId, int partId, long start, long partSize, List<PartETag> eTags) {
			this.uploadFile = uploadFile;
			this.bucket = bucket;
			this.object = object;
			this.start = start;
			this.size = partSize;
			this.eTags = eTags;
			this.partId = partId;
			this.client = client;
			this.uploadId = uploadId;
		}
		
		@Override
		public void run() {
			
			InputStream in = null;
			try {
				in = uploadFile.getInputStream();
				in.skip(start);
				
				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucket);
				uploadPartRequest.setKey(object);
				uploadPartRequest.setUploadId(uploadId);
				uploadPartRequest.setInputStream(in);
				uploadPartRequest.setPartSize(size);
				uploadPartRequest.setPartNumber(partId);
				
				UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
				
				eTags.add(uploadPartResult.getPartETag());
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception e) {
					}
			}
		}
	}
	
	private OSSUploadFileUtils() {
	}
	
	public static OSSClient getInstance() {
		return SingleTonBuilder.instance;
	}
	
	private static class SingleTonBuilder {
		
		private static OSSClient instance = new OSSClient(ENDPOINT, ACCESSKEYID, SECRETACCESSKEY);
	}
	
}
