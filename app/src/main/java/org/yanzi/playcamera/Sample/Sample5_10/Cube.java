package org.yanzi.playcamera.Sample.Sample5_10;
import static org.yanzi.playcamera.Sample.Sample5_10.Constant.*;

//������
public class Cube 
{
	//���ڻ��Ƹ��������ɫ����
	ColorRect cr;
	
	public Cube(MySurfaceView mv)
	{
		//�������ڻ��Ƹ��������ɫ����
		cr=new ColorRect(mv);
	}
	
	public void drawSelf()
	{
		//�ܻ���˼�룺ͨ���һ����ɫ������ת��λ��������ÿ�����λ��
		//�����������ÿ����
		
		//�����ֳ�
		MatrixState.pushMatrix();
		
		//����ǰС��
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, UNIT_SIZE);
		cr.drawSelf();		
		MatrixState.popMatrix();
		
		//���ƺ�С��
		MatrixState.pushMatrix();		
		MatrixState.translate(0, 0, -UNIT_SIZE);
		MatrixState.rotate(180, 0, 1, 0);
		cr.drawSelf();		
		MatrixState.popMatrix();
		
		//�����ϴ���
		MatrixState.pushMatrix();	
		MatrixState.translate(0,UNIT_SIZE,0);
		MatrixState.rotate(-90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();
		
		//�����´���
		MatrixState.pushMatrix();	
		MatrixState.translate(0,-UNIT_SIZE,0);
		MatrixState.rotate(90, 1, 0, 0);
		cr.drawSelf();
		MatrixState.popMatrix();
		
		//���������
		MatrixState.pushMatrix();	
		MatrixState.translate(UNIT_SIZE,0,0);
		MatrixState.rotate(-90, 1, 0, 0);
		MatrixState.rotate(90, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();
		
		//�����Ҵ���
		MatrixState.pushMatrix();				
		MatrixState.translate(-UNIT_SIZE,0,0);
		MatrixState.rotate(90, 1, 0, 0);
		MatrixState.rotate(-90, 0, 1, 0);
		cr.drawSelf();
		MatrixState.popMatrix();
		
		//�ָ��ֳ�
		MatrixState.popMatrix();
	}
	

}
