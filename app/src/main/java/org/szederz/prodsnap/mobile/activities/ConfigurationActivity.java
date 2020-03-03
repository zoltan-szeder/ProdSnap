package org.szederz.prodsnap.mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.adapters.AsyncAdapter;
import org.szederz.prodsnap.services.ApplicationConfigurationService;
import org.szederz.prodsnap.utils.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;

import static org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs.showConfigurationFailure;

public class ConfigurationActivity extends AppCompatActivity {
  private ApplicationConfigurationService service = ApplicationSingleton.getService(ApplicationConfigurationService.class);

  private EditText host;
  private EditText user;
  private EditText password;
  private EditText createdUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_configuration);

    host = findViewById(R.id.item_details_server);
    user = findViewById(R.id.configuration_user);
    password = findViewById(R.id.configuration_password);
    createdUser = findViewById(R.id.new_user_name);
  }

  public void fetchConfiguration(final View view) {
    new AsyncAdapter(view).run(
      new Runnable() {
        @Override
        public void run() {
          fetchConfiguration();
        }
      },
      new Runnable() {
        @Override
        public void run() {
          onBackPressed();
        }
      },
      new Consumer<Exception>() {
        @Override
        public void accept(Exception e) {
          showConfigurationFailure(ConfigurationActivity.this);
        }
      }
    );
  }

  private void fetchConfiguration() {
    service.fetchConfiguration(
      host.getText().toString(),
      user.getText().toString(),
      password.getText().toString(),
      createdUser.getText().toString()
    );
  }

  @Override
  public void onBackPressed() {
    startActivity(new Intent(this, MainActivity.class));
  }
}
