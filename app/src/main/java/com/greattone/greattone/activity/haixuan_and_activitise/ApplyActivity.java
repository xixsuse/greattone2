package com.greattone.greattone.activity.haixuan_and_activitise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.greattone.greattone.Listener.OnSelectCityListener;
import com.greattone.greattone.R;
import com.greattone.greattone.activity.BaseActivity;
import com.greattone.greattone.activity.UpdateVideoAct;
import com.greattone.greattone.adapter.PostGridAdapter;
import com.greattone.greattone.data.Data;
import com.greattone.greattone.dialog.CitySelectDialog;
import com.greattone.greattone.dialog.MyProgressDialog;
import com.greattone.greattone.dialog.NormalPopuWindow;
import com.greattone.greattone.entity.HaiXuanFilter;
import com.greattone.greattone.entity.Message2;
import com.greattone.greattone.entity.Picture;
import com.greattone.greattone.util.HttpProxyUtil;
import com.greattone.greattone.util.HttpUtil;
import com.greattone.greattone.util.HttpUtil.ResponseListener;
import com.greattone.greattone.widget.MyGridView;
import com.kf_test.picselect.GalleryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 海选报名-中华好琴声 */
public class ApplyActivity extends BaseActivity {
//	private ArrayList<String> videoFileList = new ArrayList<String>();
	private String price,originalPrice;
	private String id;
	private TextView tv_price;
	private TextView tv_sing_up1;
	private TextView tv_sing_up2;
	private TextView tv_game_area;
	private EditText et_teach_tel,et_teach,et_music,et_address,et_email,et_age,et_qq,et_phone,et_name;
	private TextView tv_area;
	private MyGridView gv_pic;
	private PostGridAdapter adapter;
	boolean isShowPic=false;
String filepass;
String mid = "20";
String classid = "73";//海选 73
private View ll_game_area;
private View ll_sing_up;
List<String> groupList1=new ArrayList<String>();
List<String> groupList2=new ArrayList<String>();
Map<String , List<String>> map=new HashMap<String, List<String>>();
private String bitype;
int baotype;
private RadioGroup radiogroup;
private EditText et_desc;
private View ll_desc;
private TextView tv_upload;
public static HaiXuanFilter haiXuanFilter = new HaiXuanFilter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply);
		this.id = getIntent().getStringExtra("id");
		this.originalPrice = getIntent().getStringExtra("price");
		this.bitype = getIntent().getStringExtra("bitype");//货币类型
String		type = getIntent().getStringExtra("baotype");//报名上传类型
		 baotype= getBaoType(type);//报名上传类型
		initView();
		getGroup();
	}
	/**获取分组*/
private void getGroup() {
	MyProgressDialog.show(context);
	HashMap<String, String> localHashMap = new HashMap<String, String>();
	localHashMap.put("api", "extend/haixuanType");
	localHashMap.put("titleid", id);
	addRequest(HttpUtil.httpConnectionByPost(context, localHashMap,
			new ResponseListener() {

				@Override
				public void setResponseHandle(Message2 message) {
					MyProgressDialog.Cancel();
					try {
						haiXuanFilter = JSON.parseObject(
							message.getData(), HaiXuanFilter.class);
					} catch (JSONException e) {
					}
					initGroups();
				}

			}, null));
		
	}
