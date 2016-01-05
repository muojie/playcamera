package org.yanzi.playcamera.Sample.Sample5_5;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

//��ɫ������
public class Cube
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������  
    int maColorHandle; //������ɫ�������� 
    String mVertexShader;//������ɫ������ű�  
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer   mVertexBuffer;//���������ݻ���
	FloatBuffer   mColorBuffer;//������ɫ��ݻ���
    int vCount=0;  
    
    public Cube(MySurfaceView mv)
    {    	
    	//��ʼ�������������ɫ���
    	initVertexData();
    	//��ʼ��shader        
    	initShader(mv);
    }
    
    //��ʼ�������������ɫ��ݵķ���
    public void initVertexData()
    {
    	//���������ݵĳ�ʼ��================begin============================
        vCount=12*6; 
        
        float vertices[]=new float[]
        {
        	//ǰ��
        	0,0,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	0,0,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	0,0,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	0,0,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	//����
        	0,0,-Constant.UNIT_SIZE,        	
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,0,-Constant.UNIT_SIZE, 
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,0,-Constant.UNIT_SIZE, 
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,0,-Constant.UNIT_SIZE, 
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	//����
        	-Constant.UNIT_SIZE,0,0,      	
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,0,0,   
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,0,0,   
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,0,0,   
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	//����
        	Constant.UNIT_SIZE,0,0,   
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,0,0,   
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,0,0,   
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,0,0,  
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	//����
        	0,Constant.UNIT_SIZE,0,      
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,Constant.UNIT_SIZE,0,        	
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,Constant.UNIT_SIZE,0,       
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE, 	
        	0,Constant.UNIT_SIZE,0,      
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,  	
        	//����
        	0,-Constant.UNIT_SIZE,0,        	
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	0,-Constant.UNIT_SIZE,0,  
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,-Constant.UNIT_SIZE,0,   
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	0,-Constant.UNIT_SIZE,0,    
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        };
        
        //�������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥��������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ��ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������ݵĳ�ʼ��================end============================
       
    	//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
        float colors[]=new float[]{
        		//ǰ��        
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,0,0,
        		1,0,0,0,
        		//����
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,0,1,0,
        		0,0,1,0, 
        		//����
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,0,1,0,
        		1,0,1,0, 
        		//����
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		1,1,0,0,
        		1,1,0,0,
        		//����
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,0,0,
        		0,1,0,0,        		
        		//����
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//�м�Ϊ��ɫ
        		0,1,1,0,
        		0,1,1,0,
        };
        //����������ɫ��ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mColorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mColorBuffer.put(colors);//�򻺳����з��붥����ɫ���
        mColorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ��ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ��ݵĳ�ʼ��================end============================
    }
    //��ʼ��shader
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
    }
    
    public void drawSelf()
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES20.glUseProgram(mProgram);
         //�����ձ任������shader����
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         //Ϊ����ָ������λ�����
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //Ϊ����ָ��������ɫ���
         GLES20.glVertexAttribPointer  
         (
        		maColorHandle, 
         		4, 
         		GLES20.GL_FLOAT, 
         		false,
                4*4,   
                mColorBuffer
         );   
         //���?��λ���������
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maColorHandle);  
         //����������         
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vCount); 
    }
}
