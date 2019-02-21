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
			{ "加拿大", "瑞典", "澳大利亚", "瑞士", "新西兰", "挪威", "丹麦", "芬兰", "奥地利", "荷兰",
					"德国", "日本", "比利时", "意大利", "英国" },
			{ "德国", "西班牙", "爱尔兰", "法国", "葡萄牙", "新加坡", "希腊", "巴西", "美国", "阿根廷",
					"波兰", "印度", "秘鲁", "阿联酋", "泰国" },
			{ "智利", "波多黎各", "南非", "韩国", "墨西哥", "土耳其", "埃及", "委内瑞拉", "玻利维亚",
					"乌克兰" },
			{ "以色列", "海地", "中国", "沙特阿拉伯", "俄罗斯", "哥伦比亚", "尼日利亚", "巴基斯坦", "伊朗",
					"伊拉克" } };

	List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homemallactivity);
		listView = (ZrcListView) findViewById(R.id.zListView);
		handler = new Handler();

		tv_jine = (TextView) findViewById(R.id.tv_jine);

		// 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
		float density = getResources().getDisplayMetrics().density;
		listView.setFirstTopOffset((int) (50 * density));

		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		listView.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		listView.setFootable(footer);

		// 设置列表项出现动画（可选）
		listView.setItemAnimForTopIn(R.anim.topitem_in);
		listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

		// 下拉刷新事件回调（可选）
		listView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				refresh();
			}
		});

		// 加载更多事件回调（可选）
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
				// int rand = (int) (Math.random() * 2); // 随机数模拟成功失败。这里从有数据开始。
				// if (rand == 0 || pageId == -1) {
				// pageId = 0;
				// for (String name : names[0]) {
				// mList.add(name);
				// }
				// adapter.notifyDataSetChanged();
				listView.setRefreshSuccess("加载成功"); // 通知加载成功
				listView.startLoadMore(); // 开启LoadingMore功能
				// } else {
				// listView.setRefreshFail("加载失败");
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

		tv_jine.setText("合计: ￥" + String.valueOf(jine));
		if (mList.size() > 0) {
			MyAdapter imageAdapter = new MyAdapter(HomeMallActivity.this,
					mList, listView);
			listView.setAdapter(imageAdapter);
			// listView.refresh(); // 主动下拉刷新
		} else {
			Toast.makeText(getApplicationContext(), "获取商品数据失败！",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取订单数据
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
