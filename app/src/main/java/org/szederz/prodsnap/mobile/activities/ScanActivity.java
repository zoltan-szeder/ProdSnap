package org.szederz.prodsnap.mobile.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.PermissionHandler;
import org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs;
import org.szederz.prodsnap.services.BasketService;
import org.szederz.prodsnap.services.ItemDetailService;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private BasketService service = ApplicationSingleton.getService(BasketService.class);
    private ItemDetailService itemDetailService = ApplicationSingleton.getService(ItemDetailService.class);
    private EditText scanValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scanValue = findViewById(R.id.scanner_scan_value);

        initializeBarCodeScanner();
    }

    private void initializeBarCodeScanner() {
        PermissionHandler.grantPermissionTo(this, Manifest.permission.CAMERA);
        ViewGroup viewGroup = findViewById(R.id.scanner_scan_zone);
        scannerView = new ZXingScannerView(viewGroup.getContext());
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        viewGroup.addView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    public void onSubmitBarCode(View view) {
        addPendingItemAndGoToNextActivity(scanValue.getText().toString());
    }

    @Override
    public void handleResult(Result result) {
        if (result.getBarcodeFormat() == BarcodeFormat.EAN_13)
            addPendingItemAndGoToNextActivity(result.getText());
    }

    private void addPendingItemAndGoToNextActivity(String barcode) {
        addPendingItemWithId(barcode);
        nextActivity(DetailsActivity.class);
    }

    private void addPendingItemWithId(String id) {
        BasketItem item = new BasketItem();

        item.setId(id);

        service.addPending(item);
    }

    @Override
    public void onBackPressed() {
        nextActivity(MainActivity.class);
    }

    private void nextActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void onDiscardCustomer(final View view) {
        ProdSnapDialogs.showDiscardCustomerDialog(this);
    }

    public void onGoToBasket(View view) {
        startActivity(new Intent(this, BasketActivity.class));
    }
}
