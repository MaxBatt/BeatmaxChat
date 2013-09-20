package de.beatmax.chat;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalMessageDB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "de.beatmax.chat";
	private static final int DATABASE_VERSION = 2;
	private static final String DICTIONARY_TABLE_NAME = "messages";
	private static final String CHAT_WITH = "chat_with";
	private static final String AUTHOR = "author";
	private static final String MESSAGE = "message";
	private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
			+ DICTIONARY_TABLE_NAME + " (" + CHAT_WITH + " TEXT, " + AUTHOR
			+ " TEXT, " + MESSAGE + " TEXT);";
	
	private SQLiteDatabase db;

	LocalMessageDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Message> getAllMessages(String chatWith) {
		
		db = this.getWritableDatabase();

		ArrayList<Message> messages = new ArrayList<Message>();


		String selectQuery = "SELECT * FROM messages WHERE chat_with like '"
				+ chatWith + "'";

		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("COUNT: " + cursor.getCount());

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				String cw = cursor.getString(0);
				String author = cursor.getString(1);
				String text = cursor.getString(2);

				Message message = new Message(cw, author, text);

				messages.add(message);

				// System.out.println("Author: " + author + " AND Message: " +
				// message);

			} while (cursor.moveToNext());

			cursor.close();
			db.close();
		}

		return messages;
	}
	
	public boolean saveMessage(Message message){
		
		db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("chat_with", message.getChatWith());
		values.put("author", message.getAuthor());
		values.put("message", message.getMessage());

		db.insert("messages", null, values);
		
		db.close();
		
		return false;
	}
}