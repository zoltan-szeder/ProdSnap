package org.szederz.prodsnap.mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.configuration.Constants;
import org.szederz.prodsnap.configuration.android.SharedPreferencesConfiguration;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.mobile.adapters.AsyncAdapter;
import org.szederz.prodsnap.mobile.dialogs.ProdSnapDialogs;
import org.szederz.prodsnap.services.ConfigurationService;
import org.szederz.prodsnap.services.StorageCacheService;
import org.szederz.prodsnap.utils.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;

public class CacheRebuildActivity extends AppCompatActivity {
  private StorageCacheService cacheService = ApplicationSingleton.getService(StorageCacheService.class);
  private ConfigurationService configurationService = ApplicationSingleton.getService(ConfigurationService.class);
  private View cacheRebuildText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SharedPreferencesConfiguration.getInstance()
      .setPreferences(getSharedPreferences("application", Context.MODE_PRIVATE));

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cache_rebuild);

    this.cacheRebuildText = findViewById(R.id.cache_rebuild_text);
  }

  public void onStart() {
    super.onStart();

    if (applicationIsInitialized())
      rebuildCache(cacheRebuildText);

    startActivity(new Intent(this, MainActivity.class));
  }

  private boolean applicationIsInitialized() {
    return configurationService.contains(Constants.CONF_STORAGE_DETAILS_HTTP_URL);
  }

  public void rebuildCache(View view) {
    System.err.println("Hello Rebuild");
    System.err.println(findViewById(R.id.cache_rebuild_text));
    new AsyncAdapter(view).run(
      new Runnable() {
        @Override
        public void run() {
          cacheService.rebuild();
        }
      },
      new Runnable() {
        @Override
        public void run() {
          startActivity(new Intent(CacheRebuildActivity.this, MainActivity.class));
        }
      },
      new Consumer<Exception>() {
        @Override
        public void accept(Exception e) {
          e.printStackTrace();
          ProdSnapDialogs.showCacheRebuildFailure(
            CacheRebuildActivity.this,
            new Runnable() {
              @Override
              public void run() {
                startActivity(new Intent(CacheRebuildActivity.this, MainActivity.class));
              }
          });
        }
      }
    );
  }
}
