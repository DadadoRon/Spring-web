package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.repository.UserAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAppointmentServiceImpl implements UserAppointmentService{

    private final UserAppointmentRepository userAppointmentRepository;
    private final ProductService productService;
    private final UserService userService;


    @Override
    public List<UserAppointment> getAllUserAppointments() {
        return userAppointmentRepository.findAll();
    }

    @Override
    public List<UserAppointment> getAllUserAppointmentsByUserId(Integer userId) {
        return userAppointmentRepository.findByUserId(userId);
    }

    @Override
    public UserAppointment createUserAppointment(UserAppointment userAppointment, Integer userId, Integer productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        userAppointment.setUser(user);
        userAppointment.setProduct(product);
        return  userAppointmentRepository.save(userAppointment);
    }

    @Override
    public UserAppointment updateUserAppointment(UserAppointment userAppointment) {
        if (!userAppointmentRepository.existsById(userAppointment.getId())) {
//            return null;
            throw new RuntimeException("User not found");
        }
        Integer userId = userAppointment.getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userId);
        User user = byId.get().getUser();
        Product product = byId.get().getProduct();
        userAppointment.setUser(user);
        userAppointment.setProduct(product);
        return userAppointmentRepository.save(userAppointment);
    }

    @Override
    public void deleteUserAppointment(Integer userAppointmentId) {
        userAppointmentRepository.deleteById(userAppointmentId);
    }

}
