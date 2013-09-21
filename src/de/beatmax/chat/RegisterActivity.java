package de.beatmax.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import de.beatmax.chat.R;

public class RegisterActivity extends Activity {

	private EditText editUsername, editPassword;
	private Button registerBtn;
	public static final String PREFS = "CHAT_PREFS";
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
		registerBtn = (Button) findViewById(R.id.registerButton);

		registerBtn.setEnabled(false);

		// Listener for any changes in the EditTest for Username
		// checks if username only contains letters and numbers
		// checks if username is empty

		editUsername.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			/**
			 * @params s Validates the Textfield for username everytime the user
			 *         types a character. Checks if the textfield is empty and
			 *         that the username only consists of letters and numbers.
			 */
			@Override
			public void afterTextChanged(Editable s) {

				// No username is typed in
				if (tvIsEmpty(editUsername)) {
					registerBtn.setEnabled(false);
				} else {
					if (!tvIsEmpty(editPassword)) {
						registerBtn.setEnabled(true);
					}
				}
			}
		});

		editPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			/**
			 * @params s Validates the Textfield for username everytime the user
			 *         types a character. Checks if the textfield is empty and
			 *         that the username only consists of letters and numbers.
			 */
			@Override
			public void afterTextChanged(Editable s) {

				// No username is typed in
				if (tvIsEmpty(editPassword)) {
					registerBtn.setEnabled(false);
				} else {
					if (!tvIsEmpty(editUsername)) {
						registerBtn.setEnabled(true);
					}
				}
			}
		});

	}

	public void register(View v) {

		// No username is typed in

		String username = editUsername.getText().toString();
		String password = editPassword.getText().toString();

		// username got invalid chars
		if (!username.matches("^[a-zA-Z0-9]+$")
				|| !password.matches("^[a-zA-Z0-9]+$")) {
			showToast("Bitte fŸr Benutzernamen und Passwort nur Buchstaben und Zahlen verwenden");
		} else {
			pd = ProgressDialog.show(this, "Bitte warten...",
					"Registrierung lŠuft.", true);
			signupUser(username, password);
		}

	}

	public void signupUser(String name, String pass) {
		final ParseUser user = new ParseUser();
		user.setUsername(name);
		user.setPassword(pass);
		final String username = name;
		final String password = pass;

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				RegisterActivity.this.pd.hide();
				if (e == null) {
					// Read Sared Prefs
					SharedPreferences settings = getSharedPreferences(PREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("username", username);
					editor.putString("password", password);
					editor.commit();
					showToast("Benutzer angelegt");
					
					Intent i = new Intent();
					 i.putExtra("username",username);
					 i.putExtra("password",password);
					 setResult(RESULT_OK,i);     
					 finish();
					
				} else {
					showToast(e.getMessage());
					Intent i = new Intent();
					setResult(RESULT_CANCELED, i);        
					finish();
				}
			}
		});
	}

	private boolean tvIsEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, 100);
		toast.setGravity(Gravity.TOP, 0, 600);
		toast.show();
	}
}
