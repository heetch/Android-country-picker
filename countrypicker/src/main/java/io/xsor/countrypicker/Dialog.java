package io.xsor.countrypicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter.Callback;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Dialog extends AppCompatDialog implements OnQueryTextListener, Callback {

  private List<Country> countries;
  private Adapter adapter;
  private Listener listener;
  private RecyclerView rvCountryList;
  private Animator mAnimator;

  private String scrollToCountryCode;
  private boolean showDialingCode = false;
  private boolean showRoundFlags = false;
  private int titleResId;
  private ProgressBar pbEditProgressBar;
  private LinearLayoutManager llm;

  private Comparator<Country> comparator = new Comparator<Country>() {
    @Override
    public int compare(Country country1, Country country2) {
      final Locale locale = Locale.getDefault();
      final Collator collator = Collator.getInstance(locale);
      collator.setStrength(Collator.PRIMARY);
      return collator.compare(
          new Locale(locale.getLanguage(), country1.getIsoCode()).getDisplayCountry(),
          new Locale(locale.getLanguage(), country2.getIsoCode()).getDisplayCountry());
    }
  };

  /**
   * @param context The calling activity or context
   * @param listener The listener to be fired when a country from the list is chosen
   */
  public Dialog(Context context, Listener listener) {
    super(context);
    this.listener = listener;
    this.titleResId = R.string.pick_a_country;
    countries = Utils.parseCountries(Utils.getCountriesJSON(this.getContext()));
    Collections.sort(countries, comparator);
  }

  /**
   * @param context The calling activity or context
   * @param titleResId The resource id for the title
   * @param listener The listener to be fired when a country from the list is chosen
   */
  public Dialog(Context context, int titleResId, Listener listener) {
    this(context, listener);
    this.titleResId = titleResId;
  }

  /**
   * @param context The calling activity or context
   * @param titleResId The resource id for the title
   * @param showRoundFlags Show round flag icons in dialog
   * @param listener The listener to be fired when a country from the list is chosen
   */
  public Dialog(Context context, int titleResId, boolean showRoundFlags,
      Listener listener) {
    this(context, titleResId, listener);
    this.showRoundFlags = showRoundFlags;
  }

  /**
   * @param context The calling activity or context
   * @param titleResId The resource id for the title
   * @param showRoundFlags Show round flag icons in dialog
   * @param showDialingCode Show country dialong codes in dialog
   * @param listener The listener to be fired when a country from the list is chosen
   */
  public Dialog(Context context, int titleResId, boolean showRoundFlags,
      boolean showDialingCode, Listener listener) {
    this(context, titleResId, showRoundFlags, listener);
    this.showDialingCode = showDialingCode;
  }

  public Dialog(Context context, boolean roundFlags, boolean dialingCodes,
      Listener callback) {
    this(context, R.string.pick_a_country, roundFlags, dialingCodes, callback);
  }

  public Dialog(Context context, boolean roundFlags, Listener callback) {
    this(context, R.string.pick_a_country, roundFlags, false, callback);
  }

  private static List<Country> filter(List<Country> models, String query) {
    final String lowerCaseQuery = query.toLowerCase();

    final List<Country> filteredModelList = new ArrayList<>();
    for (Country model : models) {
      final String text = model.getCountryName().toLowerCase();
      if (text.contains(lowerCaseQuery)) {
        filteredModelList.add(model);
      }
    }
    return filteredModelList;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.country_picker);

    setTitle(titleResId);

    // SearchView
    SearchView svCountrySearch = (SearchView) findViewById(R.id.svCountrySearch);
    if (svCountrySearch != null) {
      svCountrySearch.setOnQueryTextListener(this);
      svCountrySearch.setIconifiedByDefault(true);
      svCountrySearch.setFocusable(true);
      svCountrySearch.setIconified(false);
      svCountrySearch.clearFocus();
    }

    pbEditProgressBar = (ProgressBar) findViewById(R.id.pbEditProgressBar);

    rvCountryList = (RecyclerView) findViewById(R.id.rvCountriesList);

    if (rvCountryList != null) {
      rvCountryList.setHasFixedSize(true);
    }

    llm = new LinearLayoutManager(getContext());
    llm.setOrientation(LinearLayoutManager.VERTICAL);
    rvCountryList.setLayoutManager(llm);
    adapter = new Adapter(getContext(), comparator, listener, showRoundFlags, showDialingCode);
    adapter.addCallback(this);
    adapter.edit().add(countries).commit();

    rvCountryList.setAdapter(adapter);

  }

  public Dialog setScrollToCountry(String countryIsoCode) {
    this.scrollToCountryCode = countryIsoCode;
    return this;
  }

  private void scrollToHeadingCountry() {
    if (scrollToCountryCode != null) {
      int idx = countries.indexOf(new Country(scrollToCountryCode, "-1"));
      if (idx > -1) {
        llm.scrollToPositionWithOffset(idx, 0);
      }
    }

  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String query) {
    final List<Country> filteredModelList = filter(countries, query);
    adapter.edit()
        .replaceAll(filteredModelList)
        .commit();
    return true;
  }

  @Override
  public void onEditStarted() {
    if (pbEditProgressBar.getVisibility() != View.VISIBLE) {
      pbEditProgressBar.setVisibility(View.VISIBLE);
      pbEditProgressBar.setAlpha(0.0f);
    }

    if (mAnimator != null) {
      mAnimator.cancel();
    }

    mAnimator = ObjectAnimator.ofFloat(pbEditProgressBar, View.ALPHA, 1.0f);
    mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    mAnimator.start();

    rvCountryList.animate().alpha(0.5f);
  }

  @Override
  public void onEditFinished() {
    scrollToHeadingCountry();
    rvCountryList.animate().alpha(1.0f);

    if (mAnimator != null) {
      mAnimator.cancel();
    }

    mAnimator = ObjectAnimator.ofFloat(pbEditProgressBar, View.ALPHA, 0.0f);
    mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    mAnimator.addListener(new AnimatorListenerAdapter() {

      private boolean mCanceled = false;

      @Override
      public void onAnimationCancel(Animator animation) {
        super.onAnimationCancel(animation);
        mCanceled = true;
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (!mCanceled) {
          pbEditProgressBar.setVisibility(View.GONE);
        }
      }
    });
    mAnimator.start();
  }
}
