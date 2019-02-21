package com.guo.homemall;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.guo.adapter.HomeMallAdapter;
import com.guo.adapter.ImageAdapter;
import com.guo.adapter.MyAdapter;
import com.guo.malltemplate.R;
import com.guo.method.SimpleFooter;
import com.guo.method.SimpleHeader;
import com.guo.method.ZrcListView;
import com.guo.method.ZrcListView.OnItemClickListener;
import com.guo.method.ZrcListView.OnStartListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity implements
		android.view.View.OnClickListener {
	private ZrcListView listView;
	private Handler handler;
	private int pageId = -1;
	private MyAdapter adapter;

	private static final String[][] names = new String[][] {
			{ "���ô�", "���", "�Ĵ�����", "��ʿ", "������", "Ų��", "����", "����", "�µ���", "����",
					"�¹�", "�ձ�", "����ʱ", "�����", "Ӣ��" },
			{ "�¹�", "������", "������", "����", "������", "�¼���", "ϣ��", "����", "����", "����͢",
					"����", "ӡ��", "��³", "������", "̩��" },
			{ "����", "�������", "�Ϸ�", "����", "ī����", "������", "����", "ί������", "����ά��",
					"�ڿ���" },
			{ "��ɫ��", "����", "�й�", "ɳ�ذ�����", "����˹", "���ױ���", "��������", "�ͻ�˹̹", "����",
					"������" } };

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> aList = new ArrayList<Map<String, String>>();

	private TextView tv_qbdd, tv_wzf, tv_yzf, tv_ywc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myactivity);
		init();
		tv_qbdd = (TextView) findViewById(R.id.tv_qbdd);
		tv_wzf = (TextView) findViewById(R.id.tv_wzf);
		tv_yzf = (TextView) findViewById(R.id.tv_yzf);
		tv_ywc = (TextView) findViewById(R.id.tv_ywc);

		tv_qbdd.setOnClickListener(this);
		tv_wzf.setOnClickListener(this);
		tv_yzf.setOnClickListener(this);
		tv_ywc.setOnClickListener(this);

		getServerGoods();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		aList.clear();
		switch (arg0.getId()) {
		case R.id.tv_qbdd:
			MyAdapter imageAdapter = new MyAdapter(MyActivity.this, mList,
					listView);
			listView.setAdapter(imageAdapter);
			break;
		case R.id.tv_wzf:
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).get("is_pay").equals("false")) {
					aList.add(mList.get(i));
				}
			}
			imageAdapter = new MyAdapter(MyActivity.this, aList, listView);
			listView.setAdapter(imageAdapter);
			listView.stopLoadMore();
			break;
		case R.id.tv_yzf:
			for (int i = 0; i < mList.size(); i++) {
				if (!mList.get(i).get("is_pay").equals("false")) {
					aList.add(mList.get(i));
				}
			}
			imageAdapter = new MyAdapter(MyActivity.this, aList, listView);
			listView.setAdapter(imageAdapter);
			listView.stopLoadMore();
			break;
		case R.id.tv_ywc:
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).get("is_degauss").equals("ok")) {
					aList.add(mList.get(i));
				}
			}
			imageAdapter = new MyAdapter(MyActivity.this, aList, listView);
			listView.setAdapter(imageAdapter);
			listView.stopLoadMore();
			break;

		default:
			break;
		}
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
				map.put("is_degauss", json.getString("is_degauss"));
				map.put("goodsname", json.getString("goodsname"));
				map.put("goodprice", json.getString("goodprice"));
				map.put("quantity", json.getString("quantity"));
				map.put("imageurl", json.getString("imageurl"));
				mList.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (mList.size() > 0) {
			MyAdapter imageAdapter = new MyAdapter(MyActivity.this, mList,
					listView);
			listView.setAdapter(imageAdapter);
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

	private void init() {
		// TODO Auto-generated method stub
		listView = (ZrcListView) findViewById(R.id.zListView);
		handler = new Handler();

		// // ����Ĭ��ƫ��������Ҫ����ʵ��͸�����������ܡ�����ѡ��
		// float density = getResources().getDisplayMetrics().density;
		// listView.setFirstTopOffset((int) (50 * density));

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

		// adapter = new MyAdapter(MyActivity.this, mList);
		// listView.setAdapter(adapter);
		listView.refresh(); // ��������ˢ��

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
	}

	private void refresh() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
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
				// listView.stopLoadMore();
				// }
			}
		}, 2 * 1000);
	}

}
