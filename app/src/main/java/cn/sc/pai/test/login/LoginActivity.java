package cn.sc.pai.test.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pai.mvp.view.PaiActivity;
import cn.sc.pai.test.R;


/**
 * activity 登录
 * author：pany
 * on 2017/12/1 11:14
 */
public class LoginActivity extends PaiActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etUserPwd)
    EditText etUserPwd;

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginContract.Presenter bindPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void layoutView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btnLogin)
    void onClick(View v) {
        presenter.reqLogin();
    }

    @Override
    public String getUser() {
        return etUserName.getText().toString();
    }

    @Override
    public String getPwd() {
        return etUserPwd.getText().toString();
    }
}
