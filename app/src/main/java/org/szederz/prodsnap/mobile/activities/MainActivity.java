package org.szederz.prodsnap.mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.configuration.android.SharedPreferencesConfiguration;
import org.szederz.prodsnap.entities.Customer;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs;
import org.szederz.prodsnap.services.CustomerService;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
  private CustomerService service = ApplicationSingleton.getService(CustomerService.class);
  private EditText details;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SharedPreferencesConfiguration.getInstance()
      .setPreferences(getSharedPreferences("application", Context.MODE_PRIVATE));

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    details = findViewById(R.id.customer_details);

    initializeActivity();
  }

  private void initializeActivity() {
    String details = service.getCustomer().getDetails();
    if (!details.isEmpty()) {
      this.details.setText(details);
    }
  }

  public void onSubmitCustomerDetails(View view) {
    String customerDetails = details.getText().toString();

    if (customerDetails.isEmpty()) {
      ProdSnapDialogs.showMissingCustomerDetails(this);
      return;
    }

    service.setCustomer(createCustomerDetails(customerDetails));

    startActivity(new Intent(this, ScanActivity.class));
  }

  public void onConfigure(View view) {
    startActivity(new Intent(this, ConfigurationActivity.class));
  }

  private Customer createCustomerDetails(String customerDetails) {
    Customer customer = new Customer();

    customer.setDetails(customerDetails);

    return customer;
  }

  @Override
  public void onBackPressed() {
  }
}
