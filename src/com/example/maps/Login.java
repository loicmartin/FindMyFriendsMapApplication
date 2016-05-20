package com.example.maps;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

@EActivity(R.layout.activity_login)
@OptionsMenu(R.menu.login)
public class Login extends Activity {
	@ViewById(R.id.user_email)
	EditText login;
	@ViewById(R.id.user_password)
	EditText password;
	User userLogin;
	@RestService
	UserManager userClient;
	@Background
	void acceptLogin()
	{
		Account acc= new Account(login.getText().toString(),password.getText().toString());
		userLogin = userClient.login(acc);
		redirectToGroups();
	}
	
	@UiThread
	void redirectToGroups()
	{
		Log.v("redirectToGroups.userLogin",userLogin.toString());
		Groups_.intent(this).currentUser(userLogin).start();
	}
	
	@OptionsItem(R.id.action_connect)
    void connect() {
		acceptLogin();
		
    }
}
