package com.ylqhust.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ylqhust.adapter.ViewPagerAdapter;
import com.ylqhust.api.V2EX;
import com.ylqhust.v2ex.Global;
import com.ylqhust.v2ex.R;

/**
 * Show for user to login
 */
public class Login extends Fragment implements View.OnClickListener ,V2EX.UpdateUserPage{

    private EditText mUsername;
    private EditText mPassword;
    private Button mButton;

    private String u;
    private String p;
    private ViewPagerAdapter.SetUserPage setUserPage;
    public void setSetUserPage(ViewPagerAdapter.SetUserPage setUserPage)
    {
        this.setUserPage = setUserPage;
    }
    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mUsername = (EditText) view.findViewById(R.id.fragment_login_username);
        mPassword = (EditText) view.findViewById(R.id.fragment_login_password);
        mButton = (Button) view.findViewById(R.id.fragment_login_button);

        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id)
        {
            case R.id.fragment_login_button:
                login();
                break;
            default:
                Log.i("ERROR", "ERROR_ID");
                break;
        }
        return;
    }

    private void login() {
        final String username = mUsername.getText().toString().trim();
        if (username == null || username.length() == 0)
        {
            mUsername.setError("用户名不能为空");
            return;
        }
        final String password = mPassword.getText().toString().trim();

        if (password == null || password.length() == 0)
        {
            mPassword.setError("密码不能为空");
            return;
        }
        u = username;
        p = password;
        Global.v2EXManager.LoginBefore(username,password);
        Global.v2EXManager.setUpdateUserPage(this);
    }


    @Override
    public void UUP(String string) {
        if (string == null)
        {
            Toast.makeText(getActivity(),"登陆失败",Toast.LENGTH_SHORT).show();
            setUserPage.setUserPage(null);
        }
        else
        {
            Toast.makeText(getActivity(),"登陆成功",Toast.LENGTH_SHORT).show();
            Global.isLogin = true;
            Global.ClearAccountInfo();
            Global.SaveAccountInfo(u,p);
            setUserPage.setUserPage(string);
        }
    }
}
