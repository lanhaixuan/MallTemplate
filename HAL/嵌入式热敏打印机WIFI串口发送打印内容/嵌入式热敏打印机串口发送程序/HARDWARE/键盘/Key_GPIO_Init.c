#include "Key_GPIO_Init.h"
#include "sys.h"
#include "delay.h"

extern uint16_t Key_1;   //������������ ���ڼ�����ֵ ʵ�ʺ�����û���õ�
extern uint16_t Key_2;
extern uint16_t Key_3;
extern uint16_t Key_4;
extern uint16_t Key_5;
extern uint16_t Key_6;
extern uint16_t Key_7;
extern uint16_t Key_8;

void Key_GPIO_Init()  //����GPIO�̶˿ڳ�ʼ������
{
	GPIO_InitTypeDef GPIO_InitStructure;  //�˿ڽ�ṹ��
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOG,ENABLE);  //�����˿�G��ʱ��
	
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPD;  //����Ϊ�������� ��û������ʱΪ�͵�ƽ  ��������Ϊû������ʱ���ָߵ�ƽ
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_5 | GPIO_Pin_6 | GPIO_Pin_7 | GPIO_Pin_8;  //����G�˿ڵ�5 6 7 8 ��Ϊ����˿�
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;  //�ٶ�Ϊ50MHz
	GPIO_Init(GPIOG,&GPIO_InitStructure);  //ʹ�ܶ˿�����ĳ�ʼ��
	
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;  //����Ϊͨ���������ģʽ
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_1 | GPIO_Pin_2 | GPIO_Pin_3 | GPIO_Pin_4;  //����G�˿�1 2 3 4 ��Ϊ����˿�
	GPIO_Init(GPIOG,&GPIO_InitStructure);  //ʹ�ܶ˿�����ĳ�ʼ��
	
	GPIO_ResetBits(GPIOG,GPIO_Pin_1);  //����˿��õ�
	GPIO_ResetBits(GPIOG,GPIO_Pin_2);
	GPIO_ResetBits(GPIOG,GPIO_Pin_3);
	GPIO_ResetBits(GPIOG,GPIO_Pin_4);
	GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5);   //��ȡ����˿ڵ�ֵ
  GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6);
	GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7);
	GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8);
}

int Read_Key()  //?����??��?��3?��??��o����y
{
	/************************************************
	 ����һ�ż�ֵ ��G4���ø� 123���õ� �ж���һ���������� ������Ӧ����ֵ
	************************************************/
	
  GPIO_SetBits(GPIOG,GPIO_Pin_4);
	GPIO_ResetBits(GPIOG,GPIO_Pin_1);
	GPIO_ResetBits(GPIOG,GPIO_Pin_2);
	GPIO_ResetBits(GPIOG,GPIO_Pin_3);
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_4) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
		{
			return 1;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_4) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
		{
			return 2;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_4) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
		{
			return 3;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_4) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
		{
			return 4;
		}
	}
	
	/************************************************
	���ڶ��ż�ֵ ��G3���ø� 124���õ� �ж���һ���������� ������Ӧ����ֵ
	************************************************/
	GPIO_SetBits(GPIOG,GPIO_Pin_3);
	GPIO_ResetBits(GPIOG,GPIO_Pin_1);
	GPIO_ResetBits(GPIOG,GPIO_Pin_2);
	GPIO_ResetBits(GPIOG,GPIO_Pin_4);
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_3) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
		{
			return 5;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_3) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
		{
			return 6;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_3) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
		{
			return 7;
		}
	}
	
	if((GPIO_ReadOutputDataBit(GPIOG,GPIO_Pin_3) & GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5)) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
		{
			return 8;
		}
	}
	
		/************************************************
	 �������ż�ֵ ��G2���ø� 134���õ� �ж���һ���������� ������Ӧ����ֵ
	************************************************/
	GPIO_SetBits(GPIOG,GPIO_Pin_2);
	GPIO_ResetBits(GPIOG,GPIO_Pin_1);
	GPIO_ResetBits(GPIOG,GPIO_Pin_3);
	GPIO_ResetBits(GPIOG,GPIO_Pin_4);
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
		{
			return 9;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
		{
			return 0;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
		{
			return 10;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
		{
			return 11;
		}
	}
	
	/************************************************
	 �������ż�ֵ ��G1���ø� 234���õ� �ж���һ���������� ������Ӧ����ֵ
	************************************************/
	GPIO_SetBits(GPIOG,GPIO_Pin_1);
	GPIO_ResetBits(GPIOG,GPIO_Pin_2);
	GPIO_ResetBits(GPIOG,GPIO_Pin_3);
	GPIO_ResetBits(GPIOG,GPIO_Pin_4);
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_8) == 1)
		{
			return 12;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_7) == 1)
		{
			return 13;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_6) == 1)
		{
			return 14;
		}
	}
	
	if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
	{
		delay_ms(30);
		if(GPIO_ReadInputDataBit(GPIOG,GPIO_Pin_5) == 1)
		{
			return 15;
		}
	}
	
	return 20;
}



