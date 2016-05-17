package com.newqqeffects.activivy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.newqqeffects.R;
import com.newqqeffects.view.MyLinearLayout;
import com.newqqeffects.view.SlideMenu;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 模仿最新QQ客户端
 */
public class NewEffectsActivity extends Activity {
    public static final String[] setStrings = {
            "激活会员", "QQ钱包", "个性装扮", "我的收藏", "我的相册",
            "我的文件", "设置"};

    public static final String[] nameStrings = new String[]{"宋江", "卢俊义", "吴用",
            "公孙胜", "关胜", "林冲", "秦明", "呼延灼", "花荣", "柴进", "李应", "朱仝", "鲁智深",
            "武松", "董平", "张清", "杨志", "徐宁", "索超", "戴宗", "刘唐", "李逵", "史进", "穆弘",
            "雷横", "李俊", "阮小二", "张横", "阮小五", " 张顺", "阮小七", "杨雄", "石秀", "解珍"
    };

    private ListView mainListView, menuListView;
    private ImageView headImage;
    private SlideMenu mainSlideMenu;
    private MyLinearLayout myLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neweffects);

        //初始化View
        initView();

        //绑定适配器
        initAdapter();

        //事件监听
        mainSlideMenu.setOnDragStateChangeListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
                Toast.makeText(NewEffectsActivity.this, "打开菜单", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose() {
                ViewPropertyAnimator.animate(headImage).translationXBy(15)
                        .setInterpolator(new CycleInterpolator(4))
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onDraging(float precent) {
                ViewHelper.setAlpha(headImage, 1 - precent);
            }
        });

        myLinearLayout.setSlideMenu(mainSlideMenu);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mainListView = (ListView) findViewById(R.id.lv_main);
        menuListView = (ListView) findViewById(R.id.lv_menu);
        headImage = (ImageView) findViewById(R.id.iv_head);
        mainSlideMenu = (SlideMenu) findViewById(R.id.sm_main);
        myLinearLayout = (MyLinearLayout) findViewById(R.id.my_layout);
    }

    /**
     * 绑定适配器
     */
    private void initAdapter() {
        menuListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(20);
                return textView;
            }
        });

        mainListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView == null ? super.getView(position, convertView, parent) : convertView;
                //先缩小view
                ViewHelper.setScaleX(view, 0.5f);
                ViewHelper.setScaleY(view, 0.5f);
                //以属性动画放大
                ViewPropertyAnimator.animate(view).scaleX(1).setDuration(350).start();
                ViewPropertyAnimator.animate(view).scaleY(1).setDuration(350).start();
                return view;
            }
        });
    }
}
