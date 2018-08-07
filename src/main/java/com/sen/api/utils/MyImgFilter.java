package com.sen.api.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MyImgFilter {
	BufferedImage image;
	private int iw, ih;
	private int[] pixels;

	private static final Tesseract TESSREACT = new Tesseract();
	static {
		String path = System.getProperty("user.dir");
		path = path.substring(0, path.lastIndexOf("\\") + 1);
		path += "tessdata";
		
		TESSREACT.setLanguage("eng");
		TESSREACT.setDatapath(path);
	}

	public MyImgFilter(BufferedImage image) {
		this.image = image;
		iw = image.getWidth();
		ih = image.getHeight();
		pixels = new int[iw * ih];
	}

	public BufferedImage changeGrey() {
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
// 设定二值化的域值，默认值为100
		int grey = 100;
// 对图像进行二值化处理，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int red, green, blue;
			int alpha = cm.getAlpha(pixels[i]);
			if (cm.getRed(pixels[i]) > grey) {
				red = 255;
			} else {
				red = 0;
			}
			if (cm.getGreen(pixels[i]) > grey) {
				green = 255;
			} else {
				green = 0;
			}
			if (cm.getBlue(pixels[i]) > grey) {
				blue = 255;
			} else {
				blue = 0;
			}
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue; // 通过移位重新构成某一点像素的RGB值
		}
// 将数组中的象素产生一个图像
		Image tempImg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
		image = new BufferedImage(tempImg.getWidth(null), tempImg.getHeight(null), BufferedImage.TYPE_INT_BGR);
		image.createGraphics().drawImage(tempImg, 0, 0, null);
		return image;
	}

	public BufferedImage getMedian() {
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 对图像进行中值滤波，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < ih - 1; i++) {
			for (int j = 1; j < iw - 1; j++) {
				int red, green, blue;
				int alpha = cm.getAlpha(pixels[i * iw + j]);
				// int red2 = cm.getRed(pixels[(i - 1) * iw + j]);
				int red4 = cm.getRed(pixels[i * iw + j - 1]);
				int red5 = cm.getRed(pixels[i * iw + j]);
				int red6 = cm.getRed(pixels[i * iw + j + 1]);
				// int red8 = cm.getRed(pixels[(i + 1) * iw + j]);
				// 水平方向进行中值滤波
				if (red4 >= red5) {
					if (red5 >= red6) {
						red = red5;
					} else {
						if (red4 >= red6) {
							red = red6;
						} else {
							red = red4;
						}
					}
				} else {
					if (red4 > red6) {
						red = red4;
					} else {
						if (red5 > red6) {
							red = red6;
						} else {
							red = red5;
						}
					}
				}
				int green4 = cm.getGreen(pixels[i * iw + j - 1]);
				int green5 = cm.getGreen(pixels[i * iw + j]);
				int green6 = cm.getGreen(pixels[i * iw + j + 1]);
				// 水平方向进行中值滤波
				if (green4 >= green5) {
					if (green5 >= green6) {
						green = green5;
					} else {
						if (green4 >= green6) {
							green = green6;
						} else {
							green = green4;
						}
					}
				} else {
					if (green4 > green6) {
						green = green4;
					} else {
						if (green5 > green6) {
							green = green6;
						} else {
							green = green5;
						}
					}
				}
				// int blue2 = cm.getBlue(pixels[(i - 1) * iw + j]);
				int blue4 = cm.getBlue(pixels[i * iw + j - 1]);
				int blue5 = cm.getBlue(pixels[i * iw + j]);
				int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
				// int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);
				// 水平方向进行中值滤波
				if (blue4 >= blue5) {
					if (blue5 >= blue6) {
						blue = blue5;
					} else {
						if (blue4 >= blue6) {
							blue = blue6;
						} else {
							blue = blue4;
						}
					}
				} else {
					if (blue4 > blue6) {
						blue = blue4;
					} else {
						if (blue5 > blue6) {
							blue = blue6;
						} else {
							blue = blue5;
						}
					}
				}
				pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8 | blue;
			}
		}
		// 将数组中的象素产生一个图像
		Image tempImg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
		image = new BufferedImage(tempImg.getWidth(null), tempImg.getHeight(null), BufferedImage.TYPE_INT_BGR);
		image.createGraphics().drawImage(tempImg, 0, 0, null);
		return image;
	}

	public BufferedImage getGrey() {
		ColorConvertOp ccp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return image = ccp.filter(image, null);
	}

	// Brighten using a linear formula that increases all color values
	public BufferedImage getBrighten() {
		RescaleOp rop = new RescaleOp(1.25f, 0, null);
		return image = rop.filter(image, null);
	}

	// Blur by "convolving" the image with a matrix
	public BufferedImage getBlur() {
		float[] data = { .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, };
		ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data));
		return image = cop.filter(image, null);
	}

	// Sharpen by using a different matrix
	public BufferedImage getSharpen() {
		float[] data = { 0.0f, -0.75f, 0.0f, -0.75f, 4.0f, -0.75f, 0.0f, -0.75f, 0.0f };
		ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data));
		return image = cop.filter(image, null);
	}

	// 11) Rotate the image 180 degrees about its center point
	public BufferedImage getRotate() {
		AffineTransformOp atop = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.PI, image.getWidth() / 2, image.getHeight() / 2),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return image = atop.filter(image, null);
	}

	public BufferedImage getProcessedImg() {
		return image;
	}

	public static void savePic(String path) throws IOException {
		FileInputStream fin = new FileInputStream(path);
		BufferedImage bi = ImageIO.read(fin);

		if(bi== null) {
			ReportUtil.log("Download identify pic failed!Please check the url");
			throw  new IOException("Image file input stream error!");
		}
		MyImgFilter flt = new MyImgFilter(bi);
		flt.changeGrey();
		flt.getGrey();
		flt.getBrighten();
		bi = flt.getProcessedImg();
		// String name = path.substring(0, path.lastIndexOf(".")) + "_result";
		File file = new File("captcha_result.jpg");
		ImageIO.write(bi, "jpg", file);
	}

	public static String doOCR(String path) {
		File file = new File(path);
		String result = "";
		try {
			result = TESSREACT.doOCR(file);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		} finally {
			return result;
		}

	}

	public static boolean checkImageStr(String code) {
		if(StringUtil.isEmpty(code)) {
			return false;
		}
		
		if (code.length()!= 4) {
			return false;
		}
		
		return true;
	}

	
	public static String formatImageStr(String code) {
		if (code.equals("")) {
			return "";
		}
	    StringBuffer buffer = new StringBuffer();
	    
		for(int i=0; i< code.length(); i++) {
			char c = code.charAt(i);
			if(Character.isLetterOrDigit(c)) {
				buffer.append(c);
			}
		}
		return buffer.toString();
		
	}
	public static void main(String[] args) throws IOException {
		System.out.println("");
		/*
		 * String url = "http://qa-dsp2.suanshubang.com/dsp-admin/captcha.jpg";
		 * ClientWrapper client = null;
		 * 
		 * while (true) { try { client = MyHttpClient.sendHttpGet(url);
		 * 
		 * } catch (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } System.out.println(client.getHttpClient());
		 * System.out.println(client.getResponseBody());
		 * System.out.println("cookie store:" + MyHttpClient.cookieStore.getCookies());
		 * DownLoadPic.downloadPic(client); String path =
		 * "E:\\Java\\api_autotest\\captcha.jpg";
		 * 
		 * savePic(path);
		 * 
		 * String pathToOcr = "E:\\Java\\api_autotest\\captcha_result.jpg"; String
		 * result = doOCR(pathToOcr);
		 * 
		 * result = result.replaceAll(" ", "").replaceAll("\r|\n", "");
		 * System.out.println(result); if (!checkImageStr(result)) {
		 * MyHttpClient.resetClient(); continue; } else {
		 * 
		 * try { boolean success = MyHttpClient.login(client.getHttpClient(),"agent",
		 * "agent", result); System.out.println("login :" + success);
		 * System.out.println("cookie store:" + MyHttpClient.cookieStore.getCookies());
		 * if (success) { Map<String, String> actionMap = new HashMap<String, String>();
		 * 
		 * String jsonBody =
		 * "{\"queryType\":1,\"startTime\":1531411200000,\"endTime\":1532016000000,\"pageSize\":10,\"pageNum\":1}";
		 * actionMap.put("http://qa-dsp2.suanshubang.com/dsp-advert/campaigns/list",
		 * jsonBody); MyHttpClient.testApiOne(client.getHttpClient(), actionMap); } else
		 * { MyHttpClient.resetClient(); continue; } } catch (Exception e) {
		 * e.printStackTrace(); } break; } }
		 */
	}
}