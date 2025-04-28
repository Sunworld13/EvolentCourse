package com.example.orderservice.service;


import com.example.orderservice.dto.*;
import com.example.orderservice.model.*;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.http.protocol.HTTP;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${user.url}")
    private String userUrl;
    @Value("${product.url}")
    private String productUrl;

    @Transactional
    public ResponseEntity<?> updateById(OrderRequestById orderRequestById, Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = orderRepository.findById(orderId).get();
        //Проверка на то, что заказ отменен и не может быть изменен
        if (order.getStatus().equals(Status.CANCELLED)) {
            return new ResponseEntity<>("The order has been cancelled and cannot be changed, place a new order", HttpStatus.CONFLICT);
        }
        ResponseEntity<?> returnSuccess = returnProduct(order.getProductList());
        //Проверяем успешность возврата товара
        if (returnSuccess.getStatusCode() != HttpStatus.OK) {
            return returnSuccess;
        }
        //удаляем из бд товаров старые записи
        productRepository.deleteAllByOrder(order);
        //Пересоздаем заказ
        return createById(order.getUserId(), orderRequestById, orderId);
    }

    public ResponseEntity<?> returnProduct(List<Product> products) {
        //Возвращаем товары на склад
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<ProductRequest> productRequest = new ArrayList<>();

        //Подготавливаем данные для отправки
        for (Product product : products) {
            ProductRequest temp = new ProductRequest(product);
            productRequest.add(temp);
        }

        HttpEntity<List<ProductRequest>> requestEntity = new HttpEntity<>(productRequest, headers);
        ResponseEntity<String> returnProduct = restTemplate.exchange(
                "http://" + productUrl + "/product/returnProduct",
                HttpMethod.POST,
                requestEntity,
                String.class);
        return returnProduct;
    }

    @Transactional
    public ResponseEntity<?> updateByName(OrderRequestByName orderRequestByName, Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = orderRepository.findById(orderId).get();

        //Проверка на то, что заказ отменен и не может быть изменен
        if (order.getStatus().equals(Status.CANCELLED)) {
            return new ResponseEntity<>("The order has been cancelled and cannot be changed, place a new order", HttpStatus.CONFLICT);
        }
        ResponseEntity<?> returnSuccess = returnProduct(order.getProductList());
        //Проверяем успешность возврата товара
        if (returnSuccess.getStatusCode() != HttpStatus.OK) {
            return returnSuccess;
        }
        //удаляем из бд товаров старые записи
        productRepository.deleteAllByOrder(order);
        //Пересоздаем заказ
        return createByName(order.getUserId(), orderRequestByName, orderId);
    }

    public ResponseEntity<?> updateStatus(Long orderId, Status status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus().equals(Status.CANCELLED)) {
            return new ResponseEntity<>("The order has been cancelled and cannot be changed, place a new order", HttpStatus.CONFLICT);
        }
        //Возвращаем товар в случае, если заказ отменяется
        if (status.equals(Status.CANCELLED)) {
            ResponseEntity<?> returnSuccess = returnProduct(order.getProductList());
            //Проверяем успешность возврата товара
            if (returnSuccess.getStatusCode() != HttpStatus.OK) {
                return returnSuccess;
            }
        }

        order.setStatus(status);
        orderRepository.save(order);

        return new ResponseEntity(order, HttpStatus.OK);
    }


    public ResponseEntity<List<Order>> findAll() {
        return new ResponseEntity(orderRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Order> findById(Long id) {
        if (orderRepository.existsById(id)) {
            return new ResponseEntity(orderRepository.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Order>> findAllByUser(Long userId) {
        return new ResponseEntity(orderRepository.findAllByUserId(userId), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteAllByUserID(Long userId) {
        if (orderRepository.existsByUserId(userId)) {
            List<Order> orders = orderRepository.findAllByUserId(userId);
            //Возвращаем товары на склад, если заказ не был отменен
            for (Order order : orders) {
                if (!order.getStatus().equals(Status.CANCELLED)) {
                    returnProduct(order.getProductList());
                }
            }
            orderRepository.deleteAllByUserId(userId);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<?> deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            Order order = orderRepository.findById(id).get();
            //Возвращаем товары на склад, если заказ не был отменен
            if (!order.getStatus().equals(Status.CANCELLED)) {
                returnProduct(order.getProductList());
            }
            orderRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<?> createById(Long userId, OrderRequestById orderRequestById, Long orderId) {

        //Запрос на проверку существования пользователя
        ResponseEntity<String> isUserExist = restTemplate.getForEntity("http://" + userUrl + "/user/isExist/" + userId, String.class);
        if (isUserExist.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ProductRequestById>> requestEntity = new HttpEntity<>(orderRequestById.getProducts(), headers);

        List<Product> products = restTemplate.exchange(
                        "http://" + productUrl + "/product/getDataToOrderById",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<List<Product>>() {
                        })
                .getBody();
        //Флаг на проверку полноты заказа
        Boolean failFlag = false;
        String fails = "";
        for (Product product : products) {
            //Проверка на то, что товара нет на складе
            if (product.getId() != 0 && product.getQuantity() == 0 && product.getPrice() == 0) {
                fails += "The product - " + product.getId() + " - is not available in the store\n";
                failFlag = true;
            }
            //Проверка на то, что товара недостаточно на складе
            int orderProductQuantity = orderRequestById.findProductById(product.getId()).get().getQuantity();
            if (product.getId() != 0 && product.getPrice() != 0 && product.getQuantity() < orderProductQuantity) {
                fails += "The maximum quantity of the product - " + product.getId() + " -  is equal to " + product.getQuantity() + "\n";
                failFlag = true;
            }
            //Меняем id товара и id записи в бд
            product.setProductId(product.getId());
            product.setId(0);

        }

        //Сохранение заказа в бд при нужном количестве всех товарах
        if (!failFlag) {
            Order order = new Order();
            //установка id заказа, если метод используется в обновлении заказа(PUT)
            if (orderId != null) {
                order.setOrderId(orderId);
            }
            order.setUserId(userId);
            order.setOrderDate(orderRequestById.getOrderDate());
            order.setStatus(Status.NEW);
            order.setProductList(products);

            //Подсчет общей стоимости заказа и количества товаров
            double totalCost = 0;
            long totalQuantity = 0;
            for (Product product : products) {
                //Связывание заказа и списка товаров
                product.setOrder(order);
                totalCost += product.getQuantity() * product.getPrice();
                totalQuantity += product.getQuantity();
            }
            order.setTotalCost(totalCost);
            order.setTotalQuantity(totalQuantity);
            try {
                orderRepository.save(order);
                productRepository.saveAll(products);
                return new ResponseEntity(order, HttpStatus.CREATED);
            } catch (Exception e) {
                returnProduct(products);
                return new ResponseEntity("Save error",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(fails);
    }

    public ResponseEntity<?> createByName(Long userId, OrderRequestByName orderRequestByName, Long orderId) {
        //Запрос на проверку существования пользователя
        ResponseEntity<String> isUserExist = restTemplate.getForEntity("http://" + userUrl + "/user/isExist/" + userId, String.class);
        if (isUserExist.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        HttpStatus status = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ProductRequestByName>> requestEntity = new HttpEntity<>(orderRequestByName.getProducts(), headers);

        List<Product> products = restTemplate.exchange(
                        "http://" + productUrl + "/product/getDataToOrderByName",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<List<Product>>() {
                        })
                .getBody();
        //Флаг на проверку полноты заказа
        Boolean failFlag = false;
        String fails = "";
        for (Product product : products) {
            //Проверка на то, что товара нет на складе
            if (product.getId() == 0 && product.getQuantity() == 0 && product.getPrice() == 0) {
                fails += "The product - " + product.getName() + " - is not available in the store\n";
                failFlag = true;
            }
            //Проверка на то, что товара недостаточно на складе
            int orderProductQuantity = orderRequestByName.findProductByName(product.getName()).get().getQuantity();
            if (!product.getName().equals("") && product.getPrice() != 0 && product.getQuantity() < orderProductQuantity) {
                fails += "The maximum quantity of the product - " + product.getName() + " -  is equal to " + product.getQuantity() + "\n";
                failFlag = true;
            }
            //Меняем id товара и id записи в бд
            product.setProductId(product.getId());
            product.setId(0);

        }
        //Сохранение заказа в бд при нужном количестве всех товарах
        if (!failFlag) {
            Order order = new Order();
            if (orderId != null) {
                order.setOrderId(orderId);
            }
            order.setUserId(userId);
            order.setOrderDate(orderRequestByName.getOrderDate());
            order.setStatus(Status.NEW);
            order.setProductList(products);

            //Подсчет общей стоимости заказа и количества товаров
            double totalCost = 0;
            long totalQuantity = 0;
            for (Product product : products) {
                //Связывание заказа и списка товаров
                product.setOrder(order);
                totalCost += product.getQuantity() * product.getPrice();
                totalQuantity += product.getQuantity();
            }
            order.setTotalCost(totalCost);
            order.setTotalQuantity(totalQuantity);
            try {
                orderRepository.save(order);
                productRepository.saveAll(products);
                return new ResponseEntity(order, HttpStatus.CREATED);
            } catch (Exception e) {
                returnProduct(products);
                return new ResponseEntity("Save error",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(fails);
    }


}