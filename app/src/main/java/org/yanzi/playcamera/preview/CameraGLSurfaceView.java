package org.yanzi.playcamera.preview;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.yanzi.playcamera.CameraActivity;
import org.yanzi.playcamera.CameraInterface;
import org.yanzi.playcamera.R;
import org.yanzi.playcamera.util.ShaderUtil;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class CameraGLSurfaceView extends GLSurfaceView implements Renderer, SurfaceTexture.OnFrameAvailableListener {
	private static final String TAG = "yanzi";
	Context mContext;
	SurfaceTexture mSurface;
	int mTextureID = -1;

	//TODO：copy from DirectDrawer.java
	private FloatBuffer textureVerticesBuffer;
	private ShortBuffer drawListBuffer;
	private int mProgram;

	private int muMVPMatrixHandle;
	private int muSTMatrixHandle;
	private int maPositionHandle;
	private int maTextureHandle;

	private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

	private static final int FLOAT_SIZE_BYTES = 4;
	private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
	private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
	private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
	private static final int SAMPLE_WIDTH = 64;
	private static final int SAMPLE_HEIGHT = 32;

	private Rect mBlurRegion = new Rect(400, 500, 1150, 1000);

	private float[] mMVPMatrix = new float[16];
	private float[] mSTMatrix = new float[16];

	private FloatBuffer mTriangleVertices, mTextureBuffer, mVerticesBuffer, mOffsetVerticesBuffer;

	private final float[] mTriangleVerticesData = {
			// X, Y, Z, U, V
			-1.0f, -1.0f, 0, 0.f, 0.f,
			1.0f, -1.0f, 0, 1.f, 0.f,
			-1.0f, 1.0f, 0, 0.f, 1.f,
			1.0f, 1.0f, 0, 1.f, 1.f,
	};
	private final float[] mTextureData = {
			// U, V
			0.f, 0.f,
			1.f, 0.f,
			0.f, 1.f,
			1.f, 1.f,
	};
	private final float[] mVerticesData = {
			// X, Y, Z
			-1.0f, -1.0f, 0,
			1.0f, -1.0f, 0,
			-1.0f, 1.0f, 0,
			1.0f, 1.0f, 0,
	};

	static float textureVertices[] = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
	};

	//end

	private float[] mTcOffset = new float[50];

	private int mWidth, mHeight;

	// filter array, TODO: not support block frag, why?
	private int filters[] = {
			R.raw.normal_frag, R.raw.lomo_frag, R.raw.feather_frag, R.raw.old_frag, R.raw.lomo_frag,
			R.raw.softness_frag, R.raw.cartoon_frag, R.raw.invert_frag
	};

	private int mFilter = 0;
	private boolean mUpdateShader = true;

	public CameraGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceCreated...");
		mTextureID = createTextureID();
		mSurface = new SurfaceTexture(mTextureID);
		mSurface.setOnFrameAvailableListener(this);
		initBuffer();
		CameraInterface.getInstance().doOpenCamera(null);

	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceChanged...");
		GLES20.glViewport(0, 0, width, height);
		if(!CameraInterface.getInstance().isPreviewing()){
			CameraInterface.getInstance().doStartPreview(mSurface, 1.77f);
		}
	

	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onDrawFrame...");

		if (mUpdateShader) {
			initFilter();
			mUpdateShader = false;
		}

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		mSurface.updateTexImage();
		float[] mtx = new float[16];
		mSurface.getTransformMatrix(mtx);

		draw(mtx);
	}

	private void initFilter() {

		String fragmentShader = ShaderUtil.getShaderSource(CameraActivity.getInstance(), filters[mFilter]);
		String vertexShader = ShaderUtil.getShaderSource(CameraActivity.getInstance(), R.raw.camera_vertex);

		initShader(vertexShader, fragmentShader);
	}

	public void setFilter(int fragNum) {
		Log.d(TAG, "setFilter " + fragNum);
		mFilter = fragNum < filters.length ? fragNum : 0;
		mUpdateShader = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				setFilter(++mFilter);
				break;
		}
		return true;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CameraInterface.getInstance().doStopCamera();
	}
	private int createTextureID()
	{
		int[] texture = new int[1];

		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

		return texture[0];
	}
	public SurfaceTexture _getSurfaceTexture(){
		return mSurface;
	}
	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onFrameAvailable...");
		this.requestRender();
	}

	private void initBuffer()
	{

		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
		bb2.order(ByteOrder.nativeOrder());
		textureVerticesBuffer = bb2.asFloatBuffer();
		textureVerticesBuffer.put(textureVertices);
		textureVerticesBuffer.position(0);

		// initialize float buffer mTriangleVertices
		mTriangleVertices = ByteBuffer.allocateDirect(
				mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangleVertices.put(mTriangleVerticesData).position(0);

	}

	public void draw(float[] mtx)
	{
		GLES20.glUseProgram(mProgram);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

		//赋值给Attribute aPosition
		mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
				TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
		ShaderUtil.checkGlError("glVertexAttribPointer maPosition");
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		ShaderUtil.checkGlError("glEnableVertexAttribArray maPositionHandle");

		//赋值给Attribute aTextureCoord
		mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
		GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT, false,
				TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
		//TODO: 旋转
        /*
        textureVerticesBuffer.clear();
        textureVerticesBuffer.put(transformTextureCoordinates(textureVertices, mtx));
        textureVerticesBuffer.position(0);
        GLES20.glVertexAttribPointer(maTextureHandle, 2,
                GLES20.GL_FLOAT, false, 2*4, textureVerticesBuffer);
                */
		ShaderUtil.checkGlError("glVertexAttribPointer maTextureHandle");
		GLES20.glEnableVertexAttribArray(maTextureHandle);
		ShaderUtil.checkGlError("glEnableVertexAttribArray maTextureHandle");

		//TODO: 画三角形
		//GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		Matrix.setIdentityM(mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mtx, 0);

		GLES20.glBlendColor(1.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		ShaderUtil.checkGlError("glDrawArrays");

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(maPositionHandle);
		GLES20.glDisableVertexAttribArray(maTextureHandle);
	}
/* */
	public void draw1(float[] mtx)
	{
		IntBuffer framebuffer0 = IntBuffer.allocate(1);
		IntBuffer texture0 = IntBuffer.allocate(1);
		int texWidth = (int) (SAMPLE_WIDTH), texHeight = (int) (SAMPLE_HEIGHT);
		IntBuffer maxRenderbufferSize = IntBuffer.allocate(1);
		GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, maxRenderbufferSize);

		synchronized (this) {

			if (null == mTextureBuffer) {
				calBlurParas();
			}

			boolean sampleSuccess = initFBO(framebuffer0, texture0, texWidth, texHeight);
			if (sampleSuccess) {
				// RTT

				// 1. sample
				GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer0.get(0));

				GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

				GLES20.glUseProgram(mProgram);
				ShaderUtil.checkGlError("glUseProgram");

				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
				GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

				mOffsetVerticesBuffer.position(0);
				GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
						FLOAT_SIZE_BYTES * 3, mOffsetVerticesBuffer);
				ShaderUtil.checkGlError("glVertexAttribPointer maPosition");
				GLES20.glEnableVertexAttribArray(maPositionHandle);
				ShaderUtil.checkGlError("glEnableVertexAttribArray maPositionHandle");

				mTextureBuffer.position(0);
				GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT, false,
						FLOAT_SIZE_BYTES * 2, mTextureBuffer);
				ShaderUtil.checkGlError("glVertexAttribPointer maTextureHandle");
				GLES20.glEnableVertexAttribArray(maTextureHandle);
				ShaderUtil.checkGlError("glEnableVertexAttribArray maTextureHandle");

				Matrix.setIdentityM(mMVPMatrix, 0);
				GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
				GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);

				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
				ShaderUtil.checkGlError("glDrawArrays");
			}
		}
	}

	private void calBlurParas() {
		Log.d(TAG, "calBlurParas");

		mWidth = CameraGLSurfaceView.this.getWidth();
		mHeight = CameraGLSurfaceView.this.getHeight();

		if (mBlurRegion == null) {
			mBlurRegion = new Rect();

			mBlurRegion.left = 0;
			mBlurRegion.right = mWidth;
			mBlurRegion.top = 0;
			mBlurRegion.bottom = mHeight;
		}

		if (null == mTcOffset) {
			mTcOffset = new float[50];
		}

		float xpixel = 1f / SAMPLE_WIDTH;// SAMPLE_SIZE;//mWidth;//
		float ypixel = 1f / SAMPLE_HEIGHT;// SAMPLE_SIZE;//mHeight;//
		Log.d(TAG, "calBlurParas width:" + mWidth + " height:" + mHeight + " xpixel:" + xpixel + " ypixel:" + ypixel);

		for (int i = 0; i < 25; i++) {
			int row = i / 5;
			int col = i - row * 5;
			mTcOffset[2 * i] = (col - 2) * xpixel;
			mTcOffset[2 * i + 1] = (2 - row) * ypixel;
		}

		if (null == mTextureBuffer) {
			mTextureData[0] = ((float) mBlurRegion.left) / mWidth;
			mTextureData[4] = mTextureData[0];
			mTextureData[5] = (mHeight - (float) mBlurRegion.bottom) / mHeight;
			mTextureData[7] = mTextureData[5];
			mTextureData[1] = (mHeight - (float) mBlurRegion.top) / mHeight;
			mTextureData[3] = mTextureData[1];
			mTextureData[2] = ((float) mBlurRegion.right) / mWidth;
			mTextureData[6] = mTextureData[2];
			mTextureBuffer = ByteBuffer.allocateDirect(mTextureData.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mTextureBuffer.put(mTextureData).position(0);
		}
		if (null == mVerticesBuffer) {
			int centerx = mWidth / 2;
			int centery = mHeight / 2;
			mVerticesData[0] = ((float) mBlurRegion.left - centerx) / centerx;
			mVerticesData[6] = mVerticesData[0];
			mVerticesData[1] = (centery - (float) mBlurRegion.bottom) / centery;
			mVerticesData[4] = mVerticesData[1];
			mVerticesData[7] = (centery - (float) mBlurRegion.top) / centery;
			mVerticesData[10] = mVerticesData[7];
			mVerticesData[3] = ((float) mBlurRegion.right - centerx) / centerx;
			mVerticesData[9] = mVerticesData[3];
			mVerticesBuffer = ByteBuffer.allocateDirect(mVerticesData.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mVerticesBuffer.put(mVerticesData).position(0);
		}

		if (null == mOffsetVerticesBuffer) {
			mVerticesData[0] = -1f;
			mVerticesData[6] = mVerticesData[0];
			mVerticesData[1] = -1f;
			mVerticesData[4] = mVerticesData[1];
			mVerticesData[7] = 2 * (((float) SAMPLE_HEIGHT) / mHeight) - 1f;
			mVerticesData[10] = mVerticesData[7];
			mVerticesData[3] = 2 * (((float) SAMPLE_WIDTH) / mWidth) - 1f;
			mVerticesData[9] = mVerticesData[3];
			mOffsetVerticesBuffer = ByteBuffer.allocateDirect(mVerticesData.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mOffsetVerticesBuffer.put(mVerticesData).position(0);
		}
	}

	private void clearBlurParas() {
		mTcOffset = null;
		mTextureBuffer = null;
		mVerticesBuffer = null;
		mOffsetVerticesBuffer = null;
	}

	boolean initFBO(IntBuffer framebuffer, IntBuffer texture, int texWidth, int texHeight) {
		// generate the framebuffer, renderbuffer, and texture object names
		GLES20.glGenFramebuffers(1, framebuffer);
		GLES20.glGenTextures(1, texture);
		// bind texture and load the texture mip-level 0 texels are RGB565
		// no texels need to be specified as we are going to draw into the
		// texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.get(0));
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, texWidth, texHeight,
				0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

		// bind renderbuffer and create a 16-bit depth buffer
		// width and height of renderbuffer = width and height of the
		// texture
		// bind the framebuffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer.get(0));
		// specify texture as color attachment
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
				GLES20.GL_TEXTURE_2D, texture.get(0), 0);

		// check for framebuffer complete
		int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
		return status == GLES20.GL_FRAMEBUFFER_COMPLETE;
	}

	//初始化shader
	public void initShader(String vertexShader, String fragmentShader) {

		mProgram = ShaderUtil.createProgram(vertexShader, fragmentShader);

		//aPosition,aTextureCoord defined in file "vertext.sh", is attribute type.

		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		ShaderUtil.checkGlError("glGetAttribLocation aPosition");
		if (maPositionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aPosition");
		}
		maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
		ShaderUtil.checkGlError("glGetAttribLocation aTextureCoord");
		if (maTextureHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aTextureCoord");
		}

		//uMVPMatrix, uSTMatrix is defined in file "vertext.sh", is uniform mat4 type.

		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		ShaderUtil.checkGlError("glGetUniformLocation uMVPMatrix");
		if (muMVPMatrixHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uMVPMatrix");
		}
		muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
		ShaderUtil.checkGlError("glGetUniformLocation uSTMatrix");
		if (muSTMatrixHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uSTMatrix");
		}
	}
}
