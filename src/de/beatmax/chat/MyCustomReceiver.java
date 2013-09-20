package de.beatmax.chat;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyCustomReceiver extends BroadcastReceiver {
	private static final String TAG = "MyCustomReceiver";
	public static final String MSG_INTENT = "de.beatmax.chat.message";
	private LocalMessageDB db;

	@Override
	public void onReceive(Context context, Intent intent) {
		db = new LocalMessageDB(context.getApplicationContext());
		try {
			String json = intent.getExtras().getString("com.parse.Data");

			JSONObject jsonObj = new JSONObject(json);

			String partner = jsonObj.getString("username");
			String msg = jsonObj.getString("alert");

			System.out.println("Receiver: received a message from " + partner);

			Message message = new Message(partner, partner, msg);
			db.saveMessage(message);
			System.out.println("Receiver: saved Message to database");

			Intent i = new Intent();
			i.putExtra("author", partner);
			i.putExtra("message", msg);
			i.setAction(MSG_INTENT);
			context.sendBroadcast(i);
			System.out.println("Receiver: sent Broadcast to ChatActivity.");

		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}