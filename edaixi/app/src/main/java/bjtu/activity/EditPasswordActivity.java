package bjtu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bjtu.controller.UserController;
import bjtu.model.User;

public class EditPasswordActivity extends AppCompatActivity {

    //控件声明
    private Button nextStep;
    private EditText inputPassEdt;
    private EditText inputPassEdtSec;

    private String passwordIpt = null;
    private String passwordIptSec = null;
    private String registerPhone= null;
    private String registerRole = null;

    private UserController userController = new UserController();
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        Intent intent = this.getIntent();
        registerPhone = intent.getStringExtra("registerPhone");
        registerRole = intent.getStringExtra("registerRole");

        inputPassEdt = (EditText) findViewById(R.id.inputPassEdit);
        inputPassEdtSec = (EditText) findViewById(R.id.inputPassEditSec);
        nextStep = (Button) findViewById(R.id.nextStepPsdEdt);

        passwordIpt = inputPassEdt.getText().toString().trim();
        passwordIptSec = inputPassEdtSec.getText().toString().trim();

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordIpt.equals("") || passwordIpt==null || passwordIptSec.equals("") || passwordIptSec==null){
                    Toast.makeText(EditPasswordActivity.this, "输入不能为空，请重新输入。", Toast.LENGTH_SHORT).show();
                }else if(!passwordIpt.equals(passwordIptSec)){
                    Toast.makeText(EditPasswordActivity.this, "两次密码输入不同，请重新输入。", Toast.LENGTH_SHORT).show();
                }else{
                    Intent toMainIntent = new Intent();
                    if(registerRole.equals("普通用户")){
                        user = userController.register(registerPhone,passwordIpt,registerRole);
                        toMainIntent.putExtra("userID",user.getId());
                        toMainIntent.setClass(EditPasswordActivity.this,PTPageActivity.class);
                    }else if(registerRole.equals("取送用户")){
                        user = userController.register(registerPhone,passwordIpt,registerRole);
                        toMainIntent.putExtra("userID",user.getId());
                        toMainIntent.setClass(EditPasswordActivity.this,QSPageActivity.class);
                    }else if(registerRole.equals("加工用户")){
//                        user = userController.register(registerPhone,passwordIpt,registerRole);
//                        toMainIntent.putExtra("userID",user.getId());
//                        toMainIntent.setClass(EditPasswordActivity.this,JGPageActivity.class);
                    }
                    startActivity(toMainIntent);
                }
            }
        });
    }
}
