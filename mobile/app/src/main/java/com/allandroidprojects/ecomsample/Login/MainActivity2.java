package com.allandroidprojects.ecomsample.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Login.Login_Fragment;
import com.allandroidprojects.ecomsample.Login.Utils;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.startup.MainActivity;

import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;


public class MainActivity2 extends AppCompatActivity {
	private static FragmentManager fragmentManager;
	public ChatClientInterface chatClientInterface;
	private MyReceiver myReceiver;
	static final int CHAT_REQUEST = 0;
	private Login_Fragment login;

	private Logger logger = Logger.getJADELogger(this.getClass().getName());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myReceiver = new MyReceiver();

		IntentFilter createAgent = new IntentFilter();
		createAgent.addAction("CreateAgent");
		registerReceiver(myReceiver,createAgent);
		IntentFilter invalid = new IntentFilter();
		invalid.addAction("invalid");
		registerReceiver(myReceiver,invalid);
		setContentView(R.layout.activity_main2);
		try {
			chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
					.getO2AInterface(ChatClientInterface.class);
		} catch (StaleProxyException e) {
			logger.log(Level.INFO, "auth************************************ " + 1);
		} catch (ControllerException e) {
			logger.log(Level.INFO, "auth************************************ " + 2);
		}
		fragmentManager = getSupportFragmentManager();

		// If savedinstnacestate is null then replace login fragment
		if (savedInstanceState == null) {
		   login=new Login_Fragment();
			fragmentManager
					.beginTransaction()
					.replace(R.id.frameContainer, login,
							Utils.Login_Fragment).commit();
		}

		// On close icon click finish activity
		findViewById(R.id.close_activity).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();

					}
				});

	}

	// Replace Login Fragment with animation
	protected void replaceLoginFragment() {
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
				.replace(R.id.frameContainer, new Login_Fragment(),
						Utils.Login_Fragment).commit();
	}

	@Override
	public void onBackPressed() {

		// Find the tag of signup and forgot password fragment
		Fragment SignUp_Fragment = fragmentManager
				.findFragmentByTag(Utils.SignUp_Fragment);
		Fragment ForgotPassword_Fragment = fragmentManager
				.findFragmentByTag(Utils.ForgotPassword_Fragment);

		// Check if both are null or not
		// If both are not null then replace login fragment else do backpressed
		// task

		if (SignUp_Fragment != null)
			replaceLoginFragment();
		else if (ForgotPassword_Fragment != null)
			replaceLoginFragment();
		else
			super.onBackPressed();
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			logger.log(Level.INFO, "Received intent " + action);
			if (action.equalsIgnoreCase("jade.demo.chat.KILL")) {
				finish();
			}


			if (action.equals("CreateAgent")) {
				MainActivity.curentype=0;
				Intent showChat = new Intent(MainActivity2.this,
						MainActivity.class);
				showChat.putExtra("nickname",MainActivity1.nickname);
                 login.valid();
				MainActivity2.this
						.startActivityForResult(showChat, CHAT_REQUEST);
			}
			else if(action.equals("invalid"))
			{

					login.invalid();
			}

		}
	}
}
