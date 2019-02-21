package com.guo.homemall;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guo.adapter.HomeMallAdapter;
import com.guo.adapter.MyAdapter;
import com.guo.malltemplate.R;
import com.guo.method.SimpleFooter;
import com.guo.method.SimpleHeader;
import com.guo.method.ZrcListView;
import com.guo.method.ZrcListView.OnItemClickListener;
import com.guo.method.ZrcListView.OnStartListener;

public class HomeMallActivity extends Activity {
	private ZrcListView listView;
	private Handler handler;
	private int pageId = -1;
	private HomeMallAdapter adapter;
	private Double jine = 0.0;
	private TextView tv_jine;

	private static final String[][] names = new String[][] {
			{ "���ô�", "���", "�Ĵ�����", "��ʿ", "������", "Ų��", "����", "����", "�µ���", "����",
					"�¹�", "�ձ�", "����ʱ", "�����", "Ӣ��" },
			{ "�¹�", "������", "������", "����", "������", "�¼���", "ϣ��", "����", "����", "����͢",
					"����", "ӡ��", "��³", "������", "̩��" },
			{ "����", "�������", "�Ϸ�", "����", "ī����", "������", "����", "ί������", "����ά��",
					"�ڿ���" },
			{ "��ɫ��", "����", "�й�", "ɳ�ذ�����", "����˹", "���ױ���", "��������", "�ͻ�˹̹", "����",
					"������" } };

	List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homemallactivity);
		listView = (ZrcListView) findViewById(R.id.zListView);
		handler = new Handler();

		tv_jine = (TextView) findViewById(R.id.tv_jine);

		// ����Ĭ��ƫ��������Ҫ����ʵ��͸�����������ܡ�����ѡ��
		float density = getResources().getDisplayMetrics().density;
		listView.setFirstTopOffset((int) (50 * density));

		// ��������ˢ�µ���ʽ����ѡ�������û��Header���޷�����ˢ�£�
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		listView.setHeadable(header);

		// ���ü��ظ������ʽ����ѡ��
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		listView.setFootable(footer);

		// �����б�����ֶ�������ѡ��
		listView.setItemAnimForTopIn(R.anim.topitem_in);
		listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

		// ����ˢ���¼��ص�����ѡ��
		listView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				refresh();
			}
		});

		// ���ظ����¼��ص�����ѡ��
		listView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMore();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(ZrcListView parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(getApplicationContext(), HomeMallItem.class);
				startActivity(it);
			}
		});
		getServerGoods();
	}

	private void refresh() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getServerGoods();
				// int rand = (int) (Math.random() * 2); // �����ģ��ɹ�ʧ�ܡ�����������ݿ�ʼ��
				// if (rand == 0 || pageId == -1) {
				// pageId = 0;
				// for (String name : names[0]) {
				// mList.add(name);
				// }
				// adapter.notifyDataSetChanged();
				listView.setRefreshSuccess("���سɹ�"); // ֪ͨ���سɹ�
				listView.startLoadMore(); // ����LoadingMore����
				// } else {
				// listView.setRefreshFail("����ʧ��");
				// }
			}
		}, 2 * 1000);
	}

	private void loadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// pageId++;
				// if (pageId < names.length) {
				// for (String name : names[pageId]) {
				// mList.add(name);
				// }
				// adapter.notifyDataSetChanged();
				// listView.setLoadMoreSuccess();
				// } else {
				listView.stopLoadMore();
				// }
			}
		}, 2 * 1000);

	}

	private void setGoods(JSONArray response) {
		// TODO Auto-generated method stub
		for (int i = 0; i < response.length(); i++) {
			try {
				JSONObject json = response.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", json.getString("id"));
				map.put("orderid", json.getString("orderid"));
				map.put("is_pay", json.getString("is_pay"));
				map.put("goodsname", json.getString("goodsname"));
				map.put("goodprice", json.getString("goodprice"));
				map.put("quantity", json.getString("quantity"));
				map.put("imageurl", json.getString("imageurl"));
				mList.add(map);

				double dou = Integer.valueOf(json.getString("quantity")
						.toString())
						* Double.parseDouble(json.getString("goodprice")
								.toString());
				DecimalFormat df = new DecimalFormat("0.00");

				jine += Double.valueOf(df.format(dou));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		tv_jine.setText("�ϼ�: ��" + String.valueOf(jine));
		if (mList.size() > 0) {
			MyAdapter imageAdapter = new MyAdapter(HomeMallActivity.this,
					mList, listView);
			listView.setAdapter(imageAdapter);
			// listView.refresh(); // ��������ˢ��
		} else {
			Toast.makeText(getApplicationContext(), "��ȡ��Ʒ����ʧ�ܣ�",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public void getServerGoods() {

		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

		JsonArrayRequest request = new JsonArrayRequest(
				"http://47.94.80.89/orderitems/", new Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						System.out.println(response.toString());
						setGoods(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.toString(), error);
					}
				}) {
			protected Response<JSONArray> parseNetworkResponse(
					NetworkResponse response) {
				JSONArray JSONArray;
				try {
					JSONArray = new JSONArray(
							new String(response.data, "UTF-8"));
					return Response.success(JSONArray,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				}
			}
		};

		queue.add(request);
	}
}
