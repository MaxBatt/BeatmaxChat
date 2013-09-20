package de.beatmax.chat;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class Application extends android.app.Application {

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

	// Initialize the Parse SDK.
	Parse.initialize(this, "OV7XxKkKhMg8noKWBzE0EKyX5haWKCdv5lZo9wnK", "7dQkTcVOsanWYIgfzS4BHjmKp5zVMLZdplkHlEl9"); 

	// Specify a Activity to handle all pushes by default.
	PushService.setDefaultPushCallback(this, MainActivity.class);
	
	// Save the current installation.
	ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}