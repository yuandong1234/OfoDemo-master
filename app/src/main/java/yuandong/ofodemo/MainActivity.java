package yuandong.ofodemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yuandong.ofodemo.utils.ScreenUtils;
import yuandong.ofodemo.widget.CircleImageView;
import yuandong.ofodemo.widget.OfoMenuDrawable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG=MainActivity.class.getSimpleName();
    private RelativeLayout ofoMenu, ofoTitle, ofoContent,ofoAccount,ofoService;
    private ImageView ofoHome, ofoClose;
    private CircleImageView ofoAvatar;
    private TextView ofoAuthentication;

    //动画对象
    private ObjectAnimator
            titleAnimator,
            ofoAvatarAnimatorX,
            ofoAvatarAnimatorY,
            contentAnimator,
            ofoAccountAnimator,
            OfoAuthenticationAnimator,
            ofoServiceAnimator;
    //title起始和终止坐标，主要为动画做准备
    private int titleStartY, titleEndY;
    //avatar起始和终止坐标，主要为动画做准备
    private int avatarStartX, avatarEndX,avatarStartY, avatarEndY;
    //content起始和终止坐标，主要为动画做准备
    private int contentStartY, contentEndY;
    //account起始和终止坐标，主要为动画做准备
    private int accountStartX, accountEndX;
    //title动画标志，为事件分发做准备
    private boolean titleAnimationing;
    //content动画标志，为事件分发做准备
    private boolean contentAnimationing;

    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ofoHome = findViewById(R.id.ofo_home);
        ofoClose = findViewById(R.id.ofo_close);
        ofoMenu = findViewById(R.id.ofo_Menu);
        ofoTitle = findViewById(R.id.ofo_Title);
        ofoAvatar=findViewById(R.id.ofo_avatar);
        ofoContent = findViewById(R.id.ofo_content);
        ofoAccount=findViewById(R.id.ofo_account);
        ofoAuthentication=findViewById(R.id.ofo_account_author);
        ofoService=findViewById(R.id.ofo_service);
        ofoContent.setBackground(new OfoMenuDrawable(this));
        ofoHome.setOnClickListener(this);
        ofoClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ofo_close:
                close();
                break;
            case R.id.ofo_home:
                ofoHome.setVisibility(View.GONE);
                ofoMenu.setVisibility(View.VISIBLE);
                open();
                break;
        }
    }

    //定义动画部分
    private void initAnimation() {
        titleAnimator = ObjectAnimator.ofFloat(ofoTitle, "translationY", titleStartY, titleEndY);

        ofoAvatarAnimatorX=ObjectAnimator.ofFloat(ofoAvatar, "translationX", avatarStartX, avatarEndX);

        ofoAvatarAnimatorY=ObjectAnimator.ofFloat(ofoAvatar, "translationY", avatarStartY, avatarEndY);

        contentAnimator = ObjectAnimator.ofFloat(ofoContent, "translationY", contentStartY, contentEndY);

        ofoAccountAnimator = ObjectAnimator.ofFloat(ofoAccount, "translationX", accountStartX, contentEndY);

        OfoAuthenticationAnimator = ObjectAnimator.ofFloat(ofoAuthentication, "translationX", accountStartX, contentEndY);

        ofoServiceAnimator = ObjectAnimator.ofFloat(ofoService, "translationX", accountStartX, contentEndY);

        titleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                titleAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                titleAnimationing = false;
            }
        });

        contentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                contentAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentAnimationing = false;
                isOpen = !isOpen;
                ofoMenu.setVisibility(isOpen ? View.VISIBLE : View.INVISIBLE);
                if (isOpen) {

                } else {
                    ofoHome.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    //菜单打开的动画
    public void open() {
        int titleHeight = ofoTitle.getLayoutParams().height;
        titleStartY = -titleHeight;
        titleEndY = 0;


        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ofoAvatar.getLayoutParams();
        int leftMargin = lp.leftMargin;
        int topMargin=lp.topMargin;
        Log.e(TAG,"leftMargin : "+leftMargin+" , topMargin : "+topMargin);
        avatarStartX=-leftMargin;
        avatarEndX=0;

        avatarStartY=-topMargin;
        avatarEndY=0;

        contentStartY = ofoContent.getHeight();
        contentEndY = 0;

        Log.e(TAG,"accountWidth : "+ ScreenUtils.getScreenWidth(this));
        accountStartX= ScreenUtils.getScreenWidth(this)/2;
        accountEndX=0;

        initAnimation();

        //组合动画
        AnimatorSet set = new AnimatorSet();
        set.playTogether(titleAnimator,ofoAvatarAnimatorX,ofoAvatarAnimatorY,contentAnimator);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();

        AnimatorSet set2 = new AnimatorSet();
        //set2.play(ofoAccountAnimator).with(OfoAuthenticationAnimator).before(ofoServiceAnimator);
        set2.playTogether(ofoAccountAnimator,OfoAuthenticationAnimator,ofoServiceAnimator);
        set2.setDuration(300);
        set2.setInterpolator(new AccelerateDecelerateInterpolator());
        set2.setStartDelay(100);
        set2.start();
    }

    //菜单关闭的动画
    public void close() {
        int titleHeight = ofoTitle.getLayoutParams().height;
        titleStartY = 0;
        titleEndY = -titleHeight;

        contentStartY = 0;
        contentEndY = ofoContent.getHeight();

        avatarStartY=0;
        avatarEndY=contentEndY;

        initAnimation();


        AnimatorSet set = new AnimatorSet();
        set.playTogether(titleAnimator,ofoAvatarAnimatorY,contentAnimator);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }


    @Override
    public void onBackPressed() {
        if(isOpen){
            close();
        }else{
            finish();
        }
    }
}