/**
 * 加载分组
 */
	private void initGroups() {
		if(haiXuanFilter.getGroup()!=null&&haiXuanFilter.getGroup().startsWith("{")){
		JSONObject jsonobject = JSON.parseObject(haiXuanFilter.getGroup());
		 Set<String> set =jsonobject.keySet();
		 groupList1=new ArrayList<String>(set);
		 map=new HashMap<String, List<String>>();
		 for (String string : set) {
			 map.put(string,JSON.parseArray( jsonobject.getString(string),String.class));
		}
		}
	}

	/**
	 * 加载视图
	 */
	private void initView() {
		setHead(getResources().getString(R.string.sign_up), true, true);//我要报名
		
		this.radiogroup = ((RadioGroup) findViewById(R.id.radiogroup));
		radiogroup.setOnCheckedChangeListener(onCheckedChangeListener);
		ll_game_area=findViewById(R.id.ll_apply_game_area);
		ll_game_area	.setOnClickListener(lis);
		ll_sing_up=findViewById(R.id.ll_apply_sign_up);
		findViewById(R.id.activity_apply_commit).setOnClickListener(lis);
		findViewById(R.id.ll_apply_sign_up).setOnClickListener(lis);
		findViewById(R.id.ll_apply_game_area).setOnClickListener(lis);
		this.tv_price = ((TextView) findViewById(R.id.activity_apply_paymoney));
		this.tv_sing_up1 = ((TextView) findViewById(R.id.activity_apply_sign_up1));
		tv_sing_up1	.setOnClickListener(lis);
		this.tv_sing_up2 = ((TextView) findViewById(R.id.activity_apply_sign_up2));
		tv_sing_up2	.setOnClickListener(lis);
		this.tv_game_area = ((TextView) findViewById(R.id.activity_apply_game_area));
		this.et_teach_tel = ((EditText) findViewById(R.id.activity_apply_teach_tel));
		this.et_teach = ((EditText) findViewById(R.id.activity_apply_teach));
		this.et_music = ((EditText) findViewById(R.id.activity_apply_music));
		this.et_address = ((EditText) findViewById(R.id.activity_apply_address));
		this.tv_area = ((TextView) findViewById(R.id.activity_apply_area));
		tv_area.setOnClickListener(lis);
		this.et_email = ((EditText) findViewById(R.id.activity_apply_email));
		this.et_age = ((EditText) findViewById(R.id.activity_apply_age));
		this.et_qq = ((EditText) findViewById(R.id.activity_apply_qq));
		this.et_phone = ((EditText) findViewById(R.id.activity_apply_phone));
		this.et_name = ((EditText) findViewById(R.id.activity_apply_name));
		this.tv_upload = ((TextView) findViewById(R.id.activity_apply_upload));
		this.ll_desc = ( findViewById(R.id.ll_desc));
		this.et_desc = ((EditText) findViewById(R.id.et_desc));
		gv_pic = (MyGridView) findViewById(R.id.gv_pic);

		initViewData();
	}

	private void initViewData() {
		if (baotype==2) {
			radiogroup.setVisibility(View.VISIBLE);
			radiogroup.check(R.id.radioButton1);
		}else if (baotype==1){//上传图片
			showUpdatePicture();
		}else if (baotype==0){//上传视频
			showUpdateVideo();
		}
		price=originalPrice;
		if (bitype.endsWith("人民币")) {
			this.tv_price.setText(price);
		}else{
			this.tv_price.setText(bitype+"$" +price);
		}
		this.et_name.setText(Data.myinfo.getUsername());
		this.et_phone.setText(Data.myinfo.getPhone());
		this.et_qq.setText("");
		this.et_age.setText("");
		this.et_email.setText(Data.myinfo.getEmail());
//		if (Data.myinfo.getAddress() != null
//				&& Data.myinfo.getAddress().length() > 0) {
//			this.tv_area.setText(Data.myinfo.getAddress() + ","
//					+ Data.myinfo.getAddress1() + ","
//					+ Data.myinfo.getAddress2());
//		}
		this.et_address.setText(Data.myinfo.getAddres());
		this.et_music.setText("");
		this.et_teach.setText("");
		this.et_teach_tel.setText("");
//		this.tv_game_area.setText("全国");
//		this.tv_sing_up.setText("钢琴");
		
	}
//	OnBtnItemClickListener itemClickListener=new OnBtnItemClickListener() {
//		
//		@Override
//		public void onItemClick(View v, int position) {
//			videoFileList.remove(position);
//			adapter.setList(videoFileList);
//		}
//	};
/**
 * 上传视频
 */
	private void showUpdateVideo() {
		isShowPic=false;
		ll_desc.setVisibility(View.GONE);
		tv_upload.setText("上传参赛视频");
		et_music.setHint("活动曲目");
		adapter=new PostGridAdapter(context, GalleryActivity.TYPE_VIDEO,1);
		gv_pic.setAdapter(adapter);
	}
	/**
	 * 上传图片
	 */
	private void showUpdatePicture() {
		isShowPic=true;
		ll_desc.setVisibility(View.VISIBLE);
		tv_upload.setText("选择图片");
		et_music.setHint("图片主题");
		adapter=new PostGridAdapter(context, GalleryActivity.TYPE_PICTURE,9);
		gv_pic.setAdapter(adapter);
	}

	OnClickListener lis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_apply_commit://提交
				submiitData();
				break;
			case R.id.activity_apply_area:// 选择地址
				CitySelectDialog citySelectDialog = new CitySelectDialog(
						context);
				citySelectDialog
						.setonClickSureListener(new OnSelectCityListener() {

							@Override
							public void ClickSure(String province, String city,
									String district) {
								tv_area.setText(province + "," + city + ","
										+ district);
							}
						});
				citySelectDialog.show();
				break;
			case R.id.ll_apply_game_area://赛区
