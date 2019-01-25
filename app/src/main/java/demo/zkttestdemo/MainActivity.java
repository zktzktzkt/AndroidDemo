package demo.zkttestdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ypzn.basemodule.ARouterManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import demo.zkttestdemo.effect.activityjump.OneActivity;
import demo.zkttestdemo.effect.alipayhome.AlipayHomeActivity;
import demo.zkttestdemo.effect.alphatoolbar.AlphaToolbarActivity;
import demo.zkttestdemo.effect.baidusearch.BDSearchActivity;
import demo.zkttestdemo.effect.behavior.BehaviorActivity;
import demo.zkttestdemo.effect.bezier.BezierActivity;
import demo.zkttestdemo.effect.bottomsheet.ZHBottomSheetActivity;
import demo.zkttestdemo.effect.circleprogress.CircleProgressActivity;
import demo.zkttestdemo.effect.city_58.Loading58Activity;
import demo.zkttestdemo.effect.coordinator.CoordinatorActivity;
import demo.zkttestdemo.effect.dashboard.DashBoardActivity;
import demo.zkttestdemo.effect.doodle.DoodleActivity;
import demo.zkttestdemo.effect.dragbubble.DragBubbleActivity;
import demo.zkttestdemo.effect.draggridview.DragHelperGridActivity;
import demo.zkttestdemo.effect.draglayout.DragActivity;
import demo.zkttestdemo.effect.elemebtn.ElemeShopBtnActivity;
import demo.zkttestdemo.effect.favor.FavorActivity;
import demo.zkttestdemo.effect.filtermenu.FilterMenuActivity;
import demo.zkttestdemo.effect.flowlayout.FlowActivity;
import demo.zkttestdemo.effect.jssearchdemo.JSSearchActivity;
import demo.zkttestdemo.effect.keyboardbug.KeyBoardBugActivity;
import demo.zkttestdemo.effect.kugouguide.KuGouParallaxActivity;
import demo.zkttestdemo.effect.kugoumenu.KuGouMenuActivity;
import demo.zkttestdemo.effect.lettersidebar.LetterSideBarActivity;
import demo.zkttestdemo.effect.loadingdialog.LoadingActivity;
import demo.zkttestdemo.effect.lockpattern.LockPatternActivity;
import demo.zkttestdemo.effect.meituan2list.MeiTuanListActivity;
import demo.zkttestdemo.effect.motionevent.MotionEventTestActivity;
import demo.zkttestdemo.effect.multipage.MultiPageActivity;
import demo.zkttestdemo.effect.nestedscroll.NestedScrollTestActivity;
import demo.zkttestdemo.effect.overflyview.OverFlyActivity;
import demo.zkttestdemo.effect.paint.PaintActivity;
import demo.zkttestdemo.effect.paykeyboard.PayKeyboardActivity;
import demo.zkttestdemo.effect.piechart.PieChartActivity;
import demo.zkttestdemo.effect.progress.ProgressArcActivity;
import demo.zkttestdemo.effect.pullAndload.PtrAndloadActivity;
import demo.zkttestdemo.effect.qqslidemenu.QQSlideMenuActivity;
import demo.zkttestdemo.effect.screenmatch.ScreenMatchActivity;
import demo.zkttestdemo.effect.slidemenu.SlideMenuActivity;
import demo.zkttestdemo.effect.snow.SnowActivity;
import demo.zkttestdemo.effect.statusbar.StatusBarActivity;
import demo.zkttestdemo.effect.stikyhead.StikyHeadActivity;
import demo.zkttestdemo.effect.tablayout.MyTablayoutActivity;
import demo.zkttestdemo.effect.tempcontrol.TemControlActivity;
import demo.zkttestdemo.effect.transition.TransitionActivity;
import demo.zkttestdemo.effect.verificationinput.VerificationInputActivity;
import demo.zkttestdemo.effect.verticaldrag.VerticalDragActivity;
import demo.zkttestdemo.effect.viewdraghelper.ViewDragHelperDemoActivity;
import demo.zkttestdemo.effect.wavrecord.WAVActivity;
import demo.zkttestdemo.effect.window.FloatWindowActivity;
import demo.zkttestdemo.effect.wxaudio.AudioActivity;
import demo.zkttestdemo.realm.RealmActivity;
import demo.zkttestdemo.recyclerview.diffUtil.DiffUtilActivity;
import demo.zkttestdemo.recyclerview.header.BannerRecyclerActivity;
import demo.zkttestdemo.recyclerview.multichoice.MultiChoiceActivity;
import demo.zkttestdemo.recyclerview.nested.NestedRecyclerActivity;
import demo.zkttestdemo.recyclerview.refresh.RefreshActivity;
import demo.zkttestdemo.recyclerview.singlechoice.SingleChoiceActivity;
import demo.zkttestdemo.recyclerview.suspendmulti.SuspendMultiActivity;
import demo.zkttestdemo.recyclerview.suspendsingle.SuspendSingleActivity;
import demo.zkttestdemo.retrofit.RetrofitActivity;
import demo.zkttestdemo.rxjava.RxJavaActivity;
import demo.zkttestdemo.utils.ClickUtil;
import demo.zkttestdemo.utils.FileUtils;
import demo.zkttestdemo.utils.ImgUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView image_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClickUtil.init(new ClickUtil.IProxyClickListener() {
            @Override
            public boolean onProxyClick(WrapClickListener wrap, View v) {
                Log.e("点击了按钮", v.getId() + "");
                return false;
            }
        });
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //在onDestroy中加入
                // decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ClickUtil.hookViews(decorView, 0);
            }
        });

        /*new Thread(new Runnable() {

            public String getRandomString(int length) {
                //        String str = "0123456789";
                String str = "abcdefghijklmnopqrstuvwxyz0123456789";
                Random random = new Random();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < length; i++) {
                    //            int number = random.nextInt(36);
                    int number = random.nextInt(str.length());
                    sb.append(str.charAt(number));
                }
                return sb.toString();
            }

            @Override
            public void run() {
                try {
                    // TODO: 2018-7-7
                    List<String> list = new ArrayList<>();
                    File file = new File(getExternalCacheDir(), "F码文件.txt");
                    if (file.exists()) {
                        boolean delete = file.delete();
                        Log.e("测试F", "删除->" + delete);
                    }
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();

                    // 001 手环 //002 体脂称
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 10000; i++) {
                        String randomString = getRandomString(16);
                        if (!list.contains(randomString)) {
                            list.add("001" + randomString);
                        } else {
                            i--;
                        }
                    }
                    Log.e("测试F", "List长度->" + list.size());

                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i));
                        sb.append("\n");
                    }
                    Log.e("测试F", "添加完成");

                    FileWriter writer = new FileWriter(file);
                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();*/


        /*ArrayList<String> singInfo_MD5 = KeystoreStr.getSingInfo(this, getPackageName(), KeystoreStr.MD5);
        ArrayList<String> singInfo_SHA1 = KeystoreStr.getSingInfo(this, getPackageName(), KeystoreStr.SHA1);
        ArrayList<String> singInfo_SHA256 = KeystoreStr.getSingInfo(this, getPackageName(), KeystoreStr.SHA256);
        Log.e("singInfo_MD5", singInfo_MD5.get(0));
        Log.e("singInfo_SHA1", singInfo_SHA1.get(0));
        Log.e("singInfo_SHA256", singInfo_SHA256.get(0));*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((EditText) findViewById(R.id.et_edit)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("MainActivity", "CharSequence:" + s + "  start:" + start + "  before:" + before + "  count:" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**创建调用系统照相机待存储的临时文件*/
        createCameraTempFile(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.openDrawer(GravityCompat.END); //从右往左打开菜单，布局中设置打开方向 android:layout_gravity="end"
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        image_head = (ImageView) headerLayout.findViewById(R.id.image_head);

        image_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**弹出popwindow*/
                initPopWindow();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Retrofit) {
            Intent intent = new Intent(this, RetrofitActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_kotlin) {
            /*Intent intent = new Intent(this, KotlinActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_Daggar2) {

        } else if (id == R.id.nav_activityJump) {
            Intent intent = new Intent(this, OneActivity.class);
            startActivity(intent);
        }
        //嵌套滑动测试
        else if (id == R.id.nav_nestedScrollTest) {
            Intent intent = new Intent(this, NestedScrollTestActivity.class);
            startActivity(intent);
        }
        //viewDragHelper
        else if (id == R.id.nav_viewDragHelperTest) {
            Intent intent = new Intent(this, DragHelperGridActivity.class);
            startActivity(intent);
        }
        //嵌套滑动RecyclerView
        else if (id == R.id.nav_nestedRecycler) {
            Intent intent = new Intent(this, NestedRecyclerActivity.class);
            startActivity(intent);
        }
        //自定义下拉刷新
        else if (id == R.id.nav_refreshRecycler) {
            Intent intent = new Intent(this, RefreshActivity.class);
            startActivity(intent);
        }
        // itemTouchHelper示例
        else if (id == R.id.nav_itemTouchHelper) {
            ARouter.getInstance().build(ARouterManager.ItemTouchHelperActivity).navigation();
        }
        // 刮刮卡效果
        else if (id == R.id.nav_scratchView) {
            ARouter.getInstance().build(ARouterManager.ScratchActivity).navigation();
        }
        // 流式布局
        else if (id == R.id.nav_flow) {
            Intent intent = new Intent(this, FlowActivity.class);
            startActivity(intent);
        }
        // 仪表盘
        else if (id == R.id.nav_dashBoard) {
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }
        // 饼状图
        else if (id == R.id.nav_piechart) {
            Intent intent = new Intent(this, PieChartActivity.class);
            startActivity(intent);
        }
        //Window悬浮窗
        else if (id == R.id.nav_floatWindow) {
            Intent intent = new Intent(this, FloatWindowActivity.class);
            startActivity(intent);
        }
        //仿酷狗视差引导页
        else if (id == R.id.nav_kugou_parallax) {
            Intent intent = new Intent(this, KuGouParallaxActivity.class);
            startActivity(intent);
        }
        //仿酷狗侧滑菜单
        else if (id == R.id.nav_kugou_menu) {
            Intent intent = new Intent(this, KuGouMenuActivity.class);
            startActivity(intent);
        }
        // WAV格式录音
        else if (id == R.id.nav_wavRecord) {
            Intent intent = new Intent(this, WAVActivity.class);
            startActivity(intent);
        }
        //仿QQ拖拽气泡
        else if (id == R.id.nav_dragBubble) {
            Intent intent = new Intent(this, DragBubbleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lockPattern) {
            Intent intent = new Intent(this, LockPatternActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bezier) {
            Intent intent = new Intent(this, BezierActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mt2List) {
            Intent intent = new Intent(this, MeiTuanListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_behaviorDemo) {
            Intent intent = new Intent(this, BehaviorActivity.class);
            startActivity(intent);
        }
        //温度旋转按钮
        else if (id == R.id.nav_temp_control) {
            Intent intent = new Intent(this, TemControlActivity.class);
            startActivity(intent);
        }
        //仿简书的搜索栏动画
        else if (id == R.id.nav_JSSearch) {
            Intent intent = new Intent(this, JSSearchActivity.class);
            startActivity(intent);
        }
        //沉浸式状态栏
        else if (id == R.id.nav_statusBar) {
            Intent intent = new Intent(this, StatusBarActivity.class);
            startActivity(intent);
        }
        //圆形layout布局
        else if (id == R.id.nav_circleLayout) {
            ARouter.getInstance().build(ARouterManager.CircleLayoutActivity).navigation();
        }
        //自定义动画框架
        else if (id == R.id.nav_animatorFrameWork) {
            ARouter.getInstance().build(ARouterManager.AnimatorFrameworkActivity).navigation();
        }
        //自定义带三角指示器的progress
        else if (id == R.id.nav_progress) {
            ARouter.getInstance().build(ARouterManager.ProgressActivity).navigation();
        }
        //屏幕适配
        else if (id == R.id.nav_screenMatch) {
            Intent intent = new Intent(this, ScreenMatchActivity.class);
            startActivity(intent);
        }//仿饿了么购物车加减按钮
        else if (id == R.id.nav_elemeShopBtn) {
            Intent intent = new Intent(this, ElemeShopBtnActivity.class);
            startActivity(intent);
        }
        //仿百度的搜索栏效果
        else if (id == R.id.nav_BDSearch) {
            Intent intent = new Intent(this, BDSearchActivity.class);
            startActivity(intent);
        }
        //转盘效果
        else if (id == R.id.nav_lottery) {
            ARouter.getInstance().build(ARouterManager.LotteryActivity).navigation();
        }
        //仿58同城的加载动画
        else if (id == R.id.nav_58Loading) {
            Intent intent = new Intent(this, Loading58Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_overFlying) {
            Intent intent = new Intent(this, OverFlyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lettesSideBar) {
            Intent intent = new Intent(this, LetterSideBarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_filterMenu) {
            Intent intent = new Intent(this, FilterMenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bottomSheet) {
            Intent intent = new Intent(this, ZHBottomSheetActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doodle) {
            Intent intent = new Intent(this, DoodleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_alipayHomeAnim) {
            Intent intent = new Intent(this, AlipayHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_snow) {
            Intent intent = new Intent(this, SnowActivity.class);
            startActivity(intent);
        }
        //缩放loding加载
        else if (id == R.id.nav_loadingView) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
        }
        //paint 实验
        else if (id == R.id.nav_paint) {
            Intent intent = new Intent(this, PaintActivity.class);
            startActivity(intent);
        }
        //仿汽车之家
        else if (id == R.id.nav_verticalDrag) {
            Intent intent = new Intent(this, VerticalDragActivity.class);
            startActivity(intent);
        }
        //点赞飘心效果
        else if (id == R.id.nav_favor) {
            Intent intent = new Intent(this, FavorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drag) {
            Intent intent = new Intent(this, DragActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_verificationInput) {
            Intent intent = new Intent(this, VerificationInputActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bannerHeaderRecycler) {
            Intent intent = new Intent(this, BannerRecyclerActivity.class);
            startActivity(intent);
        }
        //事件分发测试
        else if (id == R.id.nav_MotionEventTest) {
            Intent intent = new Intent(this, MotionEventTestActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_multi_page) {
            Intent intent = new Intent(this, MultiPageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myIndicator) {
            Intent intent = new Intent(this, MyTablayoutActivity.class);
            startActivity(intent);
        }
        //圆形进度progress按钮
        else if (id == R.id.nav_circleProgress) {
            Intent intent = new Intent(this, CircleProgressActivity.class);
            startActivity(intent);
        }//运动步数progress
        else if (id == R.id.nav_sportProgress) {
            ARouter.getInstance().build(ARouterManager.SportProgressActivity).navigation();
        } else if (id == R.id.nav_slideMenu) {
            Intent intent = new Intent(this, SlideMenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_qqSlideMenu) {
            Intent intent = new Intent(this, QQSlideMenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_realm) {
            Intent intent = new Intent(this, RealmActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_RxJava) {
            Intent intent = new Intent(this, RxJavaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_viewDragHelper) {
            Intent intent = new Intent(this, ViewDragHelperDemoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Volley) {

        } else if (id == R.id.nav_keyboard_bug) {
            Intent intent = new Intent(this, KeyBoardBugActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_progress_arc) {
            Intent intent = new Intent(this, ProgressArcActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_audio_reord) {
            Intent intent = new Intent(this, AudioActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_coordinator) {
            Intent intent = new Intent(this, CoordinatorActivity.class);
            startActivity(intent);
        }
        // CoordinatorLayout实现悬浮头
        else if (id == R.id.nav_stikyHead) {
            Intent intent = new Intent(this, StikyHeadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pay_keyboard) {
            Intent intent = new Intent(this, PayKeyboardActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_suspend_single) {
            Intent intent = new Intent(this, SuspendSingleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_suspend_multi) {
            Intent intent = new Intent(this, SuspendMultiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recycler_singleChoice) {
            Intent intent = new Intent(this, SingleChoiceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recycler_multiChoice) {
            Intent intent = new Intent(this, MultiChoiceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_eatBaoZi) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pullAndLoad) {
            Intent intent = new Intent(this, PtrAndloadActivity.class);
            startActivity(intent);
        }
        // 渐变Toolbar
        else if (id == R.id.nav_alphaToolbar) {
            Intent intent = new Intent(this, AlphaToolbarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_transition) {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recycler_diff_util) {
            Intent intent = new Intent(this, DiffUtilActivity.class);
            startActivity(intent);
        }

        return true;
    }


    /***************************************************************************************
     * 以下都是相机拍照相关，注意在onCreate里调用 createCameraTempFile(savedInstanceState);
     ****************************************************************************************/
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    //调用照相机返回图片临时文件
    private File tempFile;
    private String path;//图片的路径
    private String imgName;
    private Uri mUri;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String state = (String) msg.obj;
            if (state.equals("")) {
                // MyToast.show(PersonInfoActivity.this,"图片上传失败");
                imgName = state;
            } else {
                imgName = state;
            }
            /**个人信息更新*/
            //  updateData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    photoClip(mUri);
                    break;
                case PHOTO_REQUEST:
                    if (data != null) {
                        photoClip(data.getData());
                    }
                    break;
                case PHOTO_CLIP:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap photo = extras.getParcelable("data");
                            image_head.setImageBitmap(photo);
                            //压缩头像大小
                            photo = FileUtils.decodeSampledBitmapFromBitmap(photo, 70, 70);
                            try {
                                path = saveFile(photo, "demo.jpg");
                                //  uploadImg(path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }

    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */

    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(getExternalCacheDir() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private void uploadImg(final String path) {
        final String imgUrl = "url";
        new Thread() {
            @Override
            public void run() {
                super.run();
                String string = ImgUtil.uploadFile(imgUrl, path);
                Message msg = Message.obtain();
                msg.obj = string;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    /**
     * 上传头像
     */
    private void initPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //跳转到调用系统相机
                    getPicFromCamera();
                    popupWindow.dismiss();
                }

            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    //跳转到调用系统图库
                    getPicFromPhoto();
                    popupWindow.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public String saveFile(Bitmap bm, String fileName) throws IOException {
        String path = getFilesDir() + "/temp/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getAbsolutePath();
    }

    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".picture", tempFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        } else {
            mUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        }
        startActivityForResult(intent, CAMERA_REQUEST);
    }
}
