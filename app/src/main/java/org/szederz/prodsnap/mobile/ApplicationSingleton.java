package org.szederz.prodsnap.mobile;

import org.szederz.prodsnap.configuration.BarcodeCache;
import org.szederz.prodsnap.configuration.ConfigurationSupplier;
import org.szederz.prodsnap.configuration.EmailConfiguration;
import org.szederz.prodsnap.configuration.android.SharedPreferencesConfiguration;
import org.szederz.prodsnap.services.ApplicationConfigurationService;
import org.szederz.prodsnap.services.BasketService;
import org.szederz.prodsnap.services.ConfigurationService;
import org.szederz.prodsnap.services.CustomerService;
import org.szederz.prodsnap.services.ItemDetailService;
import org.szederz.prodsnap.services.StorageCacheService;
import org.szederz.prodsnap.services.formatters.barcode.EmailSessionFactory;
import org.szederz.prodsnap.services.formatters.html.BasketItemFormatter;
import org.szederz.prodsnap.services.formatters.html.HtmlBasketFormatterWrapper;
import org.szederz.prodsnap.services.formatters.html.HtmlDivBasketFormatter;
import org.szederz.prodsnap.services.transfer.BasketMimeMessageFormatter;
import org.szederz.prodsnap.services.transfer.EmailBasketTransferService;
import org.szederz.prodsnap.storage.BasketStorage;
import org.szederz.prodsnap.storage.ItemDetailStorage;
import org.szederz.prodsnap.storage.details.CachedItemDetailStorage;
import org.szederz.prodsnap.storage.details.http.HttpItemDetailStorage;
import org.szederz.prodsnap.utils.Container;
import org.szederz.prodsnap.utils.MapUtils;
import org.szederz.prodsnap.utils.function.Supplier;
import org.szederz.prodsnap.utils.net.http.client.HttpClient;
import org.szederz.prodsnap.utils.net.http.client.UnsafeSSLConnectionBuilder;
import org.szederz.prodsnap.utils.net.http.filters.request.HeaderFilter;
import org.szederz.prodsnap.utils.net.http.filters.request.hmac.HMACRequestFilter;
import org.szederz.prodsnap.utils.net.http.filters.request.logging.RequestLoggingFilter;
import org.szederz.prodsnap.utils.net.http.filters.response.logging.ResponseLoggingFilter;

import java.util.HashMap;
import java.util.Map;

public class ApplicationSingleton {
  private static ApplicationSingleton INSTANCE;
  private final Map<Class, Object> services = new HashMap<>();
  private BasketStorage storage;
  private SharedPreferencesConfiguration conf = SharedPreferencesConfiguration.getInstance();
  private ItemDetailStorage itemDetailStorage;
  private BasketMimeMessageFormatter basketFormatter;
  private BarcodeCache barcodeCache = new BarcodeCache(conf);
  private HttpItemDetailStorage httpItemDetailsStorage;

  public static ApplicationSingleton getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ApplicationSingleton();
    }
    return INSTANCE;
  }

  private ApplicationSingleton() {
    rebuild();
  }

  public void rebuild() {
    buildDependencies();
    registerServices();
  }

  private void registerServices() {
    register(new ConfigurationService(conf));
    register(new CustomerService(storage));
    register(new BasketService(storage));
    register(new ItemDetailService(storage, itemDetailStorage));
    register(new StorageCacheService(httpItemDetailsStorage, barcodeCache));
    register(createConfigurationService());
    register(new EmailBasketTransferService(basketFormatter));
  }

  private ApplicationConfigurationService createConfigurationService() {
    Container<String> secretConsumer = new Container<>();
    return new ApplicationConfigurationService(
      new HttpClient(new UnsafeSSLConnectionBuilder())
        .withRequestFilters(
          new HMACRequestFilter(secretConsumer),
          new RequestLoggingFilter())
        .withResponseFilters(
          new ResponseLoggingFilter()),
      secretConsumer,
      conf);
  }

  private void buildDependencies() {
    storage = new BasketStorage();

    HttpClient httpClient = new HttpClient(new UnsafeSSLConnectionBuilder())
      .withRequestFilters(
        new HeaderFilter(MapUtils.<String, Supplier<String>>asMap(
          "Authorization-User", new ConfigurationSupplier(conf, "hmac.user")
        )),
        new HMACRequestFilter(new ConfigurationSupplier(conf, "hmac.secret")),
        new RequestLoggingFilter())
      .withResponseFilters(
        new ResponseLoggingFilter());

    httpItemDetailsStorage = new HttpItemDetailStorage(
      httpClient,
      new Supplier<String>() {
        @Override
        public String get() {
          return conf.getBaseHttpUrl();
        }
      },
      "/items"
    );

    itemDetailStorage = new CachedItemDetailStorage(httpItemDetailsStorage, barcodeCache);

    EmailConfiguration emailConf = new EmailConfiguration(conf);
    basketFormatter = new BasketMimeMessageFormatter(
      new EmailSessionFactory(emailConf),
      new HtmlBasketFormatterWrapper(
        new HtmlDivBasketFormatter(
          new BasketItemFormatter())),
      emailConf);
  }

  private void register(Object service) {
    services.put(service.getClass(), service);
  }

  public static <T> T getService(Class<T> tClass) {
    return (T) getInstance().services.get(tClass);
  }
}
