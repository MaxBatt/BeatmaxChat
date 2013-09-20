package de.beatmax.chat;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import de.beatmax.chat.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatActivity extends Activity {

	private EditText chatField;
	private String username, partner;
	private ParseUser currentUser;
	private LinearLayout chatList;
	private LocalMessageDB db;
	private static final String MSG_INTENT = "de.beatmax.chat.message";
	IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		db = new LocalMessageDB(this.getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

		currentUser = ParseUser.getCurrentUser();

		username = currentUser.getUsername();
		partner = extras.getString("partner");

		// System.out.println("Username: " + username + " Partner: " + partner);

		chatField = (EditText) findViewById(R.id.chatField);
		chatList = (LinearLayout) findViewById(R.id.chatList);

		refreshChat();

		filter = new IntentFilter();
		filter.addAction(MSG_INTENT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("ChatActivity: Retrieved Intent from Broadcast");
			String author = intent.getExtras().getString("author");
			String message = intent.getExtras().getString("message");

			addMessage(author, message);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void send(View v) {

		final String message = chatField.getText().toString();

		if (message.length() == 0) {
			return;
		}

		addMessage(username, message);
		chatField.setText("");

		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();

		installation.saveInBackground();

		// Create our Installation query
		@SuppressWarnings("rawtypes")
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("channels", partner);

		try {

			HashMap<String, String> obj = new HashMap<String, String>();
			obj.put("action", "de.beatmax.chat.UPDATE_STATUS");
			obj.put("username", username);
			obj.put("alert", message);
			Gson gson = new Gson();
			final String json = gson.toJson(obj);
			JSONObject jsonObj = new JSONObject(json);

			ParsePush push = new ParsePush();
			push.setChannel(partner);
			push.setData(jsonObj);
			push.sendInBackground(new SendCallback() {
				public void done(ParseException e) {
					if (e == null) {
						System.out.println("Sender: Message sent from "
								+ username + " to " + partner + ": " + message);
						Message oMessage = new Message(partner, username,
								message);
						db.saveMessage(oMessage);

					} else {
						Log.d("push", "failure");
					}
				}
			});

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void refreshChat() {

		ArrayList<Message> messages = db.getAllMessages(partner);

		System.out.println("There are " + messages.size()
				+ " messages for this chat");

		for (Message m : messages) {
			addMessage(m.getAuthor(), m.getMessage());
		}

	}

	public void addMessage(String author, String message) {
		TextView tv1 = new TextView(this);
		tv1.setText(author + ": " + message);
		tv1.setTextSize(10);
		if (author == partner) {
			tv1.setGravity(Gravity.RIGHT);
		}
		chatList.addView(tv1);
	}

}
