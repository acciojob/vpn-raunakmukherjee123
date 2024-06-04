package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception {
        // Validate the countryName
        CountryName countryNameEnum;
        try {
            countryNameEnum = CountryName.valueOf(countryName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new Exception("Country not found");
        }

        // Create a new Country
        Country country = new Country();
        country.setCountryName(CountryName.valueOf(countryName));
        // Save the new Country
        countryRepository3.save(country);

        // Create a new User
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Remember to encode the password in a real-world application
        user.setOriginalCountry(country);
        user.setConnected(false);
        // Save the new User
        userRepository3.save(user);

        // Set the originalIp using the country code and user Id
        user.setOriginalIp(countryNameEnum.toCode() + "." + user.getId());
        // Save the updated User
        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        // Find the User using the userId
        Optional<User> userOptional = userRepository3.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            // Find the ServiceProvider using the serviceProviderId
            Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository3.findById(serviceProviderId);
            if(serviceProviderOptional.isPresent()){
                ServiceProvider serviceProvider = serviceProviderOptional.get();
                // Add the ServiceProvider to the User's list of service providers
                user.getServiceProviderList().add(serviceProvider);
                // Save the updated User
                userRepository3.save(user);
                return user;
            } else {
                // Handle the case where no ServiceProvider was found with the provided serviceProviderId
                // You might want to throw an exception or return null
                return null;
            }
        } else {
            // Handle the case where no User was found with the provided userId
            // You might want to throw an exception or return null
            return null;
        }
    }
}
