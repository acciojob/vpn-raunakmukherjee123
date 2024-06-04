package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception
    {
//         Find the User using the userId
//         Find the User using the userId
        Optional<User> userOptional = userRepository2.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            // Check if the user is already connected
            if(user.getConnected()){
                throw new Exception("Already connected");
            }
            // Check if the user wants to connect to their original country
            if(user.getOriginalCountry().getCountryName().equals(countryName)){
                return user;
            }
            // Try to connect the user to a ServiceProvider that provides the given country
            List<ServiceProvider> serviceProviders = user.getServiceProviderList().stream()
                    .filter(sp -> sp.getCountryList().stream()
                            .anyMatch(c -> c.getCountryName().equals(countryName)))
                    .sorted(Comparator.comparingInt(ServiceProvider::getId))
                    .collect(Collectors.toList());
            if(serviceProviders.isEmpty()){
                throw new Exception("Unable to connect");
            }
            // Establish the connection
            ServiceProvider serviceProvider = serviceProviders.get(0); // Use the ServiceProvider with the smallest id
            user.setConnected(true);
            user.setMaskedIp(countryName + "." + serviceProvider.getId() + "." + user.getId());
            // Save the updated User
            userRepository2.save(user);
            return user;
        } else {
            // Handle the case where no User was found with the provided userId
            // You might want to throw an exception or return null
            return null;
        }

    }
    @Override
    public User disconnect(int userId) throws Exception {
        // Find the User using the userId
        Optional<User> userOptional = userRepository2.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            // Check if the user is already disconnected
            if(!user.getConnected()){
                throw new Exception("Already disconnected");
            }
            // Disconnect the user
            user.setConnected(false);
            user.setMaskedIp(null);
            // Save the updated User
            userRepository2.save(user);
            return user;
        } else {
            // Handle the case where no User was found with the provided userId
            // You might want to throw an exception or return null
            return null;
        }
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        // Find the sender and receiver Users using the userIds
        Optional<User> senderOptional = userRepository2.findById(senderId);
        Optional<User> receiverOptional = userRepository2.findById(receiverId);
        if(senderOptional.isPresent() && receiverOptional.isPresent())
        {
            User sender = senderOptional.get();
            User receiver = receiverOptional.get();
            // Determine the receiver's current country
            String receiverCountry = receiver.getConnected() ? receiver.getMaskedIp().split("\\.")[0] : String.valueOf(receiver.getOriginalCountry().getCountryName());
            // Check if the sender's original country matches the receiver's current country
            if(sender.getOriginalCountry().getCountryName().equals(receiverCountry))
            {
                return sender;
            }
            // If not, try to connect the sender to a ServiceProvider that provides the receiver's current country
            //List<ServiceProvider> serviceProviders = serviceProviderRepository2.findByCountryName(receiverCountry);
            List<ServiceProvider> serviceProviders = sender.getServiceProviderList().stream()
                    .filter(sp -> sp.getCountryList().stream()
                            .anyMatch(c -> c.getCountryName().equals(receiverCountry)))
                    .sorted(Comparator.comparingInt(ServiceProvider::getId))
                    .collect(Collectors.toList());
            if(serviceProviders.isEmpty())
            {
                throw new Exception("Cannot establish communication");
            }
            // Connect the sender to the ServiceProvider with the smallest id
            ServiceProvider serviceProvider = serviceProviders.get(0);
            sender.setConnected(true);
            sender.setMaskedIp(receiverCountry + "." + serviceProvider.getId() + "." + sender.getId());
            // Save the updated sender
            userRepository2.save(sender);
            return sender;
        }
         else
         {
            // Handle the case where no User was found with the provided userId
            // You might want to throw an exception or return null
            return null;
         }
       // return null;
    }
}
