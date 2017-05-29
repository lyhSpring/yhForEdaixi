package bjtu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import bjtu.controller.UserController;


public class RegisterActivity extends AppCompatActivity {

    //申明控件变量
    private Button getSpecificationBtn;
    private Button nextStepBtn;
    private Spinner regRoleSpinner;
    private EditText regPhoneEdt;
    private EditText specificationEdt;
    private Timer timer = new Timer();

    public String registerRole = null;
    public String registerPhone = null;
    public String regSpecification = null;

    private UserController userController = new UserController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        regRoleSpinner = (Spinner) findViewById(R.id.registerRole);
        regPhoneEdt = (EditText) findViewById(R.id.regPhoneEdt);
        specificationEdt = (EditText) findViewById(R.id.specificationEdt);
        //获取验证码按钮，差一个监听事件
        getSpecificationBtn = (Button) findViewById(R.id.getSpecificationBtn);
        getSpecificationBtn.setOnClickListener(new GetSpecificationOnClickListener());
        //下一步按钮，下一步跳转到密码编辑界面
        nextStepBtn = (Button)findViewById(R.id.nextStep);
        NextStepBtnOnClickListener nextStepBtnOnClickListener = new NextStepBtnOnClickListener();
        nextStepBtn.setOnClickListener(nextStepBtnOnClickListener);

        //获取选择的角色，获取输入的手机号，获取输入的验证码
        regRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                registerRole = regRoleSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterActivity.this, "请选择注册用户的角色", Toast.LENGTH_SHORT).show();
            }
        });
        registerPhone = regPhoneEdt.getText().toString().trim();
        regSpecification = specificationEdt.getText().toString().trim();
    }

    //添加获取验证码的点击事件
    class GetSpecificationOnClickListener implements OnClickListener{
        @Override
        public void onClick(View v){
            //调用发送短信的
            Runnable sendCodeNetWork = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 4;
                    msg.obj = userController.send(registerPhone,String.valueOf(10));
                    handler.sendMessage(msg);
                }
            };
            Thread sendCodeThread = new Thread(sendCodeNetWork);
            sendCodeThread.start();
            //计时
            TimerTask timerTask = new TimerTask() {
                int i =10; //倒数10秒
                @Override
                public void run() {
                    Message timeMsg = new Message();
                    timeMsg.what = i--;
                    timerHandler.sendMessage(timeMsg);
                }
            };
            timer.schedule(timerTask,0,1000);

            //
        }
    }

    //添加下一步的点击事件
    class NextStepBtnOnClickListener implements OnClickListener{
        @Override
        public void onClick(View v){
            //验证随机码
            Runnable verifyCodeNetWork = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = userController.verifyCode(registerPhone,regSpecification);
                    handler.sendMessage(msg);
                }
            };
            Thread verifyCodeThread = new Thread(verifyCodeNetWork);
            verifyCodeThread.start();
        }
    }

    Handler timerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what > 0){
                getSpecificationBtn.setClickable(false);
                getSpecificationBtn.setText(msg.what+"s后重试");
            }else{
                getSpecificationBtn.setClickable(true);
                timer.cancel();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent editPasswordIntent = new Intent(RegisterActivity.this,EditPasswordActivity.class);
                    editPasswordIntent.putExtra("registerPhone",registerPhone);
                    editPasswordIntent.putExtra("registerRole",registerRole);
                    startActivity(editPasswordIntent);
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this,"验证码过期",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(RegisterActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
