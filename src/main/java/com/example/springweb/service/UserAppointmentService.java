package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.repository.UserAppointmentRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "userappointment")
public class UserAppointmentService extends BaseService<UserAppointment, Integer> {
    private final UserAppointmentRepository userAppointmentRepository;
    private final ProductService productService;
    private final UserService userService;

    public UserAppointmentService(UserAppointmentRepository userAppointmentRepository, ProductService productService,
                                  UserService userService) {
        super(userAppointmentRepository);
        this.userAppointmentRepository = userAppointmentRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Cacheable(key = "#userAppointmentId")
    public UserAppointment findByIdRequired(Integer userAppointmentId) {
        return userAppointmentRepository.findByIdRequired(userAppointmentId);
    }

    @Cacheable(key = "#userId")
    public List<UserAppointment> getAllUserAppointmentsByUserId(Integer userId) {
        return userAppointmentRepository.findByUserId(userId);
    }

    public boolean checkIfExistsByUserId(Integer userId) {
        if (userId == null) {
            return false;
        }
        return userAppointmentRepository.existsByUserId(userId);
    }

    @CachePut(key = "#userAppointment.id")
    @CacheEvict(allEntries = true)
    public UserAppointment createUserAppointment(UserAppointment userAppointment, Integer userId, Integer productId) {
        User user = userService.findByIdRequired(userId);
        userAppointment.setUser(user);
        Product product = productService.findByIdRequired(productId);
        userAppointment.setProduct(product);
        return userAppointmentRepository.save(userAppointment);
    }

    @Override
    public UserAppointment update(UserAppointment userAppointment) {
            Integer userAppointmentId = userAppointment.getId();
            UserAppointment byId = findByIdRequired(userAppointmentId);
            User user = byId.getUser();
            Product product = byId.getProduct();
            userAppointment.setUser(user);
            userAppointment.setProduct(product);
            return super.update(userAppointment);
    }

    @Override
    public void delete(Integer userAppointmentId) {
        userAppointmentRepository.checkIfExistsById(userAppointmentId);
        super.delete(userAppointmentId);
    }
}