//				 final List<String> mList1 =haiXuanFilter.getArea();
				  final List<String> mList1 =new ArrayList<String>();
				  mList1.add(getIntent().getStringExtra("title"));
				NormalPopuWindow		popu1 = new NormalPopuWindow(context, mList1,
						ll_game_area);
				popu1.setOnItemClickBack(new NormalPopuWindow.OnItemClickBack() {
					public void OnClick(int position, String text) {
						tv_game_area.setText(mList1
								.get(position));
					}
				});
				 popu1.show();
				break;
			case R.id.activity_apply_sign_up1://组别1
				 if (groupList1.size() > 0) {
					 final NormalPopuWindow		popu2= new NormalPopuWindow(context, groupList1,
							ll_sing_up);
					popu2.setOnItemClickBack(new NormalPopuWindow.OnItemClickBack() {
						public void OnClick(int position, String text) {
							tv_sing_up1.setText(groupList1
									.get(position));
							groupList2=map.get(tv_sing_up1.getText().toString().trim());
							//修改价格
							if (bitype.endsWith("人民币")) {
								price=JSON.parseObject(haiXuanFilter.getRMB()).getString(text);
								if (price.equals("0")) price=originalPrice;
								tv_price.setText(price);
							}else{
								price=JSON.parseObject(haiXuanFilter.getNTC()).getString(text);
								if (price.equals("0")) price=originalPrice;
								tv_price.setText(bitype+"$" +price);
							}
							popu2.dismisss();
						}
					});
					 popu2.show();
				 }else{
					 toast(getResources().getString(R.string.暂无组别) );//暂无组别
				 }
				break;
			case R.id.activity_apply_sign_up2://组别2
			
				if (groupList2.size() > 0) {
					final NormalPopuWindow		popu2= new NormalPopuWindow(context, groupList2,
							ll_sing_up);
					popu2.setOnItemClickBack(new NormalPopuWindow.OnItemClickBack() {
						public void OnClick(int position, String text) {
							tv_sing_up2.setText(groupList2
									.get(position));
							popu2.dismisss();
						}
					});
					popu2.show();
				}else{
					toast(getResources().getString(R.string.暂无组别) );//暂无组别
				}
				break;
			default:
				break;
			}
		}
	};

	OnCheckedChangeListener onCheckedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radioButton1://上传视频
				showUpdateVideo();
				break;
			case R.id.radioButton2://上传图片
				showUpdatePicture();
				break;

			default:
				break;
			}
		}
	};
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK && requestCode == 1) {// 视频
//			videoFileList = data
//					.getStringArrayListExtra(Constants.EXTRA_PHOTO_PATHS);
//			adapter.setList(videoFileList);
//		} else if (resultCode == RESULT_OK && requestCode == 0) {// 录制视频
////			videoFileList =new ArrayList<String>();
////			videoFileList.add(FileUtil.getLocalImageUrl(context, "video.mp4"));
////			adapter.setList(videoFileList);
//			Uri uri = data.getData();
//			Cursor cursor = this.getContentResolver().query(uri, null, null,
//					null, null);
//			if (cursor != null && cursor.moveToNext()) {
//				String filePath = cursor.getString(cursor
//						.getColumnIndex(VideoColumns.DATA));
//				ArrayList<String> mList = new ArrayList<String>();
//				mList.add(filePath);
//				adapter.setList(mList);
//				cursor.close();
//			}
//		}
//	}

	/** 提交 */
	protected void submiitData() {
		String name = et_name.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
//		String qq = et_qq.getText().toString().trim();
		String age = et_age.getText().toString().trim();
//		String email = et_email.getText().toString().trim();
		String area = tv_area.getText().toString().trim();
		String address = et_address.getText().toString().trim();
		String music = et_music.getText().toString().trim();
		String teach = et_teach.getText().toString().trim();
		String teach_tel = et_teach_tel.getText().toString().trim();
		String desc = et_desc.getText().toString().trim();
		String game_area = tv_game_area.getText().toString().trim();
		String sing_up1 = tv_sing_up1.getText().toString().trim();
		String sing_up2 = tv_sing_up2.getText().toString().trim();
		ArrayList<Picture> videoFileList = adapter.getList();
		if (TextUtils.isEmpty(name)) {
			toast(getResources().getString(R.string.请输入姓名));
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			toast(getResources().getString(R.string.请输入手机号));
			return;
		}
//		if (TextUtils.isEmpty(area)) {
//			toast(getResources().getString(R.string.请选择地址));
//			return;
//		}
		if (TextUtils.isEmpty(address)) {
			toast(getResources().getString(R.string.请填写详细地址));
			return;
		}
		if (TextUtils.isEmpty(music)) {
			toast(getResources().getString(R.string.请填写活动曲目));
			return;
		}
		if (TextUtils.isEmpty(teach)) {
			toast(getResources().getString(R.string.请填写推荐老师));
			return;
		}
		if (TextUtils.isEmpty(teach_tel)) {
			toast(getResources().getString(R.string.请填写推荐老师));
			return;
		}
		if (TextUtils.isEmpty(game_area)) {
			toast(getResources().getString(R.string.请选择赛区));
			return;
		}
		if (TextUtils.isEmpty(sing_up1)) {
			toast(getResources().getString(R.string.请选择组别));
			return;
		}
		if (isShowPic&&TextUtils.isEmpty(desc)) {
			toast("请填写图片描述");
			return;
		}
		if (videoFileList.size()==0) {
			toast(getResources().getString(R.string.请选择上传视频));
			return;
		}
		 filepass = System.currentTimeMillis() + "";
		 String [] msg={name,phone,area,address,music,teach,teach_tel,game_area,sing_up1,age,sing_up2,desc};
//		 post(msg,videoFileList);
//		 if (price.equals("0")) {
//		}else {
//		}
			MyProgressDialog.show(context);
		 if (isShowPic) {
			 postPic(msg,videoFileList,0);
		}else{
			postVideo(msg,videoFileList);
		}
	}
	private ArrayList<String> pictureUrlList=new ArrayList<String>();
	/**
	 * 图片报名
	 */
	private void postPic(final String [] msg, final ArrayList<Picture> videoFileList,final int num) {
		//发送图片
//		for (int i = 0; i < videoFileList.size(); i++) {
			HttpProxyUtil.updatePictureByCompress2(context, filepass, classid, videoFileList.get(num).getPicUrl(),	new ResponseListener() {

				@Override
				public void setResponseHandle(Message2 message) {
			String picUrl = JSON.parseObject(message.getData()).getString(
							"url");
			pictureUrlList.add(picUrl);
			if (pictureUrlList.size()==videoFileList.size()) {
				post1(msg);
			}else{
				postPic(msg, videoFileList,num+1);
			}
				}
			}, null);
//		}
	}
	/**
	 * 视频报名
	 */
	private void postVideo(final String [] msg, final ArrayList<Picture> videoFileList) {
		postVideoPic(msg, videoFileList);
	}
	/**	发送视频的缩略图
	 * @param videoFileList */
	protected void postVideoPic(final String [] msg, final ArrayList<Picture> videoFileList) {
		HttpProxyUtil.updatePictureByByte(context, filepass, classid, videoFileList.get(0).getPicUrl(), 	false,new ResponseListener() {

			@Override
			public void setResponseHandle(Message2 message) {
				String imgUrl = JSON.parseObject(message.getData()).getString(
						"url");
				updateVideo(msg,videoFileList,imgUrl);
//				post1(msg,videoFileList,imgUrl);
			}
		}, null );
	}
	/**
	 * 添加到preferences和启动服务
	 * @param videoFileList 
	 */
	private void updateVideo(String [] msg,  ArrayList<Picture> videoFileList, String imgUrl) {
		preferences.edit().putString("updateTitle", msg[0])//选手姓名
		.putString("updateUrl", imgUrl)
		.putString("updatePath", videoFileList.get(0).getPicUrl())
		.putString("updateContent", msg[4])
		.putString("updateClassid", classid)
		.putString("updateId", id)
		.putString("updateFilepass", filepass)
		.putString("updateHai_phone",msg[1])//联系电话
		.putString("updateHai_address",  msg[3])//详细地址
		.putString("updateHai_petition",  msg[4])//参赛曲目
		.putString("updateHai_mend",  msg[5])//所推荐的琴行(老师)
		.putString("updateHai_piano",   msg[6])//琴行(老师)电话
		.putString("updateHai_division",   msg[7])//比赛赛区
		.putString("updateHai_grouping",   msg[8])//选择分组1
		.putString("updateHai_age",  msg[9])//年龄
		.putString("updateHai_grouping2",  msg[10])//选择分组2
				.putString("updatepPintype",  "")//乐器分类
		.putInt("updateState", 0).commit();
		Intent intent=new Intent(context, UpdateVideoAct.class);
		intent.putExtra("isSee", 1);
		startActivity(intent);
		MyProgressDialog.Cancel();
		finish();
	}
//	/**	发送视频
//	 * @param videoFileList */
//	protected void post1(final String [] msg, ArrayList<Picture> videoFileList,final String imgUrl) {
//		MyProgressDialog.show(context);
//		HttpProxyUtil.updateVideo(context, filepass, classid, videoFileList.get(0).getPicUrl(), 	new ResponseListener() {
//
//					@Override
//					public void setResponseHandle(Message2 message) {
//						String videoUrl = JSON.parseObject(message.getData()).getString(
//								"url");
//						post2(imgUrl,videoUrl,msg);
//					}
//				}, null);
//	}
	/**	报名*/
	protected void post1( String [] msg) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("api", "post/ecms_bm");
//		map.put("mid", mid);
//		map.put("enews", "MAddInfo");
//		map.put("classid", classid);
//		map.put("bao_type","3");//海选
//		map.put("hai_id", id);
//		map.put("filepass", filepass);
//		map.put("hai_name", msg[0]);//选手姓名
//		map.put("hai_phone", msg[1]);//联系电话
//		map.put("hai_address", msg[3]);//详细地址
//		map.put("hai_petition", msg[4]);//参赛曲目
//		map.put("hai_mend", msg[5]);//所推荐的琴行(老师)
//		map.put("hai_piano", msg[6]);//琴行(老师)电话
//		map.put("hai_division", msg[7]);//比赛赛区
//		map.put("hai_grouping", msg[8]);//选择分组1
//		map.put("hai_age", msg[9]);//年龄
//		map.put("hai_grouping2", msg[10]);//选择分组2
//		map.put("smalltext", msg[11]);//图片描述
//		map.put("loginuid", Data.user.getUserid());
//		map.put("logintoken", Data.user.getToken());
		String data="api=post/ecms_bm&enews=MAddInfo&mid="+mid
				+"&classid="+104
				+"&bao_type=3"
				+"&hai_id="+id
				+"&hai_photo="+pictureUrlList.get(0)//标题图片
				+"&hai_name="+msg[0]//选手姓名
				+"&hai_phone="+msg[1]//联系电话
				+"&hai_address="+msg[3]//详细地址
				+"&hai_petition="+msg[4]//参赛曲目
				+"&hai_mend="+msg[5]//所推荐的琴行(老师)
				+"&hai_piano="+msg[6]//琴行(老师)电话
				+"&hai_division="+msg[7]//比赛赛区
				+"&hai_grouping="+msg[8]//选择分组1
				+"&hai_age="+msg[9]//年龄
				+"&hai_grouping2="+msg[10]//选择分组2
				+"&smalltext="+msg[11]//图片描述
				+"&loginuid="+Data.user.getUserid()
				+"&logintoken="+Data.user.getToken()
				+"&filepass="+filepass;
		for (int i = 0; i < pictureUrlList.size(); i++) {//图片路径
			data=data+"&msmallpic[]="+pictureUrlList.get(i);
		}
		addRequest(HttpUtil.httpConnectionByPost(context, data,
				new ResponseListener() {
			
			@Override
			public void setResponseHandle(Message2 message) {
				toast(getResources().getString(R.string.报名成功));
				MyProgressDialog.Cancel();
				finish();
			}
		}, null));
	}
	private int getBaoType(String type){
		boolean  isvideo = false;
		boolean  ispicture = false;
		if (TextUtils.isEmpty(type)) {//上传视频
			return 0;
		}else{
			String s[]=type.split("\\|");
			if (isHave(s, "视频")) {//能上传视频
				isvideo=true;
			}
			if (isHave(s, "图片")) {//能上传图片
				ispicture=true;
			}
			
				if (isvideo&&!ispicture) {//上传视频
					return 0;
				}else
				if (!isvideo&&ispicture) {//上传图片
					return 1;
				}else
				if (isvideo&&ispicture) {//上传视频和图片
					return 2;
				}
		}
		return 0;
	}
	/**
	 * 数组中有该字符串
	 * @param s
	 * @param str
	 * @return
	 */
private boolean isHave(String[] s,String str){
	for (String string : s) {
		if (str.equals(string)) {
			return true;
		}
	}
	return false;
}
}
