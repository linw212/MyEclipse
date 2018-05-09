package com.profound.common.kit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 图片验证码生成类
 * 
 * @author Administrator
 *
 */
public final class RandomCodeKit {

	/**
	 * 随机取得一个字体
	 * 
	 * @param random
	 *            随机数
	 * @return Font 返回一个新字体
	 */
	private synchronized Font getsFont(Random random) {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 20);
	}

	/**
	 * 返回一个随机颜色
	 * 
	 * @param fc
	 *            随机数
	 * @param bc
	 *            随机数
	 * @param random
	 *            随机数
	 * @return Color 返回一个新颜色
	 */
	synchronized Color getRandColor(int fc, int bc, Random random) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 绘制验证码图片
	 * 
	 * @author zuoyf
	 * @param source
	 *            图片上要显示的文字
	 * @return
	 */
	public synchronized BufferedImage getImageText(String source) {
		int width = 72, height = 30;// 设置图片大小
		return getImageText(source, width, height);
	}

	/**
	 * 绘制验证码图片
	 * 
	 * @author zuoyf
	 * @param source
	 *            图片上要显示的文字
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 * @return Image object Buffer stream
	 */
	public synchronized BufferedImage getImageText(String source, int width,
			int height) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250, random));
		g.fillRect(0, 0, width, height);// 设定边框
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
		// 产生随机线
		for (int i = 0; i < 128; i++) {
			g.setColor(getRandColor(130, 150, random));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 产生随机点
		int len = source.length();
		for (int i = 0; i < len; i++) {
			g.setFont(getsFont(random));
			g.setColor(new Color(random.nextInt(101), random.nextInt(111),
					random.nextInt(121)));
			g.translate(random.nextInt(4), random.nextInt(4));
			g.drawString(String.valueOf(source.charAt(i)), 14 * i, 16);

		}
		g.dispose();
		return image;
	}

	/**
	 * 生成随机数图片
	 * 
	 * @param request
	 * @param response
	 * 
	 * 
	 */
	public synchronized void getRandcode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.setProperty("java.awt.headless", "true");
		HttpSession session = request.getSession();
		int width = 100, height = 30;// 设置图片大小
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250, random));

		g.fillRect(0, 0, width, height);// 设定边框

		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));

		// 产生随机线
		for (int i = 0; i < 11; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 产生随机点
		g.setColor(getRandColor(130, 150, random));
		// 产生5个随机数
		String sRand = "";
		for (int i = 0; i < 6; i++) {
			g.setFont(getsFont(random));
			g.setColor(new Color(random.nextInt(101), random.nextInt(111),
					random.nextInt(121)));
			String rand = String.valueOf(getRandomString(random.nextInt(62)));
			// String rand =
			// String.valueOf(getRandomString(random.nextInt(10)));
			sRand += rand;
			g.translate(random.nextInt(3), random.nextInt(3));
			g.drawString(rand, 14 * i, 16);
		}
		session.removeAttribute("Rand");
		session.setAttribute("Rand", sRand);
		g.dispose();
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 * 获取随机数
	 * 
	 * @param length
	 * @return
	 */
	public synchronized String getRandomString(int length) {
		length = length > 0 ? length : 4;
		char[] randchars = new char[length];

		String randstring = "23456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghigkmnpqrstuvwxyz";
		Random rd = new Random();

		for (int i = 0; i < length; i++) {
			randchars[i] = randstring.charAt(rd.nextInt(57));
		}
		return new String(randchars);
	}

}
