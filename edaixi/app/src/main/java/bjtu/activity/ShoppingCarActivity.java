package bjtu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bjtu.adapter.ListViewAdapter;
import bjtu.controller.ProductController;
import bjtu.model.Product;

public class ShoppingCarActivity extends AppCompatActivity {
    private ListView productListView;
    private Button toCreateOrder;

    private List<Product> shoppingCarList = new ArrayList<>();
    private int userID = 0;
    private String categoryID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        Intent intent = this.getIntent();
        shoppingCarList = (List<Product>)intent.getSerializableExtra("shoppingCarList");
        categoryID = intent.getStringExtra("categoryID");

        userID = intent.getIntExtra("userID",0);

        productListView = (ListView) findViewById(R.id.productListView);
        productListView.setAdapter(new ListViewAdapter(ShoppingCarActivity.this,shoppingCarList));

        toCreateOrder = (Button) findViewById(R.id.toCreateOrder);
        toCreateOrder.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转到订单确认界面
                Intent createOrderIntent = new Intent(ShoppingCarActivity.this,CreateOrderActivity.class);
                createOrderIntent.putExtra("shoppingCarList",(Serializable) shoppingCarList);
                createOrderIntent.putExtra("userID",userID);
                createOrderIntent.putExtra("categoryID",categoryID);
                startActivity(createOrderIntent);
            }
        });
    }
}
