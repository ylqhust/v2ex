package com.ylqhust.v2ex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ylqhust.adapter.LatestListViewAdapter;
import com.ylqhust.adapter.ViewPagerAdapter;
import com.ylqhust.api.V2EX;
import com.ylqhust.cache.DiskCache;
import com.ylqhust.contract.MemberScheam;
import com.ylqhust.fragment.Account;
import com.ylqhust.fragment.AutoComplete;
import com.ylqhust.fragment.LocalHistory;
import com.ylqhust.fragment.Login;
import com.ylqhust.fragment.UserPager;
import com.ylqhust.json.GetJSONDataTask;
import com.ylqhust.contract.LatestScheam;
import com.ylqhust.json.GetJSONObjectTask;
import com.ylqhust.json.ResolveLatestJSONArray;
import com.ylqhust.json.ResolveMemberJSONObject;
import com.ylqhust.utils.GetAdvices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AutoComplete.AutoCompleteText2,ViewPagerAdapter.SetUserPage{
    private ViewPager viewpager;
    private EditText search_edittext;
    private ImageView search_image;
    private List<View> lists;
    private LayoutInflater inflater;
    final private FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initGlobal();

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        inflater = getLayoutInflater().from(this);
        lists = new ArrayList<View>();
        View latestView = inflater.inflate(R.layout.latest_layout,null);
        View searchView = inflater.inflate(R.layout.search_layout,null);
        View userView = inflater.inflate(R.layout.fragment_user,null);

        //获取搜索框
        search_edittext = (EditText) searchView.findViewById(R.id.search_edit);
        //获取搜索按钮
        search_image = (ImageView) searchView.findViewById(R.id.search_image);
        //监听文字改变
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println("before change :" + s + " start:" + start + " count:" + count + " after:" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println("on change :"+s+" start:"+start+" before:"+before+" count:"+count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //判断是否已选择，避免Fragment移除后又新建了一个，
                if (Global.IfChosed) {
                    //重置已选择为假，为下一次选择准备
                    Global.IfChosed = false;
                    return;
                }
                String string = s.toString();
                //System.out.println("after change :"+string);
                //获取补全字符串
                List<String> advices = Global.getAdvices.getAdvice(string);
                AutoComplete autoComplete = new AutoComplete(advices);
                autoComplete.setAutoCompleteText2(MainActivity.this);
                removeAllFragment();
                fm.beginTransaction().add(R.id.auto_complete_container, autoComplete).commit();
            }
        });

        //监听搜索事件
        search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入
                final String input = search_edittext.getText().toString().trim();
                Log.i("IMAGE_CLICK", input);
                //首先根据输入尝试获得用户主页
                //构造url
                String urlString = Global.SHOW_URL + input;
                GetJSONObjectTask getJSONObjectTask = new GetJSONObjectTask();
                getJSONObjectTask.setUpdateUI(new GetJSONObjectTask.UpdateUI() {
                    @Override
                    public void updateUI(JSONObject jsonObject) {
                        MemberScheam memberScheam = ResolveMemberJSONObject.resolve(jsonObject);
                        if (jsonObject != null && memberScheam != null) {
                            //移除所有Fragment
                            removeAllFragment();
                            //设置新的用户Fragment
                            UserPager userpager = new UserPager(memberScheam);
                            fm.beginTransaction().add(R.id.user_pager_container, userpager).commit();
                        } else {
                            //移除所有Fragment
                            removeAllFragment();
                            //获取用户界面失败，尝试根据输入获取本地数据
                            LocalHistory localHistory = new LocalHistory(Global.getAdvices.FilterByInput(input));
                            fm.beginTransaction().add(R.id.user_pager_container, localHistory).commit();
                        }
                    }
                });
                getJSONObjectTask.execute(urlString);
            }
        });
        //添加view到lists
        lists.add(latestView);
        lists.add(searchView);
        lists.add(userView);
        //设置ViewPager的适配器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(lists);
        viewPagerAdapter.setSetUserPage(this);
        viewpager.setAdapter(viewPagerAdapter);

        //初始化界面
        initLatestTopic();


    }



    private void initLatestTopic()
    {
        GetJSONDataTask getLatestTask = new GetJSONDataTask();
        getLatestTask.setUpdateUI(new GetJSONDataTask.UpdateUI() {
            @Override
            public void updateUI(JSONArray jsonArray) {
                if (jsonArray == null) {
                    Toast.makeText(getApplicationContext(), "检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    List<LatestScheam> latestScheamLists = ResolveLatestJSONArray.resolve(jsonArray);
                    Global.getAdvices.setLatestScheams(latestScheamLists);
                    LatestListViewAdapter latestListViewAdapter = new LatestListViewAdapter(inflater, latestScheamLists);
                    ((ListView) (lists.get(0).findViewById(R.id.listView))).setAdapter(latestListViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getLatestTask.execute("https://www.v2ex.com/api/topics/latest.json");
    }


    private void initGlobal()
    {
        Global.diskCache = DiskCache.getInstance();
        Global.getAdvices = GetAdvices.getInstance();
        Global.activity = MainActivity.this;
        Global.progressDialogSpinner = new ProgressDialog(this);
        Global.progressDialogSpinner.setTitle("提示信息");
        Global.progressDialogSpinner.setMessage("正在获取数据...");
        Global.progressDialogSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Global.v2EXManager = new V2EX(this);
        Global.AutoLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            case R.id.action_refresh:
                initLatestTopic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //用于将选择的字符串添加到EditText中，并销毁Fragment
    @Override
    public void complete(String string)
    {
        if (this.search_edittext == null)
            return;
        //设置已选择为真
        Global.IfChosed = true;
        //设置字符串
        search_edittext.setText(string);
        //移除自动补全的Fragment
        Fragment fragment = fm.findFragmentById(R.id.auto_complete_container);
        fm.beginTransaction().remove(fragment).commit();
        return;
    }


    private void removeAllFragment()
    {
        Fragment fragment1 = fm.findFragmentById(R.id.auto_complete_container);
        Fragment fragment2 = fm.findFragmentById(R.id.user_pager_container);
        if (fragment1 != null)
            fm.beginTransaction().remove(fragment1).commit();
        if (fragment2 != null)
            fm.beginTransaction().remove(fragment2).commit();
    }


    @Override
    public void setUserPage(String string) {
        if (Global.isLogin && string != null)
        {
            Fragment existFragment = fm.findFragmentById(R.id.fragment_user_container);
            if (existFragment != null)
                fm.beginTransaction().remove(existFragment).commit();
            Account account = new Account(string);
            account.setSetUserPage(this);
            fm.beginTransaction().add(R.id.fragment_user_container,account).commit();
        }
        else
        {
            Fragment existFragment = fm.findFragmentById(R.id.fragment_user_container);
            if (existFragment != null)
                fm.beginTransaction().remove(existFragment).commit();
            Login login = new Login();
            login.setSetUserPage(this);
            fm.beginTransaction().add(R.id.fragment_user_container,login).commit();
        }
    }
}
