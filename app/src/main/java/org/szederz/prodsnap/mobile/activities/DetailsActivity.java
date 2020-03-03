package org.szederz.prodsnap.mobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs;
import org.szederz.prodsnap.services.BasketService;
import org.szederz.prodsnap.services.ItemDetailService;

import androidx.appcompat.app.AppCompatActivity;

import static org.szederz.prodsnap.utils.ThreadUtils.async;

public class DetailsActivity extends AppCompatActivity {
  private BasketService basketService = ApplicationSingleton.getService(BasketService.class);
  private ItemDetailService itemDetailService = ApplicationSingleton.getService(ItemDetailService.class);
  private EditText count;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    setContentView(R.layout.activity_details);

    this.count = findViewById(R.id.details_item_count);

    async(new DetailsActivityInitializer(this, new Handler()));
  }

  public void onBackPressed(View view) {
    onBackPressed();
  }

  @Override
  public void onBackPressed() {
    startActivity(new Intent(this, ScanActivity.class));
  }

  public void onSubmitAndGoToScan(View view) {
    submitPendingItemAndGoToNextActivity(ScanActivity.class);
  }

  public void onSubmitAndGoToCart(View view) {
    submitPendingItemAndGoToNextActivity(BasketActivity.class);
  }

  private void submitPendingItemAndGoToNextActivity(Class<? extends Activity> activity) {
    String amountString = count.getText().toString();
    int amount = 1;
    if (!amountString.isEmpty()) {
      amount = Integer.parseInt(amountString);
    } else {
    }
    basketService.saveAmountOfPendingItem(amount);
    startActivity(new Intent(this, activity));
  }

  public class DetailsActivityInitializer implements Runnable {
    private final DetailsActivity activity;
    private final Handler handler;

    public DetailsActivityInitializer(DetailsActivity activity, Handler handler) {
      this.activity = activity;
      this.handler = handler;
    }

    @Override
    public void run() {
      final ItemDetails details = itemDetailService.load();

      handler.post(new Runnable() {
        @Override
        public void run() {
          if (details == null) {
            ProdSnapDialogs.showInvalidBasketItem(activity);
            return;
          }

          TextView itemName = activity.findViewById(R.id.details_item_name);
          itemName.setText(details.getName());

          TextView itemPrice = activity.findViewById(R.id.details_item_price);
          itemPrice.setText(details.getFormattedPrice());
        }
      });
    }
  }
}
