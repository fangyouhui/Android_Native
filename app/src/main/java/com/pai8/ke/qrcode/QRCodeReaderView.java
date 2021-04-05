package com.pai8.ke.qrcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.pai8.ke.qrcode.zxing.camara.CameraManager;


/**
 * @Description: QR 二维码识别，摄像头控制类
 */
public class QRCodeReaderView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    public interface OnQRCodeReadListener {

        public void onQRCodeRead(String text, PointF[] points);

        public void cameraNotFound();

        public void QRCodeNotFoundOnCamImage();
    }

    private OnQRCodeReadListener mOnQRCodeReadListener;

    private static final String TAG = QRCodeReaderView.class.getName();

    private QRCodeReader mQRCodeReader = null;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private SurfaceHolder mHolder;
    private CameraManager mCameraManager;
    private static boolean isCamaraOpen = false;
    private static boolean isCamaraOpening = false;
    private static int openDriverCount = 0;
    private View mydecoderview = null;
    private SurfaceHolder lastHolder = null;

    public QRCodeReaderView(Context context) {
        super(context);
        init();
    }

    public QRCodeReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setDecoderView(Activity context, View mydecoderview) {
        this.mydecoderview = mydecoderview;
    }

    public void setOnQRCodeReadListener(
            OnQRCodeReadListener onQRCodeReadListener) {
        mOnQRCodeReadListener = onQRCodeReadListener;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    private void init() {
        isCamaraOpen = false;
        isCamaraOpening = false;
        if (QRCodeTools.checkCameraHardware(getContext())) {
            mCameraManager = new CameraManager(getContext());
            mHolder = this.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } else {
            Log.e(TAG, "Error: Camera not found");
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.cameraNotFound();
            }
        }
    }

    private void openCamaraDriver(final SurfaceHolder holder) {
        System.out.println("--------openCamaraDriver start");
        openDriverCount++;
        lastHolder = holder;
        if (isCamaraOpen || isCamaraOpening) {
            return;
        }
        isCamaraOpening = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isCamaraOpen = mCameraManager.openDriver(holder,
                            QRCodeReaderView.this.getWidth(), QRCodeReaderView.this.getHeight());
                } catch (Exception e) {
                    // todo auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("--------opencamaradriver=" + isCamaraOpen);
                if (!isCamaraOpen) {
                    closeCameraDriver();
                }
                hander.sendEmptyMessage(isCamaraOpen ? 1 : 0);
                openDriverCount--;
                isCamaraOpening = false;
                if (openDriverCount > 0 && !isCamaraOpen) {
                    openCamaraDriver(lastHolder);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mCameraManager.getPreviewSize() != null) {
                    mPreviewWidth = mCameraManager.getPreviewSize().x;
                    mPreviewHeight = mCameraManager.getPreviewSize().y;
                }
                if (mCameraManager.isPreviewing()) {
                    try {
                        mCameraManager.stopPreview();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
                if (mCameraManager.getCamera() != null) {
                    mCameraManager.getCamera().setDisplayOrientation(90); // Portrait mode
                }
                mCameraManager.startPreview(QRCodeReaderView.this);
                if (mQRCodeReader == null) {
                    mQRCodeReader = new QRCodeReader();
                }
                if (mydecoderview != null) {
                    mydecoderview.invalidate();
                }
            }
        }
    };

    /****************************************************
     * SurfaceHolder.Callback,Camera.PreviewCallback
     ****************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamaraDriver(holder);
        if (mQRCodeReader == null) {
            mQRCodeReader = new QRCodeReader();
        }
        mCameraManager.startPreview(QRCodeReaderView.this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCameraDriver();
    }

    public void closeCameraDriver() {
        System.out.println("--------closeCameraDriver");
        if (mCameraManager != null) {
            hander.removeCallbacksAndMessages(null);
            mCameraManager.closeDriver();
            isCamaraOpen = false;
        }
    }

    // Called when camera take a frame
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        PlanarYUVLuminanceSource source = mCameraManager.buildLuminanceSource(
                data, mPreviewWidth, mPreviewHeight);

        HybridBinarizer hybBin = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(hybBin);

        try {
            Result result = mQRCodeReader.decode(bitmap);

            // Notify We're found a QRCode
            if (mOnQRCodeReadListener != null) {
                // Transform resultPoints to View coordinates
                PointF[] transformedPoints = transformToViewCoordinates(result
                        .getResultPoints());
                mOnQRCodeReadListener.onQRCodeRead(result.getText(),
                        transformedPoints);
            }

        } catch (ChecksumException e) {
            Log.d(TAG, "ChecksumException");
            e.printStackTrace();
        } catch (NotFoundException e) {
            // Notify QR not found
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.QRCodeNotFoundOnCamImage();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mQRCodeReader.reset();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.d(TAG, "surfaceChanged");

        if (mHolder == null || mHolder.getSurface() == null) {
            Log.e(TAG, "Error: preview surface does not exist");
            return;
        }

        if (mCameraManager == null || mCameraManager.getCamera() == null || !isCamaraOpen) {
            return;
        }

        if (mCameraManager.getPreviewSize() != null) {
            mPreviewWidth = mCameraManager.getPreviewSize().x;
            mPreviewHeight = mCameraManager.getPreviewSize().y;
        }

        mCameraManager.stopPreview();
        mCameraManager.getCamera().setDisplayOrientation(90); // Portrait mode

        mCameraManager.startPreview(QRCodeReaderView.this);
    }

    private PointF[] transformToViewCoordinates(ResultPoint[] resultPoints) {
        PointF[] transformedPoints = new PointF[resultPoints.length];
        int index = 0;
        if (/*resultPoints != null && */mCameraManager.getPreviewSize() != null) {
            float previewX = mCameraManager.getPreviewSize().x;
            float previewY = mCameraManager.getPreviewSize().y;
            float scaleX = this.getWidth() / previewY;
            float scaleY = this.getHeight() / previewX;

            for (ResultPoint point : resultPoints) {
                PointF tmppoint = new PointF(
                        (previewY - point.getY()) * scaleX, point.getX()
                        * scaleY);
                transformedPoints[index] = tmppoint;
                index++;
            }
        }
        return transformedPoints;
    }

    public void startPreview() {
        if (mCameraManager != null) {
            if (!mCameraManager.isPreviewing()) {
                mCameraManager.startPreview(this);
            }
        }
    }

    /**
     * @return void
     * @Title: setLightEnable
     * @param:
     * @Description: 打开/关闭闪光灯
     */
    public void setLightEnable(boolean enable) {
        if (mCameraManager != null) {
            Camera camera = mCameraManager.getCamera();
            if (camera != null) {
                Parameters p = camera.getParameters();
                if (p != null) {
                    if (enable) {
                        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(p);
                    } else {
                        p.setFlashMode(Parameters.FLASH_MODE_OFF);
                        camera.setParameters(p);
                    }
                }
            }
        }
    }

    public void stopPreview() {
        if (mCameraManager != null) {
            if (mCameraManager.isPreviewing()) {
                mCameraManager.stopPreview();
            }
        }
    }
}