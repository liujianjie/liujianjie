package com.example.libraryselection.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by xpf on 2017/6/24 :) Function:实现高斯模糊工具类
 */

public class BitmapUtil {
	
	// 根据url读取本地图片返回bitmap
	public static Bitmap getLocalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 2;
			Bitmap btp = BitmapFactory.decodeStream(fis, null, options);
			return btp; /// 把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 读取图片方法
	// 读取资源图片
	public static Bitmap readBitMaps(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	// 读取文件夹图片，可能会内存泄漏
	public static Bitmap readBitMapsWithOutSetting(Context context, int resId) {
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}
	
	// 图片缩放比例(即模糊度)
	private static final float BITMAP_SCALE = 0.4f;

	/**
	 * @param context
	 *            上下文对象
	 * @param image
	 *            需要模糊的图片
	 * @return 模糊处理后的Bitmap
	 */
	// public static Bitmap blurBitmap(Context context, Bitmap image, float
	// blurRadius) {
	// // 计算图片缩小后的长宽
	// int width = Math.round(image.getWidth() * BITMAP_SCALE);
	// int height = Math.round(image.getHeight() * BITMAP_SCALE);
	//
	// // 将缩小后的图片做为预渲染的图片
	// Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height,
	// false);
	// // 创建一张渲染后的输出图片
	// Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
	//
	// // 创建RenderScript内核对象
	// RenderScript rs = RenderScript.create(context);
	// // 创建一个模糊效果的RenderScript的工具对象
	// ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,
	// Element.U8_4(rs));
	//
	// // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
	// // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
	// Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
	// Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
	//
	// // 设置渲染的模糊程度, 25f是最大模糊度
	// blurScript.setRadius(blurRadius);
	// // 设置blurScript对象的输入内存
	// blurScript.setInput(tmpIn);
	// // 将输出数据保存到输出内存中
	// blurScript.forEach(tmpOut);
	//
	// // 将数据填充到Allocation中
	// tmpOut.copyTo(outputBitmap);
	//
	// return outputBitmap;
	// }
	public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
		Bitmap bitmap;
		if (canReuseInBitmap) {
			bitmap = sentBitmap;
		} else {
			bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		}

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}

}