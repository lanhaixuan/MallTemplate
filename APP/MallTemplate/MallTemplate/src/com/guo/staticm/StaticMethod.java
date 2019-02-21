package com.guo.staticm;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class StaticMethod {
	/**
	 * �ж���·
	 * 
	 * @param context
	 * @param runnable
	 * @return ���ô˷��� context ������Ҫ��д (����.this)��Ȼ�ᱨ�� ����ԭ����
	 */
	public static boolean Ifwifi(Context context) {

		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			Toast.makeText(context, "��ǰ��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}

	/**
	 * �ж���· ����Ҫ�����̵߳��ô˷���
	 * 
	 * @param context
	 * @param runnable
	 * @return ���ô˷��� context ������Ҫ��д (����.this)��Ȼ�ᱨ�� ����ԭ����
	 */
	public static void Ifwifi(Context context, Runnable runnable) {

		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			Toast.makeText(context, "�����ж���Ŷ��", Toast.LENGTH_SHORT).show();
			return;
		} else {
			new Thread(runnable).start();
		}

	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// ����Ǹ���������ֵ Where ��put����JSONObject Ȼ����add JSONArray����
			// ���ȫ��pu��json
			try {
				JSONObject whe = new JSONObject();
				JSONObject jb = new JSONObject();
				JSONArray where = new JSONArray();
				whe.put("activitiesCityId", "4201");
				where.put(whe);
				jb.put("TableName", "pre_common_City_activities");
				jb.put("Fields", "");
				jb.put("Where", where);

				// GetDataByW = as.GetDataByW(jb.toString(), "ͬ���б�");
				// DataList = GetDataByW.getGetDataByW();
				// handlerData.sendEmptyMessage(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	//
}
