package com.guo.homemall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guo.attribute.ImageDownloader;
import com.guo.attribute.OnImageDownload;
import com.guo.malltemplate.R;

public class DetailsActivity extends Activity {
	private ViewPager viewPager;
	private LinearLayout point_group;
	private TextView image_desc;
	private HorizontalScrollView horizontalScrollView;
	private LinearLayout linearLayout;
	private GridView gridview;

	// ͼƬ��Դid
	private final int[] images = { R.drawable.img_xiangqing,
			R.drawable.img_xiangqing, R.drawable.img_xiangqing,
			R.drawable.img_xiangqing, R.drawable.img_xiangqing };
	// ͼƬ���⼯��
	private final String[] imageDescriptions = { "���������ף��ҾͲ��ܵ���",
			"�����ֻ��������ٳ������ϸ������˴�ϳ�", "���ر�����Ӱ�������", "������TV�������", "��Ѫ��˿�ķ�ɱ" };

	private ArrayList<ImageView> imageList;
	// ��һ��ҳ���λ��
	protected int lastPosition = 0;

	// �ж��Ƿ��Զ�����viewPager
	private boolean isRunning = true;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ִ�л�������һ��ҳ��
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			if (isRunning) {
				// �ڷ�һ��handler��ʱ
				handler.sendEmptyMessageDelayed(0, 2000);
			}

		};
	};

	private TextView name, price, address;
	private Map<String, Object> DataMap;
	private ImageView iv_image, iv_saosao;
	private ImageDownloader mDownloader;
	private String typ = "1";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		init();
		DataMap = (Map<String, Object>) getIntent().getSerializableExtra(
				"mList");
		typ = getIntent().getExtras().getString("typ");
		name = (TextView) findViewById(R.id.tv_name);
		price = (TextView) findViewById(R.id.tv_price);
		address = (TextView) findViewById(R.id.tv_address);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_saosao = (ImageView) findViewById(R.id.iv_saosao);

		name.setText(DataMap.get("name").toString());
		price.setText("��" + DataMap.get("price").toString() + "��");
		address.setText(DataMap.get("address").toString());

		String url = DataMap.get("image").toString();
		if (mDownloader == null) {
			mDownloader = new ImageDownloader();
		}
		iv_image.setTag(url);
		// �첽����ͼƬ
		mDownloader.imageDownload(url, iv_image, "/yanbin",
				DetailsActivity.this, new OnImageDownload() {
					@Override
					public void onDownloadSucc(Bitmap bitmap, String c_url,
							ImageView mimageView) {

						ImageView imageView = (ImageView) mimageView
								.findViewWithTag(c_url);
						if (imageView != null) {
							mimageView.setImageBitmap(bitmap);
							imageView.setTag("");
						}
					}
				}, url);

		iv_saosao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (typ.equals("1")) {
					Intent it = new Intent();
					it.setClass(DetailsActivity.this, ScanCodeActicity.class);
					startActivity(it);
				} else {

					JSONObject json = new JSONObject();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat formatter1 = new SimpleDateFormat(
							"MMddHHmm");
					try {
						json.put("orderid", formatter1.format(new Date()));
						json.put("goodsname", DataMap.get("name"));
						json.put("goodprice", DataMap.get("price"));
						json.put("quantity", "1");
						json.put("imageurl", DataMap.get("image"));
						json.put("is_degauss", "no");
						json.put("is_pay", "false");
						json.put("add_time", formatter.format(new Date()));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					volley_JsonObjectRequestPost(json);
				}

			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		point_group = (LinearLayout) findViewById(R.id.point_group);
		image_desc = (TextView) findViewById(R.id.image_desc);
		image_desc.setText(imageDescriptions[0]);

		// ��ʼ��ͼƬ��Դ
		imageList = new ArrayList<ImageView>();
		for (int i : images) {
			// ��ʼ��ͼƬ��Դ
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(i);
			imageList.add(imageView);

			// ���ָʾС��
			ImageView point = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5,
					5);
			params.rightMargin = 20;
			point.setLayoutParams(params);
			point.setBackgroundResource(R.drawable.ic_launcher);
			if (i == R.drawable.ic_launcher) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}

			point_group.addView(point);
		}

		viewPager.setAdapter(new MyPageAdapter());
		// ���õ�ǰviewPager��λ��
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2
				- (Integer.MAX_VALUE / 2 % imageList.size()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// ҳ���л�����ã� position���µ�ҳ��λ��

				// ʵ��������ѭ������
				position %= imageList.size();

				image_desc.setText(imageDescriptions[position]);

				// �ѵ�ǰ������Ϊtrue,����һ������Ϊfalse
				point_group.getChildAt(position).setEnabled(true);
				point_group.getChildAt(lastPosition).setEnabled(false);
				lastPosition = position;

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ҳ�����ڻ���ʱ��ص�

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// ��pageView ״̬�����ı��ʱ�򣬻ص�

			}
		});

		/**
		 * �Զ�ѭ���� 1.��ʱ����Timer 2.�����̣߳�while trueѭ�� 3.ClockManger
		 * 4.��Handler������ʱ��Ϣ��ʵ��ѭ����������
		 * 
		 */

		handler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	protected void onDestroy() {
		// ֹͣ����
		isRunning = false;
		super.onDestroy();
	}

	private class MyPageAdapter extends PagerAdapter {
		// ��Ҫʵ�������ĸ�����

		@Override
		public int getCount() {
			// ���ҳ�������
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// �ж�view��Object��Ӧ�Ƿ��й�����ϵ
			if (view == object) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// �����Ӧλ���ϵ�view�� container view����������ʵ����viewpage����,
			// position: viewpager�ϵ�λ��
			// ��container�������
			container.addView(imageList.get(position % imageList.size()));

			return imageList.get(position % imageList.size());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// ���ٶ�Ӧλ���ϵ�Object
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
			object = null;
		}

	}

	/*
	 * JsonObjectRequest JsonObjectRequest �� To send and receive JSON Object
	 * from the Server ���������������ͺͽ���JSON����
	 * ������һ�����ع��캯�����������ʵ������󷽷���DELETE��GET��POST��PUT����
	 * 
	 * GET��ʽ���������
	 */
	private void volley_JsonObjectRequestPost(JSONObject json) {
		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		JsonObjectRequest request = new JsonObjectRequest(Method.POST,
				"http://47.94.80.89/orderitems/", json,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(DetailsActivity.this, "��ӳɹ���",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				});

		request.setTag("volley_JsonObjectRequestGet");
		queue.add(request);
	}
}
