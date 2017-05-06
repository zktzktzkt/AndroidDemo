package demo.zkttestdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_LJ/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); 
			else if (file.isDirectory())
				deleteDir(); 
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static final float DISPLAY_WIDTH = 200;
	public static final float DISPLAY_HEIGHT = 200;
	/**
	 * 从path中获取图片信息
	 * @param path
	 * @return
	 */
	public static Bitmap decodeBitmap(String path){
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
		//获取比例大小
		int wRatio = (int) Math.ceil(op.outWidth/DISPLAY_WIDTH);
		int hRatio = (int) Math.ceil(op.outHeight/DISPLAY_HEIGHT);
		//如果超出指定大小，则缩小相应的比例
		if(wRatio > 1 && hRatio > 1){
			if(wRatio > hRatio){
				op.inSampleSize = wRatio;
			}else{
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}

	public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
													   int reqWidth, int reqHeight) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
		byte[] data = baos.toByteArray();

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	/**
	 * 计算InSampleSize
	 * 宽的压缩比和高的压缩比的较小值  取接近的2的次幂的值
	 * 比如宽的压缩比是3 高的压缩比是5 取较小值3  而InSampleSize必须是2的次幂，取接近的2的次幂4
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			int ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
			// inSampleSize只能是2的次幂  将ratio就近取2的次幂的值
			if (ratio < 3)
				inSampleSize = ratio;
			else if (ratio < 6.5)
				inSampleSize = 4;
			else if (ratio < 8)
				inSampleSize = 8;
			else
				inSampleSize = ratio;
		}

		return inSampleSize;
	}

}
