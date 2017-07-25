package io.xsor.countrypicker;


import android.support.annotation.NonNull;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import java.util.Locale;

@SuppressWarnings("unused")
public class Country implements SortedListAdapter.ViewModel {

  private String isoCode;
  private String dialingCode = "-1";

  public Country() {}

  public Country(String isoCode, String dialingCode) {
    this.isoCode = isoCode.toLowerCase();
    this.dialingCode = dialingCode;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

  public String getDialingCode() {
    return dialingCode;
  }

  public void setDialingCode(String dialingCode) {
    this.dialingCode = dialingCode;
  }

  public int getDialingCodeInt() {
    return Integer.parseInt(dialingCode);
  }

  public String getCountryName() {
    return new Locale(Locale.getDefault().getLanguage(), this.getIsoCode()).getDisplayCountry();

  }

  @Override
  public <T> boolean isSameModelAs(@NonNull T item) {
    if (item instanceof Country) {
      final Country other = (Country) item;
      return other.getIsoCode().equals(isoCode);
    }
    return false;
  }

  @Override
  public <T> boolean isContentTheSameAs(@NonNull T item) {
    if (item instanceof Country) {
      final Country other = (Country) item;
      return isoCode != null ? isoCode.equals(other.getIsoCode()) : other.getIsoCode() == null &&
          dialingCode != null ? getDialingCodeInt() == other.getDialingCodeInt() : other
          .getDialingCodeInt() > -1;
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Country country = (Country) o;

    return isoCode.equals(country.getIsoCode());

  }

  @Override
  public int hashCode() {
    return isoCode.hashCode();
  }
}
