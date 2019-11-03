package com.example.onmymeans;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onmymeans.model.BookItem;
import com.example.onmymeans.model.IOItem;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Button addCostBtn;
    private Button addEarnBtn;
    private Button clearBtn;
    private ImageButton addFinishBtn;
    private ImageButton addDescription;


    private ImageView bannerImage;
    private TextView bannerText;

    private TextView moneyText;

    private TextView words;

    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private SimpleDateFormat formatSum = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addCostBtn = (Button) findViewById(R.id.add_cost_button);
        addEarnBtn = (Button) findViewById(R.id.add_earn_button);
        addFinishBtn = (ImageButton) findViewById(R.id.add_finish);
        addDescription = (ImageButton) findViewById(R.id.add_description);
        clearBtn = (Button) findViewById(R.id.clear);
      words = (TextView) findViewById(R.id.anime_words);
        // 设置字体颜色
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/chinese_character.ttf");
        clearBtn.setTypeface(typeface);
        words.setTypeface(typeface);
        // 设置按钮监听
        addCostBtn.setOnClickListener(new ButtonListener());
        addEarnBtn.setOnClickListener(new ButtonListener());
        addFinishBtn.setOnClickListener(new ButtonListener());
        addDescription.setOnClickListener(new ButtonListener());
        clearBtn.setOnClickListener(new ButtonListener());


        bannerText = (TextView) findViewById(R.id.chosen_title);
        bannerImage = (ImageView) findViewById(R.id.chosen_image);

        moneyText = (TextView) findViewById(R.id.input_money_text);
        // 及时清零
        moneyText.setText("0.00");

        manager = getSupportFragmentManager();

        transaction = manager.beginTransaction();
        transaction.replace(R.id.item_fragment, new com.example.onmymeans.CostFragment());
        transaction.commit();

    }

    // 计算余额
    public void calculateMonthlyMoney(BookItem bookItem, int money_type, IOItem IOItem) {
        String sumDate = formatSum.format(new Date());

        // 求取月收支类型
        if (bookItem.getDate().equals(IOItem.getTimeStamp().substring(0, 8))) {
            if (money_type == 1) {
                bookItem.setSumMonthlyEarn(bookItem.getSumMonthlyEarn() + IOItem.getMoney());
            } else {
                bookItem.setSumMonthlyCost(bookItem.getSumMonthlyCost() + IOItem.getMoney());
            }
        } else {
            if (money_type == 1) {
                bookItem.setSumMonthlyEarn(IOItem.getMoney());
                bookItem.setSumMonthlyCost(0.0);
            } else {
                bookItem.setSumMonthlyCost(IOItem.getMoney());
                bookItem.setSumMonthlyEarn(0.0);
            }
            bookItem.setDate(sumDate);
        }

        bookItem.save();
    }

    // 数字输入
    public void calculatorNumOnclick(View v) {
        Button view = (Button) v;
        String digit = view.getText().toString();
        String money = com.example.onmymeans.GlobalVariables.getmInputMoney();
        if (com.example.onmymeans.GlobalVariables.getmHasDot() && com.example.onmymeans.GlobalVariables.getmInputMoney().length() > 2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            Log.d(TAG, "calculatorNumOnclick: " + dot);
            if (dot.equals(".")) {
                Toast.makeText(getApplicationContext(), "唔，已经不能继续输入了", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        com.example.onmymeans.GlobalVariables.setmInputMoney(money + digit);
        moneyText.setText(decimalFormat.format(Double.valueOf(com.example.onmymeans.GlobalVariables.getmInputMoney())));
    }

    // 清零按钮
    public void calculatorClear() {
        com.example.onmymeans.GlobalVariables.setmInputMoney("");
        com.example.onmymeans.GlobalVariables.setHasDot(false);
    }
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            transaction = manager.beginTransaction();

            switch (view.getId()) {
                case R.id.add_cost_button:
                    addCostBtn.setTextColor(0xffff8c00); // 设置“支出“按钮为灰色
                    addEarnBtn.setTextColor(0xff908070); // 设置“收入”按钮为橙色
                    transaction.replace(R.id.item_fragment, new com.example.onmymeans.CostFragment());
                    Log.d(TAG, "onClick: add_cost_button");

                    break;
                case R.id.add_earn_button:
                    addEarnBtn.setTextColor(0xffff8c00); // 设置“收入“按钮为灰色
                    addCostBtn.setTextColor(0xff908070); // 设置“支出”按钮为橙色
                    transaction.replace(R.id.item_fragment, new com.example.onmymeans.EarnFragment());
                    Log.d(TAG, "onClick: add_earn_button");

                    break;
                case R.id.add_finish:
                    String moneyString = moneyText.getText().toString();
                    if (moneyString.equals("0.00") || com.example.onmymeans.GlobalVariables.getmInputMoney().equals(""))
                        Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
                    else {
                        putItemInData(Double.parseDouble(moneyText.getText().toString()));
                        calculatorClear();
                        finish();
                    }
                    break;
                case R.id.clear:
                    calculatorClear();
                    moneyText.setText("0.00");
                    break;
                case R.id.add_description:
                    Intent intent = new Intent(AddItemActivity.this, AddDescription.class);
                    startActivity(intent);
            }

            transaction.commit();
        }
    }
    public void putItemInData(double money) {
        IOItem IOItem = new IOItem();
        BookItem bookItem = DataSupport.find(BookItem.class, com.example.onmymeans.GlobalVariables.getmBookId());
        String tagName = (String) bannerText.getTag();
        int tagType = (int) bannerImage.getTag();

        if (tagType < 0) {
            IOItem.setType(IOItem.TYPE_COST);
        } else IOItem.setType(IOItem.TYPE_EARN);

        IOItem.setName(bannerText.getText().toString());
        IOItem.setSrcName(tagName);
        IOItem.setMoney(money);
        IOItem.setTimeStamp(formatItem.format(new Date()));         // 存储记账时间
        IOItem.setDescription(com.example.onmymeans.GlobalVariables.getmDescription());
        IOItem.setBookId(com.example.onmymeans.GlobalVariables.getmBookId());
        IOItem.save();

        // 将收支存储在对应账本下
        bookItem.getIOItemList().add(IOItem);
        bookItem.setSumAll(bookItem.getSumAll() + money * IOItem.getType());
        bookItem.save();

        calculateMonthlyMoney(bookItem, IOItem.getType(), IOItem);

        // 存储完之后及时清空备注
        com.example.onmymeans.GlobalVariables.setmDescription("");
    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        if (com.example.onmymeans.GlobalVariables.getmHasDot()) {
            Toast.makeText(getApplicationContext(), "已输入过小数点", Toast.LENGTH_SHORT).show();
        } else {
            com.example.onmymeans.GlobalVariables.setmInputMoney(com.example.onmymeans.GlobalVariables.getmInputMoney() + ".");
            com.example.onmymeans.GlobalVariables.setHasDot(true);
        }
    }
}
