package org.yanzi.playcamera.Sample.Sample5_9;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

//��ɫ��״��
public class Belt {
	int mProgram;// �Զ�����Ⱦ������ɫ������id
	int muMVPMatrixHandle;// �ܱ任��������
	int maPositionHandle; // ����λ����������
	int maColorHandle; // ������ɫ��������
	String mVertexShader;// ������ɫ������ű�
	String mFragmentShader;// ƬԪ��ɫ������ű�

	FloatBuffer mVertexBuffer;// ���������ݻ���
	FloatBuffer mColorBuffer;// ������ɫ��ݻ���
	private ByteBuffer mIndexBuffer;// ���㹹��������ݻ���
	int vCount = 0;
	int iCount = 0;

	public Belt(MySurfaceView mv) {
		// ��ʼ�������������ɫ���
		initVertexData();
		// ��ʼ��shader
		initShader(mv);
	}

	// ��ʼ�������������ɫ��ݵķ���
	public void initVertexData() {
		// ���������ݵĳ�ʼ��================begin============================
		int n = 6;
		vCount = 2 * (n + 1);
		float angdegBegin = -90;
		float angdegEnd = 90;
		float angdegSpan = (angdegEnd - angdegBegin) / n;

		float[] vertices = new float[vCount * 3];// ������
		// �����ݳ�ʼ��
		int count = 0;
		for (float angdeg = angdegBegin; angdeg <= angdegEnd; angdeg += angdegSpan) {
			double angrad = Math.toRadians(angdeg);// ��ǰ����
			// ��ǰ��
			vertices[count++] = (float) (-0.6f * Constant.UNIT_SIZE * Math
					.sin(angrad));// �������
			vertices[count++] = (float) (0.6f * Constant.UNIT_SIZE * Math
					.cos(angrad));
			vertices[count++] = 0;
			// ��ǰ��
			vertices[count++] = (float) (-Constant.UNIT_SIZE * Math.sin(angrad));// �������
			vertices[count++] = (float) (Constant.UNIT_SIZE * Math.cos(angrad));
			vertices[count++] = 0;
		}
		// �������������ݻ���
		// vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥��������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ��ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ���������ݵĳ�ʼ��================end============================

		// ����ι���������ݳ�ʼ��==========begin==========================
		iCount = vCount;
		byte indices[] = new byte[iCount];
		for (int i = 0; i < iCount; i++) {
			indices[i] = (byte) i;
		}

		// ��������ι���������ݻ���
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);// �򻺳����з�������ι����������
		mIndexBuffer.position(0);// ���û�������ʼλ��
		// ����ι���������ݳ�ʼ��==========end==============================

		// ������ɫ��ݵĳ�ʼ��================begin============================
		// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		count = 0;
		float colors[] = new float[vCount * 4];
		for(int i=0; i<colors.length; i+=8){
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 0;
        	
        	colors[count++] = 0; 
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 0;
        }

		// ����������ɫ��ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mColorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mColorBuffer.put(colors);// �򻺳����з��붥����ɫ���
		mColorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ��ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ������ɫ��ݵĳ�ʼ��================end============================
	}

	// ��ʼ��shader
	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�����ɫ��������id
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf() {
		// �ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(mProgram);
		// �����ձ任������shader����
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		// Ϊ����ָ������λ�����
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		// Ϊ����ָ��������ɫ���
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
				4 * 4, mColorBuffer);
		// ���?��λ���������
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);
		// ����ͼ��
		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, iCount,
				GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
}
