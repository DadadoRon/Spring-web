package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.repository.UserAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "userAppointment")
@RequiredArgsConstructor
public class UserAppointmentServiceImpl implements UserAppointmentService{
    private final UserAppointmentRepository userAppointmentRepository;
    private final ProductService productService;
    private final UserService userService;


    @Override
    @Cacheable
    public List<UserAppointment> getAllUserAppointments() {
        return userAppointmentRepository.findAll();

    }
    @Override
    @Cacheable(key = "#userAppointmentId")
    public UserAppointment getUserAppointmentById(Integer userAppointmentId) {
        return userAppointmentRepository.findByIdRequired(userAppointmentId);
    }

    @Override
    @Cacheable(key = "#userId")
    public List<UserAppointment> getAllUserAppointmentsByUserId(Integer userId) {
        return userAppointmentRepository.findByUserId(userId);
    }

    @Override
    public boolean checkIfExistsByUserId(Integer userId) {
        if(userId == null) {
            return false;
        }
        return userAppointmentRepository.existsByUserId(userId);
    }

    @Override
    @CachePut(key = "#userAppointment.id")
    @CacheEvict(allEntries = true)
    public UserAppointment createUserAppointment(UserAppointment userAppointment, Integer userId, Integer productId) {
        User user = userService.getUserById(userId);
        userAppointment.setUser(user);
        Product product = productService.getProductById(productId);
        userAppointment.setProduct(product);
        return  userAppointmentRepository.save(userAppointment);
    }

    @Override
    @CachePut(key = "#userAppointment.id")
    @CacheEvict(allEntries = true)
    public UserAppointment updateUserAppointment(UserAppointment userAppointment) {
            Integer userAppointmentId = userAppointment.getId();
            UserAppointment byId = getUserAppointmentById(userAppointmentId);
            User user = byId.getUser();
            Product product = byId.getProduct();
            userAppointment.setUser(user);
            userAppointment.setProduct(product);
            return userAppointmentRepository.save(userAppointment);
    }

    @Override
    @CacheEvict(key = "#userAppointmentId", allEntries = true)
    public void deleteUserAppointment(Integer userAppointmentId) {
        userAppointmentRepository.checkIfExistsById(userAppointmentId);
        userAppointmentRepository.deleteById(userAppointmentId);
    }
}
