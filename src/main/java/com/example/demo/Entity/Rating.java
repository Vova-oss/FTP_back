package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id Рейтинга", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Рейтинг", name = "rate", required = true, example = "Хз, ещё не придумали")
    @Column(name = "rate")
    private String rate;


    @ApiModelProperty(notes = "Пользователь, который ставит оценку", name = "user")
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @ApiModelProperty(notes = "Девайс, которому ставят оценку", name = "device")
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "device_id")
    private Device device;

}
