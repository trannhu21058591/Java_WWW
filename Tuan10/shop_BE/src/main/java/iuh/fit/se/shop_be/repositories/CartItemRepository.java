package iuh.fit.se.shop_be.repositories;

import iuh.fit.se.shop_be.entities.Cart;
import iuh.fit.se.shop_be.entities.CartItem;
import iuh.fit.se.shop_be.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

