package com.sen.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;

/***
 * java抓取网络图片
 * 
 * 
 */
public class DownLoadPic {

	// 编码
	private static final String ECODING = "UTF-8";
	// 获取img标签正则
	private static final String IMGURL_REG = "<img.*src=(.*?) title=.*?>";
	// 获取src路径的正则
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";

	private static final String ROOT_URL = "http://qa-dsp2.suanshubang.com";

	/*
	 * public static void downloadPic(String html,String url, boolean flag) {
	 * 
	 * 
	 * if(flag) { if(url.contains("jpg")) { // 下载图片 DownLoadPic.download(url);
	 * return; } }
	 * 
	 * // 获得html文本内容 String HTML = html; try { HTML = DownLoadPic.getHTML(url); }
	 * catch (Exception e) { e.printStackTrace(); } if (null != HTML &&
	 * !"".equals(HTML)) {
	 * 
	 * // 获取图片标签 List<String> imgUrl = DownLoadPic.getImageUrl(HTML); // 获取图片src地址
	 * int begin = imgUrl.get(0).indexOf("/dsp"); int end =
	 * imgUrl.get(0).indexOf(" alt="); String imgSrc =
	 * imgUrl.get(0).substring(begin, end); imgSrc = ROOT_URL + imgSrc;
	 * 
	 * // 下载图片 DownLoadPic.download(imgSrc); } }
	 */

	public static void downloadPic(ClientWrapper client,String path) {
		try {
			FileOutputStream output = new FileOutputStream(new File(path));
			HttpEntity entity = client.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = in.read(buf)) != -1) {
					output.write(buf, 0, length);
				}
				output.flush();
				output.close();
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 获取HTML内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String getHTML(String url) throws Exception {
		URL uri = new URL(url);
		URLConnection connection = uri.openConnection();
		InputStream in = connection.getInputStream();
		byte[] buf = new byte[1024];
		int length = 0;
		StringBuffer sb = new StringBuffer();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			sb.append(new String(buf, ECODING));
		}
		in.close();
		return sb.toString();
	}

	/***
	 * 获取ImageUrl地址
	 * 
	 * @param HTML
	 * @return
	 */
	private static List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		while (matcher.find()) {
			System.out.println("getImageUrl:" + matcher.group());
			listImgUrl.add(matcher.group());
		}
		return listImgUrl;
	}

	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	private static List<String> getImageSrc(List<String> listImageUrl) {
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}

	/***
	 * 下载图片
	 * 
	 * @param listImgSrc
	 */
	private static void download(String url) {
		System.out.println(url);
		try {
			/*
			 * String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
			 */
			String imageName = "captcha.jpg";

			URL uri = new URL(url);
			InputStream in = uri.openStream();
			FileOutputStream fo = new FileOutputStream(new File(imageName));
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = in.read(buf, 0, buf.length)) != -1) {
				fo.write(buf, 0, length);
			}
			in.close();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}