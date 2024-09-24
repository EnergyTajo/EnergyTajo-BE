package com.energy.tajo.user.domain;


import static com.energy.tajo.global.encode.PasswordEncoderSHA256.encode;

import com.energy.tajo.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class User extends BaseTimeEntity {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
    private static final String NAME_REGEX = "^[가-힣]{2,10}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "CHAR(64)")
    private String pw;

    @Column(nullable = false, length = 64)
    private String email;

    @Column(nullable = false, columnDefinition = "CHAR(50)")
    private String uuid;

    @Column(nullable = false)
    private String tell;

    @Comment("True-삭제, False-삭제 아님")
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean consentStatus;

    @Column(nullable = false)
    private Boolean locationAgreed;

    @Comment("DB - default 값 0")
    @Column(nullable = false)
    private Integer points;

    @Comment("DB - default 값 0")
    @Column(nullable = false)
    private Float totPowerGen;

    @Comment("카드 정보 = Null 값 (카드 등록 시 입력되기 때문)")
    @Column(nullable = true, length = 16)
    private String cardNum;

    @Column(nullable = true, length = 5)
    private String validThru;

    @Column(nullable = true, length = 3)
    private String cvc;

    protected User(){
    }

    private User(final String name, final String pw, final String email,
                 final String uuid, final String tell, final Boolean locationAgreed){

        this.name = name;
        this.pw = encode(pw);
        this.email = email;
        this.uuid = uuid;
        this.tell = tell;
        this.isDeleted = false;
        this.locationAgreed = locationAgreed;
    }
}
