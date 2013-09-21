package de.beatmax.chat;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends ListActivity implements OnItemClickListener {

	private ParseUser currentUser;
	public static final String PREFS = "CHAT_PREFS";
	private String username, password;
	private UserListAdapter adapter;
	private List<ParseUser> userList;
	public String partner;
	private boolean loggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			loggedIn = savedInstanceState.getBoolean("loggedIn");
		}
		loggedIn = false;

		// Read Sared Prefs
		SharedPreferences settings = getSharedPreferences(PREFS, 0);

		// Delete Userdata in preferences if necessary
		// SharedPreferences.Editor editor = settings.edit();
		// editor.remove("username");
		// editor.remove("password");
		// editor.commit();

		// Get Userdata from Shared Prefs
		username = settings.getString("username", null);
		password = settings.getString("password", null);

		// System.out.println("USer: " + username + "PW: " + password);

		if (username == null || password == null) {
			Intent i = new Intent(this, RegisterActivity.class);
			startActivityForResult(i, 1);
		} else {
			if (!loggedIn) {
				loginUser();
			}
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putBoolean("loggedIn", loggedIn);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// If user not created yet, go to RegisterActivity

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				username = data.getStringExtra("username");
				password = data.getStringExtra("password");

				loginUser();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent i = new Intent(this, ChatActivity.class);
		i.putExtra("username", username);
		i.putExtra("partner", userList.get(position).getUsername());
		startActivity(i);

	}

	private void loginUser() {
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Hooray! The user is logged in.
					currentUser = ParseUser.getCurrentUser();
					loggedIn = true;
					
					getUsers();

					PushService.subscribe(getApplicationContext(), username,
							ChatActivity.class);
					
					showToast("Du bist eingeloggt, "
							+ currentUser.getUsername() + "!");
				} else {
					// Signup failed. Look at the ParseException to see what
					// happened.
				}
			}
		});
	}

	private void getUsers() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereNotEqualTo("username", username);
		query.orderByAscending("username");
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> userList, ParseException e) {
				if (e == null) {
					// The query was successful.

					MainActivity.this.userList = userList;

					// Create ListAdapter
					adapter = new UserListAdapter(MainActivity.this, userList);
					setListAdapter(adapter);

					// Fill ListView
					ListView listView = getListView();

					// OnItemClickListener for Click on an item in list
					listView.setOnItemClickListener(MainActivity.this);
				} else {
					// Something went wrong.
					MainActivity.this.userList = null;
				}
			}
		});

	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 600);
		toast.show();
	}

}
