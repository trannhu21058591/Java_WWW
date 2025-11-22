package iuh.fit.se.shop_be.services.Impl;

import iuh.fit.se.shop_be.dto.GuestCartItemDTO;
import iuh.fit.se.shop_be.dto.request.AddToCartRequest;
import iuh.fit.se.shop_be.dto.response.AddToCartResponse;
import iuh.fit.se.shop_be.dto.response.CartListResponse;
import iuh.fit.se.shop_be.entities.Cart;
import iuh.fit.se.shop_be.entities.CartItem;
import iuh.fit.se.shop_be.entities.Product;
import iuh.fit.se.shop_be.entities.User;
import iuh.fit.se.shop_be.exceptions.ResourceNotFoundException;
import iuh.fit.se.shop_be.repositories.CartItemRepository;
import iuh.fit.se.shop_be.repositories.CartRepository;
import iuh.fit.se.shop_be.repositories.ProductRepository;
import iuh.fit.se.shop_be.repositories.UserRepository;
import iuh.fit.se.shop_be.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddToCartResponse addToCart(Long userId, AddToCartRequest request) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Lấy sản phẩm
        Product product = productRepository.findActiveById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm hoặc sản phẩm không còn hoạt động"));

        // Kiểm tra số lượng tồn kho
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ. Hiện còn " + product.getStock() + " sản phẩm");
        }

        // Tìm hoặc tạo Cart cho user
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalPrice(0.0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ. Hiện còn " + product.getStock() + " sản phẩm");
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(product.getPrice()); 
        } else {
            // Nếu chưa có, tạo CartItem mới
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .addedAt(LocalDateTime.now())
                    .build();
        }

        cartItem = cartItemRepository.save(cartItem);

        updateCartTotalPrice(cart);

        AddToCartResponse.CartItemDTO cartItemDTO = AddToCartResponse.CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImageURL(product.getImageURL())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getPrice() * cartItem.getQuantity())
                .build();

        return AddToCartResponse.builder()
                .returnCode(200)
                .message("Thêm sản phẩm vào giỏ hàng thành công!")
                .success(true)
                .cartItem(cartItemDTO)
                .build();
    }

    @Override
    public AddToCartResponse addToGuestCart(AddToCartRequest request) {
        // Lấy sản phẩm
        Product product = productRepository.findActiveById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm hoặc sản phẩm không còn hoạt động"));

        // Kiểm tra số lượng tồn kho
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ. Hiện còn " + product.getStock() + " sản phẩm");
        }

        // Tạo response (giỏ hàng guest sẽ được lưu ở session, không cần lưu vào DB)
        AddToCartResponse.CartItemDTO cartItemDTO = AddToCartResponse.CartItemDTO.builder()
                .id(null) // Guest cart không có ID
                .productId(product.getId())
                .productName(product.getName())
                .productImageURL(product.getImageURL())
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .subtotal(product.getPrice() * request.getQuantity())
                .build();

        return AddToCartResponse.builder()
                .returnCode(200)
                .message("Thêm sản phẩm vào giỏ hàng thành công!")
                .success(true)
                .cartItem(cartItemDTO)
                .build();
    }

    @Override
    @Transactional
    public void mergeGuestCartToUserCart(Long userId, List<GuestCartItemDTO> guestCartItems) {
        if (guestCartItems == null || guestCartItems.isEmpty()) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Tìm hoặc tạo Cart cho user
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalPrice(0.0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        // Merge từng item từ guest cart vào user cart
        for (GuestCartItemDTO guestItem : guestCartItems) {
            Product product = productRepository.findActiveById(guestItem.getProductId())
                    .orElse(null);

            if (product == null || !product.isActive()) {
                continue; // Bỏ qua sản phẩm không tồn tại hoặc không active
            }

            // Kiểm tra số lượng tồn kho
            if (product.getStock() < guestItem.getQuantity()) {
                continue; // Bỏ qua nếu không đủ hàng
            }

            // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
            CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElse(null);

            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + guestItem.getQuantity();
                if (product.getStock() >= newQuantity) {
                    existingItem.setQuantity(newQuantity);
                    existingItem.setPrice(product.getPrice());
                    cartItemRepository.save(existingItem);
                }
            } else {
                // Nếu chưa có, tạo CartItem mới
                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(guestItem.getQuantity())
                        .price(product.getPrice())
                        .addedAt(LocalDateTime.now())
                        .build();
                cartItemRepository.save(newItem);
            }
        }
        updateCartTotalPrice(cart);
    }

    @Override
    public CartListResponse getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        Cart cart = cartRepository.findByUser(user).orElse(null);
        
        if (cart == null) {
            return CartListResponse.builder()
                    .returnCode(200)
                    .message("Giỏ hàng trống")
                    .success(true)
                    .items(new ArrayList<>())
                    .totalPrice(0.0)
                    .totalItems(0)
                    .build();
        }

        List<CartItem> cartItems = cartItemRepository.findAll().stream()
                .filter(item -> item.getCart().getId() == cart.getId())
                .collect(Collectors.toList());

        List<CartListResponse.CartItemDTO> itemDTOs = cartItems.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    return CartListResponse.CartItemDTO.builder()
                            .id(item.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .productImageURL(product.getImageURL())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .subtotal(item.getPrice() * item.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        int totalItems = itemDTOs.stream()
                .mapToInt(CartListResponse.CartItemDTO::getQuantity)
                .sum();

        return CartListResponse.builder()
                .returnCode(200)
                .message("Lấy danh sách giỏ hàng thành công!")
                .success(true)
                .items(itemDTOs)
                .totalPrice(cart.getTotalPrice())
                .totalItems(totalItems)
                .build();
    }

    @Override
    public CartListResponse getGuestCartItems(List<GuestCartItemDTO> guestCartItems) {
        if (guestCartItems == null || guestCartItems.isEmpty()) {
            return CartListResponse.builder()
                    .returnCode(200)
                    .message("Giỏ hàng trống")
                    .success(true)
                    .items(new ArrayList<>())
                    .totalPrice(0.0)
                    .totalItems(0)
                    .build();
        }

        List<CartListResponse.CartItemDTO> itemDTOs = guestCartItems.stream()
                .map(guestItem -> CartListResponse.CartItemDTO.builder()
                        .id(null) // Guest cart không có ID
                        .productId(guestItem.getProductId())
                        .productName(guestItem.getProductName())
                        .productImageURL(guestItem.getProductImageURL())
                        .price(guestItem.getPrice())
                        .quantity(guestItem.getQuantity())
                        .subtotal(guestItem.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        double totalPrice = itemDTOs.stream()
                .mapToDouble(CartListResponse.CartItemDTO::getSubtotal)
                .sum();

        int totalItems = itemDTOs.stream()
                .mapToInt(CartListResponse.CartItemDTO::getQuantity)
                .sum();

        return CartListResponse.builder()
                .returnCode(200)
                .message("Lấy danh sách giỏ hàng thành công!")
                .success(true)
                .items(itemDTOs)
                .totalPrice(totalPrice)
                .totalItems(totalItems)
                .build();
    }

    private void updateCartTotalPrice(Cart cart) {
        // Load lại cart với items để đảm bảo có dữ liệu mới nhất
        Cart refreshedCart = cartRepository.findById(cart.getId())
                .orElse(cart);
        
        double totalPrice = cartItemRepository.findAll().stream()
                .filter(item -> item.getCart().getId() == refreshedCart.getId())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        
        refreshedCart.setTotalPrice(totalPrice);
        refreshedCart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(refreshedCart);
    }
}

