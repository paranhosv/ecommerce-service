package com.victorparanhos.ecommerceservice.web.configurations.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckoutImpl;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Configuration
public class CheckoutConfig {
    @Value("${dt_black_friday}")
    private String dtBlackFriday;

    @Autowired
    private ProductGateway productsGateway;

    @Autowired
    private DiscountServiceGateway discountServiceGateway;

    @Bean
    public MakeCheckout createMakeCheckoutImpl() throws ParseException {
        return new MakeCheckoutImpl(productsGateway, discountServiceGateway, loadBlackFridays());
    }

    private Set<LocalDate> loadBlackFridays() throws ParseException {
        Set<LocalDate> blackFridays = new HashSet<>();
        var pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        var matcher = pattern.matcher(dtBlackFriday);
        while (matcher.find()) {
            blackFridays.add(LocalDate.parse(matcher.group()));
        }

        return blackFridays;
    }
}
