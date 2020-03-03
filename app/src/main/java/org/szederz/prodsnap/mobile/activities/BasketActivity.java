package org.szederz.prodsnap.mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.adapters.AsyncAdapter;
import org.szederz.prodsnap.mobile.adapters.BasketItemAdapter;
import org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs;
import org.szederz.prodsnap.services.BasketService;
import org.szederz.prodsnap.services.BasketTransferService;
import org.szederz.prodsnap.services.transfer.EmailBasketTransferService;
import org.szederz.prodsnap.utils.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BasketActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private BasketService service;
  private BasketTransferService transferService;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);

    initializeLayout();
  }

  private void initializeLayout() {
    this.service = ApplicationSingleton.getService(BasketService.class);
    this.transferService = ApplicationSingleton.getService(EmailBasketTransferService.class);
    recyclerView = findViewById(R.id.basket_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new BasketItemAdapter());

    TextView itemCounter = findViewById(R.id.basket_count);
    itemCounter.setText(String.valueOf(service.getBasketItemCount()));
  }

  @Override
  public void onBackPressed() {
    startActivity(new Intent(this, ScanActivity.class));
  }

  public void onGoToScan(View view) {
    startActivity(new Intent(this, ScanActivity.class));
  }

  public void onDropBasket(View view) {
    ProdSnapDialogs.showDiscardCustomerDialog(this);
  }

  public void onSendToServer(View view) {
    final Basket basket = service.getBasket();

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
      .permitAll().build();
    StrictMode.setThreadPolicy(policy);

    new AsyncAdapter(view)
      .run(
        new Runnable() {
          @Override
          public void run() {
            transferService.send(basket);
          }
        },
        new Runnable() {
          @Override
          public void run() {
            service.dropBasket();
            startActivity(new Intent(BasketActivity.this, MainActivity.class));
          }
        },
        new Consumer<Exception>() {
          @Override
          public void accept(Exception e) {
            e.printStackTrace();
            ProdSnapDialogs.showEmailFailure(BasketActivity.this);
          }
        }
      );
  }
}
