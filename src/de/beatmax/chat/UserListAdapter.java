package de.beatmax.chat;


import java.util.ArrayList;
import java.util.List;

import com.parse.ParseUser;

import de.beatmax.chat.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class UserListAdapter extends ArrayAdapter<ParseUser> {
	private final Context context;
	private final List<ParseUser> userList;
	private TextView tvUser;

	

	// Build ListAdapter from ArrayList
	public UserListAdapter(Context context,
			List<ParseUser> userList) {
		super(context, R.layout.user_row, userList);
		
		this.context = context;
		this.userList = userList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Fill ListRow
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.user_row, parent, false);
				
		
		// Put image in row via WebCachedImageView
		
		tvUser = (TextView) rowView.findViewById(R.id.tvUsername);
		
		tvUser.setText(userList.get(position).getString("username"));
		
		
		// Return ListRow.
		return rowView;

	}
}
