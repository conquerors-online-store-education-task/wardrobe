package bg.conquerors.wardrobe.service.impl;

import bg.conquerors.wardrobe.model.dto.UserRegistrationDTO;
import bg.conquerors.wardrobe.model.dto.ViewUserOrdersDTO;
import bg.conquerors.wardrobe.model.entity.Order;
import bg.conquerors.wardrobe.model.entity.User;
import bg.conquerors.wardrobe.model.enums.OrderStatusEnum;
import bg.conquerors.wardrobe.model.enums.UserRoleEnum;
import bg.conquerors.wardrobe.repository.RoleRepository;
import bg.conquerors.wardrobe.repository.UserRepository;
import bg.conquerors.wardrobe.service.OrderService;
import bg.conquerors.wardrobe.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            OrderService orderService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @Override
    public void register(UserRegistrationDTO userRegistrationDTO) {

        Optional<User> user = userRepository.findByUsername(userRegistrationDTO.getUsername());

        if (user.isPresent()) {
            return;
        }

        userRepository.save(map(userRegistrationDTO));
        orderService.createNewOrder(
                userRepository.findByUsername(
                                userRegistrationDTO
                                        .getUsername())
                        .orElse(null));
    }

    private User map(UserRegistrationDTO userRegistrationDTO) {
        User newUser = new User();

        newUser.setEmail(userRegistrationDTO.getEmail());
        newUser.setUsername(userRegistrationDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        newUser.setFirstName(userRegistrationDTO.getFirstName());
        newUser.setLastName(userRegistrationDTO.getLastName());
        newUser.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        newUser.setPoints(0);
        newUser.setOrders(new ArrayList<>());

        newUser.addRole(roleRepository.findByRole(UserRoleEnum.USER).get());

        return newUser;
    }

    @Override
    public User findCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public void editUserInformation(Long id, UserRegistrationDTO userRegistrationDTO) {
        // Find the existing user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // Update user's information
        user.setEmail(userRegistrationDTO.getEmail());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPhoneNumber(userRegistrationDTO.getPhoneNumber());

        // Assume that if the password field is not empty, the user wants to change their password.
        if (userRegistrationDTO.getPassword() != null && !userRegistrationDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        }

        // Save the updated user
        userRepository.save(user);
    }

    @Override
    public List<ViewUserOrdersDTO> getUserOrders(User currentUser) {

        return createViewUserOrdersDTO(currentUser);
    }

    private List<ViewUserOrdersDTO> createViewUserOrdersDTO(User currentUser) {

        List<ViewUserOrdersDTO> viewUserOrdersDTOS = new ArrayList<>();

        for (Order order : currentUser.getOrders()) {

            if (!order.getStatus().equals(OrderStatusEnum.CART))
                viewUserOrdersDTOS.add(new ViewUserOrdersDTO(order.getId(), order.getOrderDate(), order.getStatus(), order.getTotalPrice(), order.getAddress()));
        }

        return viewUserOrdersDTOS;
    }


}
