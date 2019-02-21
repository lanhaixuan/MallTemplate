#include "stm32f10x.h"
#include "delay.h"
#include "Key_GPIO_Init.h"
#include "sys.h"
#include "usart.h"
#include "lcd.h" 


#include "usart2.h"
#include "HTTP_Demo.h"
#include "esp8266.h"
/************************************************
Ƕ��ʽ������ӡ�����ڷ��ͳ���
���ڴ���ͨ�������޸�
������������uart1��uart2 ����1��Ϊ���͸���ӡ������Ĵ��� ����2��Ϊ��Ƭ���������ݵĴ��� 
���ݴӵ��Զ˴������ַ��� ��������2���յ���Ƭ�� ��ͨ������ѡ���Ƿ�Ҫ��ӡ ��ͨ������1���ӡ�����ʹ�ӡ����
����2�������� �ж������Ƿ�������жϷ��͵��������Ƿ����ED��Ϊ��β ԭ����Ϊ�жϽ��յ��س����з� ��ʮ������Ϊ0X0D 0X0A
��Ϊ�ó�����Ҫ��ӡ�������� �����Ի��з���Ϊ���������� ��Ϊ��ASCII��EDΪ������ ��ʮ������Ϊ0X45 0X44
���ܰѴ���1�Ľ��ճ������� ����ֳ�������ĳ���жϺ�����
�ó��򲻰�������2�ķ��ͳ��� �ó�����ֵ�����printf����ͨ������1���͵�
���߸�С˧
************************************************/

//extern unsigned char  usart2_rcv_buf[MAX_RCV_LEN];

int main(void)
{		
	u16 t,i;  //������������ ��Ϊ����ļ���ֵ
	u16 len;	//�������ݳ��ȵı���
	u16 length;
	u16 usart2_receive,usart1_trans;  //��������whileѭ�����ж�����
	char HTTP_Buf[400];     //HTTP���Ļ�����

	 
	uint16_t Getkey;  //�����ֵ����
	 
	delay_init();	    	 //��ʱ������ʼ��	  
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2); //����NVIC�жϷ���2:2λ��ռ���ȼ���2λ��Ӧ���ȼ�
	uart1_init(9600);	 //����1��ʼ������ �䲨����Ϊ9600  
 


    USART2_Config();        //USART2��������ESP8266ģ��

    ESP8266_Init();         //ESP8266��ʼ��
	 
	Key_GPIO_Init();
	LCD_Init();

	POINT_COLOR=BLACK;

	LCD_Clear(WHITE);




	while(1)
	{
		LCD_ShowChinese(32,30,0,350,350,21,26);//�������ϵͳ
		LCD_ShowChinese(32,70,70,350,350,14,16);//���Ժ�
		LCD_ShowChinese(32,50,120,350,350,38,43);//���ڴ�������
		
		usart2_receive=0;
		while(usart2_receive == 0)
		{
			USART2_Clear();
			len = HTTP_GetPkt(HTTP_Buf, "detail"); //HTTP���
			length = strlen(usart2_rcv_buf);  
			USART2_Write(USART2, (unsigned char *)(HTTP_Buf), len);			//���ķ���
			delay_ms(1000);

			if(Flag)  //������ɵı�־λ15Ϊ1
			{	
				
				for(i=192; i<400 ; i++)
				{
					USART1_RX_BUF[i] = usart2_rcv_buf[i];  //����λ�����͵�����ת�浽���������
				}
				Flag=0;  //���������ɱ�־λ
				usart2_receive = 1;
				LCD_Clear(WHITE);
			}
		}		
		
		usart1_trans=0;
		while(usart1_trans == 0)
		{
			Getkey = Read_Key();	
			LCD_ShowChinese(32,30,0,350,350,21,26);//�������ϵͳ
			LCD_ShowChinese(32,70,70,350,350,7,9);//����1
			LCD_ShowChinese(32,50,120,350,350,10,13);//��ӡСƱ
			LCD_ShowChinese(32,70,170,350,350,48,50);//����2
			LCD_ShowChinese(32,50,220,350,350,44,47);//ȡ����ӡ
			
			if(Getkey == 1)
			{
				LCD_Clear(WHITE);
				LCD_ShowChinese(32,30,0,350,350,21,26);//�������ϵͳ
				LCD_ShowChinese(32,70,70,350,350,14,16);//���Ժ�
				LCD_ShowChinese(32,50,120,350,350,17,20);//���ڴ�ӡ
//			printf("\r\n\r\n");//���뻻��  ����\r\nΪ��һ��
				for(t=0;t<400;t++)
				{
					while(USART_GetFlagStatus(USART1,USART_FLAG_TC)!=SET);//�ȴ����ͽ���
					USART_SendData(USART1, USART1_RX_BUF[t]);//�򴮿�1��������
					
				}
				printf("\r\n\r\n");//���뻻��  ����\r\nΪ��һ��
				USART1_RX_STA=0;
				delay_ms(1000);
				LCD_Clear(WHITE);
				LCD_ShowChinese(32,30,0,350,350,21,26);//�������ϵͳ
				LCD_ShowChinese(32,50,120,350,350,27,30);//��ӡ���
				LCD_ShowChinese(32,0,160,350,350,31,37);//����3������ҳ
				Getkey = Read_Key();//���»�ȡ��ֵ ʹ��ʾ����ͣ���ڴ�ӡ����
				while(Getkey == 20 || Getkey != 3)
				{
					Getkey = Read_Key();  //������2ʱ�˳���ӡ����
				}
				usart1_trans = 1;
								
				LCD_Clear(WHITE);			
			}
			if(Getkey == 3)
			{
				usart1_trans = 1;
				USART1_RX_STA=0;
				LCD_Clear(WHITE);
			}
		}
	}	 
}

