package com.example.panyunyi.growingup.ui.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.panyunyi.growingup.MainActivity;
import com.example.panyunyi.growingup.R;
import com.example.panyunyi.growingup.entity.remote.User;
import com.example.panyunyi.growingup.manager.LoginImpl;
import com.example.panyunyi.growingup.ui.adapter.InspireAdapter;
import com.example.panyunyi.growingup.ui.base.BaseActivity;
import com.example.panyunyi.growingup.ui.custom.JellyInterpolator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LoginActivity extends BaseActivity implements OnClickListener {
    private static int TAG = 2;
    private final Lock lock = new ReentrantLock();
    //TODO-LIST: 增加注册页面
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private EditText userId;
    private EditText passWord;
    private TextView signUp;
    private RelativeLayout mainToAll;
    private ImageView backButton;
    private Condition notComplete = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private String nameString, psString;
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what) {
                case 1:
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), MainActivity.class);

                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    Log.i(">>>progress", "handler");
                    User user = new User();
                    user.userId = nameString;
                    user.userPassword = psString;
                    Log.i(">>>", nameString);
                    Log.i(">>>", psString);
                    final LoginImpl login = new LoginImpl(user);
                    new Thread() {
                        public void run() {

                            boolean result = login.login();
                            Log.i(">>>result", result + "");
                            if (result) {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }else{
                                Intent intentRaw=new Intent(LoginActivity.this,LoginActivity.class);
                                startActivity(intentRaw);
                                finish();
                            }
                        }
                    }.start();


                    break;

            }

            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();


//        if(getIntent()!=null) {
//            Intent intent=getIntent();
//            Log.i("intent",intent.toString());
//            userId.setText(intent.getStringExtra("userId"));
//            passWord.setText(intent.getStringExtra("passWord"));
//            nameString=intent.getStringExtra("userId");
//            psString=intent.getStringExtra("passWord");
//            Bundle bundle=new Bundle();
//            bundle.putString("userId",nameString);
//            bundle.putString("passWord",psString);
//            Message msg=new Message();
//            msg.what=2;
//            msg.setData(bundle);
//            Log.i("msgg",msg.toString());
//            handler.sendMessage(msg);
//
//        }
//        ACache mCache=ACache.get(getApplication(),"User");
//        if(mCache.getAsString("username")!=null){
//            Message msg=new Message();
//            msg.what=1;
//            handler.sendMessage(msg);
//        }
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        signUp = (TextView) findViewById(R.id.signup);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setVisibility(View.INVISIBLE);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        userId = (EditText) findViewById(R.id.userId);
        passWord = (EditText) findViewById(R.id.passWord);
        mainToAll = (RelativeLayout) findViewById(R.id.mainToAll);
        mBtnLogin.setOnClickListener(this);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_login:
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                nameString = userId.getText().toString();
                psString = passWord.getText().toString();
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                //inputAnimator(mInputLayout, mWidth, mHeight);
                mInputLayout.setVisibility(View.INVISIBLE);
                mBtnLogin.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                Log.i(">>>progress", "initiate");
                try {
                    progressAnimator(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                break;
            case R.id.signup:
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.i("daole", "fsdafs");
        nameString = data.getStringExtra("userId");
        psString = data.getStringExtra("passWord");


    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mInputLayout.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

                try {
                    progressAnimator(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) throws InterruptedException {
        Log.i(">>>progress", "start");
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();


    }


}
