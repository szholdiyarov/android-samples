package kz.telecom.happydrive.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import kz.telecom.happydrive.R;

/**
 * Created by szholdiyarov on 8/29/16.
 */
public class QrCodeUtil {
    private static final String QR_CODE_FILE_NAME = "qr.png";
    private static final String PATH_FILE_NAME = "/happy_drive_qr";
    private static final String ISO_STANDARD = "ISO-8859-1";
    private static final String ERROR_QR = "Произошла ошибка генерации QR кода.";
    private static final String ERROR_FILE_NOT_FOUND = "Файл не найден.";
    private static final String QR_DESCRIPTION = "Просканируйте QR код визитки";

    private static final int QR_CODE_DIMENSION = 550;
    private static Activity activity;

    public QrCodeUtil(Activity activity) {
        this.activity = activity;
    }

    private static void showErrorSnackBar(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(activity.getCurrentFocus(), text, Snackbar.LENGTH_LONG).show();
                activity.finish();
            }
        });
    }


    //Change the writers as per your need
    public static File generateQRCode(String data) {
            com.google.zxing.Writer writer = new QRCodeWriter();
            String finaldata = Uri.encode(data, ISO_STANDARD);
            Bitmap mBitmap = null;
            try {

                BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, QR_CODE_DIMENSION, QR_CODE_DIMENSION);
                mBitmap = Bitmap.createBitmap(QR_CODE_DIMENSION, QR_CODE_DIMENSION, Bitmap.Config.ARGB_8888);

                for (int i = 0; i < QR_CODE_DIMENSION; i++) {
                    for (int j = 0; j < QR_CODE_DIMENSION; j++) {
                        mBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                saveFile(mBitmap);
            } catch (WriterException e) {
                showErrorSnackBar(ERROR_QR);
            }
            return getSavedQrCode();

    }

    private static File getSavedQrCode() {
        return new File(Environment.getExternalStorageDirectory() + PATH_FILE_NAME, QR_CODE_FILE_NAME);
    }

    private static void saveFile(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + PATH_FILE_NAME);
        myDir.mkdir();
        File file = new File(myDir, QR_CODE_FILE_NAME);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) { // 
            showErrorSnackBar(ERROR_FILE_NOT_FOUND);
        }
    }

    private void decodeQR() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setPrompt(QR_DESCRIPTION);
        integrator.initiateScan();
    }
}