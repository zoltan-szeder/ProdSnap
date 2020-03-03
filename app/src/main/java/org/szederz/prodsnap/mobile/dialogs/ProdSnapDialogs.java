package org.szederz.prodsnap.mobile.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.activities.MainActivity;
import org.szederz.prodsnap.mobile.activities.ScanActivity;
import org.szederz.prodsnap.services.BasketService;

public class ProdSnapDialogs {
    public static void showDiscardCustomerDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.discard_basket)
                .setPositiveButton(R.string.yes, new DropBasketListener(activity))
                .setNegativeButton(R.string.no, null)
                .show();

    }

    public static void showInvalidBasketItem(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.invalid_basket_item)
                .setPositiveButton(R.string.ok, new BackToScanListener(activity))
                .show();

    }

    public static void showMissingCustomerDetails(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.customer_details_missing)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showEmailFailure(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.could_not_transfer_basket)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showCacheRebuildFailure(final Activity activity, final Runnable onOk) {
        new AlertDialog.Builder(activity)
          .setMessage(R.string.could_not_rebuild_cache)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                onOk.run();
              }
          })
          .show();
    }

    public static void showConfigurationFailure(final Activity activity) {
        new AlertDialog.Builder(activity)
          .setMessage(R.string.could_not_fetch_configuration)
          .setPositiveButton(R.string.ok, null)
          .show();
    }

    private static class DropBasketListener implements DialogInterface.OnClickListener {
        private final Activity activity;

        DropBasketListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            BasketService service = ApplicationSingleton.getService(BasketService.class);
            service.dropBasket();
            activity.startActivity(new Intent(activity, MainActivity.class));
        }
    }

    private static class BackToScanListener implements DialogInterface.OnClickListener {
        private final Activity activity;

        public BackToScanListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            activity.startActivity(new Intent(activity, ScanActivity.class));
        }
    }
}
