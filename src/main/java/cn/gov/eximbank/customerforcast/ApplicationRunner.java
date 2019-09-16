package cn.gov.eximbank.customerforcast;

import cn.gov.eximbank.customerforcast.model.Customer;
import cn.gov.eximbank.customerforcast.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
//        customerRepository.deleteAll();
//        Customer customer = new Customer("123", "345");
//        customerRepository.save(customer);
//        Customer customerFound = customerRepository.findById("123").get();
//        System.out.println(customerFound.getGroupId());
    }
}
