package spring.study.restaurantapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spring.study.restaurantapi.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class RestaurantServiceTests {

    private RestaurantService restaurantService;
    //실제 테스트 하려는 것이 아니면 가짜 객체를 넣어줌 @Mock 사용
    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp(){
        //Mock Annotation이 붙은 곳에 올바른 객체를 초기화 시켜줌
        MockitoAnnotations.initMocks(this);

        MockRestaurantRepository();

        restaurantService=new RestaurantService(
                restaurantRepository);
    }

    private void MockRestaurantRepository() {
        List<Restaurant> restaurants=new ArrayList<>();
        Restaurant restaurant=Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("Bob zip")
                .address("Seoul")
                .build();
        restaurants.add(restaurant);

        given(restaurantRepository.findAll()).willReturn(restaurants);

        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));
    }

    @Test
    public void getRestaurantWithExisted(){
        Restaurant restaurant=restaurantService.getRestaurant(1004L);

        assertEquals(restaurant.getId(),1004L);

    }

    @Test
    public void getRestaurantWithNotExisted(){
    assertThatThrownBy(() -> {
        restaurantService.getRestaurant(10000L);
    }).isInstanceOf(RestaurantNotFoundException.class);
}

    @Test
    public void getRestaurants(){
        List<Restaurant> restaurants=restaurantService.getRestaurants();

        Restaurant restaurant=restaurants.get(0);

        assertEquals(restaurant.getId(),1004L);
    }

    @Test
    public void addRestaurant(){
        given(restaurantRepository.save(any())).will(invocation -> {
            Restaurant restaurant=invocation.getArgument(0);
            restaurant.setId(123L);
            return restaurant;
        });

        Restaurant restaurant=Restaurant.builder()
                .name("Dong")
                .address("Seoul")
                .build();

        Restaurant newRestaurant=restaurantService.addRestaurant(restaurant);

        assertEquals(newRestaurant.getId(),123L);
    }
    @Test
    public void updateRestaurant(){
        Restaurant restaurant=Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("Seoul")
                .build();

        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));

        restaurantService.updateRestaurant(1004L,"Sea Food","부산");

        assertEquals(restaurant.getName(),"Sea Food");
        assertEquals(restaurant.getAddress(),"부산");
    }
}