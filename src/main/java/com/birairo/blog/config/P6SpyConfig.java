package com.birairo.blog.config;

import java.util.Locale;


import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

@Configuration
public class P6SpyConfig implements MessageFormattingStrategy {

  private static final String CREATE = "create";
  private static final String ALTER = "alter";
  private static final String COMMENT = "comment";

  @PostConstruct
  public void setLogMessageFormat() {
    P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
  }

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
      String sql, String url) {
    return String.format("[%s] | %d ms \n%s", category, elapsed, formatSql(category, sql));
  }

  private String formatSql(String category, String sql) {

    if (sql == null || sql.trim().isEmpty()) {
      return sql;
    }

    if (isStatementWithDDL(category, sql)) {
      return FormatStyle.DDL.getFormatter().format(sql);
    }
    return FormatStyle.BASIC.getFormatter().format(sql);

  }

  private boolean isStatementWithDDL(String category, String sql) {
    return isStatement(category) && isDDL(sql.trim().toLowerCase(Locale.ROOT));
  }

  private boolean isStatement(String category) {
    return Category.STATEMENT.getName().equals(category);
  }

  private boolean isDDL(String lowerSql) {
    return lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(COMMENT);
  }

}