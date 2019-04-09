package com.acooly.openapi.framework.common.convert.converter;

import com.acooly.core.utils.Money;
import org.springframework.core.convert.converter.Converter;

public class StringToMoneyConverter implements Converter<String, Money> {

  @Override
  public Money convert(String source) {
    if (source == null || source.equals("")) {
      return null;
    }
    try {
      Money money = new Money(source);
      return money;
    } catch (Exception e) {
      throw new RuntimeException("Money格式错误");
    }
  }
}
