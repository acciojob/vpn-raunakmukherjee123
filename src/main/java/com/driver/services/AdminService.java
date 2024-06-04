package com.driver.services;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface AdminService {
//    @Autowired
//    AdminRepository adminRepository;
//    ServiceProviderRepository serviceProviderRepository;//created by myself
//    CountryRepository countryRepository;//by myself
//
//    public Admin register(String username, String password) {
//        Admin admin = new Admin();
//        admin.setUsername(username);
//        admin.setPassword(password);
//        // Assuming you have a save method in your repository
//        adminRepository.save(admin);
//        return admin;
//    }
//
//    public Admin addServiceProvider(int adminId, String providerName) {
//        // First, find the admin using the adminId
//        Optional<Admin> adminOptional = adminRepository.findById(adminId);
//        if (adminOptional.isPresent()) {
//            Admin admin = adminOptional.get();
//            // Create a new ServiceProvider
//            ServiceProvider serviceProvider = new ServiceProvider();
//            serviceProvider.setName(providerName);
//            serviceProvider.setAdmin(admin);
//            // Save the new ServiceProvider
//            serviceProviderRepository.save(serviceProvider);
//            // Add the new ServiceProvider to the admin's list of serviceProviders
//            admin.getServiceProviders().add(serviceProvider);
//            // Save the updated admin
//            adminRepository.save(admin);
//            return admin;
//        }
//        else {
//            return null;
//        }
//    }
//
//    public ServiceProvider addCountry(int serviceProviderId, String countryName) {
//        // Validate the countryName
////        try {
////            CountryName countryNameEnum = CountryName.valueOf(countryName.toUpperCase());
////        } catch (IllegalArgumentException ex) {
////            throw new Exception("Country not found");
////        }
//
//        // Find the ServiceProvider using the serviceProviderId
//        Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository.findById(serviceProviderId);
//        if (serviceProviderOptional.isPresent()) {
//            ServiceProvider serviceProvider = serviceProviderOptional.get();
//            // Create a new Country
//            Country country = new Country();
//            country.setCountryName(CountryName.valueOf(countryName));
//            // Save the new Country
//            countryRepository.save(country);
//            // Add the new Country to the serviceProvider's list of countries
//            serviceProvider.getCountryList().add(country);
//            // Save the updated ServiceProvider
//            serviceProviderRepository.save(serviceProvider);
//            return serviceProvider;
//       } else {
////            // Handle the case where no ServiceProvider was found with the provided serviceProviderId
////            // You might want to throw an exception or return null
//            return null;
////        }
//        }
//    }
Admin register(String username, String password);
    Admin addServiceProvider(int adminId, String providerName);
    ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception;
}