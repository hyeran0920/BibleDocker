package com.library.bible.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.bible.cart.model.Cart;
import com.library.bible.cart.service.ICartService;
import com.library.bible.member.model.Member;
import com.library.bible.resolver.AuthMember;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;
    
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> getAllCarts(@AuthMember Member member) {
        System.out.println("get lists cart!!");
        Integer memId=member.getMemId();
        // 장바구니 목록 조회
        List<Cart> cartList = cartService.getAllCarts(memId);
        return ResponseEntity.ok(cartList);
    }
    
    
    // 특정 장바구니 조회 (Cart ID)
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable int cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니에 책 추가
    @PostMapping("/add")
    public ResponseEntity<String> addCart(@AuthMember Member member, @RequestBody Map<String, Integer> request) {
        int bookId = request.get("bookId");
        int bookCount = request.get("bookCount");
        Integer memId = member.getMemId(); // 인증된 사용자 ID 가져오기

        // 이미 존재하는 책인지 확인
        int exists = cartService.isBookInCart(memId, bookId);
        if (exists == 1) {
            cartService.updateCartByBookId(bookId, memId, bookCount); // 수량 업데이트
            return ResponseEntity.ok("장바구니 항목이 업데이트되었습니다.");
        } else {
            cartService.addCart(bookId, memId, bookCount); // 새로 추가
            return ResponseEntity.ok("장바구니에 추가되었습니다.");
        }
    }


    // 장바구니 수량 업데이트 (cartId 기준)
    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody Map<String, Integer> request) {
        int cartId = request.get("cartId");
        int newCount = request.get("newCount");

        cartService.updateCart(cartId, newCount);
        return ResponseEntity.ok("장바구니가 업데이트되었습니다.");
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable int cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok("장바구니에서 삭제되었습니다.");
    }

    // 선택한 장바구니 항목의 가격 조회
    @PostMapping("/prices")
    public ResponseEntity<List<Integer>> getSelectedCartPrices(@RequestBody List<Integer> cartIds) {
        List<Integer> prices = cartService.getSelectedCartPrices(cartIds);
        return ResponseEntity.ok(prices);
    }
}
