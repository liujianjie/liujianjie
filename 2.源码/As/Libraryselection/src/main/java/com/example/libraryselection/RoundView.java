package com.example.libraryselection;  
  
import android.content.Context;   
import android.graphics.Bitmap;   
import android.graphics.Canvas;  
import android.graphics.Matrix;  
import android.graphics.Paint;  
import android.graphics.PorterDuff;  
import android.graphics.PorterDuffXfermode;  
import android.graphics.Rect;  
import android.graphics.drawable.BitmapDrawable;  
import android.graphics.drawable.Drawable;  
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;  
   
  
/** 
 * Created by mummyding on 15-8-7. 
 */  
public class RoundView extends ImageView {  
    private Paint paint = new Paint();  
  
    public RoundView(Context context) {  
        super(context);  
    }  
  
    public RoundView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public RoundView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    //��ͷ�񰴱�������  
    private Bitmap scaleBitmap(Bitmap bitmap){  
        int width = getWidth();  
        //һ��Ҫǿת��float ��Ȼ�п�����Ϊ���Ȳ��� ���� scaleΪ0 �Ĵ���  
        float scale = (float)width/(float)bitmap.getWidth();  
        Matrix matrix = new Matrix();  
        matrix.postScale(scale, scale);  
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
  
    }  
    //��ԭʼͼ��ü���������  
    private Bitmap dealRawBitmap(Bitmap bitmap){  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //��ȡ���  
        int minWidth = width > height ?  height:width ;  
        //���������εķ�Χ  
        int leftTopX = (width - minWidth)/2;  
        int leftTopY = (height - minWidth)/2;  
        //�ü���������  
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,leftTopX,leftTopY,minWidth,minWidth,null,false);  
        return  scaleBitmap(newBitmap);  
    }  
    @Override
    public void invalidate() {
    	// TODO Auto-generated method stub
    	super.invalidate();
    }
    public Drawable mybitmap;
    @Override  
    protected void onDraw(Canvas canvas) {  
    	Log.i("library", "RoundView draw");
    	// ���û���ⲿ��bitmap
		Drawable drawable = mybitmap;
		if(drawable == null){
			drawable = getDrawable();
		}
		if (null != drawable) {  
            Bitmap rawBitmap =((BitmapDrawable)drawable).getBitmap();  
  
            //����Bitmap ת��������  
            Bitmap newBitmap = dealRawBitmap(rawBitmap);  
            //��newBitmap ת����Բ��  
            Bitmap circleBitmap = toRoundCorner(newBitmap, 14);  
  
            final Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());  
            paint.reset();  
            //���Ƶ�������  
            canvas.drawBitmap(circleBitmap, rect, rect, paint);  
        } else {  
            super.onDraw(canvas);  
        }  
    }  
  
    private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
        //ָ��Ϊ ARGB_4444 ���Լ�СͼƬ��С  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);  
        Canvas canvas = new Canvas(output);  
  
        final int color = 0xff424242;  
        final Rect rect = new Rect(0, 0,bitmap.getWidth(), bitmap.getHeight());  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        int x = bitmap.getWidth();  
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
    }  
}  