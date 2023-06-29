package com.example.boot3;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Boot3Application {




    private static DefaultCustomerService transactionalCustomerService(TransactionTemplate template, DefaultCustomerService delegate) {

      var pfb = new ProxyFactoryBean();
      pfb.setTarget(delegate);
      pfb.setProxyTargetClass(true);
      pfb.addAdvice(
          new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
              var method = invocation.getMethod();
              var args = invocation.getArguments();

              return template.execute(status -> {
                try {
                  return method.invoke(delegate, args);
                } catch (IllegalAccessException e) {
                  throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                  throw new RuntimeException(e);
                }
              });


            }
          }
      );

     return (DefaultCustomerService) pfb.getObject();
  }
//  private static CustomerService transactionalCustomerService(TransactionTemplate template, CustomerService delegate) {
//    var transactionalCustomerService = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
//        new Class[]{CustomerService.class}, new InvocationHandler() {
//          @Override
//          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            log.info("invoking {} with arguments {}", method.getName(), args);
//            return template.execute(status -> {
//              try {
//                return method.invoke(delegate, args);
//              } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//              } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//              }
//            });
//
//          }
//        });
//    return (CustomerService) transactionalCustomerService;
//  }
  public static void main(String[] args) throws SQLException {

    final DataSource dataSource = new DriverManagerDataSource(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "password"
    );// terrible choice for production  -> one connection on every call (not a pool.

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    var cs = transactionalCustomerService(new TransactionTemplate(new DataSourceTransactionManager(dataSource)), new DefaultCustomerService(jdbcTemplate));
    cs.all().forEach(c -> log.info("Customer: {}", c));
    var id = cs.add(new Customer(null, "MMESSI" + System.currentTimeMillis()));
    log.info("Customer: {}", cs.all());
  }

}

//interface CustomerService {
//  Collection<Customer> all() throws SQLException;
//
//  int add(Customer customer);
//}

@Slf4j
class DefaultCustomerService { // implements CustomerService {

  private final JdbcTemplate jdbcTemplate;

  public DefaultCustomerService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  // @Override
  public Collection<Customer> all() throws SQLException {

    return jdbcTemplate.query("select * from customers", (rs, rowNum) -> new Customer(rs.getInt("id"), rs.getString("name")));
  }

  // @Override
  public int add (Customer customer) {
    var m = new ArrayList<Map<String, Object>>();
    var l = new HashMap<String, Object>();
    l.put("id", Integer.class);
    m.add(l);
    var keyHolder = new GeneratedKeyHolder(m);
    jdbcTemplate.update(
        con -> {
          final PreparedStatement preparedStatement = con.prepareStatement("insert into customers (name) values (?)", Statement.RETURN_GENERATED_KEYS);
          preparedStatement.setString(1, customer.name());
          return preparedStatement;
        }, keyHolder);
    return (Integer) keyHolder.getKeys().get("id");
  }
}



record Customer(Integer id, String name) {
}