package bjtu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bjtu.controller.UserController;
import bjtu.model.User;
import bjtu.util.Config;

public class LoginActivity extends AppCompatActivity {
    //申明控件变量
    private TextView forgetPassLink;
    private TextView registerLink;
    private EditText phoneInputEdit;
    private EditText passInputEdit;
    private Button loginBtn;

    public String phone = null;
    public String password = null;
    public UserController userController = new UserController();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件变量
        forgetPassLink = (TextView) findViewById(R.id.forgetPassLink);
        registerLink = (TextView) findViewById(R.id.registerLink);
        phoneInputEdit = (EditText) findViewById(R.id.phoneInputEdt);
        passInputEdit = (EditText) findViewById(R.id.passInputEdt);
        loginBtn = (Button) findViewById(R.id.loginButton);

        LoginOnClickListener loginOnClickListener = new LoginOnClickListener();
        loginBtn.setOnClickListener(loginOnClickListener);

        //注册界面的跳转事件监听
        registerLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        //忘记密码的跳转事件监听
        forgetPassLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPassIntent = new Intent();

            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if("pt".equals(data.getString("userRole"))){
                Intent ptLoginIntent = new Intent(LoginActivity.this,PTPageActivity.class);
                ptLoginIntent.putExtra("userID",data.getInt("userID"));
                startActivity(ptLoginIntent);
            }else if("qs".equals(data.getString("userRole"))){
                Intent ptLoginIntent = new Intent(LoginActivity.this,QSPageActivity.class);
                ptLoginIntent.putExtra("userID",data.getInt("userID"));
                startActivity(ptLoginIntent);
            }else{
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    //根据用户的角色跳转不同的界面
    class LoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            phone = phoneInputEdit.getText().toString().trim();
            password = passInputEdit.getText().toString().trim();
            Runnable loginNetWork = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    user = userController.login(phone,password);
                    Bundle data = new Bundle();
                    data.putString("userRole",user.getRole());
                    data.putInt("userID",user.getId());
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            };
            Thread loginThread = new Thread(loginNetWork);
            loginThread.start();
        }
    }
}
